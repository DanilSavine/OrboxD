/**
 * Represents the final destination of the puzzle
 * @author Danil
 *
 */
public class Win extends Entity {
	private Game game;

	public Win(Game game,String ref,int x,int y) {
		super(ref,x,y);
		this.game = game;
	}

	public void collidedWith(Player other) {
		game.notifyWin();
	}
}