package com.sanploy.Printer.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.StringTokenizer;

/**
 * 
 * <pre>
 *       当用户在Web浏览器地址栏中输入一个带有http://前缀的URL并按下Enter后,或者在Web页面中某个以http://开头的超链接上单击鼠标,HTTP事务处理的第一个阶段--建立连接阶段就开始了.HTTP的默认端口是80.
 *    随着连接的建立,HTTP就进入了客户向服务器发送请求的阶段.客户向服务器发送的请求是一个有特定格式的ASCII消息,其语法规则为:
 * < Method > < URL > < HTTP Version > <\n>
 * { <Header>:<Value> <\n>}*
 * <\n>
 * { Entity Body }
 *    请求消息的顶端是请求行,用于指定方法,URL和HTTP协议的版本,请求行的最后是回车换行.方法有GET,POST,HEAD,PUT,DELETE等.
 * 在请求行之后是若干个报头(Header)行.每个报头行都是由一个报头和一个取值构成的二元对,报头和取值之间以":"分隔;报头行的最后是回车换行.常见的报头有Accept(指定MIME媒体类型),Accept_Charset(响应消息的编码方式),Accept_Encoding(响应消息的字符集),User_Agent(用户的浏览器信息)等.
 *    在请求消息的报头行之后是一个回车换行,表明请求消息的报头部分结束.在这个\n之后是请求消息的消息实体(Entity Body).具体的例子参看httpRequest.txt.
 *     Web服务器在收到客户请求并作出处理之后,要向客户发送应答消息.与请求消息一样,应答消息的语法规则为:
 * < HTTP Version> <Status Code> [<Message>]<\n>
 * { <Header>:<Value> <\n> } *
 * <\n>
 * { Entity Body }
 *    应答消息的第一行为状态行,其中包括了HTTP版本号,状态码和对状态码进行简短解释的消息;状态行的最后是回车换行.状态码由3位数字组成,有5类:
 * 参看:HTTP应答码及其意义
 * 
 * 1XX 保留
 * 2XX 表示成功
 * 3XX 表示URL已经被移走
 * 4XX 表示客户错误
 * 5XX 表示服务器错误
 * 例如:415,表示不支持改媒体类型;503,表示服务器不能访问.最常见的是200,表示成功.常见的报头有:Last_Modified(最后修改时间),Content_Type(消息内容的MIME类型),Content_Length(内容长度)等.
 *    在报头行之后也是一个回车换行,用以表示应答消息的报头部分的结束,以及应答消息实体的开始.
 *    下面是一个应答消息的例子:
 * HTTP/1.0 200 OK
 * Date: Moday,07-Apr-97 21:13:02 GMT
 * Server:NCSA/1.1
 * MIME_Version:1.0
 * Content_Type:text/html
 * Last_Modified:Thu Dec 5 09:28:01 1996
 * Coentent_Length:3107
 * 
 * <HTML><HEAD><TITLE>简单的用 Java Socket 编写的 HTTP 服务器应用,帮助学习HTTP协议（支持POST信息打印 - tonrenyuye - 同人与野的博客</HTML>
 * 
 * 在用Java语言实现HTTP服务器时,首先启动一个java.net.ServerSocket在提?服务的端?上监听连接.向客户返回文本时,可以用PrintWriter,但是如果返回二进制数据,则必须使用OutputStream.write(byte[])方法,返回的应答消息字符串可以使用String.getBytes()方法转换为字节数组返回,或者使用PrintStream的print()方法写入文本,用write(byte[])方法写入二进制数据.
 * 
 * </pre>
 * 
 * @author 刘长炯
 * @version 1.0 2007-07-24 Sunday
 * @version 1.1 2008-05-18 Sunday 支持浏览器发出的POST信息打印，修正POST方式无法完成响应造成阻赛的BUG
 */
public class SimpleHttpServer_BACKUP implements Runnable {
	/**
     *
     */
	ServerSocket serverSocket;// 服务器Socket
	
	/**
	 * 服务器监听端口, 默认为 8888.
	 */
	public static int PORT = 8888;// 标准HTTP端口

	/**
	 * 开始服务器 Socket 线程.
	 */
	public SimpleHttpServer_BACKUP() {
		try {
			serverSocket = new ServerSocket(PORT);
			
		} catch (Exception e) {
			System.out.println("无法启动HTTP服务器:" + e.getLocalizedMessage());
		}
		if (serverSocket == null)
			System.exit(1);// 无法开始服务器
		new Thread(this).start();
		System.out.println("HTTP服务器正在运行,端口:" + PORT);
	}

	/**
	 * 运行服务器主线程, 监听客户端请求并返回响应.
	 */
	public void run() {
		while (true) {
			try {
				Socket client = null;// 客户Socket
				int contentLength = 0;// 客户端发送的 HTTP 请求的主体的长度
				client = serverSocket.accept();// 客户机(这里是 IE 等浏览器)已经连接到当前服务器
				if (client != null) {
					System.out.println("连接到服务器的用户:" + client);
					try {
						// 第一阶段: 打开输入流
						BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
						System.out.println("客户端发送的请求信息:\n===================");
						// 读取第一行, 请求地址
						String line = in.readLine();
						System.out.println(line);
						String resource = line.substring(line.indexOf('/'), line.lastIndexOf('/') - 5);
						// 获得请求的资源的地址
						resource = URLDecoder.decode(resource, "UTF-8");// 反编码
																		// URL
																		// 地址
						String method = new StringTokenizer(line).nextElement().toString();// 获取请求方法,
																							// GET
																							// 或者
																							// POST

						// 读取所有浏览器发送过来的请求参数头部信息
						while ((line = in.readLine()) != null) {
							System.out.println(line);
							// 读取 POST 等数据的内容长度
							if (line.startsWith("Content-Length")) {
								try {
									contentLength = Integer.parseInt(line.substring(line.indexOf(':') + 1).trim());
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							if (line.equals(""))
								break;
						}

						// 显示 POST 表单提交的内容, 这个内容位于请求的主体部分
						if ("POST".equalsIgnoreCase(method) && contentLength > 0) {
							System.out.println("以下内容为 POST 方式提交的表单数据");
							for (int i = 0; i < contentLength; i++) {
								System.out.print((char) in.read());
							}
							System.out.println();
						}
						System.out.println("请求信息结束\n===================");
						com.sanploy.Printer.print.Writer.wirte("我需要被打印", "");
						System.out.println("用户请求的资源是:" + resource);
						System.out.println("请求的类型是: " + method);
						
						// 用 writer 对客户端 socket 输出一段 HTML 代码
						PrintWriter out = new PrintWriter(client.getOutputStream(), true);
						out.println("HTTP/1.0 200 OK");// 返回应答消息,并结束应答
						out.println("Content-Type:text/html;charset=UTF-8");
						out.println();// 根据 HTTP 协议, 空行将结束头信息
						out.close();
						closeSocket(client);
						
					} catch (Exception e) {
						System.out.println("HTTP服务器错误:" + e.getLocalizedMessage());
					}
				}
				// System.out.println(client+"连接到HTTP服务器");//如果加入这一句,服务器响应速度会很慢
			} catch (Exception e) {
				System.out.println("HTTP服务器错误:" + e.getLocalizedMessage());
			}
		}
	}

	/**
	 * 关闭客户端 socket 并打印一条调试信息.
	 * @param socket 	客户端 socket.
	 */
	void closeSocket(Socket socket) {
		try {
			socket.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		System.out.println(socket + "离开了HTTP服务器");
	}

	/**
	 * 打印用途说明.
	 */
	private static void usage() {
		System.out.println("Usage: java SimpleHttpServer <port>\nDefault port is 8888.");
	}

	/**
	 * 启动简易 HTTP 服务器
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			if (args.length != 1) {
				usage();
			} else if (args.length == 1) {
				PORT = Integer.parseInt(args[0]);
			}
		} catch (Exception ex) {
			System.err.println("Invalid port arguments. It must be a integer that greater than 0");
		}

		new SimpleHttpServer_BACKUP();
	}

}