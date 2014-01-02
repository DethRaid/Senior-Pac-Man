import java.io.IOException;

public class Pinky extends Ghost {

	public Pinky( String imageName, Pacman pm ) throws IOException 
	{
		super(imageName, pm);
		homeX = 2;
		homeY = -4;
		x = 13;
		y = 14;
	}
	
	public void assignTarget()
	{
		if( player.getDir() == 0 )
		{
			targX = player.getX();
			targY = (short)(player.getY()-4);
		}
		else if( player.getDir() == 1 )
		{
			targX = (short)(player.getX()-4);
			targY = player.getY();
		}
		else if( player.getDir() == 2 )
		{
			targX = player.getX();
			targY = (short)(player.getY()+4);
		}
		else if( player.getDir() == 3 )
		{
			targX = (short)(player.getX()+4);
			targY = player.getY();
		}
	}
}
