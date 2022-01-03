package fr.univcotedazur.kairos.webots.polycreate.controler;

import com.yakindu.core.rx.Observer;

public class MyObserverStore implements Observer<Void> {
	PolyCreateControler theController;

	public MyObserverStore(PolyCreateControler controller) {
		theController = controller;
	}
	@Override
	public void next(Void value) {
		// TODO Auto-generated method stub
		System.out.println("Je suis dans le store");
		theController.store();
	}

}
