package fr.recia.grr.batch.processor;


import fr.recia.grr.batch.synchronisation.entity.ldap.ODMPersonne;
import fr.recia.grr.batch.synchronisation.repository.ldap.personne.IPersonneRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProcessorSuppressionComptesAbsentsLDAP implements ItemProcessor<String,String> {

	private static final Logger log = LoggerFactory.getLogger(ProcessorSuppressionComptesAbsentsLDAP.class);

	/*
	 * ===============================================
	 * Propriétés de la classe
	 * ===============================================
	 */

	@Autowired
	private IPersonneRepository personneRepository;

	private Set<String> personnesUID;


	/*
	 * ===============================================
	 * Suppression de la personne non present dans ldap
	 * ===============================================
	 */



	@Override
	public String process(String loginPersonne) throws Exception {

		log.info("Debut du Processor SuppressionComptesAbsentsLDAP");

		if(personnesUID == null){
			personnesUID = new HashSet<>();
			log.info("Récupération de la liste de toutes les personnes du LDAP");
			List<ODMPersonne> personnes = personneRepository.findAll();
			for(ODMPersonne personne : personnes){
				personnesUID.add(personne.getUid());
			}
		}

		log.info("Recherche si {} est present dans le ldap",loginPersonne);
		if(personnesUID.contains(loginPersonne)){
			return null;
		}
		return loginPersonne;

	}

}