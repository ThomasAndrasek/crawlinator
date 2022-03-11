package com.thomasandrasek.crawlinator.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ControlPanel extends JPanel implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JButton setSeedButton;
	
	public ControlPanel() {
		
		super.setSize(getMaximumSize());
		
		this.setSeedButton = new JButton("Set URL Seed");
		this.setSeedButton.addActionListener(this);
		super.add(this.setSeedButton);
		
		super.setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals("Set URL Seed")) {
			JOptionPane.showConfirmDialog(null, "Pressed button!");
		}
	}

}
