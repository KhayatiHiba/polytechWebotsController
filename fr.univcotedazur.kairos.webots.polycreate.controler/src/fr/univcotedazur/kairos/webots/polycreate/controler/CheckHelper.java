package fr.univcotedazur.kairos.webots.polycreate.controler;

import com.cyberbotics.webots.controller.CameraRecognitionObject;

public class CheckHelper {
	private PolyCreateControler controler;
	
	public CheckHelper(PolyCreateControler poly) {
		this.controler = poly;
	}

/////checking methods//////
	
	/**
	 * 
	 */
	public void freePathCheck() {
		if(!(controler.isThereVirtualwall())) {
			controler.theCtrl.raiseThereIsNoObstacle();
		}
		else if(controler.isThereVirtualwall()) {
			System.out.println("OUPS! a virtual wall");
			controler.theCtrl.raiseThereIsAVirtualWall();/// cas pas encore géré totalement **********
		}
	}
	
	/**
	 * 
	 */
	public void gapAndStairsCheck() {
		if ((controler.frontRightCliffSensor.getValue() == 0 || controler.frontLeftCliffSensor.getValue() == 0 || 
				controler.leftCliffSensor.getValue() == 0 || controler.rightCliffSensor.getValue() == 0) ){
			System.out.println("OUPS! a gap down ");
			controler.theCtrl.raiseThereIsAGapDown();
		}
		
		else if (!(controler.frontRightCliffSensor.getValue() == 0 || controler.frontLeftCliffSensor.getValue() == 0 || 
				controler.leftCliffSensor.getValue() == 0 || controler.rightCliffSensor.getValue() == 0) ){
			controler.theCtrl.raiseThereIsnoGap();
		}
	}
	
	/**
	 * 
	 */
	public void CollisionCheck() {
		if(controler.isThereCollisionAtLeft()) {
			System.out.println("OUPS! obstacle left" );
			controler.theCtrl.raiseThereIsAnObstacle();	
		}
		else if(controler.isThereCollisionAtRight()) {
			System.out.println("OUPS! obstacle right ");
			controler.theCtrl.raiseThereIsAnObstacle();
		}
		else if( controler.frontDistanceSensor.getValue() < 200 || controler.frontLeftDistanceSensor.getValue() < 200 || controler.frontRightDistanceSensor.getValue() < 200){
			System.out.println("OUPS! a front obstacle");
			controler.theCtrl.raiseThereIsAFrontObstacle();
		}
		else if( !(controler.frontDistanceSensor.getValue() < 200 || controler.frontLeftDistanceSensor.getValue() < 200 || controler.frontRightDistanceSensor.getValue() < 200)){
			controler.theCtrl.raiseThereIsNoObstacleFront();
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
