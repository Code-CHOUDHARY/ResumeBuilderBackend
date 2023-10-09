package com.resumebuilder.downloadtemplate;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.resumebuilder.roles.Roles;

import java.io.IOException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;



public class RolesExcelExporter {
	
	 	private XSSFWorkbook workbook;
	    private XSSFSheet sheet;
	    private List<Roles> listRoles;
	    
		public RolesExcelExporter(List<Roles> listRoles) {
			super();
			this.listRoles = listRoles;
			workbook = new XSSFWorkbook();
		}
		 // Create the header row with bold font style
		 private void writeHeaderLine() {
		        sheet = workbook.createSheet("Roles");
		         
		        Row row = sheet.createRow(0);
		         
		        CellStyle style = workbook.createCellStyle();
		        XSSFFont font = workbook.createFont();
		        font.setBold(true);
		        font.setFontHeight(16); // Use setFontHeightInPoints to set font size
		        style.setFont(font);
		         
		        createCell(row, 0, "Role ID", style);      
		        createCell(row, 1, "Roles", style);       
		        createCell(row, 2, "Modified By", style);    
		        createCell(row, 3, "Modified On", style);         
		    } 
		 
		// Create a cell and set its value based on the data type
		 private void createCell(Row row, int columnCount, Object value, CellStyle style) {
		       
			 	sheet.autoSizeColumn(columnCount);
		        Cell cell = row.createCell(columnCount);
		        
		        if (value instanceof Integer) {
		            cell.setCellValue((Integer) value);
		        } else if (value instanceof Boolean) {
		            cell.setCellValue((Boolean) value);
		        }else {
		            cell.setCellValue((String) value);
		        }
		        cell.setCellStyle(style);
		    }
		 
		  // Populate the data rows
		 private void writeDataLines() {
		        int rowCount = 1;
		 
		        CellStyle style = workbook.createCellStyle();
		        XSSFFont font = workbook.createFont();
		        font.setFontHeight(14);
		        style.setFont(font);
		                 
		        for (Roles role : listRoles) {
		            Row row = sheet.createRow(rowCount++);
		            int columnCount = 0;
		             
		            createCell(row, columnCount++, role.getRole_id(), style);
		            createCell(row, columnCount++, role.getRole_name(), style);
		            createCell(row, columnCount++,role.getModified_by(), style);
		            createCell(row, columnCount++,role.getModified_on(), style);
		             
		        }
		    }
		 
		 // Export the Excel file via HTTP response
		 
		 public void export(HttpServletResponse response) throws IOException {
		        writeHeaderLine();
		        writeDataLines();
		         
		        ServletOutputStream outputStream = response.getOutputStream();
		        workbook.write(outputStream);
		        workbook.close();
		         
		        outputStream.close();
		         
		    }
		 
}
