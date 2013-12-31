package com.codetosalvation.mongo.services.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

//@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AppErroResponse{
    private List<ErrorResponse> faultMessages;

    public void addFaultMessage(ErrorResponse errorResponse) {
    	if(faultMessages == null)
    		faultMessages = new ArrayList<ErrorResponse>();
    	faultMessages.add(errorResponse);
    }

	public List<ErrorResponse> getFaultMessages() {
		return faultMessages;
	}

	public void setFaultMessages(List<ErrorResponse> faultMessages) {
		this.faultMessages = faultMessages;
	}

}
