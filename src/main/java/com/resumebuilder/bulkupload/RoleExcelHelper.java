package com.resumebuilder.bulkupload;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import com.resumebuilder.DTO.RolesDto;


public class RoleExcelHelper {
	
	public static final Logger logger = LoggerFactory.getLogger(RoleExcelHelper.class);
	
	//check that file is of excel type or not
		public static boolean checkExcelFormat(MultipartFile file) {
			String cntentType = file.getContentType();
			if(cntentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
				return true;
			}else {
				return false;
			}
		}
		
		//convert excel to list of employees
		public static List<RolesDto> convertExcelToListofRoles(InputStream is){
			List<RolesDto> list = new ArrayList<>();
		    try {
		        XSSFWorkbook workbook = new XSSFWorkbook(is);
		   
		        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
		        	logger.info("Sheet " + i + ": " + workbook.getSheetName(i));
		        }
		        
		        XSSFSheet data = workbook.getSheet("Roles");
		        if (data == null) {
		            throw new IllegalArgumentException("Excel sheet 'Roles' not found.");
		        }
		        
		        int rowNumber = 0;
		        Iterator<Row> iterator = data.iterator();

		        DataFormatter dataFormatter = new DataFormatter();

		        while (iterator.hasNext()) {
		            Row row = iterator.next();
		            if (rowNumber == 0) {
		                rowNumber++;
		                continue;
		            }

		            Iterator<Cell> cells = row.iterator();

		            int cid = 0;
		            RolesDto r = new RolesDto();

		            while (cells.hasNext()) {
		                Cell cell = cells.next();
		                String cellValue = dataFormatter.formatCellValue(cell); // Use DataFormatter to get the cell value

		                switch (cid) {
		                    case 0:
		                        r.setRole_name(cellValue);
		                        break;
		                    default:
		                        break;
		                }
		                cid++;
		            }
		            list.add(r);
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }

		    return list;
		}

}
