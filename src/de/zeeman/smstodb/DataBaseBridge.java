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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.smslib.InboundMessage;

/**
 * @author SebastianJust
 *
 */
public class DataBaseBridge implements SmsListener {
	private final static Logger logger = Logger.getLogger(DataBaseBridge.class);
	private final Connection con;
	private final PreparedStatement ps;
	
	public DataBaseBridge(Settings theSettings) throws SQLException, ClassNotFoundException {
		Class.forName  ("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection(theSettings.getDbUrl(), theSettings.getDbUser(), theSettings.getDbPassword());
        ps = con.prepareStatement("INSERT INTO `SmsToDb`.`Messages` (" +
        	"Id, Time , Originator, Recipient, Text) VALUES (NULL , ?, ?, ?, ?)");
	}
	
	@Override
	public void newSms(LinkedList<InboundMessage> msgList) {
		for (InboundMessage m : msgList) {
			try {
				ps.setTimestamp(1, new Timestamp(m.getDate().getTime()));
				ps.setString(2, m.getOriginator());
				ps.setString(3, m.getRecipient());
				ps.setString(4, m.getText());
			
				ps.execute();
			} catch (SQLException e) {
				logger.fatal("Can't write message: " + e.getMessage());
			}
		}
	}

}
