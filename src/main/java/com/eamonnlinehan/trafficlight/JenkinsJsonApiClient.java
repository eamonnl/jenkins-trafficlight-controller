/*
 * (c) Copyright 2013 Brite:Bill Ltd.
 * 
 * 23 Windsor Place, Dublin 2, Ireland
 * info@britebill.com
 * +353 1 661 9426
 */
package com.eamonnlinehan.trafficlight;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.eamonnlinehan.trafficlight.JenkinsTrafficLightController.BuildStatus;

/**
 * Note: The admin user (Configured in Assembla Auth Plugin in Jenkins) is the only one that can
 * access the API on Jenkins and must use the API token configured at
 * http://build.britebill.com/jenkins/user/{admin-user}/configure
 * 
 * TODO: Make the Jenkins server configurable
 * 
 * @author <a href="mailto:eamonn.linehan@britebill.com">Eamonn Linehan</a>
 */
public class JenkinsJsonApiClient {

	private static final Logger log = Logger.getLogger(JenkinsJsonApiClient.class);

	private static final List<String> JOBS_TO_MONITOR = Arrays.asList(new String[] {"Platform TRUNK API",
					"Platform TRUNK API INT", "Platform TRUNK Compile Check", "Platform TRUNK Shared",
					"Platform TRUNK Web", "Platform TRUNK Ws"});

	private final HttpHost target;

	private final HttpClientContext localContext;

	private final CloseableHttpClient client;

	public JenkinsJsonApiClient(String username, String apiToken) {

		this.target = new HttpHost("build.britebill.com", 80, "http");
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(target.getHostName(), target.getPort()),
						new UsernamePasswordCredentials(username, apiToken));
		this.client = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();

		// Create AuthCache instance
		AuthCache authCache = new BasicAuthCache();
		// Generate BASIC scheme object and add it to the local
		// auth cache
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(target, basicAuth);

		// Add AuthCache to the execution context
		this.localContext = HttpClientContext.create();
		localContext.setAuthCache(authCache);

	}

	public BuildStatus getBuildStatus() throws IllegalStateException, ParseException, IOException {

		HttpGet request = new HttpGet("/jenkins/api/json");

		log.info(request.getRequestLine());

		HttpResponse response = client.execute(target, request, localContext);

		JSONParser parser = new JSONParser();
		JSONObject obj = (JSONObject) parser.parse(IOUtils.toString(response.getEntity().getContent()));
		JSONArray jobs = (JSONArray) obj.get("jobs");

		BuildStatus status = BuildStatus.SUCCESS;
		for (int i = 0; i < jobs.size(); i++) {
			Object jobName = ((JSONObject) jobs.get(i)).get("name");

			if (!JOBS_TO_MONITOR.contains(jobName)) continue;

			String color = (String) ((JSONObject) jobs.get(i)).get("color");

			BuildStatus jobStatus = toJobStatus(color);

			status = BuildStatus.min(status, jobStatus);

			log.info("Status for job '" + jobName + "' is " + jobStatus.name() + ". Updating return status to "
							+ status.name());
		}

		return status;
	}

	public BuildStatus toJobStatus(final String jobColor) {
		switch (jobColor) {
			case "blue":
				return BuildStatus.SUCCESS;
			case "blue_anime":
				return BuildStatus.SUCCESS_BUILDING;
			case "yellow":
				return BuildStatus.WARNING;
			case "yellow_anime":
				return BuildStatus.WARNING_BUILDING;
			case "aborted":
				return BuildStatus.WARNING;
			case "aborted_anime":
				return BuildStatus.WARNING_BUILDING;
			case "red":
				return BuildStatus.FAILURE;
			case "red_anime":
				return BuildStatus.FAILURE_BUILDING;
			case "disabled":
				return BuildStatus.UNKNOWN;
			default:
				return BuildStatus.UNKNOWN;
		}
	}

}
