/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.synchronisationGrr.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.crlr.synchronisationGrr.DTO.EtablissementDTO;
import org.crlr.synchronisationGrr.LDAP.ConnectionLDAP;

/**
 * Liste des structures pour optimiser les recherches uai/sirens .
 */
public final class ListeStructures {
    /**
     * Une map qui associe un UAI à la structure associée.
     */
    private static Map<String, EtablissementDTO> mapUAI ;
    /**
     * Une map qui associe un siren à la structure associée.
     */
    private static Map<String, EtablissementDTO> mapSiren ;
    
    /**
     * Constructure privé.
     */
    private ListeStructures(){
    }
    
    /**
     * Initialisation des structures de données.
     */
    private static void initMap(){
        final List<EtablissementDTO> structures = connectionLDAP.findListeEtablissements();
        mapUAI = new HashMap<String, EtablissementDTO>();
        mapSiren = new HashMap<String, EtablissementDTO>();
        for (EtablissementDTO structure : structures){
         /*   mapUAI.put(structure.getUai(), structure);
            mapSiren.put(structure.getSiren(), structure); */
        }
    }
    
    /**
     * Accesseur à la mapUAI.
     * @return la mapUAI intialisée.
     */
    private static Map<String, EtablissementDTO> getMapUAI(){
        if (mapUAI == null){
            initMap();
        }
        return mapUAI;
    }
    
    /**
     * Accesseur à la mapSiren.
     * @return la mapUAI intialisée.
     */
    private static Map<String, EtablissementDTO> getMapSiren(){
        if (mapSiren == null){
            initMap();
        }
        return mapSiren;
    }

    /**
     * Recherche une structure à partir de son SIREN.
     * @param siren le siren de la structure a retrouver.
     * @return la structure complète.
     */
    public static EtablissementDTO getStructureBySiren(String siren) {
        return getMapSiren().get(siren);
    }
    
    /**
     * Recherche une structure à partir de son SIREN.
     * @param uai l'uai de la structure a retrouver.
     * @return la structure complète.
     */
    public static EtablissementDTO getStructureByUai(String uai) {
        return getMapUAI().get(uai);
    }
    
    
    
    /** La connection au LDAP. */
    public static ConnectionLDAP connectionLDAP;

    /**
     * Setter de connectionLDAP.
     *
     * @param connectionLDAPParam the connectionLDAP to set
     */
    public void setConnectionLDAP(ConnectionLDAP connectionLDAPParam) {
        connectionLDAP = connectionLDAPParam;
    }


    
}
