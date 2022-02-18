package com.sadatmalik.fusionweb.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "weekly_expense")
public class WeeklyExpense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private BigDecimal amount;
    private int timesPerWeek;
    private int weeklyInterval;

    @Enumerated(value = EnumType.STRING)
    private ExpenseType type;

    @ManyToOne
    @JoinColumn(name = "accountId", referencedColumnName = "id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;

}
