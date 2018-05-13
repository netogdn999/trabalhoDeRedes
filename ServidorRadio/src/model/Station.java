package model;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import observer.Subject;

public class Station implements Subject{
	private int id;
	private String path;
	private ArrayList<Cliente> clientes = new ArrayList<Cliente>();
	private String nomeMusica;
	private String duracao;
	private AudioInputStream musica;
	private AudioFormat formato;
	private int totalPartes;
	private byte[] buffer;
	private int bufferSize;
	
	public Station(int id, String path, String nomeMusica, int total) throws UnsupportedAudioFileException, IOException{
		setId(id);
		setPath(path);
		setMusica(AudioSystem.getAudioInputStream(new File(path)));
		setBufferSize((int) (getFormato().getFrameSize()*getFormato().getSampleRate())/3);
		setBuffer(new byte[getBufferSize()]);
		setNomeMusica(nomeMusica);
		setTotalPartes((int)(getMusica().getFrameLength()/(getFormato().getFrameSize()*getFormato().getSampleRate())));
		if((getTotalPartes()/60)<0){
			setDuracao("00:00:"+getTotalPartes());
		}else if((getTotalPartes()/60)>10){
			setDuracao("00:"+(getTotalPartes()/60)+":"+(getTotalPartes()-((getTotalPartes()/60)*60)));
		}else{
			setDuracao("00:0"+(getTotalPartes()/60)+":"+(getTotalPartes()-((getTotalPartes()/60)*60)));
		}
		tocarMusica();
	}
	
	public void tocarMusica(){
		new Thread(){
			public void run(){
				while(true){
					try {
						while(getMusica().read(getBuffer())!=-1){
							if(!getClientes().isEmpty()){
								notificar();
							}
							Thread.sleep(1000/3);
						}
					} catch (InterruptedException | IOException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	@Override
	public void attach(Cliente cliente) {
		getClientes().add(cliente);
	}

	@Override
	public void detach(Cliente cliente) {
		getClientes().remove(cliente);
	}
	
	public void detachAll(){
		try {
			PrintStream resposta;
			for (Cliente cliente : clientes) {
				resposta = new PrintStream(cliente.getCliente().getOutputStream());
				resposta.println("2-conexão finalizada");
			}
			getClientes().clear();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void notificar() {
		for (Cliente cliente : clientes) {
			cliente.update(getBuffer());
		}
	}
	
	public void setId(int id) {this.id = id;}
	public int getId() {return id;}
	
	public void setPath(String path) {this.path = path;}
	public String getPath() {return path;}
	
	public void setNomeMusica(String nomeMusica) {this.nomeMusica = nomeMusica;}
	public String getNomeMusica() {return nomeMusica;}
	
	public ArrayList<Cliente> getClientes() {return clientes;}
	
	public void setDuracao(String duracao) {this.duracao = duracao;}
	public String getDuracao() {return duracao;}
	
	public int getTotalPartes() {return totalPartes;}
	public void setTotalPartes(int totalPartes) {this.totalPartes = totalPartes;}
	
	public void setBuffer(byte[] buffer) {this.buffer = buffer;}
	public byte[] getBuffer() {return buffer;}
	
	public void setMusica(AudioInputStream musica) {this.musica = musica;}
	public AudioInputStream getMusica() {return musica;}
	
	public AudioFormat getAudioFormat(){return new AudioFormat(Encoding.PCM_SIGNED, 44100.0f, 16, 2, 4, 44100.0f, false);}
	
	public AudioFormat getFormato() {this.formato=getAudioFormat();return formato;}
	
	public void setBufferSize(int bufferSize) {this.bufferSize = bufferSize;}
	public int getBufferSize() {return bufferSize;}
}
