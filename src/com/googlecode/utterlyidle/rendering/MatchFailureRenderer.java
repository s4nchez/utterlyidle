package com.googlecode.utterlyidle.rendering;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.template.Templates;
import com.googlecode.utterlyidle.BasePath;
import com.googlecode.utterlyidle.Binding;
import com.googlecode.utterlyidle.FormParameters;
import com.googlecode.utterlyidle.MatchFailure;
import com.googlecode.utterlyidle.NamedParameter;
import com.googlecode.utterlyidle.QueryParameters;
import com.googlecode.utterlyidle.Redirector;
import com.googlecode.utterlyidle.Renderer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.googlecode.totallylazy.Strings.EMPTY;
import static com.googlecode.totallylazy.predicates.Predicates.not;

public class MatchFailureRenderer implements Renderer<MatchFailure> {
    private final BasePath basePath;
    private final Redirector redirector;

    public MatchFailureRenderer(BasePath basePath, final Redirector redirector) {
        this.basePath = basePath;
        this.redirector = redirector;
    }

    public String render(MatchFailure value) throws IOException {
        Templates group = Templates.defaultTemplates(getClass());
        Map<String,Object> model = new HashMap<>();
        model.put("base", basePath);
        model.put("status", value.status());
        model.put("resources", value.matchesSoFar().filter(not(Binding::hidden)).map(binding -> {
            String httpMethod = binding.httpMethod();
            Sequence<NamedParameter> parameters = binding.namedParameters();

            return new HashMap<String,Object>() {{
                    put("method", httpMethod.equals("*") ? "ANY" : httpMethod);
                    put("path", redirector.uriOf(binding).path());
                    put("query", parameterAsModel(parameters.filter(p -> p.parametersClass().equals(QueryParameters.class))));
                    put("form", parameterAsModel(parameters.filter(p -> p.parametersClass().equals(FormParameters.class))));
                }};
        }));
        return group.get("matchFailure").render(model);
    }

    private Map<String,Object> parameterAsModel(Sequence<NamedParameter> parameters) {
        Map<String,Object> result = new HashMap<>();
        for (NamedParameter parameter : parameters) {
            result.put(parameter.name(), parameter.defaultValue().getOrElse(EMPTY));
        }
        return result;
    }

}
