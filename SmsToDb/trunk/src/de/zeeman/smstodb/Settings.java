/* This file is part of SmsToDb.
 *
 *  beamer-tool is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  SmsToDb is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with beamer-tool.  If not, see <http://www.gnu.org/licenses/>.
 *  
 *  Copyright (C) 2007 by Sebastian Just <zeeman@zeeman.de>
 */
package de.zeeman.smstodb;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author Sebastian Just
 *
 */
public class Settings {
	private final static Logger logger = Logger.getLogger(Settings.class);
	private final String port;
	private final String model;
	private final String vendor;
	private final String pin;
	private final String dbUrl;
	private final String dbPassword;
	private final String dbUser;
	
	public Settings(File propertiesFile) throws IOException {
		Properties p = new Properties();
		InputStream is = new FileInputStream(propertiesFile);
		p.load(is);
		is.close();
		
		port = p.getProperty("port");
		model = p.getProperty("model");
		vendor = p.getProperty("vendor");
		pin = p.getProperty("pin");
		dbUrl = p.getProperty("dbUrl");
		dbUser = p.getProperty("dbUser");
		dbPassword = p.getProperty("dbPassword");
		
		logger.debug("Loaded settings:");
		logger.debug("Port:\t" + port);
		logger.debug("Vendor:\t" + vendor);
		logger.debug("Model:\t" + model);
		logger.debug("Pin:\t" + pin);
		logger.debug("DbUrl:\t" + dbUrl);
		logger.debug("DbUser:\t" + dbUser);
		logger.debug("DbPassword:\t" + dbPassword);
	}
	
	public String getPort() {
		return port;
	}

	public String getModel() {
		return model;
	}

	public String getVendor() {
		return vendor;
	}

	public String getPin() {
		return pin;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public String getDbUser() {
		return dbUser;
	}
}
