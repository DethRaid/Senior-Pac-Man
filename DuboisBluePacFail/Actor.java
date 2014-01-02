import java.awt.Graphics2D;

public interface Actor 
{
	public void update();
	public void move();
	public void paint( Graphics2D g2 );
}