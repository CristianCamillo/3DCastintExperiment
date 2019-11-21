package math;

public class Matrix4f
{
	public final float[][] m = new float[4][4];
	
	public Matrix4f() {}
	
	public void setIdentityMat()
	{
		float[][] m = new float[4][4];
		
		m[0][0] = 1.0f;
		m[1][1] = 1.0f;
		m[2][2] = 1.0f;
		m[3][3] = 1.0f;
	}
}
