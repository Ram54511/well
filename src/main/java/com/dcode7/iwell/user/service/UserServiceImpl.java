package com.dcode7.iwell.user.service;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dcode7.iwell.CNF.screenshot.ScreenShotRepository;
import com.dcode7.iwell.CNF.screenshot.service.PaymentScreenShotImageService;
import com.dcode7.iwell.common.pincode.PincodeRepository;
import com.dcode7.iwell.constant.IwellConstant;
import com.dcode7.iwell.user.*;
import com.dcode7.iwell.user.address.UserAddress;
import com.dcode7.iwell.user.address.UserAddressRepository;
import com.dcode7.iwell.user.enums.UserRequestStatus;
import com.dcode7.iwell.user.transaction.enums.PaymentMethod;
import com.dcode7.iwell.user.wallet.Wallet;
import com.dcode7.iwell.user.wallet.WalletRepository;
import com.dcode7.iwell.utils.mail.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dcode7.iwell.common.exception.CustomException;
import com.dcode7.iwell.common.pincode.Pincode;
import com.dcode7.iwell.common.pincode.PincodeService;
import com.dcode7.iwell.common.service.EmailService;
import com.dcode7.iwell.common.service.LocalService;
import com.dcode7.iwell.user.address.UserAddressService;
import com.dcode7.iwell.user.enums.Gender;
import com.dcode7.iwell.user.enums.UserType;
import com.dcode7.iwell.user.password.ChangePasswordDTO;
import com.dcode7.iwell.user.wallet.service.WalletService;
import com.dcode7.iwell.utils.GenericUtils;
import com.dcode7.iwell.utils.PasswordEncoderUtil;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Value("${project.image}")
    private String path;

    @Autowired
    private UserRepository userRepository;

    @Value("${spring.mail.username}")
    private String fromAddress;

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private PasswordEncoderUtil passwordEncoderUtil;

    @Autowired
    private PincodeRepository pincodeRepository;

    @Autowired
    private WalletService walletService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private LocalService localService;

    @Autowired
    private PincodeService pincodeService;

    @Autowired
    private UserAddressService userAddressService;

    @Autowired
    private PaymentScreenShotImageService paymentScreenShotImageService;

    @Autowired
    private ScreenShotRepository screenShotRepository;

    @Autowired
    private MailService mailService;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByUsername(String userName) {
        return userRepository.findByUserName(userName);
    }

    @PostConstruct
    @Override
    public void createAdminUserIfNotExist() {
        logger.info("Checking for existence of admin user...");
        if (userRepository.count() == 0) {
            logger.info("No users present. Creating admin user.");
            User user = new User();
            user.setFullName("Admin");
            user.setGender(Gender.MALE);
            user.setUserName("admin");
            user.setEmail("admin@gmail.com");
            user.setUserType(UserType.ADMIN);
            user.setUserRequestStatus(UserRequestStatus.APPROVED);
            user.setPassword(passwordEncoderUtil.encode("Admin@123"));
            user.setIsActive(true);
            user.setIsEmailVerified(true);
            user.setIsRegistered(true);
            User createdUser = userRepository.save(user);
            walletService.initializeWallets(createdUser);
        } else {
            logger.info("Users already exist. No need to create an admin user.");
        }
    }

    @Override
    @Transactional
    public User registerUser(UserSignUpDTO userSignupForm, BigDecimal totalDeposite, PaymentMethod paymentMethod, MultipartFile multipartFile) throws CustomException {
        logger.info("Registering user: {}", userSignupForm.getEmail());
        sanitizeUserSignUpForm(userSignupForm);
        validateUserSignup(userSignupForm);
        User createdUser;
        if (userSignupForm.getType() == UserType.CNF) {
            createdUser = createUser(userSignupForm);
            mailService.sendAccountCreatedEmail(userSignupForm.getEmail(),userSignupForm.getFullName(), String.valueOf(userSignupForm.getType()));
        } else {
            throw new CustomException(localService.getMessage("should.be.cnf"), HttpStatus.BAD_REQUEST);
        }
        walletService.initializeWalletsForCNF(createdUser, totalDeposite, paymentMethod);
        savePaymentScreenshot(createdUser, multipartFile, path);
        userAddressService.createMailingAddressForUser(createdUser, userSignupForm);
        return createdUser;
    }

    @Override
    public User registerFieldAgent(FieldAgentSignUpDTO fieldAgentSignUpDTO) {
        logger.info("Registering user: {}", fieldAgentSignUpDTO.getEmail());
        sanitizeFieldAgentSignUpForm(fieldAgentSignUpDTO);
        validateFieldAgentSignup(fieldAgentSignUpDTO);
        User createdUser = null;
        if (fieldAgentSignUpDTO.getType() == UserType.FIELDAGENT) {
            createdUser = createFieldAgent(fieldAgentSignUpDTO);
            mailService.sendAccountCreatedEmail(fieldAgentSignUpDTO.getEmail(),fieldAgentSignUpDTO.getFullName(), String.valueOf(fieldAgentSignUpDTO.getType()));
        } else {
            throw new CustomException(localService.getMessage("should.be.fieldagent"), HttpStatus.BAD_REQUEST);
        }
        walletService.initializeWallets(createdUser);
        userAddressService.createMailingAddressForFieldAgent(createdUser, fieldAgentSignUpDTO);
        return createdUser;
    }

    private void sanitizeUserSignUpForm(UserSignUpDTO userSignupForm) {
        userSignupForm.setFullName(userSignupForm.getFullName().strip());
        userSignupForm.setEmail(userSignupForm.getEmail().strip());
        userSignupForm.setMobileNumber(userSignupForm.getMobileNumber().strip());
        userSignupForm.setDistrict(userSignupForm.getDistrict().strip());
        userSignupForm.setMailingAddress(userSignupForm.getMailingAddress().strip());
        userSignupForm.setPincode(userSignupForm.getPincode().strip());
        userSignupForm.setState(userSignupForm.getState().strip());
    }

    //for fieldagent
    private void sanitizeFieldAgentSignUpForm(FieldAgentSignUpDTO fieldAgentSignUpDTO) {
        fieldAgentSignUpDTO.setFullName(fieldAgentSignUpDTO.getFullName().strip());
        fieldAgentSignUpDTO.setEmail(fieldAgentSignUpDTO.getEmail().strip());
        fieldAgentSignUpDTO.setMobileNumber(fieldAgentSignUpDTO.getMobileNumber().strip());
        fieldAgentSignUpDTO.setDistrict(fieldAgentSignUpDTO.getDistrict().strip());
        fieldAgentSignUpDTO.setMailingAddress(fieldAgentSignUpDTO.getMailingAddress().strip());
        fieldAgentSignUpDTO.setPincode(fieldAgentSignUpDTO.getPincode().strip());
        fieldAgentSignUpDTO.setState(fieldAgentSignUpDTO.getState().strip());
    }


    private User createUser(UserSignUpDTO userSignupForm) {
        User user = new User();
        user.setFullName(userSignupForm.getFullName());
        user.setEmail(userSignupForm.getEmail());
        user.setPassword(passwordEncoderUtil.encode(userSignupForm.getPassword()));
        user.setDob(userSignupForm.getDateOfBirth());
        user.setUserName(userSignupForm.getEmail());
        user.setUserType(userSignupForm.getType());
        user.setUserRequestStatus(UserRequestStatus.PENDING);
        user.setDoj(new Date());
        user.setIsActive(true);
        user.setIsRegistered(true);
        user.setIsEmailVerified(true);
        user.setGender(userSignupForm.getGender());
        user.setMobileNumber(userSignupForm.getMobileNumber());
        return userRepository.save(user);
    }

    private void validateUserSignup(UserSignUpDTO userSignupForm) throws CustomException {
        validateEmail(userSignupForm.getEmail());
        validateAndSanitizeMobileNumber(userSignupForm);
        validateAddress(userSignupForm);
    }

    private void validateFieldAgentSignup(FieldAgentSignUpDTO fieldAgentSignUpDTO) throws CustomException {
        validateEmail(fieldAgentSignUpDTO.getEmail());
        validateAndSanitizeMobileNumber(fieldAgentSignUpDTO);
        validateFieldAgentAddress(fieldAgentSignUpDTO);
    }

    private void validateAddress(UserSignUpDTO userSignupForm) {
        Optional<Pincode> optionalPincode = pincodeService.findFirstByPincode(userSignupForm.getPincode());

        optionalPincode.ifPresentOrElse(pincode -> {
            if (!userSignupForm.getState().equals(pincode.getState())) {
                throw new CustomException(localService.getMessage("user.registration.state.invalid"),
                        HttpStatus.BAD_REQUEST);
            }

            if (!userSignupForm.getDistrict().equals(pincode.getDistrict())) {
                throw new CustomException(localService.getMessage("user.registration.district.invalid"),
                        HttpStatus.BAD_REQUEST);
            }

        }, () -> {
            throw new CustomException(localService.getMessage("user.registration.pincode.invalid"),
                    HttpStatus.BAD_REQUEST);
        });

    }

    private void validateFieldAgentAddress(FieldAgentSignUpDTO fieldAgentSignUpDTO) {
        Optional<Pincode> optionalPincode = pincodeService.findFirstByPincode(fieldAgentSignUpDTO.getPincode());

        optionalPincode.ifPresentOrElse(pincode -> {
            if (!fieldAgentSignUpDTO.getState().equals(pincode.getState())) {
                throw new CustomException(localService.getMessage("user.registration.state.invalid"),
                        HttpStatus.BAD_REQUEST);
            }

            if (!fieldAgentSignUpDTO.getDistrict().equals(pincode.getDistrict())) {
                throw new CustomException(localService.getMessage("user.registration.district.invalid"),
                        HttpStatus.BAD_REQUEST);
            }

        }, () -> {
            throw new CustomException(localService.getMessage("user.registration.pincode.invalid"),
                    HttpStatus.BAD_REQUEST);
        });

    }

    private void validateAndSanitizeMobileNumber(UserSignUpDTO userSignupForm) {

        String mobileNumber = userSignupForm.getMobileNumber();

        // get and store only the last 10 digits of mobile number
        Matcher matcher = Pattern.compile(IwellConstant.MOBILE_NUMBER_CAPTURE_REGEX).matcher(mobileNumber);
        if (matcher.find()) {

            mobileNumber = matcher.group(1);

            Optional<User> user = userRepository.findByMobileNumber(mobileNumber);
            if (user.isEmpty()) {
                userSignupForm.setMobileNumber(mobileNumber);
            } else {
                throw new CustomException(localService.getMessage("user.registration.mobile.number.already.registered"),
                        HttpStatus.BAD_REQUEST);
            }
        } else {
            throw new CustomException(localService.getMessage("user.registration.mobile.number.invalid"),
                    HttpStatus.BAD_REQUEST);
        }
    }


    private void validateAndSanitizeMobileNumber(FieldAgentSignUpDTO fieldAgentSignUpDTO) {

        String mobileNumber = fieldAgentSignUpDTO.getMobileNumber();

        // get and store only the last 10 digits of mobile number
        Matcher matcher = Pattern.compile(IwellConstant.MOBILE_NUMBER_CAPTURE_REGEX).matcher(mobileNumber);
        if (matcher.find()) {

            mobileNumber = matcher.group(1);

            Optional<User> user = userRepository.findByMobileNumber(mobileNumber);
            if (user.isEmpty()) {
                fieldAgentSignUpDTO.setMobileNumber(mobileNumber);
            } else {
                throw new CustomException(localService.getMessage("user.registration.mobile.number.already.registered"),
                        HttpStatus.BAD_REQUEST);
            }
        } else {
            throw new CustomException(localService.getMessage("user.registration.mobile.number.invalid"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    private void validateEmail(String email) {
        logger.info("Validating email: {}", email);
        Optional<User> optionalExistingUser = userRepository.findByEmail(email);
        if (optionalExistingUser.isPresent() && optionalExistingUser.get().getIsRegistered()) {
            throw new CustomException(localService.getMessage("user.already.registered"), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUserName(username);
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        return optionalUser.get();
    }

    @Override
    public User changePassword(ChangePasswordDTO changePasswordDTO) throws CustomException {
        validatePassword(changePasswordDTO);
        User user = GenericUtils.getLoggedInUser();
        user.setPassword(passwordEncoderUtil.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(user);
        return user;
    }

    private void validatePassword(ChangePasswordDTO changePasswordDTO) {
        User user = GenericUtils.getLoggedInUser();

        if (!passwordEncoderUtil.matches(changePasswordDTO.getCurrentPassword(), user.getPassword())) {
            throw new CustomException(localService.getMessage("user.password.change.invalid"), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void createAndSendToken(String email) {
        validateEmail(email);
        emailService.generateAndSaveVerificationTokenForSignUp(email);
    }


    @Override
    public UserSignupResponseDTO getRegistrationSuccessDetails(User user, String password) {
        return new UserSignupResponseDTO(user.getFullName(), user.getUserName(), password, user.getEmail());
    }

    @Override
    public void sendRegistrationToken(String email) {
        email = email.strip();
        emailService.generateAndSaveVerificationTokenForSignUp(email);
    }

    @Override
    public RegistrationOptionsResponseDTO getRegistrationOptions() {
        ArrayList<UserType> userTypes = new ArrayList<>();
        userTypes.addAll(Arrays.asList(UserType.CNF, UserType.FIELDAGENT));
        return new RegistrationOptionsResponseDTO(userTypes);
    }

    @Override
    public User ResponseForCreatingAccount(UUID userId, UserRequestStatus userRequestStatus) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(localService.getMessage("user.not.found"), HttpStatus.BAD_REQUEST));
        Wallet walletObj = walletRepository.findByUserId(user.getId());
        if (user.getUserRequestStatus() == UserRequestStatus.APPROVED) {
            throw new CustomException(localService.getMessage("request.already.approved"), HttpStatus.BAD_REQUEST);
        } else {
            user.setUserRequestStatus(userRequestStatus);
            BigDecimal existingBalance = walletObj.getBalance();
            BigDecimal additionalAmount = existingBalance.multiply(new BigDecimal("0.05"));
            BigDecimal newBalance = existingBalance.add(additionalAmount);
            walletObj.setBalance(newBalance);
            userRepository.save(user);
            walletRepository.save(walletObj);
        }
        return user;
    }

    /**
     * Registers a new Field Agent user in the system.
     * If a referenced ID is provided, validates it and sets the referenced user.
     *
     * @param fieldAgentSignUpDTO The DTO containing details of the Field Agent user to register.
     * @return The registered Field Agent user.
     * @throws CustomException          If validation fails or referenced user is not found.
     * @throws IllegalArgumentException If referenced ID is null.
     */
    private User createFieldAgent(FieldAgentSignUpDTO fieldAgentSignUpDTO) {
        User user = new User();
        UserAddress userAddress = userAddressRepository.findByDistrictAndUserType(fieldAgentSignUpDTO.getDistrict(), UserType.CNF);
        User userObj= userAddress.getUser();
        if (userObj.getUserRequestStatus() == UserRequestStatus.APPROVED) {
            user.setReferencedUser(userAddress.getUser());
        } else{
            user.setReferencedUser(null);
        }
        user.setFullName(fieldAgentSignUpDTO.getFullName());
        user.setEmail(fieldAgentSignUpDTO.getEmail());
        user.setPassword(passwordEncoderUtil.encode(fieldAgentSignUpDTO.getPassword()));
        user.setDob(fieldAgentSignUpDTO.getDateOfBirth());
        user.setUserName(fieldAgentSignUpDTO.getEmail());
        user.setUserRequestStatus(UserRequestStatus.PENDING);
        user.setUserType(UserType.FIELDAGENT);
        user.setDoj(new Date());
        user.setIsActive(true);
        user.setIsRegistered(true);
        user.setIsEmailVerified(true);
        user.setGender(fieldAgentSignUpDTO.getGender());
        user.setMobileNumber(fieldAgentSignUpDTO.getMobileNumber());
        return userRepository.save(user);

    }

    // Method to save the payment screenshot and return the file URL
    private String savePaymentScreenshot(User createdUser, MultipartFile multipartFile, String uploadPath) {
        return paymentScreenShotImageService.saveImageToStorage(createdUser, uploadPath, multipartFile);
    }


}
