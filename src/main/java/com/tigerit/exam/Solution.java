package com.tigerit.exam;

import java.util.Arrays;
import java.util.ArrayList;
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
      printTable();
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
    return -1;
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
  ArrayList<String> col_name;
  ResultData(){
    query_result = new ArrayList<ArrayList<Integer>>();
  }
  void printList(){
    System.out.println(query_result);
  }
  void addData(ArrayList<Integer> rdata){
    query_result.add(rdata);
  }
}

public class Solution implements Runnable {
  Table [] test_Tables;
  Table [] result_tables;
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
  ArrayList<Integer> castIntegerList (int [] intArray , int [] intArray2){
    ArrayList<Integer> intlist = new ArrayList<Integer>();
    for (int i: intArray){
      intlist.add(i);
    }
    for (int i: intArray2){
      intlist.add(i);
    }
    return intlist;
  }
  private ResultData joinCompare(int [][] rhsArray, int rhsColi, int[][] lhsArray, int lhsColi){
    ResultData result = new ResultData();
    for (int rhsi= 0; rhsi<rhsArray.length ; rhsi++){ //traversing the right hand for the match
      for(int lhsi=0; lhsi<lhsArray.length ; lhsi++){
        if (rhsArray[rhsi][rhsColi]==lhsArray[lhsi][lhsColi]){
          result.addData(castIntegerList(rhsArray[rhsi] , lhsArray[lhsi]));
          // printLine(Arrays.toString(rhsArray[rhsi]));
          // printLine(Arrays.toString(lhsArray[lhsi]));

        }
      }
    }
    result.printList();
    return result;
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
          result_tables=new Table[nQ];
          for (int nQi= 1; nQi<=nQ ; nQi++){
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
            int rhs_table_index = -1;
            if(rhs_table_info.equals(from_table_name) || rhs_table_info.equals(from_table_alias)){
              rhs_table_index= from_table_index;
            }else if(rhs_table_info.equals(join_table_name) || rhs_table_info.equals(join_table_alias)){
              rhs_table_index= join_table_index;
            }else{
              throw new IllegalArgumentException("Invalid Query"); 
            }
            int rhs_col_index = test_Tables[rhs_table_index].getColIndex(rhs_table_col_info);
            if (rhs_col_index== -1){
              throw new IllegalArgumentException("Invalid Query"); 
            }//end of rhs operation
            parts = lhs.split("\\."); //breaking the lhs for two parts
            String lhs_table_info = parts[0];
            String lhs_table_col_info = parts[1];
            int lhs_table_index= -1;
            if(lhs_table_info.equals(from_table_name) || lhs_table_info.equals(from_table_alias) && !(lhs_table_info.equals(rhs_table_info))){
              lhs_table_index= from_table_index;
            }else if (lhs_table_info.equals(join_table_name) || lhs_table_info.equals(join_table_alias) && !(lhs_table_info.equals(rhs_table_info))){
              lhs_table_index= join_table_index;
            }else{
              throw new IllegalArgumentException("Invalid Query"); 
            }
            int lhs_col_index = test_Tables[lhs_table_index].getColIndex(lhs_table_col_info);
            if (lhs_col_index== -1){
              throw new IllegalArgumentException("Invalid Query"); 
            }//end of lhs operation
            //**** end of line4 operation
            //*******************************
            //getting column information for line 1
            //Sending query for the comparison operation
            // int [][] joinResult = joinCompare(test_Tables[rhs_table_index].getRawData() , rhs_col_index , test_Tables[lhs_table_index].getRawData(), lhs_col_index);
            joinCompare(test_Tables[rhs_table_index].getRawData() , rhs_col_index , test_Tables[lhs_table_index].getRawData(), lhs_col_index);
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
