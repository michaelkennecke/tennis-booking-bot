package com.example.blauhofbot.Service;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import org.openqa.selenium.WebDriver;

import java.time.Duration;
import java.time.LocalDate;

import static org.openqa.selenium.support.ui.ExpectedConditions.numberOfWindowsToBe;

@Service
public class BotService {

    public void bookCourt(int startTime, LocalDate bookingDate) throws InterruptedException {
        // Setup webdriver
        System.setProperty("webdriver.chrome.driver", "/home/michael/dev/python/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.get("https://rothof.de/online-buchen/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Accept cookies
        WebElement cookiesElement = driver.findElement(By.id("CookieBoxSaveButton"));
        cookiesElement.click();

        // Switch to iFrame
        driver.switchTo().frame(driver.findElement(By.tagName("iFrame")));

        // Click on datepicker
        WebElement datepickerElement = driver.findElement(By.id("datepicker"));
        datepickerElement.click();

        // Click on the correct date in the datepicker
        int bookingDay = this.calculatePlayDate(bookingDate).getDayOfMonth();
        WebElement bookingDateElement = driver.findElement(By.linkText(String.valueOf(bookingDay)));
        bookingDateElement.click();

        // Select the next free court at the specified time
        Thread.sleep(2000); // TODO: change waiting time
        this.selectFreeCourt(driver, startTime);

        // Wait until new tab has been loaded
        wait.until(numberOfWindowsToBe(2));

        // Switch to the newly opened tab
        String originalWindow = driver.getWindowHandle();
        for (String windowHandle : driver.getWindowHandles()) {
            if (!originalWindow.contentEquals(windowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }

        // Enter email
        WebElement emailElement = driver.findElement(By.name("email"));
        emailElement.sendKeys("mkennecke@icloud.com");

        // Enter password
        WebElement passwordElement = driver.findElement(By.name("password"));
        passwordElement.sendKeys("Pao8`4[Vz6DPgd%dt^[E2");

        // Click on login button
        WebElement loginElement = driver.findElement(By.xpath("//button[@data-testid='login']"));
        loginElement.click();

        // Click on select payment method button
        WebElement paymentElement = wait.until(webDriver -> webDriver.findElement(By.xpath("//button[@data-testid='continue']")));
        paymentElement.click();

        // Click on book now button
        WebElement bookNowElement = wait.until(webDriver -> webDriver.findElement(By.xpath("//button[@data-testid='continue-with-cash']")));
        System.out.println(bookNowElement.getText());
        bookNowElement.click();

        // Close browser
        driver.quit();
    }

    private LocalDate calculatePlayDate(LocalDate bookingDate) {
        return bookingDate.plusDays(7);
    }

    private void selectFreeCourt(WebDriver driver, int startTime) {
        int endTime = startTime + 1;
        WebElement courtElement = driver
                .findElement(By.xpath(String.format("//td[@data-original-title='Free | %s - %s']", timeAsText(startTime), timeAsText(endTime))));
        courtElement.click();
    }

    private String timeAsText(int time) {
        if (time < 10) {
            return "0" + time + ":00";
        } else {
            return time + ":00";
        }
    }
}
