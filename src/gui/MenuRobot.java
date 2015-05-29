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

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;

/**
 * MenuRobot.java - Classe responsavel pelo menu de configuracoes do robot
 * 
 * @author - GA1_2
 * @version 1.0
 */

public class MenuRobot extends JFrame implements MouseListener {

	private static final long serialVersionUID = 7211727538677803065L;

	private JPanel jpall, jTextFieldJPanel;
	private int robotX, robotY, robotCap;
	private JLabel cimaLabel, robotXLabel, robotYLabel;
	private JTextField jtext;
	private Image robot;
	
	
	public static Container cont;
	private JPanel armazem, objetos;
	private JButton avancar;
	private double width, height;

	/**
	 * Construtor da classe
	 * 
	 * @throws IOException
	 */
	public MenuRobot() throws IOException {
		super("Menu Robot");
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
		
		robotX = 0;
		robotY = 0;
		robotCap = 0;
		robot = Toolkit.getDefaultToolkit().getImage("imagens/robot.png");

		jpall = new JPanel();
		initCapRobot();
		
		Avancar();
		objetos.add(jpall);
		
		cont.add(armazem);
		cont.add(objetos);
		
		pack();
		setLocationRelativeTo(null);
		setResizable(false);

		setVisible(true);

	}
	
	/**
	 * Redesenha o painel - comandado pelo swing
	 */
	public void paint(Graphics g) {
		super.paintComponents(g);
		if (robotX != 0) {
			g.drawImage(robot, robotX-37, robotY-49,	100, 100, this);
		}
	}
	
	/**
	 * Cria o botao avancar e faz verificacoes
	 */
	private void Avancar() {
	
		avancar = new JButton("Avancar");
		jpall.add(avancar);
		
		avancar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e ){
				
				JFrame frame = new JFrame();
				
				if (robotX != 0 && robotY != 0) {
					
					if (jtext.getText().matches("\\d+")) {
						robotCap = Integer.parseInt(jtext.getText());
						
						Object[] options = { "Sim", "Não", "Cancelar" };
						int n = JOptionPane.showOptionDialog(null,
								"Deseja avançar para a selecção das caixas? ",
								"Avançar", JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options,
								options[2]);

						if (n == 0) {
							try {
								setVisible(false);
								new MenuCaixas(robotX, robotY, robotCap);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						
						else {
							robotX = 0;
							robotY = 0;
							robotXLabel = new JLabel("Coordenada X: " + robotX);
							robotYLabel = new JLabel("Coordenada Y: " + robotY);
							repaint();
						}
						
					}
					
					else {
						JOptionPane.showMessageDialog(frame,
								"Capacidade do robot tem que ser um número inteiro! ", "Erro",
								JOptionPane.ERROR_MESSAGE);
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
	private void initCapRobot() {
		robotXLabel = new JLabel("Coordenada X: " + robotX);
		robotYLabel = new JLabel("Coordenada Y: " + robotY);
		
		cimaLabel = new JLabel("Capacidade do robot: ");
		jTextFieldJPanel = new JPanel();
		jTextFieldJPanel.add(cimaLabel);
		jtext = new JTextField(10);
		jTextFieldJPanel.add(cimaLabel);
		
		jpall.add(robotXLabel);
		jpall.add(robotYLabel);
		
		jpall.add(jTextFieldJPanel);
		jpall.add(jtext);

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
			
			robotXLabel.setText(String.valueOf("Coordenada X: " + x));
			robotYLabel.setText(String.valueOf("Coordenada Y: " + y));		
			
			robotX = x;
			robotY = y;
			
			repaint();
		}
		
	}


	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


}
