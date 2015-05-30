package projecto;

import java.util.ArrayList;
import java.util.List;

public class MyNode {
	
	double g = 0;
	double h = 0;
	List<Integer> list = new ArrayList<Integer>();

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
	
	public void addBoxIndex(int i)
	{
		list.add(i);
	}
	
	public void setList(List<Integer> newList)
	{
		list = newList;
	}
	
	public double getG()
	{
		return g;
	}
	
	public double getH()
	{
		return h;
	}
	
	public List<Integer> getList()
	{
		return list;
	}
}
