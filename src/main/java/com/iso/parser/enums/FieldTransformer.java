package com.iso.parser.enums;


import java.util.Map;

import static com.iso.parser.enums.SubStringStat.SUB_FIELD;

public enum FieldTransformer {
    CURRENCY_CONVERSION {
        @Override
        public void transform(Map<String, Object> subElements) {
            try {
                // We use the keys defined in the XML subfields
                int indicator = Integer.parseInt(subElements.get(SUB_FIELD+"1").toString());
                double rawRate = Double.parseDouble(subElements.get(SUB_FIELD+"2").toString());

                // Logic: 69972522 with indicator 6 -> 9.972522
                double convertedRate = rawRate / Math.pow(10, indicator);

                subElements.put("converted_value", String.format("%." + indicator + "f", convertedRate));
            } catch (Exception e) {
                subElements.put("transformer_error", "Calculation failed: " + e.getMessage());
            }
        }
    },
    DATE_TIME_FORMAT {
        @Override
        public void transform(Map<String, Object> subElements) {
            try {
                // Try to get by name first (if defined in XML), otherwise fallback to sub_field_N
                Object dateObj = subElements.getOrDefault("date_mmdd", subElements.get(SUB_FIELD + "1"));
                Object timeObj = subElements.getOrDefault("time_hhmmss", subElements.get(SUB_FIELD + "2"));

                if (dateObj == null || timeObj == null) {
                    subElements.put("transformer_error", "Missing Date/Time subfields");
                    return;
                }

                String date = dateObj.toString(); // Expected MMDD (4 chars)
                String time = timeObj.toString(); // Expected hhmmss (6 chars)

                // Format as: MM-DD hh:mm:ss
                String formatted = String.format("%s-%s %s:%s:%s",
                        date.substring(0, 2), date.substring(2, 4),
                        time.substring(0, 2), time.substring(2, 4), time.substring(4, 6));

                subElements.put("formatted_timestamp", formatted);
            } catch (Exception e) {
                subElements.put("transformer_error", "Date formatting failed: " + e.getMessage());
            }
        }
    },
    PROCESSING_CODE {
        @Override
        public void transform(Map<String, Object> subElements) {
            try {
                // Fix: Use getOrDefault with SUB_FIELD + ID to avoid 'SubField' class dependency
                Object txTypeObj = subElements.getOrDefault("transaction_type", subElements.get(SUB_FIELD + "1"));
                Object fromAccObj = subElements.getOrDefault("from_account_type", subElements.get(SUB_FIELD + "2"));
                Object toAccObj = subElements.getOrDefault("to_account_type", subElements.get(SUB_FIELD + "3"));

                if (txTypeObj == null) {
                    subElements.put("transformer_error", "Missing transaction type subfield");
                    return;
                }

                String txType = txTypeObj.toString();
                String fromAcc = (fromAccObj != null) ? fromAccObj.toString() : "00";
                String toAcc = (toAccObj != null) ? toAccObj.toString() : "00";

                String description;
                switch (txType) {
                    case "00": description = "Purchase"; break;
                    case "01": description = "Withdrawal"; break;
                    case "20": description = "Refund"; break;
                    case "30": description = "Balance Inquiry"; break;
                    case "40": description = "Transfer"; break;
                    default: description = "Other (" + txType + ")";
                }

                if ("40".equals(txType)) {
                    description += " from " + fromAcc + " to " + toAcc;
                }

                subElements.put("description", description);
            } catch (Exception e) {
                subElements.put("transformer_error", "Processing code mapping failed: " + e.getMessage());
            }
        }
    },
    // You can add more here later, e.g., DATE_FORMATTER, MASK_PAN, etc.
    NONE {
        @Override
        public void transform(Map<String, Object> subElements) {
            // Do nothing
        }
    };

    public abstract void transform(Map<String, Object> subElements);

    public static FieldTransformer fromString(String transformerName) {
        if (transformerName == null) return NONE;
        try {
            return FieldTransformer.valueOf(transformerName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return NONE;
        }
    }
}