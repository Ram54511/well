package com.dcode7.iwell.common.pincode;

import java.util.Optional;

public interface PincodeService {
	
	Iterable<Pincode> getDetailsByPincode(String pincode);

	Optional<Pincode> findFirstByPincode(String pincode);

}
