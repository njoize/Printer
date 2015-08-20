package com.sanploy.Printer.net;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.sf.json.JSONObject;

public class PrinterHttpServer {
	ServerSocket serverSocket;// 服务器Socket

	/**
	 * 服务器监听端口, 默认为 8888.
	 */
	public static int PORT = 8888;// HTTP端口

	/**
	 * 开始服务器 Socket 线程.
	 */
	public PrinterHttpServer() {
		try {
			serverSocket = new ServerSocket(PORT);
		} catch (Exception e) {
			System.out.println("无法启动HTTP服务器:" + e.getLocalizedMessage());
		}
		if (serverSocket == null)
			System.exit(1);// 无法开始服务器

		ExecutorService executorService = Executors.newCachedThreadPool();
		try {
			while (true) {
				Socket socket = serverSocket.accept();
				executorService.execute(new requestHandler(socket));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("HTTP服务器正在运行,端口:" + PORT);
	}

	private class requestHandler implements Runnable {

		private Socket client;

		public requestHandler(Socket client) {
			super();
			this.client = client;
		}

		public void run() {
			if (client == null)
				return;
			System.out.println("连接到服务器的用户:" + client);
			// System.out.println("threadname:" +
			// Thread.currentThread().getName());

			try {
				int contentLength = 0;// 客户端发送的 HTTP 请求的主体的长度
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
				//打印处理内容调用，端口传入
				InputStream propInput = new BufferedInputStream(new FileInputStream("config.properties"));
				Properties props = new Properties();
				props.load(propInput);
				String port = "";
				try{
					if(null != props.getProperty("comPort")){
						Integer.parseInt(props.getProperty("comPort"));
						port = "COM" + props.getProperty("comPort");
					}else{
						System.err.println("你所配置的端口号有误!采用默认端口COM3");
					}
				}catch (Exception e){
					System.err.println("你所配置的端口号有误!采用默认端口COM3" + e.getLocalizedMessage());
				}
				com.sanploy.Printer.print.Writer.wirte("我是需要被打印的内容.....需要定义清楚！", port);
				System.out.println("用户请求的资源是:" + resource);
				System.out.println("请求的类型是: " + method);

				System.out.println("准备返回。。。。 " + method);
				// 用 writer 对客户端 socket 输出json代码
				PrintWriter out = new PrintWriter(client.getOutputStream(), true);
				// out.println("HTTP/1.0 200 OK");// 返回应答消息,并结束应答
				// out.println("Content-Type:text/html;charset=UTF-8");
				// out.println();// 根据 HTTP 协议, 空行将结束头信息

				// DataOutputStream out = new
				// DataOutputStream(client.getOutputStream());
				JSONObject jsonObject = new JSONObject();
				jsonObject.accumulate("ACC", "success");
				System.out.println("返回客户端的内容：" + jsonObject);
				out.println("HTTP/1.1 200 OK");// 返回应答消息,并结束应答
				out.println("Content-Type:application/json");
				out.println("contentType:utf8");
				out.println("Content-Length:" + jsonObject.toString().length());
				out.println();// 根据 HTTP 协议, 空行将结束头信息
				out.write(jsonObject.toString());
				out.close();

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("HTTP服务器错误:" + e.getLocalizedMessage());
			} finally {
				closeSocket(client);
			}
		}
	}

	/**
	 * 关闭客户端 socket 并打印一条调试信息.
	 * 
	 * @param socket
	 *            客户端 socket.
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
		new PrinterHttpServer();
	}

}