package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    @Test
    void testCreateVoucherPaymentSuccess() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("1", "VOUCHER", paymentData);
        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testCreateVoucherPaymentRejectedInvalidLength() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP123");
        Payment payment = new Payment("2", "VOUCHER", paymentData);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCreateVoucherPaymentRejectedInvalidPrefix() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "KSHOP1234ABC5678");
        Payment payment = new Payment("3", "VOUCHER", paymentData);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCreateVoucherPaymentRejectedInvalidNumCount() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP12345678901");
        Payment payment = new Payment("4", "VOUCHER", paymentData);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCreateBankTransferPaymentSuccess() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "BCA");
        paymentData.put("referenceCode", "REF12345");
        Payment payment = new Payment("5", "BANK_TRANSFER", paymentData);
        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testCreateBankTransferPaymentRejectedEmptyBankName() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "");
        paymentData.put("referenceCode", "REF12345");
        Payment payment = new Payment("6", "BANK_TRANSFER", paymentData);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCreateBankTransferPaymentRejectedNullReference() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "BCA");
        paymentData.put("referenceCode", null);
        Payment payment = new Payment("7", "BANK_TRANSFER", paymentData);
        assertEquals("REJECTED", payment.getStatus());
    }
}