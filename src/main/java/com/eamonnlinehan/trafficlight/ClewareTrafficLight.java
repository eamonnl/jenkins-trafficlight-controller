/*
 * (c) Copyright 2013 Brite:Bill Ltd.
 * 
 * 23 Windsor Place, Dublin 2, Ireland
 * info@britebill.com
 * +353 1 661 9426
 */
package com.eamonnlinehan.trafficlight;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * @author <a href="mailto:eamonn.linehan@britebill.com">Eamonn Linehan</a>
 */
public class ClewareTrafficLight implements TrafficLight {

	private static final Logger log = Logger.getLogger(ClewareTrafficLight.class);

	private CommandLine commandLine;

	private int deviceSerialNumber;
	
	private Map<Light, Boolean> lightStatus;

	public ClewareTrafficLight() {

		// Setup our state cache (Should prob query the device too)
		this.lightStatus = new HashMap<Light, Boolean>();
		for (Light light : TrafficLight.Light.values()) {
			this.lightStatus.put(light, Boolean.FALSE);
		}
		
		this.commandLine = new CommandLine();

		// 1. Discover the device ID
		this.deviceSerialNumber = this.getDeviceSerialNumber();

		log.info("Using USB device serial number " + this.deviceSerialNumber);

		// 2. Make sure all lights are turned off (And do a nice flash)
		for (int i = 0; i < 2; i++) {

			for (Light light : TrafficLight.Light.values()) {
				this.signalOn(light);
			}

			for (Light light : TrafficLight.Light.values()) {
				this.signalOff(light);
			}

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.eamonnlinehan.trafficlight.TrafficLight#isSignalOn(com.eamonnlinehan.trafficlight.
	 * TrafficLight.Light)
	 */
	public synchronized boolean isSignalOn(Light light) {
		return this.lightStatus.get(light);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.eamonnlinehan.trafficlight.TrafficLight#signalOn(com.eamonnlinehan.trafficlight.TrafficLight
	 * .Light)
	 */
	public synchronized void signalOn(Light light) {

		if (isSignalOn(light))
			return;
		
		try {
			
			commandLine.execute(new String[] {"sudo", "clewarecontrol", "-d",
							Integer.toString(this.deviceSerialNumber), "-c", "1", "-as",
							Integer.toString(this.getSwitchNumber(light)), "1"});
			
			this.lightStatus.put(light, Boolean.TRUE);

		} catch (IOException e) {
			log.error("Failed to change signal " + light.name() + " on.", e);
		} catch (InterruptedException e) {
			log.error("Failed to change signal " + light.name() + " on.", e);
		}

	}

	public void signalOff(Light light) {
		
		if (!isSignalOn(light))
			return;
		
		try {

			commandLine.execute(new String[] {"sudo", "clewarecontrol", "-d",
							Integer.toString(this.deviceSerialNumber), "-c", "1", "-as",
							Integer.toString(this.getSwitchNumber(light)), "0"});
			
			this.lightStatus.put(light, Boolean.FALSE);

		} catch (IOException e) {
			log.error("Failed to change signal " + light.name() + " on.", e);
		} catch (InterruptedException e) {
			log.error("Failed to change signal " + light.name() + " on.", e);
		}
	}


	private int getDeviceSerialNumber() {

		String serial = null;
		try {
			serial = this.commandLine.execute(new String[] {"sudo", "clewarecontrol", "-l"});

			log.info(serial);

			// Take the last number in string

			String[] splitOutput = serial.split("\\s+");

			return Integer.parseInt(splitOutput[splitOutput.length - 1]);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	private int getSwitchNumber(Light light) {
		switch (light) {
			case RED:
				return 0;
			case AMBER:
				return 1;
			case GREEN:
				return 2;
			default:
				throw new IllegalArgumentException("Unknown traffic light led " + light.name());
		}
	}



}
