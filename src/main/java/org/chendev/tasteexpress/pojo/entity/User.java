package org.chendev.tasteexpress.pojo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.chendev.tasteexpress.common.enumeration.EnabledStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "`user`") // user 是关键字，习惯性加反引号
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // unique email as login identifier
    @Column(name = "email", nullable = false, length = 128, unique = true)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    // BCrypt hash, never store raw passwords
    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "name", length = 32)
    private String name;

    @Column(name = "avatar", length = 500)
    private String avatar;

    // reserved for external identity providers like Google / Apple
    @Column(name = "external_id", length = 64, unique = true)
    private String externalId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private EnabledStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "last_login_ip", length = 64)
    private String lastLoginIp;
}