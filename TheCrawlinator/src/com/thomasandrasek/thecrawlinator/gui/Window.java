package com.thomasandrasek.thecrawlinator.gui;

import javax.swing.JFrame;

public class Window extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Window() {
		
		super();
		
		super.setSize(getMaximumSize());
		super.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		super.setTitle("The Crawlinator");
		
		super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		super.add(new ControlPanel());
		
		super.setVisible(true);
		
	}
	
}
