package prueba;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import recursos.Recursos;

import java.time.Duration;

public class Google {
    public WebDriver driver;
    public WebDriverWait wait;

    @Given("Abrir Google")
    public void abrir_google() {
        System.out.println("********************************************** INICIO DEL TEST **********************************************");
        System.setProperty(Recursos.Drivers.CHROME_DRIVER, Recursos.Drivers.PATH_DRIVER);
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        driver.get("https://google.com");
    }

    @When("busco casas amarillas")
    public void busco_casas_amarillas() {
        try {
            WebElement searchBox = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("q")));
            searchBox.sendKeys("casas amarillas");
            System.out.println("Buscando casas amarillas");
        } catch (Exception e) {
            System.out.println("Error al buscar casas amarillas: " + e.getMessage());
        }
    }

    @When("pulso buscar")
    public void pulso_buscar() {
        try {
            WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.name("btnK")));
            searchButton.click();
            System.out.println("Pulsando el botón de búsqueda");
        } catch (Exception e) {
            System.out.println("Error al pulsar el botón de búsqueda: " + e.getMessage());
        }
    }

    @Then("aparece respuesta")
    public void aparece_respuesta() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("search")));
            System.out.println("Aparece la respuesta de búsqueda");
        } catch (Exception e) {
            System.out.println("Error al verificar la respuesta de búsqueda: " + e.getMessage());
        }
    }

    @Then("Cierro Google")
    public void cierro_google() {
        try {
            if (driver != null) {
                driver.quit();
                System.out.println("Navegador cerrado correctamente");
            }
        } catch (Exception e) {
            System.out.println("Error al cerrar el navegador: " + e.getMessage());
        }
    }
}