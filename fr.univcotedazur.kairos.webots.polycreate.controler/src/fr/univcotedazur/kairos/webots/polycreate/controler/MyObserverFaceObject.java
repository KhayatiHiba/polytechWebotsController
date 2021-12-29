package fr.univcotedazur.kairos.webots.polycreate.controler;

import com.yakindu.core.rx.Observer;

public class MyObserverFaceObject implements Observer{
	//attribut IHM
	PolyCreateControler theController;

	//constructeur pour l'IHM
	public MyObserverFaceObject(PolyCreateControler controller) {
		theController = controller;
	}

	@Override
	public void next(Object value) {
		// TODO Auto-generated method stub
		System.out.println("adjusting to face the object");
		theController.faceObject();
		
	}
}
