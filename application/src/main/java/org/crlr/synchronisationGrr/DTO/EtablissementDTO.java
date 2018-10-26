/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: StructureDTO.java,v 1.2 2009/09/16 08:46:39 weberent Exp $
 */

package org.crlr.synchronisationGrr.DTO;

import org.crlr.utils.StringUtils;


/**
 * DTO d'une structure provenant du LDAP.
 */
public class EtablissementDTO {

    /** Le code identifiant dans GRR. */
    private String code;

    private String nomCourt;
    
    private String nomLong;
    
    private String codePostal;

    private String ville;
    
    private String adresse;

    ////////////////////////////////////////////////
    // MODIFICATION RECIA | DEBUT | 2012-04-17
    ////////////////////////////////////////////////
    private String uaiPrincipal; 
    ////////////////////////////////////////////////
    // MODIFICATION RECIA | FIN
    ////////////////////////////////////////////////
    
    public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}

	public String getNomCourt() {
		return StringUtils.isEmpty(nomCourt) ? nomLong : nomCourt;
	}


	public void setNomCourt(String nomCourt) {
		this.nomCourt = nomCourt;
	}


	public String getNomLong() {
		return nomLong;
	}


	public void setNomLong(String nomLong) {
		this.nomLong = nomLong;
	}


	public String getCodePostal() {
		return StringUtils.isEmpty(codePostal) ? "" : codePostal;
	}


	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}


	public String getVille() {
		return StringUtils.isEmpty(ville) ? "" : ville;
	}


	public void setVille(String ville) {
		this.ville = ville;
	}


	public String getAdresse() {
		return StringUtils.isEmpty(adresse) ? "" : adresse;
	}


	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

    ////////////////////////////////////////////////
    // MODIFICATION RECIA | DEBUT | 2012-04-17
    ////////////////////////////////////////////////
	public String getUaiPrincipal() {
		return StringUtils.isEmpty(uaiPrincipal) ? "" : uaiPrincipal;
	}


	public void setUaiPrincipal(String uaiPrincipal) {
		this.uaiPrincipal = uaiPrincipal;
	}
    ////////////////////////////////////////////////
    // MODIFICATION RECIA | FIN
    ////////////////////////////////////////////////

	/**
     * Constructeur par defaut.
     */
    public EtablissementDTO() {
    }


        /**
     * Methode d'affichage.
     * @return .
     */
    public String toString(){
        final StringBuilder sb = new StringBuilder();
        sb.append("Etablissement[");
        sb.append(code);
        sb.append(",");
        sb.append(nomLong);
        sb.append("]");
        return sb.toString();
    }

    
}
