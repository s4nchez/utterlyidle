package com.googlecode.utterlyidle;

import java.util.Objects;

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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final UriTemplatePart that = (UriTemplatePart) o;
        return pathParameter == that.pathParameter &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, pathParameter);
    }
}
