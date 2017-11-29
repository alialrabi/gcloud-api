package com.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.cloud.dlp.v2beta1.DlpServiceClient;
import com.google.privacy.dlp.v2beta1.ContentItem;
import com.google.privacy.dlp.v2beta1.InfoType;
import com.google.privacy.dlp.v2beta1.InspectConfig;
import com.google.privacy.dlp.v2beta1.Likelihood;
import com.google.privacy.dlp.v2beta1.RedactContentRequest.ReplaceConfig;
import com.google.privacy.dlp.v2beta1.RedactContentResponse;
import com.google.protobuf.ByteString;

public class Redact {
	
	
	  private static void redactString(
		      String string, String replacement, Likelihood minLikelihood, List<InfoType> infoTypes)
		      throws Exception {
		    // [START dlp_redact_string]
		    // Instantiate the DLP client
		    try (DlpServiceClient dlpClient = DlpServiceClient.create()) {
		      // The minimum likelihood required before returning a match
		      // eg.minLikelihood = LIKELIHOOD_VERY_LIKELY;
		      InspectConfig inspectConfig =
		          InspectConfig.newBuilder()
		              .addAllInfoTypes(infoTypes)
		              .setMinLikelihood(minLikelihood)
		              .build();

		      ContentItem contentItem =
		          ContentItem.newBuilder()
		              .setType("text/plain")
		              .setData(ByteString.copyFrom(string.getBytes()))
		              .build();

		      List<ReplaceConfig> replaceConfigs = new ArrayList<>();

		      if (infoTypes.isEmpty()) {
		        // replace all detected sensitive elements with replacement string
		        replaceConfigs.add(ReplaceConfig.newBuilder().setReplaceWith(replacement).build());
		      } else {
		        // Replace select info types with chosen replacement string
		        for (InfoType infoType : infoTypes) {
		          replaceConfigs.add(
		              ReplaceConfig.newBuilder().setInfoType(infoType).setReplaceWith(replacement).build());
		        }
		      }

		      RedactContentResponse contentResponse =
		          dlpClient.redactContent(
		              inspectConfig, Collections.singletonList(contentItem), replaceConfigs);
		      for (ContentItem responseItem : contentResponse.getItemsList()) {
		        // print out string with redacted content
		        System.out.println(responseItem.getData().toStringUtf8());
		      }
		    }
		    // [END dlp_redact_string]
		  }
	  
	  public static void main(String args[]) throws Exception {
		  
		   String source="Credit card number: 4012-8888-8888-1881 National Provider Identifier: 1245319599";
		   
		   String replacment="**Credit card number**";
		  
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

		   
		   
	      redactString(source, replacment, minLikelihood, infoTypes);

	  }
	  
	  

}
