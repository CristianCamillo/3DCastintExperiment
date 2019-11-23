package graphics;

import math.Vector3f;

public class Camera
{
    public final Vector3f position;    
    private float pitch;
    private float yaw;
    
    private float FOV;
    
    public Camera()
    {
    	this(new Vector3f(0, 0, 0), 0f, 0f, 90f);
    }
    
    public Camera(Vector3f position)
    {
    	this(position, 0f, 0f, 90f);
    }
    
    public Camera(Vector3f position, float pitch, float yaw, float FOV)
    {
        this.position = position;
        this.pitch = pitch;
        this.yaw = yaw;
        this.FOV = FOV;
    }
    
    public float getPitch() { return pitch; }
    public float getYaw()	{ return yaw; }
    public float getFOV()	{ return FOV; }
    
    public void setPosition(float x, float y, float z)
    {
        position.x = x;
        position.y = y;
        position.z = z;
    }
    
    public void movePosition(float offsetX, float offsetY, float offsetZ)
    {   
        if(offsetZ != 0)
        {
        	float yRot = (float)Math.toRadians(yaw);
        	float yRotCos = (float)Math.cos(yRot);
        	float yRotSin = (float)Math.sin(yRot);
        	
            position.x -= yRotSin * offsetZ;
            position.z += yRotCos * offsetZ;
            position.y += (float)Math.toRadians(pitch) * offsetZ;
        }
        
        if(offsetX != 0)
        {
        	float yRot = (float)Math.toRadians(yaw - 90);
        	float yRotCos = (float)Math.cos(yRot);
        	float yRotSin = (float)Math.sin(yRot);
        	
        	position.x -= yRotSin * offsetX;
            position.z += yRotCos * offsetX;
        }
        
        position.y += offsetY;
    }
    
    public void setRotation(float deltaPitch, float deltaYaw)
    {
    	pitch = deltaPitch;
        yaw = deltaYaw;
        
        constrainPitch();
    }

    public void moveRotation(float deltaPitch, float deltaYaw)
    {
        pitch += deltaPitch;
        yaw += deltaYaw;
        
        constrainPitch();
    }
    
    public void setFOV(float FOV)
    {
    	this.FOV = FOV;
    	
    	constrainFOV();    	
    }
    
    private void constrainPitch()
    {
    	if(pitch > 90)
        	pitch = 90;
        else if(pitch < -90)
        	pitch = -90;
    }
    
    private void constrainFOV()
    {
    	FOV = FOV < 45f ? 45f : FOV;
		FOV = FOV > 135f ? 135f : FOV;
    }
}