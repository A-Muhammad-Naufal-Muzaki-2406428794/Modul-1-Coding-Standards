package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;
import java.util.Map;

@Getter
public class Payment {
    String id;
    String method;
    String status;
    Map<String, String> paymentData;
    Order order; // Tambahan referensi ke Order terkait

    // Constructor lama untuk kompatibilitas
    public Payment(String id, String method, Map<String, String> paymentData) {
        this.id = id;
        this.method = method;
        this.paymentData = paymentData;

        if (paymentData == null) {
            this.status = "REJECTED";
            return;
        }

        if ("VOUCHER".equals(method)) {
            String voucherCode = paymentData.get("voucherCode");
            if (voucherCode != null && voucherCode.length() == 16 && voucherCode.startsWith("ESHOP")) {
                int numCount = 0;
                for (char c : voucherCode.toCharArray()) {
                    if (Character.isDigit(c)) numCount++;
                }
                if (numCount == 8) {
                    this.status = "SUCCESS";
                } else {
                    this.status = "REJECTED";
                }
            } else {
                this.status = "REJECTED";
            }
        } else if ("BANK_TRANSFER".equals(method)) {
            String bankName = paymentData.get("bankName");
            String referenceCode = paymentData.get("referenceCode");
            if (bankName != null && !bankName.trim().isEmpty() &&
                    referenceCode != null && !referenceCode.trim().isEmpty()) {
                this.status = "SUCCESS";
            } else {
                this.status = "REJECTED";
            }
        } else {
            this.status = "REJECTED";
        }
    }

    // Constructor baru dengan parameter Order
    public Payment(String id, String method, Map<String, String> paymentData, Order order) {
        this(id, method, paymentData);
        this.order = order;
    }

    // Method untuk set status Payment & otomatis mengupdate status Order
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
}