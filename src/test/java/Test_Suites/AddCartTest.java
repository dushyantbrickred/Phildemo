package Test_Suites;

import dataManage.DataSource;
import net.serenitybdd.core.Serenity;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class AddCartTest {

    private WebDriver _driver = null;
    DataSource dataSource = new DataSource();



    @Test
    public void loginTest(){
        selectItemIntoCart(4);
        verifyYourCartAndAddedItems(4);
    }


    public void selectItemIntoCart(int numberOfItemsTobeAdded){
        Random rn = new Random();
        for(int i=1; i <= numberOfItemsTobeAdded; i++){
            List<WebElement> items = _driver.findElements(By.xpath("//*[contains(@id, 'add-to-cart')]"));
            int random = rn.nextInt(items.size() + 1);
            if(random == 0)random++;
            String itemName = _driver.findElement(By.xpath("(//*[contains(@id, 'add-to-cart')])[" + (random) + "]//parent::div//parent::div//*[contains(@class, 'inventory_item_name')]")).getText();
            Serenity.getCurrentSession().put("ItemName"+i, itemName);
            System.out.println(itemName);
            items.get(random -1).click();
        }
          _driver.findElement(By.xpath("//*[contains(@class, 'shopping_cart_link')]")).click();
    }

    public void verifyYourCartAndAddedItems(int numberOfItemsTobeVerified){
        String itemName = _driver.findElement(By.xpath("//*[contains(text(),'Your Cart')]")).getText();
        Assert.assertEquals("YOUR CART", itemName.trim());

        for(int x =1; x<=numberOfItemsTobeVerified; x++){
            List<WebElement> list = _driver.findElements(By.xpath("//*[contains(text(),'" + Serenity.getCurrentSession().get("ItemName"+x).toString() + "')]"));
            Assert.assertTrue("Item not found!- "+ Serenity.getCurrentSession().get("ItemName"+x).toString(), list.size() > 0);
        }
    }

    @Before
    public void InitializeAndLogin(){
        System.setProperty("webdriver.edge.driver", System.getProperty("user.dir") + dataSource.edgePath);
        System.setProperty("webdriver.edge.verboseLogging", "false");
        _driver = new EdgeDriver( );
        _driver.get(dataSource.url);
        _driver.manage().window().maximize();
        _driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        _driver.findElement(By.name("user-name")).sendKeys(dataSource.username);
        _driver.findElement(By.name("password")).sendKeys(dataSource.password);
        _driver.findElement(By.name("login-button")).click();
    }
    @After
    public void teardown(){
        _driver.quit();
    }
}
