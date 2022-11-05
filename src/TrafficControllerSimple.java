
public class TrafficControllerSimple implements TrafficController{

	private TrafficRegistrar registrar;
	private boolean emptyBridge = true;
	
	public TrafficControllerSimple(TrafficRegistrar registrar) {
		 this.registrar = registrar;
	}
	
	@Override
	public synchronized void enterRight(Vehicle v) {
		while(!emptyBridge) {		//there is a car on the bridge
			try {
				wait();				//wait till bridge is empty and the previous car left the bridge
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		emptyBridge = false; 
		this.registrar.registerRight(v);
		
	}

	@Override
	public synchronized void enterLeft(Vehicle v) {
		while(!emptyBridge) {		
			try {
				wait();			
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		emptyBridge = false; 
		this.registrar.registerLeft(v);
		
	}

	@Override
	public synchronized void leaveLeft(Vehicle v) {
		this.emptyBridge = true;		//bridge is empty again, previous car has left 
		this.registrar.deregisterLeft(v);
		notifyAll();
		
	}

	@Override
	public synchronized void leaveRight(Vehicle v) {
		this.emptyBridge = true;	 
		this.registrar.deregisterRight(v);
		notifyAll();
		
	}

}
