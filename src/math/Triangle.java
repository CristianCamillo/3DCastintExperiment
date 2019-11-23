package math;

public class Triangle
{
	public Vector3f a;
	public Vector3f b;
	public Vector3f c;
	
	public Triangle(Vector3f a, Vector3f b, Vector3f c)
	{
		this.a = a;
		this.b = b;
		this.c = c;
	}	

	public void orderByHeight()
	{
		if(b.y < c.y)
			c = getItself(b, b = c);
		
		if(a.y < c.y)
			c = getItself(a, a = c);
		
		if(a.y < b.y)
			b = getItself(a, a = b);
	}
	
	private Vector3f getItself(Vector3f itself, Vector3f dummy) { return itself; }
}
