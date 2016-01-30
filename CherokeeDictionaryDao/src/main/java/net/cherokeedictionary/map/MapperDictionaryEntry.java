package net.cherokeedictionary.map;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import net.cherokeedictionary.model.DictionaryEntry;
import net.cherokeedictionary.util.DaoUtils;

public class MapperDictionaryEntry implements ResultSetMapper<DictionaryEntry>{
	@Override
	public DictionaryEntry map(int index, ResultSet r, StatementContext ctx) throws SQLException {
		DictionaryEntry record = new DictionaryEntry();
		//get default object
		record = DaoUtils.json.fromJson(r.getString("json"), DictionaryEntry.class);
		//make sure we update to match live db specific info
		record.id=r.getInt("id");
		record.modified=DaoUtils.asDate(r.getTimestamp("modified"));
		return record;
	}
}
