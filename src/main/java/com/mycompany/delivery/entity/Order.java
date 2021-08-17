package com.mycompany.delivery.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
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
    @SequenceGenerator(name = "order_seq_gen", sequenceName = "order_id_seq")
    @Column(name = "order_id", nullable = false)
    private long id;

    @Column(name = "cost", nullable = false)
    private int cost;

    @Min(1)
    @Column(name = "weight", nullable = false)
    private int weight;

    @Min(1)
    @Column(name = "length", nullable = false)
    private int length;

    @Min(1)
    @Column(name = "width", nullable = false)
    private int width;

    @Min(1)
    @Column(name = "height", nullable = false)
    private int height;

    @NotBlank
    @Column(name = "address", nullable = false)
    private String address;

    @Future
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "delivery_date", nullable = false)
    private LocalDate deliveryDate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @CreationTimestamp
    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    private Route route;
}
