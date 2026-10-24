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

import net.cherokeedictionary.model.SearchIndex;

@BindingAnnotation(DefineSearchIndex.Factory.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
public @interface DefineSearchIndex {
	public int value() default 0;
	public static class Factory implements BinderFactory {
		public Binder<DefineSearchIndex, SearchIndex> build(Annotation annotation) {
			return new Binder<DefineSearchIndex, SearchIndex>() {
				public void bind(SQLStatement<?> q, DefineSearchIndex bind,
						SearchIndex table) {
					q.define("_search_index_", table.getTable());
				}
			};
		}
	}
}
