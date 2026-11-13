
public abstract class State {
	
	protected final VendingMachine machine;
	
	public State(VendingMachine machine) {
		this.machine = machine;
	}
	
	public abstract void handle(Event event);

	public VendingMachine getMachine() {
		return machine;
	}
}
