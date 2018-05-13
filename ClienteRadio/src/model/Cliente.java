package model;

import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;

public class Cliente {
	private String host;
	private int porta;
	private Socket servidor;
	private String[] mensagemEnviar;
	private String mensagemReceber;
	private String mensagemErro;
	private byte[] repro;
	private DatagramSocket clientSocket;
	private byte[] buffer;
	
	public Cliente(String host, int portaTCP, int portaUDP) throws UnknownHostException, IOException, IllegalArgumentException{
		super();
		setHost(host);
		setPorta(portaTCP);
		setServidor(new Socket(getHost(), getPorta()));
		setClientSocket(new DatagramSocket(portaUDP));
		reproduzir();
		falar();
		escutar();
	}
	
	public void falar(){
		new Thread(){
			public void run(){
				while(true){
					try {
						if(getMensagemEnviar()!=null){
							PrintStream falar = new PrintStream(getServidor().getOutputStream(),true);
							String mensagem="";
							for(int i=0; i<getMensagemEnviar().length; i++){
								mensagem+=" "+getMensagemEnviar()[i];
							}
							setMensagemEnviar(null);
							falar.println(mensagem.trim());
						}
					} catch (IOException e) {
						setMensagemErro(e.getMessage());
					}
				}
			}
		}.start();
	}
	
	public void escutar(){
		new Thread(){
			Scanner sc;
			public void run(){
				while(true){
					try {
						sc = new Scanner(getServidor().getInputStream());
						String message[],mensagem;
						if((mensagem=sc.nextLine().trim())!=null){
							message=mensagem.split("-");
							if(message[0].equals("2")){
								setMensagemErro(message[1]);
							}else{
								setMensagemReceber(mensagem);
							}
						}
					} catch (IOException | NoSuchElementException e) {
						setMensagemErro("Servidor caiu");
					}
				}
			}
		}.start();
	}
	
	public void reproduzir(){
		new Thread(){
			private AudioFormat format = getAudioFormat();
			private int bufferSize = (int) (format.getFrameSize()*format.getSampleRate())/3;
			public void run(){
				setBuffer(new byte[bufferSize]);
				while(true){
					try {
						DatagramPacket receivePacket = new DatagramPacket(getBuffer(), bufferSize);
						getClientSocket().receive(receivePacket);
						getClientSocket().setSoTimeout(8000);
						if(getBuffer()!=null){
							setRepro(receivePacket.getData());
						}
						setBuffer(null);
						setBuffer(new byte[bufferSize]);
					} catch (IOException e) {
						if(e instanceof SocketTimeoutException){
							setMensagemErro("servidor caiu");							
						}else{
							setMensagemErro(e.getMessage());
						}
					}
				}
			}
		}.start();
	}
	
	public void setHost(String host) {this.host = host;}
	public String getHost() {return host;}
	
	public void setPorta(int porta) {this.porta = porta;}
	public int getPorta() {return porta;}
	
	public void setBuffer(byte[] buffer) {this.buffer = buffer;}
	public byte[] getBuffer() {return buffer;}
	
	public void setServidor(Socket servidor) {this.servidor = servidor;}
	public Socket getServidor() {return servidor;}
	
	public void setClientSocket(DatagramSocket clientSocket) {this.clientSocket = clientSocket;}
	public DatagramSocket getClientSocket() {return clientSocket;}
	
	public void setMensagemEnviar(String[] mensagemEnviar) {this.mensagemEnviar = mensagemEnviar;}
	public String[] getMensagemEnviar() {return mensagemEnviar;}
	
	public void setMensagemReceber(String mensagemReceber) {this.mensagemReceber = mensagemReceber;}
	public String getMensagemReceber() {return mensagemReceber;}
	
	public AudioFormat getAudioFormat(){return new AudioFormat(Encoding.PCM_SIGNED, 44100.0f, 8, 2, 4, 44100.0f, false);}
	
	public void setMensagemErro(String mensagemErro) {this.mensagemErro = mensagemErro;}
	public String getMensagemErro() {return mensagemErro;}
	
	public void setRepro(byte[] bs) {this.repro = bs;}
	public byte[] getRepro() {return repro;}
}
