package model.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.domain.Ricompensa;
import model.domain.RicompenseRiscattate;

public class RicompensaDaoInMemory implements RicompensaDao {

	@Override
	public void registraRicompensaRiscattata(Ricompensa ricompensa) {
		// imposto la data attuale
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = sdf.format(today);
		ricompensa.setDataRiscatto(formattedDate);

		RicompenseRiscattate.getInstance().aggiungiRicompensa(ricompensa);
	}

	@Override
	public List<Ricompensa> getRicompenseByUtente(int idUtente) {
		List<Ricompensa> ricompenseRiscattate = RicompenseRiscattate.getInstance().getRicompenseRiscattate();

		List<Ricompensa> ricompenseUtente = new ArrayList<>();
		for (Ricompensa ricompensa : ricompenseRiscattate) {
			if (ricompensa.getIdUtente() == idUtente) {
				ricompenseUtente.add(ricompensa);
			}
		}
		return ricompenseUtente;
	}

}
