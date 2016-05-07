package cn.daemon.chatroomserver.controller;

import java.util.HashMap;
import java.util.Map;

import cn.daemon.chatroomserver.view.ServerWindow;

/**
 * 单例模式
 * 
 * @author Demon
 * 
 */
public class ServerManager {
	// 单例模式
	private static final ServerManager serverManager = new ServerManager();

	// 服务器端窗口
	ServerWindow serverWindow;

	// 存储连接上的socket
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
	 * 获得用户列表
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
	 * 添加新连接上的socket
	 * 
	 * @param username
	 * @param chatSocket
	 */
	public void add(String username, ChatSocket chatSocket) {
		chatSocket.setChatSocketId(chatSockets.size() + 1);
		chatSockets.put(username, chatSocket);
	}

	/**
	 * 用户名是否被用
	 * 
	 * @param username
	 * @return
	 */
	public boolean hasUsername(String username) {
		return chatSockets.containsKey(username);
	}

	/**
	 * 移除socket
	 * 
	 * @param username
	 */
	public void remove(String username) {
		chatSockets.remove(username);
	}

	/**
	 * 公聊（群发信息）
	 * 
	 * @param username
	 *            (谁发的)
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
	 * 私聊
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
	 * 开启服务器
	 */
	public void connect() {
		new ServerSocketListener().start();
	}

}
