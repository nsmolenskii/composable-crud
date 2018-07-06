package de.audibene.common.composablecrud.accessor;

import de.audibene.common.composablecrud.problem.EntityNotFoundProblem;

import java.util.function.Supplier;

public interface NotFoundExceptionAccessor<ID> {

    Supplier<? extends EntityNotFoundProblem> notFoundById(final ID id);

}
