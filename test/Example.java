import com.googlecode.utterlyidle.annotations.GET;
import com.googlecode.utterlyidle.annotations.Path;
import com.googlecode.utterlyidle.annotations.PathParam;

import static com.googlecode.utterlyidle.ApplicationBuilder.application;

public class Example {
    public static void main(String[] args) throws Exception {
        application().addAnnotated(HelloResource.class).start(52841);
    }

    public static class HelloResource {
        @GET
        @Path("/hello/{name}/{surname}/greet")
        public String hello(@PathParam("name") String name, @PathParam("surname") String surname) {
            return String.format("Hello %s %s", name, surname);
        }

    }


}
