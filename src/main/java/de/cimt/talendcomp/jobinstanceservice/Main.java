/**
 * Copyright 2024 Jan Lolling jan.lolling@gmail.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cimt.talendcomp.jobinstanceservice;

import java.io.File;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.ee10.servlet.FilterHolder;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import io.prometheus.client.hotspot.DefaultExports;

public class Main {

	private static Logger log = LogManager.getLogger(Main.class);
	public static Server server = null;
	private static int port = 10000;
	private static boolean verbose = false;
	private static String propertiesFileDir = null;
	private static String buckets = null;
	private static String version = null;
	public static final String groupId = "de.cimt.talendcomp";
	public static final String artifactId = "job-instance-service";

	/**
	 * Start sequence for the service
	 * 
	 * @throws Exception
	 */
	public static void start() throws Exception {
		if (port < 1) {
			throw new IllegalArgumentException("Port must be greater 0");
		}
		server = new Server(port);
		MBeanContainer mbContainer = new MBeanContainer(ManagementFactory.getPlatformMBeanServer());
		server.addEventListener(mbContainer);
		server.addBean(mbContainer);
		server.setStopAtShutdown(true);
		ServletContextHandler context = new ServletContextHandler();
		context.setContextPath("/");
		server.setHandler(context);
		PrometheusMetricsFilter pm = new PrometheusMetricsFilter();
		pm.setTimebucketsStr(buckets);
		log.info("Add filter: PrometheusMetricsFilter at pattern: " + PrometheusMetricsFilter.path);
		context.addFilter(new FilterHolder(pm), PrometheusMetricsFilter.path, null);
		log.info("Add servlet: ShutdownServlet at path: " + ShutdownServlet.path);
		context.addServlet(new ServletHolder(new ShutdownServlet()), ShutdownServlet.path);
		log.info("Add servlet: PingServlet at path: " + PingServlet.path);
		context.addServlet(new ServletHolder(new PingServlet()), PingServlet.path);
		log.info("Add servlet: PrometheusMetricServlet at path: " + PrometheusMetricServlet.path);
		context.addServlet(new ServletHolder(new PrometheusMetricServlet()), PrometheusMetricServlet.path);
		DefaultExports.initialize();
		// Add JobInstanceServlet here

		// Start the webserver.
		log.info("Start server");
		server.start();
	}

	/**
	 * Stop the service
	 */
	public static void stop() {
		if (server != null) {
			try {
				server.stop();
			} catch (Exception e) {
				// ignore
			}
		}
		LogManager.shutdown();
	}

	public static void main(String[] args) throws Exception {
		Options options = new Options();
		options.addOption("b", "buckets", true, "Buckets for measure and count the request durations");
		options.addOption("f", "propertiesFile", false, "Properties file for connection settings");
		options.addOption("h", "help", false, "Print help to console, do nothing else.");
		options.addOption("p", "port", true, "Port of the server");
		options.addOption("v", "verbose", false, "Print statements to console");
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(options, args);
		String portStr = cmd.getOptionValue('p', String.valueOf(port));
		verbose = cmd.hasOption('v');
		boolean help = cmd.hasOption('h');
		version = readVersionNumber();
		if (help) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.setWidth(200);
			formatter.printHelp("java -Dlog4j.configurationFile=log4j2.xml -jar " + artifactId + "-" + version + ".jar",
			        options);
			System.exit(0);
		}
		checkLog4jConfigFile();
		try {
			port = Integer.valueOf(portStr);
		} catch (Exception e) {
			log.error("Cannot parse port number: " + portStr);
			HelpFormatter formatter = new HelpFormatter();
			formatter.setWidth(200);
			formatter.printHelp("java -Dlog4j.configurationFile=log4j2.xml -jar " + artifactId + "-" + version + ".jar",
			        options);
			System.exit(4);
		}
		propertiesFileDir = cmd.getOptionValue('f');
		buckets = cmd.getOptionValue('b');
		log.info("Configuring Job Instance Service (version " + version + ") at port: " + port);
		start();
	}

	public static String readVersionNumber() {
		String pomPropertyResource = "/META-INF/maven/" + groupId + "/" + artifactId + "/pom.properties";
		try {
			InputStream in = Main.class.getResourceAsStream(pomPropertyResource);
			if (in == null) {
				log.warn("Resource: " + pomPropertyResource + " not found!");
			} else {
				Properties mavenProps = new Properties();
				mavenProps.load(in);
				in.close();
				return mavenProps.getProperty("version");
			}
		} catch (Exception e) {
			log.warn("Load maven properties failed: " + e.getMessage(), e);
		}
		return null;
	}

	private static void checkLog4jConfigFile() {
		String filename = System.getProperty("log4j2.configurationFile", "log4j2.xml");
		if (filename == null || filename.trim().isEmpty()) {
			System.err.println(
			        "WARNING: No log4j configuration file given per environment variable log4j2.configurationFile. Log will only appears in console output and logger treshold is ERROR");
		} else {
			File f = new File(filename);
			if (f.exists() == false) {
				System.err.println("Given log4j2 config file: " + f.getAbsolutePath() + " does not exist! Abort.");
				System.exit(-1);
			}
		}
	}

}