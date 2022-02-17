package com.sadatmalik.fusionweb.model;

import javax.persistence.*;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bank;
    private String accountId;
    private String currency;
    private String type;
    private String description;
}