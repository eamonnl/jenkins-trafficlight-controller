/*
 * (c) Copyright 2013 Brite:Bill Ltd.
 * 
 * 23 Windsor Place, Dublin 2, Ireland
 * info@britebill.com
 * +353 1 661 9426
 */
package com.eamonnlinehan.trafficlight;

import org.apache.log4j.Logger;

/**
 * @author <a href="mailto:eamonn.linehan@britebill.com">Eamonn Linehan</a>
 */
public class MockTrafficLight extends AbstractTrafficLight {

	private static final Logger log = Logger.getLogger(MockTrafficLight.class);


	public MockTrafficLight() {
		super();

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

		if (isSignalOn(light)) return;

		log.debug("Signal on " + light.name());

		super.signalOn(light);

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

		if (!isSignalOn(light)) return;

		super.signalOff(light);

	}


}
