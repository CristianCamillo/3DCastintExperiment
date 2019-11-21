package math;

public class MathUtils
{	
	public static float distance(Vector3f p, Vector3f normal, Vector3f planeP)
	{
		return normal.dot(p) - normal.dot(planeP);
	}
	
	public static Vector3f interPlane(Vector3f planeP, Vector3f planeN, Vector3f lineStart, Vector3f lineEnd, float[] t)
	{
		float planeD = - planeN.dot(planeP);
		float ad = lineStart.dot(planeN);
		float bd = lineEnd.dot(planeN);
		t[0] = (- planeD - ad) / (bd - ad);
		Vector3f lineStartToEnd = lineEnd.sub(lineStart);
		Vector3f lineToIntersect = lineStartToEnd.mul(t[0]);
		
		return lineStart.add(lineToIntersect);
	}
}
