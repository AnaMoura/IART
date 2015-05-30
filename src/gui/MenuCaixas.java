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

/**
 * MenuCaixas.java - Classe responsavel pelo menu de configuracoes da caixas
 * 
 * @author - GA1_2
 * @version 1.0
 */

public class MenuCaixas extends JFrame implements MouseListener {
	
	private static final long serialVersionUID = 7211727538677803065L;

	private JPanel jpall;
	private int caixaX, caixaY, numeroCaixas;
	private ArrayList<Coord<Integer, Integer>> caixas;
	private JLabel caixaXLabel, caixaYLabel, numeroCaixasLabel;
	int robotX, robotY, robotCap;
	
	public static Container cont;
	private JPanel armazem, objetos;
	private JButton guardar, avancar;
	private double width, height;
	private Image caixa, robot;

	/**
	 * Construtor com os parametros que recebe do menu anterior
	 * 
	 * @param robotX
	 * @param robotY
	 * @param robotCap
	 * @throws IOException
	 */
	public MenuCaixas(int robotX, int robotY, int robotCap) throws IOException {
		super("Menu Caixas");
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
		
		robot = Toolkit.getDefaultToolkit().getImage("imagens/robot.png");
		
		this.robotX = robotX;
		this.robotY = robotY;
		this.robotCap = robotCap;

		caixaX = 0;
		caixaY = 0;
		numeroCaixas = 0; 
		caixas = new ArrayList<Coord<Integer, Integer>>();
		caixa = Toolkit.getDefaultToolkit().getImage("imagens/caixa.png");

		jpall = new JPanel();
		initCaixas();
		
		Guardar();
		
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
		
		
		if (caixaX != 0) {
			g.drawImage(caixa, caixaX-50, caixaY-50,	100, 100, this);
		}
		
		for (int i = 0; i < caixas.size(); i++) {
			
			int cx, cy;
			
			cx = caixas.get(i).getX();
			cy = caixas.get(i).getY();
			
			g.drawImage(caixa, cx-50, cy-50,	100, 100, this);
			
		}
	}
	
	/**
	 * Cria o botao de guardar a caixa actual e faz verificacoes
	 */
	private void Guardar() {
	
		guardar = new JButton("Guardar");
		jpall.add(guardar);
		
		guardar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e ){

						Object[] options = { "Sim", "Não", "Cancelar" };
						int n = JOptionPane.showOptionDialog(null,
								"Deseja guardar esta caixa? ",
								"Guardar", JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options,
								options[2]);

						if (n == 0) {
							Coord<Integer, Integer> coord = new Coord<Integer, Integer>(caixaX, caixaY);
							caixas.add(coord);
							numeroCaixas++;
							numeroCaixasLabel.setText(String.valueOf("Numero de caixas adicionadas: " + numeroCaixas));								
							
							caixaX = 0;
							caixaY = 0;
							repaint();
							caixaXLabel.setText(String.valueOf("Coordenada X: " + caixaX));
							caixaYLabel.setText(String.valueOf("Coordenada Y: " + caixaY));
						}
						
						else {
							caixaX = 0;
							caixaY = 0;
							repaint();
							caixaXLabel.setText(String.valueOf("Coordenada X: " + caixaX));
							caixaYLabel.setText(String.valueOf("Coordenada Y: " + caixaY));
						}

			}
		}
		);	
	}
	
	/**
	 * Cria o botao para avancar para o proximo menu e faz verificacoes
	 */
	private void Avancar() {
		
		avancar = new JButton("Avancar");
		jpall.add(avancar);
		
		avancar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e ){
				
				JFrame frame = new JFrame();
				
				if (caixas.size() != 0) {
						
						Object[] options = { "Sim", "Não", "Cancelar" };
						int n = JOptionPane.showOptionDialog(null,
								"Deseja avançar para a selecção de paredes? ",
								"Avançar", JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options,
								options[2]);

						if (n == 0) {
							setVisible(false);
							try {
								new MenuParedes(robotX, robotY, robotCap, caixas);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						
						else {
							caixaX = 0;
							caixaY = 0;
							repaint();
							caixaXLabel.setText(String.valueOf("Coordenada X: " + caixaX));
							caixaYLabel.setText(String.valueOf("Coordenada Y: " + caixaY));
						}
						
					}
				
				else {
					JOptionPane.showMessageDialog(frame,
							"É necessário adicionar pelo menos uma caixa! ", "Erro",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		);	
	}

	/**
	 * Inicializa os labels
	 */
	private void initCaixas() {
		caixaXLabel = new JLabel("Coordenada X: " + caixaX);
		caixaYLabel = new JLabel("Coordenada Y: " + caixaX);
		numeroCaixasLabel = new JLabel("Numero de caixas adicionadas: " + numeroCaixas);
		
		jpall.add(caixaXLabel);
		jpall.add(caixaYLabel);
		jpall.add(numeroCaixasLabel);

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
			
			caixaXLabel.setText(String.valueOf("Coordenada X: " + x));
			caixaYLabel.setText(String.valueOf("Coordenada Y: " + y));		
			
			caixaX = x;
			caixaY = y;
		}
		
		repaint();
		
	}


	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


}
