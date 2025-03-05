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
@Table(name = "goals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Goal {

    public enum GoalStatus {
        PENDING,
        PROGRESS,
        ACHIEVED,
        UNACHIEVED
    }

    @Id
    @GeneratedValue
    private int id;

    private String name;

    @Column(name = "target_amount")
    @NotNull(message = "Goal target amount must not be null")
    private double targetAmount;

    @Column(name = "saved_amount")
    private double savedAmount;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    private GoalStatus status;

    @Column(name = "created_at")
    @Past
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @Past
    private Timestamp updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "Goal user must not be null")
    private User user;

}