package com.dcode7.iwell.common.pincode;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;


public interface PincodeRepository extends JpaRepository<Pincode, UUID> {

	Iterable<Pincode> findByPincodeStartsWith(String pincode);

	Optional<Pincode> findFirstByPincode(String pincode);

	Pincode  findByDistrict(String district);

}
