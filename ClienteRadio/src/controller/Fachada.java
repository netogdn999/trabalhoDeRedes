package controller;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;

import model.Cliente;

public class Fachada {
	private Cliente cliente;
	
	public Fachada(String host, int portaTcp, int portaUdp) throws UnknownHostException, IOException, IllegalArgumentException{
		setCliente(new Cliente(host, portaTcp, portaUdp));
	}
	
	public void setMensagemRecebida(String recebida){getCliente().setMensagemReceber(recebida);}
	public String getMensagemRecebida(){return getCliente().getMensagemReceber();}
	
	public void setMensagemEnviada(String[] enviada){getCliente().setMensagemEnviar(enviada);}
	
	public void setMensagemErro(String erro){getCliente().setMensagemErro(erro);}
	public String getMensagemErro(){return getCliente().getMensagemErro();}
	
	public void setRepro(byte[] repro){getCliente().setRepro(repro);}
	public byte[] getRepro(){return getCliente().getRepro();}
	
	public AudioFormat getAudioFormat(){return new AudioFormat(Encoding.PCM_SIGNED, 44100.0f, 16, 2, 4, 44100.0f, false);}
	
	public void setCliente(Cliente cliente) {this.cliente = cliente;}
	public Cliente getCliente() {return cliente;}
}
