package com.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.cloud.dlp.v2beta1.DlpServiceClient;
import com.google.privacy.dlp.v2beta1.ContentItem;
import com.google.privacy.dlp.v2beta1.InfoType;
import com.google.privacy.dlp.v2beta1.InspectConfig;
import com.google.privacy.dlp.v2beta1.Likelihood;
import com.google.privacy.dlp.v2beta1.RedactContentResponse;
import com.google.privacy.dlp.v2beta1.RedactContentRequest.ReplaceConfig;
import com.google.protobuf.ByteString;

public class RedactService {
	
	  public String redactString(
		      String string, String replacement, Likelihood minLikelihood, List<InfoType> infoTypes)
		      throws Exception {
		    // [START dlp_redact_string]
		    // Instantiate the DLP client
		  String output="";
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
		        output=responseItem.getData().toStringUtf8();
		      }
		    }
		    return output;
		    // [END dlp_redact_string]
		  }

}
