package com.dcode7.iwell.user.password;

import java.io.Serializable;

import jakarta.validation.constraints.AssertTrue;

public class ChangePasswordDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	// @PasswordValidator(min = 8, max = 64, messageLength = "{password.length.error}", messageNotEmpty = "{enter.password}", messageMustContainOneDigit = "{password.validator.digit}", messageMustContainOneUpperCaseAlphabet = "{password.validator.uppderCase}", messageMustContainOneLowerCaseAlphabet = "{password.validator.lowerCase}", messageMustContainNoWhiteSpace = "{password.validator.nowhitespace}", messageMustContainOneSpecialCharacter = "{password.validator.specialChar}")
	private String currentPassword;

	// @PasswordValidator(min = 8, max = 64, messageLength = "{password.length.error}", messageNotEmpty = "{enter.password}", messageMustContainOneDigit = "{password.validator.digit}", messageMustContainOneUpperCaseAlphabet = "{password.validator.uppderCase}", messageMustContainOneLowerCaseAlphabet = "{password.validator.lowerCase}", messageMustContainNoWhiteSpace = "{password.validator.nowhitespace}", messageMustContainOneSpecialCharacter = "{password.validator.specialChar}")
	private String newPassword;

	//@PasswordValidator(min = 8, max = 64, messageLength = "{password.length.error}", messageNotEmpty = "{enter.password}", messageMustContainOneDigit = "{password.validator.digit}", messageMustContainOneUpperCaseAlphabet = "{password.validator.uppderCase}", messageMustContainOneLowerCaseAlphabet = "{password.validator.lowerCase}", messageMustContainNoWhiteSpace = "{password.validator.nowhitespace}", messageMustContainOneSpecialCharacter = "{password.validator.specialChar}")
	private String newConfirmPassword;

	public String getCurrentPassword() {
		return currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public String getNewConfirmPassword() {
		return newConfirmPassword;
	}

	@AssertTrue(message = "new password and confirm password did not match")
	private boolean isValid() {
		return this.newPassword.equals(this.newConfirmPassword);
	}

	@AssertTrue(message = "new password and old password cannot be the same")
	private boolean isDifferentPassword() {
		return !this.newPassword.equals(this.currentPassword);
	}
}