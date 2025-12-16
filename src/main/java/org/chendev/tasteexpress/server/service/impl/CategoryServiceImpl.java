package org.chendev.tasteexpress.server.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chendev.tasteexpress.common.context.BaseContext;
import org.chendev.tasteexpress.common.enumeration.EnabledStatus;
import org.chendev.tasteexpress.common.error.ErrorCode;
import org.chendev.tasteexpress.common.exception.BusinessException;
import org.chendev.tasteexpress.common.result.PageResult;
import org.chendev.tasteexpress.pojo.dto.CategoryDTO;
import org.chendev.tasteexpress.pojo.dto.CategoryPageQueryDTO;
import org.chendev.tasteexpress.pojo.entity.Category;
import org.chendev.tasteexpress.pojo.vo.CategoryVO;
import org.chendev.tasteexpress.server.repository.CategoryRepository;
import org.chendev.tasteexpress.server.repository.DishRepository;
import org.chendev.tasteexpress.server.repository.SetmealRepository;
import org.chendev.tasteexpress.server.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final DishRepository dishRepository;
    private final SetmealRepository setmealRepository;

    private Long currentUserIdOrNull() {
        return BaseContext.getCurrentId();
    }

    private Long currentUserIdOrThrow() {
        Long id = BaseContext.getCurrentId();
        if (id == null) {
            throw new IllegalStateException("Current user id is not available in context");
        }
        return id;
    }

    @Override
    @Transactional
    public void save(CategoryDTO dto) {
        // Enforce uniqueness at the service level to provide a nicer error message
        if (categoryRepository.existsByName(dto.getName())) {
            throw new BusinessException(ErrorCode.DUPLICATE_NAME);
        }

        LocalDateTime now = LocalDateTime.now();
        Long currentUserId = currentUserIdOrThrow();

        Category category = new Category();
        BeanUtils.copyProperties(dto, category);
        category.setStatus(EnabledStatus.DISABLED); // Newly created categories are disabled by default
        category.setCreatedAt(now);
        category.setUpdatedAt(now);
        category.setCreatedBy(currentUserId);
        category.setUpdatedBy(currentUserId);

        categoryRepository.save(category);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult pageQuery(CategoryPageQueryDTO dto) {
        int pageIndex = Math.max(dto.getPage() - 1, 0);
        int pageSize = dto.getPageSize() <= 0 ? 10 : dto.getPageSize();

        Pageable pageable = PageRequest.of(
                pageIndex,
                pageSize,
                Sort.by(Sort.Order.asc("sort"), Sort.Order.desc("updatedAt"))
        );

        String name = (dto.getName() == null || dto.getName().isBlank())
                ? null
                : dto.getName().trim();

        Integer type = dto.getType();

        Page<Category> page = categoryRepository.pageQuery(name, type, pageable);

        List<CategoryVO> records = page.getContent().stream()
                .map(this::toVO)
                .toList();

        return new PageResult(page.getTotalElements(), records);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        // Guard rails: do not allow deleting categories that are still referenced
        long dishCount = dishRepository.countByCategoryId(id);
        if (dishCount > 0) {
            throw new BusinessException(ErrorCode.CATEGORY_DELETION_NOT_ALLOWED);
        }

        long setmealCount = setmealRepository.countByCategoryId(id);
        if (setmealCount > 0) {
            throw new BusinessException(ErrorCode.CATEGORY_DELETION_NOT_ALLOWED);
        }

        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void update(CategoryDTO dto) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException("Category id must not be null when updating");
        }

        Category existing = categoryRepository.findById(dto.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        // Only update mutable fields
        existing.setType(dto.getType());
        existing.setName(dto.getName());
        existing.setSort(dto.getSort());
        existing.setUpdatedAt(LocalDateTime.now());
        existing.setUpdatedBy(currentUserIdOrThrow());

        categoryRepository.save(existing);
    }

    @Override
    @Transactional
    public void startOrStop(EnabledStatus status, Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        category.setStatus(status);
        category.setUpdatedAt(LocalDateTime.now());
        category.setUpdatedBy(currentUserIdOrThrow());

        categoryRepository.save(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryVO> list(Integer type, boolean onlyEnabled) {
        List<Category> categories;

        if (onlyEnabled) {
            if (type != null) {
                categories = categoryRepository.findByTypeAndStatusOrderBySortAsc(type, EnabledStatus.ENABLED);
            } else {
                categories = categoryRepository.findAllByStatusOrderBySortAsc(EnabledStatus.ENABLED);
            }
        } else {
            if (type != null) {
                categories = categoryRepository.findByTypeOrderBySortAsc(type);
            } else {
                categories = categoryRepository.findAllByOrderBySortAsc();
            }
        }

        return categories.stream()
                .map(this::toVO)
                .toList();
    }

    /**
     * Map a JPA Category entity to a lightweight view object used by both admin and user APIs.
     */
    private CategoryVO toVO(Category category) {
        if (category == null) {
            return null;
        }
        return CategoryVO.builder()
                .id(category.getId())
                .type(category.getType())
                .name(category.getName())
                .sort(category.getSort())
                .status(category.getStatus())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}
