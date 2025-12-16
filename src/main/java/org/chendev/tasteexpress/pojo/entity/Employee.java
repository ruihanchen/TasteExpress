package org.chendev.tasteexpress.pojo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.chendev.tasteexpress.common.enumeration.EnabledStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employee")
@Builder
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // login username (unique)
    @Column(name = "username", nullable = false, length = 32, unique = true)
    private String username;

    // display name
    @Column(name = "name", nullable = false, length = 32)
    private String name;

    @Column(name = "phone", length = 20)
    private String phone;

    // gender code, e.g. "0"/"1" or "M"/"F"
    @Column(name = "sex", length = 2)
    private String sex;

    @Column(name = "id_number", length = 20)
    private String idNumber;

    // password hash (or plain for dev seed)
    @Column(name = "password", nullable = false, length = 64)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private EnabledStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

}
