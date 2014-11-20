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
import java.io.*;

import org.gibello.zql.*;

/**
 * This class shows how you can customize Zql so it supports custom functions
 * In this example, the following functions are defined :
 * - whynot (1 parameter)
 * - nvl (2 parameters)
 * - concat (any number of parameters)
 * For example, the following request will be accepted:
 *  select nvl(a, 0), concat(b, 'abc', d, 123), whynot(c) from mytable;
 */
public class CustomFct {

  public static void main(String args[]) {
    try {

      ZqlParser p = null;

      if(args.length < 1) {
        System.out.println("Reading SQL from stdin (quit; or exit; to quit)");
        p = new ZqlParser(System.in);
      } else {
        p = new ZqlParser(new DataInputStream(new FileInputStream(args[0])));
      }

      p.addCustomFunction("whynot", 1);
      p.addCustomFunction("nvl", 2);
      p.addCustomFunction("concat", ZUtils.VARIABLE_PLIST);
      p.addCustomFunction("curdate", 0);

      // Read all SQL statements from input
      ZStatement st;
      while((st = p.readStatement()) != null) {

        System.out.println(st.toString()); // Display the statement

      }

    } catch(Exception e) {
      e.printStackTrace();
    }
  }

};

