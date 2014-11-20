Zql
===

Zql is a java SQL parser, generated using JavaCC.
It parses SQL constructs (no DDL) and generates a parse tree, accessible through a java API.

Zql also provides basic data manipulation mechanisms, including a SQL expression evaluator, and data tuples
(although Zql is definitely not a full-fledged database !).

The parser code is in the zql-parser/ module, and there are some samples in zql-sample/ (you will find README files there).

Zql is provided "as is", with no warranty. It is covered by the GPL v3 license.

Zql is a quite old and mature project : established in 1998, with an open source version released on sourceforge in 2010 (zql.sourceforge.net),
and migrated to maven + github in late 2014.
