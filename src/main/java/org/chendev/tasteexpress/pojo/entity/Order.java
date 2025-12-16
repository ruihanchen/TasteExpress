package org.chendev.tasteexpress.pojo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(
        name = "orders",
        indexes = {
                @Index(name = "idx_orders_user_time", columnList = "user_id, order_time"),
                @Index(name = "idx_orders_user_status_time", columnList = "user_id, status, order_time"),
                @Index(name = "uk_orders_no", columnList = "number", unique = true),
                @Index(name = "idx_orders_payment_intent", columnList = "payment_intent_id")
        }
)
public class Order {


    /* -------------------- order status -------------------- */
    public static final Integer PENDING_PAYMENT      = 1;
    public static final Integer TO_BE_CONFIRMED      = 2;
    public static final Integer CONFIRMED            = 3;
    public static final Integer DELIVERY_IN_PROGRESS = 4;
    public static final Integer COMPLETED            = 5;
    public static final Integer CANCELLED            = 6;

    /* -------------------- payment status -------------------- */
    public static final Integer UN_PAID = 0;
    public static final Integer PAID    = 1;
    public static final Integer REFUND  = 2;

    /* -------------------- payment method -------------------- */
    // 1=credit_card, 2=paypal
    public static final Integer PAY_METHOD_CREDIT_CARD = 1;
    public static final Integer PAY_METHOD_PAYPAL      = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String number;

    @Column(nullable = false)
    private Integer status;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "address_book_id")
    private Long addressBookId;

    @Column(name = "order_time", nullable = false)
    private LocalDateTime orderTime;

    @Column(name = "checkout_time")
    private LocalDateTime checkoutTime;

    @Column(name = "pay_method")
    private Integer payMethod;

    @Column(name = "pay_status")
    private Integer payStatus;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    private String remark;

    private String phone;

    private String address;

    private String consignee;

    /* -------------------- fee -------------------- */
    @Column(name = "subtotal_amount", precision = 10, scale = 2)
    private BigDecimal subtotalAmount;

    @Column(name = "tax_amount", precision = 10, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "tip_amount", precision = 10, scale = 2)
    private BigDecimal tipAmount;

    @Column(name = "delivery_fee", precision = 10, scale = 2)
    private BigDecimal deliveryFee;

    @Column(name = "service_fee", precision = 10, scale = 2)
    private BigDecimal serviceFee;

    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "currency_code", length = 3)
    private String currencyCode;

    /* -------------------- other info -------------------- */
    @Column(name = "payment_provider", length = 16)
    private String paymentProvider; // stripe / paypal / ...

    @Column(name = "payment_intent_id", length = 64)
    private String paymentIntentId;

    /* -------------------- cancel/reject -------------------- */
    @Column(name = "cancel_reason", length = 200)
    private String cancelReason;

    @Column(name = "rejection_reason", length = 200)
    private String rejectionReason;

    @Column(name = "cancel_time")
    private LocalDateTime cancelTime;

    /* ---------------------------------------- */
    @Column(name = "accepted_time")
    private LocalDateTime acceptedTime;

    @Column(name = "delivery_instructions", length = 255)
    private String deliveryInstructions;

    @Column(name = "source_channel", length = 16)
    private String sourceChannel;
}
