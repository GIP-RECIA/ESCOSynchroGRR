package fr.recia.grr.batch.writer;


import fr.recia.grr.batch.synchronisation.repository.dao.IUtilisateursRepositoryDAO;
import fr.recia.grr.batch.synchronisation.repository.ldap.personne.IPersonneRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class WriterSuppressionComptesAbsentsLDAP implements ItemWriter<String> {

	private static final Logger log = LoggerFactory.getLogger(WriterSuppressionComptesAbsentsLDAP.class);

	/*
	 * ===============================================
	 * Propriétés de la classe
	 * ===============================================
	 */

	@Autowired
	private IPersonneRepository personneRepository;

	@Autowired
	private IUtilisateursRepositoryDAO personnesServiceDAO;

	/*
	 * ===============================================
	 * Suppression de la personne non present dans ldap
	 * ===============================================
	 */

	@Override
	public void write(List<? extends String> personnes) throws Exception {
		for (String loginPersonne : personnes) {
			log.info("Supression de l'utilisateur en base GrrUtilisateurs : ".concat(loginPersonne));
			personnesServiceDAO.deleteById(loginPersonne);
			log.info("Suppression des réservations de l'utilisateur ".concat(loginPersonne));
			personnesServiceDAO.deleteReservationsUtilisateur(loginPersonne);
			personnesServiceDAO.deleteModerationsUtilisateur(loginPersonne);
			personnesServiceDAO.deleteRepetitionsUtilisateur(loginPersonne);
		}
	}
}