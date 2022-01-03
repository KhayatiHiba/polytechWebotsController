package fr.univcotedazur.kairos.webots.polycreate.controler;

import com.cyberbotics.webots.controller.CameraRecognitionObject;

public class CheckHelper {
	private PolyCreateControler controler;
	double value; 
	static double turnPrecision= 0.25;
	boolean checkBack = true;
	boolean gripped = false;
	
	public CheckHelper(PolyCreateControler poly) {
		this.controler = poly;
	}

	
	public void listen() {
		freePathCheck();
		CollisionCheck();
		if (objectCheck() == true) {
			position();
		}
		if (checkBack) {
			objectCheckBack();
			if(objectCheckBack()== true) {
				checkBack= false;
				isGripped();
			}
		}
		
		gapAndStairsCheck();
		
		if (!checkBack) {
			positionCheck();
		}
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
			controler.left= true;
			controler.theCtrl.raiseThereIsAnObstacle();	
		}
		else if(controler.frontRightDistanceSensor.getValue() < 250 ||controler.isThereCollisionAtRight()) {
			System.out.println("Obstacle at right detected\n" );
			controler.left= false;
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
	public boolean objectCheck() {
		CameraRecognitionObject[] frontObjects = controler.frontCamera.getRecognitionObjects();
		boolean isWorking = false;
		
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
			
			if (Math.round(distanceObj-0.4) <=0) {
				isWorking = true ;
				System.out.println("Distance optimale from robot to object achieved");
				System.out.println("Adjusting angle to face object");
				
				int[] objectPosition = firstobj.getPositionOnImage();
				int objectPos = objectPosition[0];
				System.out.println("The position of the object is: " + objectPos);
				
				boolean condition = position()>35 && position()<435; 
				 
				if (condition) {
					
					while ((position() > 248 || position() < 243) && (10 < position() && position() < 500) ){
						controler.stop();
						controler.doStep();
						//Turning to adjust into the object view
						int sign = 1;
						if (objectPos<100) { sign = -1;}
						if (objectPos>248) { sign = -1;}
						controler.leftMotor.setVelocity(sign*1);
						controler.rightMotor.setVelocity(sign*-1);	
						controler.doStep();
					}
				}
				if(position()<=35) {
					System.out.println("Object at left detected\n" );
					controler.left= false;
					controler.theCtrl.raiseThereIsAnObstacle();
				}
				if(position()>=435) {
					System.out.println("Object at right detected\n" );
					controler.left= false;
					controler.theCtrl.raiseThereIsAnObstacle();
				}
			}
		}
		return isWorking;
	}
	
	public int position() {
		CameraRecognitionObject[] frontObjects = controler.frontCamera.getRecognitionObjects();
		CameraRecognitionObject firstobj = frontObjects[0];
		
		int[] objectPosition1 = firstobj.getPositionOnImage();
		int objectPos = objectPosition1[0];
		
		boolean condition = objectPos>150 && objectPos<350; 
		 
		if (condition) {
			if (objectPos>=243 && objectPos<=248) {
				System.out.println("There is an object in the center");
				controler.stop();
				controler.theCtrl.raiseThereIsAnObject();
			}
		}
			
		System.out.println("The position of the object is: " + objectPos);
		return objectPos;
	}
	
	
	/**
	 * 
	 */
	public boolean objectCheckBack() {
		CameraRecognitionObject[] backObjects = controler.backCamera.getRecognitionObjects();
		boolean condition = false;
		if(backObjects.length >= 1) {
			System.out.println("Distance between object and gripper "+ controler.getObjectDistanceToGripper() );
			if(controler.getObjectDistanceToGripper() < 150) {
				controler.leftMotor.setVelocity(-1);
				controler.rightMotor.setVelocity(-1);
			}
			if(controler.getObjectDistanceToGripper() < 131) {
				controler.theCtrl.raiseReadyToGrip();
				System.out.println("I raised ready to grip\n");
				value = controler.getObjectDistanceToGripper();
				condition = true;
	        }	
		}
		return condition;
	}
	public void isGripped() {
		if (controler.getObjectDistanceToGripper()== value) {
			controler.theCtrl.raiseIsGripped();
			System.out.println("heeeeere");
			checkBack = false;
		}
		else{
			checkBack= true;
		}
	}

	public void positionCheck() {
		double posX = Math.round(controler.getSelf().getPosition()[0]);
		double posY = Math.round(controler.getSelf().getPosition()[2]);
		double orientation = Math.acos(controler.getSelf().getOrientation()[0]);
		controler.compare(posX,posY,orientation);
	}
}
