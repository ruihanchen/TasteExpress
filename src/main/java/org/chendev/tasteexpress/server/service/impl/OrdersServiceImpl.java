package org.chendev.tasteexpress.server.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chendev.tasteexpress.common.context.BaseContext;
import org.chendev.tasteexpress.common.result.PageResult;
import org.chendev.tasteexpress.pojo.dto.OrderPageQueryDTO;
import org.chendev.tasteexpress.pojo.dto.OrderSubmitDTO;
import org.chendev.tasteexpress.pojo.entity.AddressBook;
import org.chendev.tasteexpress.pojo.entity.Order;
import org.chendev.tasteexpress.pojo.entity.OrderDetail;
import org.chendev.tasteexpress.pojo.entity.ShoppingCart;
import org.chendev.tasteexpress.pojo.vo.OrderSubmitVO;
import org.chendev.tasteexpress.pojo.vo.OrderVO;
import org.chendev.tasteexpress.server.repository.*;
import org.chendev.tasteexpress.server.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrderService {

    private final OrderRepository orderRepo;
    private final OrderDetailRepository orderDetailRepo;
    private final ShoppingCartRepository cartRepo;
    private final AddressBookRepository addressRepo;
    private final UserRepository userRepo;


    private Long currentUserId() {
        return BaseContext.getCurrentId();
    }


    @Override
    @Transactional
    public OrderSubmitVO submit(OrderSubmitDTO dto) {
        final Long userId = currentUserId();

        List<ShoppingCart> cart = cartRepo.findByUserIdOrderByCreatedAtAsc(userId);
        if (cart == null || cart.isEmpty()) {
            throw badRequest("Cart is empty, cannot place an order.");
        }

        AddressBook addr = resolveAddressForSubmit(userId, dto.getAddressBookId());
        if (addr == null) {
            throw badRequest("No shipping address found.");
        }

        Totals totals = computeTotals(cart);

        Order order = buildOrderEntityForSubmit(userId, addr, dto, totals);
        orderRepo.save(order);

        List<OrderDetail> details = toOrderDetails(order.getId(), cart);
        orderDetailRepo.saveAll(details);

        cartRepo.deleteAll(cart);

        return OrderSubmitVO.builder()
                .id(order.getId())
                .orderNumber(order.getNumber())
                .orderAmount(order.getAmount())
                .orderTime(order.getOrderTime())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult pageQueryUser(Integer page, Integer pageSize, Integer status) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Order.desc("orderTime")));
        Page<Order> p = (status == null)
                ? orderRepo.findByUserIdOrderByOrderTimeDesc(currentUserId(), pageable)
                : orderRepo.findByUserIdAndStatusOrderByOrderTimeDesc(currentUserId(), status, pageable);

        List<OrderVO> records = p.getContent().stream()
                .map(this::toOrdersVOWithoutDetails)
                .collect(Collectors.toList());

        return new PageResult(p.getTotalElements(), records);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderVO details(Long id) {
        Order o = orderRepo.findById(id).orElse(null);
        if (o == null) return null;

        OrderVO vo = toOrdersVOWithoutDetails(o);
        vo.setOrderDetailList(orderDetailRepo.findByOrderId(id)); // may return empty list
        return vo;
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult conditionSearch(OrderPageQueryDTO dto) {
        Pageable pageable = PageRequest.of(dto.getPage(), dto.getPageSize(), Sort.by(Sort.Order.desc("orderTime")));
        LocalDateTime begin = parseDateTime(dto.getBeginTime());
        LocalDateTime end   = parseDateTime(dto.getEndTime());

        Page<Order> page = orderRepo.conditionPage(nullIfBlank(dto.getOrderNumber()),
                nullIfBlank(dto.getPhone()),
                dto.getStatus(),
                begin, end, pageable);

        List<OrderVO> records = page.getContent().stream()
                .map(this::toOrdersVOWithoutDetails)
                .collect(Collectors.toList());

        return new PageResult(page.getTotalElements(), records);
    }

    @Override
    @Transactional
    public void confirm(Long id) {
        updateStatus(id, Order.CONFIRMED, null);
    }

    @Override
    @Transactional
    public void rejection(Long id, String reason) {
        updateStatus(id, Order.CANCELLED, reason); // original project often treats rejection as a form of cancellation
        setRejectionReason(id, reason);
    }

    @Override
    @Transactional
    public void cancel(Long id, String reason) {
        updateStatus(id, Order.CANCELLED, reason);
        setCancelInfo(id, reason, LocalDateTime.now());
    }

    @Override
    @Transactional
    public void delivery(Long id) {
        updateStatus(id, Order.DELIVERY_IN_PROGRESS, null);
    }

    @Override
    @Transactional
    public void complete(Long id) {
        updateStatus(id, Order.COMPLETED, null);
    }

    /* ======================== Private helpers (keep main flow clean) ======================== */


    private IllegalArgumentException badRequest(String msg) {
        // Keep it simple; replace with your unified BizException later if needed.
        return new IllegalArgumentException(msg);
    }

    /**
     * Resolve shipping address for placing an order:
     * - If addressId is provided, use it.
     * - Otherwise, fallback to user's default address.
     */
    private AddressBook resolveAddressForSubmit(Long userId, Long addressId) {
        if (addressId != null) {
            return addressRepo.findById(addressId).orElse(null);
        }
        return addressRepo.findFirstByUserIdAndIsDefaultTrue(userId).orElse(null);
    }

    /**
     * Compute subtotal from cart snapshots and initialize fee breakdown.
     * Current phase: we only set subtotal and final amount = subtotal (no extra fees),
     * so behavior stays identical to the original minimal flow.
     * Later you can plug in tax/tip/fees/discount here without touching the service API.
     */
    private Totals computeTotals(List<ShoppingCart> cart) {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (ShoppingCart c : cart) {
            BigDecimal unit = c.getAmount() == null ? BigDecimal.ZERO : c.getAmount();
            int qty = c.getNumber() == null ? 0 : c.getNumber();
            subtotal = subtotal.add(unit.multiply(BigDecimal.valueOf(qty)));
        }
        Totals t = new Totals();
        t.subtotal = subtotal;
        t.tax = BigDecimal.ZERO;
        t.tip = BigDecimal.ZERO;
        t.deliveryFee = BigDecimal.ZERO;
        t.serviceFee = BigDecimal.ZERO;
        t.discount = BigDecimal.ZERO;
        t.finalAmount = subtotal; // keep phase-1 behavior
        return t;
    }

    /**
     * Build order header with snapshots and fee breakdown.
     * Status is set to "TO_BE_CONFIRMED" to match the original project semantics after checkout.
     */
    private Order buildOrderEntityForSubmit(Long userId, AddressBook addr, OrderSubmitDTO dto, Totals totals) {
        Order o = new Order();
        o.setNumber(generateOrderNumber());
        o.setStatus(Order.TO_BE_CONFIRMED);
        o.setUserId(userId);
        o.setAddressBookId(addr.getId());
        o.setOrderTime(LocalDateTime.now());
        o.setCheckoutTime(LocalDateTime.now()); // we treat submit as paid in phase-1
        o.setPayMethod(dto.getPayMethod());
        o.setPayStatus(Order.PAID); // phase-1: mark as paid
        o.setAmount(totals.finalAmount);
        o.setRemark(dto.getRemark());
        o.setPhone(addr.getPhone());
        o.setAddress(composeAddressSnapshot(addr));
        o.setConsignee(addr.getConsignee());

        // fee breakdown snapshots
        o.setSubtotalAmount(totals.subtotal);
        o.setTaxAmount(totals.tax);
        o.setTipAmount(totals.tip);
        o.setDeliveryFee(totals.deliveryFee);
        o.setServiceFee(totals.serviceFee);
        o.setDiscountAmount(totals.discount);
        o.setCurrencyCode("USD");

        // placeholders for downstream systems (optional)
        // o.setPaymentProvider(resolvePaymentProvider(dto.getPayMethod()));
        // o.setPaymentIntentId(generatePaymentIntentId(o.getNumber()));
        // o.setAcceptedTime(LocalDateTime.now());

        return o;
    }

    private String generateOrderNumber() {
        // Simple, monotonically increasing-ish number; replace with a distributed ID if needed.
        return "TE" + System.currentTimeMillis();
    }

    private String composeAddressSnapshot(AddressBook a) {
        // Join non-blank parts with comma. Keeps it readable and robust if some pieces are null.
        List<String> parts = new ArrayList<>();
        addIfNotBlank(parts, a.getStreetInfo());
        addIfNotBlank(parts, a.getCityName());
        addIfNotBlank(parts, a.getStateName());
        addIfNotBlank(parts, a.getZipcode());
        return String.join(", ", parts);
    }

    private void addIfNotBlank(List<String> list, String v) {
        if (v != null && !v.isBlank()) list.add(v);
    }

    private List<OrderDetail> toOrderDetails(Long orderId, List<ShoppingCart> cart) {
        return cart.stream().map(c -> {
            OrderDetail d = new OrderDetail();
            d.setOrderId(orderId);
            d.setDishId(c.getDishId());
            d.setSetmealId(c.getSetmealId());
            d.setName(c.getName());
            d.setImage(c.getImage());
            d.setDishFlavor(c.getDishFlavor());
            d.setNumber(c.getNumber());
            d.setAmount(c.getAmount());                  // legacy field kept
            d.setUnitPrice(c.getAmount());               // clearer intent
            d.setLineTotal(safeMul(c.getAmount(), c.getNumber()));
            // d.setModifiersJson(...); // optional: map flavors to JSON if you have structured data
            return d;
        }).collect(Collectors.toList());
    }

    private BigDecimal safeMul(BigDecimal unit, Integer qty) {
        if (unit == null || qty == null) return BigDecimal.ZERO;
        return unit.multiply(BigDecimal.valueOf(qty));
    }

    private void updateStatus(Long id, int newStatus, String remarkAppend) {
        Order o = orderRepo.findById(id).orElse(null);
        if (o == null) throw badRequest("Order not found.");

        o.setStatus(newStatus);
        if (remarkAppend != null && !remarkAppend.isBlank()) {
            // Append the latest remark so we keep prior notes users/admins added.
            String old = o.getRemark();
            o.setRemark(old == null || old.isBlank() ? remarkAppend : (old + " | " + remarkAppend));
        }
        // Some flows may want to stamp business timestamps on transitions:
        // if (newStatus == Orders.CONFIRMED) o.setAcceptedTime(LocalDateTime.now());
        orderRepo.save(o);
    }

    private void setRejectionReason(Long id, String reason) {
        Order o = orderRepo.findById(id).orElse(null);
        if (o == null) return;
        o.setRejectionReason(reason);
        orderRepo.save(o);
    }

    private void setCancelInfo(Long id, String reason, LocalDateTime ts) {
        Order o = orderRepo.findById(id).orElse(null);
        if (o == null) return;
        o.setCancelReason(reason);
        o.setCancelTime(ts);
        orderRepo.save(o);
    }

    private OrderVO toOrdersVOWithoutDetails(Order o) {
        OrderVO vo = new OrderVO();
        BeanUtils.copyProperties(o, vo);
        return vo;
    }

    private static LocalDateTime parseDateTime(String s) {
        if (s == null || s.isBlank()) return null;
        // Accept "yyyy-MM-dd HH:mm:ss" by replacing space with 'T'
        return LocalDateTime.parse(s.replace(" ", "T"));
    }

    private static String nullIfBlank(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }

    /* Simple holder for fee breakdown; convenient to evolve in phase-2 */
    private static class Totals {
        BigDecimal subtotal;
        BigDecimal tax;
        BigDecimal tip;
        BigDecimal deliveryFee;
        BigDecimal serviceFee;
        BigDecimal discount;
        BigDecimal finalAmount;
    }

}
