package com.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.service.RedactService;
import com.google.privacy.dlp.v2beta1.InfoType;
import com.google.privacy.dlp.v2beta1.Likelihood;

/**
 * 
 * @author ali
 *
 */
@Controller
public class App {


	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String homePage(Model model) {
		API api = new API();
		model.addAttribute("api" , api);
		return "index";
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String homePage(@ModelAttribute("api")API api , MultipartHttpServletRequest request,Model model) throws Exception {
		
 		 System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
		 String source = request.getParameter("file");
		 
		 RedactService redactService=new RedactService();
         System.out.println("redact text is : "+source);
     
         String replacment="**sensitive_Text**";
		  
		   Likelihood minLikelihood =Likelihood.valueOf(Likelihood.LIKELIHOOD_UNSPECIFIED.name());
		   
		   List<InfoType> infoTypes =
			        Arrays.asList(
			            InfoType.newBuilder().setName("US_HEALTHCARE_NPI").build(),
			            InfoType.newBuilder().setName("CANADA_OHIP").build(),
			            InfoType.newBuilder().setName("CREDIT_CARD_NUMBER").build(),
			            InfoType.newBuilder().setName("IBAN_CODE").build(),
			            InfoType.newBuilder().setName("SWIFT_CODE").build(),
			            InfoType.newBuilder().setName("US_SOCIAL_SECURITY_NUMBER").build(),
			            InfoType.newBuilder().setName("UK_TAXPAYER_REFERENCE").build(),
			            InfoType.newBuilder().setName("PHONE_NUMBER").build(),
			            InfoType.newBuilder().setName("GERMANY_PASSPORT").build(),
			            InfoType.newBuilder().setName("EMAIL_ADDRESS").build(),
			            InfoType.newBuilder().setName("US_DRIVERS_LICENSE_NUMBER").build(),
			            InfoType.newBuilder().setName("US_MALE_NAME").build(),
	                    InfoType.newBuilder().setName("US_FEMALE_NAME").build());
		   
		   
	     String output= redactService.redactString(source , replacment, minLikelihood, infoTypes);
	     
	     String path = "C:"+File.separator+"hello"+File.separator+"output"+".html";
         File convFile = new File(path);
         convFile.createNewFile(); 
         FileOutputStream fos = new FileOutputStream(convFile); 
         
         PrintStream out = new PrintStream(fos);
        	    out.print(output);
         fos.close();

		 return "index"; 
	}
}
