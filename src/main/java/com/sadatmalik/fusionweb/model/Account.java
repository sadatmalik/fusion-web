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
@ToString(exclude = "income")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountId;
    private String name; // e.g. HSBC

    @Enumerated(value = EnumType.STRING)
    private AccountType type; // e.g current, savings, cash

    private double balance;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "account")
    private List<Income> income;

}