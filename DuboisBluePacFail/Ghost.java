import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Ghost implements Actor
{
	enum Mode
	{
		Chase,
		Evade,
		Scatter,
		Return,
	}
	
	protected boolean exitedHouse;
	protected short x, y, targX, targY, homeX, homeY, speed;	//all these numbers reflect grid coordinates. Speed is the number of frames between movements
	protected short dir, lastDir, possibleMoves; //bit 0 is up, bit 1 is left, bit 2 is down, bit 3 is right
	protected short dotCounter, dotLimit;
    protected short possMoveX;
    protected short houseX, houseY;
    protected short evadeLimit, nextScatter;
	
	protected BufferedImage normal, blue, eyes, image;
	protected BufferedWriter out;
	protected FileWriter writer;
	protected Mode mode;
	protected Pacman player;
	protected String name;
	
	public Ghost( String imageName, Pacman pm ) throws IOException
	{
		normal = ImageIO.read( new File(imageName) );
		blue = ImageIO.read( new File("Blue.gif") );
		eyes = ImageIO.read( new File("Eyes.gif") );
		image = normal;
		speed = 10;
		targX = 0;
		targY = 0;
		homeX = 0;
		homeY = 0;
		houseX = 13;
		houseY = 11;
		possMoveX = 0;
		dir = 0;	//0 is up, 1 is left, 2 is right, 2 is left
		player = pm;
		mode = Mode.Chase;
		exitedHouse = false;
		dotCounter = 0;
		name = imageName.substring( 0, imageName.indexOf( '.' ) );
	}
	
	@Override
	public void update() 
	{
		if( Driver.frameCount%speed == 0 )
		{
			evaluateScatter();
			if( evadeLimit != 0 )
				evadeLimit --;
			if( evadeLimit == 0 )
				mode = Mode.Chase;
			evaluateImage();
			if( exitedHouse )
				findDir();
			else
				evaluateDotCounter();
			determineTarget();
			move();
			lastDir = dir;
		}
	}

	public void evaluateScatter()
	{
		if( nextScatter == 0 )
			if( mode == Mode.Chase )
			{
				mode = Mode.Scatter;
				nextScatter = (short)(Math.random()*255);
			}
			else if( mode == Mode.Scatter )
			{
				mode = Mode.Chase;
				nextScatter = (short)(Math.random()*255);
			}
	}
	
	public void evaluateImage()
	{
		if( mode == Mode.Chase || mode == Mode.Scatter )
			image = normal;
		else if( mode == Mode.Evade )
			image = blue;
		else if( mode == Mode.Return )
			image = eyes;
	}
	
	public void determineTarget()
	{
		if( mode == Mode.Chase )
			assignTarget();
		else if( mode == Mode.Scatter )
			loadHome();
		else if( mode == Mode.Evade )
			if( Driver.frameCount%60 == 0 )
				randomMovement();
		else if( mode == Mode.Return )
			loadCenter();
	}
	
	@Override
	public void move() 
	{
		if( exitedHouse )
		{
			if( x == -1 )
				x = 27;
			else if( x == 28 )
				x = 0;
			if( dir == 0 )
			{
				if( Grid.grid[x][y-1] != 1 )
					y--;
			}
			else if( dir == 1 )
			{
				if( x != 0 )
				{
					if( Grid.grid[x-1][y] != 1 )
						x--;
				}
				else
					x--;
			}
			else if( dir == 2 )
			{
				if( Grid.grid[x][y+1] != 1 )
					y++;
			}
			else if( dir == 3 )
			{
				if( x != Grid.gridX-1 )
				{
					if( Grid.grid[x+1][y] != 1 )
						x++;
				}
				else
					x++;
			}
			if( x == -1 )
				x = 27;
			else if( x == 28 )
				x = 0;
		}
		else
		{
			if( dir == 0 )
				y--;
			else if( dir == 1 )
				x--;
			else if( dir == 2 )
				y++;
			else if( dir == 3 )
				x++;
		}
	}

	public void assignTarget() {}
	
	public void loadHome()
	{
		targX = homeX;
		targY = homeY;
	}
	
	public void loadCenter()
	{
		targX = houseX;
		targY = houseY;
	}
	
	public void randomMovement()
	{
		targX = (short)(Math.random()*255);
		targY = (short)(Math.random()*255);
	}
	
	public void evaluateDotCounter()
	{
		if( dotCounter >= dotLimit )
		{
			if( x < 13 )
				dir = 3;
			else if( x > 13 )
				dir = 1;
			else if( y > 12 )
				dir = 0;
			else if( y < 12 )
				dir = 2;
		}
		else
			dir = 4;
		if( y == 12 )
			exitedHouse = true;
	}
		
	//This is a wonderful algorithm. Please worship it.
	public void findDir()
	{
		loadPossibleMoves();
		if( Math.abs( targX-x ) > Math.abs( targY-y ) )	//If there is a greater difference in the x positions than the y
			dir = (short)(targX > x ? 3 : 1);			//if our x is less than our target's x, go right. Otherwise, go left.
		else											//If there is a greater difference in the y positions than the x
			dir = (short)(targY > y ? 2 : 0);			//if our y is less than our target's y, go down. Otherwise, go up
		//If the bit of possibleMoves which corresponds to the current dir is a zero, find a new dir. 
		while( (possibleMoves & (1 << dir )) == 0 )
		{
			dir++;
			if( dir == 4 )
				dir = 0;
		}
	}
	
	public void loadPossibleMoves()
	{
		possibleMoves = 0;
		if( Grid.grid[x][y-1] != 1 && lastDir != 2 )
			possibleMoves |= 1;	//set bit 1 equal to 1
		if( Grid.grid[x][y+1] != 1 && lastDir != 0 )
			possibleMoves |= 4;	//set bit 3 equal to 1
		if( x+1 != Grid.gridX )
		{
			if( Grid.grid[x+1][y] != 1 && lastDir != 1 )
				possibleMoves |= 8;	//set bit 4 equal to 1
		}
		else
			if( Grid.grid[0][y] != 1 && lastDir != 1 )
				possibleMoves |= 8;	//set bit 4 equal to 1
		if( x-1 != -1 )
		{
			if( Grid.grid[x-1][y] != 1 && lastDir != 3 )
				possibleMoves |= 2;	//set bit 3 equal to 1
		}
		
	}

	@Override
	public void paint( Graphics2D g2 ) 
	{
		//g2.setColor( Color.green );
		//Grid.drawSquare( targX, targY, g2 );
		g2.drawImage( image, (x*16)-8, (y*16)-8, null );
	}

	public void startEvade()
	{
		mode = Mode.Evade;
		evadeLimit = 60;
	}
	
	public void addDot()
	{
		dotCounter++;
	}
	
	public void setMode( Mode m )
	{
		mode = m;
	}
	
	public short getX() 
	{
		return x;
	}
	
	public short getY()
	{
		return y;
	}
	
	public short getDotCounter()
	{
		return dotCounter;
	}
	
	public short getDotLimit()
	{
		return dotLimit;
	}
	
	public String getName()
	{
		return name;
	}

	public Mode getMode()
	{
		return mode;
	}
	
	public void endStuff() throws IOException
	{
		writer.close();
	}
}