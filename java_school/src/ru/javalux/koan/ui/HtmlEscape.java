package ru.javalux.koan.ui;


public class HtmlEscape {
    public static String escape(String original)
    {
        return original.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }

}
