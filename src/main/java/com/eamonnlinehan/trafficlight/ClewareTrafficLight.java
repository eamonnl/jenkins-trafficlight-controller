/*
 * (c) Copyright 2013 Brite:Bill Ltd.
 * 
 * 23 Windsor Place, Dublin 2, Ireland
 * info@britebill.com
 * +353 1 661 9426
 */
package com.eamonnlinehan.trafficlight;

import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * @author <a href="mailto:eamonn.linehan@britebill.com">Eamonn Linehan</a>
 */
public class ClewareTrafficLight extends AbstractTrafficLight {

	private static final Logger log = Logger.getLogger(ClewareTrafficLight.class);
	
	private final CommandLine commandLine;

	private int deviceSerialNumber;
	
	public ClewareTrafficLight() {
		super();
		
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
	 * @see
	 * com.eamonnlinehan.trafficlight.TrafficLight#signalOn(com.eamonnlinehan.trafficlight.TrafficLight
	 * .Light)
	 */
	@Override
	public void signalOn(Light light) {

		if (isSignalOn(light))
			return;
		
		try {
			
			log.debug("Signal on " + light.name());
			
			super.signalOn(light);
			
			commandLine.execute(new String[] {"sudo", "clewarecontrol", "-d",
							Integer.toString(this.deviceSerialNumber), "-c", "1", "-as",
							Integer.toString(this.getSwitchNumber(light)), "1"});


		} catch (IOException e) {
			log.error("Failed to change signal " + light.name() + " on.", e);
		} catch (InterruptedException e) {
			log.error("Failed to change signal " + light.name() + " on.", e);
		}

	}

	/* (non-Javadoc)
	 * @see com.eamonnlinehan.trafficlight.TrafficLight#signalOff(com.eamonnlinehan.trafficlight.TrafficLight.Light)
	 */
	@Override
	public void signalOff(Light light) {
		
		if (!isSignalOn(light))
			return;
		
		try {
			
			super.signalOff(light);

			commandLine.execute(new String[] {"sudo", "clewarecontrol", "-d",
							Integer.toString(this.deviceSerialNumber), "-c", "1", "-as",
							Integer.toString(this.getSwitchNumber(light)), "0"});
			
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

		} catch (IOException | InterruptedException e) {
			log.error("Failed to read device serial number", e);
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
