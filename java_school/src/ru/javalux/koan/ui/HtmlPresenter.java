package ru.javalux.koan.ui;

import com.sandwich.koan.KoanMethod;
import com.sandwich.koan.result.KoanSuiteResult;
import com.sandwich.util.Strings;

public class HtmlPresenter {
    private KoanSuiteResult result;

    public HtmlPresenter(KoanSuiteResult result) {
        this.result = result;
    }

    public String getHtml() {
        if (result.isAllKoansSuccessful()) {
            return surroundWithTag(Strings.getMessage("all_koans_succeeded"), "div", "koans-succeeded");
        } else {
            return displayOneOrMoreFailure();
        }
    }

    private String displayOneOrMoreFailure() {
        String suggestion = buildSuggestion();

        String message = result.getMessage();
        if (message != null || !message.isEmpty()) {
            message = div(Strings.getMessage("what_went_wrong") + ":" + span(escapeHtml(message), "message"), "what-went-wrong");
        }
        return div(suggestion + message, "koans-fail");
    }

    private String buildSuggestion() {
        String investigate = buildLineInvestigate();
        String clue = buildLineClue();

        return div(investigate, "investigate") +
                div(clue, "clue");
    }

    private String buildLineInvestigate() {
        KoanMethod failedKoan = result.getFailingMethod();
        return Strings.getMessage("investigate") + ": " +
                span(result.getFailingCase(), "java-identifier") + " class's " +
                span(failedKoan.getMethod().getName(), "java-identifier") + " method.";
    }

    private String buildLineClue() {
        if (result.getLineNumber() != null && result.getLineNumber().trim().length() != 0) {
            return Strings.getMessage("line") + " " + span(result.getLineNumber(), "line-number") + " " + Strings.getMessage("may_offer_clue");
        }
        return "";
    }

    private String surroundWithTag(String str, String tag, String... classes) {
        String classStr = "";
        for (String name : classes) {
            classStr = classStr + name + " ";
        }

        String openTag = "<" + tag + (classStr.isEmpty() ? ">" : " class=\"" + classStr.trim() + "\">");
        String cloaseTag = "</" + tag + ">";

        return openTag + str + cloaseTag;
    }

    private String div(String content, String... clazz) {
        return surroundWithTag(content, "div", clazz);
    }

    private String span(String content, String... clazz) {
        return surroundWithTag(content, "span", clazz);
    }

    private String escapeHtml(String str) {
        return HtmlEscape.escape(str);
    }
}


