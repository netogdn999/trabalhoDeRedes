package controller;

import java.io.IOException;
import javax.sound.sampled.UnsupportedAudioFileException;
import model.Servidor;

public class Fachada {
	private Servidor servidor;
	
	public Fachada(int qtdRadios, String[][] musicas, int tcp) throws IOException, ArrayIndexOutOfBoundsException, UnsupportedAudioFileException{
		setServidor(new Servidor(qtdRadios, musicas, tcp));
	}
	
	public void setComando(String comando){getServidor().setMcomandoRecebe(comando);}
	public void zeraComando(){getServidor().setMcomandoEnvia(null);}
	public String getComando(){return getServidor().getMcomandoEnvia();}
	
	public void setReceber(String receber){getServidor().setReceber(receber);}
	public String getReceber(){return getServidor().getReceber();}
	
	public String getError(){return getServidor().getErro();}
	public void setError(String erro){getServidor().setErro(erro);}
	
	public void setServidor(Servidor servidor) {this.servidor = servidor;}
	public Servidor getServidor() {return servidor;}
}
