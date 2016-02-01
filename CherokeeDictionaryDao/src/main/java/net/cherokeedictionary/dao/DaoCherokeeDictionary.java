package net.cherokeedictionary.dao;

import java.util.List;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
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

@UseStringTemplate3StatementLocator
public interface DaoCherokeeDictionary {

	public String table_entries = "dictionary_entries";
	public String table_indexSyllabary = SearchIndex.Table.tableSyllabary;
	public String table_indexLatin = SearchIndex.Table.tableLatin;
	public String table_indexEnglish = SearchIndex.Table.tableEnglish;
	public String table_likespreadsheets = "likespreadsheets";

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
	public void _initDictionaryTable();

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
			+ ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8" // utf8mb4 not available
			+ " PACK_KEYS = 1 ROW_FORMAT=COMPRESSED;\n" //
	)
	public void _init_dictionary_indexSyllabary();

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
			+ ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8" // utf8mb4 not available
			+ " PACK_KEYS = 1 ROW_FORMAT=COMPRESSED;\n" //
	)
	public void _init_dictionary_indexLatin();

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
			+ ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8" // utf8mb4 not available
			+ " PACK_KEYS = 1 ROW_FORMAT=COMPRESSED;\n" //
	)
	public void _init_dictionary_indexEnglish();

	@SqlQuery("select min(id) from " + table_likespreadsheets)
	public int minIdLikespreadsheet();

	@SqlQuery("select max(id) from " + table_likespreadsheets)
	public int maxIdLikespreadsheet();

	@SqlQuery("select * from " + table_likespreadsheets)
	@RegisterMapper(MapperLikespreadsheet.class)
	public List<LikeSpreadsheetsRecord> getLikespreadsheetRecords();

	/**
	 * Adds new records. Records are NOT uniqued.
	 * 
	 * @param entries
	 * @return
	 */
	@SqlBatch("insert into " + table_entries + " (source, syllabary, pronunciation, definition, json, created)"
			+ " values " + "(:source, :syllabary, :pronunciation, :definition, :json, NOW())")
	@GetGeneratedKeys
	public int[] addNewDictionaryEntries(@BindDictionaryEntry Iterable<DictionaryEntry> entries);

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
	public int[] addNewDictionaryEntriesWithId(@BindDictionaryEntry Iterable<DictionaryEntry> entries);

	@SqlUpdate("update " + table_entries + " set source=:source, syllabary=:syllabary, pronunciation=:pronunciation,"
			+ " definition=:definition, json=:json where id=:id")
	public int updateDictionaryEntry(@BindDictionaryEntry DictionaryEntry entry);

	@SqlBatch("update " + table_entries + " set source=:source, syllabary=:syllabary, pronunciation=:pronunciation,"
			+ " definition=:definition, json=:json where id=:id")
	@BatchChunkSize(25)
	public int[] updateDictionaryEntries(@BindDictionaryEntry Iterable<DictionaryEntry> entries);

	@SqlBatch("delete from " + table_entries + " where id=:id")
	@BatchChunkSize(25)
	public int[] deleteDictionaryEntriesById(@Bind("id") Iterable<Integer> ids);

	@SqlUpdate("delete from " + table_entries + " where id=:id")
	public int deleteDictionaryEntryById(@Bind("id") int id);

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
	public int[] addNewIndexEnglishEntriesById(@BindEnglishIndex Iterable<DictionaryEntry> entries);

	@SqlBatch("update " + table_indexEnglish
			+ " set source=:source, syllabary=:syllabary, pronunciation=:pronunciation,"
			+ " definition=:definition, forms=:forms, examples=:examples" + " where id=:id")
	@BatchChunkSize(25)
	public int[] updateIndexEnglishEntriesById(@BindEnglishIndex Iterable<DictionaryEntry> entry);

	@SqlBatch("delete from " + table_indexEnglish + " where id=:id")
	@BatchChunkSize(25)
	public int[] deleteIndexEnglishEntriesById(@Bind("id") Iterable<Integer> ids);

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
	public int[] addNewIndexSyllabaryEntriesById(@BindSyllabaryIndex Iterable<DictionaryEntry> entries);

	@SqlBatch("update " + table_indexSyllabary
			+ " set source=:source, syllabary=:syllabary, pronunciation=:pronunciation,"
			+ " definition=:definition, forms=:forms, examples=:examples" + " where id=:id")
	@BatchChunkSize(25)
	public int[] updateIndexSyllabaryEntriesById(@BindSyllabaryIndex Iterable<DictionaryEntry> entry);

	@SqlBatch("delete from " + table_indexSyllabary + " where id=:id")
	@BatchChunkSize(25)
	public int[] deleteIndexSyllabaryEntriesById(@Bind("id") Iterable<Integer> ids);

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
	public int[] addNewIndexLatinEntriesById(@BindLatinIndex Iterable<DictionaryEntry> entries);

	@SqlBatch("update " + table_indexLatin + " set source=:source, syllabary=:syllabary, pronunciation=:pronunciation,"
			+ " definition=:definition, forms=:forms, examples=:examples" + " where id=:id")
	@BatchChunkSize(25)
	public int[] updateIndexLatinEntriesById(@BindLatinIndex Iterable<DictionaryEntry> entry);

	@SqlBatch("delete from " + table_indexLatin + " where id=:id")
	@BatchChunkSize(25)
	public int[] deleteIndexLatinEntriesById(@Bind("id") Iterable<Integer> ids);

	/**
	 * Util functions used in lew of Java 8 default methods.
	 * 
	 * @author mjoyner
	 *
	 */
	public static class Util {
		public static void init() {
			dao._init_dictionary_indexEnglish();
			dao._init_dictionary_indexLatin();
			dao._init_dictionary_indexSyllabary();
			dao._initDictionaryTable();
		}
	}

	@SqlQuery("select id, source, json, modified from " + table_entries
			+ " where indexed is null OR indexed = 0 OR modified>=indexed limit :limit")
	@RegisterMapper(MapperDictionaryEntry.class)
	public List<DictionaryEntry> needsIndexing(@Bind("limit") int limit);

	@SqlQuery("select id, json, modified from " + table_entries + " where id in (<ids>)")
	@RegisterMapper(MapperDictionaryEntry.class)
	public List<DictionaryEntry> entriesById(@BindIn("ids") Iterable<Integer> ids);

	/**
	 * update indexed without updating modified and verifying to do the update
	 * by an unchanged modified
	 * 
	 * @param forIndexing
	 */
	@SqlBatch("update " + table_entries + " set indexed=now(), modified=modified"
			+ " where id=:id and CAST(modified as DATETIME) = CAST(:modified as DATETIME)")
	@BatchChunkSize(25)
	public int[] updateIndexMarksById(@BindDictionaryEntry Iterable<DictionaryEntry> forIndexing);

	@SqlQuery("select id from " + table_indexEnglish + " where id in (<ids>)")
	public List<Integer> existingEnglishIds(@BindIn("ids") Iterable<Integer> forIndexing);

	@SqlQuery("select id from " + table_indexLatin + " where id in (<ids>)")
	public List<Integer> existingLatinIds(@BindIn("ids") Iterable<Integer> forIndexing);

	@SqlQuery("select id from " + table_indexSyllabary + " where id in (<ids>)")
	public List<Integer> existingSyllabaryIds(@BindIn("ids") Iterable<Integer> forIndexing);

	@SqlQuery("SELECT id FROM <_search_index_>" + " WHERE MATCH (<_search_field_>)"
			+ " AGAINST (:query IN BOOLEAN MODE);")
	public List<Integer> search(@DefineSearchIndex SearchIndex index, @DefineSearchField SearchField field,
			@Bind("query") String query);
}
