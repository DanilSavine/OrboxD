
import java.awt.Canvas;
import java.awt.event.KeyEvent;
import java.util.ArrayList;


public class Game extends Canvas {

	private static final long serialVersionUID = 1L;
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private Player player;
	private double acceleration = 2;
	private int level = 1;
	private int maxlevel = 2;
	private Sprite message;
	private boolean waitingForKeyPress = true;
	private long lastLoopTime = System.nanoTime();
	private static GameWindow window;

	private Sprite pressAnyKey;
	private Sprite youWin;
	private Sprite gotYou;

	private long lastFpsTime = 0;
	private int fps;

	private String windowTitle = "Orbox D";



	/**
	 * Game constructor creating the window
	 */
	public Game() {

		if (window == null) {
			window = new GameWindow();
		}

		window.setResolution(800,600);
		window.setGameWindowCallback(this);
		window.setTitle(windowTitle);

		window.startRendering();
	}
	
	public static GameWindow getGameWindow() {
		return window;
	}

	/**
	 * Initiates elements of the game and starts it
	 */
	public void initialise() {
		gotYou = SpriteStore.get().getSprite("Ressources/gotyou.gif");
		youWin = SpriteStore.get().getSprite("Ressources/youwin.gif");
		pressAnyKey = SpriteStore.get().getSprite("Ressources/pressanykey.gif");
		// setup the initial game state
		message = pressAnyKey;

		startGame();
	}

	/**
	 * Starts a new game
	 */
	private void startGame() {
		entities.clear();
		Level lvl = new Level(this,"src/Ressources/level"+level+".xml",entities,player);
		lvl.initEntities();
		player = lvl.player;



	}
	/**
	 * Notifies the player of his death and starts a new game 
	 */

	public void notifyDeath() {
		message = gotYou;
		waitingForKeyPress = true;
		startGame();
	}

	public void notifyWin() {
		message = youWin;
		waitingForKeyPress = true;
		if (level==maxlevel){
			level = 0;
		}
		level = level+1;
		startGame();
	}

	/**
	 * The core of the game cycle
	 */
	public void frameRendering() {		
		//we don't want to go too fast at refreshing our game 
		try {
			Thread.sleep((-lastLoopTime+10+System.nanoTime())/1000000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//we'll use delta for movement of the player
		long delta = (System.nanoTime() - lastLoopTime)/1000000;
		lastLoopTime = System.nanoTime();

		lastFpsTime += delta;
		fps++;

		if (lastFpsTime >= 1000) {
			window.setTitle(windowTitle+" (FPS: "+fps+")");
			lastFpsTime = 0;
			fps = 0;
		}



		//the player moves
		player.move(delta);

		//let's draw them all !
		player.draw();
		for (int i=0;i<entities.size();i++) {
			Entity entity = (Entity) entities.get(i);

			entity.draw();
		}

		if (waitingForKeyPress) {
			message.draw(325,250);
		}
		//we detect collisions between the player and its environment
		for (int p=0;p<entities.size();p++) {
			Entity him = (Entity) entities.get(p);

			if (player.collidesWith(him)) {
				him.collidedWith(player);

			}
		}



		//here we move, variables are local because we only need them at
		//this turn

		boolean leftPressed = window.isKeyPressed(KeyEvent.VK_LEFT);
		boolean rightPressed = window.isKeyPressed(KeyEvent.VK_RIGHT);
		boolean upPressed = window.isKeyPressed(KeyEvent.VK_UP);
		boolean downPressed = window.isKeyPressed(KeyEvent.VK_DOWN);
		boolean spacePressed = window.isKeyPressed(KeyEvent.VK_SPACE);
		
		// but the game must have started !
		if(!waitingForKeyPress){
			if ((leftPressed) && (!rightPressed) && player.dy == player.dx) {
				player.setHorizontalMovement(-acceleration);
			}  if ((rightPressed) && (!leftPressed) && player.dy == player.dx) {
				player.setHorizontalMovement(acceleration);
			} else if ((upPressed) && (!downPressed) && player.dy == player.dx) {
				player.setVerticalMovement(-acceleration);
			} else if ((downPressed) && (!upPressed) && player.dy == player.dx) {
				player.setVerticalMovement(acceleration);
			} 
		}
		//otherwise we can start the game ! 
		else if (spacePressed){
			waitingForKeyPress = false;
		}
		// if escape has been pressed, stop the game
		if (window.isKeyPressed(KeyEvent.VK_ESCAPE)) {
			windowClosed();
		}
	}

	public void windowClosed() {
		System.exit(0);
	}


	public static void main(String argv[]) {
		new Game();	
	}
}