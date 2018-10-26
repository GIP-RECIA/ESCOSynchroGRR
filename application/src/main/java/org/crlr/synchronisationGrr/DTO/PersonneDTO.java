/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: PersonneDTO.java,v 1.2 2009/09/16 08:46:39 weberent Exp $
 */

package org.crlr.synchronisationGrr.DTO;

import java.util.HashSet;
import java.util.Set;



/**
 * DTO d'une personne provenant du LDAP.
 */
public class PersonneDTO {
    /** uid. */
    private String uid;

    /** surname. */
    private String nom;
    
    /** surname. */
    private String prenom;

    /** surname. */
    private String email;

    private Profil profil;
    
    private Set<String> etablissementsAdmin = new HashSet<String>(); 
    private Set<String> etablissements = new HashSet<String>(); 
    
    private String codeEtablissementPrincipal;
    
    /**
     * Constructeur par defaut.
     */
    public PersonneDTO() {
    }



	public String getUid() {
		return uid;
	}



	public void setUid(String uid) {
		this.uid = uid;
	}



	public String getNom() {
		return nom;
	}



	public void setNom(String nom) {
		this.nom = nom;
	}



	public String getPrenom() {
		return prenom;
	}



	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public Profil getProfil() {
		return profil;
	}



	public void setProfil(Profil profil) {
		this.profil = profil;
	}




	


	public Set<String> getEtablissementsAdmin() {
		return etablissementsAdmin;
	}



	public void setEtablissementsAdmin(Set<String> etablissementsAdmin) {
		this.etablissementsAdmin = etablissementsAdmin;
	}



	public Set<String> getEtablissements() {
		return etablissements;
	}



	public void setEtablissements(Set<String> etablissements) {
		this.etablissements = etablissements;
	}



	public String getCodeEtablissementPrincipal() {
		return codeEtablissementPrincipal;
	}



	public void setCodeEtablissementPrincipal(String codeEtablissementPrincipal) {
		this.codeEtablissementPrincipal = codeEtablissementPrincipal;
	}


    
    

    
    
}
