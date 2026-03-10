package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private Model model;

    @InjectMocks
    private ProductController productController;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
    }

    @Test
    void testCreateProductPage() {
        String viewName = productController.createProductPage(model);
        assertEquals("createProduct", viewName);
        verify(model, times(1)).addAttribute(eq("product"), any(Product.class));
    }

    @Test
    void testCreateProductPost() {
        String viewName = productController.createProductPost(product, model);
        assertEquals("redirect:list", viewName);
        verify(productService, times(1)).create(product);
    }

    @Test
    void testProductListPage() {
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        when(productService.findAll()).thenReturn(productList);

        String viewName = productController.productListPage(model);
        assertEquals("productList", viewName);
        verify(model, times(1)).addAttribute("products", productList);
    }

    @Test
    void testEditProductPage() {
        when(productService.findById(product.getProductId())).thenReturn(product);

        String viewName = productController.editProductPage(product.getProductId(), model);
        assertEquals("editProduct", viewName);
        verify(model, times(1)).addAttribute("product", product);
    }

    @Test
    void testEditProductPost() {
        String viewName = productController.editProductPost(product);
        assertEquals("redirect:list", viewName);
        verify(productService, times(1)).update(product);
    }

    @Test
    void testDeleteProduct() {
        String viewName = productController.deleteProduct(product.getProductId());
        assertEquals("redirect:../list", viewName);
        verify(productService, times(1)).delete(product.getProductId());
    }
}