package ru.ange.mhb;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Test {

    private static final ObjectMapper OM = new ObjectMapper();

    public static void main(String[] args) {

        String str = "{\"name\":\"mv_page_callback\"}";
        try {
            TestCll testCll = OM.readValue(str, TestCll.class);
            System.out.println("testCll = " + testCll);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
