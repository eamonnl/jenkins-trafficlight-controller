/*
 * (c) Copyright 2013 Brite:Bill Ltd.
 * 
 * 23 Windsor Place, Dublin 2, Ireland
 * info@britebill.com
 * +353 1 661 9426
 */
package com.eamonnlinehan.trafficlight;

import java.util.HashMap;
import java.util.Map;


/**
 * 
 * @author <a href="mailto:eamonn.linehan@britebill.com">Eamonn Linehan</a>
 */
public abstract class AbstractTrafficLight implements TrafficLight {

	protected Map<Light, Boolean> lightStatus;

	public AbstractTrafficLight() {
		super();

		// Setup our state cache (Should prob query the device too)
		this.lightStatus = new HashMap<Light, Boolean>();
		for (Light light : TrafficLight.Light.values()) {
			this.lightStatus.put(light, Boolean.FALSE);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.eamonnlinehan.trafficlight.TrafficLight#isSignalOn(com.eamonnlinehan.trafficlight.
	 * TrafficLight.Light)
	 */
	public boolean isSignalOn(Light light) {
		return this.lightStatus.get(light);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.eamonnlinehan.trafficlight.TrafficLight#signalOn(com.eamonnlinehan.trafficlight.TrafficLight
	 * .Light)
	 */
	public void signalOn(Light light) {
		this.lightStatus.put(light, Boolean.TRUE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.eamonnlinehan.trafficlight.TrafficLight#signalOff(com.eamonnlinehan.trafficlight.TrafficLight
	 * .Light)
	 */
	@Override
	public void signalOff(Light light) {
		this.lightStatus.put(light, Boolean.FALSE);
	}
	
}
