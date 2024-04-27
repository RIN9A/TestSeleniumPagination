import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class TerminalPage {

    public WebDriver driver;

    public TerminalPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    @FindBy(xpath="//*[@id=\"grid\"]")
    private WebElement processingQueueBtn;


    //@FindBy(xpath = "//*[@class,\" user_fullname\"]")
    //private WebElement username;



    @FindBy(xpath = "//div[@class=\"top_panel_container\"]/app-user-menu/app-user-info/p")
    private WebElement username;


    public String getTitlePage() {
        return this.driver.getTitle();
    }

    public void clickProcessingQueueBtn(){
        processingQueueBtn.click();
    }


    //public void clickMenu() {
      //  userMenu.click();
    //}

    public String getUserName() {
        return username.getText();
    }


}
