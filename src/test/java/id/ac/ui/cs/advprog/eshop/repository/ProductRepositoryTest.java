package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @InjectMocks
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateAndFind() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product.getProductId(), savedProduct.getProductId());
        assertEquals(product.getProductName(), savedProduct.getProductName());
        assertEquals(product.getProductQuantity(), savedProduct.getProductQuantity());
    }

    @Test
    void testEditProductPositive() {
        // Setup: Buat produk awal
        Product product = new Product();
        product.setProductId("12345");
        product.setProductName("Sampo Awal");
        product.setProductQuantity(10);
        productRepository.create(product);

        // Action: Edit produk tersebut
        Product updatedProduct = new Product();
        updatedProduct.setProductId("12345"); // ID harus sama
        updatedProduct.setProductName("Sampo Berubah");
        updatedProduct.setProductQuantity(20);
        Product result = productRepository.update(updatedProduct);

        // Verify: Pastikan nilainya berubah
        assertNotNull(result);
        assertEquals("Sampo Berubah", result.getProductName());
        assertEquals(20, result.getProductQuantity());
    }

    @Test
    void testEditProductNegative_NotFound() {
        // Action: Mencoba mengedit produk dengan ID yang tidak ada ("999")
        Product ghostProduct = new Product();
        ghostProduct.setProductId("999");
        ghostProduct.setProductName("Produk Hantu");
        ghostProduct.setProductQuantity(0);

        Product result = productRepository.update(ghostProduct);

        // Verify: Harus mengembalikan null karena ID tidak ditemukan
        assertNull(result);
    }

    @Test
    void testDeleteProductPositive() {
        // Setup: Buat produk
        Product product = new Product();
        product.setProductId("12345");
        product.setProductName("Sampo Dihapus");
        product.setProductQuantity(10);
        productRepository.create(product);

        // Action: Hapus produk
        productRepository.delete("12345");

        // Verify: Pastikan repository menjadi kosong
        Iterator<Product> iterator = productRepository.findAll();
        assertFalse(iterator.hasNext());
    }

    @Test
    void testDeleteProductNegative_NotFound() {
        // Setup: Buat produk
        Product product = new Product();
        product.setProductId("12345");
        product.setProductName("Sampo Aman");
        product.setProductQuantity(10);
        productRepository.create(product);

        // Action: Coba hapus ID yang salah
        productRepository.delete("999");

        // Verify: Produk asli ("12345") harus tetap ada / tidak terhapus
        Iterator<Product> iterator = productRepository.findAll();
        assertTrue(iterator.hasNext());
        assertEquals("12345", iterator.next().getProductId());
    }
}