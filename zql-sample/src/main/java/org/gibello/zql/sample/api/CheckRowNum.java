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
 import java.util.*;
 
 import java.sql.*;
 import org.gibello.zql.*;
 public class CheckRowNum
 {
 
   public CheckRowNum()
   {
   }
 
   public String fixSqlWithRowNum( String sSql) throws Exception
   {
      StringBufferInputStream si = new StringBufferInputStream( sSql );
     ZqlParser par = new ZqlParser(si);
       ZStatement st = par.readStatement();
        ZQuery z = ( ZQuery ) st;
         ZGroupBy zGroupBy = z.getGroupBy();
         Vector vSelect = z.getSelect();
         ZExpression  vWhere  = (  ZExpression)z.getWhere();
         System.out.println(vWhere + vWhere.getClass().getName());
         for ( int i =0; i < vWhere. nbOperands() ; i++)
         {
          System.out.println(vWhere.getOperand(i));
         }

 ZExpression  obj = new ZExpression(
                    "<",
                    new ZConstant("ROWNUM", ZConstant.COLUMNNAME),
                    new ZConstant("900", ZConstant.NUMBER));
 vWhere.addOperand((ZExp)(obj));

         for ( int i =0; i < vWhere. nbOperands() ; i++)
         {
          System.out.println(vWhere.getOperand(i) );
         }
 
       return st.toString();
   }
   public static void main(String[] args)
   {
     CheckRowNum cr = new CheckRowNum();
 
     String sql="SELECT * from NPA where x =1 and y=4;";
     try
     {
    System.out.println( cr.fixSqlWithRowNum(sql));
    }
    catch ( Exception ex)
    {
      System.out.println(ex);
      ex.printStackTrace();
    }
 
   }
   private boolean invokedStandalone = false;
 }

