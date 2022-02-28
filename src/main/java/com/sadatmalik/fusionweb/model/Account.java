package com.sadatmalik.fusionweb.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "accounts")
@ToString(exclude = {"incomeList","monthlyIncomeList","debts","weeklyExpenses", "monthlyExpenses", "goals"})
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountId;
    private String name; // e.g. HSBC

    @Enumerated(value = EnumType.STRING)
    private AccountType type; // e.g current, savings, cash

    // @todo use Balance object
    private double balance;

    // @todo hardcoded
    private String currency;

    private String description;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "account")
    private List<Income> incomeList;

    @OneToMany(mappedBy = "account")
    private List<MonthlyIncome> monthlyIncomeList;

    @OneToMany(mappedBy = "account")
    private List<Debt> debts;

    @OneToMany(mappedBy = "account")
    private List<WeeklyExpense> weeklyExpenses;

    @OneToMany(mappedBy = "account")
    private List<MonthlyExpense> monthlyExpenses;

    @OneToMany(mappedBy = "account")
    private List<Goal> goals;

    @ManyToOne
    @JoinColumn(name = "bankId", referencedColumnName = "id")
    private Bank bank;

    public String displayBalance() {
        return String.format("£%,.2f", balance);
    }
}