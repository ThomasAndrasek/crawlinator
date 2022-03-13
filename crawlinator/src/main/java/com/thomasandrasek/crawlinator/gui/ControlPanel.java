package com.thomasandrasek.crawlinator.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import com.thomasandrasek.crawlinator.App;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ControlPanel extends JPanel implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean isCrawling = false;
	
	private JTextField seedURLEnterField;
	private JButton setSeedButton;
	private JLabel seedLabel;

	private JLabel visitedLabel;
	private JLabel visitedCount;
	private JLabel totalLabel;
	private JTextPane visitedTextPane;

	private JButton startButton;
	private JButton stopButton;
	
	public ControlPanel() {
		
		this.setSize(getMaximumSize());

		this.seedURLEnterField = new JTextField("https://thomasandrasek.com");
		this.seedURLEnterField.setPreferredSize(new Dimension(300, 20));
		this.add(this.seedURLEnterField);
		
		this.setSeedButton = new JButton("Set URL Seed");
		this.setSeedButton.addActionListener(this);
		this.add(this.setSeedButton);

		this.seedLabel = new JLabel("https://thomasandrasek.com");
		this.add(this.seedLabel);

		this.add(Box.createRigidArea(new Dimension(100000, 27)));

		this.visitedLabel = new JLabel("Visited URLs:");
		this.add(this.visitedLabel);

		this.add(Box.createRigidArea(new Dimension(100000, 3)));

		this.visitedTextPane = new JTextPane();
		this.visitedTextPane.setEditable(false);
		this.visitedTextPane.setPreferredSize(new Dimension(300, 400));
		this.visitedTextPane.setMaximumSize(new Dimension(300, 400));
		JScrollPane sp = new JScrollPane(this.visitedTextPane);
		sp.setPreferredSize(new Dimension(300, 400));
		sp.setMaximumSize(new Dimension(300, 400));
		this.add(sp);

		this.add(Box.createRigidArea(new Dimension(100000, 3)));

		this.visitedCount = new JLabel("Visited: 0");
		this.add(this.visitedCount);

		this.totalLabel = new JLabel("Total: 0");
		this.add(this.totalLabel);

		this.add(Box.createRigidArea(new Dimension(100000, 3)));

		this.startButton = new JButton("Start");
		this.startButton.addActionListener(this);
		this.add(this.startButton);

		this.stopButton = new JButton("Stop");
		this.stopButton.addActionListener(this);
		this.stopButton.setEnabled(false);
		this.add(this.stopButton);
		
		this.setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Set URL Seed")) {
			try {
				Document doc = Jsoup.connect(this.seedURLEnterField.getText()).get();
			} catch (MalformedURLException malformedException) {
				JOptionPane.showMessageDialog(this, "The URL you entered is not valid.");
				return;
			} catch (UnknownHostException unknownHostException) {
				JOptionPane.showMessageDialog(this, "The URL you entered is not valid.");
				return;
			} catch (IOException ioException) {
				JOptionPane.showMessageDialog(this, "The URL you entered is not valid.");
				return;
			} catch (IllegalArgumentException illegalArgumentException) {
				JOptionPane.showMessageDialog(this, "The URL you entered is not valid.");
				return;
			}

			this.seedLabel.setText(this.seedURLEnterField.getText());
		} 
		else if (e.getActionCommand().equals("Start")) {
			if (this.isCrawling != true) {
				this.isCrawling = true;

				this.setSeedButton.setEnabled(false);

				App.crawler.setSeedURL(this.seedLabel.getText());
				App.crawler.crawl();
			} else {
				App.crawler.startCrawler();
			}

			this.startButton.setEnabled(false);
			this.stopButton.setEnabled(true);
		}
		else if (e.getActionCommand().equals("Stop")) {
			App.crawler.stopCrawler();
			this.stopButton.setEnabled(false);
			this.startButton.setEnabled(true);
		}
	}

	public void addVisitedURL(String url) {
		this.visitedTextPane.setText(this.visitedTextPane.getText() + url + "\n");
	}

	public void scrollToBottom() {
		this.visitedTextPane.setCaretPosition(this.visitedTextPane.getDocument().getLength());
	}

	public void updateVisitedCount(long count) {
		this.visitedCount.setText("Visited: " + count);
	}

	public void updateTotalCount(long count) {
		this.totalLabel.setText("Total: " + count);
	}
}
