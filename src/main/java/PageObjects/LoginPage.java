package PageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Base.BaseClassLocal;

public class LoginPage  extends BaseClassLocal{
    
    	   
    	    public LoginPage(WebDriver driver) {
    	        PageFactory.initElements(driver, this);
    	    }

    	    
    	    @FindBy(id = "username")
    	    private WebElement usernameInput;

    	    @FindBy(id = "password")
    	    private WebElement passwordInput;

    	    @FindBy(xpath =  "//i[@class='fa fa-2x fa-sign-in']")
    	    private WebElement loginButton;

    	    
    	    public void enterUsername(String username) {
    	        usernameInput.clear();
    	        usernameInput.sendKeys(username);
    	    }

    	    public void enterPassword(String password) {
    	        passwordInput.clear();
    	        passwordInput.sendKeys(password);
    	    }

    	    public void clickLogin() {
    	        loginButton.click();
    	    }

    	   
    	

    
}
