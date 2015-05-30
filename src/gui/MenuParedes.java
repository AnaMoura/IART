package gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
 * MenuParedes.java - Classe responsavel pelo menu de configuracoes das paredes
 * 
 * @author - GA1_2
 * @version 1.0
 */

public class MenuParedes extends JFrame implements MouseListener {

	private static final long serialVersionUID = 7211727538677803065L;

	private JPanel jpall;
	private int paredeP1X, paredeP1Y, paredeP2X, paredeP2Y, numeroParedes;
	private ArrayList<Wall<Coord<Integer, Integer>, Coord<Integer, Integer>>> paredes;
	private JLabel paredeP1XLabel, paredeP1YLabel, paredeP2XLabel, paredeP2YLabel, numeroParedesLabel;
	
	public static Container cont;
	private JPanel armazem, objetos;
	private JButton guardar, avancar, clear;
	private double width, height;
	private Image caixa, robot;
	int robotX, robotY, robotCap;
	private ArrayList<Coord<Integer, Integer>> caixas;

	/**
	 * Construtor com os parametros que recebe do menu anterior
	 * 
	 * @param robotX
	 * @param robotY
	 * @param robotCap
	 * @param caixas
	 * @throws IOException
	 */
	public MenuParedes(int robotX, int robotY, int robotCap, ArrayList<Coord<Integer, Integer>> caixas) throws IOException {
		super("Menu Paredes");
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
		
		this.robotX = robotX;
		this.robotY = robotY;
		this.robotCap = robotCap;
		this.caixas = caixas;
		robot = Toolkit.getDefaultToolkit().getImage("imagens/robot.png");
		caixa = Toolkit.getDefaultToolkit().getImage("imagens/caixa.png");
		
		paredeP1X = 0;
		paredeP1Y = 0;
		paredeP2X = 0;
		paredeP2Y = 0;
		
		paredes = new ArrayList<Wall<Coord<Integer, Integer>, Coord<Integer, Integer>>>();

		jpall = new JPanel();
		initParedes();
		
		Guardar();
		Apagar();
		Avancar();
		objetos.add(jpall);
		
		cont.add(armazem);
		cont.add(objetos);
		
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		//dispose();
		//setUndecorated(true);

		setVisible(true);

	}
	
	/**
	 * Redesenha o painel - comandado pelo swing
	 */
	public void paint(Graphics g) {
		super.paintComponents(g);
		
		g.drawImage(robot, robotX-37, robotY-49,	100, 100, this);
		
		for (int i = 0; i < caixas.size(); i++) {
			
			int cx, cy;
			
			cx = caixas.get(i).getX();
			cy = caixas.get(i).getY();
			
			g.drawImage(caixa, cx-50, cy-50,	100, 100, this);
			
		}
		
		if (paredeP1X != 0 && paredeP2X != 0) {
			g.setColor(Color.WHITE);
			g.drawLine(paredeP1X, paredeP1Y, paredeP2X, paredeP2Y);		
		}
		
		for (int i = 0; i < paredes.size(); i++) {
			
			int cx, cy, dx, dy;
			
			cx = paredes.get(i).getX().getX();
			cy = paredes.get(i).getX().getY();
			dx = paredes.get(i).getY().getX();
			dy = paredes.get(i).getY().getY();
			
			g.setColor(Color.WHITE);
			g.drawLine(cx, cy, dx, dy);	
			
		}
	}

	/**
	 * Cria o botao de fazer reset da parede desenhada
	 */
	private void Apagar() {
	
		clear = new JButton("Clear");
		jpall.add(clear);
		
		clear.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e ){
								
				paredeP1X = 0;
				paredeP1Y = 0;
				paredeP2X = 0;
				paredeP2Y = 0;
				
				paredeP1XLabel.setText(String.valueOf("Coordenada X Ponto 1: " + paredeP1X));
				paredeP1YLabel.setText(String.valueOf("Coordenada Y Ponto 1: " + paredeP1Y));
				paredeP2XLabel.setText(String.valueOf("Coordenada X Ponto 2: " + paredeP2X));
				paredeP2YLabel.setText(String.valueOf("Coordenada Y Ponto 2: " + paredeP2Y));
				
				repaint();
			}
		}
		);	
	}
	
	/**
	 * Cria o botao guardar a parede e faz verificacoes
	 */
	private void Guardar() {
	
		guardar = new JButton("Guardar");
		jpall.add(guardar);
		
		guardar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e ){

						Object[] options = { "Sim", "Não", "Cancelar" };
						int n = JOptionPane.showOptionDialog(null,
								"Deseja guardar esta parede? ",
								"Guardar", JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options,
								options[2]);

						if (n == 0) {
							Coord<Integer, Integer> coord1 = new Coord<Integer, Integer>(paredeP1X, paredeP1Y);
							Coord<Integer, Integer> coord2 = new Coord<Integer, Integer>(paredeP2X, paredeP2Y);
							
							Wall<Coord<Integer, Integer>, Coord<Integer, Integer>> parede = new Wall<Coord<Integer, Integer>, Coord<Integer, Integer>>(coord1,coord2);
							
							paredes.add(parede);
							numeroParedes++;
							numeroParedesLabel.setText(String.valueOf("Numero de paredes adicionadas: " + numeroParedes));								
							
							paredeP1X = 0;
							paredeP1Y = 0;
							paredeP2X = 0;
							paredeP2Y = 0;
							
							paredeP1XLabel.setText(String.valueOf("Coordenada X Ponto 1: " + paredeP1X));
							paredeP1YLabel.setText(String.valueOf("Coordenada Y Ponto 1: " + paredeP1Y));
							paredeP2XLabel.setText(String.valueOf("Coordenada X Ponto 2: " + paredeP2X));
							paredeP2YLabel.setText(String.valueOf("Coordenada Y Ponto 2: " + paredeP2Y));
							
							repaint();
						}
						
						else {
							paredeP1X = 0;
							paredeP1Y = 0;
							paredeP2X = 0;
							paredeP2Y = 0;
							
							paredeP1XLabel.setText(String.valueOf("Coordenada X Ponto 1: " + paredeP1X));
							paredeP1YLabel.setText(String.valueOf("Coordenada Y Ponto 1: " + paredeP1Y));
							paredeP2XLabel.setText(String.valueOf("Coordenada X Ponto 2: " + paredeP2X));
							paredeP2YLabel.setText(String.valueOf("Coordenada Y Ponto 2: " + paredeP2Y));
							
							repaint();
						}

			}
		}
		);	
	}

	/**
	 * Cria o botao para avancar para o proximo e ultimo menu
	 */	
	private void Avancar() {
		
		avancar = new JButton("Avancar");
		jpall.add(avancar);
		
		avancar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e ){
				
				JFrame frame = new JFrame();
				
				if (paredes.size() != 0) {
						
						Object[] options = { "Sim", "Não", "Cancelar" };
						int n = JOptionPane.showOptionDialog(null,
								"Deseja avançar para a selecção de paredes? ",
								"Avançar", JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options,
								options[2]);

						if (n == 0) {
							setVisible(false);
							try {
								new MenuSaida(robotX, robotY, robotCap, caixas, paredes);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						
						else {
							paredeP1X = 0;
							paredeP1Y = 0;
							paredeP2X = 0;
							paredeP2Y = 0;
							
							paredeP1XLabel.setText(String.valueOf("Coordenada X Ponto 1: " + paredeP1X));
							paredeP1YLabel.setText(String.valueOf("Coordenada Y Ponto 1: " + paredeP1Y));
							paredeP2XLabel.setText(String.valueOf("Coordenada X Ponto 2: " + paredeP2X));
							paredeP2YLabel.setText(String.valueOf("Coordenada Y Ponto 2: " + paredeP2Y));
							
							repaint();
						}

						
					}
				
				else {
					JOptionPane.showMessageDialog(frame,
							"É necessário adicionar pelo menos uma parede! ", "Erro",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		);	
	}
	
	/**
	 * Inicializa os labels
	 */
	private void initParedes() {
		paredeP1XLabel = new JLabel("Coordenada X Ponto 1: " + paredeP1X);
		paredeP1YLabel = new JLabel("Coordenada Y Ponto 1: " + paredeP1Y);
		paredeP2XLabel = new JLabel("Coordenada X Ponto 2: " + paredeP2X);
		paredeP2YLabel = new JLabel("Coordenada Y Ponto 2: " + paredeP2Y);
		numeroParedesLabel = new JLabel("Numero de paredes adicionadas: " + numeroParedes);
		
		jpall.add(paredeP1XLabel);
		jpall.add(paredeP1YLabel);
		jpall.add(paredeP2XLabel);
		jpall.add(paredeP2YLabel);
		jpall.add(numeroParedesLabel);

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
		
		int y = arg0.getY();
		
		if (y <= 950) {		
			int x = arg0.getX();
			
			if (paredeP1X == 0 ) {
		
				paredeP1XLabel.setText(String.valueOf("Coordenada X Ponto 1: " + x));
				paredeP1YLabel.setText(String.valueOf("Coordenada Y Ponto 1: " + y));
				
				paredeP1X = x;
				paredeP1Y = y;
			}
			else {
				
				paredeP2XLabel.setText(String.valueOf("Coordenada X Ponto 2: " + x));
				paredeP2YLabel.setText(String.valueOf("Coordenada Y Ponto 2: " + y));
				
				paredeP2X = x;
				paredeP2Y = y;
				
			}
		}
		repaint();
		
	}


	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


}
