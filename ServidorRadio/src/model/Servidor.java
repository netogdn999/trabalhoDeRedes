package model;

import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.sound.sampled.UnsupportedAudioFileException;

public class Servidor {
	private int porta;
	private ArrayList<Station> stations = new ArrayList<Station>();
	private ServerSocket servidor;
	private String receber;
	private String erro;
	private String McomandoRecebe;
	private String McomandoEnvia;
	
	public Servidor(int stations,String[][] musicas,int porta) throws IOException, ArrayIndexOutOfBoundsException, UnsupportedAudioFileException{
		try {
			setPorta(porta);
			setServidor(new ServerSocket(getPorta()));
			for (int i = 0; i < stations; i++) {
				getStations().add(new Station(i,musicas[i][0],musicas[i][1],100*(i+1)));
			}
			comando();
			aceitar();
		} catch (IOException e) {
			throw new IOException("Porta TCP já em uso");
		}
	}
	
	public void comando(){
		new Thread(){
			private String aux="";
			public void run(){
				while(true){
					String comando=getMcomandoRecebe();
					if(comando!=null){
						setMcomandoRecebe(null);
						if(comando.equals("p")){
							if(!getStations().isEmpty()){
								for(int i=0; i<getStations().size(); i++){
									aux+="------------Estação "+i+"-----------\n"+"Qtd. clientes: "+getStations().get(i).getClientes().size()+"\n";
									for(int j=0; j<getStations().get(i).getClientes().size(); j++)
										aux+="->cliente "+j+": "+getStations().get(i).getClientes().get(j).getIp()+"\n";
								}
								setMcomandoEnvia(aux);
								aux="";
							}
						}else if(comando.equals("q")){
							if(!getStations().isEmpty()){
								for(int i=0; i<getStations().size(); i++){
									getStations().get(i).detachAll();
								}
							}
							System.exit(0);
						}else{
							setErro("comando inválido");
						}
					}
				}
			}
		}.start();
	}
	
	public void aceitar(){
		new Thread(){
			public void run(){
				while(true){
					try {
						setReceber("esperando cliente");
						Cliente cliente = new Cliente(getServidor().accept());
						cliente.setIp(cliente.getCliente().getInetAddress().getHostAddress());
						setReceber("cliente conectado");
						conversar(cliente);
					} catch (IOException e) {
						setErro(e.getMessage());
					}
				}
			}
		}.start();
	}
	public void conversar(Cliente cliente){
		new Thread(){
			private boolean flag=false;
			private Scanner s;
			public void run(){
				while(true){
					try {
						s = new Scanner(cliente.getCliente().getInputStream());
						String aux;
						if((aux=s.nextLine())!=null){
							String message[]=((String)aux.trim().replaceAll("[\\s]{2,}", " ")).split(" ");
							PrintStream resposta = new PrintStream(cliente.getCliente().getOutputStream());
							if(message!=null){
								if(message[0].trim().equals("0") && !flag){
									setReceber("cliente: "+aux);
									cliente.setPorta(Integer.parseInt(message[1].trim()));
									cliente.setClienteUdp(new DatagramSocket());
									resposta.println("0-quantiade de estações: "+getStations().size());
									flag=true;
								}else if(message[0].trim().equals("1") && flag){
									setReceber("cliente: "+aux);
									if(Integer.parseInt(message[1].trim())<getStations().size()){
										if(cliente.getStation()!=null)
											cliente.getStation().detach(cliente);
										resposta.println("1-"+getStations().get(Integer.parseInt(message[1].trim())).getNomeMusica()+"-"+getStations().get(Integer.parseInt(message[1])).getDuracao());
										cliente.setStation(getStations().get(Integer.parseInt(message[1].trim())));
										getStations().get(Integer.parseInt(message[1].trim())).attach(cliente);
									}else{
										resposta.println("2-a estação "+Integer.parseInt(message[1].trim())+" não existe\n");
									}
								}else if(message[0].trim().equals("q") && flag){
									cliente.getStation().detach(cliente);
									resposta.println("2-desconectou");
								}else{
									resposta.println("2-comando inválido");
								}
							}
						}
					} catch (IOException | NoSuchElementException e) {
						cliente.getStation().detach(cliente);
						cliente.doneConnection();
						break;
					}
				}
			}
		}.start();
	}
	
	public void setPorta(int porta) {this.porta = porta;}
	public int getPorta() {return porta;}
	
	public void setStations(ArrayList<Station> stations) {this.stations = stations;}
	public ArrayList<Station> getStations() {return stations;}
	
	public void setServidor(ServerSocket servidor) {this.servidor = servidor;}
	public ServerSocket getServidor() {return servidor;}
	
	public void setReceber(String receber) {this.receber = receber;}
	public String getReceber() {return receber;}
	
	public void setErro(String erro) {this.erro = erro;}
	public String getErro() {return erro;}
	
	public void setMcomandoRecebe(String mcomando) {McomandoRecebe = mcomando;}
	public String getMcomandoRecebe() {return McomandoRecebe;}
	
	public void setMcomandoEnvia(String mcomandoEnvia) {McomandoEnvia = mcomandoEnvia;}
	public String getMcomandoEnvia() {return McomandoEnvia;}
}
