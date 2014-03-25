/*
 * (c) Copyright 2013 Brite:Bill Ltd.
 * 
 * 23 Windsor Place, Dublin 2, Ireland
 * info@britebill.com
 * +353 1 661 9426
 */
package com.eamonnlinehan.trafficlight;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.json.simple.parser.ParseException;
import org.junit.Test;

import com.eamonnlinehan.trafficlight.JenkinsTrafficLightController.BuildStatus;

/**
 * Check the credentials are correct and a JSON response is coming back and being parsed.
 * 
 * @author <a href="mailto:eamonn.linehan@britebill.com">Eamonn Linehan</a>
 */
public class JenkinsJsonApiClientTest {

	@Test
	public void test() throws IllegalStateException, ParseException, IOException {

		JenkinsJsonApiClient client = new JenkinsJsonApiClient("eamonnlinehan", "69cfa7b13574f541f5466e9752bd008d");
		
		BuildStatus status = client.getBuildStatus();
		
		assertNotNull(status);
	}

}
