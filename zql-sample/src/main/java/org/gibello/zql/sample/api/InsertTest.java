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
import java.util.Vector;
import org.gibello.zql.*;

public class InsertTest {

  public static void main(String args[]) {
    try {

      ZqlParser p = new ZqlParser();

      p.initParser(new ByteArrayInputStream(args[0].getBytes()));
      ZStatement st = p.readStatement();

      if(st instanceof ZInsert) {
        ZInsert ins = (ZInsert)st;
        Vector columns = ins.getColumns();
        Vector values = ins.getValues();
        System.out.println("Insert: Table=" + ins.getTable());
        for(int i=0; i<columns.size(); i++) {
          System.out.println(
           "  " + columns.elementAt(i) + "=" + values.elementAt(i));
        }
      }

    } catch(Exception e) {
      e.printStackTrace();
    }
  }

};

