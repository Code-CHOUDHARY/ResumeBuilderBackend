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

import com.resumebuilder.DTO.TechnologyDto;

public class TechnologyExcelHelper {
	
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
		public static List<TechnologyDto> convertExcelToListofTechnology(InputStream is){
			List<TechnologyDto> list = new ArrayList<>();
		    try {
		        XSSFWorkbook workbook = new XSSFWorkbook(is);
		   
		        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
		        	logger.info("Sheet " + i + ": " + workbook.getSheetName(i));
		        }
		        
		        XSSFSheet data = workbook.getSheet("Technologies");
		        if (data == null) {
		            throw new IllegalArgumentException("Excel sheet 'Technologies' not found.");
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
		            TechnologyDto t = new TechnologyDto();

		            while (cells.hasNext()) {
		                Cell cell = cells.next();
		                String cellValue = dataFormatter.formatCellValue(cell); // Use DataFormatter to get the cell value

		                switch (cid) {
		                    case 0:
		                        t.setTechnology_name(cellValue);
		                        break;
		                    default:
		                        break;
		                }
		                cid++;
		            }
		            list.add(t);
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }

		    return list;
		}

}
