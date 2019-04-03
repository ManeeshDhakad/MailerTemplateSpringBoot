package com.mailertemplate.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mailertemplate.entity.ExcelFile;
import com.mailertemplate.service.IEmailService;
import com.mailertemplate.service.IMergeExcelFiles;

@Controller  
public class UserController {  
	@Autowired
	IEmailService emailService;
	
	@Autowired
	IMergeExcelFiles mergeExcelFiles;
	
	
	@RequestMapping(value="/", method = RequestMethod.GET)
    public String showHomePage(ModelMap model){
		model.addAttribute("message", "Welcome to Mailer Template");
        return "index";
    } 
	
	@RequestMapping(value="/error", method = RequestMethod.GET)
    public String error(ModelMap model){
		model.addAttribute("message", "Welcome to Mailer Template");
        return "index";
    } 
	
	
	@RequestMapping(value = "/learning-email/file", method = RequestMethod.POST)
	public  @ResponseBody String  learningEmail(@RequestBody ExcelFile file, HttpServletRequest request) {
		String message = "Learning emails sent successfully.";
		try {
			System.out.println("Excel Path = " + file.getFullPath());
			emailService.sendEmail(file.getFullPath());
			
			System.out.println("");
		} catch (Exception e) {
			message = "Error ocurred while sending Learning emails";
			System.out.println("******** Error occured while send mail : " + e.getMessage());
			e.printStackTrace();
		}
		return message;
	}
	
	@RequestMapping(value = "/merge-excel/files", method = RequestMethod.POST)
	public  @ResponseBody String  mergeExcelFiles(@RequestBody ExcelFile file, HttpServletRequest request) {
		String message = "Merging completed successfully. New consolidate Excel generated for classroom and online training data.";
		try {
			System.out.println("Excel 1 path = " + file.getFullPath());
			System.out.println("Excel 2 path = " + file.getFullPath2());
			mergeExcelFiles.mergeFiles(file);
		} catch (Exception e) {
			message = "Error ocurred while merging excel files.";
			System.out.println("******** Error occured while send mail : " + e.getMessage());
			e.printStackTrace();
		}
		return message;
	}
	
	
//	@PostMapping(value="/learning-email/file")
//	public String singleFileUpload(@RequestParam("excelFile") MultipartFile excelFile) {
//		if (excelFile.isEmpty()) {
//			System.out.println("File is empty of invalid file.");
//            return "File is empty of invalid file.";
//        }
//		
//		String message = "Learning emails sent successfully.";
//		try {
//			//byte[] bytes = file.getBytes();
//			//FileUtils.writeByteArrayToFile(new File("pathname"), excelFile.getBytes())
//			//InputStream inputStream =  new BufferedInputStream(excelFile.getInputStream());
//			System.out.println("Excel Name = " + excelFile.getOriginalFilename());
//			emailService.sendEmail(excelFile);
//			
//			System.out.println("");
//		} catch (Exception e) {
//			message = "Error ocurred while sending Learning emails";
//			System.out.println("******** Error occured while send mail : " + e.getMessage());
//			e.printStackTrace();
//		}
//		return message;
//    }
}  