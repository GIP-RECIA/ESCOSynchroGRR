/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.synchronisationGrr;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import org.crlr.exception.base.CrlrException;
import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.crlr.synchronisationGrr.DB.InterfacePropagationGrr;
import org.crlr.synchronisationGrr.DTO.EtablissementDTO;
import org.crlr.synchronisationGrr.DTO.PersonneDTO;
import org.crlr.synchronisationGrr.LDAP.ConnectionLDAP;
import org.crlr.synchronisationGrr.Utils.FileUtils;
import org.crlr.utils.PropertiesUtils;
import org.crlr.utils.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Application de propagation des modifications de l'annuaire LDAP à la base de données du cahier de texte.
 *
 * @author aurore.weber
 * @version $Revision: 1.2 $
 */
public final class Synchronisation {
    /** Fichier de log pour les cas d'erreur. */
    private static final Log log = LogFactory.getLog(Synchronisation.class);

    
    
    /**
     * Constructeur.
     */
    private Synchronisation() {
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
        final ConnectionLDAP connectionLDAP = (ConnectionLDAP) applicationContext.getBean("ConnectionLDAP");
        final FileUtils fileUtils = (FileUtils) applicationContext.getBean("FileUtils");

        
        final String dateDerniereMiseAJour = fileUtils.getDateDerniereModification();
        log.debug("Date de dernière mise à jour {0}", dateDerniereMiseAJour);
        final String pattern = "yyyyMMddHHmmss";
        final String nouvelleDateDerniereMiseAJour = 
            new SimpleDateFormat(pattern).format(Calendar.getInstance().getTime())+"Z"; 
        log.debug("Nouvelle date de mise à jour {0}", nouvelleDateDerniereMiseAJour);


        log.info("Recherche des établissements modifiées");
        final List<EtablissementDTO> etablissements = connectionLDAP.findListeEtablissementsModifies(dateDerniereMiseAJour);
        log.info("Nombre de établissements modifiées {0}", etablissements.size());
        
        ////////////////////////////////////////////////
        // MODIFICATION RECIA | DEBUT | 2012-04-17
        ////////////////////////////////////////////////
        Properties regroupement = PropertiesUtils.load("/regroupementEtablissements.properties"); 
        ////////////////////////////////////////////////
        // MODIFICATION RECIA | FIN
        ////////////////////////////////////////////////

        //Traitement des entrées.
        for (EtablissementDTO etablissement : etablissements){
            try {
                ////////////////////////////////////////////////
                // MODIFICATION RECIA | DEBUT | 2012-04-17
                ////////////////////////////////////////////////
                String uai = etablissement.getCode();
	           	String uaiPrincipal = regroupement.getProperty(uai+".principal");
	            if (! StringUtils.isEmpty(uaiPrincipal)){
	            	etablissement.setUaiPrincipal(uaiPrincipal);
	            }
                ////////////////////////////////////////////////
                // MODIFICATION RECIA | FIN
                ////////////////////////////////////////////////
            	grr.propageStructureModification(etablissement);
            } catch (CrlrException e) {
            	log.error("impossible de propager l'établissement : " +
                        etablissement +
                " dans la BDD");
                log.error(e.getMessage());
                //La propagation des établissements est vitale pour s'assurer que 
                //les personnes pourront ensuite être inscrites proprement dans leurs établissements.
                return;
            }
        }

        log.info("Recherche des personnes modifiées");
        final List<String> uids = connectionLDAP.findListeUtilisateursModifies(dateDerniereMiseAJour);
        log.info("Nombre de personnes modifiées {0}", uids.size());
        
        //Traitement des entrées.
        for (String uid : uids){
            try {
            	PersonneDTO personneDTO = connectionLDAP.findPersonne(uid);
                log.debug("Traitement de la personne {0} , profil {1}", personneDTO.getUid(), personneDTO.getProfil().name());
                grr.propagePersonneModification(personneDTO);
            } catch (CrlrException e) {
                log.error("impossible de propager la personne : " +
                		uid +
                " dans la BDD");
                log.error(e.getMessage());
            }
        }
        
        log.info("Ecriture de la nouvelle date de modification");
        fileUtils.setDateDerniereModification(nouvelleDateDerniereMiseAJour);
        
        log.info("Traitement finalisé");
    }

}
