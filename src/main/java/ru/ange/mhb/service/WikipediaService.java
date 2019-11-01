package ru.ange.mhb.service;

import com.bordercloud.sparql.Endpoint;
import com.bordercloud.sparql.EndpointException;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WikipediaService {

    public enum Profession {
        DIRECTOR, ACTOR, PRODUCER, COMPOSER, PHOTOGRAPHER
    };

    private static final Map<Profession, String> PROFESSIONS_PROP = new HashMap<>();
    static {
        PROFESSIONS_PROP.put(Profession.DIRECTOR,     "Q2526255");
        PROFESSIONS_PROP.put(Profession.ACTOR,        "Q33999");
        PROFESSIONS_PROP.put(Profession.PRODUCER,     "Q3282637");
        PROFESSIONS_PROP.put(Profession.COMPOSER,     "Q1415090");
        PROFESSIONS_PROP.put(Profession.PHOTOGRAPHER, "Q33231");
    }

    private static final String END_POINT_URL = "https://query.wikidata.org/sparql";

    private static final String PERSON_QUERY_BY_EQUALS =
            "SELECT ?human ?label_en ?label_ru WHERE {\n" +
            "  ?human wdt:P31 wd:Q5;\n" +
            "    wdt:P106 wd:%s;\n" +
            "    ?label \"%s\"@en;\n" +
            "    rdfs:label ?label_en.\n" +
            "  FILTER((LANG(?label_en)) = \"en\")\n" +
            "  ?human rdfs:label ?label_ru.\n" +
            "  FILTER((LANG(?label_ru)) = \"ru\")\n" +
            "  SERVICE wikibase:label { bd:serviceParam wikibase:language \"ru\". }\n" +
            "}\n" +
            "LIMIT 1";

    private static final String PERSON_QUERY_BY_REGEX =
            "SELECT ?human ?label_en ?label_ru" +
            "WHERE {" +
            "    ?human wdt:P31 wd:Q5" +
            "	; wdt:P106 wd:%s ." +
            "    ?human rdfs:label ?label_en filter (lang(?label_en) = \"en\")." +
            "    ?human rdfs:label ?label_ru filter (lang(?label_ru) = \"ru\")." +
            "    FILTER(REGEX(STR(?label_en), \"%s\"))" +
            //"    FILTER(REGEX(STR(?label_en), \"Christopher[.,!?:… ].*Nolan\"))" +
            "    SERVICE wikibase:label { bd:serviceParam wikibase:language \"ru\". }" +
            "}" +
            "LIMIT 1";


    private static final String PAGE_URL_QUERY_BY_REGEX =
            "SELECT ?item ?label_en ?label_ru ?sitelinkRu" +
            "WHERE {" +
            "  ?item ?label \"%s\"@en." +
            "  ?sitelinkRu schema:about ?item; schema:isPartOf <https://ru.wikipedia.org/>" +
            "  ?item rdfs:label ?label_en filter (lang(?label_en) = \"en\")." +
            "  ?item rdfs:label ?label_ru filter (lang(?label_ru) = \"ru\")." +
            "  SERVICE wikibase:label { bd:serviceParam wikibase:language \"ru\". }" +
            "}" +
            "LIMIT 1";


    public String getRussianPersonName(String enName, Profession profession) {
        try {
            String query = String.format( PERSON_QUERY_BY_EQUALS, PROFESSIONS_PROP.get(profession), enName );
            String res = getParameterFromWiki( query, "label_ru" );

            if (res.contains("(") && res.contains(")")) {
                int startIdx = res.indexOf("(");
                int endIdx = res.indexOf(")");
                res = res.substring(0, startIdx) + res.substring(endIdx+1);
            }

            return res.replace(",", "");

        } catch (NullPointerException | IndexOutOfBoundsException e) {
            return null;
        }

    }

    private HashMap<String, HashMap> retrieveData(String query) throws EndpointException {
        Endpoint sp = new Endpoint(END_POINT_URL, true);
        HashMap<String, HashMap> rs = sp.query( query );
        return rs;
    }

    private String getParameterFromWiki(String query, String parName) {
        try {
            HashMap<String, HashMap> data = retrieveData(query);
            List<HashMap> rows = (ArrayList<HashMap>) data.get("result").get("rows");
            HashMap<String, Object> row = rows.get(0);
            return (String) row.get(parName);
        } catch (EndpointException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }


    public String getMovieLink(String name) {
        String query = String.format( PAGE_URL_QUERY_BY_REGEX, name );
        return getParameterFromWiki( query, "sitelinkRu" );
    }

    /*

    public String getLocalizedName(String query) {
        String subject = "Ed Sheeran";

        try {
            URL url = new URL("https://ru.wikipedia.org/w/api.php?action=query&uselang=user&prop=extracts&format=json&exsentences=1&exintro=&explaintext=&exsectionformat=plain&titles="
                    + subject.replace(" ", "%20"));
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));

            String text = "";
            String line = null;
            while (null != (line = br.readLine())) {
                line = line.trim();
                if (true) {
                    text += line;
                }
            }

            System.out.println("text = " + text);

            return text;


        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }


//        }) {
//
//            String text = "";
//
//            String line = null;
//            while (null != (line = br.readLine())) {
//                line = line.trim();
//                if (true) {
//                    text += line;
//                }
//            }
//
//            System.out.println("text = " + text);
//            JSONObject json = new JSONObject(text);
//            JSONObject query = json.getJSONObject("query");
//            JSONObject pages = query.getJSONObject("pages");
//            for(String key: pages.keySet()) {
//                System.out.println("key = " + key);
//                JSONObject page = pages.getJSONObject(key);
//                String extract = page.getString("extract");
//                System.out.println("extract = " + extract);
//            }
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
        //}
    }


    public String getArticleTitle(String url) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openConnection().getInputStream()));

            String text = "";
            String line = null;
            while (null != (line = br.readLine())) {
                line = line.trim();
                if (true) {
                    text += line;
                }
            }

            System.out.println("text = " + text);

            int startIdx = text.indexOf("<title>") + "<title>".length();
            int endIdx = text.indexOf("</title>");

            if (startIdx > 0 && endIdx > 0 && startIdx < endIdx)
                return text.substring(startIdx, endIdx).replace("— Википедия", "").trim();
            else
                return null;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public String getPersonName(String url, Profession profession) {
        // TODO более жлегантное закшлядывае в блок проффесий
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openConnection().getInputStream()));
            String text = "";
            String line = null;
            while (null != (line = br.readLine())) {
                line = line.trim();
                if (true) {
                    text += line;
                }
            }

            if (text.contains(professionsLinks.get(profession))) {
                int startIdx = text.indexOf("<title>") + "<title>".length();
                int endIdx = text.indexOf("</title>");

                if (startIdx > 0 && endIdx > 0 && startIdx < endIdx)
                    return text.substring(startIdx, endIdx).replace("— Википедия", "").trim();

            }

            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    */
}
