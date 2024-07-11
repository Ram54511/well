package com.dcode7.iwell.user;

import java.util.Date;

import com.dcode7.iwell.common.annotation.AgeLimit;
import com.dcode7.iwell.common.annotation.MobileNumber;
import com.dcode7.iwell.common.annotation.Password;
import com.dcode7.iwell.user.enums.Gender;
import com.dcode7.iwell.user.enums.UserType;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSignUpDTO {

	@NotNull(message = "user registration user type required")
	private UserType type;

	@NotBlank(message = "user registration fullname required}")
	private String fullName;

	@NotNull(message = "user registration gender required}")
	private Gender gender;

	@MobileNumber(message = "user registration mobile number invalid}")
	@NotBlank(message = "user registration mobile number required}")
	private String mobileNumber;

	@Email(message = "user registration email invalid}")
	@NotBlank(message = "user registration email required}")
	private String email;

	@AgeLimit(value = 7, message = "{user.registration.age.invalid}")
	@NotNull(message = "user registration dob required.")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateOfBirth;

	@NotBlank(message = "user registration pincode required}")
	private String pincode;

	@NotBlank(message = "user registration state required")
	private String state;

	@NotBlank(message = "user registration district required")
	private String district;

	@NotBlank(message = "user registration mailiing address required}")
	private String mailingAddress;

	@Password(min = 8, max = 64, messageLength = "{password.length.error}", messageMustContainOneDigit = "{password.validator.digit}", messageMustContainOneUpperCaseAlphabet = "{password.validator.upperCase}", messageMustContainOneLowerCaseAlphabet = "{password.validator.lowerCase}", messageMustContainNoWhiteSpace = "{password.validator.nowhitespace}", messageMustContainOneSpecialCharacter = "{password.validator.specialChar}")
	@NotNull(message = "user registration password required")
	private String password;

	@NotBlank(message = "user registration confirm  password required}")
	private String confirmPassword;

	@NotNull(message = "payment screenshot required}")
	private MultipartFile file;

	@AssertTrue(message = "user registration password not match")
	private boolean isMatchingPasswords() {
		return this.password == null || this.confirmPassword == null || this.password.equals(this.confirmPassword);
	}

}