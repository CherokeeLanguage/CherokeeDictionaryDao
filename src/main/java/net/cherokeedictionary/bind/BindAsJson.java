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

import net.cherokeedictionary.util.DaoUtils;

@BindingAnnotation(BindAsJson.Factory.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
public @interface BindAsJson {
	public static class Factory implements BinderFactory {
		public Binder<BindAsJson, Object> build(Annotation annotation) {
			return new Binder<BindAsJson, Object>() {
				public void bind(SQLStatement<?> q, BindAsJson bind, Object record) {
					q.bind("json", DaoUtils.json.toJson(record));
				}
			};
		}
	}
}
