package net.cherokeedictionary.dao;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface DaoCherokeeDictionary {
	
	public String table_entries = "dictionary_entries";
	public String table_indexSyllabary = "dictionary_index_syllabary";
	public String table_indexTranslit = "dictionary_index_translit";
	public String table_indexEnglish = "dictionary_index_english";
	
	/**
	 * automatic connection/disconnect from database via the connection pool as needed (autocloses)
	 */
	public static final DaoCherokeeDictionary dao = new DBI(new Db()).onDemand(DaoCherokeeDictionary.class);

	@SqlUpdate("CREATE TABLE IF NOT EXISTS "+table_entries+" (\n" + 
			"  `id` BIGINT NOT NULL AUTO_INCREMENT,\n" + 
			"  `source` VARCHAR(16) NULL,\n" + 
			"  `syllabary` VARCHAR(254) NULL,\n" + 
			"  `pronunciation` VARCHAR(254) NULL,\n" + 
			"  `definition` VARCHAR(254) NULL,\n" + 
			"  `json` LONGTEXT NULL,\n" + 
			"  `created` DATETIME NULL,\n" + 
			"  `indexed` DATETIME NULL,\n" + 
			"  `modified` TIMESTAMP NULL DEFAULT current_timestamp on update current_timestamp,\n" + 
			"  PRIMARY KEY (`id`),\n" + 
			"  INDEX `source` (`source` ASC),\n" + 
			"  INDEX `created` (`created` ASC),\n" + 
			"  INDEX `indexed` (`indexed` ASC),\n" + 
			"  INDEX `modified` (`modified` ASC))\n" + 
			"ENGINE = InnoDB\n" + 
			"DEFAULT CHARACTER SET = utf8\n" + //utf8mb4 is not available for mysql 5.0.x
			"PACK_KEYS = 1\n" + 
			"ROW_FORMAT = COMPRESSED;\n")
	public void _initDictionaryTable();
	
	@SqlUpdate("CREATE TABLE IF NOT EXISTS "+table_indexSyllabary+" (\n" + 
			"  `id` BIGINT NOT NULL AUTO_INCREMENT,\n" + 
			"  `source` VARCHAR(16) NULL,\n" + 
			"  `syllabary` VARCHAR(254) NULL,\n" + 
			"  `pronunciation` VARCHAR(254) NULL,\n" + 
			"  `definition` VARCHAR(254) NULL,\n" + 
			"  `forms` LONGTEXT NULL,\n" + 
			"  `examples` LONGTEXT NULL,\n" + 
			"  PRIMARY KEY (`id`),\n" + 
			"  INDEX `source` (`source` ASC),\n" + 
			"  FULLTEXT INDEX `forms` (`forms` ASC),\n" + 
			"  FULLTEXT INDEX `examples` (`examples` ASC))\n" + 
			"ENGINE = MyISAM\n" + 
			"DEFAULT CHARACTER SET = utf8\n" + //utf8mb4 is not available for mysql 5.0.x
			"PACK_KEYS = 1\n" + 
			"ROW_FORMAT = DYNAMIC;\n")
	public void _init_dictionary_indexSyllabary();
	
	@SqlUpdate("CREATE TABLE IF NOT EXISTS "+table_indexTranslit+" (\n" + 
			"  `id` BIGINT NOT NULL AUTO_INCREMENT,\n" + 
			"  `source` VARCHAR(16) NULL,\n" + 
			"  `syllabary` VARCHAR(254) NULL,\n" + 
			"  `pronunciation` VARCHAR(254) NULL,\n" + 
			"  `definition` VARCHAR(254) NULL,\n" + 
			"  `forms` LONGTEXT NULL,\n" + 
			"  `examples` LONGTEXT NULL,\n" + 
			"  PRIMARY KEY (`id`),\n" + 
			"  INDEX `source` (`source` ASC),\n" + 
			"  FULLTEXT INDEX `forms` (`forms` ASC),\n" + 
			"  FULLTEXT INDEX `examples` (`examples` ASC))\n" + 
			"ENGINE = MyISAM\n" + 
			"DEFAULT CHARACTER SET = utf8\n" + //utf8mb4 is not available for mysql 5.0.x
			"PACK_KEYS = 1\n" + 
			"ROW_FORMAT = DYNAMIC;\n")
	public void _init_dictionary_indexLatin();
	
	@SqlUpdate("CREATE TABLE IF NOT EXISTS "+table_indexEnglish+" (\n" + 
			"  `id` BIGINT NOT NULL AUTO_INCREMENT,\n" + 
			"  `source` VARCHAR(16) NULL,\n" + 
			"  `syllabary` VARCHAR(254) NULL,\n" + 
			"  `pronunciation` VARCHAR(254) NULL,\n" + 
			"  `definition` VARCHAR(254) NULL,\n" + 
			"  `forms` LONGTEXT NULL,\n" + 
			"  `examples` LONGTEXT NULL,\n" + 
			"  PRIMARY KEY (`id`),\n" + 
			"  INDEX `source` (`source` ASC),\n" + 
			"  FULLTEXT INDEX `forms` (`forms` ASC),\n" + 
			"  FULLTEXT INDEX `examples` (`examples` ASC))\n" + 
			"ENGINE = MyISAM\n" + 
			"DEFAULT CHARACTER SET = utf8\n" + //utf8mb4 is not available for mysql 5.0.x
			"PACK_KEYS = 1\n" + 
			"ROW_FORMAT = DYNAMIC;\n")
	public void _init_dictionary_indexEnglish();
	
	/**
	 * Util functions used in lew of Java 8 default methods.
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
