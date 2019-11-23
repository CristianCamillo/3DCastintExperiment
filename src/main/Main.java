package main;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

import engine.SimpleEngine;
import graphics.Camera;
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
		
	private Camera camera;
	
	private float pixelWidth;
	private float pixelHeight;
	private float gridDist;
	
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
		camera = new Camera(new Vector3f(0, 0, 0), 0f, 0f, 90f);
		
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
					
					pixelWidth = 2f / getWidth();
					pixelHeight = 2f / getHeight();
				}
				
				
				float elapsedTime = getElapsedTime();
				
				
				float offsetX = 0f;
				float offsetY = 0f;
				float offsetZ = 0f;
				
				if(key(KeyEvent.VK_W))
					offsetZ = - elapsedTime;
				if(key(KeyEvent.VK_S))
					offsetZ = elapsedTime;
				
				if(key(KeyEvent.VK_A))
					offsetX = elapsedTime;
				if(key(KeyEvent.VK_D))
					offsetX = - elapsedTime;
				
				if(key(KeyEvent.VK_E))
					offsetY = - elapsedTime;
				if(key(KeyEvent.VK_X))
					offsetY = elapsedTime;
				
				camera.movePosition(offsetX, offsetY, offsetZ);
								
				
				float deltaPitch = 0f;
				float deltaYaw = 0f;
				
				if(key(KeyEvent.VK_UP))
					deltaPitch = elapsedTime;
				if(key(KeyEvent.VK_DOWN))
					deltaPitch = - elapsedTime;
				
				if(key(KeyEvent.VK_LEFT))
					deltaYaw = - elapsedTime;
				if(key(KeyEvent.VK_RIGHT))
					deltaYaw = elapsedTime;
				
				camera.moveRotation(deltaPitch * 50, deltaYaw * 50);
				
				
				if(key(KeyEvent.VK_F))
					camera.setFOV(camera.getFOV() + elapsedTime * 100);
				if(key(KeyEvent.VK_G))
					camera.setFOV(camera.getFOV() - elapsedTime * 100);

				gridDist = (float)Math.tan((Math.PI - Math.toRadians(camera.getFOV())) / 2);
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
					
				float xYaw   = (float)Math.sin(Math.toRadians(camera.getYaw()));
				
				float yPitch = (float)Math.sin(Math.toRadians(camera.getPitch()));
				
				float zPitch = (float)Math.cos(Math.toRadians(camera.getPitch()));
				float zYaw   = (float)Math.cos(Math.toRadians(camera.getYaw()));
				
				float xPos;
				float yPos = 1f - pixelHeight / 2f;			
				float zPos = gridDist * zPitch * zYaw;
				
				for(int y = 0; y < getHeight(); y++)
				{					
					xPos = -1f + pixelWidth / 2f;
					
					for(int x = 0; x < getWidth(); x++)
					{												
						Vector3f pixelPos = new Vector3f(xPos + xYaw * gridDist, yPos + yPitch * gridDist, zPos);					
						Vector3f cameraRay = pixelPos.sub(camera.position); // ray starting from the camera position and directed towards the pixel
						
						for(int i = 0; i < tris.length; i++)
						{	
							Vector3f triNorm = triNormals.get(i);
							if(triNorm.dot(cameraRay) < 0f) // skips triangles not facing the camera
							{
								Vector3f p = MathUtils.interPlane(tris[i].a, triNorm, camera.position, cameraRay.mul(10000f));
								
								float pCamera = p.sub(camera.position).lenght();
								if(pCamera < cameraRay.lenght()) // skip points between the camera and the grid
									continue;
								
								Vector3f a = tris[i].a;
								Vector3f b = tris[i].b;
								Vector3f c = tris[i].c;
								
								Vector3f u = b.sub(a);
								Vector3f v = c.sub(a);
								Vector3f w = p.sub(a);
								
								float uu = u.dot(u);
								float vv = v.dot(v);
								float uv = u.dot(v);
								float wu = w.dot(u);
								float wv = w.dot(v);
								
								float s = (uv * wv - vv * wu) / (uv * uv - uu * vv);
								float t = (uv * wu - uu * wv) / (uv * uv - uu * vv);
								
								if(s >= 0 && t >= 0 && s + t <= 1)
									if(pCamera < depthBuffer[x + y * getWidth()])
									{
										Color color = colors[i];
										depthBuffer[x + y * getWidth()] = pCamera;										
										getFrameBuffer().drawPixel(x, y, (byte)color.getRed(), (byte)color.getGreen(), (byte)color.getBlue());
									}
							}
						}
						
						xPos += pixelWidth;
					}
					
					yPos -= pixelHeight;
				}
			}
		};
		
		depthBuffer = new float[se.getWidth() * se.getHeight()];
		
		pixelWidth = 2f / se.getWidth();
		pixelHeight = 2f / se.getHeight();
		gridDist = (float)Math.tan((Math.PI - Math.toRadians(camera.getFOV())) / 2);
		
		se.start();
	}
}
