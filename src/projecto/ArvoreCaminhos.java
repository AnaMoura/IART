package projecto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;


/* -1 é Start Point
 * -2 é Armazem
 */
public class ArvoreCaminhos 
{
	List<Wall<Coord<Integer, Integer>, Coord<Integer, Integer>>> walls;
	List<Coord<Integer, Integer>> boxes;
	Coord<Integer, Integer> startPoint;
	Coord<Integer, Integer> storage;
	Comparator<MyNode> comparator = new MyComparator();
	PriorityQueue<MyNode> queue = new PriorityQueue<MyNode>(10, comparator);


	public void inserirDados ()
	{
		//-.-

	}
	public void init()
	{
		walls = new ArrayList<Wall<Coord<Integer, Integer>, Coord<Integer, Integer>>>();
		boxes = new ArrayList<Coord<Integer, Integer>>();

		Coord<Integer, Integer> c1 = new Coord<Integer, Integer>(15,0);
		Coord<Integer, Integer> c2 = new Coord<Integer, Integer>(15,10);
		Wall<Coord<Integer, Integer>, Coord<Integer, Integer>> Wall = 
				new Wall<Coord<Integer, Integer>, Coord<Integer, Integer>>(c1,c2);
		walls.add(Wall);
		c1 = new Coord<Integer, Integer>(17,5);
		c2 = new Coord<Integer, Integer>(25,5);
		Wall = new Wall<Coord<Integer, Integer>, Coord<Integer, Integer>>(c1,c2);
		walls.add(Wall);

		c1 = new Coord<Integer, Integer>(10,0);
		boxes.add(c1);
		c1 = new Coord<Integer, Integer>(20,10);
		boxes.add(c1);
		c1 = new Coord<Integer, Integer>(20,0);
		boxes.add(c1);

		startPoint = new Coord<Integer, Integer>(0,0);	
	}

	public double getDistance(Coord<Integer, Integer> a, Coord<Integer, Integer> b)
	{
		int ax = a.getX();
		int ay = a.getY();
		int bx = b.getX();
		int by = b.getY();

		double dab = Math.sqrt((bx-ax)*(bx-ax)+(by-ay)*(by-ay));
		return dab;
	}

	public double[] getDistance(Wall<Coord<Integer,Integer>,Coord<Integer,Integer>> a, Coord<Integer, Integer> b)
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

	/*
	 * Inicialização da priority queue com todas as caixas produzidas para nós
	 * 
	 */
	public  PriorityQueue<MyNode> setNodes()	
	{
		for(int i = 0; i < boxes.size(); i++)
		{
			MyNode node = new MyNode();
			double g = getDistance(boxes.get(i), startPoint);

			node.setG(g);
			//-------------------Como fazer set da heuristica eu nao sei gg ---
			double h =0;
			node.setH(h);
			//-------------------
			node.addBoxIndex(-1);
			queue.add(node);
		}
		return queue;
	}
	
	/*
	 * Função para verificar se todas as caixas foram apanhas e posição final é a do armazém
	 * Parametros: Lista de um nó.
	 * Retorno: true, se número de caixas existentes na lista = número caixas existentes e ultimo indice = armazém
	 * 			false, se não corresponder às condições.
	 */
	public boolean allCatched(List<Integer> list)
	{
		int count = 0;

		for (int i = 0; i < list.size(); i++)
			if (list.get(i)>= 0)
				count++;

		if(count  == boxes.size() && list.get(list.size()-1) ==  -2 )
			return true;
		else 
			return false;
	}

	public MyNode aStar()
	{
		boolean end = false;
		List<Integer> oldList = new ArrayList<Integer>();
		MyNode oldNode;
		double oldG;

		do
		{
			oldNode = queue.poll();
			oldList = oldNode.getList();	
			oldG = oldNode.getG();

			for(int i=0; i < boxes.size(); i++ )
			{
				if (!oldList.contains(i))
				{
					double g = 0;
					double h =0;
					int lastElement = oldList.get(oldList.size()-1);

					MyNode node = new MyNode();
					if(lastElement == -2)
						g = getDistance(storage, boxes.get(i)) + oldG;
					else
						g = getDistance(boxes.get(lastElement), boxes.get(i)) + oldG;	
					node.setG(g);	
					//-------------------Como fazer set da heuristica eu nao sei gg ---
					//
					node.setH(h);
					//
					//-------------------			
					// adicionar a lista antiga
					node.setList(oldList);
					node.addBoxIndex(i);

					if (allCatched(node.getList()))
						return node;				
					queue.add(node);
				}
			}
		}while (!end);

		return null;
	}


	/*
	 * Override do Comparator para a priority queue, é comparado a function do node.
	 */
	public class MyComparator implements Comparator<MyNode>
	{
		@Override
		public int compare(MyNode x,MyNode y)
		{
			if (x.function() < y.function())
				return -1;
			if (x.function() > y.function())
				return 1;
			return 0;
		}
	}

}
