package fr.univcotedazur.kairos.webots.polycreate.controler;

import com.yakindu.core.rx.Observer;

public class MyObserverSituateSelf implements Observer<Void> {

	PolyCreateControler theController;

	//constructeur pour l'IHM
	public MyObserverSituateSelf(PolyCreateControler controller) {
		theController = controller;
	}
	@Override
	public void next(Void value) {
		System.out.println("Situating robot to grab object \n");
		theController.situateSelf();

	}

}
