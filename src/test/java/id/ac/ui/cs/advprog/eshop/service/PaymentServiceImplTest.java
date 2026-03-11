package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @InjectMocks
    PaymentServiceImpl paymentService;

    @Mock
    PaymentRepository paymentRepository;

    private Order order;
    private Payment payment;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(2);
        products.add(product1);

        order = new Order("13652556-012a-4c07-b546-54eb1396d79b", products, 1708560000L, "Safira Sudrajat");

        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        payment = new Payment("1", "VOUCHER", paymentData, order);
    }

    @Test
    void testAddPayment() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        doReturn(payment).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(order, "VOUCHER", paymentData);

        verify(paymentRepository, times(1)).save(any(Payment.class));
        assertEquals(payment.getId(), result.getId());
    }

    @Test
    void testSetStatusSuccess() {
        doReturn(payment).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.setStatus(payment, "SUCCESS");

        assertEquals("SUCCESS", result.getStatus());
        assertEquals("SUCCESS", order.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testSetStatusRejected() {
        doReturn(payment).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.setStatus(payment, "REJECTED");

        assertEquals("REJECTED", result.getStatus());
        assertEquals("FAILED", order.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testSetStatusInvalid() {
        assertThrows(IllegalArgumentException.class, () -> paymentService.setStatus(payment, "MEOW"));
        verify(paymentRepository, times(0)).save(any(Payment.class));
    }

    @Test
    void testGetPaymentIfFound() {
        doReturn(payment).when(paymentRepository).findById(payment.getId());

        Payment result = paymentService.getPayment(payment.getId());

        assertEquals(payment.getId(), result.getId());
    }

    @Test
    void testGetPaymentIfNotFound() {
        doReturn(null).when(paymentRepository).findById("invalid-id");
        assertNull(paymentService.getPayment("invalid-id"));
    }

    @Test
    void testGetAllPayments() {
        List<Payment> paymentList = new ArrayList<>();
        paymentList.add(payment);
        doReturn(paymentList).when(paymentRepository).findAll();

        List<Payment> result = paymentService.getAllPayments();

        assertEquals(1, result.size());
    }
}