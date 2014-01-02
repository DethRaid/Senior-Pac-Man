import java.io.IOException;


public class Clyde extends Ghost 
{
	public Clyde( String imageName, Pacman pm ) throws IOException
	{
		super( imageName, pm );
		homeX = 0;
		homeY = 34;
		dotLimit = 60;
		x = 15;
		y = 14;
	}

	public void findTarget()
	{
		if( Grid.distance( x, y, player.getX(), player.getY() ) < 8 )
		{
			targX = homeX;
			targY = homeY;
		}
		else
		{
			targX = player.getX();
			targY = player.getY();
		}
	}
}
