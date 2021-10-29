package fr.univcotedazur.kairos.webots.polycreate.controler;

import com.yakindu.core.IStatemachine;
import com.yakindu.core.ITimed;
import com.yakindu.core.ITimerService;
import com.yakindu.core.rx.Observable;
import java.util.LinkedList;
import java.util.Queue;

public class Statechart2 implements IStatemachine, ITimed {
	public enum State {
		MAIN_REGION_ROBOT_IS_MOVING,
		MAIN_REGION_ROBOT_IS_MOVING_MAIN_MOVEFRONT,
		MAIN_REGION_ROBOT_IS_MOVING_MAIN_TURNING,
		$NULLSTATE$
	};
	
	private final State[] stateVector = new State[1];
	
	private ITimerService timerService;
	
	private final boolean[] timeEvents = new boolean[1];
	
	private Queue<Runnable> inEventQueue = new LinkedList<Runnable>();
	private boolean isExecuting;
	
	protected boolean getIsExecuting() {
		return isExecuting;
	}
	
	protected void setIsExecuting(boolean value) {
		this.isExecuting = value;
	}
	public Statechart2() {
		for (int i = 0; i < 1; i++) {
			stateVector[i] = State.$NULLSTATE$;
		}
		
		clearInEvents();
		
		
		isExecuting = false;
	}
	
	public void enter() {
		if (timerService == null) {
			throw new IllegalStateException("Timer service must be set.");
		}
		
		
		if (getIsExecuting()) {
			return;
		}
		isExecuting = true;
		
		enterSequence_main_region_default();
		isExecuting = false;
	}
	
	public void exit() {
		if (getIsExecuting()) {
			return;
		}
		isExecuting = true;
		
		exitSequence_main_region();
		isExecuting = false;
	}
	
	/**
	 * @see IStatemachine#isActive()
	 */
	public boolean isActive() {
		return stateVector[0] != State.$NULLSTATE$;
	}
	
	/** 
	* Always returns 'false' since this state machine can never become final.
	*
	* @see IStatemachine#isFinal()
	*/
	public boolean isFinal() {
		return false;
	}
	private void clearInEvents() {
		thereIsAnObstacle = false;
		thereIsNoObstacle = false;
		timeEvents[0] = false;
	}
	
	private void microStep() {
		switch (stateVector[0]) {
		case MAIN_REGION_ROBOT_IS_MOVING_MAIN_MOVEFRONT:
			main_region_robot_is_moving_main_moveFront_react(-1);
			break;
		case MAIN_REGION_ROBOT_IS_MOVING_MAIN_TURNING:
			main_region_robot_is_moving_main_turning_react(-1);
			break;
		default:
			break;
		}
	}
	
	private void runCycle() {
		if (timerService == null) {
			throw new IllegalStateException("Timer service must be set.");
		}
		
		
		if (getIsExecuting()) {
			return;
		}
		isExecuting = true;
		
		nextEvent();
		do { 
			microStep();
			
			clearInEvents();
			
			nextEvent();
		} while (((thereIsAnObstacle || thereIsNoObstacle) || timeEvents[0]));
		
		isExecuting = false;
	}
	
	protected void nextEvent() {
		if(!inEventQueue.isEmpty()) {
			inEventQueue.poll().run();
			return;
		}
	}
	/**
	* Returns true if the given state is currently active otherwise false.
	*/
	public boolean isStateActive(State state) {
	
		switch (state) {
		case MAIN_REGION_ROBOT_IS_MOVING:
			return stateVector[0].ordinal() >= State.
					MAIN_REGION_ROBOT_IS_MOVING.ordinal()&& stateVector[0].ordinal() <= State.MAIN_REGION_ROBOT_IS_MOVING_MAIN_TURNING.ordinal();
		case MAIN_REGION_ROBOT_IS_MOVING_MAIN_MOVEFRONT:
			return stateVector[0] == State.MAIN_REGION_ROBOT_IS_MOVING_MAIN_MOVEFRONT;
		case MAIN_REGION_ROBOT_IS_MOVING_MAIN_TURNING:
			return stateVector[0] == State.MAIN_REGION_ROBOT_IS_MOVING_MAIN_TURNING;
		default:
			return false;
		}
	}
	
	public void setTimerService(ITimerService timerService) {
		this.timerService = timerService;
	}
	
	public ITimerService getTimerService() {
		return timerService;
	}
	
	public void raiseTimeEvent(int eventID) {
		inEventQueue.add(() -> {
			timeEvents[eventID] = true;
		});
		runCycle();
	}
	
	
	private boolean thereIsAnObstacle;
	
	
	public void raiseThereIsAnObstacle() {
		inEventQueue.add(() -> {
			thereIsAnObstacle = true;
		});
		runCycle();
	}
	
	private boolean thereIsNoObstacle;
	
	
	public void raiseThereIsNoObstacle() {
		inEventQueue.add(() -> {
			thereIsNoObstacle = true;
		});
		runCycle();
	}
	
	private boolean turn;
	
	
	protected void raiseTurn() {
		turn = true;
		turnObservable.next(null);
	}
	
	private Observable<Void> turnObservable = new Observable<Void>();
	
	public Observable<Void> getTurn() {
		return turnObservable;
	}
	
	private boolean moveFront;
	
	
	protected void raiseMoveFront() {
		moveFront = true;
		moveFrontObservable.next(null);
	}
	
	private Observable<Void> moveFrontObservable = new Observable<Void>();
	
	public Observable<Void> getMoveFront() {
		return moveFrontObservable;
	}
	
	private boolean check;
	
	
	protected void raiseCheck() {
		check = true;
		checkObservable.next(null);
	}
	
	private Observable<Void> checkObservable = new Observable<Void>();
	
	public Observable<Void> getCheck() {
		return checkObservable;
	}
	
	/* Entry action for state 'robot is moving'. */
	private void entryAction_main_region_robot_is_moving() {
		timerService.setTimer(this, 0, 3, true);
	}
	
	/* Entry action for state 'moveFront'. */
	private void entryAction_main_region_robot_is_moving_main_moveFront() {
		raiseMoveFront();
	}
	
	/* Entry action for state 'turning'. */
	private void entryAction_main_region_robot_is_moving_main_turning() {
		raiseTurn();
	}
	
	/* Exit action for state 'robot is moving'. */
	private void exitAction_main_region_robot_is_moving() {
		timerService.unsetTimer(this, 0);
	}
	
	/* 'default' enter sequence for state robot is moving */
	private void enterSequence_main_region_robot_is_moving_default() {
		entryAction_main_region_robot_is_moving();
		enterSequence_main_region_robot_is_moving_main_default();
	}
	
	/* 'default' enter sequence for state moveFront */
	private void enterSequence_main_region_robot_is_moving_main_moveFront_default() {
		entryAction_main_region_robot_is_moving_main_moveFront();
		stateVector[0] = State.MAIN_REGION_ROBOT_IS_MOVING_MAIN_MOVEFRONT;
	}
	
	/* 'default' enter sequence for state turning */
	private void enterSequence_main_region_robot_is_moving_main_turning_default() {
		entryAction_main_region_robot_is_moving_main_turning();
		stateVector[0] = State.MAIN_REGION_ROBOT_IS_MOVING_MAIN_TURNING;
	}
	
	/* 'default' enter sequence for region main region */
	private void enterSequence_main_region_default() {
		react_main_region__entry_Default();
	}
	
	/* 'default' enter sequence for region main */
	private void enterSequence_main_region_robot_is_moving_main_default() {
		react_main_region_robot_is_moving_main__entry_Default();
	}
	
	/* Default exit sequence for state robot is moving */
	private void exitSequence_main_region_robot_is_moving() {
		exitSequence_main_region_robot_is_moving_main();
		exitAction_main_region_robot_is_moving();
	}
	
	/* Default exit sequence for state moveFront */
	private void exitSequence_main_region_robot_is_moving_main_moveFront() {
		stateVector[0] = State.$NULLSTATE$;
	}
	
	/* Default exit sequence for state turning */
	private void exitSequence_main_region_robot_is_moving_main_turning() {
		stateVector[0] = State.$NULLSTATE$;
	}
	
	/* Default exit sequence for region main region */
	private void exitSequence_main_region() {
		switch (stateVector[0]) {
		case MAIN_REGION_ROBOT_IS_MOVING_MAIN_MOVEFRONT:
			exitSequence_main_region_robot_is_moving_main_moveFront();
			exitAction_main_region_robot_is_moving();
			break;
		case MAIN_REGION_ROBOT_IS_MOVING_MAIN_TURNING:
			exitSequence_main_region_robot_is_moving_main_turning();
			exitAction_main_region_robot_is_moving();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region main */
	private void exitSequence_main_region_robot_is_moving_main() {
		switch (stateVector[0]) {
		case MAIN_REGION_ROBOT_IS_MOVING_MAIN_MOVEFRONT:
			exitSequence_main_region_robot_is_moving_main_moveFront();
			break;
		case MAIN_REGION_ROBOT_IS_MOVING_MAIN_TURNING:
			exitSequence_main_region_robot_is_moving_main_turning();
			break;
		default:
			break;
		}
	}
	
	/* Default react sequence for initial entry  */
	private void react_main_region__entry_Default() {
		enterSequence_main_region_robot_is_moving_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_main_region_robot_is_moving_main__entry_Default() {
		enterSequence_main_region_robot_is_moving_main_moveFront_default();
	}
	
	private long react(long transitioned_before) {
		return transitioned_before;
	}
	
	private long main_region_robot_is_moving_react(long transitioned_before) {
		long transitioned_after = transitioned_before;
		
		if (transitioned_after<0) {
			if (timeEvents[0]) {
				exitSequence_main_region_robot_is_moving();
				raiseCheck();
				
				enterSequence_main_region_robot_is_moving_default();
				react(0);
				
				transitioned_after = 0;
			}
		}
		/* If no transition was taken then execute local reactions */
		if (transitioned_after==transitioned_before) {
			transitioned_after = react(transitioned_before);
		}
		return transitioned_after;
	}
	
	private long main_region_robot_is_moving_main_moveFront_react(long transitioned_before) {
		long transitioned_after = transitioned_before;
		
		if (transitioned_after<0) {
			if (thereIsAnObstacle) {
				exitSequence_main_region_robot_is_moving_main_moveFront();
				enterSequence_main_region_robot_is_moving_main_turning_default();
				main_region_robot_is_moving_react(0);
				
				transitioned_after = 0;
			}
		}
		/* If no transition was taken then execute local reactions */
		if (transitioned_after==transitioned_before) {
			transitioned_after = main_region_robot_is_moving_react(transitioned_before);
		}
		return transitioned_after;
	}
	
	private long main_region_robot_is_moving_main_turning_react(long transitioned_before) {
		long transitioned_after = transitioned_before;
		
		if (transitioned_after<0) {
			if (thereIsNoObstacle) {
				exitSequence_main_region_robot_is_moving_main_turning();
				enterSequence_main_region_robot_is_moving_main_moveFront_default();
				main_region_robot_is_moving_react(0);
				
				transitioned_after = 0;
			}
		}
		/* If no transition was taken then execute local reactions */
		if (transitioned_after==transitioned_before) {
			transitioned_after = main_region_robot_is_moving_react(transitioned_before);
		}
		return transitioned_after;
	}
	
}