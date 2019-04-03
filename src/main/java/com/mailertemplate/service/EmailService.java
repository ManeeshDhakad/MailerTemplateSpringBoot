package com.mailertemplate.service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.mailertemplate.entity.Upskilling;
import com.mailertemplate.entity.User;

@Service
public class EmailService implements IEmailService{
	static final String Email_Template_File = "src/main/resources/email-template.html";
	static final String IMG_HEADER = "src/main/resources/static/images/header.jpg";
	static final String IMG_FOOTER = "src/main/resources/static/images/footer.jpg";

	@Autowired
	private JavaMailSender sender;

	public void sendEmail(String filePath) throws Exception {	
		ReadAndWriteExcel readAndWriteExcel = new ReadAndWriteExcel();
		List<User> userRecordList = readAndWriteExcel.readExcelFile(filePath);
		
		String templateLines[] = readEmailTemplate(Email_Template_File).toString().split("\\n");
		
		if(userRecordList == null || userRecordList.isEmpty()) {
			throw new Exception("No user record found!");
		}

		for(int i = 0; i < userRecordList.size(); i++) {
			User user = userRecordList.get(i);
			
			MimeMessage message = sender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setSubject(user.getName() + "'s Learning Dashboard - 2019");
			helper.setTo(user.getEmail());
			
			StringBuilder contentBuilder = new StringBuilder();
			
			String instructorLearningHours = user.getClassroomLearningHours() != null ? user.getClassroomLearningHours() : "";
			instructorLearningHours = (!instructorLearningHours.isEmpty() && Double.parseDouble(instructorLearningHours) == 0) ? "" : instructorLearningHours;
			String onlineLearningHours = user.getOnlineLearningHours() != null ? user.getOnlineLearningHours() : "";
			onlineLearningHours = (!onlineLearningHours.isEmpty() && Double.parseDouble(onlineLearningHours) == 0) ? "" : onlineLearningHours;
			String totalDays = user.getTotalLearningDays() != null ? user.getTotalLearningDays() : "0";

			int trColor = 1;
			for (int s = 0; s < templateLines.length; s++) {	
				String currLine = templateLines[s];
				
				//Update total learning days
				if(currLine.contains("totalLearningDays")) {
					currLine = currLine.replace("totalLearningDays", totalDays);
				} 
				
				//Update user name
				if(currLine.contains("userName")) {
					currLine = currLine.replace("userName", user.getName().split(" ")[0]);
				}

				//Update Instructor Training Learning Hours
				if(currLine.contains("trInstructorTrainingLearningHours")) {
					while(!currLine.contains("</tr>")) {
						if(currLine.contains("_instructorTrainingLearningHours")) {
							currLine = currLine.replace("_instructorTrainingLearningHours", instructorLearningHours);
						} 
						contentBuilder.append(currLine);
						currLine = templateLines[++s];
					}
					contentBuilder.append(currLine);
					currLine = "";
				}

				//Update Instructor Training Session Data
				if(currLine.contains("trInstructorTrainingSessionData")) {
					while(!currLine.contains("</tr>")) {
						String data = user.getClassroomSessionCount() != null ? user.getClassroomSessionCount() : "";
						data = (!data.isEmpty() && Double.parseDouble(data) == 0) ? "" : data;
						if(currLine.contains("_instructorTrainingSessionData")) {
							currLine = currLine.replace("_instructorTrainingSessionData", data);
						}
						if(currLine.contains("(Excel file attached)") && data.isEmpty()) {
							currLine = currLine.replace("(Excel file attached)", "");
						}
						contentBuilder.append(currLine);
						currLine = templateLines[++s];
					}
					contentBuilder.append(currLine);
					currLine = "";
				}

				//Update Online Training Learning Hours
				if(currLine.contains("trOnlineTrainingLearningHours")) {
					while(!currLine.contains("</tr>")) {
						if(currLine.contains("_onlineTrainingLearningHours")) {
							currLine = currLine.replace("_onlineTrainingLearningHours", onlineLearningHours);	
						}
						contentBuilder.append(currLine);
						currLine = templateLines[++s];
					}
					contentBuilder.append(currLine);
					currLine = "";
				}


				//Update Online Training Session Data
				if(currLine.contains("trOnlineTrainingSessionData")) {
					
					while(!currLine.contains("</tr>")) {
						String data = user.getOnlineCourseCount() != null ? user.getOnlineCourseCount() : "";
						data = (!data.isEmpty() && Double.parseDouble(data) == 0) ? "" : data;
						if(currLine.contains("_onlineTrainingSessionData")) {
							currLine = currLine.replace("_onlineTrainingSessionData", data);		    				
						}
						if(currLine.contains("(Excel file attached)") && data.isEmpty()) {
							currLine = currLine.replace("(Excel file attached)", "");
						}
						contentBuilder.append(currLine);
						currLine = templateLines[++s];
					
					}
					contentBuilder.append(currLine);
					currLine = "";
				}

				// Update Upskilling
				if(currLine.contains("trUpskillingCert")) {
					String upskillingRow = "\n";
					while(!currLine.contains("</tr>")) {
						upskillingRow = upskillingRow + currLine + "\n";
						currLine = templateLines[++s];
						
					}
					int size = isUpskillingListNotEmpty(user.getUpskillingList())? user.getUpskillingList().size() : 1;
					upskillingRow = upskillingRow + "\n" + currLine;
					upskillingRow = upskillingRow.replace("rowspan='2'", "rowspan='" + (size + 1) + "'");
					contentBuilder.append(upskillingRow);
					
					if(isUpskillingListNotEmpty(user.getUpskillingList())) {
						for(int j = 0; j < user.getUpskillingList().size(); j++) {
							
							String programName = user.getUpskillingList().get(j).getProgramName() != null? user.getUpskillingList().get(j).getProgramName() : "";
							String status = user.getUpskillingList().get(j).getStatus() != null ? user.getUpskillingList().get(j).getStatus() : "";
							upskillingRow = "<tr style='height:18px;" + ((trColor % 2 == 0) ? "background-color: #acbdeb;" : "") + "'>\r\n" + 
									"<td colspan='2' style='border: 2px solid #6f93f3;'>" + programName + "</td>\r\n" + 
									"<td style='text-align: center;border-bottom: 2px solid #6f93f3'>" + status + " </td>\r\n</tr>\n";
							trColor++;
							contentBuilder.append(upskillingRow); 
						}
					} else {
						upskillingRow = "<tr style='height:18px;'>\r\n" + 
										"<td colspan='2' style='border: 2px solid #6f93f3;'></td>\r\n" + 
										"<td style='text-align: center;border-bottom: 2px solid #6f93f3'></td>\r\n</tr>\n";
						contentBuilder.append(upskillingRow);
						trColor++;
					}
					currLine = "";
				}

				
				//Update Certification Done
				if(currLine.contains("trcertificationDone")) {
					String certificationDoneRow = "\n";
					while(!currLine.contains("</tr>")) {
						certificationDoneRow = certificationDoneRow + currLine + "\n";
						currLine = templateLines[++s];
					}
		
					certificationDoneRow = certificationDoneRow + "\n" + currLine;
					certificationDoneRow = certificationDoneRow.replace("certificationDone", user.getCertificationsDone());
					if((trColor % 2 == 0)) 
						certificationDoneRow = certificationDoneRow.replace("style='height:18px;", "style='height:18px;background-color: #acbdeb;");
						
					contentBuilder.append(certificationDoneRow);
					trColor++;
					currLine = "";
				}

				// Update trEnrolledButNotAttended1 - enrolledButNotAttended1
				if(currLine.contains("trEnrolledButNotAttended")) {
					String enrollButNotAttendedRow = "\n";
					while(!currLine.contains("</tr>")) {
						enrollButNotAttendedRow = enrollButNotAttendedRow + currLine + "\n";
						currLine = templateLines[++s];
					}

					int size = isStrListNotEmpty(user.getEnrollButNotAttendedList())? user.getEnrollButNotAttendedList().size() : 1;
					
					String firstProgramName = isStrListNotEmpty(user.getEnrollButNotAttendedList()) ? user.getEnrollButNotAttendedList().get(0) : ""; 
					enrollButNotAttendedRow = enrollButNotAttendedRow + "\n" + currLine;
					enrollButNotAttendedRow = enrollButNotAttendedRow.replace("rowspan='1'", "rowspan='" + size + "'");
					enrollButNotAttendedRow = enrollButNotAttendedRow.replace("enrolledButNotAttended1", firstProgramName);
					if((trColor % 2 == 0)) 
						enrollButNotAttendedRow = enrollButNotAttendedRow.replace("style='height:18px;", "style='height:18px;background-color: #acbdeb;");
						
					contentBuilder.append(enrollButNotAttendedRow);
					trColor++;
					
					if(isStrListNotEmpty(user.getEnrollButNotAttendedList())) {
						for(int j = 1; j < user.getEnrollButNotAttendedList().size(); j++) {
							String courseName = user.getEnrollButNotAttendedList().get(j);
							
							enrollButNotAttendedRow = "<tr style='height:18px;border: 2px solid #6f93f3;" + ((trColor % 2 == 0) ? "background-color: #acbdeb;" : "") + "'>\r\n" + 
													  "<td colspan='3' style='border: 2px solid #6f93f3'>" + courseName + "</td>\r\n</tr>\n";
							
							contentBuilder.append(enrollButNotAttendedRow);	
							trColor++;
						}
					}
					currLine = "";
				}

				contentBuilder.append(currLine);
			}

			String htmlBody = contentBuilder.toString();


			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Now set the actual message   //text/html; charset=utf-8
			messageBodyPart.setContent(htmlBody, "text/html");

			// Create a multipar message
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Part two is attachment
			XSSFWorkbook excelFile = readAndWriteExcel.generateExcel(user);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			excelFile.write(bos); // write excel data to a byte array
			bos.close();

			// Now use your ByteArrayDataSource as
			DataSource fds = new ByteArrayDataSource(bos.toByteArray(), "application/vnd.ms-excel");
			messageBodyPart = new MimeBodyPart();
			messageBodyPart.setDataHandler(new DataHandler(fds));
			messageBodyPart.setFileName(user.getName().split(" ")[0] + "_" + user.getOracleId() + "_LnOD_Sessions_Data_" + new Date().getYear()+ ".xlsx");
			multipart.addBodyPart(messageBodyPart);
			
			//Header image 
			messageBodyPart = new MimeBodyPart();
			DataSource fdsImage = new FileDataSource(IMG_HEADER);
			messageBodyPart.setDataHandler(new DataHandler(fdsImage));
			messageBodyPart.setHeader("Content-ID","<headerImage>");
			multipart.addBodyPart(messageBodyPart);
			
			//Header image 
			messageBodyPart = new MimeBodyPart();
			fdsImage = new FileDataSource(IMG_FOOTER);
			messageBodyPart.setDataHandler(new DataHandler(fdsImage));
			messageBodyPart.setHeader("Content-ID","<footerImage>");
			multipart.addBodyPart(messageBodyPart);
			
			//Send the complete message parts
			message.setContent(multipart);
			System.out.println("Sending mail to " + user.getName());
			sender.send(message);
		} 
	}
	
	String calculateTotalDays(String h1, String h2) {
		double hours1, hours2;
		if(h1 != null && !h1.isEmpty()) {
			hours1 = Double.parseDouble(h1);
		} else {
			hours1 = 0;
		}
		
		if(h2 != null && !h2.isEmpty()) {
			hours2 = Double.parseDouble(h2);
		} else {
			hours2 = 0;
		}
		
		return new String("" + ((hours1 + hours2)/8));
	}

	boolean isUpskillingListNotEmpty(List<Upskilling> list) {
		return (list != null && !list.isEmpty());
	}
	
	boolean isStrListNotEmpty(List<String> list) {
		return (list != null && !list.isEmpty());
	}
	
	private StringBuilder readEmailTemplate(String file) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(
				new FileInputStream(file)));
		String currLine;
		StringBuilder contentBuilder = new StringBuilder();

		while ((currLine = in.readLine()) != null) {
			contentBuilder.append(currLine + "\n");
		}
		in.close();
		return contentBuilder;
	}
}