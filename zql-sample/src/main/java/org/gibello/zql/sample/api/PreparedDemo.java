/*
 * This file is part of Zql.
 *
 * Zql is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Zql is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Zql.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.gibello.zql.sample.api;

import java.sql.SQLException;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.io.*;

import org.gibello.zql.*;

/**
 * <pre>
 * </pre>
 */
public class PreparedDemo {

  public static void main(String args[]) {
    try {

      ZqlParser p = null;

      if(args.length < 1) {
        System.out.println("Reading SQL from stdin (quit; or exit; to quit)");
        p = new ZqlParser(System.in);
      } else {
        p = new ZqlParser(new DataInputStream(new FileInputStream(args[0])));
      }

      // Read all SQL statements from input
      ZStatement st;
      while((st = p.readStatement()) != null) {

        System.out.println(st.toString()); // Display the statement

        if(st instanceof ZQuery) { // An SQL query: query the DB
          handleQuery((ZQuery)st);
        } else if(st instanceof ZInsert) { // An SQL insert
          handleInsert((ZInsert)st);
        } else if(st instanceof ZUpdate) {
          handleUpdate((ZUpdate)st);
        } else if(st instanceof ZDelete) {
          handleDelete((ZDelete)st);
        }
      }

    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  /**
   */
  static void handleQuery(ZQuery q) throws Exception {
    System.out.println("SELECT Statement:");

    Vector sel = q.getSelect(); // SELECT part of the query

    ZExpression w = (ZExpression)q.getWhere();
    if(w != null) {

      Hashtable meta = null;

      Vector from = q.getFrom();  // FROM part of the query

      // Column metadata: Hashtable (alias, table name)
      // Will be used by handleWhere() to resolve aliases into table names
      meta = new Hashtable();
      for(int i=0; i<from.size(); i++) {
        ZFromItem fi = (ZFromItem)from.elementAt(i);
        String alias = fi.getAlias();
        if(alias == null) alias = fi.getTable();
        meta.put(alias.toUpperCase(), fi.getTable());
      }

      handleWhere(w, meta);
    }

  }

  static void handleInsert(ZInsert ins) throws Exception {
    System.out.println("INSERT Statement:");
    String tab = ins.getTable();
    Vector values = ins.getValues();
    if(values == null) {
      System.out.println("no VALUES(), probably a subquery ?");
    }
    int nval = values.size();
    Vector columns = ins.getColumns();
    if(columns == null) {
      System.out.println("no column names, assuming _col_1"
       + (nval > 1 ? " to _col_" + nval : ""));
      columns = new Vector(nval);
      for(int i=1; i<=nval; i++) {
        columns.addElement("_col_" + i);
      }
    }

    for (int i=0; i<nval; i++) {
      ZExp v = (ZExp)values.elementAt(i);
      if(isPreparedColumn(v)) {
        System.out.println("[" + tab + "," + columns.elementAt(i) + "]");
      }
    }
  }

  static void handleUpdate(ZUpdate upd) throws Exception {
    System.out.println("UPDATE Statement:");

    String tab = upd.getTable();
    Hashtable set = upd.getSet();
    Enumeration k = set.keys();
    while(k.hasMoreElements()) {
      String col = (String)k.nextElement();
      ZExp e = (ZExp)set.get(col);
      if(isPreparedColumn(e)) {
        System.out.println("[" + tab + "," + col + "]");
      }
    }

    ZExpression w = (ZExpression)upd.getWhere();
    if(w != null) {
      Hashtable meta = new Hashtable(1);
      meta.put(tab, tab);
      handleWhere(w, meta);
    }

  }

  static void handleDelete(ZDelete del) throws Exception {
    System.out.println("DELETE Statement:");

    String tab = del.getTable();

    ZExpression w = (ZExpression)del.getWhere();
    if(w != null) {
      Hashtable meta = new Hashtable(1);
      meta.put(tab, tab);
      handleWhere(w, meta);
    }

  }

  static void handleWhere(ZExp e, Hashtable meta) throws Exception {

    //if(meta != null) System.out.println(meta);

    if(! (e instanceof ZExpression)) return;
    ZExpression w = (ZExpression)e;

    Vector operands = w.getOperands();
    if(operands == null) return;

    // Look for prepared column ("?")
    String prepared = null;
    for(int i=0; i<operands.size(); i++) {
      if(isPreparedColumn((ZExp)operands.elementAt(i))) {
        prepared = ((ZConstant)operands.elementAt(0)).getValue();
        if(operands.size() != 2) {
          throw new Exception("ERROR in where clause ?? found:"
           + w.toString());
        }
        break;
      }
    }

    if(prepared != null) {  // prepared contains the (raw) column or alias name

      boolean noalias = false;

      // Parse raw column name to look for table name & columnalias name
      // Syntax: [[schema].table.]columnalias
      String tbl = null;

      int pos = prepared.lastIndexOf('.');
      if(pos > 0) {  // [schema.]table.columnalias

        tbl = prepared.substring(0, pos);
        prepared = prepared.substring(pos+1); // The real column name

        if((pos = tbl.lastIndexOf('.')) > 0) { // schema.table.columnalias
          tbl = tbl.substring(pos+1);
          noalias = true;
        }
      }

      // Now tbl is the table name or null, prepared is the column name
      // Note tbl may be an alias
      // (like in "select * from mytable t where t.mykey=1", the table name is
      // "mytable", not "t")

      if(! noalias) {
        // If tbl is an alias, resolve it
        if(tbl != null) tbl = (String)meta.get(tbl.toUpperCase());

      }

      if(tbl == null && meta.size() == 1) {
        Enumeration keys = meta.keys();
        tbl = (String)keys.nextElement();
      }

      // Now tbl is either the real table name, or null if unresolved
      System.out.println("[" + (tbl == null ? "unknown" : tbl) + ","
       + prepared + "]");

    } else {  // No prepared column, go further analyzing the expression

      for(int i=0; i<operands.size(); i++) {
        handleWhere(w.getOperand(i), meta); // WARNING - Recursive call...
      }
    }

  }

  static boolean isPreparedColumn(ZExp v) {
    return
     (v instanceof ZExpression && ((ZExpression)v).getOperator().equals("?"));
  }

};

