package com.dcode7.iwell.CNF.screenshot;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ScreenShotRepository extends JpaRepository<PaymentScreenShotImage, UUID> {
}
