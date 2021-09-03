package com.mycompany.delivery.entity;

import com.mycompany.delivery.validator.CalculateOrderValidation;
import com.mycompany.delivery.validator.FullOrderValidation;
import com.mycompany.delivery.validator.ValidDate;
import com.mycompany.delivery.validator.ValidationConstants;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

@Entity
@Table(name="order", schema = "public")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq_gen")
    @SequenceGenerator(name = "order_seq_gen", sequenceName = "order_id_seq", allocationSize = 1)
    @Column(name = "order_id")
    private long id;

    @Column(nullable = false)
    private int cost;

    @DecimalMin(value = ValidationConstants.MIN_WEIGHT, groups = {CalculateOrderValidation.class, FullOrderValidation.class})
    @DecimalMax(value = ValidationConstants.MAX_WEIGHT, groups = {CalculateOrderValidation.class, FullOrderValidation.class})
    @Column(nullable = false)
    private double weight;

    @DecimalMin(value = ValidationConstants.MIN_DIMENSION, groups = {CalculateOrderValidation.class, FullOrderValidation.class})
    @DecimalMax(value = ValidationConstants.MAX_DIMENSION, groups = {CalculateOrderValidation.class, FullOrderValidation.class})
    @Column(nullable = false)
    private double length;

    @DecimalMin(value = ValidationConstants.MIN_DIMENSION, groups = {CalculateOrderValidation.class, FullOrderValidation.class})
    @DecimalMax(value = ValidationConstants.MAX_DIMENSION, groups = {CalculateOrderValidation.class, FullOrderValidation.class})
    @Column(nullable = false)
    private double width;

    @DecimalMin(value = ValidationConstants.MIN_DIMENSION, groups = {CalculateOrderValidation.class, FullOrderValidation.class})
    @DecimalMax(value = ValidationConstants.MAX_DIMENSION, groups = {CalculateOrderValidation.class, FullOrderValidation.class})
    @Column(nullable = false)
    private double height;

    @NotBlank(groups = FullOrderValidation.class)
    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ValidDate(groups = FullOrderValidation.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(nullable = false)
    private LocalDate deliveryDate;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    private Route route;
}
