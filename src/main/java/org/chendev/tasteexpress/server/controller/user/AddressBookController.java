package org.chendev.tasteexpress.server.controller.user;

import lombok.RequiredArgsConstructor;
import org.chendev.tasteexpress.common.context.BaseContext;
import org.chendev.tasteexpress.common.result.Result;
import org.chendev.tasteexpress.pojo.entity.AddressBook;
import org.chendev.tasteexpress.server.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
@RequiredArgsConstructor
public class AddressBookController {

    private final AddressBookService addressBookService;

    @PostMapping
    public Result<Void> save(@RequestBody AddressBook entity) {
        Long userId = BaseContext.getCurrentId();
        addressBookService.save(entity, userId);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody AddressBook entity) {
        Long userId = BaseContext.getCurrentId();
        addressBookService.update(entity, userId);
        return Result.success();
    }

    @DeleteMapping
    public Result<Void> delete(@RequestParam Long id) {
        Long userId = BaseContext.getCurrentId();
        addressBookService.delete(id, userId);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<AddressBook>> list() {
        Long userId = BaseContext.getCurrentId();
        return Result.success(addressBookService.list(userId));
    }

    @GetMapping("/default")
    public Result<AddressBook> getDefault() {
        Long userId = BaseContext.getCurrentId();
        return Result.success(addressBookService.getDefault(userId));
    }

    @PutMapping("/default")
    public Result<Void> setDefault(@RequestParam Long id) {
        Long userId = BaseContext.getCurrentId();
        addressBookService.setDefault(id, userId);
        return Result.success();
    }
}