package fr.univcotedazur.kairos.webots.polycreate.controler;

import com.yakindu.core.rx.Observer;

public class MyObserverDodgeObject implements Observer {

//attribut IHM
	PolyCreateControler theController;
	
	//constructeur pour l'IHM
	public MyObserverDodgeObject(PolyCreateControler controller) {
		theController = controller;
	}
	
	@Override
	public void next(Object value) {
		// TODO Auto-generated method stub
		System.out.println("I saw an Object");
		theController.dodgeObject();
		
	}
}