package math;

public class Vector3f implements Cloneable
{
	public float x;
	public float y;
	public float z;
	
	public Vector3f() {}
	
	public Vector3f(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3f add(float v) 	{ return new Vector3f(x + v, y + v, z + v); }
	public Vector3f add(Vector3f v) { return new Vector3f(x + v.x, y + v.y, z + v.z); }
	public Vector3f sub(float v) 	{ return new Vector3f(x - v, y - v, z - v); }
	public Vector3f sub(Vector3f v) { return new Vector3f(x - v.x, y - v.y, z - v.z); }
	public Vector3f mul(float v) 	{ return new Vector3f(x * v, y * v, z * v); }
	public Vector3f mul(Vector3f v) { return new Vector3f(x * v.x, y * v.y, z * v.z); }
	public Vector3f div(float v) 	{ return new Vector3f(x / v, y / v, z / v); }
	public Vector3f div(Vector3f v) { return new Vector3f(x / v.x, y / v.y, z / v.z); }
	
	public float lenght() 		 { return (float)Math.sqrt(x * x + y * y + z * z); }
	public float dot(Vector3f v) { return x * v.x + y * v.y + z * v.z; }
	
	public Vector3f cross(Vector3f v)
	{
		float x = this.y * v.z - this.z * v.y;
		float y = this.z * v.x - this.x * v.z;
		float z = this.x * v.y - this.y * v.x;
		
		return new Vector3f(x, y, z);
	}
	
	public Vector3f normalize()
	{
		float lenght = lenght();
		
		Vector3f r = new Vector3f(x, y, z);
		
		r.x /= lenght;
		r.y /= lenght;
		r.z /= lenght;
		
		return r;
	}
	
	/*public Vector3f mulMat(Matrix4f m)
	{
		return new Vector3f
		(
			x * m.m[0][0] + y * m.m[1][0] + z * m.m[2][0] + w * m.m[3][0],
			x * m.m[0][1] + y * m.m[1][1] + z * m.m[2][1] + w * m.m[3][1],
			x * m.m[0][2] + y * m.m[1][2] + z * m.m[2][2] + w * m.m[3][2],
			x * m.m[0][3] + y * m.m[1][3] + z * m.m[2][3] + w * m.m[3][3]
		);
	}*/
	
	public String toString() 		  { return "(" + x + ", " + y + ", " + z + ")"; }
	public boolean equals(Vector3f v) { return x == v.x && y == v.y && z == v.z; }
	public Vector3f clone()			  { return new Vector3f(x, y, z); }
}
