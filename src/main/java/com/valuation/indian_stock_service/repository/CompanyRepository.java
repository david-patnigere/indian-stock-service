package com.valuation.indian_stock_service.repository;

import com.valuation.indian_stock_service.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByTickerNSE(String tickerNse);

    Optional<Company> findByName(String companyName);

    // check if company exists using Isin
    boolean existsByIsin(String isin);
}
