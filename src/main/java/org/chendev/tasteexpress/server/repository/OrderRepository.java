package org.chendev.tasteexpress.server.repository;

import org.chendev.tasteexpress.pojo.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findByNumber(String number);

    Page<Order> findByUserIdOrderByOrderTimeDesc(Long userId, Pageable pageable);

    Page<Order> findByUserIdAndStatusOrderByOrderTimeDesc(Long userId, Integer status, Pageable pageable);

    @Query("""
      select o from Order o
      where (:number is null or o.number like concat('%', :number, '%'))
        and (:phone  is null or o.phone  like concat('%', :phone , '%'))
        and (:status is null or o.status = :status)
        and (:begin  is null or o.orderTime >= :begin)
        and (:end    is null or o.orderTime <= :end)
      order by o.orderTime desc
    """)
    Page<Order> conditionPage(@Param("number") String number,
                              @Param("phone") String phone,
                              @Param("status") Integer status,
                              @Param("begin") LocalDateTime begin,
                              @Param("end") LocalDateTime end,
                              Pageable pageable);
}
