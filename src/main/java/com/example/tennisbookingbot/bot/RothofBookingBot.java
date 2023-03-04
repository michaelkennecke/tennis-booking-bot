package com.example.tennisbookingbot.bot;

import com.example.tennisbookingbot.model.Booking;
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
    private final String ROTHOF_BOOKING_URL = "https://rothof.de/online-buchen/";

    public RothofBookingBot(Environment environment) {
        super(environment);
    }

    @Override
    public boolean book(Booking booking) {
        booking.setBookingAttempts(booking.getBookingAttempts() + 1);
        try {
            this.openRothofPage();
            this.acceptCookiesOnRothofPage();
            this.selectPlayingDate(booking);
            this.selectFreeCourt(booking);
            this.switchToOpenedEversportsTab();
            this.acceptCookiesOnEversportsPage();
            this.loginOnEversportsPage();
            this.selectPaymentMethod();
            if (!this.isDryRun) {
                this.clickBookNowButton();
            }
        } catch (WebDriverException e) {
            driver.close();
            driver.quit();
            return false;
        }
        driver.quit();
        return true;
    }

    private void openRothofPage() {
        driver = new ChromeDriver(chromeOptions);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get(ROTHOF_BOOKING_URL);
    }

    private void acceptCookiesOnRothofPage() {
        WebElement cookiesElement = driver.findElement(By.id("CookieBoxSaveButton"));
        cookiesElement.click();
    }

    private void selectPlayingDate(Booking booking) {
        driver.switchTo().frame(driver.findElement(By.tagName("iFrame")));
        WebElement datepickerElement = driver.findElement(By.id("datepicker"));
        datepickerElement.click();
        datepickerElement.clear();
        datepickerElement.sendKeys(
                booking.getLocalDateOfEvent().getDayOfMonth() + "/" +
                        booking.getLocalDateOfEvent().getMonthValue() + "/" +
                        booking.getLocalDateOfEvent().getYear()
        );
        datepickerElement.sendKeys(Keys.RETURN);
    }

    private void selectFreeCourt(Booking booking) {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//td[@data-original-title='Free | 23:00 - 00:00']")));
        WebElement courtElement = this.getFreeCourt(booking.getPreferences(), booking.getLocalDateOfEvent().toString());
        courtElement.click();
    }

    private void switchToOpenedEversportsTab() {
        wait.until(numberOfWindowsToBe(2));
        String originalWindow = driver.getWindowHandle();
        for (String windowHandle : driver.getWindowHandles()) {
            if (!originalWindow.contentEquals(windowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
    }

    private void acceptCookiesOnEversportsPage() {
        WebElement cookies = driver.findElement(By.xpath("//button[@data-testid='accept-all-cookies']"));
        cookies.click();
    }

    private void loginOnEversportsPage() {
        this.enterEmailOnEversportsLogin();
        this.enterPasswordOnEversportsLogin();
        this.clickLoginButtonOnEversportsPage();
    }

    private void enterEmailOnEversportsLogin() {
        WebElement emailElement = driver.findElement(By.name("email"));
        emailElement.sendKeys(environment.getProperty("eversports.username"));
    }

    private void enterPasswordOnEversportsLogin() {
        WebElement passwordElement = driver.findElement(By.name("password"));
        passwordElement.sendKeys(environment.getProperty("eversports.password"));
    }

    private void clickLoginButtonOnEversportsPage() {
        WebElement loginElement = driver.findElement(By.xpath("//button[@data-testid='login']"));
        loginElement.click();
    }

    private void selectPaymentMethod() {
        WebElement paymentElement = wait.until(webDriver -> webDriver.findElement(By.xpath("//button[@data-testid='continue']")));
        paymentElement.click();
    }

    private void clickBookNowButton() {
        WebElement bookNowElement = wait.until(webDriver -> webDriver.findElement(By.xpath("//button[@data-testid='continue-with-cash']")));
        bookNowElement.click();
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
        throw new WebDriverException();
    }

    private String timeAsText(int time) {
        if (time < 10) {
            return "0" + time + ":00";
        } else {
            return time + ":00";
        }
    }
}
