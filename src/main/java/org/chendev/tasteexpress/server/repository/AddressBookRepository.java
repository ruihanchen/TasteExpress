package org.chendev.tasteexpress.server.repository;

import org.chendev.tasteexpress.pojo.entity.AddressBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AddressBookRepository extends JpaRepository<AddressBook, Long> {

    List<AddressBook> findByUserIdOrderByIsDefaultDescIdDesc(Long userId);

    // 单条：查不到返回 null（Spring Data 支持返回实体或 null）
    Optional<AddressBook> findFirstByUserIdAndIsDefaultTrue(Long userId);

    @Modifying
    @Query("update AddressBook a set a.isDefault = false where a.userId = :userId and a.isDefault = true")
    int clearDefault(@Param("userId") Long userId);

    @Modifying
    @Query("update AddressBook a set a.isDefault = true where a.id = :id and a.userId = :userId")
    int setDefault(@Param("userId") Long userId, @Param("id") Long id);

    long deleteByIdAndUserId(Long id, Long userId);
}
