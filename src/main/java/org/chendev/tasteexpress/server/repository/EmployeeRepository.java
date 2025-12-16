package org.chendev.tasteexpress.server.repository;

import org.chendev.tasteexpress.pojo.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByUsername(String username);

    boolean existsByUsername(String username);

    Page<Employee> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
