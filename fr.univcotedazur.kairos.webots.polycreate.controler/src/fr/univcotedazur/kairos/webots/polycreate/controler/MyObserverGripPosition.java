package fr.univcotedazur.kairos.webots.polycreate.controler;

import com.yakindu.core.rx.Observer;

public class MyObserverGripPosition implements Observer{
	//attribut IHM
	PolyCreateControler theController;

	//constructeur pour l'IHM
	public MyObserverGripPosition(PolyCreateControler controller) {
		theController = controller;
	}

	@Override
	public void next(Object value) {
		System.out.println("Adjusting robot's position to face the object \n");
		theController.gripPosition();
		
	}
}
