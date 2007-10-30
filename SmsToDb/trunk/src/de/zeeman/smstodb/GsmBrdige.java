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

import java.util.Collection;
import java.util.LinkedList;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.smslib.IInboundMessageNotification;
import org.smslib.InboundMessage;
import org.smslib.Library;
import org.smslib.Service;
import org.smslib.Message.MessageTypes;
import org.smslib.gateway.ModemGateway;
import org.smslib.gateway.SerialModemGateway;

/**
 * @author Sebastian Just
 *
 */
public class GsmBrdige {

	private static final Logger logger = Logger.getLogger(GsmBrdige.class);

	private final Collection<SmsListener> listener = new Vector<SmsListener>();

	private final Settings theSettings;
	
	private Service smsService;
	
	public GsmBrdige(Settings theSettings) {
		this.theSettings = theSettings;
	}
	
	public void start() {
		smsService = new Service(logger);

		logger.info("SMSlib - Version: " + Library.LIB_VERSION + "." + Library.LIB_RELEASE + "." + Library.LIB_SUBRELEASE);

		ModemGateway gsmGateway = new SerialModemGateway("SmsToDb", theSettings.getPort(), 9600, theSettings.getVendor(), theSettings.getModel(), smsService.getLogger());

		gsmGateway.setInbound(true);
		gsmGateway.setOutbound(false);

		gsmGateway.setSimPin(theSettings.getPin());
		gsmGateway.setInboundNotification(new IInboundMessageNotification() {
			@Override
			public void process(String gatewayId, MessageTypes msgType, String memLoc, int memIndex) {
				LinkedList<InboundMessage> msgList = new LinkedList<InboundMessage>();

				switch (msgType) {
				case INBOUND:
					logger.debug(">>> New Inbound message detected from Gateway: " + gatewayId + " : " + memLoc + " @ " + memIndex);
					try {
						// Read...
						smsService.readMessages(msgList, InboundMessage.MessageClasses.UNREAD, gatewayId);
						for (SmsListener aListener : listener) {
							aListener.newSms(msgList);
						}
						for (InboundMessage msg : msgList) {
								logger.info("delete: " + msg);
								smsService.deleteMessage(msg);
						}
					} catch (Exception e) {
						logger.warn("Oops, some bad happened...");
						e.printStackTrace();
					}
					break;
				case STATUSREPORT:
					System.out.println(">>> New Status Report message detected from Gateway: " + gatewayId + " : " + memLoc + " @ " + memIndex);
					break;
				}
			}
		});

		smsService.addGateway(gsmGateway);

		boolean connected = false;
		do {
			try {
				smsService.startService();
				connected = true;

				logger.info("Modem Information:");
				logger.info("  Manufacturer: " + gsmGateway.getManufacturer());
				logger.info("  Model: " + gsmGateway.getModel());
				logger.info("  Serial No: " + gsmGateway.getSerialNo());
				logger.info("  SIM IMSI: " + gsmGateway.getImsi());
				logger.info("  Signal Level: " + gsmGateway.getSignalLevel() + "%");
				logger.info("  Battery Level: " + gsmGateway.getBatteryLevel() + "%");
			} catch (Exception ex) {
				logger.fatal("Can't start SMS service: " + ex.getLocalizedMessage());
				logger.fatal("Retry in 3 seconds.");
				ex.printStackTrace();
				try {
					Thread.sleep(3 * 1000);
				} catch (InterruptedException e) {}
			}
		} while (!connected);
	}
	
	public void addSmsListener(SmsListener newListener) {
		listener.add(newListener);
	}
}
