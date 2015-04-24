/*
 * (c) Copyright 2013 Brite:Bill Ltd.
 * 
 * 23 Windsor Place, Dublin 2, Ireland
 * info@britebill.com
 * +353 1 661 9426
 */
package com.eamonnlinehan.trafficlight;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.eamonnlinehan.trafficlight.JenkinsTrafficLightController.BuildStatus;

/**
 * Check we are correctly determining the minimum of two build status values.
 * This is used to decide what light to turn on when monitoring a group of jobs
 * which may each have a build status.
 * 
 * @author <a href="mailto:eamonn.linehan@britebill.com">Eamonn Linehan</a>
 */
public class JobStatusTest {

	@Test
	public void test() {

		assertEquals(BuildStatus.WARNING, BuildStatus.min(BuildStatus.SUCCESS, BuildStatus.WARNING));
		assertEquals(BuildStatus.FAILURE_BUILDING, BuildStatus.min(BuildStatus.FAILURE_BUILDING, BuildStatus.WARNING));
		assertEquals(BuildStatus.WARNING_BUILDING, BuildStatus.min(BuildStatus.WARNING, BuildStatus.WARNING_BUILDING));

	}

}
