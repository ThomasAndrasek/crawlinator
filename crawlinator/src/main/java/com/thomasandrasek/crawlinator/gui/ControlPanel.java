package com.thomasandrasek.crawlinator.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.thomasandrasek.crawlinator.App;
import com.thomasandrasek.crawlinator.crawler.LinkChecker;
import com.thomasandrasek.crawlinator.crawler.OutputChecker;

import org.jsoup.Jsoup;

public class ControlPanel extends JPanel implements ActionListener, ChangeListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean isCrawling = false;
	
	private JTextField seedURLEnterField;
	private JButton setSeedButton;
	private JLabel seedLabel;

	private JSpinner threadCountSpinner;

	private JCheckBox matchDomainCheckBox;
	private JCheckBox matchSubdomainCheckBox;
	private JCheckBox visitSamePageCheckBox;

	private JCheckBox enableExtensionMatching;
	private JTextField extensionMatchingField;

	private JCheckBox enableRegexCheckBox;
	private JTextField regexField;

	private JCheckBox enableOutputRegexCheckBox;
	private JTextField outputRegexField;

	private JCheckBox autoSaveCheckBox;
	private JCheckBox condenseOutputCheckBox;
	private JCheckBox loadDataCheckBox;
	private JCheckBox displayOutputCheckBox;

	private JLabel visitedLabel;
	private JLabel visitedCount;
	private JLabel toVisitCount;
	private JLabel totalLabel;
	private JTextArea visitedTextArea;

	private JButton startButton;
	private JButton stopButton;

	private JButton saveButton;
	
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

		this.add(Box.createRigidArea(new Dimension(100000, 5)));

		this.add(new JLabel("Thread Count:"));

		this.threadCountSpinner = new JSpinner();
		this.threadCountSpinner.setModel(new javax.swing.SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
		this.threadCountSpinner.setValue(1);
		this.threadCountSpinner.setPreferredSize(new Dimension(50, 20));
		this.threadCountSpinner.addChangeListener(this);
		this.add(this.threadCountSpinner);

		this.add(Box.createRigidArea(new Dimension(100000, 5)));

		this.matchDomainCheckBox = new JCheckBox("Match Full Domain");
		this.matchDomainCheckBox.setSelected(false);
		this.matchDomainCheckBox.addActionListener(this);
		this.add(this.matchDomainCheckBox);

		this.matchSubdomainCheckBox = new JCheckBox("Match Domain");
		this.matchSubdomainCheckBox.setSelected(false);
		this.matchSubdomainCheckBox.addActionListener(this);
		this.add(this.matchSubdomainCheckBox);

		this.visitSamePageCheckBox = new JCheckBox("Visit Same Page");
		this.visitSamePageCheckBox.setSelected(false);
		this.visitSamePageCheckBox.addActionListener(this);
		this.add(this.visitSamePageCheckBox);

		this.add(Box.createRigidArea(new Dimension(100000, 5)));

		this.enableExtensionMatching = new JCheckBox("Enable Extension Matching");
		this.enableExtensionMatching.setSelected(false);
		this.enableExtensionMatching.addActionListener(this);
		this.add(this.enableExtensionMatching);

		this.extensionMatchingField = new JTextField("");
		this.extensionMatchingField.setPreferredSize(new Dimension(300, 20));
		this.extensionMatchingField.setEnabled(false);
		this.add(this.extensionMatchingField);

		this.add(Box.createRigidArea(new Dimension(100000, 5)));

		this.enableRegexCheckBox = new JCheckBox("Enable Regex");
		this.enableRegexCheckBox.setSelected(false);
		this.enableRegexCheckBox.addActionListener(this);
		this.add(this.enableRegexCheckBox);

		this.regexField = new JTextField("");
		this.regexField.setPreferredSize(new Dimension(300, 20));
		this.regexField.setEnabled(false);
		this.add(this.regexField);

		this.add(Box.createRigidArea(new Dimension(100000, 5)));

		this.enableOutputRegexCheckBox = new JCheckBox("Enable Output Regex");
		this.enableOutputRegexCheckBox.setSelected(false);
		this.enableOutputRegexCheckBox.addActionListener(this);
		this.add(this.enableOutputRegexCheckBox);

		this.outputRegexField = new JTextField("");
		this.outputRegexField.setPreferredSize(new Dimension(300, 20));
		this.outputRegexField.setEnabled(false);
		this.add(this.outputRegexField);

		this.add(Box.createRigidArea(new Dimension(100000, 5)));

		this.autoSaveCheckBox = new JCheckBox("Auto Save");
		this.autoSaveCheckBox.setSelected(false);
		this.autoSaveCheckBox.addActionListener(this);
		this.add(this.autoSaveCheckBox);

		this.condenseOutputCheckBox = new JCheckBox("Condense Save");
		this.condenseOutputCheckBox.setSelected(false);
		this.condenseOutputCheckBox.addActionListener(this);
		this.add(this.condenseOutputCheckBox);

		this.loadDataCheckBox = new JCheckBox("Load Data");
		this.loadDataCheckBox.setSelected(false);
		this.loadDataCheckBox.addActionListener(this);
		this.add(this.loadDataCheckBox);

		this.displayOutputCheckBox = new JCheckBox("Display Output");
		this.displayOutputCheckBox.setSelected(true);
		this.displayOutputCheckBox.addActionListener(this);
		this.add(this.displayOutputCheckBox);

		this.add(Box.createRigidArea(new Dimension(100000, 5)));

		this.visitedLabel = new JLabel("Visited URLs:");
		this.add(this.visitedLabel);

		this.add(Box.createRigidArea(new Dimension(100000, 3)));

		this.visitedTextArea = new JTextArea();
		this.visitedTextArea.setEditable(false);
		this.visitedTextArea.setLineWrap(true);
		this.visitedTextArea.setWrapStyleWord(true);
		JScrollPane sp = new JScrollPane(this.visitedTextArea);
		sp.setPreferredSize(new Dimension(600, 325));
		sp.setMaximumSize(new Dimension(300, 325));
		this.add(sp);

		redirectSystemStreams();

		this.add(Box.createRigidArea(new Dimension(100000, 3)));

		this.visitedCount = new JLabel("Visited: 0");
		this.add(this.visitedCount);

		this.toVisitCount = new JLabel("To Visit: 0");
		this.add(this.toVisitCount);

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
		
		this.add(Box.createRigidArea(new Dimension(100000, 3)));

		this.saveButton = new JButton("Save");
		this.saveButton.addActionListener(this);
		this.saveButton.setEnabled(false);
		this.add(this.saveButton);

		this.setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Set URL Seed")) {
			try {
				Jsoup.connect(this.seedURLEnterField.getText()).get();
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
			LinkChecker.baseLink = this.seedURLEnterField.getText();
		} 
		else if (e.getActionCommand().equals("Match Full Domain")) {
			LinkChecker.matchDomain = this.matchDomainCheckBox.isSelected();
		}
		else if (e.getActionCommand().equals("Match Domain")) {
			LinkChecker.matchSubdomain = this.matchSubdomainCheckBox.isSelected();
		}
		else if (e.getActionCommand().equals("Visit Same Page")) {
			LinkChecker.linkToCurrentPage = this.visitSamePageCheckBox.isSelected();
		}
		else if (e.getActionCommand().equals("Enable Extension Matching")) {
			LinkChecker.matchExtension = this.enableExtensionMatching.isSelected();
			this.extensionMatchingField.setEnabled(LinkChecker.matchExtension);
		}
		else if (e.getActionCommand().equals("Enable Regex")) {
			this.regexField.setEnabled(this.enableRegexCheckBox.isSelected());
			LinkChecker.useRegex = this.enableRegexCheckBox.isSelected();
		}
		else if (e.getActionCommand().equals("Enable Output Regex")) {
			this.outputRegexField.setEnabled(this.enableOutputRegexCheckBox.isSelected());
			OutputChecker.useRegex = this.enableOutputRegexCheckBox.isSelected();
		}
		else if (e.getActionCommand().equals("Auto Save")) {
			if (this.autoSaveCheckBox.isSelected()) {
				JFileChooser fileChooser = new JFileChooser("./");
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileChooser.setDialogTitle("Save crawled data");
				fileChooser.setApproveButtonText("Save");
				fileChooser.setFileFilter(new FileNameExtensionFilter("Text File", "csv"));
				int returnValue = fileChooser.showOpenDialog(this);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					App.crawler.setAutoSaveFile(file);
				}
			} else {
				App.crawler.setAutoSaveFile(null);
			}
		}
		else if (e.getActionCommand().equals("Condense Save")) {
			App.condenseOutput = this.condenseOutputCheckBox.isSelected();
		}
		else if (e.getActionCommand().equals("Load Data")) {
			if (this.loadDataCheckBox.isSelected()) {
				JFileChooser fileChooser = new JFileChooser("./");
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileChooser.setDialogTitle("Save crawled data");
				fileChooser.setApproveButtonText("Save");
				fileChooser.setFileFilter(new FileNameExtensionFilter("Text File", "csv"));
				int returnValue = fileChooser.showOpenDialog(this);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					if (file.exists()) {
						App.dataFile = file;
					}
				}
			} else {
				App.dataFile = null;
			}
		}
		else if (e.getActionCommand().equals("Display Output")) {
			App.displayOutput = this.displayOutputCheckBox.isSelected();
		}
		else if (e.getActionCommand().equals("Start")) {
			if (this.isCrawling != true) {
				this.isCrawling = true;

				if (LinkChecker.matchExtension) {
					String userExtensions = this.extensionMatchingField.getText();
					LinkChecker.extensions = new ArrayList<>();
					for (String extension : userExtensions.split(",")) {
						if (LinkChecker.isValidExtension(extension.trim().toUpperCase())) {
							LinkChecker.extensions.add(extension.trim().toUpperCase());
						}
					}
				}

				if (LinkChecker.useRegex) {
					LinkChecker.regex = this.regexField.getText();
				}

				if (OutputChecker.useRegex) {
					OutputChecker.regex = this.outputRegexField.getText();
				}

				this.setSeedButton.setEnabled(false);

				App.runCrawler = true;
				App.crawler.setSeedURL(this.seedLabel.getText());
				App.crawler.crawl();
			} else {
				App.crawler.startCrawler();
			}

			this.startButton.setEnabled(false);
			this.stopButton.setEnabled(true);
			this.saveButton.setEnabled(false);
		}
		else if (e.getActionCommand().equals("Stop")) {
			App.crawler.stopCrawler();
			this.stopButton.setEnabled(false);
			this.startButton.setEnabled(true);
			this.saveButton.setEnabled(true);
		}
		else if (e.getActionCommand().equals("Save")) {
			JFileChooser fileChooser = new JFileChooser("./");
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooser.setDialogTitle("Save crawled data");
			fileChooser.setApproveButtonText("Save");
			fileChooser.setFileFilter(new FileNameExtensionFilter("Text File", "csv"));
			int returnValue = fileChooser.showOpenDialog(this);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				if (file.exists()) {
					int response = JOptionPane.showConfirmDialog(this, "The file already exists. Do you want to overwrite it?", "Overwrite file?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (response == JOptionPane.NO_OPTION) {
						return;
					}
				}
				App.crawler.saveCrawledData(file);
				JOptionPane.showMessageDialog(this, "Crawled data saved to " + file.getAbsolutePath());
			}
		}
	}

	// public void addVisitedURL(String url) {
	// 	this.visitedTextPane.setText(this.visitedTextPane.getText() + url + "\n");
	// }

	// public void scrollToBottom() {
	// 	this.visitedTextPane.setCaretPosition(this.visitedTextPane.getDocument().getLength());
	// }

	public void updateVisitedCount(long count) {
		this.visitedCount.setText("Visited: " + count);
	}

	public void updateTotalCount(long count) {
		this.totalLabel.setText("Total: " + count);
	}

	public void updateToVisitCount(long size) {
		this.toVisitCount.setText("To Visit: " + size);
	}

	// public void updateBackUpCount(int size) {
	// 	this.backupLabel.setText("Back Up: " + size);
	// }

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() instanceof JSpinner) {
			JSpinner source = (JSpinner) e.getSource();
			App.crawler.setThreadCount((int) source.getValue());
		}
		
	}

	/**
	 * Code adopted from:
	 * 	http://unserializableone.blogspot.com/2009/01/redirecting-systemout-and-systemerr-to.html
	 */
	private void updateTextArea(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
		  public void run() {
			visitedTextArea.append(text);
		  }
		});
	  }
	

	  /**
	 * Code adopted from:
	 * 	http://unserializableone.blogspot.com/2009/01/redirecting-systemout-and-systemerr-to.html
	 */
	  private void redirectSystemStreams() {
		OutputStream out = new OutputStream() {
		  @Override
		  public void write(int b) throws IOException {
			updateTextArea(String.valueOf((char) b));
		  }
	
		  @Override
		  public void write(byte[] b, int off, int len) throws IOException {
			updateTextArea(new String(b, off, len));
		  }
	
		  @Override
		  public void write(byte[] b) throws IOException {
			write(b, 0, b.length);
		  }
		};
	
		System.setOut(new PrintStream(out, true));
		System.setErr(new PrintStream(out, true));
	  }
}
