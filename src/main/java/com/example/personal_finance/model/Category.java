package com.example.personal_finance.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    public enum CategoryType {
        INCOME,
        EXPENSE
    }

    @Id
    @GeneratedValue
    private int id;

    @NotEmpty(message = "Category name must not be empty")
    private String name;

    private CategoryType type;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private ArrayList<Transaction> transactions;

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private ArrayList<Budget> budgets;

}