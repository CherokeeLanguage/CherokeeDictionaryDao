package net.cherokeedictionary.map;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import net.cherokeedictionary.model.LikeSpreadsheetsRecord;
import net.cherokeedictionary.util.DaoUtils;

public class MapperLikespreadsheet implements ResultSetMapper<LikeSpreadsheetsRecord>{
	@Override
	public LikeSpreadsheetsRecord map(int index, ResultSet r, StatementContext ctx) throws SQLException {
		LikeSpreadsheetsRecord record = new LikeSpreadsheetsRecord();
		record.id=r.getInt("id");
		record.version=r.getInt("version");
		record.crossreferencet=r.getString("crossreferencet");
		record.definitiond=r.getString("definitiond");
		record.entrya=r.getString("entrya");
		record.nounadjplurale=r.getString("nounadjplurale");
		record.nounadjpluralsyllf=r.getString("nounadjpluralsyllf");
		record.partofspeechc=r.getString("partofspeechc");
		record.sentenceenglishs=r.getString("sentenceenglishs");
		record.sentenceq=r.getString("sentenceq");
		record.sentencesyllr=r.getString("sentencesyllr");
		record.syllabaryb=r.getString("syllabaryb");
		record.vfirstpresg=r.getString("vfirstpresg");
		record.vfirstpresh=r.getString("vfirstpresh");
		record.vsecondimperm=r.getString("vsecondimperm");
		record.vsecondimpersylln=r.getString("vsecondimpersylln");
		record.vthirdinfo=r.getString("vthirdinfo");
		record.vthirdinfsyllp=r.getString("vthirdinfsyllp");
		record.vthirdpasti=r.getString("vthirdpasti");
		record.vthirdpastsyllj=r.getString("vthirdpastsyllj");
		record.vthirdpresk=r.getString("vthirdpresk");
		record.vthirdpressylll=r.getString("vthirdpressylll");
		record.entrytranslit=r.getString("entrytranslit");
		record.nounadjpluraltranslit=r.getString("nounadjpluraltranslit");
		record.sentencetranslit=r.getString("sentencetranslit");
		record.vfirstprestranslit=r.getString("vfirstprestranslit");
		record.vsecondimpertranslit=r.getString("vsecondimpertranslit");
		record.vthirdinftranslit=r.getString("vthirdinftranslit");
		record.vthirdpasttranslit=r.getString("vthirdpasttranslit");
		record.vthirdprestranslit=r.getString("vthirdprestranslit");
		record.entrytone=r.getString("entrytone");
		record.nounadjpluraltone=r.getString("nounadjpluraltone");
		record.vfirstprestone=r.getString("vfirstprestone");
		record.vsecondimpertone=r.getString("vsecondimpertone");
		record.vthirdinftone=r.getString("vthirdinftone");
		record.vthirdpasttone=r.getString("vthirdpasttone");
		record.vthirdprestone=r.getString("vthirdprestone");
		record.source=r.getString("source");
		record.definitionlarge=r.getString("definitionlarge");
		record.etymology=r.getString("etymology");
		record.category=r.getString("category");
		record.notes=r.getString("notes");
		record.modified=DaoUtils.asDate(r.getTimestamp("modified"));
		return record;
	}
}
