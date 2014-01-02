import java.io.IOException;

public class Blinky extends Ghost 
{
	public Blinky( String imageName, Pacman pm ) throws IOException 
	{
		super( imageName, pm );
		homeX = 27;
		homeY = -4;
		x = 13;
		y = 11;
		exitedHouse = true;
		dir = 1;
	}

	public void assignTarget()
	{
		targX = player.getX();
		targY = player.getY();
	}
}
