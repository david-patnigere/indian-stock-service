package com.valuation.indian_stock_service.service;

import com.valuation.indian_stock_service.model.Company;
import com.valuation.indian_stock_service.model.FinancialMetric;
import com.valuation.indian_stock_service.repository.CompanyRepository;
import com.valuation.indian_stock_service.repository.FinancialMetricRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataService {

    private final CompanyRepository companyRepository;
    private final FinancialMetricRepository financialMetricRepository;

    @Autowired
    public DataService(CompanyRepository companyRepository, FinancialMetricRepository financialMetricRepository) {
        this.companyRepository = companyRepository;
        this.financialMetricRepository = financialMetricRepository;
    }

    @Transactional
    public void saveCompanyData(Company company){
        companyRepository.save(company);
    }

    public FinancialMetric getFinancialMetricData(String companyName){
        return null;
    };
}
