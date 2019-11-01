package ru.ange.mhb.service;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.ange.mhb.pojo.movie.KpInfo;

import java.io.IOException;
import javax.xml.parsers.*;
import org.w3c.dom.Element;

@Service
public class KinoPoiskService {

    public static final String RARING_URL_PTT = "https://rating.kinopoisk.ru/%s.xml";

    private float readDocTagValue(Element el, String tagName) {
        NodeList kp_ratingNL = el.getElementsByTagName( tagName );
        if (kp_ratingNL != null && kp_ratingNL.getLength() > 0)
            return Float.valueOf( kp_ratingNL.item(0).getTextContent() );
        else
            return 0;
    }

    public KpInfo getKPInfo(String kpLink) {
        KpInfo kpInfo = new KpInfo( getId( kpLink ), kpLink );
        String urlString = String.format(RARING_URL_PTT, kpInfo.getId());

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(urlString);
            doc.getDocumentElement().normalize();
            Element rootEl = doc.getDocumentElement();

            kpInfo.setKpRating( readDocTagValue(rootEl, "kp_rating") );
            kpInfo.setIMDbRating(readDocTagValue(rootEl, "imdb_rating"));

        } catch (SAXException | ParserConfigurationException | IOException e1) {
            e1.printStackTrace();
        }
        return kpInfo;
    }

    private int getId(String kpLink) {
        try {
            String ss = kpLink.substring(0, kpLink.length() - 1);
            if (ss.lastIndexOf("-") > 0) {
                return Integer.valueOf(ss.substring(ss.lastIndexOf("-") + 1));
            } else {
                return Integer.valueOf(ss.substring(ss.lastIndexOf("/") + 1));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }



}
