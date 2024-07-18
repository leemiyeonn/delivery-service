package org.delivery.db.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.delivery.db.BaseEntity;
import org.delivery.db.user.enums.UserStatus;

@Data
@Entity
@SuperBuilder
@Table(name = "user")
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
public class UserEntity extends BaseEntity {

    @Column(length = 45, nullable = false)
    private String name;

    @Column(length = 45, nullable = false)
    private String email;

    @Column(length = 45, nullable = false)
    private String password;

    @Column(length = 45, nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(length = 100, nullable = false)
    private String address;

    private LocalDateTime registeredAt;

    private LocalDateTime unregisteredAt;

    private LocalDateTime lastLoginAt;

}
