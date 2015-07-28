/**
 * Represents the player and enables him to move and interact
 * 
 * @author Danil
 */

public class Player extends Entity {

	
	private Game game;
	protected double dx;
	protected double dy;
	private double te = 0;

	public Player(Game game,String sprite,int x,int y) {
		super(sprite,x,y);
		this.game = game;
	}

	public void setHorizontalMovement(double dx) {
		this.dx = dx;
	}

	public void setVerticalMovement(double dy) {
		this.dy = dy;
	}

	public double getHorizontalMovement() {
		return dx;
	}

	public double getVerticalMovement() {
		return dy;
	}

	public void setTimer() {
		te = 0;
	}

	public void collidedWith(Player other) {

	}

	public void move(long delta) {
		x += (delta * dx * te) / 1000;
		y += (delta * dy *te) / 1000;
		te = te +delta;

		if (y < 0 || y > 600 || x > 800 || x < 0) {	
			game.notifyDeath();
		}
	}
}