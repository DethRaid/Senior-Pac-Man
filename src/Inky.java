import java.io.IOException;


public class Inky extends Ghost 
{
	private short xOffset, yOffset;
	
	private Blinky blinky;
	
	public Inky( String imageName, Pacman pm, Blinky blin ) throws IOException 
	{
		super( imageName, pm );
		dotLimit = 30;
		blinky = blin;
		x = 11;
		y = 14;
		homeX = 27;
		homeY = 34;
	}

	public void assignTarget()
	{
		if( player.getDir() == 0 )
		{
			xOffset = (short) (player.getX()-blinky.getX());
			yOffset = (short) (player.getY()-2-blinky.getY());
			targX = (short) (player.getX()+xOffset);
			targY = (short) (player.getY()-2+yOffset);
		}
		else if( player.getDir() == 1 )
		{
			xOffset = (short) (player.getX()-2-blinky.getX());
			yOffset = (short) (player.getY()-blinky.getY());
			targX = (short) (player.getX()-2+xOffset);
			targY = (short) (player.getY()+yOffset);
		}
		else if( player.getDir() == 2 )
		{
			xOffset = (short) (player.getX()-blinky.getX());
			yOffset = (short) (player.getY()+2-blinky.getY());
			targX = (short) (player.getX()+xOffset);
			targY = (short) (player.getY()+2+yOffset);
		}
		else if( player.getDir() == 3 )
		{
			xOffset = (short) (player.getX()+2-blinky.getX());
			yOffset = (short) (player.getY()-blinky.getY());
			targX = (short) (player.getX()+2+xOffset);
			targY = (short) (player.getY()+yOffset);
		}
	}
}
