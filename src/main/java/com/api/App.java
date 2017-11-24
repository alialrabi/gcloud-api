package com.api;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.cloud.dlp.v2beta1.DlpServiceClient;
import com.google.privacy.dlp.v2beta1.ContentItem;
import com.google.privacy.dlp.v2beta1.Finding;
import com.google.privacy.dlp.v2beta1.InfoType;
import com.google.privacy.dlp.v2beta1.InspectConfig;
import com.google.privacy.dlp.v2beta1.InspectContentRequest;
import com.google.privacy.dlp.v2beta1.InspectContentResponse;
import com.google.privacy.dlp.v2beta1.InspectResult;
import com.google.privacy.dlp.v2beta1.Likelihood;

@Controller
public class App {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String homePage(Model model) {
		API api = new API();
		model.addAttribute("api", api);
		return "index";
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String homePage(@ModelAttribute("api") API api, Model model , HttpServletRequest httpRequest) {
		
 		 System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
		 String text = api.getText();
		    
         System.out.println("redact text is : "+text);
         Likelihood minLikelihood = Likelihood.VERY_LIKELY;
		    int maxFindings = 0;

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

		    boolean includeQuote = true;

		    try {
		     DlpServiceClient dlpServiceClient = DlpServiceClient.create();
		      InspectConfig inspectConfig =
		          InspectConfig.newBuilder()
		              .addAllInfoTypes(infoTypes)
		              .setMinLikelihood(minLikelihood)
		              .setMaxFindings(maxFindings)
		              .setIncludeQuote(includeQuote)
		              .build();

		      ContentItem contentItem =
		          ContentItem.newBuilder().setType("text/plain").setValue(text).build();

		      InspectContentRequest request =
		          InspectContentRequest.newBuilder()
		              .setInspectConfig(inspectConfig)
		              .addItems(contentItem)
		              .build();

		      InspectContentResponse response = dlpServiceClient.inspectContent(request);

		      for (InspectResult result : response.getResultsList()) {
		        if (result.getFindingsCount() > 0) {
		          System.out.println("Findings: ");
		          for (Finding finding : result.getFindingsList()) {
		            if (includeQuote) {
		              System.out.print("Quote: " + finding.getQuote());
		            }
		            System.out.print("\tInfo type: " + finding.getInfoType().getName());
		            System.out.println("\tLikelihood: " + finding.getLikelihood());
		    		model.addAttribute("text", finding.getInfoType().getName());
		          }
		        } else {
		          System.out.println("No findings.");
		        }
		      }
		    } catch (Exception e) {
		      System.out.println("Error in inspectString: " + e.getMessage());
		    }
		    return "index"; 
	}
}
