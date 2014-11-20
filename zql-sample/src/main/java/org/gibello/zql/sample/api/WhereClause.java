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

/**
 * WARNING: This code uses INTERNAL APIs
 * ZqlJJParser should not be used directly by Zql users
 */

import java.io.*;
import org.gibello.zql.*;

public class WhereClause {

  public static void main(String args[]) {
    try {

      ZqlJJParser p = new ZqlJJParser(
        new ByteArrayInputStream(args[0].getBytes()));

      ZExp e = p.SQLExpression();
      System.out.println(e.toString());

    } catch(Exception e) {
      e.printStackTrace();
    }
  }

};

