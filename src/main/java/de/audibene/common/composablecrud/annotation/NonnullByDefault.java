package de.audibene.common.composablecrud.annotation;

import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierDefault;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Nonnull
@Inherited
@Retention(RUNTIME)
@TypeQualifierDefault({PARAMETER, METHOD})
public @interface NonnullByDefault {
}