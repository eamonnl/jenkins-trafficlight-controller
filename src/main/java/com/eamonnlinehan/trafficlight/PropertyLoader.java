package com.eamonnlinehan.trafficlight;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

public class PropertyLoader {

	private static final Properties PROPERTIES = PropertyLoader.loadProperties("jenkins.properties");

	public static final String USERNAME = "jenkins.username";
	public static final String API_TOKEN = "jenkins.apitoken";
	public static final String URL = "jenkins.url";
	public static final String JOBS = "jenkins.jobs";

	public static Properties loadProperties(String name) {

		Properties props = new Properties();

		InputStream classpath = null;
		try {
			classpath = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
			if (classpath != null)
				props.load(classpath);
		} catch (IOException e) {
			// Ignore - there may be properties on the filesystem
		} finally {
			IOUtils.closeQuietly(classpath);
		}

		FileInputStream file = null;
		try {
			file = new FileInputStream("./" + name);
			if (file != null)
				props.load(file);
		} catch (IOException e) {
			// Ignore error
		} finally {
			IOUtils.closeQuietly(file);
		}

		return props;
	}

	/**
	 * The username to authenticate with Jenkins
	 * 
	 * @return the username or null
	 */
	public static String getUsername() {
		return PROPERTIES.getProperty(USERNAME);
	}

	/**
	 * The Jenkins API token
	 * 
	 * @return the token or null
	 */
	public static String getApiToken() {
		return PROPERTIES.getProperty(API_TOKEN);
	}

	/**
	 * The URL for the Jenkins server
	 * 
	 * @return the url or null
	 */
	public static String getUrl() {
		return PROPERTIES.getProperty(URL);
	}

	/**
	 * Configures the list of Jenkins Jobs to be monitored. The build traffic
	 * light will only be green if all of these jobs are passing.
	 * 
	 * @return the Jenkins jobs to monitor (can be only one)
	 */
	public static String[] getJenkinsJobNames() {
		String jobs = PROPERTIES.getProperty(JOBS);

		if (jobs == null)
			return null;

		return jobs.split("\\s*,\\s*");
	}

}