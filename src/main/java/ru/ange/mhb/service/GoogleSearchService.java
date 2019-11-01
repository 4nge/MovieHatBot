package ru.ange.mhb.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.CustomsearchRequestInitializer;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoogleSearchService {

    private static final String APP_NAME = "MovieHatBot";
    private static final String API_KEY = "AIzaSyAZRGc42pOFBhuEM-vssa5X8bnWufM3U6U";

    private static final String KP_SEARCH_ENGINE = "015723958389398585317:e5a5fbrey4b";
    private static final String WIKI_DIRECTOR_SEARCH_ENGINE = "015723958389398585317:rsgqtaefxe8";

    public List<String> getKpLinks(String query)  {
        return getLinks(query, KP_SEARCH_ENGINE);
    }

    public List<String> getWikiLinks(String query)  {
        return getLinks(query, WIKI_DIRECTOR_SEARCH_ENGINE);
    }

    private List<String> getLinks(String query, String searchEngine)  {
        try {
            List<String> links = new ArrayList<>();

            //Instance Customsearch
            Customsearch cs = new Customsearch.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(), null)
                    .setApplicationName(APP_NAME)
                    .setGoogleClientRequestInitializer(new CustomsearchRequestInitializer(API_KEY))
                    .build();

            //Set search parameter
            Customsearch.Cse.List list = cs.cse().list(query).setCx(searchEngine);

            //Execute search
            Search result = list.execute();
            if ( result.getItems() != null ) {
                for (Result ri : result.getItems()) {
                    links.add(ri.getLink());
                }
            }
            return links;
        } catch (GeneralSecurityException | IOException e) {
            return null;
        }
    }
}
