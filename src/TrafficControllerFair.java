import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TrafficControllerFair implements TrafficController {

	private TrafficRegistrar registrar;
	private boolean emptyBridge = true;
	Lock myLock = new ReentrantLock(true);
	Condition emptyBridgeCondition = myLock.newCondition();

	public TrafficControllerFair(TrafficRegistrar registrar) {
		this.registrar = registrar;
	}

	@Override
	public void enterRight(Vehicle v) {
		try {
			myLock.lock();
			while (!emptyBridge) { // while there is a car on the bridge
				emptyBridgeCondition.await();
			}
			emptyBridge = false;
			this.registrar.registerRight(v);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void enterLeft(Vehicle v) {

		try {
			myLock.lock();
			while (!emptyBridge) { // while there is a car on the bridge
				emptyBridgeCondition.await();
			}
			emptyBridge = false;
			this.registrar.registerLeft(v);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void leaveLeft(Vehicle v) {
		emptyBridge = true;
		this.registrar.deregisterLeft(v);
		emptyBridgeCondition.signalAll();
		
		myLock.unlock();
		
	}

	@Override
	public void leaveRight(Vehicle v) {
		emptyBridge = true;
		this.registrar.deregisterRight(v);
		emptyBridgeCondition.signalAll();
		
		myLock.unlock();
	}

}
