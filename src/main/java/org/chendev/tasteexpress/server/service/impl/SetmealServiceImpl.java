package org.chendev.tasteexpress.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.chendev.tasteexpress.common.enumeration.EnabledStatus;
import org.chendev.tasteexpress.common.error.ErrorCode;
import org.chendev.tasteexpress.common.exception.AuthException;
import org.chendev.tasteexpress.common.result.PageResult;
import org.chendev.tasteexpress.pojo.dto.SetmealDTO;
import org.chendev.tasteexpress.pojo.dto.SetmealPageQueryDTO;
import org.chendev.tasteexpress.pojo.entity.Dish;
import org.chendev.tasteexpress.pojo.entity.Setmeal;
import org.chendev.tasteexpress.pojo.entity.SetmealDish;
import org.chendev.tasteexpress.pojo.vo.DishItemVO;
import org.chendev.tasteexpress.pojo.vo.SetmealVO;
import org.chendev.tasteexpress.server.repository.DishRepository;
import org.chendev.tasteexpress.server.repository.SetmealDishRepository;
import org.chendev.tasteexpress.server.repository.SetmealRepository;
import org.chendev.tasteexpress.server.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SetmealServiceImpl implements SetmealService {

    private final SetmealRepository setmealRepository;
    private final SetmealDishRepository setmealDishRepository;
    private final DishRepository dishRepository;


    @Override
    @Transactional
    public void saveWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        if (setmeal.getStatus() == null) setmeal.setStatus(EnabledStatus.ENABLED);

        setmealRepository.save(setmeal);
        Long id = setmeal.getId();

        List<SetmealDish> items = setmealDTO.getSetmealDishes();
        if (items != null && !items.isEmpty()) {
            for (SetmealDish smd : items) {
                smd.setId(null);
                smd.setSetmealId(id);
            }
            setmealDishRepository.saveAll(items);
        }
    }

    @Override
    public PageResult pageQuery(SetmealPageQueryDTO dto) {
        Pageable pageable = PageRequest.of(Math.max(dto.getPage()-1, 0), dto.getPageSize(),
                Sort.by(Sort.Order.desc("updatedAt")));
        String name = (dto.getName() == null || dto.getName().isBlank()) ? null : dto.getName();

        Page<Setmeal> page = setmealRepository.pageQuery(name, dto.getCategoryId(), dto.getStatus(), pageable);

        List<SetmealVO> records = new ArrayList<>(page.getContent().size());
        for (Setmeal s : page.getContent()) {
            SetmealVO vo = new SetmealVO();
            BeanUtils.copyProperties(s, vo);
            records.add(vo);
        }
        return new PageResult(page.getTotalElements(), records);
    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        for (Long id : ids) {
            Setmeal s = setmealRepository.findById(id)
                    .orElseThrow(() -> new AuthException(ErrorCode.SETMEAL_NOT_FOUND));
            if (Objects.equals(s.getStatus(), EnabledStatus.ENABLED)) {
                throw new AuthException(ErrorCode.SETMEAL_ON_SALE);
            }
        }

        for (Long id : ids) {
            setmealDishRepository.deleteBySetmealId(id);
            setmealRepository.deleteById(id);
        }
    }

    @Override
    public SetmealVO getByIdWithDish(Long id) {
        Setmeal s = setmealRepository.findById(id)
                .orElseThrow(() -> new AuthException(ErrorCode.SETMEAL_NOT_FOUND));
        SetmealVO vo = new SetmealVO();
        BeanUtils.copyProperties(s, vo);
        vo.setSetmealDishes(setmealDishRepository.findBySetmealId(id));
        return vo;
    }

    @Override
    @Transactional
    public void update(SetmealDTO setmealDTO) {
        Setmeal s = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, s);
        setmealRepository.save(s);

        Long id = setmealDTO.getId();
        setmealDishRepository.deleteBySetmealId(id);

        List<SetmealDish> items = setmealDTO.getSetmealDishes();
        if (items != null && !items.isEmpty()) {
            for (SetmealDish smd : items) {
                smd.setId(null);
                smd.setSetmealId(id);
            }
            setmealDishRepository.saveAll(items);
        }
    }

    @Override
    @Transactional
    public void startOrStop(EnabledStatus status, Long id) {
        if (Objects.equals(status, EnabledStatus.ENABLED)) {
            List<Dish> dishList = dishRepository.findBySetmealId(id);
            if (dishList != null && !dishList.isEmpty()) {
                for (Dish d : dishList) {
                    if (Objects.equals(d.getStatus(), EnabledStatus.DISABLED)) {
                        throw new AuthException(ErrorCode.SETMEAL_ENABLE_FAILED);
                    }
                }
            }
        }
        Setmeal s = setmealRepository.findById(id)
                .orElseThrow(() -> new AuthException(ErrorCode.SETMEAL_NOT_FOUND));
        s.setStatus(status);
        setmealRepository.save(s);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Setmeal> list(Setmeal setmeal) {
        Long categoryId = setmeal.getCategoryId();
        EnabledStatus status = setmeal.getStatus();

        if (categoryId != null && status != null) {
            return setmealRepository.findByCategoryIdAndStatus(categoryId, status);
        } else if (categoryId != null) {
            return setmealRepository.findByCategoryId(categoryId);
        } else if (status != null) {
            return setmealRepository.findByStatus(status);
        }
        return setmealRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealDishRepository.findDishItemsBySetmealId(id);
    }
}
