package com.api;

import java.util.Arrays;
import java.util.List;

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
	public String homePage(@ModelAttribute("api") API api, Model model) {
		

		System.out.println("111111111111111111111111111111111");
		/**
		String accessToken="ya29.c.El8MBUrRoc6Lv4NwP6Cc_yrjV6g8FQh3Z-prYmkO6bJgakTUOMyxDRS211P3EdjyBxPjeFQephb5Qh6n_AHgIWFCWn7gaKCcKBB_JfnJmoJxeH0mVmCPIa2ebFYT9rnzHA";
		GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
		Plus plus = new Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
		    .setApplicationName("dlp-redaction-poc")
		    .build();
		**/
		System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
		
		    // string to inspect
		    String text = api.getText();
		    
		    System.out.println("redact text is : "+text);

		    // The minimum likelihood required before returning a match:
		    // LIKELIHOOD_UNSPECIFIED, VERY_UNLIKELY, UNLIKELY, POSSIBLE, LIKELY, VERY_LIKELY, UNRECOGNIZED
		    Likelihood minLikelihood = Likelihood.VERY_LIKELY;

		    // The maximum number of findings to report (0 = server maximum)
		    int maxFindings = 0;

		    // The infoTypes of information to match
		    List<InfoType> infoTypes =
		        Arrays.asList(
		            InfoType.newBuilder().setName("US_MALE_NAME").build(),
		            InfoType.newBuilder().setName("US_FEMALE_NAME").build());

		    // Whether to include the matching string
		    boolean includeQuote = true;

		    // instantiate a client
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

		      // Inspect the text for info types
		      InspectContentResponse response = dlpServiceClient.inspectContent(request);

		      // Print the response
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
		return "inspect";
	}
}
