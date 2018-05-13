package view;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.FlowLayout;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import javax.swing.UnsupportedLookAndFeelException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.JPanel;

public class ServidorPegaTCP {

	private JFrame frame;
	private int radios;
	private int portaTCP;
	private JTextField txtRadios;
	private JButton btnEntrar;
	private ServidorRadioGUI window;
	private JTextField txtTcp;
	private JPanel panel_1;
	private JPanel panel_2;
	private JPanel panel_3;
	private JLabel lblDigiteAPorta;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
					ServidorPegaTCP window = new ServidorPegaTCP();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ServidorPegaTCP() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 318, 197);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setHgap(20);
		flowLayout.setVgap(10);
		panel_2 = new JPanel();
		panel_3 = new JPanel();
		txtTcp = new JTextField();
		txtRadios = new JTextField();
		frame.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		frame.getContentPane().add(panel_1);
		txtTcp.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode()==KeyEvent.VK_ENTER)
					txtRadios.requestFocus();
			}
		});
		
		lblDigiteAPorta = DefaultComponentFactory.getInstance().createLabel("Digite a porta tcp:                 ");
		panel_1.add(lblDigiteAPorta);
		panel_1.add(txtTcp);
		txtTcp.setColumns(10);
		
		frame.getContentPane().add(panel_2);
		JLabel lblRadios = DefaultComponentFactory.getInstance().createLabel("Digite a quantidade de radios:");
		panel_2.add(lblRadios);
		lblRadios.setVerticalAlignment(SwingConstants.BOTTOM);
		panel_2.add(txtRadios);
		txtRadios.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode()==KeyEvent.VK_ENTER){
					try {
						setRadios(Integer.parseInt(txtRadios.getText().trim().replaceAll(" ", "")));
						setPortaTCP(Integer.parseInt(txtTcp.getText().trim().replaceAll(" ", "")));
						frame.dispose();
						UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
						setWindow(new ServidorRadioGUI(getRadios(),new String[][]{{"C:\\Users\\adiministrator\\Desktop\\redes\\musicas\\passengerlethergo.wav","let her go"},{"C:\\Users\\adiministrator\\Desktop\\redes\\musicas\\maroon5animals.wav", "animals"}},getPortaTCP()));
						getWindow().getFrame().setVisible(true);
					} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException | IllegalArgumentException | ArrayIndexOutOfBoundsException | UnsupportedAudioFileException e) {
						JOptionPane.showMessageDialog(frame, e.getMessage(), "Mensagem de erro", JOptionPane.ERROR_MESSAGE);
						System.exit(0);
					} catch (IOException e) {
						JOptionPane.showMessageDialog(frame, "A porta TCP já está sendo usada", "Mensagem de erro", JOptionPane.ERROR_MESSAGE);
						System.exit(0);
					}
				}
			}
		});
		txtRadios.setColumns(10);
		
		frame.getContentPane().add(panel_3);
		panel_3.setLayout(new FlowLayout(FlowLayout.CENTER, 95, 5));
		btnEntrar = new JButton("Entrar");
		panel_3.add(btnEntrar);
		btnEntrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					setRadios(Integer.parseInt(txtRadios.getText().trim().replaceAll(" ", "")));
					setPortaTCP(Integer.parseInt(txtTcp.getText().trim().replaceAll(" ", "")));
					frame.dispose();
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
					setWindow(new ServidorRadioGUI(getRadios(),new String[][]{{"C:\\Users\\adiministrator\\Desktop\\redes\\musicas\\passengerlethergo.wav","let her go"},{"C:\\Users\\adiministrator\\Desktop\\redes\\musicas\\maroon5animals.wav", "animals"}},getPortaTCP()));
					getWindow().getFrame().setVisible(true);
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException | IllegalArgumentException | UnsupportedAudioFileException e) {
					JOptionPane.showMessageDialog(frame, e.getMessage(), "Mensagem de erro", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(frame, "A porta TCP já está sendo usada", "Mensagem de erro", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
			}
		});
	}

	public void setRadios(int radios) {this.radios = radios;}
	public int getRadios() {return radios;}
	
	public void setWindow(ServidorRadioGUI window) {this.window = window;}
	public ServidorRadioGUI getWindow() {return window;}
	
	public void setPortaTCP(int portaTCP) {this.portaTCP = portaTCP;}
	public int getPortaTCP() {return portaTCP;}
}
