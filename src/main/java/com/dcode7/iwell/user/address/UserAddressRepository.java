package com.dcode7.iwell.user.address;

import java.util.UUID;

import com.dcode7.iwell.user.User;
import com.dcode7.iwell.user.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAddressRepository extends JpaRepository<UserAddress, UUID> {

    UserAddress findByDistrictAndUserType(String district, UserType userType);

    UserAddress findByDistrict(String district);

}
