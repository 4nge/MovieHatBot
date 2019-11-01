package ru.ange.mhb;

public class TestCll {

    public static final String NAME = "mv_page_callback";

    public TestCll() {}
    public TestCll(int i ) {}

    private String name;

    public String getName() {
        return NAME;
    }


    @Override
    public String toString() {
        return "TestCll{" +
                "name='" + name + '\'' +
                '}';
    }
}
