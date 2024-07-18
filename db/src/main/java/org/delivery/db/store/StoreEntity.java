package org.delivery.db.store;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.delivery.db.BaseEntity;
import org.delivery.db.store.enums.StoreCategory;
import org.delivery.db.store.enums.StoreStatus;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "store")
@EqualsAndHashCode(callSuper = true)
public class StoreEntity extends BaseEntity {

    @Column(length = 45, nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String address;

    @Column(length = 45, nullable = false)
    @Enumerated(EnumType.STRING)
    private StoreStatus status;

    @Column(length = 45, nullable = false)
    @Enumerated(EnumType.STRING)
    private StoreCategory category;

    private double star;

    @Column(length = 200, nullable = false)
    private String thumbnailUrl;

    @Column(precision = 11, scale = 4, nullable = false)
    private BigDecimal minimumAmount;

    @Column(precision = 11, scale = 4, nullable = false)
    private BigDecimal minimumDeliveryAmount;

    @Column(length = 20)
    private String phoneNumber;
}
