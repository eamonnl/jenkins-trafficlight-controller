/*
 * (c) Copyright 2013 Brite:Bill Ltd.
 * 
 * 23 Windsor Place, Dublin 2, Ireland
 * info@britebill.com
 * +353 1 661 9426
 */
package com.eamonnlinehan.trafficlight;

import java.io.IOException;

import org.json.simple.parser.ParseException;
import org.junit.Test;

import com.eamonnlinehan.trafficlight.JenkinsTrafficLightController.BuildStatus;

/**
 * @author <a href="mailto:eamonn.linehan@britebill.com">Eamonn Linehan</a>
 */
public class JenkinsJsonApiClientITTest {

	@Test
	public void test() throws IllegalStateException, ParseException, IOException {
		
		JenkinsJsonApiClient jenkins = new JenkinsJsonApiClient("eamonnlinehan", "dc97c72d0d339ee169c9a290d6ac5f81");
		
		BuildStatus status = jenkins.getBuildStatus();
		
	}

}
