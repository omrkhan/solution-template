package com.tigerit.exam;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import static com.tigerit.exam.IO.*;

/**
 * All of your application logic should be placed inside this class.
 * Remember we will load your application from our custom container.
 * You may add private method inside this class but, make sure your
 * application's execution points start from inside run method.
 */
class Table{
  String tname;
  String [] col_name;
  int [][] tdata;
  int row, col, lastIndex;
  Table(){
  }
  Table(String name, String row_col , String cname){
    String [] rc= row_col.split(" ");
    col= Integer.parseInt(rc[0]);
    row= Integer.parseInt(rc[1]);
    if (col>=2 && col<=100 && row>=2 && row<=100){
      tname= name;
      lastIndex= 0;
      col_name = cname.split(" "); 
      tdata= new int [row][col];
      for (int rdata=0; rdata<row; rdata++){
        setData(readLine()); // initializing data for the table
      }
    }else{
      throw new IllegalArgumentException("Invalid nC nD Line Input");
    }
    
  }
  String getName(){
    return tname;
  }
  int[][] getRawData(){
    return tdata;
  }
  int getColIndex(String colName){
    for (int ci=0; ci<col_name.length; ci++) {
      if (colName.equals(col_name[ci])){
        return ci;
      }
    }
    throw new IllegalArgumentException("Invalid Query"); 
  }
  String[] getColNames(){
    return col_name;
  }
  void setData(String row_data){
    String [] rd= row_data.split(" ");
    if (rd.length == row){
      for (int rowi = 0; rowi<rd.length; rowi++){
        int rval=Integer.parseInt(rd[rowi]); // individual column data
        if (rval >=0 && rval<=Math.pow(10, 6) ){
          tdata[lastIndex][rowi]= rval;  
        }else{
          throw new IllegalArgumentException("Invalid di value");
        }        
      }
      lastIndex++;
    }else{
      throw new IllegalArgumentException("Row and data doesn't match");
    } 
  }
  void printTable(){
    printLine(Arrays.toString(col_name));
    for (int[] row : tdata) {
      for (int elem : row) {
        System.out.printf("%4d", elem);
      }
      System.out.println();
    }
    System.out.println();
  }
  
}
class ResultData{
  ArrayList<ArrayList<Integer>> query_result;
  ArrayList<String> col_names_print_serial;
  ArrayList<String> col_names;


  ResultData(){
    query_result = new ArrayList<ArrayList<Integer>>();
  }
  void setCol(ArrayList<String> arra1, ArrayList<String> arra2 ){
    col_names = arra1;
    col_names.addAll(arra2);
  }
 ArrayList<Integer> printIndexSerial(){
  ArrayList<Integer> printSeries = new ArrayList<Integer>();
  // System.out.println("col_names_print_serial: ");
  // System.out.println(col_names_print_serial);
   for (String serial : col_names_print_serial){
     int j=0;
     for (String name : col_names){
       if(serial.equals(name)){
        printSeries.add(j);
        break;
       }
       j++;
     }
   }
   return printSeries;
 }

  String printResult(boolean flag){
    ArrayList<String> sorts= new ArrayList<String>();
    String str ="";
    if (flag){
      int i=0;
      for(String cname : col_names){
        if (i==0){
          str= str+ cname;
          i=1; 
        }else{
          str= str+" "+ cname;
        }
      }
      str= str+"\n";
      for(ArrayList<Integer> rd: query_result){
        i=0;
        String str2= "";
        for (Integer number : rd){
          if (i==0){
            str2 = str2+number;
            i=1;
          }else{
            str2 = str2+ " "+number;
          }
        }
        sorts.add(str2);
      }
      
      Collections.sort(sorts);
      for (String fstr : sorts){
        str= str+ fstr+ "\n";
      }
      return str;
    }else{
      ArrayList<Integer> printSeries = printIndexSerial();
      int i=0;
      for(String cname : col_names_print_serial){
        if (i==0){
          str= str+ cname;
          i=1; 
        }else{
          str= str+" "+ cname;
        }
      }
      str= str+"\n";

      for(ArrayList<Integer> rd: query_result){
        i=0;
        String str2= "";
        for (int printIndex : printSeries){
          if (i==0){
            str2= str2+rd.get(printIndex);
            i=1;
          }else{
            str2= str2+" "+rd.get(printIndex);
          }
        }
        sorts.add(str2);
      }Collections.sort(sorts);
      for (String fstr : sorts){
        str= str+ fstr+ "\n";
      }
      return str;
    }
    
    // System.out.println(col_names);
    // System.out.println(query_result.size());
    // System.out.println(query_result.get(0).get(1));
    // System.out.println(query_result);
  }
  void setSerial(ArrayList<String> column_serial){
    col_names_print_serial= column_serial;
  }
  void setData(ArrayList<ArrayList<Integer>> rdata){
    query_result= rdata;
  }
}

public class Solution implements Runnable {
  Table [] test_Tables;
  
  String output;
  //this method is to get the table index from tables
  private int getTableIndex(String tname){
    for(int tindex = 0; tindex<test_Tables.length ;tindex++){
      if (tname.equals(test_Tables[tindex].getName())){
        return tindex;
      }
    }
    return -1;
  }
  boolean hasSelected(int index , ArrayList<Integer> selectCol){
    for (int i: selectCol){
      if (i==index){
        return true;
      }
    }
    return false;
  }
  ArrayList<Integer> castIntegerList (int [] intArray, ArrayList<Integer> firstSelectedCol , int [] intArray2, ArrayList<Integer> secondSelectedCol, boolean all){
    ArrayList<Integer> intlist = new ArrayList<Integer>();
    for (int i=0; i<intArray.length; i++){
      if (hasSelected(i , firstSelectedCol) || all){
        intlist.add(intArray[i]);
      }
    }
    for (int i=0; i<intArray2.length; i++){
      if (hasSelected(i , secondSelectedCol) || all){
        intlist.add(intArray2[i]);
      }
    }
    return intlist;
  }
  private ArrayList<ArrayList<Integer>> joinCompare(int [][] rhsArray, int rhsColi, ArrayList<Integer> rhsSelectedCol, int[][] lhsArray, int lhsColi, ArrayList<Integer> lhsSelectedCol, boolean all){
    ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
    for (int rhsi= 0; rhsi<rhsArray.length ; rhsi++){ //traversing the right hand for the match
      for(int lhsi=0; lhsi<lhsArray.length ; lhsi++){
        if (rhsArray[rhsi][rhsColi]==lhsArray[lhsi][lhsColi]){
          result.add(castIntegerList(rhsArray[rhsi], rhsSelectedCol , lhsArray[lhsi], lhsSelectedCol, all));
        }
      }
    }

    // System.out.println(result);
    return result;
  }
  private ArrayList<String> castingStringArray(String [] strarray){
    ArrayList<String> r = new ArrayList<String>();
    for(String str : strarray){
      r.add(str);
    }
    return r;
  }
  
  @Override
  public void run() {
    int t= readLineAsInteger();
    if (t>=1 && t<=10){
      for(int ti= 1; ti<= t; ti++){
        output= "Test: "+ ti;
        int nT= readLineAsInteger();
        if (nT>=2 && nT<=10){
          test_Tables=new Table[nT];
          for (int nTi=0 ; nTi<nT ; nTi++){
            String table_name= readLine();
            String row_col= readLine();
            String cname= readLine();
            test_Tables[nTi]= new Table (table_name, row_col, cname);
          }
        }else{
          throw new IllegalArgumentException("Invalid nT Input");
        }
        //*************************************************************************************
        int nQ = readLineAsInteger();
        if (nQ>=1 && nQ<=50){
          for (int nQi= 1; nQi<=nQ ; nQi++){
            ResultData query_output = new ResultData();
            String line1= readLine();
            String line2= readLine();
            String line3= readLine();
            String line4= readLine();
            readLine();
            // getting table information from line2
            String [] parts = line2.split(" ");
            String from_table_name = parts[1];
            String from_table_alias = "";
            if (parts.length ==3){
              from_table_alias = parts[2];
            }
            int from_table_index= getTableIndex(from_table_name);
            // printLine("FromTableName: "+ from_table_name + " FromTableAlias: " + from_table_alias+ " FromTableIndex: " + from_table_index);
            //**** end of line2 operation
            //*************************************
            // getting table information from line3
            parts = line3.split(" ");
            String join_table_name = parts[1];
            String join_table_alias = "";
            if (parts.length ==3){
              join_table_alias = parts[2];
            }
            int join_table_index= getTableIndex(join_table_name);
            // printLine("JoinTableName: "+ join_table_name + " JoinTableAlias: " + join_table_alias+ " JoinTableIndex: " + join_table_index);
            //**** end of line3 operation
            //****************************
            // getting Join Information from line 4
            parts = line4.split(" ");
            String rhs = parts[1]; // rigth hand side of join
            String lhs = parts[3]; // left hand side of join
            parts = rhs.split("\\."); //breaking the rhs for two parts
            String rhs_table_info = parts[0];
            String rhs_table_col_info = parts[1];
            String rhs_table_alias = "";
            int rhs_table_index = -1;
            if(rhs_table_info.equals(from_table_name) || rhs_table_info.equals(from_table_alias)){
              rhs_table_index= from_table_index;
              rhs_table_alias= from_table_alias;
            }else if(rhs_table_info.equals(join_table_name) || rhs_table_info.equals(join_table_alias)){
              rhs_table_index= join_table_index;
              rhs_table_alias= join_table_alias;
            }else{
              throw new IllegalArgumentException("Invalid Query"); 
            }
            int rhs_col_index = test_Tables[rhs_table_index].getColIndex(rhs_table_col_info);
            //end of rhs operation
            parts = lhs.split("\\."); //breaking the lhs for two parts
            String lhs_table_info = parts[0];
            String lhs_table_col_info = parts[1];
            String lhs_table_alias = "";
            int lhs_table_index= -1;
            if(lhs_table_info.equals(from_table_name) || lhs_table_info.equals(from_table_alias) && !(lhs_table_info.equals(rhs_table_info))){
              lhs_table_index= from_table_index;
              lhs_table_alias= from_table_alias;
            }else if (lhs_table_info.equals(join_table_name) || lhs_table_info.equals(join_table_alias) && !(lhs_table_info.equals(rhs_table_info))){
              lhs_table_index= join_table_index;
              lhs_table_alias= join_table_alias;
            }else{
              throw new IllegalArgumentException("Invalid Query"); 
            }
            int lhs_col_index = test_Tables[lhs_table_index].getColIndex(lhs_table_col_info);
            //end of lhs operation
            //**** end of line4 operation
            //*******************************
            //getting column information for line 1
            boolean flag_all = false;
            ArrayList<String>  column_serial = new ArrayList<String>();
            ArrayList<Integer> t1_selected_col = new ArrayList<Integer>();
            ArrayList<String>  t1_selected_col_name = new ArrayList<String>();
            ArrayList<Integer> t2_selected_col = new ArrayList<Integer>();
            ArrayList<String>  t2_selected_col_name = new ArrayList<String>();
            String selectCol = line1.substring(7);
            if (selectCol.equals("*")){
              flag_all= true;
              query_output.setCol(castingStringArray(test_Tables[rhs_table_index].getColNames()), castingStringArray(test_Tables[lhs_table_index].getColNames()));
            }else{
                parts= selectCol.split(",");
                for (String columns : parts){
                  columns= columns.trim(); 
                  String [] col_det = columns.split("\\.");
                  if (col_det[0].equals(rhs_table_info) || col_det[0].equals(rhs_table_alias)){
                    t1_selected_col.add(test_Tables[rhs_table_index].getColIndex(col_det[1]));
                    t1_selected_col_name.add(col_det[1]);
                    column_serial.add(col_det[1]);
                  }else if(col_det[0].equals(lhs_table_info) || col_det[0].equals(lhs_table_alias)){
                    t2_selected_col.add(test_Tables[lhs_table_index].getColIndex(col_det[1]));
                    t2_selected_col_name.add(col_det[1]);
                    column_serial.add(col_det[1]);
                  }
                }
                query_output.setCol(t1_selected_col_name , t2_selected_col_name);
                query_output.setSerial(column_serial);
            }
            //**** end of line1 operation
            //*******************************
            //Sending query for the comparison operation
            // int [][] joinResult = joinCompare(test_Tables[rhs_table_index].getRawData() , rhs_col_index , test_Tables[lhs_table_index].getRawData(), lhs_col_index);
            query_output.setData(joinCompare(test_Tables[rhs_table_index].getRawData() , rhs_col_index , t1_selected_col,  test_Tables[lhs_table_index].getRawData(), lhs_col_index , t2_selected_col, flag_all));
            
            output= output+ "\n"+query_output.printResult(flag_all);
            //**** end of comparison operation
            //*******************************
          }
        }else{
          throw new IllegalArgumentException("Invalid nQ Input"); 
        }
        //*************************************************************************************
        printLine(output);
        
        
        
      }
    }else{
      throw new IllegalArgumentException("Invalid t Input");
    }
    
    
  }
}
