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

import java.io.*;
import org.gibello.zql.*;

public class TestOp {

  public static void main(String args[]) {
    try {

      ZqlParser p = new ZqlParser();

      p.initParser(new ByteArrayInputStream(args[0].getBytes()));
      ZQuery st = (ZQuery) p.readStatement();
      System.out.println(st.toString()); // Display the statement

        ZExpression where = (ZExpression) st.getWhere();
        ZExpression prjNums = new ZExpression("OR");
        for (int i = 1; i < 4; i++) {
            prjNums.addOperand(
                new ZExpression(
                    "=",
                    new ZConstant("ID", ZConstant.COLUMNNAME),
                    new ZConstant("" + i, ZConstant.NUMBER)));
        }
 
        if (where != null) {
            //where.addOperand(new ZExpression("AND", prjNums));
            ZExpression w = new ZExpression("AND");
            w.addOperand(where);
            w.addOperand(prjNums);
            where = w;
        } else {
            where = prjNums;
        }
        st.addWhere(where);
      System.out.println(st.toString()); // Display the statement

    } catch(ParseException e) {
      System.err.println("PARSE EXCEPTION:");
      e.printStackTrace(System.err);
    } catch(Error e) {
      System.err.println("ERROR");
    } catch(Exception e) {
      System.err.println("CLASS" + e.getClass());
      e.printStackTrace(System.err);
    }
  }

};
