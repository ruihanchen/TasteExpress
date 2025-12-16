package org.chendev.tasteexpress.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.chendev.tasteexpress.pojo.entity.AddressBook;
import org.chendev.tasteexpress.server.repository.AddressBookRepository;
import org.chendev.tasteexpress.server.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AddressBookServiceImpl implements AddressBookService {


    private final AddressBookRepository addressBookRepository;


    @Override
    @Transactional
    public void save(AddressBook entity, Long userId) {
        entity.setId(null);
        entity.setUserId(userId);
        if (Boolean.TRUE.equals(entity.getIsDefault())) {
            addressBookRepository.clearDefault(userId);
            entity.setIsDefault(true);
        }
        addressBookRepository.save(entity);
    }

    @Override
    @Transactional
    public void update(AddressBook entity, Long userId) {
        AddressBook db = addressBookRepository.findById(entity.getId())
                .orElseThrow(() -> new IllegalArgumentException("Address not found"));
        if (!Objects.equals(db.getUserId(), userId)) {
            throw new IllegalArgumentException("Address not owned by current user");
        }

        db.setConsignee(entity.getConsignee());
        db.setSex(entity.getSex());
        db.setPhone(entity.getPhone());
        db.setStreetInfo(entity.getStreetInfo());
        db.setCityName(entity.getCityName());
        db.setZipcode(entity.getZipcode());
        db.setStateName(entity.getStateName());
        db.setDetail(entity.getDetail());
        db.setLabel(entity.getLabel());

        if (Boolean.TRUE.equals(entity.getIsDefault())) {
            addressBookRepository.clearDefault(userId);
            db.setIsDefault(true);
        }
        addressBookRepository.save(db);
    }

    @Override
    @Transactional
    public void delete(Long id, Long userId) {
        long affected = addressBookRepository.deleteByIdAndUserId(id, userId);
        if (affected == 0) {
            throw new IllegalArgumentException("Address not found or not owned by user");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressBook> list(Long userId) {
        return addressBookRepository.findByUserIdOrderByIsDefaultDescIdDesc(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public AddressBook getDefault(Long userId) {
        return addressBookRepository.findFirstByUserIdAndIsDefaultTrue(userId).orElse(null);
    }

    @Override
    @Transactional
    public void setDefault(Long id, Long userId) {
        addressBookRepository.clearDefault(userId);
        int n = addressBookRepository.setDefault(userId, id);
        if (n == 0) {
            throw new IllegalArgumentException("Address not found for this user");
        }
    }
}
