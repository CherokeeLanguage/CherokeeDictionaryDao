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
import net.cherokeedictionary.model.DictionaryEntry.EntryExample;
import net.cherokeedictionary.util.DaoUtils;

@BindingAnnotation(BindEnglishIndex.Factory.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
public @interface BindEnglishIndex {
	public static class Factory implements BinderFactory {
		public Binder<BindEnglishIndex, DictionaryEntry> build(Annotation annotation) {
			return new Binder<BindEnglishIndex, DictionaryEntry>() {
				public void bind(SQLStatement<?> q, BindEnglishIndex bind, DictionaryEntry record) {
					String syllabary = null;
					String pronunciation=null;
					String definition=null;
					if (record.forms!=null && record.forms.size()>0) {
						syllabary=record.forms.get(0).syllabary;
						pronunciation=record.forms.get(0).pronunciation;
						if (pronunciation==null||pronunciation.isEmpty()) {
							pronunciation=record.forms.get(0).latin;
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
					q.bind("pronunciation", DaoUtils.unicodePronunciation(pronunciation));
					q.bind("definition", definition);
					
					StringBuilder sb = new StringBuilder();
					if (record.definitions!=null) {
						for (String def: record.definitions) {
							sb.append(def);
							sb.append("\n");
						}
					}
					q.bind("forms", sb.toString());
					sb.setLength(0);
					if (record.examples!=null) {
						for (EntryExample example: record.examples) {
							if (example.english!=null) {
								sb.append(example.english);
								sb.append("\n");
							}
						}
					}
					q.bind("examples", sb.toString());
				}
			};
		}
	}
}

/*
(select :id as id, :source as source, :syllabary as syllabary,"
			+ " :pronunciation as pronunciation, :definition as definition,"
			+ " :forms as forms, :examples as examples
*/