package com.valuation.indian_stock_service.controller;

import com.valuation.indian_stock_service.model.Company;
import com.valuation.indian_stock_service.service.CompanyAnnualReportFinderService;
import com.valuation.indian_stock_service.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

@RestController
@CrossOrigin
public class FinancialDataController {

    private static final Logger LOG = Logger.getLogger(FinancialDataController.class.getName());

    CompanyAnnualReportFinderService annualReportFinderService;
    CompanyService companyService;

    @Autowired
    public FinancialDataController(CompanyAnnualReportFinderService annualReportFinderService, CompanyService companyService) {
        this.annualReportFinderService = annualReportFinderService;
        this.companyService = companyService;
    }

    @GetMapping("/in/financials/{companyName}")
    public ResponseEntity<Boolean> getCompany(@PathVariable String companyName) {
        LOG.info("Checking Company Financials Details for: " + companyName);
        String currentYear = LocalDate.now().getYear() + "";
        this.companyService.fetchFinancialData(companyName, currentYear);


        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
