package com.dcode7.iwell.user.address;

import com.dcode7.iwell.common.exception.CustomException;
import com.dcode7.iwell.common.service.LocalService;
import com.dcode7.iwell.user.FieldAgentSignUpDTO;
import com.dcode7.iwell.user.UserRepository;
import com.dcode7.iwell.user.enums.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.dcode7.iwell.user.User;
import com.dcode7.iwell.user.UserSignUpDTO;

@Service
public class UserAddressServiceImpl implements UserAddressService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserAddressRepository userAddressRepository;

	@Autowired
	private LocalService localService;

	@Override
	public UserAddress createMailingAddressForUser(User user, UserSignUpDTO signUpDTO) {
		UserAddress userAddressObj = userAddressRepository.findByDistrictAndUserType(signUpDTO.getDistrict(), signUpDTO.getType());
		if (userAddressObj != null) {
			if (userAddressObj.getUserType() == UserType.CNF) {
				throw new CustomException(localService.getMessage("CNF.register.district"), HttpStatus.BAD_REQUEST);
			}
		}
		UserAddress userAddress = new UserAddress();
		userAddress.setUser(user);
		userAddress.setPincode(signUpDTO.getPincode());
		userAddress.setUserType(signUpDTO.getType());
		userAddress.setState(signUpDTO.getState());
		userAddress.setDistrict(signUpDTO.getDistrict());
		userAddress.setMailingAddress(signUpDTO.getMailingAddress());
		return userAddressRepository.save(userAddress);
	}

	@Override
	public UserAddress createMailingAddressForFieldAgent(User user, FieldAgentSignUpDTO fieldAgentSignUpDTO) {
		UserAddress userAddress = new UserAddress();
		userAddress.setUser(user);
		userAddress.setUserType(fieldAgentSignUpDTO.getType());
		userAddress.setPincode(fieldAgentSignUpDTO.getPincode());
		userAddress.setState(fieldAgentSignUpDTO.getState());
		userAddress.setDistrict(fieldAgentSignUpDTO.getDistrict());
		userAddress.setMailingAddress(fieldAgentSignUpDTO.getMailingAddress());
		return userAddressRepository.save(userAddress);
	}

}
