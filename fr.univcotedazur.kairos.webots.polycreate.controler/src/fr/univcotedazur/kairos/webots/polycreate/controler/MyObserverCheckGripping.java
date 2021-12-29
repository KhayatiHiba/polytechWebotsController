package fr.univcotedazur.kairos.webots.polycreate.controler;

import com.yakindu.core.rx.Observer;

public class MyObserverCheckGripping implements Observer {
	//attribut IHM
	PolyCreateControler theController;

	//constructeur pour l'IHM
	public MyObserverCheckGripping(PolyCreateControler controller) {
		theController = controller;
	}

	@Override
	public void next(Object value) {
		// TODO Auto-generated method stub
		//System.out.println("checking");
		theController.grip();
		
	}
}
