package engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class Bitmap implements Cloneable
{	
	private final int width;
	private final int height;
	
	private final BufferedImage image;	
	
	private final byte[] comps;
	protected final Graphics2D g;
	
	public Bitmap(int width, int height)
	{
		image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		
		this.width = width;
		this.height = height;
		
		comps = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
		g = image.createGraphics();
	}
	
	public Bitmap(String fileName) throws IOException
	{
		BufferedImage buffer = ImageIO.read(new File(fileName));		
		
		width = buffer.getWidth();
		height = buffer.getHeight();
		
		image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

		g = image.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		g.drawImage(buffer, 0, 0, null);

		comps = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
	}
	
	private Bitmap(BufferedImage image)
	{		
		width = image.getWidth();
		height = image.getHeight();
		
		this.image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

		g = this.image.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		g.drawImage(image, 0, 0, null);

		comps = ((DataBufferByte)this.image.getRaster().getDataBuffer()).getData();
	}
	
	public int getWidth() 				{ return width; }	
	public int getHeight() 				{ return height; }
	public BufferedImage getImage()		{ return image; }
	public byte getComponent(int index) { return comps[index]; }
	public Graphics2D getGraphics2D()	{ return g; }
	
	public void setComponent(int index, byte v) { comps[index] = v; }
	
	public void clear(byte shade)
	{
		Arrays.fill(comps, shade);
	}
	
	public void drawPixel(int x, int y, byte r, byte g, byte b)
	{		
		int index = (x + y * image.getWidth()) * 3;	
		
		comps[index	+ 0] = b;
		comps[index + 1] = g;
		comps[index + 2] = r;
	}
	
	public Bitmap clone()
	{
		return new Bitmap(image);
	}
}