/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.synchronisationGrr;

import java.util.List;
import java.util.Properties;

import org.crlr.exception.base.CrlrException;
import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.crlr.synchronisationGrr.DB.InterfaceMigrationGrr;
import org.crlr.synchronisationGrr.DB.InterfacePropagationGrr;
import org.crlr.synchronisationGrr.DTO.DataEtablissementDTO;
import org.crlr.utils.PropertiesUtils;
import org.crlr.utils.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;



/**
 * Application d'alimentation de la BDD de GRR depuis un annuaire LDAP.
 *
 * @author aurore.weber
 * @version $Revision: 1.2 $
 */
public final class Migration {
    /** Fichier de log pour les cas d'erreur. */
    private static final Log log = LogFactory.getLog(Migration.class);

    
    
    /**
     * Constructeur.
     */
    private Migration() {
    }

    /**
     * propagation des modifications de l'annuaire LDAP à la base de données du cahier de texte.
     *
     * @param args pas de parametres
     * 
     * @throws Exception .
     */
    public static void main(String[] args) throws Exception  {
        // Instanciation du contexte Spring à partir d'un fichier XML dans le classpath
        final ApplicationContext applicationContext =
            new ClassPathXmlApplicationContext("spring-dao.xml");
        
        final InterfacePropagationGrr grr  = (InterfacePropagationGrr) applicationContext.getBean("PropagationGrr");
        final InterfaceMigrationGrr migrationGrr  = (InterfaceMigrationGrr) applicationContext.getBean("MigrationGrr");
        
        Properties correspondance = PropertiesUtils.load("/correspondanceBDUAI.properties");
        
        log.info("Recherche des établissements à inserer");
        final List<String> etablissements = grr.findListeCodesEtablissements();
        log.info("Nombre de établissements à inserer {0}", etablissements.size());
        
        //Traitement des entrées.
        for (String uai : etablissements){
        	log.debug("Etablissement {0}", uai);
        	
            try {
            	
            	String nomBDD = correspondance.getProperty(uai);
            	if (StringUtils.isEmpty(nomBDD)){
            		log.error("Pas de BDD associée à l'UAI "+uai);
            	} else {
	            	String typeBDD = correspondance.getProperty(uai+".multi");
	            	Boolean isInCommonDB = false; 
	            	if (! StringUtils.isEmpty(typeBDD)){
	            		isInCommonDB = true;
	            	}
	            	log.info("Migration de "+uai+" --> " +nomBDD);
	            	if (migrationGrr.openConnection(nomBDD)) {
		            	DataEtablissementDTO datasEtablissement = migrationGrr.findDataEtablissement(isInCommonDB, uai);
		            	migrationGrr.closeConnection();
		            	
		            	grr.propageMigration(uai, datasEtablissement);
		            	migrationGrr.closeConnection();
	            	} else {
	            		log.error("Impossible de se connecter à la BDD "+nomBDD);
	            	}
            	}
            	
            } catch (CrlrException e) {
                log.error("impossible de propager l'établissement : " +
                        uai +
                " dans la BDD");
                log.error(e.getMessage());
                //La propagation des établissements est vitale pour s'assurer que 
                //les personnes pourront ensuite être inscrites proprement dans leurs établissements.
                return;
            }
        }

        
        log.info("Traitement finalisé");
    }

}
