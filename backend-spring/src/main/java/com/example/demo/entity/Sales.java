package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Sales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codeTransaksi;
    private LocalDate tanggalTransaksi;

    @ManyToOne
    private Customer customer;

    @ManyToMany
    private List<Item> items;

    private int qty;
    private double totalDiskon;
    private double totalHarga;
    private double totalBayar;


}

