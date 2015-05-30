package projecto;

import java.util.ArrayList;
import java.util.List;

public class PathNode {
	
	double g = 0;
	double h = 0;
	List<Coord<Integer, Integer>> path;

	public double function()
	{
		return g+h;
	}
	
	public void setG(double i)
	{
		g = i;
	}
	
	public void setH(double i)
	{
		h = i;
	}
	
	public void setPath(List<Coord<Integer, Integer>> newPath)
	{
		path = new ArrayList<Coord<Integer, Integer>>(newPath);
	}
	
	public void addCoord(Coord<Integer, Integer> c)
	{
		path.add(c);
	}
	
	public double getG()
	{
		return g;
	}
	
	public double getH()
	{
		return h;
	}
	
	public Coord<Integer, Integer> getCoord()
	{
		return path.get(path.size()-1);
	}
	
	public List<Coord<Integer, Integer>> getPath()
	{
		return path;
	}
}


