package com.jackdahms;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ControllableThreadDemo extends JPanel implements Controllable {
	
	double x, y;
	double xInc, yInc;

	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(600, 600);
		f.setLocationRelativeTo(null);
		
		f.add(new ControllableThreadDemo());
		
		f.setVisible(true);
	}
	
	public ControllableThreadDemo() {
		x = 1;
		y = 1;
		xInc = 1;
		yInc = 1;
		
		ControllableThread ct = new ControllableThread(this);
		ct.start();
	}
	
	public void render() {
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.blue);
		g.fillOval((int) x, (int) y, 20, 20);
	}
	
	public void update() {
		x += xInc;
		y += yInc;
		
		if (x > 600)
			xInc = -1;
		if (x < 0)
			xInc = 1;
		if (y > 600)
			yInc = -1;
		if (y < 0)
			yInc = 1;
	}
	
}
