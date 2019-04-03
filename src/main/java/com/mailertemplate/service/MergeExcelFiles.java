package com.mailertemplate.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.mailertemplate.entity.ExcelFile;
import com.mailertemplate.entity.MergeUser;

@Service
public class MergeExcelFiles implements IMergeExcelFiles{

	static List<MergeUser> userRecordList1 = new ArrayList<MergeUser>();
	static List<MergeUser> userRecordList2 = new ArrayList<MergeUser>();
	static List<MergeUser> userRecordList = new ArrayList<MergeUser>();
	static final String DELIMETER = ";";
	
	final Properties properties = new Properties();
	
	public void mergeFiles(ExcelFile file) {
		MergeExcelFiles readAndWriteExcel = new MergeExcelFiles();
		readAndWriteExcel.readFile(file.getFullPath(), 1);
		readAndWriteExcel.readFile(file.getFullPath2(), 2);

		if(!userRecordList1.isEmpty() && !userRecordList2.isEmpty()) {
			readAndWriteExcel.mergeBothList(userRecordList1, userRecordList2);
			readAndWriteExcel.writeFile(userRecordList);
		}
	}

	public void readFile(String fileName, int listNumber) {
		try {
			FileInputStream file = new FileInputStream(new File(fileName));

			//Create Workbook instance holding reference to .xlsx file
			XSSFWorkbook workbook = new XSSFWorkbook(file);

			//Get first/desired sheet from the workbook
			XSSFSheet sheet = workbook.getSheetAt(0);

			//Iterate through each rows one by one
			Iterator<Row> rowIterator = sheet.iterator();
			boolean firstRow = true;
			while (rowIterator.hasNext()) {
				if(firstRow) {
					firstRow = false;
					rowIterator.next();
					continue;
				}

				Row row = rowIterator.next();
				saveRecords(row, listNumber);
			}
			file.close();
			System.out.println("Reading file completed successfully.");
		}
		catch (Exception e) {
			System.out.println("Error ocurred while reading file. File name should be classroom_data.xlsx/online_courses.xlsx. And should be placed in D drive.");
			System.out.println(e.getMessage());
		}
	}

	private void saveRecords(Row row, int listNumber) {
		//For each row, iterate through all the columns
		Iterator<Cell> cellIterator = row.cellIterator();
		int cellNum = 1;
		String email = "";
		while (cellIterator.hasNext()) {
			Cell cell = cellIterator.next();
			if(cellNum == 1) {
				email = cell.getStringCellValue();
			}
			else if(cellNum == 2) {
				if(listNumber == 1) {
					String cellValue = cell.getStringCellValue();
					String[] courseNameList = cellValue.split(DELIMETER);
					for(String courseName : courseNameList) {
						MergeUser userRecord = new MergeUser(email.trim(), courseName.trim(), "");
						userRecordList1.add(userRecord);
					}
				} else {
					String onlineCourse = cell.getStringCellValue();
					MergeUser userRecord = new MergeUser(email.trim(), "", onlineCourse.trim());
					userRecordList2.add(userRecord);
				}
				break;
			}
			cellNum++;
		}
	}

	private void mergeBothList(List<MergeUser> userRecordList1, List<MergeUser> userRecordList2) {

		Collections.sort(userRecordList1, new MergeEmailComparator());
		Collections.sort(userRecordList2, new MergeEmailComparator());
		int i = 0, j = 0;
		int len1 = userRecordList1.size();
		int len2 = userRecordList2.size();

		while(i < len1 && j < len2) {
			MergeUser user1 = userRecordList1.get(i);
			MergeUser user2 = userRecordList2.get(j);
			if(user1.getEmailId().compareTo(user2.getEmailId()) == 0) {
				MergeUser userRecord = new MergeUser(user1.getEmailId(), user1.getClassroomSessionAttended(), user2.getOnlineCoursesEnrolled());
				userRecordList.add(userRecord);
				i++;
				j++;
			} else if(user1.getEmailId().compareTo(user2.getEmailId()) > 0) {
				MergeUser userRecord = new MergeUser(user2.getEmailId(), "", user2.getOnlineCoursesEnrolled());
				userRecordList.add(userRecord);
				j++;
			} else {
				MergeUser userRecord = new MergeUser(user1.getEmailId(), user1.getClassroomSessionAttended(), "");
				userRecordList.add(userRecord);
				i++;
			}

		}

		while(i < len1) {
			MergeUser user1 = userRecordList1.get(i);
			MergeUser userRecord = new MergeUser(user1.getEmailId(), user1.getClassroomSessionAttended(), "");
			userRecordList.add(userRecord);
			i++;
		}

		while(j < len2) {
			MergeUser user2 = userRecordList2.get(j);
			MergeUser userRecord = new MergeUser(user2.getEmailId(), "", user2.getOnlineCoursesEnrolled());
			userRecordList.add(userRecord);
			j++;
		}


	}

	public XSSFWorkbook writeFile(List<MergeUser> userRecordList) {
		//Blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();
		try {

			//Create a blank sheet
			XSSFSheet sheet = workbook.createSheet("Classroom And Enrolled Courses");

			//Header
			Row row = sheet.createRow(0);
			Cell headerCell = row.createCell(0);
			headerCell.setCellValue("Email ID");
			headerCell = row.createCell(1);
			headerCell.setCellValue("Classroom Sessions Attended");
			headerCell = row.createCell(2);
			headerCell.setCellValue("Online Courses Enrolled");

			int rownum = 1;
			for (MergeUser userRecord : userRecordList) {
				row = sheet.createRow(rownum);

				Cell cellEmail = row.createCell(0);
				cellEmail.setCellValue(userRecord.getEmailId());
				Cell cellCourseName = row.createCell(1);
				cellCourseName.setCellValue(userRecord.getClassroomSessionAttended());
				Cell cellOnlineCourses = row.createCell(2);
				cellOnlineCourses.setCellValue(userRecord.getOnlineCoursesEnrolled());

				rownum++;
			}
			
			
			//Write the workbook in file system
			properties.load(new FileInputStream("src/main/resources/application.properties"));
			FileOutputStream out = new FileOutputStream(new File(properties.getProperty("merge.excel.path")));
			workbook.write(out);
			out.close();
			System.out.println("File written successfully on disk.");
		}
		catch (Exception e) {
			System.out.println("Error occured while writing file.");
			System.out.println(e.getMessage());
		}
		return workbook;

	}
}
