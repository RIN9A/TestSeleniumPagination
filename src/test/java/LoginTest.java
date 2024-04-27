import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class LoginTest {

    public static LoginPage loginPage;
    public static ProcessingQueuePage processingQueuePage;
    public static TerminalPage terminalPage;
    public static WebDriver driver;
    public static WebDriverWait wait;
    public static ChromeOptions options;
    public static FileWriter fileWriter;
    public static PrintWriter printWriter;

    @BeforeAll
    public static void setup() throws IOException {

        fileWriter = new FileWriter("src/test/java/output.txt");
        printWriter = new PrintWriter(fileWriter);
        System.setProperty("webdriver.chrome.driver", ConfProperties.getProperty("chromedriver"));

        options = new ChromeOptions();
        driver = new ChromeDriver(options);

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        loginPage = new LoginPage(driver);
        processingQueuePage = new ProcessingQueuePage(driver);
        terminalPage = new TerminalPage(driver);

        driver.manage().window().maximize();

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(ConfProperties.getProperty("loginpage"));


    }

    @AfterAll
    public static void quitProfile() throws IOException {

        driver.quit();
    }

    @Test
    public void loginTest() throws IOException {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        loginPage.inputUsername(ConfProperties.getProperty("username"));
        loginPage.inputPassword(ConfProperties.getProperty("password"));
        String titleLogpage = loginPage.getTitlePage();
        printWriter.println("--- TEST LOGIN --- ");
        printWriter.println("SignIn page title: " + titleLogpage);
        loginPage.clickLoginBtn();
        String titleTerminalPage = terminalPage.getTitlePage();
        printWriter.println("Terminal page title: " + titleTerminalPage);
        //Проверка на различие заголовков страницы
        Assertions.assertNotEquals(titleLogpage, titleTerminalPage, "Title equals!");
        terminalPage.clickProcessingQueueBtn();
        processingQueuePage.clickDatePickerFilter();
        printWriter.println("Full username: " + terminalPage.getUserName());
        processingQueuePage.clickLastMnth();
        processingQueuePage.clickOtfl();

        printWriter.println("\n--- TEST PAGINATION ---");
        printWriter.println("\n----- Date Type filter-----");

        countItems();
        clearBtn();

        //автобус
        processingQueuePage.clickDatePickerFilter();
        printWriter.println("\n----- Date & Vehicle Types filter-----");
        processingQueuePage.clickLastMnth();
        WebElement vehicleTypeButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@formcontrolname='selectedVehicleType']/div/button")));
        vehicleTypeButton.click();
        WebElement busOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[contains(text(), 'Автобус')]")));
        busOption.click();
        processingQueuePage.clickOtfl();
        //wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@class=\"mat-row cdk-row\"]"))));

        countItems();
        //ждем пока загрузится таблица
        clearBtn();

        //one page
        processingQueuePage.clickDatePickerFilter();
        printWriter.println("\n----- One Page -----");
        processingQueuePage.clickLastMnth();
        vehicleTypeButton.click();
        WebElement noType = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[contains(text(), 'Неизвестный тип ТС')]")));
        noType.click();
        processingQueuePage.clickOtfl();
        //wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@class=\"mat-row cdk-row\"]"))));
        countItems();
        //ждем пока загрузится таблица
        clearBtn();


        processingQueuePage.clickDatePickerFilter();
        printWriter.println("\n----- Now Day -----");
        processingQueuePage.clickNow();
        //processingQueuePage.clickOtfl();
        countItems();
        clearBtn();







        processingQueuePage.clickUserMenu();
        processingQueuePage.clickExitBtn();
        printWriter.close();
        fileWriter.close();
    }

    public void countItems() {
        //WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        int sum = 0;
        // try {
        WebElement countPagesEl;

        List<WebElement> numbrs = driver.findElements(By.xpath("//*[@class='page_numbers']/*[starts-with(@class, 'page_num')]"));
        System.out.println(numbrs.size());
        if (numbrs.size() > 2) {
            countPagesEl = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("last_page")));


        } else if (numbrs.size() == 2) {
            countPagesEl = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("next_page")));
        } else if (numbrs.isEmpty()){
            String str = driver.findElement(By.xpath("//div[@class=\"no_data_row mat-no-data-row\"]")).getText();
            printWriter.println(str);
            return;
        } else {
            countPagesEl = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("current_page")));

        }


        int countPages = Integer.parseInt(countPagesEl.getText());
        printWriter.println("Количество страниц: " + countPages);


        if (!numbrs.isEmpty()) {
            // System.out.println(numbrs.toString());
            for (int i = 1; i < (countPages+1); i++) {
                String xpathExpression = "//*[@class='mat-row cdk-row']";
                System.out.println("Pages: " + i);
                WebElement stringTable = driver.findElement(By.xpath("//*[@class='mat-row cdk-row']"));
                stringTable.getText();
                //wait.until(!(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='current_page' and contains(text(), '" +  + "')]"))));

                wait.until(ExpectedConditions.visibilityOfElementLocated((By.xpath("//*[@class='mat-row cdk-row']"))));

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='current_page' and contains(text(), '" + i + "')]")));
                System.out.println("Current page: " + driver.findElement(By.xpath("//*[@id='current_page']")).getText());


                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(xpathExpression)));

                List<WebElement> tableElements2 = driver.findElements(By.xpath("//*[@class='mat-row cdk-row']"));
                sum += tableElements2.size();
                System.out.println(tableElements2.size() + "   " + driver.findElement(By.xpath("//*[@id='current_page']")).getText());
                if (!driver.findElements(By.xpath("//*[@id='next_page']")).isEmpty()) {
                    WebElement next = driver.findElement(By.xpath("//*[@id='next_page']"));
                    System.out.println(next.getText());
                    next.click();
                    wait.until(ExpectedConditions.invisibilityOf(stringTable));
                }

            }



            printWriter.println("Всего строк в таблице: " + sum);
        } else {
            String str = driver.findElement(By.xpath("//div[@class=\"no_data_row mat-no-data-row\"]")).getText();
            printWriter.println(str);
        }
            /*

            if (countPagesEl.getAttribute("id").equals("last_page") || countPagesEl.getAttribute("id").equals("next_page")) {
                System.out.println(countPagesEl.getText());
                countPagesEl.click();

                String xpathExpression = "//*[@class='mat-row cdk-row']";
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='current_page' and contains(text(), '"+countPages+"')]")));
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(xpathExpression)));
                List<WebElement> tableElements2 = driver.findElements(By.xpath("//*[@class='mat-row cdk-row']"));
                System.out.println(tableElements2.size());

                WebElement firstBtn = driver.findElement(By.xpath("//*[@id='first_page']"));

                firstBtn.click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='current_page' and contains(text(), 1)]")));
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(xpathExpression)));

                List<WebElement> tableElements1 = driver.findElements(By.xpath("//*[@class='mat-row cdk-row']"));
                System.out.println(tableElements1.size());


                printWriter.println("Всего строк в таблице: " + (tableElements2.size() * (countPages - 1) + tableElements1.size()));


            } else{
                String xpathExpression = "//*[@class='mat-row cdk-row']";
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='current_page' and contains(text(), 1)]")));
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(xpathExpression)));

                List<WebElement> tableElements1 = driver.findElements(By.xpath("//*[@class='mat-row cdk-row']"));
                System.out.println(tableElements1.size());


                printWriter.println("Всего строк в таблице: " + (tableElements1.size()));
            }



             */
            /*
        } catch (NoSuchElementException e) {
            String str = driver.findElement(By.xpath("//div[@class=\"no_data_row mat-no-data-row\"]")).getText();
            printWriter.println(str);
        }

             */
    }

    public void clearBtn() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        processingQueuePage.clickBtnCln();
        //ждем пока данные очистятся
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class=\"no_data_row mat-no-data-row\"]")));
        String str = driver.findElement(By.xpath("//div[@class=\"no_data_row mat-no-data-row\"]")).getText();
        printWriter.println("After clear: " + str);

    }


}
