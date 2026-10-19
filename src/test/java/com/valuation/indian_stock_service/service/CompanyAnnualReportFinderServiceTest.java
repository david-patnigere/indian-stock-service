package com.valuation.indian_stock_service.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CompanyAnnualReportFinderServiceTest {

    @Autowired
    private CompanyAnnualReportFinderService scraperService;

    @Test
    void verifyReportDownloadForValidTicker() throws IOException {
        String testTicker = "BERGEPAINT";

        // Execute the scraper for a single test company
        scraperService.downloadAnnualReport(testTicker);

        // Verify that the target download directory actually contains the output
        Path reportDirectory = Paths.get("src/main/resources/reports/");

        assertTrue(Files.exists(reportDirectory), "Report directory should exist.");

        // Assert that at least one file for this ticker was downloaded and is non-empty
        try (var stream = Files.list(reportDirectory)) {
            boolean fileExistsAndNonEmpty = stream
                .filter(path -> path.getFileName().toString().startsWith(testTicker))
                .anyMatch(path -> {
                    try {
                        return Files.size(path) > 0; // Ensures the file isn't 0 bytes (corrupt)
                    } catch (IOException e) {
                        return false;
                    }
                });

            assertTrue(fileExistsAndNonEmpty, "At least one non-empty file should be downloaded for " + testTicker);
        }
    }
}