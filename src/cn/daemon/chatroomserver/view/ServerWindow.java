package cn.daemon.chatroomserver.view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import cn.daemon.chatroomserver.controller.ServerManager;

public class ServerWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTable table;
	private DefaultTableModel defaultModel;

	private JTextArea textArea;

	private JButton button;

	/**
	 * Create the frame.
	 */
	public ServerWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 350, 463);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		// label:服务器
		JLabel label = new JLabel("\u670D\u52A1\u5668");
		label.setHorizontalAlignment(SwingConstants.CENTER);

		// table:在线用户
		String[] name = { "ip", "username" };// 列名
		String[][] data = null;// 数据

		defaultModel = new DefaultTableModel(data, name);
		table = new JTable(defaultModel);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setViewportView(table);

		// textArea:公聊消息
		textArea = new JTextArea();

		// button：开启服务器
		button = new JButton("\u5F00\u542F\u670D\u52A1\u5668");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				ServerManager.getServerManager().connect();
				button.setEnabled(false);
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane
				.setHorizontalGroup(gl_contentPane
						.createParallelGroup(Alignment.LEADING)
						.addComponent(label, Alignment.TRAILING,
								GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE,
								424, Short.MAX_VALUE)
						.addComponent(textArea, GroupLayout.DEFAULT_SIZE, 424,
								Short.MAX_VALUE)
						.addGroup(
								gl_contentPane
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(button,
												GroupLayout.DEFAULT_SIZE, 404,
												Short.MAX_VALUE)
										.addContainerGap()));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_contentPane
						.createSequentialGroup()
						.addComponent(label)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 88,
								Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(textArea, GroupLayout.DEFAULT_SIZE, 104,
								Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(button, GroupLayout.PREFERRED_SIZE, 23,
								GroupLayout.PREFERRED_SIZE)));

		contentPane.setLayout(gl_contentPane);
	}

	public void appendText(String in) {
		textArea.append("\n" + in);
	}

	public void appendTableRow(String[] rowValues) {
		defaultModel.addRow(rowValues);
	}

	public void removeTableRow(int id) {
		defaultModel.removeRow(id);
	}
}
