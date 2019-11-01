package ru.ange.mhb.config;


import info.movito.themoviedbapi.TmdbApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TmdbConfig {

    @Value("${tmdb.token}")
    private String token;

    @Value("${tmdb.language}")
    private String language;

    @Bean
    public TmdbApi tmdbApi() {
        return new TmdbApi( token );
    }
}
