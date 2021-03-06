package com.googlecode.utterlyidle;

import com.googlecode.totallylazy.io.Uri;
import com.googlecode.utterlyidle.Request.Builder;
import com.googlecode.utterlyidle.cookies.Cookie;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Lists.list;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.io.Uri.uri;
import static com.googlecode.totallylazy.predicates.Predicates.is;
import static com.googlecode.totallylazy.time.Dates.date;
import static com.googlecode.utterlyidle.HttpHeaders.*;
import static com.googlecode.utterlyidle.Parameters.Builder.add;
import static com.googlecode.utterlyidle.HttpMessage.Builder.cookie;
import static com.googlecode.utterlyidle.Request.delete;
import static com.googlecode.utterlyidle.HttpMessage.Builder.entity;
import static com.googlecode.utterlyidle.Request.Builder.form;
import static com.googlecode.utterlyidle.Request.get;
import static com.googlecode.utterlyidle.Request.head;
import static com.googlecode.utterlyidle.HttpMessage.Builder.header;
import static com.googlecode.utterlyidle.Request.Builder.method;
import static com.googlecode.totallylazy.functions.Functions.modify;
import static com.googlecode.utterlyidle.Request.options;
import static com.googlecode.utterlyidle.Parameters.Builder.param;
import static com.googlecode.utterlyidle.Request.patch;
import static com.googlecode.utterlyidle.Request.post;
import static com.googlecode.utterlyidle.Request.put;
import static com.googlecode.utterlyidle.Request.Builder.query;
import static com.googlecode.utterlyidle.Parameters.Builder.remove;
import static com.googlecode.utterlyidle.Request.request;
import static com.googlecode.utterlyidle.annotations.HttpMethod.DELETE;
import static com.googlecode.utterlyidle.annotations.HttpMethod.GET;
import static com.googlecode.utterlyidle.annotations.HttpMethod.HEAD;
import static com.googlecode.utterlyidle.annotations.HttpMethod.OPTIONS;
import static com.googlecode.utterlyidle.annotations.HttpMethod.PATCH;
import static com.googlecode.utterlyidle.annotations.HttpMethod.POST;
import static com.googlecode.utterlyidle.annotations.HttpMethod.PUT;
import static com.googlecode.utterlyidle.cookies.Cookie.cookie;
import static com.googlecode.utterlyidle.cookies.CookieAttribute.expires;
import static org.junit.Assert.assertEquals;

public class RequestTest {
    @Test
    public void supportsChainingAsWellAsFunctionalBuilderStyle() throws Exception {
        Request request = get("/").query("name", "Dan").form("name", "Matt").cookie("name", "Bob").header(HOST, "localhost");
        assertThat(request.toString(), is("GET /?name=Dan HTTP/1.1\r\n" +
                "Content-Type: application/x-www-form-urlencoded; charset=UTF-8\r\n" +
                "Cookie: name=\"Bob\"; \r\n" +
                "Host: localhost\r\n" +
                "Content-Length: 9\r\n" +
                "\r\n" +
                "name=Matt"));
    }

    @Test
    public void supportsGet() throws Exception {
        Request request = get("http://localhost/");
        assertThat(request.method(), is(GET));
        assertThat(request.uri(), is(uri("http://localhost/")));
    }

    @Test
    public void supportsPost() throws Exception {
        Request request = post("http://localhost/");
        assertThat(request.method(), is(POST));
        assertThat(request.uri(), is(uri("http://localhost/")));
    }

    @Test
    public void supportsPut() throws Exception {
        Request request = put("http://localhost/");
        assertThat(request.method(), is(PUT));
        assertThat(request.uri(), is(uri("http://localhost/")));
    }

    @Test
    public void supportsPatch() throws Exception {
        Request request = patch("http://localhost/");
        assertThat(request.method(), is(PATCH));
        assertThat(request.uri(), is(uri("http://localhost/")));
    }

    @Test
    public void supportsDelete() throws Exception {
        Request request = delete("http://localhost/");
        assertThat(request.method(), is(DELETE));
        assertThat(request.uri(), is(uri("http://localhost/")));
    }

    @Test
    public void supportsHead() throws Exception {
        Request request = head("http://localhost/");
        assertThat(request.method(), is(HEAD));
        assertThat(request.uri(), is(uri("http://localhost/")));
    }

    @Test
    public void supportsOptions() throws Exception {
        Request request = options("http://localhost/");
        assertThat(request.method(), is(OPTIONS));
        assertThat(request.uri(), is(uri("http://localhost/")));
    }

    @Test
    public void supportsCustomMethod() throws Exception {
        Request request = request("TICKLE", Uri.uri("http://localhost/"));
        assertThat(request.method(), is("TICKLE"));
        assertThat(request.uri(), is(uri("http://localhost/")));
    }

    @Test
    public void canChangeMethod() throws Exception {
        Request request = modify(get("/"), method(POST));
        assertThat(request.method(), is(POST));
    }

    @Test
    public void canChangeUri() throws Exception {
        Request request = modify(get("/"), Builder.uri("/different"));
        assertThat(request.uri(), is(uri("/different")));
    }

    @Test
    public void canSetHeaderParameters() throws Exception {
        assertThat(get("/", header(ACCEPT, "Chickens")).headers().getValue(ACCEPT), is("Chickens"));
        HeaderParameters headers = get("/", header(ACCEPT, "Chickens"), header(CONTENT_TYPE, "Cats")).headers();
        assertThat(headers.getValue(ACCEPT), is("Chickens"));
        assertThat(headers.getValue(CONTENT_TYPE), is("Cats"));
    }

    @Test
    public void canSetMultipleHeaderParametersInOneGoForPerformanceReasons() throws Exception {
        assertThat(get("/", header(param(ACCEPT, list("Chickens", "Cats")))).headers().getValues(ACCEPT), is(sequence("Chickens", "Cats")));
        assertThat(get("/", header(add(ACCEPT, "Chickens"), add(ACCEPT, "Cats"))).headers().getValues(ACCEPT), is(sequence("Chickens", "Cats")));
    }

    @Test
    public void canRemoveAHeader() throws Exception {
        Request original = get("/", header(ACCEPT, "Chickens"), header(CONTENT_TYPE, "Cats"));
        HeaderParameters headers = modify(original, header(remove(ACCEPT))).headers();
        assertThat(headers.contains(ACCEPT), is(false));
        assertThat(headers.getValue(CONTENT_TYPE), is("Cats"));
    }

    @Test
    public void canSetEntity() throws Exception {
        Request request = get("/", entity("Hello"));
        assertThat(request.entity().toString(), is("Hello"));
    }

    @Test
    public void canSetQueryParameters() throws Exception {
        assertThat(get("/", query("name", "Dan")).uri(), is(uri("/?name=Dan")));
        assertThat(get("/", query("first", "Dan"), query("last", "Bod")).uri(), is(uri("/?first=Dan&last=Bod")));
    }

    @Test
    public void canSetMultipleQueryParametersInOneGoForPerformanceReasons() throws Exception {
        assertThat(get("/", query(param("name", list("Dan", "Matt")))).uri(), is(uri("/?name=Dan&name=Matt")));
        assertThat(get("/", query(add("name", "Dan"), add("name", "Matt"))).uri(), is(uri("/?name=Dan&name=Matt")));
    }

    @Test
    public void canRemoveAQuery() throws Exception {
        Request original = get("/", query("first", "Dan"), query("last", "Bod"));
        assertThat(modify(original, query(remove("first"))).uri(), is(uri("/?last=Bod")));
    }

    @Test
    public void canSetFormParameters() throws Exception {
        assertThat(get("/", form("name", "Dan")).entity().toString(), is("name=Dan"));
        assertThat(get("/", form("first", "Dan"), form("last", "Bod")).entity().toString(), is("first=Dan&last=Bod"));
    }

    @Test
    public void canSetMultipleFormParametersInOneGoForPerformanceReasons() throws Exception {
        assertThat(get("/", form(param("name", list("Dan", "Matt")))).entity().toString(), is("name=Dan&name=Matt"));
        assertThat(get("/", form(add("name", "Dan"), add("name", "Matt"))).entity().toString(), is("name=Dan&name=Matt"));
    }

    @Test
    public void canRemoveAForm() throws Exception {
        Request original = get("/", form("first", "Dan"), form("last", "Bod"));
        assertThat(modify(original, form(remove("first"))).entity().toString(), is("last=Bod"));
    }

    @Test
    public void canSetCookieParameters() throws Exception {
        String value = get("/", cookie("name", "Dan")).headers().getValue(COOKIE);
        assertThat(value, is("name=\"Dan\"; "));
        assertThat(get("/", cookie("first", "Dan"), cookie("last", "Bod")).headers().getValues(COOKIE), is(sequence("first=\"Dan\"; ", "last=\"Bod\"; ")));
    }

    @Test
    public void doesNotStoreCookieAttributes() throws Exception {
        Cookie cookie = cookie("name", "Dan", expires(date(2001, 1, 1)));
        Request request = Request.get("/").cookie(cookie);
        assertThat(request.cookie(cookie.name()), is(some(Cookie.cookie("name", "Dan"))));
    }

    @Test
    public void canSetMultipleCookieParametersInOneGoForPerformanceReasons() throws Exception {
        assertThat(get("/", cookie(param("name", list("Dan", "Matt")))).headers().getValues(COOKIE), is(sequence("name=\"Dan\"; ", "name=\"Matt\"; ")));
        assertThat(get("/", cookie(add("name", "Dan"), add("name", "Matt"))).headers().getValues(COOKIE), is(sequence("name=\"Dan\"; ", "name=\"Matt\"; ")));
    }

    @Test
    public void canRemoveACookie() throws Exception {
        Request original = get("/", cookie("first", "Dan"), cookie("last", "Bod"));
        assertThat(modify(original, cookie(remove("first"))).headers().getValues(COOKIE), is(sequence("last=\"Bod\"; ")));
    }

}
