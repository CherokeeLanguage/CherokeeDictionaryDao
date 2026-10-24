need to have 1 primary - non-searchable table:

This will contain the entries as 'id', 'source', 'syllabary', 'tones', 'definition', 'json' (bigtext, holds real dict entry), created timestamp, indexed timestamp, modified timestamp

then need to create three auxiliary tables fulltext/myisam tables with fields:
[id - matches primary id], 'source', 'syllabary', 'tones', 'definition', 'searchable text' bigtext [fulltextindexed], modified

the auxiliary tables are searched in fulltext binary mode and only the ids matched are pulled. these ids are then used to pull the appropriate raw json column from the primary table.

the 'syllabary', 'tones', and 'definition' columns are only for db admin use to simply debugging/checking

the source column is there only to facility filtering by source type (ced, noq, etc)

actually each index would need two fulltext fields, one for entries, the second for sentences.

how does that sound?