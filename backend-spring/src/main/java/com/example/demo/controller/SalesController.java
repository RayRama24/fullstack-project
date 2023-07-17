package com.example.demo.controller;

import com.example.demo.entity.Customer;
import com.example.demo.entity.Item;
import com.example.demo.entity.Sales;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.SalesRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sales")
public class SalesController {
    private final SalesRepository salesRepository;
    private final CustomerRepository customerRepository;
    private final ItemRepository itemRepository;

    public SalesController(SalesRepository salesRepository, CustomerRepository customerRepository, ItemRepository itemRepository) {
        this.salesRepository = salesRepository;
        this.customerRepository = customerRepository;
        this.itemRepository = itemRepository;
    }

    @GetMapping
    public List<Sales> getAllSales() {
        return salesRepository.findAll();
    }

    @PostMapping
    public Sales createSales(@RequestBody Sales sales) {

        Customer customer = customerRepository.findById(sales.getCustomer().getId())
                .orElseThrow(() -> new NoSuchElementException("Customer not found"));

        List<Item> items = sales.getItems().stream()
                .map(item -> itemRepository.findById(item.getId())
                        .orElseThrow(() -> new NoSuchElementException("Item not found")))
                .collect(Collectors.toList());

        // Calculate totalDiskon, totalHarga, totalBayar based on customer's diskon and items' hargaSatuan
        double totalDiskon = customer.getDiskon();
        double totalHarga = items.stream()
                .mapToDouble(item -> item.getHargaSatuan() * sales.getQty())
                .sum();
        double totalBayar = totalHarga - totalDiskon;

        sales.setCustomer(customer);
        sales.setItems(items);
        sales.setTotalDiskon(totalDiskon);
        sales.setTotalHarga(totalHarga);
        sales.setTotalBayar(totalBayar);

        return salesRepository.save(sales);
    }
}
