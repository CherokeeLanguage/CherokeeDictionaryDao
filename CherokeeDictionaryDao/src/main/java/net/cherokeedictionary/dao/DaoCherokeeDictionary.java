package net.cherokeedictionary.dao;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.Define;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;
import org.skife.jdbi.v2.unstable.BindIn;

import net.cherokeedictionary.bind.BindDictionaryEntry;
import net.cherokeedictionary.bind.BindEnglishIndex;
import net.cherokeedictionary.bind.BindLatinIndex;
import net.cherokeedictionary.bind.BindSyllabaryIndex;
import net.cherokeedictionary.bind.DefineSearchField;
import net.cherokeedictionary.bind.DefineSearchIndex;
import net.cherokeedictionary.map.MapperDictionaryEntry;
import net.cherokeedictionary.map.MapperLikespreadsheet;
import net.cherokeedictionary.model.DictionaryEntry;
import net.cherokeedictionary.model.LikeSpreadsheetsRecord;
import net.cherokeedictionary.model.SearchField;
import net.cherokeedictionary.model.SearchIndex;
import net.cherokeedictionary.util.DaoUtils;

@UseStringTemplate3StatementLocator
public abstract class DaoCherokeeDictionary {

	public static final String table_entries = "dictionary_entries";
	public static final String table_indexSyllabary = SearchIndex.Table.tableSyllabary;
	public static final String table_indexLatin = SearchIndex.Table.tableLatin;
	public static final String table_indexEnglish = SearchIndex.Table.tableEnglish;
	public static final String table_likespreadsheets = "likespreadsheets";

	/**
	 * automatic connection/disconnect from database via the connection pool as
	 * needed (autocloses)
	 */
	public static final DaoCherokeeDictionary dao = new DBI(new Db()).onDemand(DaoCherokeeDictionary.class);

	@SqlUpdate("CREATE TABLE IF NOT EXISTS " + table_entries + " (\n" //
			+ "  `id` BIGINT NOT NULL AUTO_INCREMENT,\n" //
			+ "  `source` VARCHAR(16) NULL,\n" //
			+ "  `syllabary` VARCHAR(254) NULL,\n" //
			+ "  `pronunciation` VARCHAR(254) NULL,\n" //
			+ "  `definition` VARCHAR(254) NULL,\n" //
			+ "  `json` LONGTEXT NULL,\n" //
			+ "  `created` DATETIME NULL,\n" //
			+ "  `indexed` DATETIME NULL,\n" //
			+ "  `modified` TIMESTAMP NULL DEFAULT current_timestamp on update current_timestamp,\n" //
			+ "  PRIMARY KEY (`id`),\n" //
			+ "  INDEX `source` (`source` ASC),\n" //
			+ "  INDEX `created` (`created` ASC),\n" //
			+ "  INDEX `indexed` (`indexed` ASC),\n" //
			+ "  INDEX `modified` (`modified` ASC))\n" //
			+ "ENGINE = InnoDB\n" //
			+ "DEFAULT CHARACTER SET = utf8\n" // utf8mb4 not available
			+ "PACK_KEYS = 1\n" //
			+ "ROW_FORMAT = COMPRESSED;\n")
	public abstract void _initDictionaryTable();

	@SqlUpdate("CREATE TABLE IF NOT EXISTS " + table_indexSyllabary //
			+ " (\n" //
			+ "  `id` bigint(20) NOT NULL auto_increment,\n" //
			+ "  `source` varchar(16) default NULL,\n" //
			+ "  `syllabary` varchar(254) default NULL,\n" //
			+ "  `pronunciation` varchar(254) default NULL,\n" //
			+ "  `definition` varchar(254) default NULL,\n" //
			+ "  `forms` longtext,\n" //
			+ "  `examples` longtext,\n" //
			+ "  `created` datetime default NULL,\n" //
			+ "  `modified` timestamp NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,\n" //
			+ "  PRIMARY KEY  (`id`),\n" //
			+ "  KEY `source` (`source`),\n" //
			+ "  KEY `forms` (`forms`(384)),\n" //
			+ "  KEY `examples` (`examples`(384))\n" //
			+ ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8" // utf8mb4
																		// not
																		// available
			+ " PACK_KEYS = 1 ROW_FORMAT=COMPRESSED;\n" //
	)
	public abstract void _init_dictionary_indexSyllabary();

	@SqlUpdate("CREATE TABLE IF NOT EXISTS " + table_indexLatin //
			+ " (\n" //
			+ "  `id` bigint(20) NOT NULL auto_increment,\n" //
			+ "  `source` varchar(16) default NULL,\n" //
			+ "  `syllabary` varchar(254) default NULL,\n" //
			+ "  `pronunciation` varchar(254) default NULL,\n" //
			+ "  `definition` varchar(254) default NULL,\n" //
			+ "  `forms` longtext,\n" //
			+ "  `examples` longtext,\n" //
			+ "  `created` datetime default NULL,\n" //
			+ "  `modified` timestamp NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,\n" //
			+ "  PRIMARY KEY  (`id`),\n" //
			+ "  KEY `source` (`source`),\n" //
			+ "  KEY `forms` (`forms`(384)),\n" //
			+ "  KEY `examples` (`examples`(384))\n" //
			+ ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8" // utf8mb4
																		// not
																		// available
			+ " PACK_KEYS = 1 ROW_FORMAT=COMPRESSED;\n" //
	)
	public abstract void _init_dictionary_indexLatin();

	@SqlUpdate("CREATE TABLE IF NOT EXISTS " + table_indexEnglish //
			+ " (\n" //
			+ "  `id` bigint(20) NOT NULL auto_increment,\n" //
			+ "  `source` varchar(16) default NULL,\n" //
			+ "  `syllabary` varchar(254) default NULL,\n" //
			+ "  `pronunciation` varchar(254) default NULL,\n" //
			+ "  `definition` varchar(254) default NULL,\n" //
			+ "  `forms` longtext,\n" //
			+ "  `examples` longtext,\n" //
			+ "  `created` datetime default NULL,\n" //
			+ "  `modified` timestamp NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,\n" //
			+ "  PRIMARY KEY  (`id`),\n" //
			+ "  KEY `source` (`source`),\n" //
			+ "  KEY `forms` (`forms`(384)),\n" //
			+ "  KEY `examples` (`examples`(384))\n" //
			+ ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8" // utf8mb4
																		// not
																		// available
			+ " PACK_KEYS = 1 ROW_FORMAT=COMPRESSED;\n" //
	)
	public abstract void _init_dictionary_indexEnglish();

	@SqlQuery("select min(id) from " + table_likespreadsheets)
	public abstract int minIdLikespreadsheet();

	@SqlQuery("select max(id) from " + table_likespreadsheets)
	public abstract int maxIdLikespreadsheet();

	@SqlQuery("select * from " + table_likespreadsheets)
	@RegisterMapper(MapperLikespreadsheet.class)
	public abstract List<LikeSpreadsheetsRecord> getLikespreadsheetRecords();

	@SqlQuery("select * from " + table_likespreadsheets + " where source=:source")
	@RegisterMapper(MapperLikespreadsheet.class)
	public abstract List<LikeSpreadsheetsRecord> getLikespreadsheetRecords(@Bind("source") String source);
	
	@SqlBatch("update "+table_likespreadsheets //
			+" set" //
			+ " sentencesyllr=:sentencesyllr," //
			+ " sentenceq=:sentenceq," //
			+ " sentenceenglishs=:sentenceenglishs," //
			+ " sentencetranslit=:sentencetranslit" //
			+ " where id=:id" //
			+ " AND modified=:modified" //
			+ " AND (" //
			+ "    sentencesyllr != :sentencesyllr" //
			+ "    OR sentenceq != :sentenceq" //
			+ "    OR sentenceenglishs != :sentenceenglishs" //
			+ "    OR sentencetranslit != :sentencetranslit" //
			+ " )")
	@BatchChunkSize(10)
	public abstract int[] updateLikespreadsheetSentences(@BindLikespreadsheetsRecord Iterable<LikeSpreadsheetsRecord> records);
	
	/**
	 * Adds new records. Records are NOT uniqued.
	 * 
	 * @param entries
	 * @return
	 */
	@SqlBatch("insert into " + table_entries + " (source, syllabary, pronunciation, definition, json, created)"
			+ " values " + "(:source, :syllabary, :pronunciation, :definition, :json, NOW())")
	@GetGeneratedKeys
	public abstract int[] addNewDictionaryEntries(@BindDictionaryEntry Iterable<DictionaryEntry> entries);

	/**
	 * Adds new records. Records are uniqued by id.
	 * 
	 * @param entries
	 * @return
	 */
	@SqlBatch("insert into " + table_entries + " (id, source, syllabary, pronunciation, definition, json, created)"
			+ " select * from " + "(select :id as id, :source as source, :syllabary as syllabary,"
			+ " :pronunciation as pronunciation, :definition as definition, :json as json, NOW() as created) as TMP"
			+ " where not exists (select 1 from " + table_entries + " where id=:id AND :id!=0)")
	@BatchChunkSize(25)
	@GetGeneratedKeys
	public abstract int[] addNewDictionaryEntriesWithId(@BindDictionaryEntry Iterable<DictionaryEntry> entries);

	@SqlUpdate("update " + table_entries + " set source=:source, syllabary=:syllabary, pronunciation=:pronunciation,"
			+ " definition=:definition, json=:json where id=:id")
	public abstract int updateDictionaryEntry(@BindDictionaryEntry DictionaryEntry entry);

	@SqlBatch("update " + table_entries + " set source=:source, syllabary=:syllabary, pronunciation=:pronunciation,"
			+ " definition=:definition, json=:json where id=:id")
	@BatchChunkSize(25)
	public abstract int[] updateDictionaryEntries(@BindDictionaryEntry Iterable<DictionaryEntry> entries);

	@SqlBatch("delete from " + table_entries + " where id=:id")
	@BatchChunkSize(25)
	public abstract int[] deleteDictionaryEntriesById(@Bind("id") Iterable<Integer> ids);

	@SqlUpdate("delete from " + table_entries + " where id=:id")
	public abstract int deleteDictionaryEntryById(@Bind("id") int id);

	/**
	 * Add records to indexing table. Prefilled id is mandatory.
	 */
	@SqlBatch("insert into " + table_indexEnglish + " (id, source, syllabary, pronunciation, definition,"
			+ " forms, examples, created)" + " select * from "
			+ "(select :id as id, :source as source, :syllabary as syllabary,"
			+ " :pronunciation as pronunciation, :definition as definition,"
			+ " :forms as forms, :examples as examples, NOW() as created) as TMP" + " where not exists (select 1 from "
			+ table_indexEnglish + " where id=:id AND :id!=0)")
	@BatchChunkSize(25)
	@GetGeneratedKeys
	public abstract int[] addNewIndexEnglishEntriesById(@BindEnglishIndex Iterable<DictionaryEntry> entries);

	@SqlBatch("update " + table_indexEnglish
			+ " set source=:source, syllabary=:syllabary, pronunciation=:pronunciation,"
			+ " definition=:definition, forms=:forms, examples=:examples" + " where id=:id")
	@BatchChunkSize(25)
	public abstract int[] updateIndexEnglishEntriesById(@BindEnglishIndex Iterable<DictionaryEntry> entry);

	@SqlBatch("delete from " + table_indexEnglish + " where id=:id")
	@BatchChunkSize(25)
	public abstract int[] deleteIndexEnglishEntriesById(@Bind("id") Iterable<Integer> ids);

	/**
	 * Add records to indexing table. Prefilled id is mandatory.
	 */
	@SqlBatch("insert into " + table_indexSyllabary + " (id, source, syllabary, pronunciation, definition,"
			+ " forms, examples, created)" + " select * from "
			+ "(select :id as id, :source as source, :syllabary as syllabary,"
			+ " :pronunciation as pronunciation, :definition as definition,"
			+ " :forms as forms, :examples as examples, NOW() as created) as TMP" + " where not exists (select 1 from "
			+ table_indexSyllabary + " where id=:id AND :id!=0)")
	@BatchChunkSize(25)
	@GetGeneratedKeys
	public abstract int[] addNewIndexSyllabaryEntriesById(@BindSyllabaryIndex Iterable<DictionaryEntry> entries);

	@SqlBatch("update " + table_indexSyllabary
			+ " set source=:source, syllabary=:syllabary, pronunciation=:pronunciation,"
			+ " definition=:definition, forms=:forms, examples=:examples" + " where id=:id")
	@BatchChunkSize(25)
	public abstract int[] updateIndexSyllabaryEntriesById(@BindSyllabaryIndex Iterable<DictionaryEntry> entry);

	@SqlBatch("delete from " + table_indexSyllabary + " where id=:id")
	@BatchChunkSize(25)
	public abstract int[] deleteIndexSyllabaryEntriesById(@Bind("id") Iterable<Integer> ids);

	/**
	 * Add records to indexing table. Prefilled id is mandatory.
	 */
	@SqlBatch("insert into " + table_indexLatin + " (id, source, syllabary, pronunciation, definition,"
			+ " forms, examples, created)" + " select * from "
			+ "(select :id as id, :source as source, :syllabary as syllabary,"
			+ " :pronunciation as pronunciation, :definition as definition,"
			+ " :forms as forms, :examples as examples, NOW() as created) as TMP" + " where not exists (select 1 from "
			+ table_indexLatin + " where id=:id AND :id!=0)")
	@BatchChunkSize(25)
	@GetGeneratedKeys
	public abstract int[] addNewIndexLatinEntriesById(@BindLatinIndex Iterable<DictionaryEntry> entries);

	@SqlBatch("update " + table_indexLatin + " set source=:source, syllabary=:syllabary, pronunciation=:pronunciation,"
			+ " definition=:definition, forms=:forms, examples=:examples" + " where id=:id")
	@BatchChunkSize(25)
	public abstract int[] updateIndexLatinEntriesById(@BindLatinIndex Iterable<DictionaryEntry> entry);

	@SqlBatch("delete from " + table_indexLatin + " where id=:id")
	@BatchChunkSize(25)
	public abstract int[] deleteIndexLatinEntriesById(@Bind("id") Iterable<Integer> ids);

	@SqlQuery("select id, source, json, modified from " + table_entries
			+ " where indexed is null OR indexed = 0 OR modified>=indexed limit :limit")
	@RegisterMapper(MapperDictionaryEntry.class)
	public abstract List<DictionaryEntry> needsIndexing(@Bind("limit") int limit);

	@SqlQuery("select id, json, modified from " + table_entries + " where id in (<ids>)")
	@RegisterMapper(MapperDictionaryEntry.class)
	public abstract List<DictionaryEntry> entriesById(@BindIn("ids") Iterable<Integer> ids);

	/**
	 * update indexed without updating modified and verifying to do the update
	 * by an unchanged modified
	 * 
	 * @param forIndexing
	 */
	@SqlBatch("update " + table_entries + " set indexed=now(), modified=modified"
			+ " where id=:id and CAST(modified as DATETIME) = CAST(:modified as DATETIME)")
	@BatchChunkSize(25)
	public abstract int[] updateIndexMarksById(@BindDictionaryEntry Iterable<DictionaryEntry> forIndexing);

	@SqlQuery("select id from " + table_indexEnglish + " where id in (<ids>)")
	public abstract List<Integer> existingEnglishIds(@BindIn("ids") Iterable<Integer> forIndexing);

	@SqlQuery("select id from " + table_indexLatin + " where id in (<ids>)")
	public abstract List<Integer> existingLatinIds(@BindIn("ids") Iterable<Integer> forIndexing);

	@SqlQuery("select id from " + table_indexSyllabary + " where id in (<ids>)")
	public abstract List<Integer> existingSyllabaryIds(@BindIn("ids") Iterable<Integer> forIndexing);

	@SqlQuery("SELECT id FROM <_search_index_>" + " WHERE MATCH (<_search_field_>)"
			+ " AGAINST (:query IN BOOLEAN MODE);")
	public abstract List<Integer> searchFT(@DefineSearchIndex SearchIndex index, @DefineSearchField SearchField field,
			@Bind("query") String query);

	@SqlQuery("SELECT id FROM <_search_index_>" //
			+ " WHERE CONCAT(<_search_field_>) rlike " //
			+ " :query")
	public abstract List<Integer> search(@DefineSearchIndex SearchIndex index, @DefineSearchField SearchField field,
			@Bind("query") String query);

	@SqlQuery("select id, json, modified from " + table_entries + " where source=:source")
	@RegisterMapper(MapperDictionaryEntry.class)
	public abstract List<DictionaryEntry> getRecordsForSource(@Bind("source") String string);

	/**
	 * Util functions used in lew of Java 8 default methods.
	 * 
	 * @author mjoyner
	 *
	 */

	public void backupLikeSpreadsheets() {
		Date stamp = new Date();
		_createBackupTableLikeSpreadsheets(table_likespreadsheets+"_"+stamp.getTime());
		_insertIntoBackupTableLikeSpreadsheets(table_likespreadsheets+"_"+stamp.getTime());
	}

	@SqlUpdate("create table <table> like "+table_likespreadsheets)
	public abstract void _createBackupTableLikeSpreadsheets(@Define("table") String table);
	
	@SqlUpdate("insert into <table> select * from "+table_likespreadsheets)
	public abstract void _insertIntoBackupTableLikeSpreadsheets(@Define("table") String table);

	public static void removeEntriesWithInvalidSyllabary(List<LikeSpreadsheetsRecord> entries) {
		Iterator<LikeSpreadsheetsRecord> ientry = entries.iterator();
		while (ientry.hasNext()) {
			LikeSpreadsheetsRecord entry = ientry.next();
			entry.syllabaryb = defaultString(entry.syllabaryb);
			if (!isBlank(entry.syllabaryb.replaceAll("[Ꭰ-Ᏼ\\s,\\-]", ""))) {
				ientry.remove();
				continue;
			}
			entry.nounadjpluralsyllf = defaultString(entry.nounadjpluralsyllf);
			if (!isBlank(entry.nounadjpluralsyllf.replaceAll("[Ꭰ-Ᏼ\\s,\\-]", ""))) {
				ientry.remove();
				continue;
			}
			entry.vfirstpresh = defaultString(entry.vfirstpresh);
			if (!isBlank(entry.vfirstpresh.replaceAll("[Ꭰ-Ᏼ\\s,\\-]", ""))) {
				ientry.remove();
				continue;
			}
			entry.vsecondimpersylln = defaultString(entry.vsecondimpersylln);
			if (!isBlank(entry.vsecondimpersylln.replaceAll("[Ꭰ-Ᏼ\\s,\\-]", ""))) {
				ientry.remove();
				continue;
			}
			entry.vthirdinfsyllp = defaultString(entry.vthirdinfsyllp);
			if (!isBlank(entry.vthirdinfsyllp.replaceAll("[Ꭰ-Ᏼ\\s,\\-]", ""))) {
				ientry.remove();
				continue;
			}
			entry.vthirdpastsyllj = defaultString(entry.vthirdpastsyllj);
			if (!isBlank(entry.vthirdpastsyllj.replaceAll("[Ꭰ-Ᏼ\\s,\\-]", ""))) {
				ientry.remove();
				continue;
			}
			entry.vthirdpressylll = defaultString(entry.vthirdpressylll);
			if (!isBlank(entry.vthirdpressylll.replaceAll("[Ꭰ-Ᏼ\\s,\\-]", ""))) {
				ientry.remove();
				continue;
			}
		}
	}

	public static void removeEntriesWithMissingPronunciations(List<LikeSpreadsheetsRecord> entries) {
		Iterator<LikeSpreadsheetsRecord> ientry = entries.iterator();
		while (ientry.hasNext()) {
			LikeSpreadsheetsRecord entry = ientry.next();
			if (isBlank(entry.entrytone)) {
				ientry.remove();
				continue;
			}
			if (isBlank(entry.nounadjpluraltone.replace("-", "")) != isBlank(
					entry.nounadjpluralsyllf.replace("-", ""))) {
				System.err.println("nounadjpl: "+DaoUtils.json.toJson(entry));
				ientry.remove();
				continue;
			}
			if (isBlank(entry.vfirstprestone.replace("-", "")) != isBlank(entry.vfirstpresh.replace("-", ""))) {
				System.err.println("vfirstpres: "+DaoUtils.json.toJson(entry));
				ientry.remove();
				continue;
			}
			if (isBlank(entry.vsecondimpertone.replace("-", "")) != isBlank(
					entry.vsecondimpersylln.replace("-", ""))) {
				System.err.println("vsecondimper: "+DaoUtils.json.toJson(entry));
				ientry.remove();
				continue;
			}
			if (isBlank(entry.vthirdpasttone.replace("-", "")) != isBlank(entry.vthirdpastsyllj.replace("-", ""))) {
				System.err.println("vthirdpast: "+DaoUtils.json.toJson(entry));
				ientry.remove();
				continue;
			}
			if (isBlank(entry.vthirdprestone.replace("-", "")) != isBlank(entry.vthirdpressylll.replace("-", ""))) {
				System.err.println("vthirdpres: "+DaoUtils.json.toJson(entry));
				ientry.remove();
				continue;
			}
		}
	}

	public static String defaultString(String text, String _default) {
		return text == null ? "" : _default;
	}

	public static String defaultString(String text) {
		return text == null ? "" : text;
	}

	public static boolean isBlank(String text) {
		return (text == null) || (text.trim().isEmpty());
	}

	public static void removeEntriesWithBogusDefinitions(List<LikeSpreadsheetsRecord> entries) {
		Iterator<LikeSpreadsheetsRecord> ientry = entries.iterator();
		while (ientry.hasNext()) {
			LikeSpreadsheetsRecord entry = ientry.next();
			if (entry.definitiond.startsWith("(see")) {
				ientry.remove();
				continue;
			}
			if (DaoCherokeeDictionary.isBlank(entry.definitiond)) {
				ientry.remove();
				continue;
			}
		}
	}

	/**
	 * We don't want "empty", "word parts", or
	 * "Cross references to the not-included Grammar"
	 * 
	 * @param entries
	 */
	public static void removeUnwantedEntries(List<LikeSpreadsheetsRecord> entries) {
		Iterator<LikeSpreadsheetsRecord> ientry = entries.iterator();
		while (ientry.hasNext()) {
			LikeSpreadsheetsRecord entry = ientry.next();
			if (entry.syllabaryb.contains("-")) {
				ientry.remove();
				continue;
			}
			if (entry.entrya.startsWith("-")) {
				ientry.remove();
				continue;
			}
			if (entry.entrya.endsWith("-")) {
				ientry.remove();
				continue;
			}
			if (entry.definitiond.contains("(see Gram")) {
				ientry.remove();
				continue;
			}
			if (entry.definitiond.startsWith("see ")) {
				ientry.remove();
				continue;
			}
			if (DaoCherokeeDictionary.isBlank(entry.syllabaryb) && DaoCherokeeDictionary.isBlank(entry.entrytone))
				ientry.remove();
			continue;
		}
	}

	public static void init() {
		dao._init_dictionary_indexEnglish();
		dao._init_dictionary_indexLatin();
		dao._init_dictionary_indexSyllabary();
		dao._initDictionaryTable();
	}

}
