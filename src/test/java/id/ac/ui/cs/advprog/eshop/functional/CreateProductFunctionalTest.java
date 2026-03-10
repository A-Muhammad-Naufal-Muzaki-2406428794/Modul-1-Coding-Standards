package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class CreateProductFunctionalTest {

    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }

    @Test
    void createProduct_isSuccessful(ChromeDriver driver) throws Exception {
        // 1. Robot membuka halaman form Create Product
        driver.get(baseUrl + "/product/create");

        // 2. Robot mencari elemen form berdasarkan ID dan mengetik nama produk
        WebElement nameInput = driver.findElement(By.id("nameInput"));
        nameInput.clear();
        nameInput.sendKeys("Sampo Cap Kuda");

        // 3. Robot mencari elemen form untuk quantity dan mengetik jumlahnya
        WebElement quantityInput = driver.findElement(By.id("quantityInput"));
        quantityInput.clear();
        quantityInput.sendKeys("50");

        // 4. Robot mencari tombol submit dan mengkliknya
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        // 5. Menyuruh robot menunggu maksimal 5 detik sampai URL berubah menjadi "/product/list"
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.urlToBe(baseUrl + "/product/list"));

        // Verifikasi: Apakah robot diarahkan (redirect) ke halaman product list?
        assertEquals(baseUrl + "/product/list", driver.getCurrentUrl());

        // 6. Verifikasi: Apakah produk "Sampo Cap Kuda" muncul di halaman tersebut?
        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("Sampo Cap Kuda"));
        assertTrue(pageSource.contains("50"));
    }
}