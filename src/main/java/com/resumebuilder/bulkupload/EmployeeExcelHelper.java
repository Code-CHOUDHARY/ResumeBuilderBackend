package com.resumebuilder.bulkupload;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.resumebuilder.DTO.UserDto;
import com.resumebuilder.reportingmanager.ReportingManager;
import com.resumebuilder.user.User;

public class EmployeeExcelHelper {
	
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
	public static List<UserDto> convertExcelToListofEmployee(InputStream is){
		List<UserDto> list = new ArrayList<>();
	    try {
	        XSSFWorkbook workbook = new XSSFWorkbook(is);
	        XSSFSheet data = workbook.getSheet("Employees");
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
	            UserDto u = new UserDto();

	            while (cells.hasNext()) {
	                Cell cell = cells.next();
	                String cellValue = dataFormatter.formatCellValue(cell); // Use DataFormatter to get the cell value

	                switch (cid) {
	                    case 0:
	                        u.setEmployee_Id(cellValue);
	                        break;
	                    case 1:
	                        u.setFull_name(cellValue);
	                        break;
	                    case 2:
	                        u.setDate_of_joining(cellValue);
	                        break;
	                    case 3:
	                        u.setDate_of_birth(cellValue);
	                        break;
	                    case 4:
	                        u.setCurrent_role(cellValue);
	                        break;
	                    case 5:
	                        u.setEmail(cellValue);
	                        break;
	                    case 6:
	                        u.setGender(cellValue);
	                        break;
	                    case 7:
	                        u.setMobile_number(cellValue);
	                        break;
	                    case 8:
	                        u.setLocation(cellValue);
	                        break;
	                    case 9:
	                    	u.setManager_employee_id(cellValue);
	                    	break;
	                    default:
	                        break;
	                }
	                cid++;
	            }
	            list.add(u);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return list;
	}

}