package com.valuation.indian_stock_service.config;

import com.valuation.indian_stock_service.model.Company;
import com.valuation.indian_stock_service.repository.CompanyRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class InitialDatabaseLoader {

    @Bean
    public CommandLineRunner initialDatabaseLoad(CompanyRepository companyRepository) {
        return args -> {
            if (companyRepository.count() > 0) {
                System.out.println("Database is already seeded. Skipping initial load.");
                return;
            }
            System.out.println("Performing initial data load for database");
            List<Company> companyBatch = new ArrayList<>();

            // this is a try with resources. it removes the need to manually close the reader
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new ClassPathResource("company_list.csv").getInputStream()))) {
                String line;
                boolean isHeader = true;

                while ((line = br.readLine()) != null) {
                    if (isHeader) {
                        isHeader = false;
                        continue;
                    }
                    String[] data = line.split(",");
                    Company company = new Company();
                    if (data.length == 8) {
                        company.setTickerNSE(data[0].trim());
                        company.setName(data[1].trim());
                        company.setIsin(data[6].trim());
                        company.setIsDataComplete(false);
                        companyBatch.add(company);
                        System.out.println("Added " + company.getName() + " to the batch");
                    }
                }
                if (companyBatch.size() > 0) {
                    System.out.println("Saving " + companyBatch.size() + " companies in database");
                    companyRepository.saveAll(companyBatch);
                }
                System.out.println("Finished initial load of companies");
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        };
    }
}
