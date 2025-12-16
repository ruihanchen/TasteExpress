package org.chendev.tasteexpress.server.service;

import org.chendev.tasteexpress.pojo.entity.AddressBook;

import java.util.List;

public interface AddressBookService {
    void save(AddressBook entity, Long userId);

    void update(AddressBook entity, Long userId);

    void delete(Long id, Long userId);

    List<AddressBook> list(Long userId);

    AddressBook getDefault(Long userId);

    void setDefault(Long id, Long userId);
}