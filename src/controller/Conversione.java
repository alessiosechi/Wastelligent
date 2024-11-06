package controller;

import java.util.ArrayList;
import java.util.List;

import model.domain.OperatoreEcologico;
import model.domain.UtenteBean;


public class Conversione {

	public List<UtenteBean> convertOperatoriEcologiciListToBeanList(List<OperatoreEcologico> operatoriEcologici) {
		if (operatoriEcologici != null) {
			List<UtenteBean> operatoriBeanList = new ArrayList<>();

			for (OperatoreEcologico operatore : operatoriEcologici) {
				UtenteBean bean = new UtenteBean();
				bean.setIdUtente(operatore.getIdUtente());
				bean.setUsername(operatore.getUsername());
				operatoriBeanList.add(bean);
			}

			return operatoriBeanList;
		} else {
			return new ArrayList<>();
		}
	}
}
