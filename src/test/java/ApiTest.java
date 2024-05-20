import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.AllureLifecycle;
import org.junit.jupiter.api.AfterAll;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;


import java.util.Date;

import static io.restassured.RestAssured.given;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class ApiTest {

    private AllureLifecycle allureLifecycle;
    public static LoginPage loginPage;
    public static ProcessingQueuePage processingQueuePage;
    public static TerminalPage terminalPage;
    public static WebDriver driver;
    public static WebDriverWait wait;
    public static ChromeOptions options;
    public static FileWriter fileWriter;
    public static PrintWriter printWriter;


    @BeforeClass
    @Parameters({"browser"})
    public void setup(String browser) throws IOException {
        RestAssured.baseURI = "https://devcs.avtodoria.ru/terminal/api/rest/preprocessing/video";
        fileWriter = new FileWriter("src/test/java/output3.txt", true);
        printWriter = new PrintWriter(fileWriter);
        System.setProperty("webdriver.chrome.driver", ConfProperties.getProperty("chromedriver"));
        System.setProperty("webdriver.gecko.driver", ConfProperties.getProperty("moziladriver"));
        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
            printWriter.println("\n----- TEST CHROME BROWSER -----");
            driver.manage().window().maximize();

        } else if (browser.equalsIgnoreCase("firefox")) {

            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
            printWriter.println("\n----- TEST FIREFOX BROWSER -----");
            driver.manage().window().fullscreen();


        } else if (browser.equalsIgnoreCase("safari")) {

            WebDriverManager.safaridriver().setup();
            driver = new SafariDriver();
            printWriter.println("----- TEST SAFARI BROWSER -----");
            driver.manage().window().maximize();

        }
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        loginPage = new LoginPage(driver);
        processingQueuePage = new ProcessingQueuePage(driver);
        terminalPage = new TerminalPage(driver);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(ConfProperties.getProperty("loginpage"));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='username']")));
        loginPage.inputUsername(ConfProperties.getProperty("username"));
        loginPage.inputPassword(ConfProperties.getProperty("password"));
        loginPage.clickLoginBtn();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"grid\"]")));
        terminalPage.clickProcessingQueueBtn();


    }

    @Test
    public void loginTest() throws IOException, InterruptedException {



        processingQueuePage.clickDatePickerFilter();

        sleep(1000);

        //driver.findElement(By.xpath("/html/body/app-root/div/div/div/main/app-processing-queue-page/app-queue-filter/div/form/div/ngx-daterangepicker-material/div/div[2]/div[1]/table/thead/tr[1]/th[2]/div[1]/select")).click();
        //sleep(500);
        //driver.findElement(By.xpath("//select[@class='monthselect']/option[@value='2']")).click();

        //sleep(500);
        driver.findElement(By.xpath("//select[@class='monthselect']/option[@value='2']")).click();
        sleep(500);

        driver.findElement(By.xpath("/html/body/app-root/div/div/div/main/app-processing-queue-page/app-queue-filter/div/form/div/ngx-daterangepicker-material/div/div[2]/div[1]/table/tbody/tr[4]/td[5]")).click();
        driver.findElement(By.xpath("/html/body/app-root/div/div/div/main/app-processing-queue-page/app-queue-filter/div/form/div/ngx-daterangepicker-material/div/div[3]/div[1]/table/tbody/tr[3]/td[2]")).click();
        driver.findElement(By.xpath("/html/body/app-root/div/div/div/main/app-processing-queue-page/app-queue-filter/div/form/div/ngx-daterangepicker-material/div/div[4]/div/button")).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"filter_button\"]/button")));
        processingQueuePage.clickOtfl();
        printWriter.println("\n--- TEST PAGINATION ---");
        printWriter.println("\n----- Date Type filter-----");
        String requestBody = "{\"operatorId\":null,\"pageNumber\":0,\"sortOrder\":\"DESC\",\"dateFrom\":\"2024-03-21T21:00:00.000Z\",\"dateTo\":\"2024-06-11T20:59:00.000Z\",\"sessionId\":null,\"processingStatuses\":[],\"contractIds\":[],\"deviceIds\":[],\"pageSize\":10}";
        countItems(requestBody);
        clearBtn();

        //автобус
        processingQueuePage.clickDatePickerFilter();
        printWriter.println("\n----- Date & Vehicle Types filter-----");
        sleep(1000);
        processingQueuePage.clickLastMnth();
        WebElement vehicleTypeButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@formcontrolname='selectedVehicleType']/div/button")));
        vehicleTypeButton.click();
        WebElement busOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[contains(text(), 'Автобус')]")));
        busOption.click();
        processingQueuePage.clickOtfl();
        //wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@class=\"mat-row cdk-row\"]"))));

        countItems("{\"operatorId\":null,\"pageNumber\":0,\"sortOrder\":\"DESC\",\"dateFrom\":\"2024-04-19T21:00:00.000Z\",\"dateTo\":\"2024-05-20T20:59:59.999Z\",\"sessionId\":null,\"processingStatuses\":[],\"contractIds\":[],\"deviceIds\":[],\"vehicleTypeCode\":\"BUS\",\"pageSize\":10}");
        //ждем пока загрузится таблица
        clearBtn();
        sleep(1000);
        //one page
        processingQueuePage.clickDatePickerFilter();
        printWriter.println("\n----- One Page -----");
        processingQueuePage.clickLastMnth();
        vehicleTypeButton.click();
        WebElement noType = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[contains(text(), 'Неизвестный тип ТС')]")));
        noType.click();
        processingQueuePage.clickOtfl();
        //wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@class=\"mat-row cdk-row\"]"))));
        countItems("{\"operatorId\":null,\"pageNumber\":0,\"sortOrder\":\"DESC\",\"dateFrom\":\"2024-04-19T21:00:00.000Z\",\"dateTo\":\"2024-05-20T20:59:59.999Z\",\"sessionId\":null,\"processingStatuses\":[],\"contractIds\":[],\"deviceIds\":[],\"vehicleTypeCode\":\"UNKNOWN\",\"pageSize\":10}");
        //ждем пока загрузится таблица
        clearBtn();
        processingQueuePage.clickDatePickerFilter();
        printWriter.println("\n----- Now Day -----");
        sleep(1000);
        processingQueuePage.clickNow();
        processingQueuePage.clickOtfl();
        countItems("{\"operatorId\":null,\"pageNumber\":0,\"sortOrder\":\"DESC\",\"dateFrom\":\"2024-05-19T21:00:00.000Z\",\"dateTo\":\"2024-05-20T20:59:59.999Z\",\"sessionId\":null,\"processingStatuses\":[],\"contractIds\":[],\"deviceIds\":[],\"vehicleTypeCode\":\"UNKNOWN\",\"pageSize\":10}");
        clearBtn();
        sleep(1000);
        processingQueuePage.clickUserMenu();
        processingQueuePage.clickExitBtn();
        printWriter.close();
        fileWriter.close();
        driver.close();


    }



    @Test
    public void testAPI0Page() {
        String requestBody = "{\"operatorId\":null,\"pageNumber\":0,\"sortOrder\":\"DESC\",\"dateFrom\":\"2024-04-21T21:00:00.000Z\",\"dateTo\":\"2024-04-22T20:59:00.000Z\",\"sessionId\":null,\"processingStatuses\":[],\"contractIds\":[],\"deviceIds\":[],\"pageSize\":10}";
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post("/all")
                .then()
                .extract().response();
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals("[]", response.getBody().asString());
    }



    public void countItems(String requestBody) {
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
        } else if (numbrs.isEmpty()) {
            String str = driver.findElement(By.xpath("//div[@class=\"no_data_row mat-no-data-row\"]")).getText();
            printWriter.println(str);
            System.out.println("Всего строк в таблице: " + sum);
            equalCount(sum, requestBody);

            return;
        } else {
            countPagesEl = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("current_page")));

        }

        int countPages = Integer.parseInt(countPagesEl.getText());
        printWriter.println("Количество страниц: " + countPages);


        if (!numbrs.isEmpty()) {
            // System.out.println(numbrs.toString());
            for (int i = 1; i < (countPages + 1); i++) {
                String xpathExpression = "//*[@class='mat-row cdk-row']";
                System.out.println("Pages: " + i);
                WebElement stringTable = driver.findElement(By.xpath("//*[@class='mat-row cdk-row']"));
                stringTable.getText();
                //wait.until(!(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='current_page' and contains(text(), '" +  + "')]"))));

                wait.until(ExpectedConditions.visibilityOfElementLocated((By.xpath("//*[@class='mat-row cdk-row']"))));

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='current_page' and contains(text(), '" + i + "')]")));
                //System.out.println("Current page: " + driver.findElement(By.xpath("//*[@id='current_page']")).getText());


                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(xpathExpression)));

                List<WebElement> tableElements2 = driver.findElements(By.xpath("//*[@class='mat-row cdk-row']"));
                sum += tableElements2.size();
                //System.out.println(tableElements2.size() + "   " + driver.findElement(By.xpath("//*[@id='current_page']")).getText());
                if (!driver.findElements(By.xpath("//*[@id='next_page']")).isEmpty()) {
                    WebElement next = driver.findElement(By.xpath("//*[@id='next_page']"));
                    System.out.println(next.getText());
                    next.click();
                    wait.until(ExpectedConditions.invisibilityOf(stringTable));
                }

            }


            printWriter.println("Всего строк в таблице: " + sum);
            System.out.println("Всего строк в таблице: " + sum);

            equalCount(sum, requestBody);
        } else {
            String str = driver.findElement(By.xpath("//div[@class=\"no_data_row mat-no-data-row\"]")).getText();
            printWriter.println(str);
        }

    }
    public void equalCount(int expected, String body) {

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post("/count")
                .then()
                .extract().response();
        int count = Integer.parseInt(response.body().asString());
        System.out.println("API Count: " + count);
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(expected, count);

    }


    public void clearBtn() throws InterruptedException {

        // wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='clear_button']/button")));
        processingQueuePage.clickBtnCln();
        //ждем пока данные очистятся
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class=\"no_data_row mat-no-data-row\"]")));
        String str = driver.findElement(By.xpath("//div[@class=\"no_data_row mat-no-data-row\"]")).getText();
        printWriter.println("After clear: " + str);


    }
    @Test
    public void testAPI2Page() {
        String requestBody = "{\"operatorId\":null,\"pageNumber\":0,\"sortOrder\":\"DESC\",\"dateFrom\":\"2024-03-20T21:00:00.000Z\",\"dateTo\":\"2024-03-23T20:59:00.000Z\",\"sessionId\":null,\"processingStatuses\":[],\"contractIds\":[],\"deviceIds\":[],\"pageSize\":10}";
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post("/all")
                .then()
                .extract().response();
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals("test", response.jsonPath().get("[0].descriptions.onDevice"));
    }

    @Test
    public void testAPI1Page() {
        String requestBody = "{\"operatorId\":null,\"pageNumber\":0,\"sortOrder\":\"DESC\",\"dateFrom\":\"2024-04-19T21:00:00.000Z\",\"dateTo\":\"2024-05-20T20:59:59.999Z\",\"sessionId\":null,\"processingStatuses\":[],\"contractIds\":[],\"deviceIds\":[],\"vehicleTypeCode\":\"BUS\",\"pageSize\":10}";
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post("/all")
                .then()
                .extract().response();
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals("dina10", response.jsonPath().get("[0].descriptions.onDevice"));
    }


    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @AfterClass
    public static void tearDown(){
        //processingQueuePage.clickExitBtn();
        driver.quit();
    }






}
