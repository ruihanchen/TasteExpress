package org.chendev.tasteexpress.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.chendev.tasteexpress.common.context.BaseContext;
import org.chendev.tasteexpress.common.enumeration.EnabledStatus;
import org.chendev.tasteexpress.common.error.ErrorCode;
import org.chendev.tasteexpress.common.exception.AuthException;
import org.chendev.tasteexpress.common.result.PageResult;
import org.chendev.tasteexpress.pojo.dto.*;
import org.chendev.tasteexpress.pojo.entity.Employee;
import org.chendev.tasteexpress.pojo.vo.EmployeeLoginVO;
import org.chendev.tasteexpress.server.repository.EmployeeRepository;
import org.chendev.tasteexpress.server.service.EmployeeService;
import org.chendev.tasteexpress.server.service.TokenService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Override
    public EmployeeLoginVO login(EmployeeLoginDTO dto) {
        String username = dto.getUsername();
        String rawPassword = dto.getPassword();

        Employee employee = employeeRepository.findByUsername(username)
                .orElseThrow(() -> new AuthException(ErrorCode.AUTH_ACCOUNT_NOT_FOUND));

        if (!passwordEncoder.matches(rawPassword, employee.getPassword())) {
            throw new AuthException(ErrorCode.AUTH_PASSWORD_ERROR);
        }

        if (EnabledStatus.DISABLED.equals(employee.getStatus())) {
            throw new AuthException(ErrorCode.AUTH_ACCOUNT_LOCKED);
        }

        // Issue admin token via TokenService to keep JWT details in one place
        String token = tokenService.createAdminToken(employee.getId());

        return EmployeeLoginVO.builder()
                .id(employee.getId())
                .username(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();
    }

    @Override
    @Transactional
    public void save(EmployeeDTO dto) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(dto, employee);

        String rawPassword = (dto.getPassword() == null || dto.getPassword().isBlank())
                ? "123456"
                : dto.getPassword();

        employee.setPassword(passwordEncoder.encode(rawPassword));
        employeeRepository.save(employee);
    }

    @Override
    public PageResult pageQuery(EmployeePageQueryDTO dto) {
        int pageIndex = dto.getPage() <= 0 ? 0 : dto.getPage() - 1;
        int pageSize = dto.getPageSize() <= 0 ? 10 : dto.getPageSize();

        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Employee> page;
        if (StringUtils.hasText(dto.getName())) {
            page = employeeRepository.findByNameContainingIgnoreCase(dto.getName().trim(), pageable);
        } else {
            page = employeeRepository.findAll(pageable);
        }

        return new PageResult(page.getTotalElements(), page.getContent());
    }

    @Override
    public Employee getById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void updateEmp(EmployeeDTO dto) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException("Employee id must not be null when updating");
        }

        Employee existing = employeeRepository.findById(dto.getId())
                .orElseThrow(() -> new AuthException(ErrorCode.AUTH_ACCOUNT_NOT_FOUND));

        existing.setUsername(dto.getUsername());
        existing.setName(dto.getName());
        existing.setPhone(dto.getPhone());
        existing.setSex(dto.getSex());
        existing.setIdNumber(dto.getIdNumber());
        existing.setUpdatedAt(LocalDateTime.now());
        existing.setUpdatedBy(BaseContext.getCurrentId());

        employeeRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteEmp(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new AuthException(ErrorCode.AUTH_ACCOUNT_NOT_FOUND));
        employeeRepository.delete(employee);
    }

    @Override
    @Transactional
    public void startOrStop(EnabledStatus status, Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new AuthException(ErrorCode.AUTH_ACCOUNT_NOT_FOUND));

        employee.setStatus(status);
        employee.setUpdatedAt(LocalDateTime.now());
        employee.setUpdatedBy(BaseContext.getCurrentId());

        employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public void editPassword(PasswordEditDTO dto) {
        Long currentId = BaseContext.getCurrentId();
        Employee employee = employeeRepository.findById(currentId)
                .orElseThrow(() -> new AuthException(ErrorCode.AUTH_ACCOUNT_NOT_FOUND));

        if (!passwordEncoder.matches(dto.getOldPassword(), employee.getPassword())) {
            throw new AuthException(ErrorCode.AUTH_PASSWORD_ERROR);
        }

        String encodedNew = passwordEncoder.encode(dto.getNewPassword());
        employee.setPassword(encodedNew);
        employee.setUpdatedAt(LocalDateTime.now());
        employee.setUpdatedBy(currentId);

        employeeRepository.save(employee);
    }
}