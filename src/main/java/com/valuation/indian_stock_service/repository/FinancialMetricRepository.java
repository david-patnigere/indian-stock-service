package com.valuation.indian_stock_service.repository;

import com.valuation.indian_stock_service.model.Company;
import com.valuation.indian_stock_service.model.FinancialMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinancialMetricRepository extends JpaRepository<FinancialMetric, Long> {

    // fetch 5 years data for a specific company sorted by year
    List<FinancialMetric> findByCompanyOrderByYearDesc(Company company);

    // count how many years of data we have for a company from the present year
    Long countByCompanyId(Long companyId);
}
