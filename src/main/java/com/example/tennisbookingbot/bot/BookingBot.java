package com.example.tennisbookingbot.bot;

import com.example.tennisbookingbot.model.Booking;

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
    protected boolean isClaySeason;

    public BookingBot(Environment environment) {
        this.environment = environment;
        System.out.println("IsDryRun: " + environment.getProperty("isDryRun", Boolean.class));
        System.out.println("IsClaySeason: " + environment.getProperty("isClaySeason", Boolean.class));
        System.out.println("Webdriver: " + environment.getProperty("webdriver.address"));
        this.isDryRun = environment.getProperty("isDryRun", Boolean.class);
        this.isClaySeason = environment.getProperty("isClaySeason", Boolean.class);
        this.chromeOptions = new ChromeOptions();
        this.chromeOptions.addArguments("--headless", "--disable-dev-shm-usage");
    }

    public abstract boolean book(Booking booking);
}
