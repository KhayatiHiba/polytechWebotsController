package fr.univcotedazur.kairos.webots.polycreate.controler;

import com.yakindu.core.rx.Observer;

public class MyObserverFullTurn implements Observer<Void> {

	PolyCreateControler theController;

	//constructeur pour l'IHM
	public MyObserverFullTurn(PolyCreateControler controller) {
		theController = controller;
	}
	
	@Override
	public void next(Void value) {
		System.out.println("Turning at 180°");
		theController.fullTurn();

	}

}
