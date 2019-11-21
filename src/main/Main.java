package main;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

import engine.SimpleEngine;
import math.MathUtils;
import math.Triangle;
import math.Vector3f;

public class Main
{
	private int width = 800;
	private int height = 600;
	private String title = "Ray Casting";
	private int FPSCap = 0;
	private boolean showFPS = true;
	private boolean fullscreen = false;
	
	///////////////////////////////////////////////////////
	
	private Vector3f cameraPos;
	private Vector3f cameraDir;
	private float gridDist;
	private float FOV;
	
	private float[] depthBuffer;
	
	private Triangle[] tris = new Triangle[] // points are in clockwise order
	{
		new Triangle(new Vector3f(-3, 0, 10), new Vector3f(0, 4, 10), new Vector3f(3, 0, 10)),
		new Triangle(new Vector3f(-3, 0, 10), new Vector3f(3, 0, 10), new Vector3f(0, -4, 10)),
		new Triangle(new Vector3f(-7, 0, 10), new Vector3f(-7, -10, 10), new Vector3f(-15, 0, 10))
	};
	
	private Color[] colors = new Color[]
	{
		Color.CYAN,
		Color.ORANGE,
		Color.RED
	};
	
	public Main()
	{
		cameraPos = new Vector3f(0, 0, 0);
		cameraDir = new Vector3f(0, 0, 1).normalize();
		gridDist = 1;
		FOV = 90;
		
		float ipot = (float)(gridDist / Math.cos(Math.toRadians(FOV) / 2));
		float gridWidth = (float)Math.sqrt(ipot * ipot - gridDist * gridDist);
		
		/*float ipot = (float)(gridWidth / 2 / Math.cos((Math.PI - Math.toRadians(FOV)) / 2));
		float gridDist = (float)Math.sqrt(ipo * ipo - gridWidth * gridWidth / 4);*/
		
		ArrayList<Vector3f> triNormals = new ArrayList<Vector3f>();		
		
		SimpleEngine se = new SimpleEngine(width, height, title, FPSCap, showFPS, fullscreen)
		{
			public void update()
			{
				if(key(KeyEvent.VK_ESCAPE))
					stop();
				
				if(keyToggle(KeyEvent.VK_F3))
				{
					setSize(width, height, !isFullscreen());
					depthBuffer = new float[getWidth() * getHeight()];
				}
				
				if(key(KeyEvent.VK_W))
					cameraPos.z += getElapsedTime();
				if(key(KeyEvent.VK_S))
					cameraPos.z -= getElapsedTime();
			}
			
			public void render()
			{			
				Arrays.fill(depthBuffer, Float.POSITIVE_INFINITY);
				
				triNormals.clear();
				
				for(Triangle tri : tris)
				{
					Vector3f line1 = tri.b.sub(tri.a);
					Vector3f line2 = tri.c.sub(tri.a);
					triNormals.add(line1.cross(line2).normalize());
				}
				
				float pixelDim = gridWidth / getWidth();
				
				float halfWidth = gridWidth / 2;
				float halfHeight = halfWidth * getHeight() / getWidth();
				
				float xPos;
				float yPos = - halfHeight;
				
				for(int y = 0; y < getHeight(); y++)
				{					
					xPos = - halfWidth;
					for(int x = 0; x < getWidth(); x++)
					{						
						Vector3f pixelPos = new Vector3f(xPos, yPos, cameraPos.z + gridDist);	// TODO: account for camera pos	and rot				
						Vector3f cameraRay = pixelPos.sub(cameraPos); // ray starting from the camera position and directed towards the pixel
						
						for(int i = 0; i < tris.length; i++)
						{	
							Vector3f triNorm = triNormals.get(i);
							if(triNorm.dot(cameraRay) < 0f) // skips triangles not facing the camera
							{
								float[] t = new float[1];
								Vector3f p = MathUtils.interPlane(tris[i].a, triNorm, cameraPos, cameraRay, t);
								
								float pCamera = p.sub(cameraPos).lenght();
								
								if(pCamera < cameraRay.lenght()) // skip points between the camera and the grid
									continue;
								
								Vector3f a = tris[i].a;
								Vector3f b = tris[i].b;
								Vector3f c = tris[i].c;
								
								Vector3f pc = p.sub(c);
								
								float area  = a.sub(b).cross(a.sub(c)).lenght() / 2;
								float alpha = p.sub(b).cross(pc).lenght() / (2 * area);
								float beta  = pc.cross(p.sub(a)).lenght() / (2 * area);
								float gamma = 1 - alpha - beta;
								
								//if(alpha + beta + gamma == 1)
								if(0 <= alpha && alpha <= 1 && 0 <= beta && beta <= 1 && 0 <= gamma && gamma <= 1)
								{									
									if(pCamera < depthBuffer[x + y * getWidth()])
									{
										Color color = colors[i];
										depthBuffer[x + y * getWidth()] = pCamera;										
										getFrameBuffer().drawPixel(x, y, (byte)color.getRed(), (byte)color.getGreen(), (byte)color.getBlue());
									}
								}
							}
						}
						
						xPos += pixelDim;
					}
					
					yPos += pixelDim;
				}
			}
		};
		
		depthBuffer = new float[se.getWidth() * se.getHeight()];
		
		se.start();
	}
}
