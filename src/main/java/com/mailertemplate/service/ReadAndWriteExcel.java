package com.mailertemplate.service;

import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.mailertemplate.entity.Upskilling;
import com.mailertemplate.entity.User;

public class ReadAndWriteExcel {

	static List<User> userRecordList = new ArrayList<User>();
	static final String DELIMETER = ";";


	public List<User> readExcelFile(String filePath) {
		try {
			FileInputStream file = new FileInputStream(filePath);
			//Create Workbook instance holding reference to .xlsx file
			XSSFWorkbook workbook = new XSSFWorkbook(file);

			//Get desired sheet from the workbook
			for(int i = 0; i < 3; i++) {
				XSSFSheet sheet = workbook.getSheetAt(i);

				//Iterate through each rows one by one
				Iterator<Row> rowIterator = sheet.iterator();


				boolean rowNotEmpty = true;
				while (rowIterator.hasNext() && rowNotEmpty) {
					Row row = rowIterator.next();
					Cell cell = row.getCell(0);
					if (row == null || cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
						rowNotEmpty = false;
					} else {
						if(row.getRowNum() == 0) {
							continue;
						}

						if(i == 0) { //Trainings
							User user = new User();
							user.setOracleId(String.format("%.0f", row.getCell(2).getNumericCellValue()));
							user.setName(row.getCell(3).getStringCellValue().trim());
							user.setEmail(row.getCell(4).getStringCellValue().trim());
							
							DecimalFormat format = new DecimalFormat("0.#");
							String hours = String.format("%.1f", row.getCell(6).getNumericCellValue());
							user.setClassroomLearningHours(format.format(Double.parseDouble(hours)));
							
							hours = String.format("%.1f", row.getCell(7).getNumericCellValue());
							user.setOnlineLearningHours(format.format(Double.parseDouble(hours)));
							
							user.setClassroomSessionCount(String.format("%.0f", row.getCell(8).getNumericCellValue()));
							user.setOnlineCourseCount(String.format("%.0f", row.getCell(9).getNumericCellValue()));
							
							user.setCertificationsDone(row.getCell(10).getStringCellValue().trim());
							
							String[] notAttended = row.getCell(11).getStringCellValue().split(";");
							List<String> enrolledButNotAttended = new ArrayList<String>();
							for(String str : notAttended)
								enrolledButNotAttended.add(str);
							
							user.setTotalLearningDays(String.format("%.0f", row.getCell(12).getNumericCellValue()));					
							
							user.setEnrollButNotAttendedList(enrolledButNotAttended);
							user.setClassroomSessions(new ArrayList<String>());
							user.setOnlineCourses(new ArrayList<String>());
							user.setUpskillingList(new ArrayList<Upskilling>());
							userRecordList.add(user);
						} else if(i == 1) {//Upskillings
							String oracleId = String.format("%.0f", row.getCell(0).getNumericCellValue());
							User user = new User();
							user.setOracleId(oracleId);
							int index =	userRecordList.indexOf(user);
							if(index > -1) {
								user = userRecordList.get(index);
								String programName =  row.getCell(1).getStringCellValue().trim();
								String status = "";
								if(row.getCell(2).getCellType() == Cell.CELL_TYPE_NUMERIC) {
									int d = (int)(row.getCell(2).getNumericCellValue() * 100);
									status = d + "%";
									
								}
								else {
									status = row.getCell(2).getStringCellValue().trim();
								}
								if(programName != null && !programName.isEmpty()) {
									List<Upskilling> upskilling = user.getUpskillingList();
									upskilling.add(new Upskilling(programName, status));
									user.setUpskillingList(upskilling);
								}
								
							}
							
						} else if(i == 2) {//Sessions
							String oracleId = String.format("%.0f", row.getCell(0).getNumericCellValue());
							User user = new User();
							user.setOracleId(oracleId);
							int index =	userRecordList.indexOf(user);
							if(index > -1) {
								user = userRecordList.get(index);
								String classroomSessions =  row.getCell(1).getStringCellValue().trim();
								String onlineCourse =  row.getCell(2).getStringCellValue().trim();

								if(classroomSessions != null && !classroomSessions.isEmpty()) {
									List<String> classroomSessionList = user.getClassroomSessions();
									classroomSessionList.add(classroomSessions);
									//user.setClassroomSessions(classroomSessionList);
								}
								
								if(onlineCourse != null && !onlineCourse.isEmpty()) {
									List<String> onlineCourseList = user.getOnlineCourses();
									onlineCourseList.add(onlineCourse);
									//user.setOnlineCourses(onlineCourseList);
								}
							}
						}
					}

				}
			}

			for(User user : userRecordList) {
				System.out.println(user);
			}
			System.out.println("Reading file completed successfully.");
		}
		catch (Exception e) {
			System.out.println("Error ocurred while reading file. Please ensure file name and path is correct.");
			e.printStackTrace();
		}
		return userRecordList;
	}

	public XSSFWorkbook generateExcel(User user) {
		XSSFWorkbook workbook = new XSSFWorkbook();
		try {

			//Create a blank sheet
			XSSFSheet sheet = workbook.createSheet("Classroom And Enrolled Courses");

			int rownum = 0;
			Row row = sheet.createRow(rownum++);
			int cellCount = 0;
			
			String sessionCount = "";
			sessionCount = user.getClassroomSessionCount() != null ? user.getClassroomSessionCount() : "";
			if(sessionCount != null && !sessionCount.isEmpty() && Double.parseDouble(sessionCount) > 0) {
				Cell cell = row.createCell(cellCount++);
				cell.setCellValue("Classroom Sessions Attended");
			}
			sessionCount = user.getOnlineCourseCount() != null ? user.getOnlineCourseCount() : "";
			if(sessionCount != null && !sessionCount.isEmpty() && Double.parseDouble(sessionCount) > 0) {
				Cell cell = row.createCell(cellCount++);
				cell.setCellValue("Online Courses Enrolled");
			}
			
			int i1 = 0, i2 = 0;
			int totalCell = cellCount;		
			while(i1 < user.getClassroomSessions().size() || i2 < user.getOnlineCourses().size()) {				
				row = sheet.createRow(rownum++);		
				
				cellCount = 0;
				if(i1 < user.getClassroomSessions().size()) {
					Cell cell = row.createCell(cellCount++);
					cell.setCellValue(user.getClassroomSessions().get(i1++));
				}
				cellCount = (totalCell == 2) ? 1 : 0;
				if(i2 < user.getOnlineCourses().size()) {
					Cell cell = row.createCell(cellCount);
					cell.setCellValue(user.getOnlineCourses().get(i2++));
				}
			}
		}
		catch (Exception e) {
			System.out.println("Error occured while writing file.");
			System.out.println(e.getMessage());
		}
		return workbook;
	}
}
