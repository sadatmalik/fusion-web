package com.sadatmalik.fusionweb.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * JPA entity class that models an application user - the intention is to
 * separate application specific user data from security specific user data.
 * The latter is encapsulated within the {@code UserPrincipal} type.
 *
 * @see com.sadatmalik.fusionweb.model.websecurity.UserPrincipal
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString(exclude = {"accounts","incomeList","monthlyIncomeList","debts","weeklyExpenses","monthlyExpenses","goals"})
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;

    @OneToMany(mappedBy = "user")
    private List<Account> accounts;

    @OneToMany(mappedBy = "user")
    private List<Income> incomeList;

    @OneToMany(mappedBy = "user")
    private List<MonthlyIncome> monthlyIncomeList;

    @OneToMany(mappedBy = "user")
    private List<Debt> debts;

    @OneToMany(mappedBy = "user")
    private List<WeeklyExpense> weeklyExpenses;

    @OneToMany(mappedBy = "user")
    private List<MonthlyExpense> monthlyExpenses;

    @OneToMany(mappedBy = "user")
    private List<Goal> goals;
}
