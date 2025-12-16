package org.chendev.tasteexpress.server.service;

import org.chendev.tasteexpress.common.enumeration.EnabledStatus;
import org.chendev.tasteexpress.common.result.PageResult;
import org.chendev.tasteexpress.pojo.dto.EmployeeDTO;
import org.chendev.tasteexpress.pojo.dto.EmployeeLoginDTO;
import org.chendev.tasteexpress.pojo.dto.EmployeePageQueryDTO;
import org.chendev.tasteexpress.pojo.dto.PasswordEditDTO;
import org.chendev.tasteexpress.pojo.entity.Employee;
import org.chendev.tasteexpress.pojo.vo.EmployeeLoginVO;

public interface EmployeeService {

    EmployeeLoginVO login(EmployeeLoginDTO dto);

    void save(EmployeeDTO dto);

    PageResult pageQuery(EmployeePageQueryDTO dto);

    Employee getById(Long id);

    void updateEmp(EmployeeDTO dto);

    void deleteEmp(Long id);

    void startOrStop(EnabledStatus status, Long id);

    void editPassword(PasswordEditDTO dto);
}
