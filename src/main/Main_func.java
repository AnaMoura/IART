package main;

import gui.MenuInicial;

import java.io.IOException;
import java.util.List;

import projecto.ArvoreCaminhos;
import projecto.MyNode;

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
    	ArvoreCaminhos caminho = new ArvoreCaminhos();
    	MyNode node = new MyNode();
    	List<Integer> list;
    	caminho.init();
    	caminho.setNodes();
    	node = caminho.aStar();
    	list = node.getList();
    	
    	 for(int i = 0; i < list.size(); i++) {
             System.out.println(list.get(i));
         }
    	 
    }




}