package net.cherokeedictionary.dao;

import java.util.List;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import net.cherokeedictionary.bind.BindDictionaryEntry;
import net.cherokeedictionary.bind.BindEnglishIndex;
import net.cherokeedictionary.bind.BindSyllabaryIndex;
import net.cherokeedictionary.map.MapperLikespreadsheet;
import net.cherokeedictionary.model.DictionaryEntry;
import net.cherokeedictionary.model.LikeSpreadsheetsRecord;

public interface DaoCherokeeDictionary {

	public String table_entries = "dictionary_entries";
	public String table_indexSyllabary = "dictionary_index_syllabary";
	public String table_indexLatin = "dictionary_index_latin";
	public String table_indexEnglish = "dictionary_index_english";
	public String table_likespreadsheets = "likespreadsheets";

	/**
	 * automatic connection/disconnect from database via the connection pool as
	 * needed (autocloses)
	 */
	public static final DaoCherokeeDictionary dao = new DBI(new Db()).onDemand(DaoCherokeeDictionary.class);

	@SqlUpdate("CREATE TABLE IF NOT EXISTS " + table_entries + " (\n" + "  `id` BIGINT NOT NULL AUTO_INCREMENT,\n"
			+ "  `source` VARCHAR(16) NULL,\n" + "  `syllabary` VARCHAR(254) NULL,\n"
			+ "  `pronunciation` VARCHAR(254) NULL,\n" + "  `definition` VARCHAR(254) NULL,\n"
			+ "  `json` LONGTEXT NULL,\n" + "  `created` DATETIME NULL,\n" + "  `indexed` DATETIME NULL,\n"
			+ "  `modified` TIMESTAMP NULL DEFAULT current_timestamp on update current_timestamp,\n"
			+ "  PRIMARY KEY (`id`),\n" + "  INDEX `source` (`source` ASC),\n" + "  INDEX `created` (`created` ASC),\n"
			+ "  INDEX `indexed` (`indexed` ASC),\n" + "  INDEX `modified` (`modified` ASC))\n" + "ENGINE = InnoDB\n"
			+ "DEFAULT CHARACTER SET = utf8\n" + // utf8mb4 is not available for
													// mysql 5.0.x
	"PACK_KEYS = 1\n" + "ROW_FORMAT = COMPRESSED;\n")
	public void _initDictionaryTable();

	@SqlUpdate("CREATE TABLE IF NOT EXISTS " + table_indexSyllabary + " (\n"
			+ "  `id` BIGINT NOT NULL AUTO_INCREMENT,\n" + "  `source` VARCHAR(16) NULL,\n"
			+ "  `syllabary` VARCHAR(254) NULL,\n" + "  `pronunciation` VARCHAR(254) NULL,\n"
			+ "  `definition` VARCHAR(254) NULL,\n" + "  `forms` LONGTEXT NULL,\n" + "  `examples` LONGTEXT NULL,\n"
			+ "  PRIMARY KEY (`id`),\n" + "  INDEX `source` (`source` ASC),\n"
			+ "  FULLTEXT INDEX `forms` (`forms` ASC),\n" + "  FULLTEXT INDEX `examples` (`examples` ASC))\n"
			+ "ENGINE = MyISAM\n" + "DEFAULT CHARACTER SET = utf8\n" + // utf8mb4
																		// is
																		// not
																		// available
																		// for
																		// mysql
																		// 5.0.x
	"PACK_KEYS = 1\n" + "ROW_FORMAT = DYNAMIC;\n")
	public void _init_dictionary_indexSyllabary();

	@SqlUpdate("CREATE TABLE IF NOT EXISTS " + table_indexLatin + " (\n" + "  `id` BIGINT NOT NULL AUTO_INCREMENT,\n"
			+ "  `source` VARCHAR(16) NULL,\n" + "  `syllabary` VARCHAR(254) NULL,\n"
			+ "  `pronunciation` VARCHAR(254) NULL,\n" + "  `definition` VARCHAR(254) NULL,\n"
			+ "  `forms` LONGTEXT NULL,\n" + "  `examples` LONGTEXT NULL,\n" + "  PRIMARY KEY (`id`),\n"
			+ "  INDEX `source` (`source` ASC),\n" + "  FULLTEXT INDEX `forms` (`forms` ASC),\n"
			+ "  FULLTEXT INDEX `examples` (`examples` ASC))\n" + "ENGINE = MyISAM\n" + "DEFAULT CHARACTER SET = utf8\n"
			+ // utf8mb4 is not available for mysql 5.0.x
			"PACK_KEYS = 1\n" + "ROW_FORMAT = DYNAMIC;\n")
	public void _init_dictionary_indexLatin();

	@SqlUpdate("CREATE TABLE IF NOT EXISTS " + table_indexEnglish + " (\n" + "  `id` BIGINT NOT NULL AUTO_INCREMENT,\n"
			+ "  `source` VARCHAR(16) NULL,\n" + "  `syllabary` VARCHAR(254) NULL,\n"
			+ "  `pronunciation` VARCHAR(254) NULL,\n" + "  `definition` VARCHAR(254) NULL,\n"
			+ "  `forms` LONGTEXT NULL,\n" + "  `examples` LONGTEXT NULL,\n" + "  PRIMARY KEY (`id`),\n"
			+ "  INDEX `source` (`source` ASC),\n" + "  FULLTEXT INDEX `forms` (`forms` ASC),\n"
			+ "  FULLTEXT INDEX `examples` (`examples` ASC))\n" + "ENGINE = MyISAM\n" + "DEFAULT CHARACTER SET = utf8\n"
			+ // utf8mb4 is not available for mysql 5.0.x
			"PACK_KEYS = 1\n" + "ROW_FORMAT = DYNAMIC;\n")
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
			+ "values" + "(:source, :syllabary, :pronunciation, :definition, :json, NOW())")
	public int[] addNewDictionaryEntries(@BindDictionaryEntry Iterable<DictionaryEntry> entries);

	/**
	 * Adds new records. Records are uniqued by id.
	 * 
	 * @param entries
	 * @return
	 */
	@SqlBatch("insert into " + table_entries + " (id, source, syllabary, pronunciation, definition, json, created)"
			+ "select * from " + "(select :id as id, :source as source, :syllabary as syllabary,"
			+ " :pronunciation as pronunciation, :definition as definition, :json as json, NOW() as created) as TMP"
			+ " where not exists (select 1 from " + table_entries + " where id=:id AND :id!=0")
	public int[] addNewDictionaryEntriesWithId(@BindDictionaryEntry Iterable<DictionaryEntry> entries);

	@SqlUpdate("update " + table_entries + " set source=:source, syllabary=:syllabary, pronunciation=:pronunciation,"
			+ " definition=:definition, json=:json where id=:id")
	public int updateDictionaryEntry(@BindDictionaryEntry DictionaryEntry entry);
	
	@SqlBatch("update " + table_entries + " set source=:source, syllabary=:syllabary, pronunciation=:pronunciation,"
			+ " definition=:definition, json=:json where id=:id")
	public int[] updateDictionaryEntries(@BindDictionaryEntry Iterable<DictionaryEntry> entries);
	
	@SqlBatch("delete from "+table_entries+" where id=:id")
	public int[] deleteDictionaryEntriesById(@Bind("id")Iterable<Integer>ids);
	
	@SqlUpdate("delete from "+table_entries+" where id=:id")
	public int deleteDictionaryEntryById(@Bind("id")int id);
	
	/**
	 * Add records to indexing table. Prefilled id is mandatory.
	 */
	@SqlBatch("insert into " + table_indexEnglish + " (id, source, syllabary, pronunciation, definition,"
			+ " forms, examples, created)"
			+ "select * from " + "(select :id as id, :source as source, :syllabary as syllabary,"
			+ " :pronunciation as pronunciation, :definition as definition,"
			+ " :forms as forms, :examples as examples, NOW() as created) as TMP"
			+ " where not exists (select 1 from " + table_indexEnglish + " where id=:id AND :id!=0")
	public int[] addNewIndexEnglishEntriesById(@BindEnglishIndex Iterable<DictionaryEntry> entries);

	@SqlBatch("update " + table_indexEnglish + " set source=:source, syllabary=:syllabary, pronunciation=:pronunciation,"
			+ " definition=:definition, forms=:forms, examples=:examples"
			+ " where id=:id")
	public int[] updateIndexEnglishEntriesById(@BindEnglishIndex Iterable<DictionaryEntry> entry);
	
	@SqlBatch("delete from "+table_indexEnglish+" where id=:id")
	public int[] deleteIndexEnglishEntriesById(@Bind("id")Iterable<Integer>ids);
	
	/**
	 * Add records to indexing table. Prefilled id is mandatory.
	 */
	@SqlBatch("insert into " + table_indexSyllabary + " (id, source, syllabary, pronunciation, definition,"
			+ " forms, examples, created)"
			+ "select * from " + "(select :id as id, :source as source, :syllabary as syllabary,"
			+ " :pronunciation as pronunciation, :definition as definition,"
			+ " :forms as forms, :examples as examples, NOW() as created) as TMP"
			+ " where not exists (select 1 from " + table_indexSyllabary + " where id=:id AND :id!=0")
	public int[] addNewIndexSyllabaryEntriesById(@BindSyllabaryIndex Iterable<DictionaryEntry> entries);

	@SqlBatch("update " + table_indexSyllabary + " set source=:source, syllabary=:syllabary, pronunciation=:pronunciation,"
			+ " definition=:definition, forms=:forms, examples=:examples"
			+ " where id=:id")
	public int[] updateIndexSyllabaryEntriesById(@BindSyllabaryIndex Iterable<DictionaryEntry> entry);
	
	@SqlBatch("delete from "+table_indexSyllabary+" where id=:id")
	public int[] deleteIndexSyllabaryEntriesById(@Bind("id")Iterable<Integer>ids);
	
	/**
	 * Add records to indexing table. Prefilled id is mandatory.
	 */
	@SqlBatch("insert into " + table_indexLatin + " (id, source, syllabary, pronunciation, definition,"
			+ " forms, examples, created)"
			+ "select * from " + "(select :id as id, :source as source, :syllabary as syllabary,"
			+ " :pronunciation as pronunciation, :definition as definition,"
			+ " :forms as forms, :examples as examples, NOW() as created) as TMP"
			+ " where not exists (select 1 from " + table_indexLatin + " where id=:id AND :id!=0")
	public int[] addNewIndexLatinEntriesById(Iterable<DictionaryEntry> entries);

	@SqlBatch("update " + table_indexEnglish + " set source=:source, syllabary=:syllabary, pronunciation=:pronunciation,"
			+ " definition=:definition, forms=:forms, examples=:examples"
			+ " where id=:id")
	public int[] updateIndexLatinEntriesById(Iterable<DictionaryEntry> entry);
	
	@SqlBatch("delete from "+table_indexEnglish+" where id=:id")
	public int[] deleteIndexLatinEntriesById(@Bind("id")Iterable<Integer>ids);
	
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
}
