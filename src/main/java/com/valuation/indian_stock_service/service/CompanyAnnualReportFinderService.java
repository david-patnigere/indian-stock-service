package com.valuation.indian_stock_service.service;

import com.microsoft.playwright.*;
import com.valuation.indian_stock_service.controller.CompanyController;
import com.valuation.indian_stock_service.model.JobStatusResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class CompanyAnnualReportFinderService {

  private final DataService  dataService;
  private final CompanyService companyService;

  private final Map<String, JobStatusResponse> jobTracker = new ConcurrentHashMap<>();

  private static final Logger LOG = Logger.getLogger(CompanyController.class.getName());

  public CompanyAnnualReportFinderService(DataService dataService, CompanyService companyService) {
    this.dataService = dataService;
    this.companyService = companyService;
  }

  public JobStatusResponse initialDataFetch(String companyName) {
    if (this.companyService.isCompanyDataComplete(companyName)) {
      // immediately return if data already present
      return new JobStatusResponse(null, "COMPLETED", "Data already present", null);
    }
    return null;
  }

  @Async


  public int downloadAnnualReport(String companyName) {
    // Initialize the Playwright engine
    try (Playwright playwright = Playwright.create()) {
      // Launching a headless Chromium browser instance
      Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
      BrowserContext context = browser.newContext();
      Page page = context.newPage();

      // Navigate to the NSE corporate filing page for annual reports
      String url = "https://www.nseindia.com/companies-listing/corporate-filings-annual-reports";

      // Emulating a realistic user-agent to prevent bot blocks
      page.navigate(url);
      // Wait for page to load completely
      // We will specify to Playwright the element we are waiting for
      page.waitForSelector("#ARCompanyInput");

      // Enter the company name in the search box and submit the form
      page.locator("#ARCompanyInput").fill(companyName);
      page.locator("#annualReportFilter_equities").click();

      // Wait for the table with the annual reports to load
      page.waitForSelector("#CFannualreportEquityTable");

      // Prepare to loop over the 5 latest annual reports
      page.waitForSelector("#CFannualreportEquityTable > tbody > tr");
      // Grab all the rows in the tbody
      Locator rows = page.locator("#CFannualreportEquityTable > tbody > tr");
      int rowCount = rows.count();
      Set<String> seenYears = new HashSet<>();
      int targetYears = Math.min(5, rowCount); // in case a company has less than 5 years worth of annual reports

      // Loop over the table rows for the target years
      for (int i = 0; seenYears.size() < targetYears; i++) {
        Locator currentRow = rows.nth(i);
        String toYear = currentRow.locator("> td").nth(2).textContent().trim(); // fetching the year to label the file
        if (seenYears.contains(toYear)) {
          continue;
        }
        seenYears.add(toYear);

        Locator downloadLink = currentRow.locator("> td").nth(3).locator("a").first();

        // Extract the absolute URL directly from the link's href attribute
        String fileUrl = (String) downloadLink.evaluate("node => node.href");

        // Safety check: If for some reason the link is empty, skip this row
        if (fileUrl == null || fileUrl.isEmpty()) {
          LOG.warning("No valid URL found in row for year " + toYear);
          continue;
        }

        // 1. Download the file bytes first, regardless of format
        APIResponse apiResponse = page.request().get(fileUrl);

        if (apiResponse.ok()) {
          byte[] fileBytes = apiResponse.body();

          // 2. Check if the file is a zip or pdf
          String contentType = apiResponse.headers().get("content-type");
          byte[] pdfBytes;

          if (contentType != null && contentType.equalsIgnoreCase("application/zip")) {
            // Extract the PDF from the zip
            pdfBytes = extractDataFromZip(fileBytes);
          } else if (contentType != null && contentType.equalsIgnoreCase("application/pdf")) {
            pdfBytes = fileBytes;
          } else {
            LOG.warning("Unsupported content type: " + contentType);
            continue; // skip this iteration and move to the next report
          }

          // 3. Save the PDF to the local filesystem
          Path outputPath = Paths.get("src/main/resources/reports/" + companyName + "_Annual_Report_" + toYear + ".pdf");
          Files.createDirectories(outputPath.getParent()); // Ensure the directory exists
          try (InputStream is = new ByteArrayInputStream(pdfBytes)) {
            Files.copy(is, outputPath, StandardCopyOption.REPLACE_EXISTING);
            LOG.info("Saved annual report for " + companyName + " for year " + toYear);
          }

        } else {
          LOG.warning("Failed to download file for " + companyName + " for year " + toYear + ". Status: " + apiResponse.status());

        }

      }
      return seenYears.size();
    } catch (IOException e) {
      LOG.severe("IOException occurred: " + e.getMessage());
      return 0;
    }
  }

  // method to extract data from zip files
  private byte[] extractDataFromZip(byte[] zipFileBytes) throws IOException {
    try (ByteArrayInputStream bais = new ByteArrayInputStream(zipFileBytes)) {
      ZipInputStream zis = new ZipInputStream(bais);

      ZipEntry zEntry;
      while ((zEntry = zis.getNextEntry()) != null) {
        // check if the current file in the zip is a pdf
        if (!zEntry.isDirectory() && zEntry.getName().toLowerCase().endsWith(".pdf")) {
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          byte[] data = new byte[8192];
          int bytesRead;

          // read the pdf bytes into our baos buffer
          while ((bytesRead = zis.read(data, 0, data.length)) != -1) {
            baos.write(data, 0, bytesRead);
          }
          zis.closeEntry();
          return baos.toByteArray(); // return the raw pdf bytes
        }
      }
      zis.closeEntry();
    }
    throw new IOException("No PDF file found in the provided ZIP archive.");
  }
}
