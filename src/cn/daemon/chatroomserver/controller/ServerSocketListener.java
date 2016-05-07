package cn.daemon.chatroomserver.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

public class ServerSocketListener extends Thread {

	@Override
	public void run() {
		try {
			// 1.����һ����������Socket����ServerSocket��ָ���󶨵Ķ˿ڣ��������˶˿�
			ServerSocket serverSocket = new ServerSocket(8888);

			JOptionPane.showMessageDialog(null, "***�����������������ȴ��ͻ��˵�����***");

			Socket socket = null;

			System.out.println("***�����������������ȴ��ͻ��˵�����***");

			ServerManager serverManager = ServerManager.getServerManager();

			// ѭ�������ȴ��ͻ��˵�����
			while (true) {
				// ����accept()������ʼ�������ȴ��ͻ��˵�����
				socket = serverSocket.accept();

				// ����һ���µ��߳�
				ChatSocket chatSocket = new ChatSocket(socket);

				// ��֤�û����Ƿ����
				String username = chatSocket.checkUsername();
				if (username == null)
					continue;

				// ����
				serverManager.add(username, chatSocket);

				// �����߳�
				chatSocket.start();
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "������������������");
			// e.printStackTrace();
		}
	}
}
