/*
 * (c) Copyright 2013 Brite:Bill Ltd.
 * 
 * 23 Windsor Place, Dublin 2, Ireland
 * info@britebill.com
 * +353 1 661 9426
 */
package com.eamonnlinehan.trafficlight;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * 
 * @author <a href="mailto:eamonn.linehan@britebill.com">Eamonn Linehan</a>
 */
public class CommandLine {

	private static final Logger log = Logger.getLogger(CommandLine.class);


	public synchronized String execute(String[] command) throws IOException, InterruptedException {

		log.debug("Executing command " + StringUtils.join(command, " "));

		Process process = new ProcessBuilder(command).start();

		return this.executeProcess(process, true);

	}

	/**
	 * Reads the processes streams.
	 * 
	 * @param process the executing process to read streams from
	 * @param failOnError if true an exception will be thrown if the process exits with an error
	 *        code
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private String executeProcess(Process process, boolean failOnError) throws IOException, InterruptedException {
		StringBuffer stdOutBuffer = new StringBuffer();
		StringBuffer stdErrBuffer = new StringBuffer();

		try {
			// Read what the process outputs
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				stdOutBuffer.append(line);
				stdOutBuffer.append('\n');
			}
			input.close();

			BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			String errorLine = null;
			while ((errorLine = error.readLine()) != null) {
				stdErrBuffer.append(errorLine);
				stdErrBuffer.append(" ");
			}
			error.close();

			process.waitFor();

			if (failOnError && process.exitValue() != 0)
				throw new IllegalStateException("Forked process '" + process.toString() + "' exited with error code "
								+ process.exitValue() + ". " + stdErrBuffer.toString());

		} finally {
			// Close process streams
			if (process != null) {
				IOUtils.closeQuietly(process.getOutputStream());
				IOUtils.closeQuietly(process.getInputStream());
				IOUtils.closeQuietly(process.getErrorStream());
			}
		}

		return stdOutBuffer.toString();
	}

}
