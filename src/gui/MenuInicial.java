package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javax.swing.*;

/**
 * MenuInicial.java - Classe responsavel pelo menu incial da vertente grafica do jogo
 * 
 * @author - GA1_2
 * @version 1.0
 */
public class MenuInicial extends JFrame implements MouseListener {
	
	private static final long serialVersionUID = 5566717366387871929L;
	private JPanel jpanel;
	private Image main;
	private double width, height;
	
	/**
	 * Construtor padrao da classe
	 */
	public MenuInicial(){
		super("Determincao do Trajeto de um Robot");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = screenSize.getWidth(); //largura
		height = screenSize.getHeight(); //altura

		int largura = (int) (width*1062)/1920;
		int altura  = (int) (height*820)/1080;
		
		setPreferredSize(new Dimension(largura,altura));
		loadImages();
	
		jpanel = new MouseMotionEventDemo();
		add(jpanel);
		jpanel.addMouseListener(this);

        pack();  
        setVisible(true);
		setLocationRelativeTo(null);
		setResizable(false);
	}

	/**
	 * Load da imagem de fundo
	 */
	private void loadImages() {
		Toolkit kit = Toolkit.getDefaultToolkit();
		main = kit.getImage("imagens/main.png");
	}

	
	public class MouseMotionEventDemo extends JPanel implements MouseMotionListener {

		private static final long serialVersionUID = 1L;

		public MouseMotionEventDemo() {
			addMouseMotionListener(this);
			setVisible(true);
		}

		public void mouseMoved(MouseEvent me) {;
		}

		public void mouseDragged(MouseEvent me) {
			mouseMoved(me);
		}

		public void paintComponent(Graphics g) {
			
			g.drawImage(main, 0, 0, (int) (width*1062)/(1920), (int) (height*820)/(1080), this);

		}

	}
	
	/**
	 * EventListeners do rato, na escolha de opcoes do menu
	 */
	@Override
	public void mouseClicked(MouseEvent e) { // Escolher a opcao do menu
		
		if ( e.getX() >= (int) (width*700) / (1920) && e.getX() <= (int) (width*1332) / (1011) ) {
			if ( e.getY() >= (int) (height*431) / (1080) && e.getY() <= (int) (height*512) / (1080) ) {
				try {
					new MenuRobot();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		
			else if (  e.getY() >= (int) (height*526) / (1080) && e.getY() <= (int) (height*605) / (1080) ) {
				try {
					new MenuRobot();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		
			else if (  e.getY() >= (int) (height*617) / (1080) && e.getY() <= (int) (height*695) / (1080) ) {
				Object[] options = { "Sim", "N�o", "Cancelar" };
				int n = JOptionPane.showOptionDialog(null,
						"Deseja abandonar a aplica��o? ",
						"Fechar", JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, options,
						options[2]);

				if (n == 0) {
					System.exit(-1);
				} 
			}
		}

	}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}

}