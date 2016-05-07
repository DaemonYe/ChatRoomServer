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
		String helloMsg = "[ϵͳ֪ͨ]����ӭ**" + username + "**��½������!";
		serverManager.sendMsgToAll("", helloMsg);
		InetAddress address = socket.getInetAddress();
		String[] rowValues = { address.getHostAddress() + "", username };
		serverWindow.appendTableRow(rowValues);
		serverWindow.appendText(helloMsg);
		String userList = serverManager.getUserList(username);
		sendMsg(userList);
		serverManager.sendMsgToAll(username, "#AddUser####" + username);
	}

	// �߳�ִ�еĲ�������Ӧ�ͻ��˵�����
	public void run() {

		try {
			writer = new PrintWriter(new OutputStreamWriter(
					socket.getOutputStream()));

			reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			firstCome();

			String info = null;
			while ((info = reader.readLine()) != null) {// ѭ����ȡ�ͻ��˵���Ϣ
				if (info.startsWith("#PrivateChat#")) {// ˽��
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
	 * �Ͽ�����
	 */
	public void disConnect() {
		// �ر���Դ
		try {
			System.out.println("�Ͽ���һ���ͻ�������xxx");
			serverWindow.removeTableRow(getChatSocketId() - 1);
			serverWindow.appendText("[ϵͳ֪ͨ]��" + username + "�Ѿ�������!");
			serverManager.sendMsgToAll(username, "[ϵͳ֪ͨ]��" + username
					+ "�Ѿ�������!");
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
	 * ��֤�û����Ƿ����
	 * 
	 * @return
	 */
	public String checkUsername() {
		try {
			// �����
			writer = new PrintWriter(new OutputStreamWriter(
					socket.getOutputStream()));

			// ������
			reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));

			// ��ȡ�ͻ��˷������ǳ�
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
	 * �������˷�����Ϣ
	 * 
	 * @param message
	 */
	public void sendMsg(String message) {
		writer.println(message);
		writer.flush();
	}
}
