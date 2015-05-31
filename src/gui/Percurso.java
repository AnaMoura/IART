package gui;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import projecto.*;


public class Percurso extends JFrame implements MouseListener{

	private static final long serialVersionUID = 7211727538677803065L;

	private JPanel jpall;

	public static Container cont;
	private JPanel objetos;
	private JButton fechar, novoPercurso;
	private double width, height;
	private Image caixa, robot, exit;
	private JLabel distanciaLabel;
	private ArrayList<Double> distanciaPercorrida;
	private double totalDist;
	JFrame frame;

	public Percurso(final int robotX, final int robotY, final int robotCap, final ArrayList<Coord<Integer, Integer>> caixas, final ArrayList<Wall<Coord<Integer, Integer>, Coord<Integer, Integer>>> paredes, final int saidaX, final int saidaY) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				frame = new JFrame("Percurso");
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				width = screenSize.getWidth(); //largura
				height = screenSize.getHeight(); //altura

				int largura = (int) (width*1356)/1920;
				int altura  = (int) (height*928)/1080;

				JPanel jfloor = new AnimationPane(robotX, robotY, robotCap, caixas, paredes, saidaX, saidaY);
				jfloor.setPreferredSize(new Dimension(largura,altura));
				jfloor.setBackground(Color.BLACK);

				frame.getContentPane().setLayout(new GridLayout(0,1));

				cont = frame.getContentPane();
				cont.setLayout(new BoxLayout(cont,BoxLayout.Y_AXIS));

				cont.add(jfloor);

				objetos = new JPanel();
				objetos.setLayout(new BoxLayout(objetos,BoxLayout.X_AXIS));

				jpall = new JPanel();
				initDist();

				NovoPercurso();
				Fechar();

				frame.add(jfloor);

				objetos.add(jpall);
				cont.add(objetos);

				robot = Toolkit.getDefaultToolkit().getImage("imagens/robot.png");
				caixa = Toolkit.getDefaultToolkit().getImage("imagens/caixa.png");
				exit = Toolkit.getDefaultToolkit().getImage("imagens/exit.png");

				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setResizable(false);
				frame.setVisible(true);
			}

			/**
			 * Cria o botao de para voltar a realizar outro percurso
			 */
			private void NovoPercurso() {

				novoPercurso = new JButton("Novo Percurso");
				jpall.add(novoPercurso);

				novoPercurso.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e ){

						Object[] options = { "Sim", "Não", "Cancelar" };
						int n = JOptionPane.showOptionDialog(null,
								"Deseja fazer um novo percurso? ",
								"Novo Percurso", JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options,
								options[2]);

						if (n == 0) {
							frame.setVisible(false);
						}

					}
				}
						);	
			}

			/**
			 * Cria o botao para avancar para o proximo menu e faz verificacoes
			 */
			private void Fechar() {

				fechar = new JButton("Fechar");
				jpall.add(fechar);

				fechar.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e ){

						Object[] options = { "Sim", "Não", "Cancelar" };
						int n = JOptionPane.showOptionDialog(null,
								"Deseja abandonar a aplicação? ",
								"Fechar", JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options,
								options[2]);

						if (n == 0) {
							System.exit(-1);
						}

					}
				}
						);	
			}

			/**
			 * Inicializa os labels
			 */
			private void initDist() {
				distanciaLabel = new JLabel("Distancia Percorrida: " + totalDist);

				jpall.add(distanciaLabel);

			}

		});
	}

	public class AnimationPane extends JPanel {

		private static final long serialVersionUID = 1L;
		private BufferedImage robot, caixa, exit;
		private int robX, robY, robotCap;
		private ArrayList<Coord<Integer, Integer>> caixas;
		private ArrayList<Wall<Coord<Integer, Integer>, Coord<Integer, Integer>>> paredes;
		private ArrayList<Coord<Integer, Integer>> percurso;
		private int saidaX, saidaY;
		private int counter, path1x, path1y;
		private boolean stopped;

		public AnimationPane(int robotX, int robotY, int robotCap, ArrayList<Coord<Integer, Integer>> caixas, ArrayList<Wall<Coord<Integer, Integer>, Coord<Integer, Integer>>> paredes, int saidaX, int saidaY) {
			try {

				this.robX = robotX;
				this.robY = robotY;
				this.robotCap = robotCap;
				this.caixas = caixas;
				this.paredes = paredes;
				this.saidaX = saidaX;
				this.saidaY = saidaY;
				counter = 0;
				stopped = false;

				robot = ImageIO.read(new File("imagens/robot.png"));
				caixa = ImageIO.read(new File("imagens/caixa.png"));
				exit = ImageIO.read(new File("imagens/exit.png"));

				calcula();

				Timer timer = new Timer(2000, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						
						distanciaLabel.setText(String.valueOf("Distancia Percorrida: " + totalDist));
						
						if (counter < percurso.size()) {
							robX = percurso.get(counter).getX();
							robY = percurso.get(counter).getY();

							if (counter != percurso.size()-1) {
								path1x = percurso.get(counter+1).getX();
								path1y = percurso.get(counter+1).getY();
							} 

							repaint();
							counter++;    
						}

						else {
							stopped = true;
							repaint();
						}
		
					}

				});
				timer.setRepeats(true);
				timer.setCoalesce(true);
				timer.start();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		/**
		 * Funcao que passa o valor das variaveis as funcoes de logica para calcularem o percurso
		 */
		private void calcula() {
			percurso = new ArrayList<Coord<Integer, Integer>>();

			ArvoreCaminhos caminho = new ArvoreCaminhos(robX, robY, robotCap, caixas, paredes, saidaX, saidaY);
			MyNode node = new MyNode();
			List<Integer> list;
			caminho.setNodes();
			node = caminho.aStar();
			list = node.getList();
			Coord<Integer, Integer> old = new Coord<Integer, Integer>(0,0);
			
			totalDist = 0;

			for(int i = 0; i < list.size()-1; i++)
			{
				int index = list.get(i);
				if(index == -1)
				{
					percurso.add(new Coord<Integer, Integer>(robX, robY));
					System.out.println("At the start point.");
					old = new Coord<Integer, Integer>(robX,robY);
				}
				else if(index == -2)
				{
					percurso.add(new Coord<Integer, Integer>(saidaX, saidaY));
					System.out.println("At full capacity going to the storage.");
					
					Coord<Integer, Integer> c1 = new Coord<Integer, Integer>(saidaX,saidaY);
					
					totalDist += caminho.lineDistance(old, c1);
					old = new Coord<Integer, Integer>(saidaX,saidaY);
				}
				else
				{
					percurso.add(caixas.get(index));
					System.out.println("Going to box: " + index);
				}
				String key = index + "," + list.get(i+1);
				PathNode n = caminho.getMap().get(key);
				if(n != null)
				{
					List<Coord<Integer, Integer>> vertexList = n.getPath();
					for(int j = 0; j < vertexList.size(); j++)
					{
						System.out.println("Going to coord: " + vertexList.get(j).getX() + "," + vertexList.get(j).getY());
						Coord<Integer, Integer> c1 = new Coord<Integer, Integer>(vertexList.get(j).getX(),vertexList.get(j).getY());
						percurso.add(c1);
						
						totalDist += caminho.lineDistance(old, c1);;
						
						old = c1;
					}
				}
				key = list.get(i+1) + "," + index;
				n = caminho.getMap().get(key);
				if(n != null)
				{
					List<Coord<Integer, Integer>> vertexList = n.getPath();
					for(int j = vertexList.size()-1; j >= 0; j--)
					{
						System.out.println("Going to coord: " + vertexList.get(j).getX() + "," + vertexList.get(j).getY());
						Coord<Integer, Integer> c1 = new Coord<Integer, Integer>(vertexList.get(j).getX(),vertexList.get(j).getY());
						percurso.add(c1);
						
						totalDist += caminho.lineDistance(old, c1);
						
						old = c1;

					}
				}
			}
			percurso.add(new Coord<Integer, Integer>(saidaX, saidaY));
			
			Coord<Integer, Integer> c1 = new Coord<Integer, Integer>(saidaX,saidaY);
			totalDist += caminho.lineDistance(old, c1);
			
			System.out.println("Going to storage.");
			System.out.println("Total Distance:" + totalDist);
			
		}


		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			for (int i = 0; i < caixas.size(); i++) {

				int cx, cy;

				cx = caixas.get(i).getX();
				cy = caixas.get(i).getY();

				g.drawImage(caixa, cx-50, cy-50,	100, 100, this);

			}

			for (int i = 0; i < paredes.size(); i++) {

				int cx, cy, dx, dy;

				cx = paredes.get(i).getX().getX();
				cy = paredes.get(i).getX().getY();
				dx = paredes.get(i).getY().getX();
				dy = paredes.get(i).getY().getY();

				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(Color.WHITE);
				g2.setStroke(new BasicStroke(5));
				g2.draw(new Line2D.Float(cx, cy, dx, dy));

			}

			g.drawImage(exit, saidaX-50, saidaY-50,	100, 100, this);   

			if (path1x != 0 && path1y != 0 && !stopped && path1x != robX) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(Color.GREEN);
				g2.setStroke(new BasicStroke(7));
				g2.draw(new Line2D.Float(path1x, path1y, robX, robY));
			}
			
			g.drawImage(robot, robX-37, robY-49,	100, 100, this);

		}

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}