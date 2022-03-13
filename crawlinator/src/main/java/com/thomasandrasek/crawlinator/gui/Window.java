package com.thomasandrasek.crawlinator.gui;

import java.util.ResourceBundle.Control;

import javax.swing.JFrame;

public class Window extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ControlPanel controlPanel;

	public Window() {
		
		super();
		
		int defaultHeight = 800;
		int defaultWidth = 800;
		super.setSize(defaultWidth, defaultHeight);
		// super.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		super.setTitle("The Crawlinator");
		
		super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		this.controlPanel = new ControlPanel();

		super.add(this.controlPanel);

		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		super.setVisible(true);
		
	}

	public ControlPanel getControlPanel() {
		return this.controlPanel;
	}
	
}
