package ru.ange.mhb.service;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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

    public enum LanguageCode {
        RU, EN
    };

    private static final Map<LanguageCode, String> LANGUGAGE_PROP = new HashMap<>();
    static {
        LANGUGAGE_PROP.put(LanguageCode.RU, "label_ru");
        LANGUGAGE_PROP.put(LanguageCode.EN, "label_en");
    }

    private static final String END_POINT = "https://query.wikidata.org/sparql";

    public static final String QUERY_PREFIX =
            "PREFIX bd: <http://www.bigdata.com/rdf#>" +
            "PREFIX wikibase: <http://wikiba.se/ontology#>" +
            "PREFIX wdt: <http://www.wikidata.org/prop/direct/>" +
            "PREFIX wd: <http://www.wikidata.org/entity/>" +
            "PREFIX schema: <http://schema.org/>" +
            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
            "PREFIX hist: <http://wikiba.se/history/ontology#>" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>";

    private String addPrefix(String query) {
        return QUERY_PREFIX + query;
    }

    private QuerySolution queryForRow(String queryStr) {
        QueryExecution qexec = null;
        try {
            qexec = QueryExecutionFactory.sparqlService(END_POINT, queryStr);
            ResultSet rs = qexec.execSelect();
            if (rs.hasNext()) {
                QuerySolution soln = rs.nextSolution();
                return soln;
            }
            return null;
        } finally {
            qexec.close();
        }
    }

    private Literal queryForLiteral(String queryStr, String label) {
        QuerySolution soln = queryForRow(queryStr);
        Literal literal = soln.getLiteral(label);
        return literal;
    }

    private Resource queryForResource(String queryStr, String label) {
        QuerySolution soln = queryForRow(queryStr);
        Resource resource = soln.getResource(label);
        return resource;
    }


    private static final String PERSON_BY_NAME_QUERY_PTT =
            "SELECT ?human ?label_en ?label_ru WHERE { " +
            "  ?human wdt:P31 wd:Q5; " +
            "    wdt:P106 wd:%s; " +
            "    ?label \"%s\"@en; " +
            "    rdfs:label ?label_en. " +
            "  FILTER((LANG(?label_en)) = \"en\") " +
            "  ?human rdfs:label ?label_ru. " +
            "  FILTER((LANG(?label_ru)) = \"ru\") " +
            "  SERVICE wikibase:label { bd:serviceParam wikibase:language \"ru\". } " +
            "} LIMIT 1";

    private  String getPersonName(String query,LanguageCode locale) {
        String label = LANGUGAGE_PROP.get(locale);
        try {
            Literal literal = queryForLiteral(query, label);
            String res = (String) literal.getValue();
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

    public String getLocalePersonName(String name, Profession profession, LanguageCode locale) {
        String query = String.format(addPrefix(PERSON_BY_NAME_QUERY_PTT), PROFESSIONS_PROP.get(profession), name);
        return getPersonName(query, locale);
    }

    private static final String PAGE_URL_BY_NAME_QUERY_PTT =
            "SELECT ?item ?sitelinkRu " +
            "WHERE { " +
            "  ?item wdt:P31 wd:Q11424. " +
            "  ?item ?label \"%s\"@en. " +
            "  ?sitelinkRu schema:about ?item; schema:isPartOf <https://ru.wikipedia.org/> " +
            "  SERVICE wikibase:label { bd:serviceParam wikibase:language \"ru\". } " +
            "} LIMIT 1";

    public String getMovieLink(String name) {
        try {
            String query = String.format(addPrefix(PAGE_URL_BY_NAME_QUERY_PTT), name);
            Resource resource = queryForResource(query, "sitelinkRu");
            return resource.getURI();
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static void main(String[] args) {
//        WikipediaService ws = new WikipediaService();
//        String like = "Christopher Nolan";
//        String name = ws.getLocalePersonName(like, Profession.DIRECTOR, LanguageCode.RU);
//        System.out.println("name = " + name);

//        String like = "Home Alone";
//        String url = ws.getMovieLink(like);
//        System.out.println("url = " + url);
    }
}
