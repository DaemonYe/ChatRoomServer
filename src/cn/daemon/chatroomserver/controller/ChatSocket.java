package cn.daemon.chatroomserver.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import cn.daemon.chatroomserver.view.ServerWindow;

public class ChatSocket extends Thread {

	Socket socket;
	int chatSocketId;
	String username;

	BufferedReader reader;
	PrintWriter writer;

	ServerManager serverManager = ServerManager.getServerManager();
	ServerWindow serverWindow = serverManager.getServerWindow();

	public ChatSocket(Socket s) {
		this.socket = s;
	}

	public void setChatSocketId(int chatSocketId) {
		this.chatSocketId = chatSocketId;
	}

	public int getChatSocketId() {
		return chatSocketId;
	}

	public void firstCome() {
		String helloMsg = "[系统通知]：欢迎**" + username + "**登陆聊天室!";
		serverManager.sendMsgToAll("", helloMsg);
		InetAddress address = socket.getInetAddress();
		String[] rowValues = { address.getHostAddress() + "", username };
		serverWindow.appendTableRow(rowValues);
		serverWindow.appendText(helloMsg);
		String userList = serverManager.getUserList(username);
		sendMsg(userList);
		serverManager.sendMsgToAll(username, "#AddUser####" + username);
	}

	// 线程执行的操作，响应客户端的请求
	public void run() {

		try {
			writer = new PrintWriter(new OutputStreamWriter(
					socket.getOutputStream()));

			reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			firstCome();

			String info = null;
			while ((info = reader.readLine()) != null) {// 循环读取客户端的信息
				if (info.startsWith("#PrivateChat#")) {// 私聊
					String[] userList = info.split(":");
					System.out.println(userList.length);
					String[] usernames = userList[0].split("@");
					serverManager.sendMsgToPrivate(usernames, "[" + username
							+ "]:" + userList[1]);
					continue;
				}
				String message = "[" + username + "]:" + info;
				serverWindow.appendText(message);
				serverManager.sendMsgToAll(username, message);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			disConnect();
		}
	}

	/**
	 * 断开连接
	 */
	public void disConnect() {
		// 关闭资源
		try {
			System.out.println("断开了一个客户端链接xxx");
			serverWindow.removeTableRow(getChatSocketId() - 1);
			serverWindow.appendText("[系统通知]：" + username + "已经下线了!");
			serverManager.sendMsgToAll(username, "[系统通知]：" + username
					+ "已经下线了!");
			serverManager.sendMsgToAll(username, "#DeleteUser####" + username);
			serverManager.remove(username);
			if (reader != null) {
				reader.close();
			}
			if (writer != null) {
				writer.close();
			}
			if (socket != null)
				socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 验证用户名是否存在
	 * 
	 * @return
	 */
	public String checkUsername() {
		try {
			// 输出流
			writer = new PrintWriter(new OutputStreamWriter(
					socket.getOutputStream()));

			// 输入流
			reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));

			// 读取客户端发来的昵称
			username = reader.readLine();
			if (serverManager.hasUsername(username)) {
				writer.println("FAIL");
				writer.flush();
				// writer.close();
				// reader.close();
				// writer = null;
				// reader = null;
				if (socket != null)
					socket.close();
			} else {
				writer.println("OK");
				writer.flush();
				return username;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 服务器端发送消息
	 * 
	 * @param message
	 */
	public void sendMsg(String message) {
		writer.println(message);
		writer.flush();
	}
}
