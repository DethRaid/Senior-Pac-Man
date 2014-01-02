import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Driver extends JFrame implements Runnable, KeyListener
{
	public static int frameCount;
	public static int score;
	
	private static final long serialVersionUID = 5401553244118461976L;
	
	private boolean lost;
	private short nextCherry;
	
	private Blinky blinky;
	private BufferedImage background;
	private Clyde clyde;
	private Container con;
	private Grid grid;
	private Inky inky;
	private Pacman pacman;
	private Pinky pinky;
	private Random ran;
	private Thread t;
	
	public Driver() throws IOException
	{
		lost = false;
		ran = new Random();
		nextCherry = (short)(ran.nextInt()%255);
		if( nextCherry < 0 )
			nextCherry *= -1;
		background = ImageIO.read( new File("background.gif") );
		grid = new Grid();
		pacman = new Pacman( this );
		blinky = new Blinky( "Blinky.gif", pacman );
		pinky = new Pinky( "Pinky.gif", pacman );
		inky = new Inky( "Inky.gif", pacman, blinky );
		clyde = new Clyde( "Clyde.gif", pacman );
		pacman.giveGhost( pinky );
		pacman.giveGhost( inky );
		pacman.giveGhost( clyde );
		addKeyListener( pacman );
		con = getContentPane();
		con.setLayout( new FlowLayout() );
		t = new Thread( this );
		t.start();
		setDefaultCloseOperation( EXIT_ON_CLOSE );
		addKeyListener( this );
	}
	
	@SuppressWarnings("static-access")
	public void run() 
	{
		try
		{
			while( !lost )
			{
				if( !lost )
				{
					frameCount++;
					updateActors();
					checkDeath();
					evaluateCherryPlacement();
				}
				t.sleep( 16 );
				repaint();
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	
	public void update( Graphics g )
	{
		paint( g );
	}
	
	public void updateActors()
	{
		pacman.update();
		blinky.update();
		pinky.update();
		inky.update();
		clyde.update();
	}
	
	public void checkDeath()
	{
		if( pacman.getX() == blinky.getX() && pacman.getY() == blinky.getY() )
		{
			if( blinky.getMode() == Ghost.Mode.Evade || blinky.getMode() == Ghost.Mode.Return )
			{
				blinky.setMode( Ghost.Mode.Return );
				score += 200;
			}
			else
				lost = true;
		}
		else if( pacman.getX() == pinky.getX() && pacman.getY() == pinky.getY() )
		{
			if( pinky.getMode() == Ghost.Mode.Evade || pinky.getMode() == Ghost.Mode.Return )
			{
				pinky.setMode( Ghost.Mode.Return );
				score += 200;
			}
			else
				lost = true;
		}
		else if( pacman.getX() == inky.getX() && pacman.getY() == inky.getY() )
		{
			if( inky.getMode() == Ghost.Mode.Evade || inky.getMode() == Ghost.Mode.Return )
			{
				inky.setMode( Ghost.Mode.Return );
				score += 200;
			}
			else
				lost = true;
		}
		else if( pacman.getX() == clyde.getX() && pacman.getY() == clyde.getY() )
		{
			if( clyde.getMode() == Ghost.Mode.Evade || clyde.getMode() == Ghost.Mode.Return )
			{
				clyde.setMode( Ghost.Mode.Return );
				score += 200;
			}
			else
				lost = true;
		}
	}
	
	public void evaluateCherryPlacement()
	{
		nextCherry--;
		if( nextCherry == 0 )
		{
			Grid.grid[13][17] = 4;
			nextCherry = (short)(ran.nextInt()%255);
			if( nextCherry < 0 )
				nextCherry *= -1;
		}
	}
	
	public void onEatSuperPellet()
	{
		blinky.startEvade();
		pinky.startEvade();
		inky.startEvade();
		clyde.startEvade();
	}
	
	public void paint( Graphics g )
	{
		Image i = createImage( getSize().width, getSize().height );
		Graphics2D g2 = (Graphics2D)i.getGraphics();
		g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		g2.setRenderingHint( RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY );
		g2.drawImage( background, 0, 0, this );
		grid.slightlyMoreConformistDrawingProc( g2 );
		//be sure to un-comment if you've un-commented line 22 in Grid.java or if you just want to look at blue squares
		//grid.interestingDrawingProc( g2 );
		pacman.paint( g2 );
		blinky.paint( g2 );
		pinky.paint( g2 );
		inky.paint( g2 );
		clyde.paint( g2 );
		if( lost )
		{
			g2.setColor( Color.white );
	        g2.setFont( new Font("Serif", Font.PLAIN, 96) );	 
	        g2.drawString("You Lost", 40, 120); 
	        g2.setFont( new Font("Serif", Font.PLAIN, 72) );
	        g2.drawString( "Points: " +score, 40, 200 );
		}
		g2.dispose();
		g.drawImage( i, 8, 30, this );
	}	
	
	public static void main( String[] args )
	{
		Driver d;
		try 
		{
			d = new Driver();
			d.setSize( Grid.gridX*16+16, Grid.gridY*16+38 );
			d.setVisible( true );
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}
}
