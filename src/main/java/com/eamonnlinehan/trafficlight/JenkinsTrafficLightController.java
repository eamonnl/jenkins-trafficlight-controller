/*
 * (c) Copyright 2013 Brite:Bill Ltd.
 * 
 * 23 Windsor Place, Dublin 2, Ireland
 * info@britebill.com
 * +353 1 661 9426
 */
package com.eamonnlinehan.trafficlight;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.eamonnlinehan.trafficlight.TrafficLight.Light;

/**
 * Checks the Jenkins build status once a minute and updates the lights once every two seconds.
 * 
 * @author <a href="mailto:eamonn.linehan@britebill.com">Eamonn Linehan</a>
 */
public class JenkinsTrafficLightController {

	private static final Logger log = Logger.getLogger(JenkinsTrafficLightController.class);

	private static BuildStatus buildStatus = BuildStatus.UNKNOWN;

	/**
	 * @param args
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException, ExecutionException {


		if (args == null || args.length != 2) {
			log.error("You must supply a Jenkins username and api token to this program.");
			System.exit(0);
		}

		final TrafficLight light = new ClewareTrafficLight();

		final JenkinsJsonApiClient jenkins = new JenkinsJsonApiClient(args[0], args[1]);

		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

		// Once a minute find the jenkins status
		scheduler.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {

				try {
					buildStatus = jenkins.getBuildStatus();
				} catch (Throwable e) {
					log.error("Failed to update build status from Jenkins", e);
					buildStatus = BuildStatus.UNKNOWN;
				}

			}
		}, 15, 60, TimeUnit.SECONDS);

		log.info("Scheduled Jenkins build status update thread.");

		// Controller - Once a second check the cached status and update the traffic light if
		// necessary
		scheduler.scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {

				try {
					
					log.debug("Updating light for build status " + buildStatus.name());

					switch (buildStatus) {
						case SUCCESS: {
							light.signalOn(Light.GREEN);
							light.signalOff(Light.AMBER);
							light.signalOff(Light.RED);
							break;
						}
						case SUCCESS_BUILDING: {
							if (light.isSignalOn(Light.GREEN))
								light.signalOff(Light.GREEN);
							else
								light.signalOn(Light.GREEN);
							light.signalOff(Light.AMBER);
							light.signalOff(Light.RED);
							break;
						}
						case WARNING: {
							light.signalOff(Light.GREEN);
							light.signalOn(Light.AMBER);
							light.signalOff(Light.RED);
							break;
						}
						case UNKNOWN:
						case WARNING_BUILDING: {
							light.signalOff(Light.GREEN);
							if (light.isSignalOn(Light.AMBER))
								light.signalOff(Light.AMBER);
							else
								light.signalOn(Light.AMBER);
							light.signalOff(Light.RED);
							break;
						}
						case FAILURE: {
							light.signalOff(Light.GREEN);
							light.signalOff(Light.AMBER);
							light.signalOn(Light.RED);
							break;
						}
						case FAILURE_BUILDING: {
							light.signalOff(Light.GREEN);
							light.signalOff(Light.AMBER);
							if (light.isSignalOn(Light.RED))
								light.signalOff(Light.RED);
							else
								light.signalOn(Light.RED);
							break;
						}
					}

				} catch (Throwable t) {
					log.error("Failed to command traffic light.", t);
				}

			}
		}, 0, 2, TimeUnit.SECONDS);
		
		log.info("Scheduled Traffic Light update thread.");
		
		boolean terminated = true;
		try {
			terminated = scheduler.awaitTermination(30, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			log.error("Scheduler interrupted.", e);
		}
		
		if (!terminated)
			log.warn("Controller exited due to timeout.");
		else
			log.error("Controller exited after threads finished.");
		
	}


	public static enum BuildStatus {
		SUCCESS, SUCCESS_BUILDING, WARNING, WARNING_BUILDING, FAILURE, FAILURE_BUILDING, UNKNOWN;

		public static BuildStatus min(BuildStatus a, BuildStatus b) {
			int minI = -1;
			for (int i = 0; i < BuildStatus.values().length; i++) {
				if (BuildStatus.values()[i].equals(a)) minI = Math.max(minI, i);
				if (BuildStatus.values()[i].equals(b)) minI = Math.max(minI, i);
			}
			return BuildStatus.values()[minI];
		}
	}
}
