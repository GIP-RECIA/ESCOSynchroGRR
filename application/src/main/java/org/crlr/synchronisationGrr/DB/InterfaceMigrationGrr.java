/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: InterfacePropagationCahierTexte.java,v 1.1 2009/07/24 14:20:00 ent_breyton Exp $
 */

package org.crlr.synchronisationGrr.DB;

import java.sql.SQLException;

import org.crlr.synchronisationGrr.DTO.DataEtablissementDTO;


/**
 * Interface de synchronisation des données provenant de l'annuaire LDAP vers la BDD de GRR.
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public interface InterfaceMigrationGrr {

   Boolean openConnection(String nomBDD);
   Boolean closeConnection();
   
   DataEtablissementDTO findDataEtablissement(Boolean isInCommonDB,String uai) throws SQLException ; 

}
