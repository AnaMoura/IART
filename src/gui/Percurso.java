package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;

import projecto.Coord;
import projecto.Wall;

/**
 * Percurso.java - Classe responsavel pela representacao do trajecto do robot
 * 
 * @author - GA1_2
 * @version 1.0
 */

public class Percurso extends JFrame implements MouseListener {
	
	private static final long serialVersionUID = 7211727538677803065L;

	private JPanel jpall;
	int robotX, robotY, robotCap;
	private ArrayList<Coord<Integer, Integer>> caixas;
	private ArrayList<Wall<Coord<Integer, Integer>, Coord<Integer, Integer>>> paredes;
	private int saidaX, saidaY;
	
	public static Container cont;
	private JPanel armazem, objetos;
	private JButton fechar, novoPercurso;
	private double width, height;
	private Image caixa, robot, exit;
	private JLabel distanciaLabel;
	private int distanciaPercorrida;
	private boolean solutionFound;
	private ArrayList<Coord<Integer, Integer>> percurso;
	
	private int counter;

	/**
	 * Construtor com os parametros que recebe do menu anterior
	 * 
	 * @param robotX
	 * @param robotY
	 * @param robotCap
	 * @param caixas
	 * @param paredes
	 * @param saidaX
	 * @param saidaY
	 * @throws IOException
	 */
	public Percurso(int robotX, int robotY, int robotCap, ArrayList<Coord<Integer, Integer>> caixas, ArrayList<Wall<Coord<Integer, Integer>, Coord<Integer, Integer>>> paredes, int saidaX, int saidaY) throws IOException {
		super("Percurso");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = screenSize.getWidth(); //largura
		height = screenSize.getHeight(); //altura
		
		int largura = (int) (width*1356)/1920;
		int altura  = (int) (height*928)/1080;
		
		JPanel jfloor = new JPanel();
		jfloor.setPreferredSize(new Dimension(largura,altura));
		jfloor.setBackground(Color.BLACK);

		this.getContentPane().setLayout(new GridLayout(0,1));
		
		cont = this.getContentPane();
		cont.setLayout(new BoxLayout(cont,BoxLayout.Y_AXIS));
		
		cont.add(jfloor);
		
		armazem = new JPanel();
		objetos = new JPanel();
		objetos.setLayout(new BoxLayout(objetos,BoxLayout.X_AXIS));
		
		this.addMouseListener(this);
		solutionFound = false;
		this.robotX = robotX;
		this.robotY = robotY;
		this.robotCap = robotCap;
		this.caixas = caixas;
		this.paredes = paredes;
		this.saidaX = saidaX;
		this.saidaY = saidaY;
		distanciaPercorrida = 0;
		
		counter = 0;
		
		robot = Toolkit.getDefaultToolkit().getImage("imagens/robot.png");
		caixa = Toolkit.getDefaultToolkit().getImage("imagens/caixa.png");
		exit = Toolkit.getDefaultToolkit().getImage("imagens/exit.png");
		
		jpall = new JPanel();
		initDist();
		
		NovoPercurso();
		Fechar();
		
		objetos.add(jpall);
		
		cont.add(armazem);
		cont.add(objetos);
		
		pack();
		setLocationRelativeTo(null);
		setResizable(false);

		setVisible(true);
		
		calcula();
	}
	
	/**
	 * Redesenha o painel - comandado pelo swing
	 */
	public void paint(Graphics g) {
		super.paintComponents(g);
		
		
		g.drawImage(robot, robotX-37, robotY-49,	100, 100, this);
		
		/*for (int i = 0; i < caixas.size(); i++) {
			
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
		
		if (solutionFound) {
			for (int i = 1; i < percurso.size(); i++) {
				
				int cx, cy, dx, dy;
				
				cx = percurso.get(i-1).getX();
				cy = percurso.get(i-1).getY();
				dx = percurso.get(i).getX();
				dy = percurso.get(i).getY();
				
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(Color.GREEN);
	            g2.setStroke(new BasicStroke(7));
	            g2.draw(new Line2D.Float(cx, cy, dx, dy));
				
			}
		}*/
		
	}
	
	/**
	 * Funcao que passa o valor das variaveis as funcoes de logica para calcularem o percurso
	 */
	private void calcula() {
		percurso = new ArrayList<Coord<Integer, Integer>>();
		//percurso = func(robotX, robotY, robotCap, caixas, paredes, saidaX, saidaY)
			
		Coord<Integer, Integer> c1 = new Coord<Integer, Integer>(10,10);
		percurso.add(c1);
		Coord<Integer, Integer> c2 = new Coord<Integer, Integer>(200,100);
		percurso.add(c2);
		Coord<Integer, Integer> c3 = new Coord<Integer, Integer>(600,700);
		percurso.add(c3);
		solutionFound = true;

		/*for (int cnt = 0; cnt < percurso.size(); cnt++) {
			robotX = percurso.get(counter).getX();
			robotY = percurso.get(counter).getY();
			System.out.println(robotX);
			System.out.println(robotY);
			counter++;
			
			System.out.println("----");
			
			repaint();
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}*/
		
		/*for (int i = 0; i < percurso.size(); i++) {
			coordx = percurso.get(i).getX();
			coordy = percurso.get(i).getY();
			
			System.out.println(robotX);
			System.out.println(robotY);
			
			boolean reaching = true;
			
			while (reaching) {
				
				if (robotX < coordx) {
					//System.out.println("1");
					robotX++;
				}
				
				if (robotX > coordx) {
					//System.out.println("2");
					robotX--;
				}
				
				if (robotY < coordy) {
					//System.out.println("3");
					robotY++;
				}
				
				if (robotY > coordy) {
					//System.out.println("4");
					robotY--;
				}
				
				if (robotX == coordx && robotY == coordy) {
					//System.out.println("5");
					reaching = false;
				}
			
				repaint();

			}
		}*/
		
		
		
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
							setVisible(false);
							try {
								new MenuRobot();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
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
		distanciaLabel = new JLabel("Distancia Percorrida: " + distanciaPercorrida);
		
		jpall.add(distanciaLabel);

	}


	/**
	 * ActionListener
	 */
	public void actionPerformed(ActionEvent arg0) {
	}


	public void stateChanged(ChangeEvent arg0) {
		// TODO Auto-generated method stub
		
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
		
	}


	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


}
