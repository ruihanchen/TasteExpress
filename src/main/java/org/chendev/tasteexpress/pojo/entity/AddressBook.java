package org.chendev.tasteexpress.pojo.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "address_book", indexes = {
        @Index(name = "idx_address_user", columnList = "user_id")
})
public class AddressBook {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    private String consignee;
    private String sex;
    private String phone;

    @Column(name = "street_info")
    private String streetInfo;

    @Column(name = "city_name")
    private String cityName;

    private String zipcode;

    @Column(name = "state_name")
    private String stateName;

    private String detail;

    private String label;

    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false; // 用 Boolean 对应 tinyint(1)
}