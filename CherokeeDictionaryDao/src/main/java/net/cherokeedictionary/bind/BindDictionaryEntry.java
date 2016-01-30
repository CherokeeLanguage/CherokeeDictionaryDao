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

import net.cherokeedictionary.model.DictionaryEntry;
import net.cherokeedictionary.util.DaoUtils;

@BindingAnnotation(BindDictionaryEntry.Factory.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
public @interface BindDictionaryEntry {
	public static class Factory implements BinderFactory {
		public Binder<BindDictionaryEntry, DictionaryEntry> build(Annotation annotation) {
			return new Binder<BindDictionaryEntry, DictionaryEntry>() {
				public void bind(SQLStatement<?> q, BindDictionaryEntry bind, DictionaryEntry record) {
					String syllabary = null;
					String pronunciation=null;
					String definition=null;
					if (record.forms!=null && record.forms.size()>0) {
						syllabary=record.forms.get(0).syllabary;
						pronunciation=record.forms.get(0).pronunciation;
						if (pronunciation==null||pronunciation.isEmpty()) {
							pronunciation=record.forms.get(0).translit;
						}
					}
					if (record.definitions!=null) {
						definition="";
						for (String def: record.definitions) {
							definition+=(";"+def);
						}
					}
					
					q.bind("id", record.id);
					q.bind("source", record.source);
					q.bind("syllabary", syllabary);
					q.bind("pronunciation", pronunciation);
					q.bind("definition", definition);
					q.bind("json", DaoUtils.json.toJson(record));
				}
			};
		}
	}
}

/*
(:source, :syllabary, :pronunciation, :definition, :json, NOW())")
*/