/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.synchronisationGrr.LDAP;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.crlr.synchronisationGrr.DTO.EtablissementDTO;
import org.crlr.synchronisationGrr.DTO.PersonneDTO;
import org.crlr.synchronisationGrr.LDAP.Mapper.EntryAttributesMapperPersonne;
import org.crlr.synchronisationGrr.LDAP.Mapper.EntryAttributesMapperStructure;
import org.springframework.beans.factory.InitializingBean;

/**
 * methodes utilitaires de connection au LDAP .
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class ConnectionLDAP implements InitializingBean{
    private static final Log log = LogFactory.getLog(ConnectionLDAP.class);
    
    /** Url du ldap. */
    private String url;

    /** login du ldap. */
    private String userDn;

    /** Mot de passe du ldap. */
    private String password;

    /** Injection de la base. */
    private String baseDn;

    /** Injection de la branche structure. */
    private String brancheStructure;

    /** Injection de la branche personne. */
    private String branchePersonne;
    
    /** Injection des attributs de structure. */
    private AttributStructure attributStructure;
    
    /** Injection des attributs de structure. */
    private AttributPersonne attributPersonne;
    
    /** Les profils utilisateurs. */
    private String profilUsager;

    /** Les profils utilisateurs. */
    private String profilAdministrateurCentral;

    /** Les profils utilisateurs. */
    private String profilAdministrateurEtablissement;

    
    /**
     * Methode appellée lors de l'initialisation de la classe
     * On traite les paramètre.
     * @throws Exception .
     */
    public void afterPropertiesSet() throws Exception {
    }
    
    /**
     * Le constructeur de la classe. ne fait rien.
     */
    public ConnectionLDAP() {
    }

    /**
     * Recherche la liste des établissements. 
     * @return Une liste de structureDTO.
     */
    public List<EtablissementDTO> findListeEtablissements(){
        
        final List<EtablissementDTO> listeStructures =
            new ArrayList<EtablissementDTO>();
        /** final String filter = "(ObjectClass=ENTEtablissement)"; 
        final String filter = "(ENTStructureJointure=AC-ORLEANS-TOURS$*)"; */
        //Ancien filtre : final String filter = "(&(objectclass=ENTEtablissement)(!(ENTStructureTypeStruct=CFA)))";
        //Pendant la fucion des etablissements :
	//final String filter = "(&(objectclass=ENTEtablissement)(!(ENTStructureTypeStruct=CFA))(|(!(ENTStructureTypeStruct=EF2S))(ENTStructureUAI=0370074E)))";
        //final String filter = "(&(objectclass=ENTEtablissement)(!(ENTStructureTypeStruct=CFA))(|(!(ENTStructureTypeStruct=EF2S))(ENTStructureUAI=0370074E))(!(ENTStructureUAI=0370016S))(!(ENTStructureUAI=0360024F)))";
        final String filter = "(&(objectclass=ENTEtablissement)(!(ENTStructureTypeStruct=CFA))(!(&(ENTStructureTypeStruct=COLLEGE)(ENTStructureUAI=018*)))(|(!(ENTStructureTypeStruct=EF2S))(ENTStructureUAI=0370074E))(!(ENTStructureUAI=0370016S))(!(ENTStructureUAI=0360024F)))";
        
        final List<Attributes> liste =
            LdapUtils.searchWithPaged(url, userDn, password,
                    "ou="+brancheStructure+"," +baseDn, filter);
        
        try {
            for (final Attributes atts : liste) {
                final EntryAttributesMapperStructure mapper =
                    new EntryAttributesMapperStructure(attributStructure);
                listeStructures.add(mapper.mapFromAttributes(atts));
            }
        } catch (NamingException e) {
           throw new RuntimeException("La recherche des structures a échoué.");
        }
        return listeStructures;

    }
    
    /**
     * Recherche la liste des établissements modifiés depuis une date données.
     * @param dateDerniereMiseAJour la date de dernière mise à jour.
     * @return la liste des établissements modifiés.
     */
    public List<EtablissementDTO> findListeEtablissementsModifies(String dateDerniereMiseAJour) {
        final List<EtablissementDTO> listeStructures =
            new ArrayList<EtablissementDTO>();
        
        /** final String filter = "(&(objectClass=ENTEtablissement)(modifyTimestamp>="+dateDerniereMiseAJour+"))"; 
        final String filter = "(&(ENTStructureJointure=AC-ORLEANS-TOURS$*)(modifyTimestamp>="+dateDerniereMiseAJour+"))";   */
        //Ancien filtre : final String filter = "(&(objectclass=ENTEtablissement)(!(ENTStructureTypeStruct=CFA))(modifyTimestamp>="+dateDerniereMiseAJour+"))";
        //Pendant la fucion des etablissements :
        //final String filter = "(&(objectclass=ENTEtablissement)(!(ENTStructureTypeStruct=CFA))(|(!(ENTStructureTypeStruct=EF2S))(ENTStructureUAI=0370074E))(modifyTimestamp>="+dateDerniereMiseAJour+"))";
        //final String filter = "(&(objectclass=ENTEtablissement)(!(ENTStructureTypeStruct=CFA))(|(!(ENTStructureTypeStruct=EF2S))(ENTStructureUAI=0370074E))(!(ENTStructureUAI=0370016S))(!(ENTStructureUAI=0360024F))(modifyTimestamp>="+dateDerniereMiseAJour+"))";
        final String filter = "(&(objectclass=ENTEtablissement)(!(ENTStructureTypeStruct=CFA))(!(&(ENTStructureTypeStruct=COLLEGE)(ENTStructureUAI=018*)))(|(!(ENTStructureTypeStruct=EF2S))(ENTStructureUAI=0370074E))(!(ENTStructureUAI=0370016S))(!(ENTStructureUAI=0360024F))(modifyTimestamp>="+dateDerniereMiseAJour+"))";
        final String branche = "ou="+brancheStructure+"," +baseDn;
        
        log.debug("Requete LDAP : {0};{1};{2};{3};{4}", url, userDn, password, branche, filter );
        final List<Attributes> liste =
            LdapUtils.searchWithPaged(url, userDn, password,
                    "ou="+brancheStructure+"," +baseDn, filter);
        
        try {
            for (final Attributes atts : liste) {
                final EntryAttributesMapperStructure mapper =
                    new EntryAttributesMapperStructure(attributStructure);
                listeStructures.add(mapper.mapFromAttributes(atts));
            }
        } catch (NamingException e) {
           throw new RuntimeException("La recherche des structures a échoué.");
        }
        return listeStructures;
    }
    
    
	public PersonneDTO findPersonne(String uid) {
            final String branche = "ou="+branchePersonne+"," +baseDn;
            final String filter = "uid="+uid;
            log.debug("Requete LDAP : {0};{1};{2};{3};{4}", url, userDn, password, branche, filter );
            
            final List<Attributes> liste =
                LdapUtils.searchWithPaged(url, userDn, password,
                        branche , filter);
            
            try {
                for (final Attributes atts : liste) {
                	final EntryAttributesMapperPersonne mapper =
                			new EntryAttributesMapperPersonne(attributPersonne);
                	return  mapper.mapFromAttributes(atts);
                }
            } catch (NamingException e) {
               throw new RuntimeException("La recherche des personnes a échoué.");
            }
            return null;
	}
    
	
	
    private List<String> findListeUtilisateurs(String filter) {
 
            final String branche = "ou="+branchePersonne+"," +baseDn;
            
            log.debug("Requete LDAP : {0};{1};{2};{3};{4}", url, userDn, password, branche, filter );
            
            final List<String> listePersonnes =
                LdapUtils.searchUIDWithPaged(url, userDn, password,
                        branche , filter);
            
            
            return listePersonnes;
	}
    
    public List<String> findListeUtilisateurs() {
    	return findListeUtilisateurs("(|"+profilUsager+profilAdministrateurCentral+profilAdministrateurEtablissement+")");
    }
    
    
    /**
     * Recherche la liste des personnes modifiés depuis une date données.
     * @param dateDerniereMiseAJour la date de dernière mise à jour.
     * @return la liste des personnes modifiés.
     */
    public List<String> findListeUtilisateursModifies(String dateDerniereMiseAJour) {
        //final String filter = "(|(ESCOUAICourant=0370794M)(ESCOUAICourant=0370878D)";
        final String filter = "(&(modifyTimestamp>="+dateDerniereMiseAJour+")(|"+profilUsager+profilAdministrateurCentral+profilAdministrateurEtablissement+"))";
        final String branche = "ou="+branchePersonne+"," +baseDn;
        
        log.debug("Requete LDAP : {0};{1};{2};{3};{4}", url, userDn, password, branche, filter );
        
        final List<String> listePersonnes =
            LdapUtils.searchUIDWithPaged(url, userDn, password,
                    branche , filter);
        
        
        return listePersonnes;
    }
   
    /**
     * Positionne l'attribut structure.
     *
     * @param brancheStructure brancheStructure.
     */
    public void setBrancheStructure(String brancheStructure) {
        this.brancheStructure = brancheStructure;
    }

    /**
     * Positionne l'attribut personne.
     *
     * @param branchePersonne branchePersonne.
     */
    public void setBranchePersonne(String branchePersonne) {
        this.branchePersonne = branchePersonne;
    }

    /**
     * Mutateur de url.
     * @param url le url à modifier.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Mutateur de userDn.
     * @param userDn le userDn à modifier.
     */
    public void setUserDn(String userDn) {
        this.userDn = userDn;
    }

    /**
     * Mutateur de password.
     * @param password le password à modifier.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Mutateur de baseDn.
     * @param baseDn le baseDn à modifier.
     */
    public void setBaseDn(String baseDn) {
        this.baseDn = baseDn;
    }

    
    
	public void setProfilUsager(String profilUsager) {
		this.profilUsager = profilUsager;
	}

	public void setProfilAdministrateurCentral(String profilAdministrateurCentral) {
		this.profilAdministrateurCentral = profilAdministrateurCentral;
	}

	public void setProfilAdministrateurEtablissement(
			String profilAdministrateurEtablissement) {
		this.profilAdministrateurEtablissement = profilAdministrateurEtablissement;
	}

	public void setAttributStructure(AttributStructure attributStructure) {
		this.attributStructure = attributStructure;
	}

	public void setAttributPersonne(AttributPersonne attributPersonne) {
		this.attributPersonne = attributPersonne;
	}

	
	
}
