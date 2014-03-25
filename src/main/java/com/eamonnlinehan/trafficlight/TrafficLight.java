/*
 * (c) Copyright 2013 Brite:Bill Ltd.
 * 
 * 23 Windsor Place, Dublin 2, Ireland
 * info@britebill.com
 * +353 1 661 9426
 */
package com.eamonnlinehan.trafficlight;

/**
 * @author <a href="mailto:eamonn.linehan@britebill.com">Eamonn Linehan</a>
 */
public interface TrafficLight {

	static enum Light {
		RED, AMBER, GREEN;
	}
	
	boolean isSignalOn(Light light);
	
	void signalOn(Light light);
	
	void signalOff(Light light);
	
}
