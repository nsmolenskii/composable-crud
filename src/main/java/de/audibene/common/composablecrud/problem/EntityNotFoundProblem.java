package de.audibene.common.composablecrud.problem;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.DefaultProblem;
import org.zalando.problem.Status;

import java.net.URI;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

import static de.audibene.common.composablecrud.problem.EntityNotFoundProblem.TYPE_NAME;
import static java.util.Collections.singletonMap;

@JsonTypeName(TYPE_NAME)
public class EntityNotFoundProblem extends AbstractThrowableProblem {

    protected static final String TYPE_NAME = "https://audibene-gmbh.github.io/docs/errors/entity-not-found";

    protected EntityNotFoundProblem(final Map<String, Object> params) {
        this(URI.create(TYPE_NAME), "Entity not found", params);
    }

    protected EntityNotFoundProblem(URI type, String message, Map<String, Object> params) {
        super(type, message, Status.NOT_FOUND, null, null, null, params);
    }

    @JsonCreator
    protected static EntityNotFoundProblem valueOf(DefaultProblem problem) {
        return new EntityNotFoundProblem(problem.getParameters());
    }

    public static Supplier<EntityNotFoundProblem> withId(final String entityType, Object id) {
        return withParams(entityType, singletonMap("id", id));
    }

    public static Supplier<EntityNotFoundProblem> withParams(final String entityType, final Map<String, Object> params) {
        return () -> {
            final TreeMap<String, Object> map = new TreeMap<>(params);
            map.put("entityType", entityType);
            return new EntityNotFoundProblem(map);
        };
    }
}
