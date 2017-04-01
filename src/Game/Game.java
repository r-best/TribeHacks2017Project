package Game;

import graphics.Assets;
import states.StateManager;
import utils.KeyManager;
import utils.Preferences;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import javax.swing.JFrame;

public class Game extends Canvas{

	private static int FPS = 60;
	private static int Width, Height;
	private static boolean RUNNING = false;

	private static JFrame frame;

	public Game(Dimension dimension){
		this.setPreferredSize(dimension);
		this.setMinimumSize(dimension);
		this.setMaximumSize(dimension);
		this.setFocusable(false);

		Width = dimension.width;
		Height = dimension.height;

		frame = new JFrame("TribeHacks 2017 Project");
		frame.setSize(Width, Height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.add(this);
		frame.pack();
	}

	private void Init() throws FontFormatException, IOException{
		frame.addKeyListener(KeyManager.getInstance());

		Assets.init();

		StateManager.init();

		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		long timer = 0;
		int ticks = 0;

		//GAME LOOP
		RUNNING = true;
		while(RUNNING){
			now = System.nanoTime();
			double timePerTick = 1000000000 / FPS;
			delta += (now - lastTime) / timePerTick;
			timer += now - lastTime;
			lastTime = now;

			if(delta >= 1){
				Update();
				Draw();
				ticks++;
				delta--;
			}
			if(timer >= 1000000000){
				System.out.println("Frames: " +ticks);
				ticks = 0;
				timer = 0;
			}
		}
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}

	private void Update(){
		if(frame.getContentPane().getWidth() != Width || frame.getContentPane().getHeight() != Height){
			this.setPreferredSize(frame.getSize());
			Width = frame.getWidth();
			Height = frame.getHeight();
		}

		StateManager.update();

		if(KeyManager.checkKeyWithoutReset(KeyEvent.VK_ESCAPE))
			Game.stop();
	}

	private void Draw(){
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			this.createBufferStrategy(3);
			return;
		}
		Graphics2D graphics = (Graphics2D)bs.getDrawGraphics();
		//Clear Screen
		graphics.clearRect(0, 0, Width, Height);

		//Color Background
		graphics.setColor(Color.cyan);
		graphics.fillRect(0, 0, Width, Height);

		//Set initial text font & color
		graphics.setFont(Assets.timesNewRoman.deriveFont(30f * (float)Preferences.scale));
		graphics.setColor(new Color(0, 0, 0));

		//Draw here
		StateManager.draw(graphics);

		//Stop draw
		bs.show();
		graphics.dispose();
	}

	/**
	 * Called to start the game
	 */
	public void start(){
		if(!RUNNING){
			RUNNING = true;
			try {
				Init();
			} catch (FontFormatException | IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Called to stop the game
	 */
	public static void stop(){
		RUNNING = false;
	}

	public static void setTargetFPS(int fps){
		FPS = fps;
	}

	public static int getGameWidth(){
		return Width;
	}
	public static int getGameHeight(){
		return Height;
	}

	public static void main(String[] args) {
		Preferences.loadPrefs();
		Game game = new Game(new Dimension(Preferences.gamewidth, Preferences.gameheight));
		game.start();
	}
}