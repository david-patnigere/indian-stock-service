package com.valuation.indian_stock_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "financial_metrics")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class FinancialMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(nullable = false)
    private Integer year;

    // MetaData fields
    @Column(nullable = false)
    private Long unitMultiplier;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private String source;

    // FinanceData fields
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalAssets;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalLiabilities;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalEquity;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal cashAndEquivalents;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal shortTermDebt;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal longTermDebt;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal cashFromOperatingActivities;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal capitalExpenditures;

    @Column(nullable = false)
    private BigDecimal sharesOutstanding;
}
