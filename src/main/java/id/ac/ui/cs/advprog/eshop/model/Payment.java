package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;
import java.util.Map;

@Getter
public class Payment {
    String id;
    String method;
    String status;
    Map<String, String> paymentData;
    Order order;

    public Payment(String id, String method, Map<String, String> paymentData) {
        this.id = id;
        this.method = method;
        this.paymentData = paymentData;

        if (paymentData == null) {
            this.status = "REJECTED";
            return;
        }

        if ("VOUCHER".equals(method)) {
            this.status = validateVoucher(paymentData) ? "SUCCESS" : "REJECTED";
        } else if ("BANK_TRANSFER".equals(method)) {
            this.status = validateBankTransfer(paymentData) ? "SUCCESS" : "REJECTED";
        } else {
            this.status = "REJECTED";
        }
    }

    public Payment(String id, String method, Map<String, String> paymentData, Order order) {
        this(id, method, paymentData);
        this.order = order;
    }

    public void setStatus(String status) {
        this.status = status;
        if (this.order != null) {
            if ("SUCCESS".equals(status)) {
                this.order.setStatus("SUCCESS");
            } else if ("REJECTED".equals(status)) {
                this.order.setStatus("FAILED");
            }
        }
    }

    // REFACTOR: Ekstraksi logika validasi Voucher
    private boolean validateVoucher(Map<String, String> paymentData) {
        String voucherCode = paymentData.get("voucherCode");
        if (voucherCode != null && voucherCode.length() == 16 && voucherCode.startsWith("ESHOP")) {
            int numCount = 0;
            for (char c : voucherCode.toCharArray()) {
                if (Character.isDigit(c)) numCount++;
            }
            return numCount == 8;
        }
        return false;
    }

    // REFACTOR: Ekstraksi logika validasi Bank Transfer
    private boolean validateBankTransfer(Map<String, String> paymentData) {
        String bankName = paymentData.get("bankName");
        String referenceCode = paymentData.get("referenceCode");
        return bankName != null && !bankName.trim().isEmpty() &&
                referenceCode != null && !referenceCode.trim().isEmpty();
    }
}