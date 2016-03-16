package com.googlecode.utterlyidle;

public class UriTemplatePart {
    private String name;
    private boolean pathParameter;

    public UriTemplatePart(final String name, final boolean pathParameter) {
        this.name = name;
        this.pathParameter = pathParameter;
    }

    public static UriTemplatePart uriTemplatePart(String name, boolean pathParameter) {
        return new UriTemplatePart(name, pathParameter);
    }

    public String name() {
        return name;
    }

    public boolean isPathParameter() {
        return pathParameter;
    }

}
