package com.dcode7.iwell.user.address;

import com.dcode7.iwell.user.FieldAgentSignUpDTO;
import com.dcode7.iwell.user.User;
import com.dcode7.iwell.user.UserSignUpDTO;

public interface UserAddressService {

	UserAddress createMailingAddressForUser(User user, UserSignUpDTO signUpDTO);

	UserAddress createMailingAddressForFieldAgent(User user, FieldAgentSignUpDTO fieldAgentSignUpDTO);

}
