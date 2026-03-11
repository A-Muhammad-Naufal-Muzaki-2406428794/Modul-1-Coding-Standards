package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRepositoryTest {
    PaymentRepository paymentRepository;
    Payment payment1;
    Payment payment2;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();

        Map<String, String> paymentData1 = new HashMap<>();
        paymentData1.put("voucherCode", "ESHOP1234ABC5678");
        payment1 = new Payment("1", "VOUCHER", paymentData1);

        Map<String, String> paymentData2 = new HashMap<>();
        paymentData2.put("bankName", "BCA");
        paymentData2.put("referenceCode", "REF12345");
        payment2 = new Payment("2", "BANK_TRANSFER", paymentData2);
    }

    @Test
    void testSaveCreate() {
        Payment result = paymentRepository.save(payment1);

        Payment findResult = paymentRepository.findById(payment1.getId());
        assertEquals(payment1.getId(), result.getId());
        assertEquals(payment1.getId(), findResult.getId());
        assertEquals(payment1.getMethod(), findResult.getMethod());
        assertEquals(payment1.getStatus(), findResult.getStatus());
        assertEquals(payment1.getPaymentData(), findResult.getPaymentData());
    }

    @Test
    void testSaveUpdate() {
        paymentRepository.save(payment1);

        // Asumsikan kita mengupdate datanya (objek baru dengan ID sama)
        Payment newPayment = new Payment(payment1.getId(), "VOUCHER", payment1.getPaymentData());
        Payment result = paymentRepository.save(newPayment);

        assertEquals(payment1.getId(), result.getId());
    }

    @Test
    void testFindByIdIfFound() {
        paymentRepository.save(payment1);
        Payment findResult = paymentRepository.findById(payment1.getId());
        assertEquals(payment1.getId(), findResult.getId());
    }

    @Test
    void testFindByIdIfNotFound() {
        assertNull(paymentRepository.findById("invalid-id"));
    }

    @Test
    void testFindAll() {
        paymentRepository.save(payment1);
        paymentRepository.save(payment2);

        List<Payment> paymentList = paymentRepository.findAll();
        assertEquals(2, paymentList.size());
    }
}