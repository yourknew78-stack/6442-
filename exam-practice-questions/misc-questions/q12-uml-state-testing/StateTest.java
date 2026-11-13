import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class StateTest {

	private VendingMachine machine;

	@Before
	public void init() {
		this.machine = new VendingMachine();
	}

	@Test(timeout = 1000)
	public void testNormalWorkflow() {
		this.machine.getState().handle(Event.AnyButton);
		assertTrue(this.machine.getState() instanceof WaitingToSelect);

		this.machine.getState().handle(Event.ItemSelected);
		assertTrue(this.machine.getState() instanceof WaitingToPay);
		
		this.machine.getState().handle(Event.ItemPaid);
		assertTrue(this.machine.getState() instanceof WaitingToDispense);

		this.machine.getState().handle(Event.ItemDispensed);
		assertTrue(this.machine.getState() instanceof ReadyToCollect);

		this.machine.getState().handle(Event.Timeout);
		assertTrue(this.machine.getState() instanceof Idle);
	}
	
	@Test(timeout = 1000)
	public void testCancel1() {
		this.machine.getState().handle(Event.AnyButton);
		assertTrue(this.machine.getState() instanceof WaitingToSelect);

		this.machine.getState().handle(Event.Cancelled);
		assertTrue(this.machine.getState() instanceof Idle);
	}
	
	@Test(timeout = 1000)
	public void testCancel2() {
		this.machine.getState().handle(Event.AnyButton);
		assertTrue(this.machine.getState() instanceof WaitingToSelect);
		
		this.machine.getState().handle(Event.ItemSelected);
		assertTrue(this.machine.getState() instanceof WaitingToPay);

		this.machine.getState().handle(Event.Cancelled);
		assertTrue(this.machine.getState() instanceof Idle);
	}

	@Test(timeout = 1000)
	public void testTimeout1() {
		this.machine.getState().handle(Event.AnyButton);
		assertTrue(this.machine.getState() instanceof WaitingToSelect);

		this.machine.getState().handle(Event.Timeout);
		assertTrue(this.machine.getState() instanceof Idle);
	}

	@Test(timeout = 1000)
	public void testTimeout2() {
		this.machine.getState().handle(Event.AnyButton);
		assertTrue(this.machine.getState() instanceof WaitingToSelect);

		this.machine.getState().handle(Event.ItemSelected);
		assertTrue(this.machine.getState() instanceof WaitingToPay);

		this.machine.getState().handle(Event.Timeout);
		assertTrue(this.machine.getState() instanceof Idle);
	}
}
