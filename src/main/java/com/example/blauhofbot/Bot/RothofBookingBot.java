package com.example.blauhofbot.Bot;

import com.example.blauhofbot.Model.Booking;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.numberOfWindowsToBe;

@Component
public class RothofBookingBot extends BookingBot {

    public RothofBookingBot(Environment environment) {
        super(environment);
    }

    @Override
    public boolean book(Booking booking) {
        booking.setBookingAttempts(booking.getBookingAttempts() + 1);
        try {
            driver = new ChromeDriver(chromeOptions);
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            driver.get("https://rothof.de/online-buchen/");

            // Accept cookies
            WebElement cookiesElement = driver.findElement(By.id("CookieBoxSaveButton"));
            cookiesElement.click();

            // Switch to iFrame
            driver.switchTo().frame(driver.findElement(By.tagName("iFrame")));

            // Click on datepicker and enter the playing date
            WebElement datepickerElement = driver.findElement(By.id("datepicker"));
            datepickerElement.click();
            datepickerElement.clear();
            datepickerElement.sendKeys(
                    booking.getLocalDateOfEvent().getDayOfMonth() + "/" +
                            booking.getLocalDateOfEvent().getMonthValue() + "/" +
                            booking.getLocalDateOfEvent().getYear()
            );
            datepickerElement.sendKeys(Keys.RETURN);

            // Wait until booking table is visible Select
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//td[@data-original-title='Free | 23:00 - 00:00']")));

            // Book the next free court at the specified time
            WebElement courtElement = this.getFreeCourt(booking.getPreferences(), booking.getLocalDateOfEvent().toString());
            courtElement.click();

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
            // Accept all cookies
            WebElement cookies = driver.findElement(By.xpath("//button[@data-testid='accept-all-cookies']"));
            // wait.until(webDriver -> webDriver.findElement())
            cookies.click();

            // Enter email
            WebElement emailElement = driver.findElement(By.name("email"));
            emailElement.sendKeys(environment.getProperty("eversports.username"));

            // Enter password
            WebElement passwordElement = driver.findElement(By.name("password"));
            passwordElement.sendKeys(environment.getProperty("eversports.password"));

            // Click on login button
            WebElement loginElement = driver.findElement(By.xpath("//button[@data-testid='login']"));
            loginElement.click();

            // Click on select payment method button
            WebElement paymentElement = wait.until(webDriver -> webDriver.findElement(By.xpath("//button[@data-testid='continue']")));
            paymentElement.click();

            // Click on book now button
            WebElement bookNowElement = wait.until(webDriver -> webDriver.findElement(By.xpath("//button[@data-testid='continue-with-cash']")));
            bookNowElement.click();

            // Close browser
            driver.quit();
            return true;
        } catch (Exception e) {
            // Close browser
            driver.close();
            driver.quit();
            return false;
        }
    }

    private WebElement getFreeCourt(List<LocalTime> preferences, String usDateFormatOEventDate) {
        for (var preference : preferences) {
            int startTime = preference.getHour();
            int endTime = startTime + 1;
            WebElement courtElement;
            try {
                courtElement = driver.findElement(By.xpath(String.format("//td[contains(@data-date, '%s') and contains (@data-original-title, 'Free | %s - %s')]", usDateFormatOEventDate, this.timeAsText(startTime), this.timeAsText(endTime))));
            } catch (WebDriverException e) {
                continue;
            }
            if (courtElement.isDisplayed()) {
                return courtElement;
            }
        }
        return null;
    }

    private String timeAsText(int time) {
        if (time < 10) {
            return "0" + time + ":00";
        } else {
            return time + ":00";
        }
    }
}
