package com.dcode7.iwell.common.pincode;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PincodeServiceImpl implements PincodeService {
	
	@Autowired
	private PincodeRepository pincodeRepository;

	@Override
	public Iterable<Pincode> getDetailsByPincode(String pincode) {
		if(pincode.length() >= 3) {
			return pincodeRepository.findByPincodeStartsWith(pincode);
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public Optional<Pincode> findFirstByPincode(String pincode) {
		return pincodeRepository.findFirstByPincode(pincode);
	}
	
}
