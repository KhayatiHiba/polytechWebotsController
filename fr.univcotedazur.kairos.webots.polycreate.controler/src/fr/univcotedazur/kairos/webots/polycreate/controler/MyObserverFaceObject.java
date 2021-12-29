package fr.univcotedazur.kairos.webots.polycreate.controler;

import com.yakindu.core.rx.Observer;

public class MyObserverFaceObject implements Observer{
	PolyCreateControler theController;

	public MyObserverFaceObject(PolyCreateControler controller) {
		theController = controller;
	}

	@Override
	public void next(Object value) {
		System.out.println("Object detected \n");
		theController.faceObject();
		
	}
}
