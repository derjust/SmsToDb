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

import org.apache.log4j.Logger;

/**
 * @author Sebastian Just
 *
 */
public class Main {
	private static Logger logger = Logger.getRootLogger();

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			logger.warn("Use: java de.zeeman.smstodb.Main <propertiesfile.properties>");
			System.exit(-1);
		}
		
		logger.info("Load settings");
		Settings theSettings = new Settings(new File(args[0]));
		logger.info("Open database conneciton");
		DataBaseBridge db = new DataBaseBridge(theSettings);
		logger.info("Register eventlistener");
		GsmBrdige gsm = new GsmBrdige(theSettings);
		gsm.addSmsListener( db );
		logger.info("Liste to incomming SMS");
		gsm.start();
	}

}
