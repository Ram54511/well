package com.dcode7.iwell.CNF.screenshot;

import com.dcode7.iwell.user.User;
import com.dcode7.iwell.utils.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentScreenShotImage extends BaseEntity {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private String url;

    @ManyToOne
    private User user;
}
