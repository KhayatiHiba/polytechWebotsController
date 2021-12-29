/*******************************************************************************
 * Copyright (c) 2017 I3S and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *     UCA - I3S Laboratory - julien.deantoni@univ-cotedazur.fr -> initial API
 *******************************************************************************/

package fr.univcotedazur.kairos.webots.polycreate.controler;

import java.util.Random;

import javax.swing.Timer;
import javax.swing.text.Position;

import fr.univcotedazur.kairos.webots.polycreate.controler.Statechart2;

//import org.eclipse.january.dataset.Dataset;
//import org.eclipse.january.dataset.DatasetFactory;

import com.cyberbotics.webots.controller.Camera;
import com.cyberbotics.webots.controller.CameraRecognitionObject;
import com.cyberbotics.webots.controller.DistanceSensor;
import com.cyberbotics.webots.controller.GPS;
import com.cyberbotics.webots.controller.LED;
import com.cyberbotics.webots.controller.Motor;
import com.cyberbotics.webots.controller.Node;
import com.cyberbotics.webots.controller.Pen;
import com.cyberbotics.webots.controller.PositionSensor;
import com.cyberbotics.webots.controller.Receiver;
import com.cyberbotics.webots.controller.Robot;
import com.cyberbotics.webots.controller.Supervisor;
import com.cyberbotics.webots.controller.TouchSensor;
import com.yakindu.core.ITimerService;
import com.yakindu.core.TimerService;
import com.yakindu.core.rx.Observer;


public class PolyCreateControler extends Supervisor {

	static int MAX_SPEED = 18;
	static int NULL_SPEED = 0;
	static int HALF_SPEED = 6;
	static int MIN_SPEED = -18;
	static double turnPrecision= 0.25;

	static double WHEEL_RADIUS = 0.031;
	static double AXLE_LENGTH = 0.271756;
	static double ENCODER_RESOLUTION = 507.9188;

	/**
	 * the inkEvaporation parameter in the WorldInfo element of the robot scene may be interesting to access
	 */
	public Pen pen = null;

	public Pen getPen() {
		return pen;
	}
		
	public boolean thereIsAnObstacle, thereIsNoObstacle ;

	
	public Motor[] gripMotors = new Motor[2];
	public DistanceSensor gripperSensor = null;

	public Motor leftMotor = null;
	public Motor rightMotor = null;

	public PositionSensor leftSensor = null;
	public PositionSensor rightSensor = null;

	public LED ledOn = null;
	public LED ledPlay = null;
	public LED ledStep = null;

	public TouchSensor leftBumper = null;
	public TouchSensor rightBumper = null;

	public DistanceSensor leftCliffSensor = null;
	public DistanceSensor rightCliffSensor = null;
	public DistanceSensor frontLeftCliffSensor = null;
	public DistanceSensor frontRightCliffSensor = null;

	public DistanceSensor frontDistanceSensor = null;
	public DistanceSensor frontLeftDistanceSensor = null;
	public DistanceSensor frontRightDistanceSensor = null;
	
	public Camera frontCamera = null;
	public Camera backCamera = null;

	public Receiver receiver = null;

	protected Timer msTimer;
	public GPS gps = null;
	
	public 	int timestep = Integer.MAX_VALUE;
	public 	Random random = new Random();

	//added modification
	public Statechart2 theCtrl;
	CheckHelper theChecker;
	//boolean 
	private boolean isTurning = false;
	public boolean carryObject = false;
	public boolean facingTheObject = false;
	//saving robot position
	public double robotSavedPositionX;
	public double robotSavedPositionY;
	public double robotSavedOrientation;



	public PolyCreateControler() {
		timestep = (int) Math.round(this.getBasicTimeStep());
		this.carryObject = false;

/////////added
		theCtrl = new Statechart2(); 
		theChecker = new CheckHelper(this);
		TimerService timer = new TimerService();
		theCtrl.setTimerService(timer);

		////////////
		
		pen = createPen("pen");

		gripMotors[0] = createMotor("motor 7");
		gripMotors[1] = createMotor("motor 7 left");
		gripperSensor = createDistanceSensor("gripper middle distance sensor");
		gripperSensor.enable(timestep);

		leftMotor = createMotor("left wheel motor");
		rightMotor = createMotor("right wheel motor");
		leftMotor.setPosition(Double.POSITIVE_INFINITY);
		rightMotor.setPosition(Double.POSITIVE_INFINITY);
		leftMotor.setVelocity(0.0);
		rightMotor.setVelocity(0.0);

		leftSensor = createPositionSensor("left wheel sensor");
		rightSensor = createPositionSensor("right wheel sensor");
		leftSensor.enable(timestep);
		rightSensor.enable(timestep);

		ledOn = createLED("led_on");
		ledPlay = createLED("led_play");
		ledStep = createLED("led_step");

		leftBumper = createTouchSensor("bumper_left");
		rightBumper = createTouchSensor("bumper_right");
		leftBumper.enable(timestep);
		rightBumper.enable(timestep);
		
		thereIsAnObstacle  = false;
		thereIsNoObstacle = true;
	

		leftCliffSensor = createDistanceSensor("cliff_left");
		rightCliffSensor = createDistanceSensor("cliff_right");
		frontLeftCliffSensor = createDistanceSensor("cliff_front_left");
		frontRightCliffSensor = createDistanceSensor("cliff_front_right");
		leftCliffSensor.enable(timestep);
		rightCliffSensor.enable(timestep);
		frontLeftCliffSensor.enable(timestep);
		frontRightCliffSensor.enable(timestep);
		
		frontDistanceSensor = createDistanceSensor("front distance sensor");
		frontDistanceSensor.enable(timestep);
		frontLeftDistanceSensor = createDistanceSensor("front left distance sensor");
		frontLeftDistanceSensor.enable(timestep);
		frontRightDistanceSensor = createDistanceSensor("front right distance sensor");
		frontRightDistanceSensor.enable(timestep);
		
		frontCamera = createCamera("frontCamera");
		frontCamera.enable(timestep);
		frontCamera.recognitionEnable(timestep);

		backCamera = createCamera("backCamera");
		backCamera.enable(timestep);
		backCamera.recognitionEnable(timestep);

		receiver = createReceiver("receiver");
		receiver.enable(timestep);

		gps = createGPS("gps");
		gps.enable(timestep);
		
		
		//observeurs
		theCtrl.getTurn().subscribe( new MyObserverTurn(this));
		theCtrl.getMoveFront().subscribe( new MyObserverMoveFront(this));
		theCtrl.getCheck().subscribe(new MyObserverCheck(this));
		theCtrl.getMoveBack().subscribe(new MyObserverMoveBack(this));
		theCtrl.getTurnRound().subscribe(new MyObserverTurnRound(this));
		theCtrl.getFullTurn().subscribe(new MyObserverFullTurn(this));
		theCtrl.getStop().subscribe(new MyObserverStop(this));
		theCtrl.getTurnRound().subscribe(new MyObserverTurnRound(this));
		theCtrl.getGrip().subscribe(new MyObserverGrip(this));
		theCtrl.getDoPW().subscribe(new MyObserverPassiveWait(this));
		theCtrl.getSaveRobotPosition().subscribe(new MyObserverSaveRobotPosition(this));
		theCtrl.getCheckGripping().subscribe(new MyObserverCheckGripping(this));
		theCtrl.getFaceObject().subscribe(new MyObserverFaceObject(this));
		
		theCtrl.enter();

		//////////////////////////////////
		PolyCreateControler ctrl = this;
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			@Override
			public void run()
			{
				System.out.println("Shutdown hook ran!");
				ctrl.delete();
			}
		});
	}
		///////////////////////////////

	//////stateChart Methods//////
	public void check() {
		this.theChecker.freePathCheck();
		this.theChecker.CollisionCheck();
		this.theChecker.objectCheck();
		this.theChecker.gapAndStairsCheck();
		this.theChecker.objectCheckBack();	
		this.checkGripping();
	}
	
	/**
	 * Method used to dodge an obstacle while cleaning. 
	 * It turns the robot by a PI/4 angle.
	 */
	public void dodgeObstacle() {
		this.turn(Math.PI/4);
		theCtrl.raiseThereIsNoObstacle();
	}
	
	public void goForward() {
		leftMotor.setVelocity(MAX_SPEED);
		rightMotor.setVelocity(MAX_SPEED);
	}

	public void goBackward() {
		leftMotor.setVelocity(-HALF_SPEED);
		rightMotor.setVelocity(-HALF_SPEED);
	}

	public void stop() {
		leftMotor.setVelocity(NULL_SPEED);
		rightMotor.setVelocity(NULL_SPEED);
	}
	
	public void openGripper() {
		gripMotors[0].setPosition(0.5);
		gripMotors[1].setPosition(0.5);
	}

	public void closeGripper() {
		gripMotors[0].setPosition(-0.2);
		gripMotors[1].setPosition(-0.2);
	}
	
	public void saveRobotPosition() {
		this.robotSavedPositionX = Math.round(this.getSelf().getPosition()[0]);
		//System.out.println("position x saved: "+this.robotSavedPositionX);
		this.robotSavedPositionY = Math.round(this.getSelf().getPosition()[2]);
		//System.out.println("position y saved: "+this.robotSavedPositionY);
		this.robotSavedOrientation = Math.acos(this.getSelf().getOrientation()[0]);
		//System.out.println("orientation saved: "+this.robotSavedOrientation);
	}
	
	public void faceObject() {
		// TODO Auto-generated method stub
		CameraRecognitionObject[] ojsf = this.frontCamera.getRecognitionObjects();
		double angleRotation = 0.0;
		double robotOrientation = this.getOrientation();
		double objectOrientation = ojsf[0].getOrientation()[0];
		if(robotOrientation > 0 && objectOrientation > 0) {
			if(objectOrientation > robotOrientation) {
				angleRotation =  objectOrientation - robotOrientation;
			}
			else {
				angleRotation = robotOrientation - objectOrientation;
			}
		}
		else if(robotOrientation < 0 && objectOrientation < 0) {
			if(objectOrientation > robotOrientation) {
				angleRotation =  robotOrientation - objectOrientation ;
			}
			else {
				angleRotation = objectOrientation - robotOrientation;
			}
		}
		System.out.println("angle rotation" + angleRotation);
		System.out.println("object orientation" + objectOrientation);
		System.out.println("robot orientationn" + robotOrientation);
		this.turn(angleRotation);
		this.checkFacingTheObject();
		
	}
	
	public void grip() {
		// TODO Auto-generated method stub
		this.openGripper();
		this.goBackward();
		this.closeGripper();
		this.stop();
		carryObject = true;
	}
	
	/**
	 * 
	 */
	public void checkGripping() {
		if(this.carryObject) {
			theCtrl.raiseTheObjectIsGrip();
		}
	}
	
	public void checkFacingTheObject() {
		CameraRecognitionObject[] ojsf = this.frontCamera.getRecognitionObjects();
		if(ojsf.length != 0) {
			//System.out.println("i'm checking if i'm facing the object");
			double robotOrientation = this.getOrientation();
			double objectOrientation = ojsf[0].getOrientation()[0];
			if(robotOrientation == objectOrientation) {
				this.facingTheObject = true;
				theCtrl.raiseIsFacingTheObject();
			}
		}
		
	}
	
	public void turnRound() {
		this.turn(Math.PI/2 + 0.263999383);
		theCtrl.raiseThereIsNoObstacle();
	}
	
	public void fullTurn() {
		this.turn(Math.PI);
		flushIRReceiver();
		this.theChecker.freePathCheck();
	}
	
	/**
	 * turn the robot to getOrientation()+angle
	 * @param angle: the angle to apply
	 */
	void turn(double angle) {
		this.isTurning=true;
		stop();
		doStep();
		double direction = (angle < 0.0) ? -1.0 : 1.0;
		leftMotor.setVelocity(direction * HALF_SPEED);
		rightMotor.setVelocity(-direction * HALF_SPEED);
		double targetOrientation = (this.getOrientation()+angle)%(2*Math.PI);
		double actualOrientation;
		System.out.println("do");
		do {
			actualOrientation = this.getOrientation();
			doStep();
		} while (!(actualOrientation > (targetOrientation - turnPrecision) && actualOrientation < (targetOrientation + turnPrecision)));
		stop();
		doStep();
		this.isTurning=false;
	}
	
	
	
	//////helping check methods//////
	/**
	 * give the obstacle distance from the gripper sensor. max distance (i.e., no obstacle detected) is 1500
	 * @return
	 */
	public double getObjectDistanceToGripper() { return gripperSensor.getValue();}

	public boolean isThereCollisionAtLeft() { return leftBumper.getValue() != 0.0 ;}

	public boolean isThereCollisionAtRight() { return (rightBumper.getValue() != 0.0 );}

	public void flushIRReceiver() {
		while (receiver.getQueueLength() > 0)
			receiver.nextPacket();
	}

	public boolean isThereVirtualwall() { return (receiver.getQueueLength() > 1);}
	
	public boolean closeToObject(CameraRecognitionObject[] ojs) {
		double xObj = ((double)Math.round(ojs[0].getPosition()[1]*1000))/10;
		double yObj = Math.round(ojs[0].getPosition()[0]*180/Math.PI) ;
		double xRob = Math.round(this.getSelf().getPosition()[0]);
		double yRob = Math.round(this.getSelf().getPosition()[2]);
		double a = yObj-yRob;
		double b = xObj-xRob;
		double c = Math.sqrt((a*a)+(b*b));
		//System.out.println(c+"close object");
		if(c < 3 && this.facingTheObject)
			return true;
		return false;
	}
	
	public boolean gripperCloseToObject(CameraRecognitionObject[] ojsb) {
		double c = this.getObjectDistanceToGripper();
		//System.out.println(c + "close gripper");
		if(c < 145)
			return true;
		return false;
	}

	
	///////other methods///////
	synchronized void passiveWait(double sec) {
		double start_time = getTime();
		do {
			if (!isTurning ) {
				doStep();
			}else {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} while (start_time + sec > getTime());
	}
		
	void doStep() { step(timestep);}

	public double randdouble() { return  random.nextDouble();}

	public double[] getPosition() { return gps.getValues();}
	
	/**
	 * 
	 * @return the orientation wrt the north in [0; 2PI[
	 */
	public double getOrientation() {
		double res = Math.acos(this.getSelf().getOrientation()[0]);
		if(this.getSelf().getOrientation()[1] < 0) {
			return (2*Math.PI) - res;
		}else {
			return res;
		}
	}

	
			
	////////////main///////////
	public static void main(String[] args) {
		
		System.out.println("let's start");
		PolyCreateControler controler = new PolyCreateControler();
		while(true) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

	}


	@Override
	protected void finalize() {
		this.delete();
		super.finalize();
	}

	

	

	

}
