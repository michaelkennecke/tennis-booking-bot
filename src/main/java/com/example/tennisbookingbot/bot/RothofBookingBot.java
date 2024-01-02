package com.example.tennisbookingbot.bot;

import com.example.tennisbookingbot.model.Booking;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.remote.RemoteWebDriver;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.net.MalformedURLException;
import java.net.URL;

@Component
public class RothofBookingBot extends BookingBot {
    private static final String ROTHOF_BOOKING_URL = "https://eversports.de/widget/w/jmjffr";
    private static final String[] CLAY_COURTS = { "44643", "44644", "44645", "44646", "44647", "44648", "44649",
            "44650",
            "44651", "44652", "44653", "44654" };

    public RothofBookingBot(Environment environment) {
        super(environment);
    }

    @Override
    public boolean book(Booking booking) {
        try {
            this.bookRothofTennisCourt(booking);
        } catch (WebDriverException e) {
            driver.close();
            driver.quit();
            return false;
        }
        driver.quit();
        return true;
    }

    private void bookRothofTennisCourt(Booking booking) throws WebDriverException {
        booking.setBookingAttempts(booking.getBookingAttempts() + 1);
        this.startWebdriver();
        this.openBookingPage();
        this.selectPlayingDate(booking);
        this.selectFreeCourt(booking);
        this.acceptCookiesOnEversportsPage();
        this.loginOnEversportsPage();
        this.selectPaymentMethod();
        if (!this.isDryRun) {
            this.clickBookNowButton();
        }
    }

    private void startWebdriver() {
        try {
            this.driver = new RemoteWebDriver(new URL(environment.getProperty("webdriver.address", String.class)),
                    this.chromeOptions);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void openBookingPage() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get(RothofBookingBot.ROTHOF_BOOKING_URL);
    }

    private void selectPlayingDate(Booking booking) {
        WebElement datepickerElement = driver.findElement(By.id("datepicker"));
        datepickerElement.click();
        datepickerElement.clear();
        datepickerElement.sendKeys(
                booking.getLocalDateOfEvent().getDayOfMonth() + "/" +
                        booking.getLocalDateOfEvent().getMonthValue() + "/" +
                        booking.getLocalDateOfEvent().getYear());
        datepickerElement.sendKeys(Keys.RETURN);
    }

    private void selectFreeCourt(Booking booking) {
        wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//td[@data-original-title='Free | 23:00 - 00:00']")));
        WebElement courtElement = this.getFreeCourt(booking.getPreferences(), booking.getLocalDateOfEvent().toString());
        courtElement.click();
    }

    private void acceptCookiesOnEversportsPage() {
        wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[@data-testid='accept-all-cookies']")));
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
        WebElement paymentElement = wait
                .until(webDriver -> webDriver.findElement(By.xpath("//button[@data-testid='continue']")));
        paymentElement.click();
    }

    private void clickBookNowButton() {
        WebElement bookNowElement = wait
                .until(webDriver -> webDriver.findElement(By.xpath("//button[@data-testid='continue-with-cash']")));
        bookNowElement.click();
    }

    private WebElement getFreeCourt(List<LocalTime> preferences, String usDateFormatOEventDate) {
        for (var preference : preferences) {
            int startTime = preference.getHour();
            int endTime = startTime + 1;
            WebElement courtElement;
            try {
                if (this.isClaySeason) {
                    courtElement = this.findFreeClayCourt(startTime, endTime, usDateFormatOEventDate);
                } else {
                    courtElement = driver.findElement(By.xpath(String.format(
                            "//td[contains(@data-date, '%s') and contains (@data-original-title, 'Free | %s - %s')]",
                            usDateFormatOEventDate, this.timeAsText(startTime), this.timeAsText(endTime))));
                }
            } catch (WebDriverException e) {
                continue;
            }
            if (courtElement.isDisplayed()) {
                return courtElement;
            }
        }
        throw new WebDriverException();
    }

    private WebElement findFreeClayCourt(int startTime, int endTime, String usDateFormatOEventDate) {
        WebElement courtElement;
        for (var clayCourt : RothofBookingBot.CLAY_COURTS) {
            try {
                courtElement = driver.findElement(By.xpath(String.format(
                        "//td[contains(@data-date, '%s') and contains (@data-original-title, 'Free | %s - %s') and contains (@data-court, '%s')]",
                        usDateFormatOEventDate, this.timeAsText(startTime), this.timeAsText(endTime), clayCourt)));
                return courtElement;
            } catch (WebDriverException e) {
                continue;
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
