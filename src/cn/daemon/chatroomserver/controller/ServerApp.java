package cn.daemon.chatroomserver.controller;

import java.awt.EventQueue;

import cn.daemon.chatroomserver.view.ServerWindow;

public class ServerApp {

	/**
	 * Launch the server application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerWindow frame = new ServerWindow();
					frame.setVisible(true);
					ServerManager.getServerManager().setServerWindow(frame);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
