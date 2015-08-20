package com.sanploy.Printer.print;

/*
 * @(#)SimpleWrite.java	1.12 98/06/25 SMI
 * 
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license
 * to use, modify and redistribute this software in source and binary
 * code form, provided that i) this copyright notice and license appear
 * on all copies of the software; and ii) Licensee does not utilize the
 * software in a manner which is disparaging to Sun.
 * 
 * This software is provided "AS IS," without a warranty of any kind.
 * ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES,
 * INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND
 * ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY
 * LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THE
 * SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS
 * BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES,
 * HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING
 * OUT OF THE USE OF OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * This software is not designed or intended for use in on-line control
 * of aircraft, air traffic, aircraft navigation or aircraft
 * communications; or in the design, construction, operation or
 * maintenance of any nuclear facility. Licensee represents and
 * warrants that it will not use or redistribute the Software for such
 * purposes.
 */
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

/**
 * Class declaration
 * 
 * 
 * @author
 * @version 1.10, 08/04/00
 */
public class Writer {

	@SuppressWarnings("rawtypes")
	static Enumeration portList;
	static CommPortIdentifier portId;
	static SerialPort serialPort;
	static OutputStream outputStream;
	static boolean outputBufferEmptyFlag = false;

	/**
	 * Method declaration
	 * @param args
	 * @see
	 */
	public static void main(String[] args) {
		Writer.wirte("我是测试的文字内容", args.length>0 ? args[0] : "");
	}
	
	/**
	 * 要打印的信息与端口名字，例如：COM3
	 * @param message
	 * @param port
	 */
	public static void wirte(String message,String port){
		boolean portFound = false;
		String defaultPort = "COM3";
		if (port != null && !"".endsWith(port) && port.length() > 0) {
			defaultPort = port;
		}
		portList = CommPortIdentifier.getPortIdentifiers();
		while (portList.hasMoreElements()) {
			portId = (CommPortIdentifier) portList.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				if (portId.getName().equals(defaultPort)) {
					System.out.println("Found port " + defaultPort);
					portFound = true;
					try {
						serialPort = (SerialPort) portId.open("Writer",2000);
					} catch (PortInUseException e) {
						System.out.println("Port in use:使用中！");
						continue;
					}
					try {
						outputStream = serialPort.getOutputStream();
					} catch (IOException e) {
					}
					try {
						serialPort.setSerialPortParams( 115200,
														SerialPort.DATABITS_8, 
														SerialPort.STOPBITS_1,
														SerialPort.PARITY_NONE);
					} catch (UnsupportedCommOperationException e) {}
					try {
						serialPort.notifyOnOutputEmpty(true);
					} catch (Exception e) {
						System.out.println("Error setting event notification");
//						System.exit(-1);
					}
					System.out.println("\"串口名字： \"" + serialPort.getName());
					try {
						outputStream.write(Command.INIT);
						outputStream.write(Command.LEFTMARGIN);
						outputStream.write("(客户存根)".getBytes("gbk"));
						outputStream.write(Command.LF);
						
						outputStream.write(Command.ALIGN_CENTER);
						outputStream.write(Command.BOLD);
						outputStream.write(Command.DOUBLE_WIDTH);
						outputStream.write("欢迎光临".getBytes("gbk"));
						outputStream.write(Command.LF);
						outputStream.flush();
						
						outputStream.write(Command.CANCEL_BOLD);
						outputStream.write("------2015-01-01 12:00:00----".getBytes());
						outputStream.write(Command.LF);
						
						outputStream.write(Command.ALIGN_LEFT);
						outputStream.write("当前排号为：5555555555555".getBytes("gbk"));
						outputStream.write(Command.LF);
						outputStream.write("在您前面还有：55555".getBytes("gbk"));
						outputStream.write(Command.LF);
						
						outputStream.write("我的员工编号是：0000".getBytes("gbk"));
						outputStream.write(Command.LF);
						
						outputStream.write("今天天气非常好：晴".getBytes("gbk"));
						outputStream.write(Command.LF);
						
						outputStream.write(message.getBytes("gbk"));
						outputStream.write(Command.LF);
						
						outputStream.write("-----------------------------".getBytes());
						outputStream.write(Command.LF);
						outputStream.flush();
						
						outputStream.write(Command.BOLD);
						outputStream.write("谢谢惠顾,欢迎再次光临！".getBytes("gbk"));
						outputStream.write(Command.MOVE_LINE);
						outputStream.write(Command.CLEAN);
						outputStream.flush();
						outputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					try {
						Thread.sleep(2000); // Be sure data is xferred before
											// closing
					} catch (Exception e) {
						e.printStackTrace();
					}
					serialPort.close();
//					System.exit(1);
				}
			}
		}
		
		if (!portFound) {
			System.out.println("port " + defaultPort + " not found.");
		}
	}
	

}
