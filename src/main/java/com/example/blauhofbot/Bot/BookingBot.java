package com.example.blauhofbot.Bot;

import com.example.blauhofbot.Model.Booking;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.core.env.Environment;

public abstract class BookingBot {
    protected Environment environment;
    protected WebDriver driver;
    protected ChromeOptions chromeOptions;
    protected WebDriverWait wait;

    public BookingBot(Environment environment) {
        this.environment = environment;
        System.setProperty("webdriver.chrome.driver", environment.getProperty("chromium.webdriver.path"));
        this.chromeOptions = new ChromeOptions();
        this.chromeOptions.addArguments("--headless");
    }

    public abstract boolean book(Booking booking);
}
