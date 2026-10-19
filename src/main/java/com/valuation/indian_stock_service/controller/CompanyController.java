package com.valuation.indian_stock_service.controller;

import com.valuation.indian_stock_service.model.ApiResponse;
import com.valuation.indian_stock_service.model.Company;
import com.valuation.indian_stock_service.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RestController
@CrossOrigin
public class CompanyController {

    private static final Logger LOG = Logger.getLogger(CompanyController.class.getName());

    CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/in/companies")
    public ResponseEntity<List<String>> getAllCompanies() {
        List<String> companyNames = this.companyService.getCompanyList();
        LOG.info("Retrieved Company Names. Returning to client");
//        return companyNames;
        return new ResponseEntity<>(companyNames, HttpStatus.OK);
    }

    @GetMapping("/in/companies/{companyName}")
    public ResponseEntity<Company> getCompany(@PathVariable String companyName) {
        LOG.info("Fetching Company Details for: " + companyName);
        Company company = this.companyService.getCompanyData(companyName);
        if (company == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(company, HttpStatus.OK);
    }
}
