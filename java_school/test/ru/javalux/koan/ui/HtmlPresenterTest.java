package ru.javalux.koan.ui;

import com.sandwich.koan.result.KoanSuiteResult;
import com.sandwich.util.Strings;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

public class HtmlPresenterTest {
    @Test
    public void getHtml_allKoansSuccessful_returnDivWithSuccessNotice() {
        KoanSuiteResult result = mock(KoanSuiteResult.class);
        stub(result.isAllKoansSuccessful()).toReturn(true);
        HtmlPresenter cut = new HtmlPresenter(result);

        String expected = "<div class=\"koans-succeeded\">" + Strings.getMessage("all_koans_succeeded") + "</div>";
        String actual = cut.getHtml();

        assertEquals(expected, actual);
    }

    @Test
    public void getHtml_oneFailedKoan_returnDivWithSuggestionNotice() {
        String source = "import static com.sandwich.util.Assert.fail;\n" +
                "\n" +
                "import com.sandwich.koan.Koan;\n" +
                "\n" +
                "public class AboutKoans {\n" +
                "\n" +
                "\t@Koan";

        Pattern classPattern = Pattern.compile("^ *(public)? *class ([^{\n ]+)", Pattern.MULTILINE);
        Matcher m = classPattern.matcher(source);

        KoanSuiteResult result = mock(KoanSuiteResult.class);
        stub(result.isAllKoansSuccessful()).toReturn(false);
        HtmlPresenter cut = new HtmlPresenter(result);

        String expected = "";
        String actual = cut.getHtml();

        assertTrue(actual.contains(expected));
    }
}