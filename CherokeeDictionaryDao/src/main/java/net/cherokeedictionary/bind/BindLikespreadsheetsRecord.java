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

import net.cherokeedictionary.model.LikeSpreadsheetsRecord;

@BindingAnnotation(BindLikespreadsheetsRecord.Factory.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
public @interface BindLikespreadsheetsRecord {
	public static class Factory implements BinderFactory
	  {
	    public Binder<BindLikespreadsheetsRecord, LikeSpreadsheetsRecord> build(Annotation annotation)
	    {
	      return new Binder<BindLikespreadsheetsRecord, LikeSpreadsheetsRecord>()
	      {
	        public void bind(SQLStatement<?> q, BindLikespreadsheetsRecord bind, LikeSpreadsheetsRecord rec)
	        {
	        	q.bind("id", rec.id);
	        	q.bind("category", rec.category);
	        	q.bind("crossreferencet", rec.crossreferencet);
	        	q.bind("definitiond", rec.definitiond);
	        	q.bind("definitionlarge", rec.definitionlarge);
	        	q.bind("entrya", rec.entrya);
	        	q.bind("entrytone", rec.entrytone);
	        	q.bind("entrytranslit", rec.entrytranslit);
	        	q.bind("etymology", rec.etymology);
	        	q.bind("modified", rec.modified);
	        	q.bind("notes", rec.notes);
	        	q.bind("nounadjplurale", rec.nounadjplurale);
	        	q.bind("nounadjpluralsyllf", rec.nounadjpluralsyllf);
	        	q.bind("nounadjpluraltone", rec.nounadjpluraltone);
	        	q.bind("nounadjpluraltranslit", rec.nounadjpluraltranslit);
	        	q.bind("partofspeechc", rec.partofspeechc);
	        	q.bind("sentenceenglishs", rec.sentenceenglishs);
	        	q.bind("sentenceq", rec.sentenceq);
	        	q.bind("sentencesyllr", rec.sentencesyllr);
	        	q.bind("sentencetranslit", rec.sentencetranslit);
	        	q.bind("source", rec.source);
	        	q.bind("syllabaryb", rec.syllabaryb);
	        	q.bind("version", rec.version);
	        	q.bind("vfirstpresg", rec.vfirstpresg);
	        	q.bind("vfirstpresh", rec.vfirstpresh);
	        	q.bind("vfirstpresh", rec.vfirstpresh);
	        	q.bind("vfirstprestone", rec.vfirstprestone);
	        	q.bind("vfirstprestranslit", rec.vfirstprestranslit);
	        	q.bind("vsecondimperm", rec.vsecondimperm);
	        	q.bind("vsecondimpersylln", rec.vsecondimpersylln);
	        	q.bind("vsecondimpertone", rec.vsecondimpertone);
	        	q.bind("vsecondimpertranslit", rec.vsecondimpertranslit);
	        	q.bind("vthirdinfo", rec.vthirdinfo);
	        	q.bind("vthirdinfsyllp", rec.vthirdinfsyllp);
	        	q.bind("vthirdinftone", rec.vthirdinftone);
	        	q.bind("vthirdinftranslit", rec.vthirdinftranslit);
	        	q.bind("vthirdpasti", rec.vthirdpasti);
	        	q.bind("vthirdpastsyllj", rec.vthirdpastsyllj);
	        	q.bind("vthirdpasttone", rec.vthirdpasttone);
	        	q.bind("vthirdpasttranslit", rec.vthirdpasttranslit);
	        	q.bind("vthirdpresk", rec.vthirdpresk);
	        	q.bind("vthirdpressylll", rec.vthirdpressylll);
	        	q.bind("vthirdprestone", rec.vthirdprestone);
	        	q.bind("vthirdprestranslit", rec.vthirdprestranslit);
	        }
	      };
	    }
	  }	
}
