package com.qtpselenium.hybrid.util;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class XLS_reader {
	//=System.getProperty("user.dir")+"\\src\\TestData.xlsx"
public  String path;
	 FileInputStream fs;
	 XSSFWorkbook wb;
	 XSSFSheet sheet;
	 FileOutputStream fileOut;
	 XSSFRow row;
	 XSSFCell cell;
	 String cellText;
		public XLS_reader(String path) throws FileNotFoundException{
			this.path=path;
			try {
				fs=new FileInputStream(path);
				wb= new XSSFWorkbook(fs);
				sheet=wb.getSheetAt(0);
				//wb.close();
				fs.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//returns rows count in the sheet
		public int getRowCount(String sheetName){
			int index = wb.getSheetIndex(sheetName);
			if(index==-1)
				return 0;
			else{
			sheet = wb.getSheetAt(index);
			int number=sheet.getLastRowNum()+1;
		//	System.out.println("no of rows: "+ number);
			return number;
			
			}
			
		}
		public String getCellData(String sheetName,String colName,int rNum){
			try{
				if(rNum <=0)
					return "";
			
			int index = wb.getSheetIndex(sheetName);
			int col_Num=-1;
			if(index==-1)
				return "";
			
			sheet = wb.getSheetAt(index);
			row=sheet.getRow(0);
			for(int i=0;i<row.getLastCellNum();i++){
				if(row.getCell(i).getStringCellValue().trim().equals(colName.trim()))
					col_Num=i;
			}
			if(col_Num==-1)
				return "";
			
			sheet = wb.getSheetAt(index);
			row = sheet.getRow(rNum-1);
			if(row==null)
				return "";
			cell = row.getCell(col_Num);
			 //cellText=row.getCell(col_Num).toString();
			 //if(cellText.equals("")){
			//	 return "";
			// }
			if(cell==null)
				return "";
			//System.out.println(cell.getCellType());
			
				  
			}  
			
			catch(Exception e){
				
				e.printStackTrace();
				return "row "+rNum+" or column "+colName +" does not exist in xls";
			}
			return cell.toString();
		}
		//returns cell data
		public Object[][] getData(String sheetName) throws IOException{
			fs=new FileInputStream(path);
			wb=new XSSFWorkbook(fs);
			sheet=wb.getSheet(sheetName);
			int lastRow=sheet.getLastRowNum();
			//int rows=lastRow+1;
		//	System.out.println(lastRow);
			int cols=sheet.getRow(lastRow).getLastCellNum();
			Object[][] obj=new Object[lastRow][cols];
			for(int row=1;row<=sheet.getLastRowNum();row++){
				XSSFRow rows=sheet.getRow(row);
				//System.out.println(rows.getLastCellNum());
				for(int col=0;col<rows.getLastCellNum();col++){
					XSSFCell cell=rows.getCell(col);
					if(cell.toString()==""){
					break;
					}else
					{
						obj[row-1][col]=rows.getCell(col).toString();		
						//System.out.println(obj[row-1][col]);
					}				
				}
			}	
			fs.close();	
		
			return obj;
		
	}	
		//public static void main(String[] args) throws IOException{
		//XLS_reader read= new XLS_reader(System.getProperty("user.dir")+"\\src\\test\\resources\\TetsDataZoho.xlsx");
		//read.getData("Testcases");
			
		//}
}
