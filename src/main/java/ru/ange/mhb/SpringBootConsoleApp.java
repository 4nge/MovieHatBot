package ru.ange.mhb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import ru.ange.mhb.bot.MovieHatBot;


@SpringBootApplication(scanBasePackages = { "ru.ange.mhb" })
public class SpringBootConsoleApp implements CommandLineRunner {

    @Autowired
    private MovieHatBot movieHatBot;

    static {
        ApiContextInitializer.init();
    }

    @Override
    public void run(String... args) throws TelegramApiRequestException {
        // Create the TelegramBotsApi object to register bot
        TelegramBotsApi botsApi = new TelegramBotsApi();
        botsApi.registerBot(movieHatBot);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(SpringBootConsoleApp.class);
        app.setBannerMode(Banner.Mode.OFF); // don't show the spring logo
        app.run(args);
    }
}
