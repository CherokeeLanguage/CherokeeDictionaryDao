package net.cherokeedictionary.bind;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.skife.jdbi.v2.SQLStatement;
import org.skife.jdbi.v2.sqlobject.Binder;
import org.skife.jdbi.v2.sqlobject.BinderFactory;
import org.skife.jdbi.v2.sqlobject.BindingAnnotation;

import net.cherokeedictionary.model.SearchField;

/**
 * _search_field_
 * @author mjoyner
 *
 */
@BindingAnnotation(DefineSearchField.Factory.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
public @interface DefineSearchField {
	public int value() default 0;

	public static class Factory implements BinderFactory {
		public Binder<DefineSearchField, SearchField> build(Annotation annotation) {
			return new Binder<DefineSearchField, SearchField>() {
				public void bind(SQLStatement<?> q, DefineSearchField bind, SearchField fields) {
					q.define("_search_field_", fields.getFields());
				}
			};
		}
	}
}
