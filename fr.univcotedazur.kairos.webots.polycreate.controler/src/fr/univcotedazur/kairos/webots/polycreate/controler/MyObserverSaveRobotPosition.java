package fr.univcotedazur.kairos.webots.polycreate.controler;

import com.yakindu.core.rx.Observer;

public class MyObserverSaveRobotPosition implements Observer{
	
	//attribut IHM
	PolyCreateControler theController;

	//constructeur pour l'IHM
	public MyObserverSaveRobotPosition(PolyCreateControler controller) {
		theController = controller;
	}

	@Override
	public void next(Object value) {
		// TODO Auto-generated method stub
		//System.out.println("Robot position saved");
		theController.saveRobotPosition();
		
	}
}
