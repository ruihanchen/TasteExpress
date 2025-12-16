package org.chendev.tasteexpress.server.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.chendev.tasteexpress.common.enumeration.EnabledStatus;
import org.chendev.tasteexpress.common.error.ErrorCode;
import org.chendev.tasteexpress.common.exception.BusinessException;
import org.chendev.tasteexpress.common.result.PageResult;
import org.chendev.tasteexpress.pojo.dto.DishDTO;
import org.chendev.tasteexpress.pojo.dto.DishFlavorDTO;
import org.chendev.tasteexpress.pojo.dto.DishQueryDTO;
import org.chendev.tasteexpress.pojo.entity.Dish;
import org.chendev.tasteexpress.pojo.entity.DishFlavor;
import org.chendev.tasteexpress.pojo.vo.DishFlavorItemVO;
import org.chendev.tasteexpress.pojo.vo.DishVO;
import org.chendev.tasteexpress.server.repository.DishFlavorRepository;
import org.chendev.tasteexpress.server.repository.DishRepository;
import org.chendev.tasteexpress.server.service.DishService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;
    private final DishFlavorRepository dishFlavorRepository;

    // ---------- public APIs ----------

    @Override
    @Transactional
    public void createDish(DishDTO dto) {
        // Basic duplication guard
        dishRepository.findByNameIgnoreCase(dto.getName().trim())
                .ifPresent(d -> {
                    throw new BusinessException(
                            ErrorCode.CONFLICT,
                            "Dish name already exists: " + dto.getName()
                    );
                });

        Dish dish = toEntity(dto);
        dishRepository.save(dish);

        upsertFlavors(dish.getId(), dto.getFlavors());
    }

    @Override
    @Transactional
    public void updateDish(DishDTO dto) {
        Dish existing = dishRepository.findById(dto.getId())
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.DISH_NOT_FOUND, // 可以加一个 DISH.NOT_FOUND
                        "Dish not found: id=" + dto.getId()
                ));

        // Only override mutable fields
        existing.setName(dto.getName());
        existing.setCategoryId(dto.getCategoryId());
        existing.setPrice(dto.getPrice());
        existing.setImage(dto.getImage());
        existing.setDescription(dto.getDescription());
        existing.setStatus(dto.getStatus());

        dishRepository.save(existing);

        upsertFlavors(existing.getId(), dto.getFlavors());
    }

    @Override
    @Transactional
    public void deleteDishes(List<Long> ids) {
        ids.forEach(id -> {
            Dish dish = dishRepository.findById(id)
                    .orElseThrow(() -> new BusinessException(
                            ErrorCode.DISH_DELETION_NOT_ALLOWED,
                            "Dish not found: id=" + id
                    ));

            if (Objects.equals(dish.getStatus(), EnabledStatus.ENABLED)) {
                throw new BusinessException(
                        ErrorCode.DISH_DELETION_NOT_ALLOWED,
                        "Dish is currently on sale and cannot be deleted"
                );
            }
        });

        ids.forEach(id -> {
            dishFlavorRepository.deleteByDishId(id);
            dishRepository.deleteById(id);
        });
    }

    @Override
    @Transactional
    public void changeStatus(Long id, EnabledStatus status) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.DISH_DELETION_NOT_ALLOWED,
                        "Dish not found: id=" + id
                ));

        dish.setStatus(status);
        dishRepository.save(dish);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<DishVO> pageQuery(DishQueryDTO dto) {
        int pageIndex = dto.getPage() <= 1 ? 0 : dto.getPage() - 1;
        Pageable pageable = PageRequest.of(pageIndex, dto.getPageSize(),
                Sort.by(Sort.Order.desc("updatedAt")));

        String name = StringUtils.hasText(dto.getName())
                ? dto.getName().trim()
                : null;

        Page<Dish> page = dishRepository.pageQuery(
                name,
                dto.getCategoryId(),
                dto.getStatus(),
                pageable
        );

        List<DishVO> records = page.getContent().stream()
                .map(this::toVOWithoutFlavors)
                .toList();

        return new PageResult<>(page.getTotalElements(), records);
    }

    @Override
    @Transactional(readOnly = true)
    public DishVO getDishWithFlavors(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.BUSINESS_ERROR,
                        "Dish not found: id=" + id
                ));

        List<DishFlavor> flavors = dishFlavorRepository.findByDishId(id);
        return toVOWithFlavors(dish, flavors);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DishVO> listByCategoryForAdmin(Long categoryId) {
        List<Dish> dishes = dishRepository.findByCategoryIdAndStatusOrderByUpdatedAtDesc(
                categoryId,
                EnabledStatus.ENABLED
        );
        return dishes.stream()
                .map(this::toVOWithoutFlavors)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DishVO> listByCategoryForUser(Long categoryId) {
        List<Dish> dishes = dishRepository.findByCategoryIdAndStatus(categoryId, EnabledStatus.ENABLED);

        return dishes.stream()
                .map(dish -> {
                    List<DishFlavor> flavors = dishFlavorRepository.findByDishId(dish.getId());
                    return toVOWithFlavors(dish, flavors);
                })
                .toList();
    }

    // ---------- private helpers ----------

    private Dish toEntity(DishDTO dto) {
        return Dish.builder()
                .id(dto.getId())
                .name(dto.getName().trim())
                .categoryId(dto.getCategoryId())
                .price(dto.getPrice())
                .image(dto.getImage())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .build();
    }

    private DishVO toVOWithoutFlavors(Dish dish) {
        return DishVO.builder()
                .id(dish.getId())
                .name(dish.getName())
                .categoryId(dish.getCategoryId())
                .price(dish.getPrice())
                .image(dish.getImage())
                .description(dish.getDescription())
                .status(dish.getStatus())
                .updatedAt(dish.getUpdatedAt())
                .build();
    }

    private DishVO toVOWithFlavors(Dish dish, List<DishFlavor> flavors) {
        List<DishFlavorItemVO> flavorVOs = flavors.stream()
                .map(f -> DishFlavorItemVO.builder()
                        .name(f.getName())
                        .value(f.getValue())
                        .build())
                .toList();

        return DishVO.builder()
                .id(dish.getId())
                .name(dish.getName())
                .categoryId(dish.getCategoryId())
                .price(dish.getPrice())
                .image(dish.getImage())
                .description(dish.getDescription())
                .status(dish.getStatus())
                .updatedAt(dish.getUpdatedAt())
                .flavors(flavorVOs)
                .build();
    }

    private void upsertFlavors(Long dishId, List<DishFlavorDTO> flavors) {
        dishFlavorRepository.deleteByDishId(dishId);

        if (flavors == null || flavors.isEmpty()) {
            return;
        }

        List<DishFlavor> entities = flavors.stream()
                .map(dto -> DishFlavor.builder()
                        .dishId(dishId)
                        .name(dto.getName())
                        .value(dto.getValue())
                        .build())
                .toList();

        dishFlavorRepository.saveAll(entities);
    }
}

