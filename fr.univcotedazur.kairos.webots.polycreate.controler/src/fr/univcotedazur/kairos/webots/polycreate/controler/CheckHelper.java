package fr.univcotedazur.kairos.webots.polycreate.controler;

import com.cyberbotics.webots.controller.CameraRecognitionObject;

public class CheckHelper {
	private PolyCreateControler controler;
	
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
		CameraRecognitionObject[] ojsf = controler.frontCamera.getRecognitionObjects();
		
		if(ojsf.length >= 1) {
				//if(controler.closeToObject(ojsf)) {
					//System.out.println("OUPS! an object front");
					//System.out.println("I saw "+" on front Camera at : "+ojsf[0].getPosition()[0]);
					if(controler.carryObject == false) {
						controler.theCtrl.raiseThereIsAnObjectFront();
					}
					else {
						controler.theCtrl.raiseThereIsAnObstacle();
					}
					
				//}
		}
	}
	
	
	/**
	 * 
	 */
	public void objectCheckBack() {
		CameraRecognitionObject[] ojsb = controler.backCamera.getRecognitionObjects();
		
		if(ojsb.length >= 1) {
			if(controler.gripperCloseToObject(ojsb)) {
				//System.out.println("I see the object back");
				//System.out.println("I saw "+" on back Camera at : "+ojsb[0].getPosition()[0]);
				controler.theCtrl.raiseTheGripIsClose();
			}
			else {
				controler.theCtrl.raiseTheGripIsNotClose();
			}
		}
	}
	
}
