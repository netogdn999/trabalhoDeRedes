package model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import observer.Observer;

public class Cliente implements Observer {
	private Socket cliente;
	private DatagramSocket clienteUdp;
	private int porta;
	private InetAddress ip;
	private Station station;
	private byte[] buffer;
	
	public Cliente(Socket cliente){
		setCliente(cliente);
	}
	
	@Override
	public void update(byte[] buffer) {
		try {
			setBuffer(buffer);
			DatagramPacket resposta = new DatagramPacket(getBuffer(), getBuffer().length, getIp(), getPorta());
			getClienteUdp().send(resposta);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void doneConnection(){
		try {
			getClienteUdp().close();
			getCliente().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setCliente(Socket cliente) {this.cliente = cliente;}
	public Socket getCliente() {return cliente;}
	
	public void setPorta(int porta) {this.porta = porta;}
	public int getPorta() {return porta;}
	
	public void setStation(Station station) {this.station = station;}
	public Station getStation() {return station;}
	
	public void setIp(String ip) {
		try {
			this.ip = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	public InetAddress getIp() {return ip;}
	
	public void setClienteUdp(DatagramSocket clienteUdp) {this.clienteUdp = clienteUdp;}
	public DatagramSocket getClienteUdp() {return clienteUdp;}
	
	public byte[] getBuffer() {return buffer;}
	public void setBuffer(byte[] buffer) {this.buffer = buffer;}
}
