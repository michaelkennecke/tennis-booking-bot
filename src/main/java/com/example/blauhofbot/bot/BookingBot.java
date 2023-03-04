package com.example.blauhofbot.bot;

import com.example.blauhofbot.model.Booking;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.core.env.Environment;

public abstract class BookingBot {
    protected Environment environment;
    protected WebDriver driver;
    protected ChromeOptions chromeOptions;
    protected WebDriverWait wait;
    protected boolean isDryRun;

    public BookingBot(Environment environment) {
        this.environment = environment;
        System.setProperty("webdriver.chrome.driver", environment.getProperty("chromium.webdriver.path"));
        this.chromeOptions = new ChromeOptions();
        this.isDryRun = environment.getProperty("isDryRun", Boolean.class);
        if (!this.isDryRun) {
            this.chromeOptions.addArguments("--headless");
        }
    }

    public abstract boolean book(Booking booking);
}
