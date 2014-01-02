import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Grid 
{
	public static short gridX = 28, gridY = 31;
	public static short[][] grid; //0 = empty, 1 = wall, 2 = dot, 3 = big dot, 4 = cherry
	
	private int tunnelCount, maxTunnelCount;
	private short dir, curX, curY;
	
	private BufferedImage cherry;
	private Random ran;
	
	public Grid() throws IOException
	{
		ran = new Random();
		grid = new short[gridX][gridY];
		cherry = ImageIO.read( new File("Cherry.gif") );
		loadBoringPacGrid();	//at bottom of the page because readability
		//un-comment for buggy awesomeness
		//int k = createGrid();
	}
	
	/* First loads the grid with walls. Then loops through the grid creating tunnels.
	 * dir controls the direction of the tunneling. It has a 25% chance to be changed every block.
	 * 0 is up, 1 is left, 2 is down, 3 is right
	 * If a hole it cut into the edge of the grid, the tunnel picks up again at the opposite edge*/

	public int createGrid()
	{
		tunnelCount = 0;
		maxTunnelCount = (int)((gridX*gridY)/1.5);
		dir = (short)(ran.nextInt()%4);
		curX = (short)(gridX/2);
		curY = (short)(gridY/2);
		for( int i = 0; i < gridX; i++ )
			for( int j = 0; j < gridY; j++ )
				grid[i][j] = 1;					
		grid[curX][curY] = 2;
		return cutTunnel();
	}
	
	public int cutTunnel()
	{
		if( ran.nextInt()%4 == 0 )
			dir = (short)(ran.nextInt()%4);
		if( dir == 0 )
			curY--;
		else if( dir == 1 )
			curX--;
		else if( dir == 2 )
			curY++;
		else if( dir == 3 )
			curX++;
		if( curX == -1 )
			curX = 27;
		else if( curX == 28 )
			curX = 0;
		else if( curY == -1 )
			curY = 29;
		else if( curY == 30 )
			curY = 0;
		grid[curX][curY] = 2;
		tunnelCount++;
		if( tunnelCount < maxTunnelCount )
			return cutTunnel();
		return 0;
	}
	
	public void interestingDrawingProc( Graphics2D g2 )
	{
		for( int j = 0; j < gridX; j++ )
			for( int k = 0; k < gridY; k++ )
				if( grid[j][k] == 0 )
				{
					g2.setColor( Color.BLACK );
					g2.fillRect( j*16, k*16, 16, 16 );
				}
				else if( grid[j][k] == 1 )
				{
					g2.setColor( Color.BLUE );
					g2.fillRect( j*16, k*16, 16, 16 );
				}
				else if( grid[j][k] == 2 )
				{
					//g2.setColor( Color.BLACK );
					//g2.fillRect( j*16, k*16, 16, 16 );
					g2.setColor( Color.yellow );
					g2.fillOval( j*16+4, k*16+4, 8, 8 );
				}
				else if( grid[j][k] == 3 )
				{
					g2.setColor( Color.yellow );
					g2.fillOval( j*16, k*16, 16, 16 );
				}
				else if( grid[j][k] == 4 )
					g2.drawImage( cherry, (j*16)-8, (k*16)-8, null );
	}
	
	public void slightlyMoreConformistDrawingProc( Graphics2D g2 )
	{
		for( int j = 0; j < gridX; j++ )
			for( int k = 0; k < gridY; k++ )
				if( grid[j][k] == 2 )
				{
					g2.setColor( Color.yellow );
					g2.fillOval( j*16+4, k*16+4, 8, 8 );
				}
				else if( grid[j][k] == 3 )
				{
					g2.setColor( Color.yellow );
					g2.fillOval( j*16, k*16, 16, 16 );
				}
				else if( grid[j][k] == 4 )
					g2.drawImage( cherry, (j*16)-8, (k*16)-8, null );
	}

	public static short nextTile( short x, short y, short dir )
	{
		if( x == -1 )
			x = 30;
		else if( x == 31 )
			x = 0;
		if( dir == 0 )
			return grid[x][y-1];
		else if( dir == 1 )
			return grid[x-1][y];
		else if( dir == 2 )
			return grid[x][y+1];
		else if( dir == 3 )
			return grid[x+1][y];
		return -1;
	}
	
	public static void drawSquare( int x, int y, Graphics2D g2 )
	{
		g2.fillRect( x*16, y*16, 16, 16 );
	}
	
	public static float distance( int x1, int y1, int x2, int y2 )
	{
		return (float)Math.sqrt( Math.pow( x1-x2, 2 ) - Math.pow( y1-y2, 2 ) );
	}
	
	public void loadBoringPacGrid()
	{
		//First, set all grid squares to be a white dot
		for( int i = 0; i < gridX; i++ )
			for( int j = 0; j < gridY; j++ )
				grid[i][j] = 2;
		//next, set the top and bottom edges to be walls
		for( int i = 0; i < gridX; i++ )
		{
			grid[i][0] = 1;
			grid[i][gridY-1] = 1;
		}
		//Set the Ghost pen to be walls, because pathing
		grid[11][13] = 1;
		grid[12][13] = 1;
		grid[14][13] = 1;
		grid[15][13] = 1;
		grid[16][13] = 1;
		grid[13][13] = 1;
		grid[11][14] = 1;
		grid[12][14] = 1;
		grid[14][14] = 1;
		grid[15][14] = 1;
		grid[16][14] = 1;
		grid[13][14] = 1;
		grid[11][15] = 1;
		grid[12][15] = 1;
		grid[14][15] = 1;
		grid[15][15] = 1;
		grid[16][15] = 1;
		grid[13][15] = 1;
		//finally, set all the odd things in the maze to be walls, too.
		//un-comment interestingDrawingProc() and comment out slightlyMoreConformistDrawingProc() to see the walls render prettily
		grid[2][2] = 1;
		grid[3][2] = 1;
		grid[4][2] = 1;
		grid[5][2] = 1;
		grid[2][3] = 1;
		grid[3][3] = 1;
		grid[4][3] = 1;
		grid[5][3] = 1;
		grid[2][4] = 1;
		grid[3][4] = 1;
		grid[4][4] = 1;
		grid[5][4] = 1;
		
		grid[2][6] = 1;
		grid[3][6] = 1;
		grid[4][6] = 1;
		grid[5][6] = 1;
		grid[2][7] = 1;
		grid[3][7] = 1;
		grid[4][7] = 1;
		grid[5][7] = 1;
		
		grid[8][2] = 1;
		grid[9][2] = 1;
		grid[10][2] = 1;
		grid[11][2] = 1;
		grid[7][2] = 1;
		grid[8][3] = 1;
		grid[9][3] = 1;
		grid[10][3] = 1;
		grid[11][3] = 1;
		grid[7][3] = 1;
		grid[7][4] = 1;
		grid[8][4] = 1;
		grid[9][4] = 1;
		grid[10][4] = 1;
		grid[11][4] = 1;
		
		grid[13][1] = 1;
		grid[14][1] = 1;
		grid[13][2] = 1;
		grid[14][2] = 1;
		grid[13][3] = 1;
		grid[14][3] = 1;
		grid[13][4] = 1;
		grid[14][4] = 1;
		
		grid[16][2] = 1;
		grid[17][2] = 1;
		grid[18][2] = 1;
		grid[19][2] = 1;
		grid[20][2] = 1;
		grid[16][3] = 1;
		grid[17][3] = 1;
		grid[18][3] = 1;
		grid[19][3] = 1;
		grid[20][3] = 1;
		grid[16][4] = 1;
		grid[17][4] = 1;
		grid[18][4] = 1;
		grid[19][4] = 1;
		grid[20][4] = 1;
		
		grid[22][2] = 1;
		grid[23][2] = 1;
		grid[24][2] = 1;
		grid[25][2] = 1;
		grid[22][3] = 1;
		grid[23][3] = 1;
		grid[24][3] = 1;
		grid[25][3] = 1;
		grid[22][4] = 1;
		grid[23][4] = 1;
		grid[24][4] = 1;
		grid[25][4] = 1;
		
		grid[22][6] = 1;
		grid[23][6] = 1;
		grid[24][6] = 1;
		grid[25][6] = 1;
		grid[22][7] = 1;
		grid[23][7] = 1;
		grid[24][7] = 1;
		grid[25][7] = 1;
		
		grid[8][7] = 1;
		grid[7][7] = 1;
		grid[8][8] = 1;
		grid[7][8] = 1;
		grid[8][9] = 1;
		grid[7][9] = 1;
		grid[8][10] = 1;
		grid[9][10] = 1;
		grid[10][10] = 1;
		grid[11][10] = 1;
		grid[7][10] = 1;
		grid[8][11] = 1;
		grid[9][9] = 1;
		grid[10][9] = 1;
		grid[11][9] = 1;
		grid[7][11] = 1;
		grid[8][12] = 1;
		grid[7][12] = 1;
		grid[8][13] = 1;
		grid[7][13] = 1;
		grid[8][6] = 1;
		grid[7][6] = 1;
		
		grid[10][6] = 1;
		grid[11][6] = 1;
		grid[12][6] = 1;
		grid[13][6] = 1;
		grid[14][6] = 1;
		grid[15][6] = 1;
		grid[16][6] = 1;
		grid[17][6] = 1;
		grid[10][7] = 1;
		grid[11][7] = 1;
		grid[12][7] = 1;
		grid[13][7] = 1;
		grid[14][7] = 1;
		grid[15][7] = 1;
		grid[16][7] = 1;
		grid[17][7] = 1;
		grid[14][8] = 1;
		grid[13][8] = 1;
		grid[14][9] = 1;
		grid[13][9] = 1;
		grid[14][10] = 1;
		grid[13][10] = 1;
		
		grid[19][6] = 1;
		grid[20][6] = 1;
		grid[19][7] = 1;
		grid[20][7] = 1;
		grid[19][8] = 1;
		grid[20][8] = 1;
		grid[16][9] = 1;
		grid[17][9] = 1;
		grid[18][9] = 1;
		grid[19][9] = 1;
		grid[20][9] = 1;
		grid[16][10] = 1;
		grid[17][10] = 1;
		grid[18][10] = 1;
		grid[19][13] = 1;
		grid[20][13] = 1;
		grid[19][10] = 1;
		grid[20][10] = 1;
		grid[19][11] = 1;
		grid[20][11] = 1;
		grid[19][12] = 1;
		grid[20][12] = 1;
		
		grid[0][1] = 1;
		grid[0][2] = 1;
		grid[0][3] = 1;
		grid[0][4] = 1;
		grid[0][5] = 1;
		grid[0][6] = 1;
		grid[0][7] = 1;
		grid[0][8] = 1;

		grid[27][1] = 1;
		grid[27][2] = 1;
		grid[27][3] = 1;
		grid[27][4] = 1;
		grid[27][5] = 1;
		grid[27][6] = 1;
		grid[27][7] = 1;
		grid[27][8] = 1;
		
		grid[0][9] = 1;
		grid[1][9] = 1;
		grid[2][9] = 1;
		grid[3][9] = 1;
		grid[4][9] = 1;
		grid[5][9] = 1;
		grid[0][10] = 1;
		grid[1][10] = 1;
		grid[2][10] = 1;
		grid[3][10] = 1;
		grid[4][10] = 1;
		grid[5][10] = 1;
		grid[0][11] = 1;
		grid[1][11] = 1;
		grid[2][11] = 1;
		grid[3][11] = 1;
		grid[4][11] = 1;
		grid[5][11] = 1;
		grid[0][12] = 1;
		grid[1][12] = 1;
		grid[2][12] = 1;
		grid[3][12] = 1;
		grid[4][12] = 1;
		grid[5][12] = 1;
		grid[0][13] = 1;
		grid[1][13] = 1;
		grid[2][13] = 1;
		grid[3][13] = 1;
		grid[4][13] = 1;
		grid[5][13] = 1;
		
		grid[22][9] = 1;
		grid[23][9] = 1;
		grid[24][9] = 1;
		grid[25][9] = 1;
		grid[26][9] = 1;
		grid[27][9] = 1;
		grid[22][10] = 1;
		grid[23][10] = 1;
		grid[24][10] = 1;
		grid[25][10] = 1;
		grid[26][10] = 1;
		grid[27][10] = 1;
		grid[22][11] = 1;
		grid[23][11] = 1;
		grid[24][11] = 1;
		grid[25][11] = 1;
		grid[26][11] = 1;
		grid[27][11] = 1;
		grid[22][12] = 1;
		grid[23][12] = 1;
		grid[24][12] = 1;
		grid[25][12] = 1;
		grid[26][12] = 1;
		grid[27][12] = 1;
		grid[22][13] = 1;
		grid[23][13] = 1;
		grid[24][13] = 1;
		grid[25][13] = 1;
		grid[26][13] = 1;
		grid[27][13] = 1;
		
		grid[10][12] = 1;
		grid[11][12] = 1;
		grid[12][12] = 1;
		grid[13][12] = 1;
		grid[14][12] = 1;
		grid[15][12] = 1;
		grid[16][12] = 1;
		grid[17][12] = 1;
		grid[10][13] = 1;
		grid[17][13] = 1;
		grid[10][14] = 1;
		grid[17][14] = 1;
		grid[10][15] = 1;
		grid[17][15] = 1;
		grid[10][16] = 1;
		grid[11][16] = 1;
		grid[12][16] = 1;
		grid[13][16] = 1;
		grid[14][16] = 1;
		grid[15][16] = 1;
		grid[16][16] = 1;
		grid[17][16] = 1;
		
		grid[0][15] = 1;
		grid[1][15] = 1;
		grid[2][15] = 1;
		grid[3][15] = 1;
		grid[4][15] = 1;
		grid[5][15] = 1;
		grid[0][16] = 1;
		grid[1][16] = 1;
		grid[2][16] = 1;
		grid[3][16] = 1;
		grid[4][16] = 1;
		grid[5][16] = 1;
		grid[0][17] = 1;
		grid[1][17] = 1;
		grid[2][17] = 1;
		grid[3][17] = 1;
		grid[4][17] = 1;
		grid[5][17] = 1;
		grid[0][18] = 1;
		grid[1][18] = 1;
		grid[2][18] = 1;
		grid[3][18] = 1;
		grid[4][18] = 1;
		grid[5][18] = 1;
		grid[0][19] = 1;
		grid[1][19] = 1;
		grid[2][19] = 1;
		grid[3][19] = 1;
		grid[4][19] = 1;
		grid[5][19] = 1;
		
		grid[7][15] = 1;
		grid[8][15] = 1;
		grid[7][16] = 1;
		grid[8][16] = 1;
		grid[7][17] = 1;
		grid[8][17] = 1;
		grid[7][18] = 1;
		grid[8][18] = 1;
		grid[7][19] = 1;
		grid[8][19] = 1;

		grid[10][19] = 1;
		grid[11][19] = 1;
		grid[12][19] = 1;
		grid[13][22] = 1;
		grid[14][22] = 1;
		grid[15][19] = 1;
		grid[16][19] = 1;
		grid[17][19] = 1;
		grid[10][18] = 1;
		grid[11][18] = 1;
		grid[12][18] = 1;
		grid[13][18] = 1;
		grid[14][18] = 1;
		grid[15][18] = 1;
		grid[16][18] = 1;
		grid[17][18] = 1;
		grid[14][19] = 1;
		grid[13][19] = 1;
		grid[14][20] = 1;
		grid[13][20] = 1;
		grid[14][21] = 1;
		grid[13][21] = 1;

		grid[19][15] = 1;
		grid[20][15] = 1;
		grid[19][16] = 1;
		grid[20][16] = 1;
		grid[19][17] = 1;
		grid[20][17] = 1;
		grid[19][18] = 1;
		grid[20][18] = 1;
		grid[19][19] = 1;
		grid[20][19] = 1;
		
		grid[22][15] = 1;
		grid[23][15] = 1;
		grid[24][15] = 1;
		grid[25][15] = 1;
		grid[26][15] = 1;
		grid[27][15] = 1;
		grid[22][16] = 1;
		grid[23][16] = 1;
		grid[24][16] = 1;
		grid[25][16] = 1;
		grid[26][16] = 1;
		grid[27][16] = 1;	
		grid[22][17] = 1;
		grid[23][17] = 1;
		grid[24][17] = 1;
		grid[25][17] = 1;
		grid[26][17] = 1;
		grid[27][17] = 1;
		grid[22][18] = 1;
		grid[23][18] = 1;
		grid[24][18] = 1;
		grid[25][18] = 1;
		grid[26][18] = 1;
		grid[27][18] = 1;
		grid[22][19] = 1;
		grid[23][19] = 1;
		grid[24][19] = 1;
		grid[25][19] = 1;
		grid[26][19] = 1;
		grid[27][19] = 1;
		
		grid[0][20] = 1;
		grid[0][21] = 1;
		grid[0][22] = 1;
		grid[0][23] = 1;
		grid[0][24] = 1;
		grid[0][25] = 1;
		grid[0][26] = 1;
		grid[0][27] = 1;
		grid[0][28] = 1;
		grid[0][29] = 1;
		grid[1][24] = 1;
		grid[2][24] = 1;
		grid[1][25] = 1;
		grid[2][25] = 1;
		
		grid[2][21] = 1;
		grid[3][21] = 1;
		grid[4][21] = 1;
		grid[5][21] = 1;
		grid[2][22] = 1;
		grid[3][22] = 1;
		grid[4][22] = 1;
		grid[5][22] = 1;
		grid[4][23] = 1;
		grid[5][23] = 1;
		grid[4][24] = 1;
		grid[5][24] = 1;
		grid[4][25] = 1;
		grid[5][25] = 1;
		
		grid[7][21] = 1;
		grid[8][21] = 1;
		grid[9][21] = 1;
		grid[10][21] = 1;
		grid[11][21] = 1;
		grid[7][22] = 1;
		grid[8][22] = 1;
		grid[9][22] = 1;
		grid[10][22] = 1;
		grid[11][22] = 1;
		
		grid[16][21] = 1;
		grid[17][21] = 1;
		grid[18][21] = 1;
		grid[19][21] = 1;
		grid[20][21] = 1;
		grid[16][22] = 1;
		grid[17][22] = 1;
		grid[18][22] = 1;
		grid[19][22] = 1;
		grid[20][22] = 1;
		
		grid[22][21] = 1;
		grid[23][21] = 1;
		grid[24][21] = 1;
		grid[25][21] = 1;
		grid[22][22] = 1;
		grid[23][22] = 1;
		grid[24][22] = 1;
		grid[25][22] = 1;
		grid[22][23] = 1;
		grid[23][23] = 1;
		grid[23][24] = 1;
		grid[22][24] = 1;
		grid[22][25] = 1;
		grid[23][25] = 1;
		
		grid[8][24] = 1;
		grid[7][24] = 1;
		grid[7][25] = 1;
		grid[8][25] = 1;
		grid[7][26] = 1;
		grid[8][26] = 1;
		grid[2][27] = 1;
		grid[3][27] = 1;
		grid[4][27] = 1;
		grid[5][27] = 1;
		grid[6][27] = 1;
		grid[7][27] = 1;
		grid[8][27] = 1;
		grid[9][27] = 1;
		grid[10][27] = 1;
		grid[11][27] = 1;
		grid[2][28] = 1;
		grid[3][28] = 1;
		grid[4][28] = 1;
		grid[5][28] = 1;
		grid[6][28] = 1;
		grid[7][28] = 1;
		grid[8][28] = 1;
		grid[9][28] = 1;
		grid[10][28] = 1;
		grid[11][28] = 1;
		
		grid[10][24] = 1;
		grid[11][24] = 1;
		grid[12][24] = 1;
		grid[13][24] = 1;
		grid[14][24] = 1;
		grid[15][24] = 1;
		grid[16][24] = 1;
		grid[17][24] = 1;
		grid[10][25] = 1;
		grid[11][25] = 1;
		grid[12][25] = 1;
		grid[13][25] = 1;
		grid[14][25] = 1;
		grid[15][25] = 1;
		grid[16][25] = 1;
		grid[17][25] = 1;
		grid[13][26] = 1;
		grid[14][26] = 1;
		grid[13][27] = 1;
		grid[14][27] = 1;
		grid[13][28] = 1;
		grid[14][28] = 1;
		
		grid[19][24] = 1;
		grid[20][24] = 1;
		grid[19][25] = 1;
		grid[20][25] = 1;
		grid[19][26] = 1;
		grid[20][26] = 1;
		grid[16][27] = 1;
		grid[17][27] = 1;
		grid[18][27] = 1;
		grid[19][27] = 1;
		grid[20][27] = 1;
		grid[21][27] = 1;
		grid[22][27] = 1;
		grid[23][27] = 1;
		grid[24][27] = 1;
		grid[25][27] = 1;
		grid[16][28] = 1;
		grid[17][28] = 1;
		grid[18][28] = 1;
		grid[19][28] = 1;
		grid[20][28] = 1;
		grid[21][28] = 1;
		grid[22][28] = 1;
		grid[23][28] = 1;
		grid[24][28] = 1;
		grid[25][28] = 1;
		
		grid[27][20] = 1;
		grid[27][21] = 1;
		grid[27][22] = 1;
		grid[27][23] = 1;
		grid[27][24] = 1;
		grid[27][25] = 1;
		grid[27][26] = 1;
		grid[27][27] = 1;
		grid[27][28] = 1;
		grid[27][29] = 1;
		grid[25][24] = 1;
		grid[26][24] = 1;
		grid[25][25] = 1;
		grid[26][25] = 1;
		
		grid[1][3] = 3;
		grid[1][23] = 3;
		grid[26][3] = 3;
		grid[26][23] = 3;
	}
}