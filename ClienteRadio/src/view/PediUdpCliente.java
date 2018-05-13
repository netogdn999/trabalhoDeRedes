package view;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import javax.swing.JTextField;
import java.awt.FlowLayout;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

import javax.swing.JPanel;

public class PediUdpCliente {

	private JFrame frame;
	private int portaUdp;
	private String host;
	private int portaTCP;
	private JTextField txtPorta;
	private JButton btnEntrar;
	private ClienteRadioGUI window;
	private JLabel lblIpHost;
	private JTextField txtIp;
	private JLabel lblDigiteAPorta;
	private JTextField txtTcp;
	private JPanel panel;
	private JPanel panel_1;
	private JPanel panel_2;
	private JPanel panel_3;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
					PediUdpCliente window = new PediUdpCliente();
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
	public PediUdpCliente() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 283, 197);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT, 5, 0);
		flowLayout.setAlignOnBaseline(true);
		frame.getContentPane().setLayout(flowLayout);
		
		/******************************
		 *							  *
		 *		    paineis 		  *
		 * 							  *
		 ******************************/
		
		panel = new JPanel();
		panel_1 = new JPanel();
		panel_2 = new JPanel();
		panel_3 = new JPanel();
		
		/******************************
		 *							  *
		 *		    TextField 		  *
		 * 							  *
		 ******************************/
		
		txtIp = new JTextField();
		txtTcp = new JTextField();
		txtPorta = new JTextField();
		
		frame.getContentPane().add(panel);
		lblIpHost = DefaultComponentFactory.getInstance().createLabel("Digite o ip do host:       ");
		panel.add(lblIpHost);
		txtIp.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER)
					txtTcp.requestFocus();
			}
		});
		panel.add(txtIp);
		txtIp.setColumns(10);
		
		frame.getContentPane().add(panel_1);
		lblDigiteAPorta = DefaultComponentFactory.getInstance().createLabel("Digite a porta tcp:         ");
		panel_1.add(lblDigiteAPorta);
		txtTcp.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode()==KeyEvent.VK_ENTER)
					txtPorta.requestFocus();
			}
		});
		panel_1.add(txtTcp);
		txtTcp.setColumns(10);
		
		frame.getContentPane().add(panel_2);
		JLabel lblPortaUdp = DefaultComponentFactory.getInstance().createLabel("Digite a sua porta udp:");
		panel_2.add(lblPortaUdp);
		lblPortaUdp.setVerticalAlignment(SwingConstants.BOTTOM);
		panel_2.add(txtPorta);
		txtPorta.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode()==KeyEvent.VK_ENTER){
					try {
						setPortaUdp(Integer.parseInt(txtPorta.getText().trim().replaceAll(" ", "")));
						setPortaTCP(Integer.parseInt(txtTcp.getText().trim().replaceAll(" ", "")));
						setHost(txtIp.getText().trim().replaceAll(" ", ""));
						frame.dispose();
						UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
						setWindow(new ClienteRadioGUI(getHost(),getPortaTCP(),getPortaUdp()));
						getWindow().getFrame().setVisible(true);
					} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException | IllegalArgumentException e) {
						JOptionPane.showMessageDialog(frame, e.getMessage(), "Mensagem de erro", JOptionPane.ERROR_MESSAGE);
						System.exit(0);
					} catch (IOException e) {
						if(e instanceof ConnectException)
							JOptionPane.showMessageDialog(frame, "A porta TCP já está sendo usada", "Mensagem de erro", JOptionPane.ERROR_MESSAGE);
						else
							JOptionPane.showMessageDialog(frame, "Servido não está disponivel ou ip está inválido", "Mensagem de erro", JOptionPane.ERROR_MESSAGE);
						System.exit(0);
					}	
				}
			}
		});
		txtPorta.setColumns(10);
		
		frame.getContentPane().add(panel_3);
		panel_3.setLayout(new FlowLayout(FlowLayout.CENTER, 95, 5));
		btnEntrar = new JButton("Entrar");
		panel_3.add(btnEntrar);
		btnEntrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					setPortaUdp(Integer.parseInt(txtPorta.getText().trim().replaceAll(" ", "")));
					setPortaTCP(Integer.parseInt(txtTcp.getText().trim().replaceAll(" ", "")));
					setHost(txtIp.getText().trim().replaceAll(" ", ""));
					frame.dispose();
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
					setWindow(new ClienteRadioGUI(getHost(),getPortaTCP(),getPortaUdp()));
					getWindow().getFrame().setVisible(true);
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException | IllegalArgumentException e) {
					JOptionPane.showMessageDialog(frame, e.getMessage(), "Mensagem de erro", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				} catch (UnknownHostException e) {
					JOptionPane.showMessageDialog(frame, "Servido não está disponivel ou ip está inválido", "Mensagem de erro", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(frame, "A porta TCP já está sendo usada ou está fora do range", "Mensagem de erro", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
			}
		});
	}

	public void setPortaUdp(int portaUdp) {this.portaUdp = portaUdp;}
	public int getPortaUdp() {return portaUdp;}
	
	public void setWindow(ClienteRadioGUI window) {this.window = window;}
	public ClienteRadioGUI getWindow() {return window;}
	
	public void setHost(String host) {this.host = host;}
	public String getHost() {return host;}
	
	public void setPortaTCP(int portaTCP) {this.portaTCP = portaTCP;}
	public int getPortaTCP() {return portaTCP;}
}
