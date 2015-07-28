
public class Block extends Entity {


	public Block(int x,int y) {
		super("Ressources/Block.gif",x,y);
	}

	public void collidedWith(Player other) {
	
			if (other.dx<0){
				other.x = this.getX() + this.sprite.getWidth();
			}
			if (other.getHorizontalMovement()>0){
				other.x = this.getX() - other.sprite.getWidth();
			}
			if (other.getVerticalMovement()>0){
				other.y = this.getY() - this.sprite.getHeight();
			}
			if (other.getVerticalMovement()<0){
				other.y = this.getY() + this.sprite.getHeight();
			}
			other.setHorizontalMovement(0);
			other.setVerticalMovement(0);
			other.setTimer();	
		
	}

}