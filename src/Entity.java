
import java.awt.Rectangle;

public abstract class Entity {
	protected double x;
	protected double y;
	protected Sprite sprite;

	// Rectangles are used for collision detection
	private Rectangle me = new Rectangle();
	private Rectangle him = new Rectangle();
	
	//Entity is constructed based on the image and the location
	public Entity(String ref,int x,int y) {
		this.sprite = SpriteStore.get().getSprite(ref);
		this.x = x;
		this.y = y;
	}
	
	public void draw() {
		sprite.draw((int) x,(int) y);
	}
	
	public int getX() {
		return (int) x;
	}

	public int getY() {
		return (int) y;
	}
	
	public boolean collidesWith(Entity other) {
		me.setBounds((int) x,(int) y,sprite.getWidth(),sprite.getHeight());
		him.setBounds((int) other.x,(int) other.y,other.sprite.getWidth(),other.sprite.getHeight());

		return me.intersects(him);
	}
	
	public abstract void collidedWith(Player other);
}