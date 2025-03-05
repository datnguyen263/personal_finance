package com.example.personal_finance.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue
    private int id;

    @NotNull(message = "Transaction amount must not be null")
    private double amount;

    private String description;

    @Column(name = "transaction_date")
    @Past(message = "Transaction date must be in the past")
    private LocalDate transactionDate;

    @Column(name = "created_at")
    @Past
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @Past
    private Timestamp updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "Transaction user ID must not be null")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @NotNull(message = "Transaction category ID must not be null")
    private Category category;

}