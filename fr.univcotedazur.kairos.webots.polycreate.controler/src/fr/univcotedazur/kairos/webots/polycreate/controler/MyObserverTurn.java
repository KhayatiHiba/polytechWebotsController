package fr.univcotedazur.kairos.webots.polycreate.controler;

import com.yakindu.core.rx.Observer;

public class MyObserverTurn implements Observer {
	
	//attribut IHM
		PolyCreateControler theController;

		//constructeur pour l'IHM
		public MyObserverTurn(PolyCreateControler controller) {
			theController = controller;
		}

		@Override
		public void next(Object value) {
			System.out.println("Turning");
			theController.dodgeObstacle();
			
		}

}

