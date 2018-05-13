package view;

import java.io.IOException;

import javax.swing.JFrame;

import controller.Fachada;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.jgoodies.forms.factories.DefaultComponentFactory;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ServidorRadioGUI {

	private JFrame frame;
	private Fachada fachada;
	private JTextField txtMensagem;
	private JTextArea txtrChat;
	
	public ServidorRadioGUI(int radios, String[][] musicas, int tcp) throws IOException, ArrayIndexOutOfBoundsException, UnsupportedAudioFileException {
		setFachada(new Fachada(radios, musicas, tcp));
		initialize();
		erro();
		escutar();
	}
	
	public void erro(){
		new Thread(){
			public void run(){
				while(true){
					try{
						String checa=getFachada().getError();
						if(checa!=null && checa.equals("comando inválido")){
							getFachada().setError(null);
							JOptionPane.showMessageDialog(getFrame(), checa, "Mensagem de erro", JOptionPane.ERROR_MESSAGE);
						}else if(checa!=null){
							getFachada().setError(null);
							JOptionPane.showMessageDialog(getFrame(), checa, "Mensagem de erro", JOptionPane.ERROR_MESSAGE);
							Thread.sleep(1500);
							System.exit(0);
						}
					}catch (InterruptedException e) {
						JOptionPane.showMessageDialog(getFrame(), e.getMessage(), "Mensagem de erro", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}.start();
	}
	
	public void escutar(){
		new Thread(){
			private String recebe;
			public void run(){
				while(true){
					recebe=getFachada().getReceber();
					if(recebe!=null){
						getFachada().setReceber(null);
						getTxtrChat().setText(getTxtrChat().getText()+recebe+"\n");
					}
					recebe=getFachada().getComando();
					if(recebe!=null){
						getFachada().zeraComando();
						getTxtrChat().setText(getTxtrChat().getText()+recebe+"\n");
					}
				}
			}
		}.start();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 706, 509);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.EAST);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] {220, 0};
		gbl_panel.rowHeights = new int[] {40, 30, 30, 0};
		gbl_panel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblListaDeComandos = DefaultComponentFactory.getInstance().createLabel("Lista de comandos:");
		GridBagConstraints gbc_lblListaDeComandos = new GridBagConstraints();
		gbc_lblListaDeComandos.insets = new Insets(0, 0, 5, 0);
		gbc_lblListaDeComandos.gridx = 0;
		gbc_lblListaDeComandos.gridy = 0;
		panel.add(lblListaDeComandos, gbc_lblListaDeComandos);
		
		JLabel lblPLista = DefaultComponentFactory.getInstance().createLabel("p : lista as est\u00E7\u00F5es com seus clientes");
		GridBagConstraints gbc_lblPLista = new GridBagConstraints();
		gbc_lblPLista.insets = new Insets(0, 0, 5, 0);
		gbc_lblPLista.gridx = 0;
		gbc_lblPLista.gridy = 1;
		panel.add(lblPLista, gbc_lblPLista);
		
		JLabel label = new JLabel("");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.WEST;
		gbc_label.gridx = 0;
		gbc_label.gridy = 2;
		panel.add(label, gbc_label);
		
		JLabel lblQEncerra = DefaultComponentFactory.getInstance().createLabel("q : encerra o servidor ");
		GridBagConstraints gbc_lblQEncerra = new GridBagConstraints();
		gbc_lblQEncerra.gridx = 0;
		gbc_lblQEncerra.gridy = 2;
		panel.add(lblQEncerra, gbc_lblQEncerra);
		
		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		setTxtrChat(new JTextArea());
		getTxtrChat().setEditable(false);
		getTxtrChat().setForeground(Color.RED);
		getTxtrChat().addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				txtMensagem.requestFocus();
			}
		});
		scrollPane.setViewportView(getTxtrChat());
		
		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		txtMensagem = new JTextField();
		txtMensagem.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode()==KeyEvent.VK_ENTER){
					getFachada().setComando(txtMensagem.getText());
					txtMensagem.setText("");
				}
			}
		});
		panel_1.add(txtMensagem);
		txtMensagem.setColumns(10);
		
		JButton btnEnviar = new JButton("Enviar");
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				getFachada().setComando(txtMensagem.getText());
				txtMensagem.setText("");
				txtMensagem.requestFocus();
			}
		});
		panel_1.add(btnEnviar, BorderLayout.EAST);
	}
	
	public void setFachada(Fachada fachada) {this.fachada = fachada;}
	public Fachada getFachada() {return fachada;}
	
	public void setTxtrChat(JTextArea txtrChat) {this.txtrChat = txtrChat;}
	public JTextArea getTxtrChat() {return txtrChat;}
	
	public JFrame getFrame() {return frame;}
}
