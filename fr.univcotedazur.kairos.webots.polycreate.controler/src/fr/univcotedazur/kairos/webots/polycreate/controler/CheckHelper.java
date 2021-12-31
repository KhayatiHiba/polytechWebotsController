package fr.univcotedazur.kairos.webots.polycreate.controler;

import com.cyberbotics.webots.controller.CameraRecognitionObject;

public class CheckHelper {
	private PolyCreateControler controler;
	static double turnPrecision= 0.25;
	
	public CheckHelper(PolyCreateControler poly) {
		this.controler = poly;
	}

	/**
	 * Detects obstacles in the environment using the distance sensors. 
	 * When the robot approaches an obstacle it raises it's presence. 
	 * In the special cases where the sensors cannot detect the obstacles,
	 * this function will relies on the bumpers to detect a collision.
	 */
	public void CollisionCheck() {
		if(controler.frontLeftDistanceSensor.getValue() < 250 || controler.isThereCollisionAtLeft()) {
			System.out.println("Obstacle at left detected\n" );
			controler.theCtrl.raiseThereIsAnObstacle();	
		}
		else if(controler.frontRightDistanceSensor.getValue() < 250 ||controler.isThereCollisionAtRight()) {
			System.out.println("Obstacle at right detected\n" );
			controler.theCtrl.raiseThereIsAnObstacle();
		}
		else if( controler.frontDistanceSensor.getValue() < 250){
			System.out.println("Front Obstacle detected\n" );
			controler.theCtrl.raiseThereIsAFrontObstacle();
		}
		else if( controler.frontDistanceSensor.getValue() < 250 || controler.frontLeftDistanceSensor.getValue() < 250 || controler.frontRightDistanceSensor.getValue() < 250){
			System.out.println("Front Obstacle detected\n" );
			controler.theCtrl.raiseThereIsAFrontObstacle();
		}
		else if( !(controler.frontDistanceSensor.getValue() < 250)){
			controler.theCtrl.raiseThereIsNoObstacleFront();
		}
		else if( !(controler.frontDistanceSensor.getValue() < 250 || controler.frontLeftDistanceSensor.getValue() < 250 || controler.frontRightDistanceSensor.getValue() < 250)){
			controler.theCtrl.raiseThereIsNoObstacleFront();
		}
		else {
			controler.theCtrl.raiseThereIsNoObstacle();
		}
		
	}
	
	/**
	 * Determines the presence of a cliff using the distance sensors located under the robot.
	 * Two distance sensors are located at the front of the robot detecting thus a cliff in the front.
	 * While two remaining on each side of the robot detecting cliffs on either side.
	 * 
	 * When the robot approaches a cliff it raises it's presence and declares the side. 
	 */
	public void gapAndStairsCheck() {
		if ((controler.frontRightCliffSensor.getValue() == 0 && controler.frontLeftCliffSensor.getValue() == 0)) {
			System.out.println("A gap detected in front of the robot\n ");
			controler.theCtrl.raiseThereIsAGapDown();
		}
		else if ((controler.leftCliffSensor.getValue() == 0 )) {
			System.out.println("A gap detected in the left of the robot\n ");
			controler.theCtrl.raiseThereIsAGapDown();
		}
		else if ((controler.rightCliffSensor.getValue() == 0) ){
			System.out.println("A gap detected in the right of the robot\n ");
			controler.theCtrl.raiseThereIsAGapDown();
		}
		else if (!(controler.frontRightCliffSensor.getValue() == 0 || controler.frontLeftCliffSensor.getValue() == 0 || 
				controler.leftCliffSensor.getValue() == 0 || controler.rightCliffSensor.getValue() == 0) ){
			controler.theCtrl.raiseThereIsnoGap();
		}
	}
	
	/**
	 * Determines if there's a presence of a virtual wall detected in the receiver's queue.
	 * When the robot approaches a virtual wall it raises it's presence. 
	 */
	public void freePathCheck() {
		if(controler.isThereVirtualwall()) {
			System.out.println("Virtual wall detected\n");
			controler.theCtrl.raiseThereIsAVirtualWall();
		}
		else {
			controler.theCtrl.raiseThereIsNoVirtualWall();
		}
	}
	
	
	
	/**
	 * 
	 */
	public void objectCheck() {
		CameraRecognitionObject[] frontObjects = controler.frontCamera.getRecognitionObjects();
		if(frontObjects.length >= 1) {
			CameraRecognitionObject firstobj = frontObjects[0];
			
			double[] frontObjPos = firstobj.getPosition();
	        double xObj = frontObjPos[0];
	        double yObj = frontObjPos[2]; 
	        double angle = Math.atan(xObj/yObj*Math.PI/180);
	        
	        if(angle >= turnPrecision) {
	            controler.turn(angle);
	        }
	     
	        double distanceObj = Math.sqrt((xObj*xObj)+(yObj*yObj));
			System.out.println("Distance from origin to object: "+ distanceObj);
			
			//double distanceRob = Math.sqrt((controler.getSelf().getPosition()[0]*controler.getSelf().getPosition()[0])+(controler.getSelf().getPosition()[2]*controler.getSelf().getPosition()[2]));
			//System.out.println("Distance from origin to robot: "+ distanceRob);
			
			//double RobToObj = Math.abs(distanceRob-distanceObj);
			//System.out.println("Distance between object and robot "+ RobToObj );
			
			if (Math.round(distanceObj-0.3) <=0) {
				controler.theCtrl.raiseThereIsAnObject();
			}
		}
	}
	
	
	/**
	 * 
	 */
	public void objectCheckBack() {
		CameraRecognitionObject[] backObjects = controler.backCamera.getRecognitionObjects();
		
		if(backObjects.length >= 1) {
			System.out.println("Distance between object and gripper "+ controler.getObjectDistanceToGripper() );
			if(controler.getObjectDistanceToGripper() < 115) {
				controler.theCtrl.raiseReadyToGrip();
				System.out.println("I raised ready to grip\n");
	        }
		}
	}
	
}
