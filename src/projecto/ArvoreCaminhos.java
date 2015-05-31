package projecto;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;


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
	int capacity = 4;
	HashMap<String, PathNode> distances = new HashMap<String, PathNode>();


	public void inserirDados ()
	{
		//-.-

	}
	public void init()
	{
		walls = new ArrayList<Wall<Coord<Integer, Integer>, Coord<Integer, Integer>>>();
		boxes = new ArrayList<Coord<Integer, Integer>>();

		Coord<Integer, Integer> c1 = new Coord<Integer, Integer>(5,15);
		Coord<Integer, Integer> c2 = new Coord<Integer, Integer>(15,15);

		Wall<Coord<Integer, Integer>, Coord<Integer, Integer>> Wall = 
				new Wall<Coord<Integer, Integer>, Coord<Integer, Integer>>(c1,c2);
		walls.add(Wall);
		c1 = new Coord<Integer, Integer>(15,15);
		c2 = new Coord<Integer, Integer>(15,40);
		Wall = new Wall<Coord<Integer, Integer>, Coord<Integer, Integer>>(c1,c2);
		walls.add(Wall);

		c1 = new Coord<Integer, Integer>(20,20);
		boxes.add(c1);
		c1 = new Coord<Integer, Integer>(10,10);
		boxes.add(c1);
		c1 = new Coord<Integer, Integer>(20,30);
		boxes.add(c1);
		c1 = new Coord<Integer, Integer>(30,30);
		boxes.add(c1);
		c1 = new Coord<Integer, Integer>(10,20);
		boxes.add(c1);
		c1 = new Coord<Integer, Integer>(10,10);
		boxes.add(c1);
		//		c1 = new Coord<Integer, Integer>(50,70);
		//		boxes.add(c1);

		startPoint = new Coord<Integer, Integer>(0,0);	
		storage = new Coord<Integer, Integer>(50,50);
	}

	public PathNode realDistance(int index1, int index2)
	{
		PathNode node = null;

		String key = index1 + "," + index2;
		node = distances.get(key);
		if(node != null)
			return node;
		key = index2 + "," + index1;
		node = distances.get(key);
		if(node != null)
			return node;

		Coord<Integer, Integer> coord1, coord2;
		if(index1 == -1)
			coord1 = startPoint;
		else if(index1 == -2)
			coord1 = storage;
		else
			coord1 = boxes.get(index1);
		if(index2 == -1)
			coord2 = startPoint;
		else if(index2 == -2)
			coord2 = storage;
		else
			coord2 = boxes.get(index2);

		List<Wall<Coord<Integer, Integer>, Coord<Integer, Integer>>> intersectedWalls = intersect(coord1, coord2);

		if(intersectedWalls.size() == 0)
		{
			int ax = coord1.getX();
			int ay = coord1.getY();
			int bx = coord2.getX();
			int by = coord2.getY();

			double g = Math.sqrt((bx-ax)*(bx-ax)+(by-ay)*(by-ay));
			node = new PathNode();
			node.setG(g);
			distances.put(key, node);
		}
		else
		{
			node = aStar(coord1, coord2, intersectedWalls);
			distances.put(key, node);
		}
		return node;
	}

	private PathNode aStar(Coord<Integer, Integer> a, Coord<Integer, Integer> b,
			List<Wall<Coord<Integer, Integer>, Coord<Integer, Integer>>> intersectedWalls)
	{
		Comparator<PathNode> coordComparator = new CoordComparator();
		PriorityQueue<PathNode> coordQueue = new PriorityQueue<PathNode>(10, coordComparator);

		for(int i = 0; i < intersectedWalls.size(); i++)
		{
			int x1, x2, y1, y2;
			x1 = intersectedWalls.get(i).getX().getX();
			y1 = intersectedWalls.get(i).getX().getY();
			x2 = intersectedWalls.get(i).getY().getX();
			y2 = intersectedWalls.get(i).getY().getY();

			if(x1 == x2)
			{
				if(y1 > y2)
				{
					y1 += 1;
					y2 -= 1;
				}
				else
				{
					y1 -= 1;
					y2 += 1;
				}
			}
			else if(y1 == y2)
			{
				if(x1 > x2)
				{
					x1 += 1;
					x2 -= 1;
				}
				else
				{
					x1 -= 1;
					x2 += 1;
				}
			}
			else
			{
				double m, c;
				m = (y2-y1)/(x2-x1);
				c = y1 - x1*m;
				if(x1 > x2)
				{
					x1 += 1;
					y1 = (int) Math.ceil(x1 * m + c);
					x2 -= 1;
					y2 = (int) Math.floor(x2 * m + c);
				}
				else
				{
					x1 -= 1;
					y1 = (int) Math.floor(x1 * m + c);
					x2 += 1;
					y2 = (int) Math.ceil(x2 * m + c);
				}
			}

			Coord<Integer, Integer> coord = new Coord<Integer, Integer>(x1, y1);
			if(intersect(a, coord).size() == 0)
			{
				double g, h;
				PathNode node = new PathNode();
				node.addCoord(coord);
				g = lineDistance(a, coord);
				h = lineDistance(coord, b);
				node.setG(g);
				node.setH(h);
				coordQueue.add(node);
			}
			coord = new Coord<Integer, Integer>(x2, y2);
			if(intersect(a, coord).size() == 0)
			{
				double g, h;
				PathNode node = new PathNode();
				node.addCoord(coord);
				g = lineDistance(a, coord);
				h = lineDistance(coord, b);
				node.setG(g);
				node.setH(h);
				coordQueue.add(node);
			}
		}

		Coord<Integer, Integer> oldCoord;
		PathNode oldNode;
		double oldG;

		while(coordQueue.size() != 0)
		{
			oldNode = coordQueue.poll();
			oldCoord = oldNode.getCoord();	
			oldG = oldNode.getG();

			List<Wall<Coord<Integer, Integer>, Coord<Integer, Integer>>> walls = intersect(oldCoord, b);

			if(walls.size() == 0)
			{
				double g, h;
				PathNode node = new PathNode();
				node.setPath(oldNode.getPath());
				g = lineDistance(oldCoord, b) + oldG;
				h = 0;
				node.setG(g);
				node.setH(h);
				return node;
			}
			else
			{
				for(int i = 0; i < walls.size(); i++)
				{
					int x1, x2, y1, y2;
					x1 = walls.get(i).getX().getX();
					y1 = walls.get(i).getX().getY();
					x2 = walls.get(i).getY().getX();
					y2 = walls.get(i).getY().getY();

					if(x1 == x2)
					{
						if(y1 > y2)
						{
							y1 += 1;
							y2 -= 1;
						}
						else
						{
							y1 -= 1;
							y2 += 1;
						}
					}
					else if(y1 == y2)
					{
						if(x1 > x2)
						{
							x1 += 1;
							x2 -= 1;
						}
						else
						{
							x1 -= 1;
							x2 += 1;
						}
					}
					else
					{
						double m, c;
						m = (y2-y1)/(x2-x1);
						c = y1 - x1*m;
						if(x1 > x2)
						{
							x1 += 1;
							y1 = (int) Math.ceil(x1 * m + c);
							x2 -= 1;
							y2 = (int) Math.floor(x2 * m + c);
						}
						else
						{
							x1 -= 1;
							y1 = (int) Math.floor(x1 * m + c);
							x2 += 1;
							y2 = (int) Math.ceil(x2 * m + c);
						}
					}
					Coord<Integer, Integer> coord = new Coord<Integer, Integer>(x1, y1);
					if(!oldNode.getPath().contains(coord) && intersect(oldCoord, coord).size() == 0)
					{
						double g, h;
						PathNode node = new PathNode();
						node.setPath(oldNode.getPath());
						node.addCoord(coord);
						g = lineDistance(oldCoord, coord) + oldG;
						h = lineDistance(coord, b);
						node.setG(g);
						node.setH(h);
						coordQueue.add(node);
					}
					coord = new Coord<Integer, Integer>(x2, y2);
					if(!oldNode.getPath().contains(coord) && intersect(oldCoord, coord).size() == 0)
					{
						double g, h;
						PathNode node = new PathNode();
						node.setPath(oldNode.getPath());
						node.addCoord(coord);
						g = lineDistance(oldCoord, coord) + oldG;
						h = lineDistance(coord, b);
						node.setG(g);
						node.setH(h);
						coordQueue.add(node);
					}
				}
			}
		}
		return null;
	}

	public double lineDistance(Coord<Integer, Integer> a, Coord<Integer, Integer> b)
	{
		int ax = a.getX();
		int ay = a.getY();
		int bx = b.getX();
		int by = b.getY();

		double distance = Math.sqrt((bx-ax)*(bx-ax)+(by-ay)*(by-ay));
		return distance;
	}

	public double[] lineDistance(Wall<Coord<Integer,Integer>,Coord<Integer,Integer>> a, Coord<Integer, Integer> b)
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

	/*
	 * Funcao que calcula se entre dois pontos se encontra uma parede
	 * @param a
	 *            Coord<Integer,Integer> coordenadas do primeiro ponto
	 * @param b
	 *            Coord<Integer,Integer> coordenadas do segundo ponto
	 */
	public List<Wall<Coord<Integer, Integer>, Coord<Integer, Integer>>> intersect(Coord<Integer,Integer> a,Coord<Integer,Integer> b)
	{
		int ax = a.getX();
		int ay = a.getY();
		int bx = b.getX();
		int by = b.getY();
		float cx, cy, dx, dy;

		List<Wall<Coord<Integer, Integer>, Coord<Integer, Integer>>> intersectedWalls = 
				new ArrayList<Wall<Coord<Integer, Integer>, Coord<Integer, Integer>>>();

		for (int i = 0; i < walls.size(); i++) {

			cx = walls.get(i).getX().getX();
			cy = walls.get(i).getX().getY();
			dx = walls.get(i).getY().getX();
			dy = walls.get(i).getY().getY();

			Line2D line1 = new Line2D.Float(ax, ay, bx, by);
			Line2D line2 = new Line2D.Float(cx, cy, dx, dy);
			boolean result = line2.intersectsLine(line1);

			if (result) {
				intersectedWalls.add(walls.get(i));
			}
		}
		return intersectedWalls;
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
			double g = lineDistance(boxes.get(i), startPoint);

			node.setG(g);
			//-------------------Como fazer set da heuristica eu nao sei gg ---
			double h =0;
			node.setH(h);
			//-------------------
			node.addBoxIndex(-1);
			node.addBoxIndex(i);
			queue.add(node);
		}
		return queue;
	}


	/*
	 * Função para verificar se todas as caixas foram apanhas e posição final é a do armazém
	 * Parametros
	 * 			 List<Integer> list Lista de um nó.
	 * Retorno: 
	 * 			true, se número de caixas existentes na lista = número caixas existentes e ultimo indice = armazém
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

	public boolean maxCapacity(MyNode node)
	{
		boolean wentStorage = false;
		for(int i = node.getList().size()-1 ; i >= node.getList().size()- capacity; i--)
		{
			if(node.getList().get(i) == -2)
			{
				wentStorage = true;
				break;
			}
			if (i==0)
			{
				wentStorage = true;
				break;
			}
		}
		if (wentStorage)
			return false;
		else
			return true;
	}

	//-------------------------------------get Distance modificar para a distancia já previamente calculada
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
			int lastElement = oldList.get(oldList.size()-1);

			if (maxCapacity(oldNode))
			{
				double g = 0;
				double h = 0;
				MyNode node = new MyNode();

				g = realDistance(lastElement, -2).getG() + oldG;
				node.setG(g);

				node.setList(oldList);
				node.addBoxIndex(-2);
				if (allCatched(node.getList()))
					return node;

				for(int j = 0; j < boxes.size(); j++)
				{
					if(!node.getList().contains(j))
					{
						double d = realDistance(-2, j).getG() + realDistance(j, -2).getG();
						if(d > h)
							h = d;
					}
				}
				node.setH(h);

				queue.add(node);
			}

			else 
			{
				for(int i=0; i < boxes.size(); i++ )
				{
					if (!oldList.contains(i))
					{
						double g = 0;
						double h =0;

						MyNode node = new MyNode();
						g = realDistance(lastElement, i).getG() + oldG; 
						node.setG(g);

						node.setList(oldList);
						node.addBoxIndex(i);

						for(int j = 0; j < boxes.size(); j++)
						{
							if(!node.getList().contains(j))
							{
								double d = realDistance(i, j).getG() + realDistance(j, -2).getG();
								if(d > h)
									h = d;
							}
						}
						node.setH(h);

						queue.add(node);
					}
				}

				//Adicionar a opção ir ao armazém excepto se ultimo elemento da lista for armazém, -2
				if(lastElement != -2)
				{
					double g = 0;
					double h = 0;
					MyNode node = new MyNode();
					g = realDistance(lastElement, -2).getG() + oldG;
					node.setG(g);

					node.setList(oldList);
					node.addBoxIndex(-2);
					if (allCatched(node.getList()))
						return node;

					for(int j = 0; j < boxes.size(); j++)
					{
						if(!node.getList().contains(j))
						{
							double d = realDistance(-2, j).getG() + realDistance(j, -2).getG();
							if(d > h)
								h = d;
						}
					}
					node.setH(h);

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
		public int compare(MyNode x, MyNode y)
		{
			if (x.function() < y.function())
				return -1;
			if (x.function() > y.function())
				return 1;
			return 0;
		}
	}


	/*
	 * Override do Comparator para a priority queue de coordenadas, é comparado a function do node.
	 */
	public class CoordComparator implements Comparator<PathNode>
	{
		@Override
		public int compare(PathNode x, PathNode y)
		{
			if (x.function() < y.function())
				return -1;
			if (x.function() > y.function())
				return 1;
			return 0;
		}
	}


	public HashMap<String, PathNode> getMap()
	{
		return distances;
	}

}
