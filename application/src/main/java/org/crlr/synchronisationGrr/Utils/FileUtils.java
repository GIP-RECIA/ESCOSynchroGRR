/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.synchronisationGrr.Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.crlr.utils.StringUtils;

/**
 * Gestion du fichier pour conserver la date de dernière modification.
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class FileUtils {
    private static final Log log = LogFactory.getLog(FileUtils.class);
    /**
     * Le chemin vers le fichier contenant la date de dernière modification.
     * Injecté via spring.
     */
    private String fichierDateDerniereModification;

    /**
     * Lit la date de dernière modification dans le fichier contenant la date de dernière modification.
     * @return la date de dernière modification. 
     * @throws Exception Arret du processus si on ne sait pas lire la date.
     */
    public String getDateDerniereModification() throws Exception {
        log.debug("Fichier de modification : {0}", fichierDateDerniereModification);
        final File f = new File(fichierDateDerniereModification);
        
        try {
            final BufferedReader inputFileTemp =
                new BufferedReader(new FileReader(f));
            final String myLine = inputFileTemp.readLine();
            inputFileTemp.close();
            if (StringUtils.isEmpty(myLine)){
                log.error("Impossible de lire le fichier de gestion des dates de dernieres modifications : " + fichierDateDerniereModification);
                throw new Exception("Arret preventif de la synchronisation.");
            }
            return myLine;
        } catch (Exception e) {
            log.error("Impossible de lire le fichier de gestion des dates de dernieres modifications.");
            throw new Exception("Arret preventif de la synchronisation.");
        }
    }
    
    /**
     * Ecrit la date de dernière modification dans le fichier contenant la date de dernière modification.
     * @param dateDerniereModification la date de dernière modification. 
     * @throws Exception Arret du processus si on ne sait pas ecrire la date.
     */
    public void setDateDerniereModification(String dateDerniereModification) throws Exception {
        log.debug("Fichier de modification : {0}", fichierDateDerniereModification);
        final File f = new File(fichierDateDerniereModification);

        try {
            final BufferedWriter outputFileTemp =
                new BufferedWriter(new FileWriter(f));
            outputFileTemp.write(dateDerniereModification);
            outputFileTemp.flush();
            outputFileTemp.close();
        } catch (Exception e) {
            log.error("Impossible d'écrire le fichier de gestion des dates de dernieres modifications : "+dateDerniereModification);
            throw new Exception("Arret preventif de la synchronisation.");
        }
    }

    /**
     * Mutateur de fichierDateDerniereModification.
     * @param fichierDateDerniereModification le fichierDateDerniereModification à modifier.
     */
    public void setFichierDateDerniereModification(
            String fichierDateDerniereModification) {
        this.fichierDateDerniereModification = fichierDateDerniereModification;
    }
    
    
}
