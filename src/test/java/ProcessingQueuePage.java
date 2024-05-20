import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ProcessingQueuePage {

    public WebDriver driver;

    public ProcessingQueuePage(WebDriver driver){
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    @FindBy(id = "date_picker_input")
    private WebElement datePickerFilter;

    @FindBy(xpath ="//*[text()=\"Последний месяц\"]")
    private WebElement buttonLastMnth;

    @FindBy(xpath ="//*[text()=\"Сегодня\"]")
    private WebElement buttonNow;

    @FindBy(xpath = "//*[@class=\"btn\"]/[text()=\"Применить\"]")
    private WebElement buttonPrimenit;

    @FindBy(css = "#filter_button > button")
    private WebElement btnOtfiltrov;

    //@FindBy(className = "mat-row cdk-row")
    //private WebElement rowTable;

    @FindBy(xpath = "//*[@id=\"clear_button\"]/button")
    private WebElement btnClean;

    @FindBy(xpath = "//i[@class=\"user_push\"]")
    private WebElement userMenu;



    @FindBy(xpath = "//*[@iconclassname=\"button_exit\"]/button")
    private WebElement exitBtn;

    public void clickDatePickerFilter() {
        datePickerFilter.click();
    }

    public void clickLastMnth(){
        buttonLastMnth.click();
    }

    public void clickNow(){
        buttonNow.click();
    }

    public void clickButtonPrmenit(){
        buttonPrimenit.click();
    }

    public void clickOtfl(){
        btnOtfiltrov.click();
    }


    public void clickBtnCln() {
        btnClean.click();
    }

    public void clickUserMenu() {
        userMenu.click();
    }

    public void clickExitBtn() {
        exitBtn.click();
    }


    public WebElement getExitBtn() {
        return exitBtn;
    }

    public void setExitBtn(WebElement exitBtn) {
        this.exitBtn = exitBtn;
    }

    public WebElement getUserMenu() {
        return userMenu;
    }

    public void setUserMenu(WebElement userMenu) {
        this.userMenu = userMenu;
    }

    public WebElement getBtnClean() {
        return btnClean;
    }

    public void setBtnClean(WebElement btnClean) {
        this.btnClean = btnClean;
    }

    public WebElement getBtnOtfiltrov() {
        return btnOtfiltrov;
    }

    public void setBtnOtfiltrov(WebElement btnOtfiltrov) {
        this.btnOtfiltrov = btnOtfiltrov;
    }

    public WebElement getButtonPrimenit() {
        return buttonPrimenit;
    }

    public void setButtonPrimenit(WebElement buttonPrimenit) {
        this.buttonPrimenit = buttonPrimenit;
    }

    public WebElement getButtonNow() {
        return buttonNow;
    }

    public void setButtonNow(WebElement buttonNow) {
        this.buttonNow = buttonNow;
    }

    public WebElement getButtonLastMnth() {
        return buttonLastMnth;
    }

    public void setButtonLastMnth(WebElement buttonLastMnth) {
        this.buttonLastMnth = buttonLastMnth;
    }

    public WebElement getDatePickerFilter() {
        return datePickerFilter;
    }

    public void setDatePickerFilter(WebElement datePickerFilter) {
        this.datePickerFilter = datePickerFilter;
    }
}
