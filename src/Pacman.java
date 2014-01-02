import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;

public class Pacman implements Actor, KeyListener 
{
	private boolean fill;
	private int fillStart;
	private short x, y, speed, dir;	//dir of 0 is up, 1 is left, 2 is down, 3 is right. x, and y are in grid coordinates, speed is the number of frames between moves
	
	private ArrayList<Ghost> ghosts; 
	private Driver driver;
	
	public Pacman( Driver newDrive ) throws IOException
	{
		driver = newDrive;
		fill = false;
		fillStart = 0;
		x = 13;
		y = 17;
		speed = 8;
		dir = 0;
		ghosts = new ArrayList<Ghost>();
	}
	
	public void giveGhost( Ghost g )
	{
		ghosts.add( g );
	}
	
	@Override
	public void update() 
	{
		checkCurGridSpace();
		evaluateFill();
		if( Driver.frameCount%speed == 0 )
			move();
		if( Driver.frameCount%20 == 0 )
			fill = !fill;
	}

	@Override
	public void move() 
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
			if( Grid.grid[(x-1 == -1 ? 27 : x-1)][y] != 1 )
				x --;
		}
		else if( dir == 2 )
		{
			if( Grid.grid[x][y+1] != 1 )
				y ++;
		}
		else if( dir == 3 )
		{
			if( Grid.grid[(x+1 == 28 ? 0 : x+1)][y] != 1 )
				x ++;
		}
		if( x == -1 )
			x = 27;
		else if( x == 28 )
			x = 0;
	}

	@Override
	public void paint(Graphics2D g2) 
	{
		g2.setColor( Color.yellow );
		g2.fillArc( (x*16)-5, (y*16)-5, 26, 26, fillStart, (fill ? 360 : -300) );
	}
	
	public void checkCurGridSpace()
	{
		if( Grid.grid[x][y] == 2 )
		{
			Grid.grid[x][y] = 0;
			Driver.score += 100;
			addToGhostDotCounter();
		}
		else if( Grid.grid[x][y] == 3 )
		{
			Grid.grid[x][y] = 0;
			Driver.score += 500;
			driver.onEatSuperPellet();
		}else if( Grid.grid[x][y] == 4 )
		{
			Grid.grid[x][y] = 0;
			Driver.score += 200;
		}
	}
	
	public void evaluateFill()
	{
		if( dir == 0 )
			fillStart = 60;
		else if( dir == 1 )
			fillStart = 150;
		else if( dir == 2 )
			fillStart = 240;
		else if( dir == 3 )
			fillStart = 330;
	}
	
	public void addToGhostDotCounter()
	{
		for( Ghost g : ghosts )
			if( g.getDotCounter() < g.getDotLimit() )
			{
				g.addDot();
				return;
			}
	}
	
	//in the next four methods, the engine checks to see if the grid tile in the desired direction is a wall.
	//if so, dir remains constant. If it is not a wall, however, dir is changed to point in the desired direction 
	public void turnUp() 
	{
		if( Grid.grid[x][y-1] != 1 )
			dir = 0;
	}
	
	public void turnLeft() 
	{
		if( Grid.grid[x-1][y] != 1 )
			dir = 1;
	}
	
	public void turnDown() 
	{
		if( Grid.grid[x][y+1] != 1 )
			dir = 2;
	}
	
	public void turnRight() 
	{
		if( Grid.grid[x+1][y] != 1 )
			dir = 3;
	}

	public short getX()
	{
		return x;
	}
	
	public short getY()
	{
		return y;
	}
	
	public short getDir()
	{
		return dir;
	}

	@Override
	public void keyPressed(KeyEvent e) 
	{}

	@Override
	public void keyReleased(KeyEvent e) 
	{
		if( e.getKeyCode() == KeyEvent.VK_UP )
			turnUp();
		else if( e.getKeyCode() == KeyEvent.VK_LEFT )
			turnLeft();
		else if( e.getKeyCode() == KeyEvent.VK_DOWN )
			turnDown();
		else if( e.getKeyCode() == KeyEvent.VK_RIGHT )
			turnRight();
	}

	@Override
	public void keyTyped(KeyEvent e) 
	{}
}
