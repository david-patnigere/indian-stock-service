package com.valuation.indian_stock_service.service;

import com.valuation.indian_stock_service.model.Company;
import com.valuation.indian_stock_service.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class CompanyService {

    private static final Logger LOG = Logger.getLogger(CompanyService.class.getName());

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<String> getCompanyList() {
        LOG.info("Fetching Full Company List from Database");
        List<Company> companyList = companyRepository.findAll().stream().toList();
        List<String> companyNames = companyList.stream()
                        .map(Company::getName)
                                .toList();
        LOG.info("Retrieved Company Names");

        return companyNames;
    }

    public Company getCompanyData(String companyName) {
        LOG.info("Fetching Company Details for: " + companyName);
      return this.companyRepository.findByName(companyName)
          .orElse(null);
    }

    public boolean isCompanyDataComplete(String companyName) {
        return false;
    }

    public void fetchFinancialData(String companyName, String currentYear) {
    }
}
