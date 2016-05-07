package cn.daemon.chatroomserver.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

public class ServerSocketListener extends Thread {

	@Override
	public void run() {
		try {
			// 1.创建一个服务器端Socket，即ServerSocket，指定绑定的端口，并监听此端口
			ServerSocket serverSocket = new ServerSocket(8888);

			JOptionPane.showMessageDialog(null, "***服务器即将启动，等待客户端的连接***");

			Socket socket = null;

			System.out.println("***服务器即将启动，等待客户端的连接***");

			ServerManager serverManager = ServerManager.getServerManager();

			// 循环监听等待客户端的连接
			while (true) {
				// 调用accept()方法开始监听，等待客户端的连接
				socket = serverSocket.accept();

				// 创建一个新的线程
				ChatSocket chatSocket = new ChatSocket(socket);

				// 验证用户名是否存在
				String username = chatSocket.checkUsername();
				if (username == null)
					continue;

				// 加入
				serverManager.add(username, chatSocket);

				// 启动线程
				chatSocket.start();
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "出问题啦啦啦！！！");
			// e.printStackTrace();
		}
	}
}
