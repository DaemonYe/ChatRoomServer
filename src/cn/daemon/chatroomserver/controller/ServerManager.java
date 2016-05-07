package cn.daemon.chatroomserver.controller;

import java.util.HashMap;
import java.util.Map;

import cn.daemon.chatroomserver.view.ServerWindow;

/**
 * ����ģʽ
 * 
 * @author Demon
 * 
 */
public class ServerManager {
	// ����ģʽ
	private static final ServerManager serverManager = new ServerManager();

	// �������˴���
	ServerWindow serverWindow;

	// �洢�����ϵ�socket
	Map<String, ChatSocket> chatSockets = new HashMap<String, ChatSocket>();

	private ServerManager() {
	}

	public static ServerManager getServerManager() {
		return serverManager;
	}

	public void setServerWindow(ServerWindow serverWindow) {
		this.serverWindow = serverWindow;
	}

	public ServerWindow getServerWindow() {
		return serverWindow;
	}

	/**
	 * ����û��б�
	 * 
	 * @return
	 */
	public String getUserList(String username) {
		String userList = "#UserList####";
		for (String key : chatSockets.keySet()) {
			if (!username.equals(key)) {
				userList = userList + key + "####";
			}
		}
		return userList;
	}

	/**
	 * ����������ϵ�socket
	 * 
	 * @param username
	 * @param chatSocket
	 */
	public void add(String username, ChatSocket chatSocket) {
		chatSocket.setChatSocketId(chatSockets.size() + 1);
		chatSockets.put(username, chatSocket);
	}

	/**
	 * �û����Ƿ���
	 * 
	 * @param username
	 * @return
	 */
	public boolean hasUsername(String username) {
		return chatSockets.containsKey(username);
	}

	/**
	 * �Ƴ�socket
	 * 
	 * @param username
	 */
	public void remove(String username) {
		chatSockets.remove(username);
	}

	/**
	 * ���ģ�Ⱥ����Ϣ��
	 * 
	 * @param username
	 *            (˭����)
	 * @param message
	 */
	public void sendMsgToAll(String username, String message) {
		for (String key : chatSockets.keySet()) {
			ChatSocket othercs = chatSockets.get(key);
			if (!username.equals(key)) {
				othercs.sendMsg(message);
			}
		}
	}

	/**
	 * ˽��
	 * 
	 * @param username
	 * @param message
	 */
	public void sendMsgToPrivate(String[] usernames, String message) {
		for (int i = 1; i < usernames.length; i++) {
			chatSockets.get(usernames[i]).sendMsg(message);
		}
	}

	/**
	 * ����������
	 */
	public void connect() {
		new ServerSocketListener().start();
	}

}
