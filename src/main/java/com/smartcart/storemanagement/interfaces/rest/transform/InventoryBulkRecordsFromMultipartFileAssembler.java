package com.smartcart.storemanagement.interfaces.rest.transform;

import com.smartcart.storemanagement.domain.services.InventoryBulkRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InventoryBulkRecordsFromMultipartFileAssembler {

    public static List<InventoryBulkRecord> toRecords(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Bulk inventory file is required");
        }
        try (var reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            List<InventoryBulkRecord> records = new ArrayList<>();
            String line;
            boolean headerChecked = false;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split(",");
                if (!headerChecked) {
                    headerChecked = true;
                    if (parts.length > 0 && "sku".equalsIgnoreCase(parts[0].trim())) {
                        continue;
                    }
                }
                if (parts.length < 8) {
                    throw new IllegalArgumentException("Each row must include at least 8 columns");
                }
                String sku = get(parts, 0);
                String name = get(parts, 1);
                String brand = get(parts, 2);
                Long categoryId = parseLong(get(parts, 3), "categoryId");
                BigDecimal priceAmount = parseDecimal(get(parts, 4), "priceAmount");
                String currency = get(parts, 5);
                int quantity = parseInt(get(parts, 6), "quantity");
                int minThreshold = parseInt(get(parts, 7), "minThreshold");
                boolean promotional = parseBoolean(get(parts, 8));
                BigDecimal discount = parts.length > 9 && !get(parts, 9).isBlank() ? parseDecimal(get(parts, 9), "discountPercentage") : null;
                LocalDate expiryDate = parts.length > 10 && !get(parts, 10).isBlank() ? LocalDate.parse(get(parts, 10)) : null;
                if (promotional && (discount == null || expiryDate == null)) {
                    throw new IllegalArgumentException("Promotional records require discountPercentage and expiryDate");
                }
                records.add(new InventoryBulkRecord(
                        sku,
                        name,
                        brand,
                        categoryId,
                        priceAmount,
                        currency,
                        quantity,
                        minThreshold,
                        promotional,
                        discount,
                        expiryDate
                ));
            }
            return records;
        } catch (IOException ex) {
            throw new IllegalArgumentException("Unable to read bulk inventory file", ex);
        }
    }

    private static String get(String[] parts, int index) {
        if (index >= parts.length) {
            return "";
        }
        return parts[index].trim();
    }

    private static Long parseLong(String value, String field) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid " + field + " value", ex);
        }
    }

    private static int parseInt(String value, String field) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid " + field + " value", ex);
        }
    }

    private static BigDecimal parseDecimal(String value, String field) {
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid " + field + " value", ex);
        }
    }

    private static boolean parseBoolean(String value) {
        return value != null && Boolean.parseBoolean(value.trim());
    }
}

