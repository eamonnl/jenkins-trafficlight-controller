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
 * Sends a request to an actual Jenkins server to get the status of the
 * configured jobs.
 * 
 * @author <a href="mailto:eamonn.linehan@britebill.com">Eamonn Linehan</a>
 */
public class JenkinsJsonApiClientITTest {

	@Test
	public void testGetBuildStatus() throws IllegalStateException, ParseException, IOException {

		JenkinsJsonApiClient jenkins = new JenkinsJsonApiClient(PropertyLoader.getUrl(), PropertyLoader.getUsername(),
				PropertyLoader.getApiToken());

		BuildStatus status = jenkins.getBuildStatus();

		assertNotNull(status);

	}

}
