package fr.univcotedazur.kairos.webots.polycreate.controler;

import com.yakindu.core.rx.Observer;

public class MyObserverGrip implements Observer {
	//attribut IHM
			PolyCreateControler theController;

			//constructeur pour l'IHM
			public MyObserverGrip(PolyCreateControler controller) {
				theController = controller;
			}

			@Override
			public void next(Object value) {
				System.out.println("Gripping the object\n");
				theController.grip();
				
			}
}
