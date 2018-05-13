package view;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import controller.Fachada;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JScrollPane;

public class ClienteRadioGUI {

	private JFrame frame;
	private Fachada fachada;
	private JTextField txtCampomensage;
	private JTextArea txtrChat;
	private JLabel lblNome, lblDuracao;
	private JScrollPane scrollPane;

	public ClienteRadioGUI(String host,int portaTCP,int portaUDP) throws UnknownHostException, IOException {
		setFachada(new Fachada(host, portaTCP, portaUDP));
		escutar();
		checarErro();
		reproduzir();
		initialize();
	}
	
	public void reproduzir(){
		new Thread(){
			public void run(){
				try {
					AudioFormat af = getFachada().getAudioFormat();
					DataLine.Info dl = new DataLine.Info(SourceDataLine.class, af);
					SourceDataLine tdl = (SourceDataLine) AudioSystem.getLine(dl);
					tdl.open(af);
					tdl.start();
					ByteArrayInputStream bais;
					AudioInputStream ais;
					while(true){
						byte[] repro=getFachada().getRepro();
						if(repro!=null){
							bais = new ByteArrayInputStream(repro);
							if(bais.available()>0){
								ais = new AudioInputStream(bais, af, repro.length);
								int blidos = ais.read(repro);
								if(blidos!=-1){
									tdl.write(repro, 0, blidos);
								}
							}
							repro=null;
						}
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(getFrame(), e.getMessage(), "Mensagem de erro", JOptionPane.ERROR_MESSAGE);
				}
			}
		}.start();
	}
	
	public void checarErro(){
		new Thread(){
			public void run(){
				while(true){
					try{
						String checa=getFachada().getMensagemErro();
						if(checa!=null){
							getFachada().setMensagemErro(null);
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
			public void run(){
				while(true){
					try{
						String escutar=getFachada().getMensagemRecebida();
						if(escutar!=null && getTxtrChat()!=null){
							getFachada().setMensagemRecebida(null);
							String message[]=escutar.trim().split("-");
							if(message[0].equals("1")){
								lblNome.setText(message[1]);
								lblDuracao.setText(message[2]);
							}else if(message[0].equals("0")){
								getTxtrChat().setText(getTxtrChat().getText()+"servidor: "+message[1]+"\n");
							}else if(message[0].equals("2")){
								getTxtrChat().setText(getTxtrChat().getText()+"servidor: "+message[1]+"\n");
								Thread.sleep(1500);
								System.exit(0);
							}
						}
					}catch (InterruptedException e) {
						JOptionPane.showMessageDialog(getFrame(), e.getMessage(), "Mensagem de erro", JOptionPane.ERROR_MESSAGE);
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
		
		setTxtrChat(new JTextArea());
		getTxtrChat().addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				txtCampomensage.requestFocus();
			}
		});
		
		scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, BorderLayout.WEST);
		getTxtrChat().setForeground(Color.BLACK);
		getTxtrChat().setBackground(Color.WHITE);
		getTxtrChat().setEditable(false);
		scrollPane.setViewportView(getTxtrChat());
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		txtCampomensage = new JTextField();
		txtCampomensage.requestFocusInWindow();
		txtCampomensage.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					getFachada().setMensagemEnviada(new String[]{txtCampomensage.getText()});
					getTxtrChat().setText(getTxtrChat().getText()+"você: "+txtCampomensage.getText()+"\n");
					txtCampomensage.setText("");
				}
			}
		});
		panel.add(txtCampomensage, BorderLayout.CENTER);
		txtCampomensage.setColumns(10);
		
		JButton btnEnviar = new JButton("Enviar");
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				getFachada().setMensagemEnviada(new String[]{txtCampomensage.getText()});
				getTxtrChat().setText(getTxtrChat().getText()+"você: "+txtCampomensage.getText()+"\n");
				txtCampomensage.setText("");
				txtCampomensage.requestFocus();
			}
		});
		panel.add(btnEnviar, BorderLayout.EAST);
		
		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.NORTH);
		
		JLabel lblNomeDaMusica = DefaultComponentFactory.getInstance().createLabel("Nome da m\u00FAsica:");
		panel_1.add(lblNomeDaMusica);
		
		lblNome = DefaultComponentFactory.getInstance().createLabel("nome");
		panel_1.add(lblNome);
		
		JLabel lblDuracaoDaMusica = DefaultComponentFactory.getInstance().createLabel("        Dura\u00E7\u00E3o da m\u00FAsica: ");
		panel_1.add(lblDuracaoDaMusica);
		
		lblDuracao = DefaultComponentFactory.getInstance().createLabel("00:00:00");
		panel_1.add(lblDuracao);
	}

	public JFrame getFrame() {return frame;}
	
	public void setFachada(Fachada fachada) {this.fachada = fachada;}
	public Fachada getFachada() {return fachada;}
	
	public void setTxtrChat(JTextArea txtrChat) {this.txtrChat = txtrChat;}
	public JTextArea getTxtrChat() {return txtrChat;}
}
