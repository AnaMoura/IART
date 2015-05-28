package projecto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;



public class ArvoreCaminhos 
{
	List<Parede<Coord<Integer, Integer>, Coord<Integer, Integer>>> paredes;
	List<Coord<Integer, Integer>> caixas;
	Coord<Integer, Integer> robot;

	public void inserirDados ()
	{
		//-.-

	}
	public void inicializar()
	{
		paredes = new ArrayList<Parede<Coord<Integer, Integer>, Coord<Integer, Integer>>>();
		caixas = new ArrayList<Coord<Integer, Integer>>();

		Coord<Integer, Integer> c1 = new Coord<Integer, Integer>(15,0);
		Coord<Integer, Integer> c2 = new Coord<Integer, Integer>(15,10);
		Parede<Coord<Integer, Integer>, Coord<Integer, Integer>> parede = 
				new Parede<Coord<Integer, Integer>, Coord<Integer, Integer>>(c1,c2);
		paredes.add(parede);
		c1 = new Coord<Integer, Integer>(17,5);
		c2 = new Coord<Integer, Integer>(25,5);
		parede = new Parede<Coord<Integer, Integer>, Coord<Integer, Integer>>(c1,c2);
		paredes.add(parede);

		c1 = new Coord<Integer, Integer>(10,0);
		caixas.add(c1);
		c1 = new Coord<Integer, Integer>(20,10);
		caixas.add(c1);
		c1 = new Coord<Integer, Integer>(20,0);
		caixas.add(c1);

		robot = new Coord<Integer, Integer>(0,0);	
	}

	public double calcularDistancia(Coord<Integer, Integer> a, Coord<Integer, Integer> b)
	{
		int ax = a.getX();
		int ay = a.getY();
		int bx = b.getX();
		int by = b.getY();

		double dab = Math.sqrt((bx-ax)*(bx-ax)+(by-ay)*(by-ay));
		return dab;

	}

	public double[] calcularDistancia(Parede<Coord<Integer,Integer>,Coord<Integer,Integer>> a, Coord<Integer, Integer> b)
	{
		int v1x = a.getX().getX();
		int v1y = a.getX().getY();
		int v2x = a.getY().getX();
		int v2y = a.getY().getY();
		int bx = b.getX();
		int by = b.getY();

		double dav1 = Math.sqrt((bx-v1x)*(bx-v1x)+(by-v1y)*(by-v1y));
		double dav2 = Math.sqrt((bx-v2x)*(bx-v2x)+(by-v2y)*(by-v2y));
		double[] ret = new double[2];
		ret[0] = dav1;
		ret[1] = dav2;
		return ret;

	}

	public void intersecao(Coord<Integer,Integer> a,Coord<Integer,Integer> b)
	{
		int ax = a.getX();
		int ay = a.getY();
		int bx = b.getX();
		int by = b.getY();
		 
		
	}
	
	public void aStar()
	{
		   Comparator<MyNode> comparator = new MyComparator();
	       PriorityQueue<MyNode> queue = new PriorityQueue<MyNode>(10, comparator);
	}
	
	
	public class MyComparator implements Comparator<MyNode>
	{
	    @Override
	    public int compare(MyNode x,MyNode y)
	    {
	        // Comparação da função para priorityQueue
	        if (x.function() < y.function())
	        {
	            return -1;
	        }
	        if (x.function() > y.function())
	        {
	            return 1;
	        }
	        return 0;
	    }

	}


}
