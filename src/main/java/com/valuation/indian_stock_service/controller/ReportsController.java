package com.valuation.indian_stock_service.controller;

import com.valuation.indian_stock_service.service.CompanyAnnualReportFinderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@CrossOrigin
public class ReportsController {

  private static final Logger LOG = Logger.getLogger(FinancialDataController.class.getName());
  CompanyAnnualReportFinderService carfs;

  @Autowired
  public ReportsController(CompanyAnnualReportFinderService carfs) { this.carfs = carfs; }

  @GetMapping("/in/reports/{companyName}")
  public ResponseEntity<Integer> fetchReports(@PathVariable String companyName) {
    try {
      LOG.info("Fetching Annual Reports for Company: " + companyName);
      int count = carfs.downloadAnnualReport(companyName);
      LOG.info(count + " Annual Reports fetched successfully for Company: " + companyName);
      return new ResponseEntity<>(count, org.springframework.http.HttpStatus.OK);
    }
    catch (Exception e) {
      LOG.severe("Error occurred while fetching annual reports for Company: " + companyName);
      return new ResponseEntity<>(0, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
