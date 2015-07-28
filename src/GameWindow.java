
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JPanel;



public class GameWindow extends Canvas {

	private static final long serialVersionUID = 1L;
	
	/** Strategy allowing us to to accelerate the rendering */
	private BufferStrategy strategy;
	/** True if the game is running*/
	private boolean gameRunning = true;
	
	/** The frame displaying the canvas */
	private JFrame frame;
	/** The width of the display */
	private int width;
	/** The height of the display */
	private int height;
	/** The callback which should be notified of events caused by this window */
	private Game game;
	/** The current accelerated graphics context */
	private Graphics2D g;
	
	/**
	 * Create a new window that will be rendered later
	 */
	public GameWindow() {
		frame = new JFrame();
	}
	
	/**
	 * Set the title of the window
	 * 
	 * @param title The title to display on the window 
	 */
	public void setTitle(String title) {
		frame.setTitle(title);
	}

	/**
	 * Set the resolution of the game window
	 * 
	 * @param x The width of the game 
	 * @param y The height of the game 
	 */
	public void setResolution(int x, int y) {
		width = x;
		height = y;
	}


	/**
	 * Starts to render 
	 */
	public void startRendering() {
		// holds the content of the frame and sets the resolution
		JPanel panel = (JPanel) frame.getContentPane();
		panel.setPreferredSize(new Dimension(800,600));
		panel.setLayout(null);
	
		Keyboard.init(this);
		
		// setup our canvas size and put it into the content of the frame
		this.setBounds(0,0,width,height);
		panel.add(this);
		
		// Tell AWT not to bother repainting our canvas since we're
		// going to do that our self in accelerated mode
		this.setIgnoreRepaint(true);
		
		// finally make the window visible 
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		
		// add a listener to respond to the user closing the window. If they
		// do we'd like to exit the game
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (game != null) {
					game.windowClosed();
				} else {
					System.exit(0);
				}
			}
		});

		// request the focus so key events come to us
		requestFocus();
		
		// create the buffering strategy which will allow AWT
		// to manage our accelerated graphics
		createBufferStrategy(3);
		strategy = getBufferStrategy();
		
		// it that initialisation is taking place
		if (game != null) {
			game.initialise();
		}
		
		// start the game loop
		gameLoop();
	}

	/**
	 * Register the callback that should be notified of game
	 * window events.
	 * 
	 * @param callback The callback to be notified of display events 
	 */
	public void setGameWindowCallback(Game game) {
		this.game = game;
	}

	/**
	 * Check if a particular key is pressed
	 * 
	 * @param keyCode The code associated with the key to check
	 * @return True if the specified key is pressed
	 */
	public boolean isKeyPressed(int keyCode) {
		return Keyboard.isPressed(keyCode);
	}
	
	/**
	 * Retrieve the current accelerated graphics context. Note this
	 * method has been made package scope since only the other 
	 * members of the "java2D" package need to access it.
	 * 
	 * @return The current accelerated graphics context for this window
	 */
	Graphics2D getDrawGraphics() {
		return g;
	}
	
	/**
	 * Run the main game loop. This method keeps rendering the scene
	 * and requesting that the callback update its screen.
	 */
	private void gameLoop() {
		while (gameRunning) {
			// Get hold of a graphics context for the accelerated 
			// surface and blank it out
			g = (Graphics2D) strategy.getDrawGraphics();
			g.setColor(Color.blue);
			g.fillRect(0,0,800,600);
			
			if (game != null) {
				game.frameRendering();
			}

			// finally, we've completed drawing so clear up the graphics
			// and flip the buffer over
			g.dispose();
			strategy.show();
		}
	}
}