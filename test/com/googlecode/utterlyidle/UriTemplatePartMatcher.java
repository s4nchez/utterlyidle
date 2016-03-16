package com.googlecode.utterlyidle;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class UriTemplatePartMatcher extends TypeSafeMatcher<UriTemplatePart> {
    private final String name;
    private final boolean pathParameter;

    public static UriTemplatePartMatcher pathParameter(String expression) {
        return new UriTemplatePartMatcher(expression, true);
    }

    public static UriTemplatePartMatcher pathPart(String expression) {
        return new UriTemplatePartMatcher(expression, false);
    }

    public UriTemplatePartMatcher(String name, boolean pathParameter) {
        this.name = name;
        this.pathParameter = pathParameter;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("uri template path '");
        description.appendText(name);
        description.appendText("' (path parameter = ");
        description.appendText(String.valueOf(pathParameter));
        description.appendText(")");

    }

    @Override
    protected boolean matchesSafely(final UriTemplatePart uriTemplatePart) {
        return uriTemplatePart.isPathParameter() == pathParameter && name.equals(uriTemplatePart.name());
    }
}
