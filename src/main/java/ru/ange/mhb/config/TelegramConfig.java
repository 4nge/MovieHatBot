package ru.ange.mhb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import ru.ange.mhb.bot.MovieHatBot;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Map;

@Configuration
public class TelegramConfig {

    @Value("${telegram.proxy.custom.host}")
    private String customProxyHost;

    @Value("${telegram.proxy.custom.port}")
    private int customProxyPort;

    @Value("${telegram.proxy.custom.user}")
    private String customProxyUser;

    @Value("${telegram.proxy.custom.password}")
    private String customProxyPass;

    @Value("${telegram.proxy.rest.api.url}")
    private String restProxyUrl;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.name}")
    private String botName;



    @Autowired
    private RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    private void authenticate(String user, String pass) {
        // Create the Authenticator that will return auth's parameters for proxy authentication
        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication( user, pass.toCharArray() );
            }
        });
    }

    private DefaultBotOptions getDefaultOptions(String host, int port) {
        DefaultBotOptions botOptions = ApiContext.getInstance( DefaultBotOptions.class );
        botOptions.setProxyType( DefaultBotOptions.ProxyType.SOCKS5 );
        botOptions.setProxyHost( host );
        botOptions.setProxyPort( port );
        return botOptions;
    }


    private DefaultBotOptions getCustomProxy() {
        try {
            if (customProxyUser.isEmpty() && customProxyPass.isEmpty()) {
                authenticate(customProxyUser, customProxyPass);
            }
            return getDefaultOptions(customProxyHost, customProxyPort);
        } catch (Exception e) {
            return null;
        }
    }

    private DefaultBotOptions getRestProxy() {
        try {
            ResponseEntity<Map> entity = restTemplate.getForEntity(restProxyUrl, Map.class);
            Map body = entity.getBody();
            String host = (String) body.get("ip");
            int port = (int) body.get("port");

            return getDefaultOptions(host, port);

        } catch (RestClientException | NullPointerException e) {
            return null;
        }
    }

    @Bean
    public DefaultBotOptions botOptions() {

        // try connect via custom proxy
        DefaultBotOptions customOptions = getCustomProxy();
        if (customOptions != null) {
            return customOptions;
        }

        // try connect via proxy from rest service api
        DefaultBotOptions proxyOptions = getRestProxy();
        if (proxyOptions != null) {
            return proxyOptions;
        }

        // return empty DefaultBotOptions
        return ApiContext.getInstance(DefaultBotOptions.class);
    }


    @Bean
    public MovieHatBot movieHatBot() {
        return new MovieHatBot( botToken, botName, botOptions() );
    }
}
