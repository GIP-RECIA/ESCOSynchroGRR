/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: InterfacePropagationCahierTexte.java,v 1.1 2009/07/24 14:20:00 ent_breyton Exp $
 */

package org.crlr.synchronisationGrr.DB;

import java.util.List;

import org.crlr.exception.base.CrlrException;
import org.crlr.synchronisationGrr.DTO.DataEtablissementDTO;
import org.crlr.synchronisationGrr.DTO.EtablissementDTO;
import org.crlr.synchronisationGrr.DTO.PersonneDTO;


/**
 * Interface de synchronisation des données provenant de l'annuaire LDAP vers la BDD de GRR.
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public interface InterfacePropagationGrr {

    /**
     * Synchronise les modifications des etablissements.
     * @param etablissement l'établissement qui a été modifié.
     * @throws CrlrException .
     */
    void propageStructureModification(EtablissementDTO etablissement) throws CrlrException;

    /**
     * Synchronise les modifications des personnes.
     * @param personne la personne qui a été modifié.
     * @param profil 
     * @throws CrlrException .
     */
    void propagePersonneModification(PersonneDTO personne) throws CrlrException;
    
    /**
     * Recherche la liste des codes des établissements (pour la migration).
     * @return la liste des codes des établissements.
     */
	List<String> findListeCodesEtablissements();

	/**
	 * Propage dans la BDD cible les informations des autres BDD.
	 * @param uai le code de l'établissement.
	 * @param datasEtablissement les données à migrer
	 */
	void propageMigration(String uai, DataEtablissementDTO datasEtablissement) throws CrlrException;

}
