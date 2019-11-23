package math;

public class Plane
{
	public final Vector3f point;
	public final Vector3f normal;
	
	public Plane(Vector3f point, Vector3f normal)
	{
		this.point = point;
		this.normal = normal;
	}
	
	public Vector3f intersectSegment(Vector3f segStart, Vector3f segEnd)
	{
		float planeD = - normal.dot(point);
		float ad = segStart.dot(normal);
		float bd = segEnd.dot(normal);
		float t = (- planeD - ad) / (bd - ad);
		Vector3f segStartToEnd = segEnd.sub(segStart);
		Vector3f segToIntersect = segStartToEnd.mul(t);
		
		return segStart.add(segToIntersect);
	}
}
