package com.sanploy.Printer.net;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class SendJson {
	public void send() {
		String url_toAdd = "http://localhost:8888";
		JSONObject jo1 = new JSONObject().accumulate("memberPartnerId", "4");
		JSONObject jo2 = new JSONObject().accumulate("jsontest", "test");
		// String query =
		// " {\"mainUser\":{\"name\":\""+name+"\",\"gender\":\""+gender+"\",\"birthDate\":\""+birthDate+"\",\"birthDateAccurate\":\""+birthDateAccurate+"\",\"addrId\":\""+addrId+"\"},\"productId\":\""+productId+"\"}";
		HttpURLConnection connection = null;
		DataOutputStream out = null;
		try {
			URL url = new URL(url_toAdd);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			// connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("contentType", "utf8");
			connection.connect();
			out = new DataOutputStream(connection.getOutputStream());
			JSONArray jArray = JSONArray.fromObject(jo1);
			jArray.add(jo2);
			System.out.println(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS") + " 发送内容：" + jArray.toString());
			// out.writeBytes(jArray.toString());
			out.write(jArray.toString().getBytes("UTF-8"));
			out.flush();

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String lines;
			StringBuffer sbf = new StringBuffer();
			// while(reader.ready()){
			// try {
			// Thread.sleep(1000);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
			// }
			while ((lines = reader.readLine()) != null) {
				lines = new String(lines.getBytes(), "utf-8");
				sbf.append(lines);
			}
			System.out.println(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS") + " 接收内容：" + sbf);
			reader.close();
			// 断开连接
			connection.disconnect();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		// for (int i = 0; i < 100; i++)
		new SendJson().send();
	}
}

/*
 * 有些人在读取服务器端返回的数据的时候，使用了BufferedReader类的ready: while(reader.ready()) {
 * //执行读取操作，即readLine } 这个方法很经常被用到， 但是返回的却都是空，这是什么原因呢，查看了一下帮助文档，以及上网查了查，总结如下:1.
 * ready是查看流是否已经准备好被读，是一个非阻塞的方法，所以会立刻返回，由于服务器没有准备好被读，所以会立刻返回，所以读取到的都是null。2.
 * readLine是一个阻塞的方法，只要没有断开连接，就会一直等待，直到有东西返回，那么什么时候返回空呢，只有读到数据流最末尾，才返回null。
 * 实际上，在读网络数据的时候经常会遇到数据延迟等问题。这时候直接运行读网络流中的数据时很容易遇到 java.io.IOException:
 * Premature EOF的异常。 这时就可以使用 ready函数查看BufferedReader是否已经准备好。
 * while(!reader.ready){ //阻塞，等待一段时间 } while(reader.readLine()!==null){ //执行操作 }
 * 特别要注意的是当reader.readLine已经读完之后，如果继续执行ready操作，会返回false; 因此，下面两段代码可能造成死循环：
 * while(reader.readLine()!==null){ //执行操作 while(!reader.ready){ //阻塞，等待一段时间 } }
 * while(reader.readLine()!==null){ //执行操作 } while(!reader.ready){ //阻塞，等待一段时间 }
 */

