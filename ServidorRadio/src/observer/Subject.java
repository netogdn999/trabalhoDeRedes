package observer;

import model.Cliente;

public interface Subject {
	public void attach(Cliente cliente);
	public void detach(Cliente cliente);
	public void detachAll();
	public void notificar();
}
