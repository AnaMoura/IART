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
import projecto.Parede;

/**
 * MenuSaida.java - Classe responsavel pelo menu de configuracao da saida
 * 
 * @author - GA1_2
 * @version 1.0
 */

public class MenuSaida extends JFrame implements MouseListener {

	private static final long serialVersionUID = 7211727538677803065L;

	private JPanel jpall;
	private int saidaX, saidaY;
	private JLabel saidaXLabel, saidaYLabel;
	private Image exit, caixa, robot;
	int robotX, robotY, robotCap;
	private ArrayList<Coord<Integer, Integer>> caixas;
	private ArrayList<Parede<Coord<Integer, Integer>, Coord<Integer, Integer>>> paredes;
	
	public static Container cont;
	private JPanel armazem, objetos;
	private JButton avancar;
	private double width, height;

	/**
	 * Construtor com os parametros que recebe do menu anterior
	 * 
	 * @param robotX
	 * @param robotY
	 * @param robotCap
	 * @param caixas
	 * @param paredes
	 * @throws IOException
	 */
	public MenuSaida(int robotX, int robotY, int robotCap, ArrayList<Coord<Integer, Integer>> caixas, ArrayList<Parede<Coord<Integer, Integer>, Coord<Integer, Integer>>> paredes) throws IOException {
		super("Menu Saida");
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
		this.paredes = paredes;
		robot = Toolkit.getDefaultToolkit().getImage("imagens/robot.png");
		caixa = Toolkit.getDefaultToolkit().getImage("imagens/caixa.png");
		
		saidaX = 0;
		saidaY = 0;
		exit = Toolkit.getDefaultToolkit().getImage("imagens/exit.png");

		jpall = new JPanel();
		initSaida();
		
		Terminar();
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
		
		if (saidaX != 0) {
			g.drawImage(exit, saidaX-50, saidaY-50,	100, 100, this);
		}
	}
	
	/**
	 * Cria o botao terminar as configuracoes e faz verificacoes
	 */
	private void Terminar() {
	
		avancar = new JButton("Terminar");
		jpall.add(avancar);
		
		avancar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e ){
				
				JFrame frame = new JFrame();
				
				if (saidaX != 0 && saidaY != 0) {
					
						
						Object[] options = { "Sim", "Não", "Cancelar" };
						int n = JOptionPane.showOptionDialog(null,
								"Deseja terminar a selecção de parâmetros? ",
								"Avançar", JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options,
								options[2]);

						if (n == 0) {
								setVisible(false);
								try {
									new Percurso(robotX, robotY, robotCap, caixas, paredes, saidaX, saidaY);
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
						}
				}
				
				else {
					JOptionPane.showMessageDialog(frame,
							"É necessário seleccionar qual a posição inicial do robot! ", "Erro",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		);	
	}

	/**
	 * Inicializa os labels
	 */
	private void initSaida() {
		saidaXLabel  = new JLabel("Coordenada X: " + saidaX);
		saidaYLabel  = new JLabel("Coordenada Y: " + saidaY);
		
		jpall.add(saidaXLabel);
		jpall.add(saidaYLabel);

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
		
		if (y <= (int) (height*950) / (1080)) {		
			
			int x = arg0.getX();
			
			saidaXLabel.setText(String.valueOf("Coordenada X: " + x));
			saidaYLabel.setText(String.valueOf("Coordenada Y: " + y));		
			
			saidaX = x;
			saidaY = y;
			
			repaint();
		}
		
	}


	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


}
