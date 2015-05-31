package main;

import gui.MenuInicial;

import java.io.IOException;
import java.util.List;

import projecto.ArvoreCaminhos;
import projecto.Coord;
import projecto.MyNode;
import projecto.PathNode;

/**
 * Main_func.java - Classe main do programa
 * 
 * @author - GA1_2
 * @version 1.0
 */
public class Main_func {
	/**
	 * funcao main do programa
	 * chama a funcao MenuInicial
	 * @param args null
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException  {
//		ArvoreCaminhos caminho = new ArvoreCaminhos();
//		MyNode node = new MyNode();
//		List<Integer> list;
//		caminho.init();
//		caminho.setNodes();
//		node = caminho.aStar();
//		list = node.getList();
//
//		System.out.println("At the start point.");
//		for(int i = 1; i < list.size()-1; i++)
//		{
//			if(list.get(i) != -2)
//				System.out.println("Going to box: " + list.get(i));
//			else
//				System.out.println("At full capacity going to the storage.");
//			String key = list.get(i) + "," + list.get(i+1);
//			PathNode n = caminho.getMap().get(key);
//			if(n != null)
//			{
//				List<Coord<Integer, Integer>> vertexList = n.getPath();
//				for(int j = 0; j < vertexList.size(); j++)
//				{
//					System.out.println("Going to coord: " + vertexList.get(j).getX() + "," + vertexList.get(j).getY());
//				}
//			}
//			key = list.get(i+1) + "," + list.get(i);
//			n = caminho.getMap().get(key);
//			if(n != null)
//			{
//				List<Coord<Integer, Integer>> vertexList = n.getPath();
//				for(int j = vertexList.size()-1; j >= 0; j--)
//				{
//					System.out.println("Going to coord: " + vertexList.get(j).getX() + "," + vertexList.get(j).getY());
//				}
//			}
//		}
//		System.out.println("Going to storage.");
		new MenuInicial();
	}




}