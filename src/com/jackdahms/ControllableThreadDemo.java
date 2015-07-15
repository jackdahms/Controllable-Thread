package com.jackdahms;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ControllableThreadDemo extends JPanel implements Controllable {
	
	ControllableThread ct;
	
	Ball ball;
	float delta;

	public static void main(String[] args) {
		JFrame frame = new JFrame("Controllable Thread Ball Demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new ControllableThreadDemo());
		frame.setSize(600, 600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public ControllableThreadDemo() {
		setFocusable(true);
		
		ball = new Ball();
		
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();
				if (keyCode == KeyEvent.VK_W) {
					ball.up = true;
				}
				if (keyCode == KeyEvent.VK_A) {
					ball.left = true;
				}
				if (keyCode == KeyEvent.VK_S){
					ball.down = true;
				}
				if (keyCode == KeyEvent.VK_D) {
					ball.right = true;
				}
			}
			
			public void keyReleased(KeyEvent e) {
				int keyCode = e.getKeyCode();
				if (keyCode == KeyEvent.VK_W) {
					ball.up = false;
				}
				if (keyCode == KeyEvent.VK_A) {
					ball.left = false;
				}
				if (keyCode == KeyEvent.VK_S){
					ball.down = false;
				}
				if (keyCode == KeyEvent.VK_D) {
					ball.right = false;
				}
			}
		});
		
		ct = new ControllableThread(this);
		ct.start();
	}
	
	public void render(float delta) {
		this.delta = delta;
		repaint();
	}
	
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		ball.render(g, delta);
		
		g.setColor(Color.black);
		g.drawString("FPS: " + ct.getFps(), 5, 10);
		
		g.drawString("vel: " + (int) ball.vel, 5, 20);
		
	}
	
	public void update() {
		ball.update();
	}
	
	class Ball {
		
		double x, y;
		double lastx, lasty;
		double lastdrawx, lastdrawy;
		
		double velx, vely;
		double vel;
		
		double accx, accy;
		double acc;		
		
		int width, height;
		
		Color color;
		
		boolean up, left, down, right;
		
		public Ball() {
			x = 300.0;
			y = 300.0;
			
			lastx = x;
			lasty = y;
			
			velx = 0.0;
			vely = 0.0;
			
			vel = Math.sqrt(velx * velx + vely * vely);
			
			accx = 0.5;
			accy = 0.5;
			
			width = 20;
			height = 20;
			
			color = Color.blue;
		}
		
		public void update() {
			//simple controls
			if (up) {
				vely -= accy;
			}
			if (left) {
				velx -= accx;
			}
			if (down) {
				vely += accy;
			}
			if (right) {
				velx += accx;
			}
			
			//update
			lastx = x;
			lasty = y;
			
			vel = Math.sqrt(velx * velx + vely * vely);
			
			x += velx;
			y += vely;
			
			if (x < -width) {
				lastx = 600.0;
				x = 600.0;
			} else if (x > 600.0) {
				lastx = -width;
				x = -width;
			}
			
			if (y < -height) {
				lasty = 600.0;
				y = 600.0;
			} else if (y > 600.0) {
				lasty = -height;
				y = -height;
			}
			
			int red = 10 * (int) vel;
			color = new Color(red > 255 ? 255 : red, 0, 255 - red < 0 ? 0 : 255 - red);
		}
		
		public void render(Graphics2D g, float delta) {
			g.setColor(color);
			int drawx = (int) ((x - lastx) * delta + lastx - width / 2);
			int drawy = (int) ((y - lasty) * delta + lasty - height / 2);
			g.fillOval(drawx, drawy, width, height);
		}
		
	}
	
}
