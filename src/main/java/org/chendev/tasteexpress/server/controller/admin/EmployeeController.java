package org.chendev.tasteexpress.server.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chendev.tasteexpress.common.enumeration.EnabledStatus;
import org.chendev.tasteexpress.common.result.PageResult;
import org.chendev.tasteexpress.common.result.Result;
import org.chendev.tasteexpress.pojo.dto.EmployeeDTO;
import org.chendev.tasteexpress.pojo.dto.EmployeeLoginDTO;
import org.chendev.tasteexpress.pojo.dto.EmployeePageQueryDTO;
import org.chendev.tasteexpress.pojo.dto.PasswordEditDTO;
import org.chendev.tasteexpress.pojo.entity.Employee;
import org.chendev.tasteexpress.pojo.vo.EmployeeLoginVO;
import org.chendev.tasteexpress.server.service.EmployeeService;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody @Valid EmployeeLoginDTO dto) {
        log.info("Employee login: {}", dto.getUsername());
        return Result.success(employeeService.login(dto));
    }

    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success("ok");
    }

    @PostMapping
    public Result<Void> save(@RequestBody @Valid EmployeeDTO dto) {
        log.info("New employee: {}", dto.getUsername());
        employeeService.save(dto);
        return Result.success();
    }

    @GetMapping("/page")
    public Result<PageResult> page(EmployeePageQueryDTO dto) {
        log.info("Employee page query: {}", dto);
        return Result.success(employeeService.pageQuery(dto));
    }

    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable Long id) {
        log.info("Get employee by id: {}", id);
        return Result.success(employeeService.getById(id));
    }

    @PutMapping
    public Result<Void> update(@RequestBody @Valid EmployeeDTO dto) {
        log.info("Update employee: {}", dto.getUsername());
        employeeService.updateEmp(dto);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteById(@PathVariable Long id) {
        log.info("Delete employee id={}", id);
        employeeService.deleteEmp(id);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    public Result<Void> startOrStop(@PathVariable EnabledStatus status, Long id) {
        log.info("Enable/disable employee, status={}, id={}", status, id);
        employeeService.startOrStop(status, id);
        return Result.success();
    }

    @PutMapping("/editPassword")
    public Result<String> editPassword(@RequestBody @Valid PasswordEditDTO dto) {
        log.info("Edit employee password");
        employeeService.editPassword(dto);
        return Result.success();
    }
}
