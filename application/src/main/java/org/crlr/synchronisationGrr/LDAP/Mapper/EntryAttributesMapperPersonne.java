/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.synchronisationGrr.LDAP.Mapper;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.crlr.synchronisationGrr.DTO.PersonneDTO;
import org.crlr.synchronisationGrr.DTO.Profil;
import org.crlr.synchronisationGrr.LDAP.AttributPersonne;
import org.crlr.utils.BooleanUtils;
import org.crlr.utils.StringUtils;
import org.springframework.ldap.core.AttributesMapper;

/**
 * Mapper des données provenant du LDAP vers un objet de type PersonneDTO.
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class EntryAttributesMapperPersonne implements AttributesMapper {

	private AttributPersonne attributPersonne;

    public EntryAttributesMapperPersonne(AttributPersonne attributPersonne) {
        this.attributPersonne = attributPersonne;
    }

    /**
     * Mapper des personnes.
     *
     * @param attrs Les attributs du mapper.
     *
     * @return la personneDTO.
     *
     * @throws NamingException .
     */
    public PersonneDTO mapFromAttributes(final Attributes attrs)
                                  throws NamingException {
    	final Attribute codeAtt = attrs.get(attributPersonne.getAttributCode());
        final Attribute nomAtt = attrs.get(attributPersonne.getAttributNom());
        final Attribute prenomAtt = attrs.get(attributPersonne.getAttributPrenom());
        final Attribute emailAtt = attrs.get(attributPersonne.getAttributEmail());
        
        final Attribute administrateurAtt = attrs.get(attributPersonne.getAttributAdministrateur());
        final String regexpAdministrateur = attributPersonne.getRegexpAdministrateur();
        
        final Attribute administrateurEtablissementAtt = attrs.get(attributPersonne.getAttributAdministrateurEtablissement());
        final String regexpAdministrateurEtablissement = attributPersonne.getRegexpAdministrateurEtablissement();
        
        final Attribute etablissmentsAtt = attrs.get(attributPersonne.getAttributEtablissement());
        final String regexpEtablissement = attributPersonne.getRegexpEtablissement();
        
        final Attribute etablissmentPrincipalAtt = attrs.get(attributPersonne.getAttributEtablissementPrincipal());
        
       
        
        final PersonneDTO dto = new PersonneDTO();
        
        if ((codeAtt != null) && !StringUtils.isEmpty(codeAtt.get().toString())) {
            dto.setUid(codeAtt.get().toString());
        }
        
        if ((nomAtt != null) && !StringUtils.isEmpty(nomAtt.get().toString())) {
            dto.setNom(StringUtils.truncate(nomAtt.get().toString(), 30));
        }
        
        if ((prenomAtt != null) && !StringUtils.isEmpty(prenomAtt.get().toString())) {
            dto.setPrenom(StringUtils.truncate(prenomAtt.get().toString(),30));
        }

        if ((emailAtt != null) && !StringUtils.isEmpty(emailAtt.get().toString())) {
            dto.setEmail(emailAtt.get().toString());
        } else {
        	dto.setEmail(attributPersonne.getDefaultEmail());
        }
        
        if ((etablissmentsAtt != null) ) {
        	@SuppressWarnings("unchecked")
        	final NamingEnumeration<String> attributsEtablissement =
                    (NamingEnumeration<String>) etablissmentsAtt.getAll();
        	while (attributsEtablissement.hasMore()) {
        		final String attributEtablissement = attributsEtablissement.next().toString();
        		if ( attributEtablissement.matches(regexpEtablissement)){
        			int indexStart = attributEtablissement.indexOf(attributPersonne.getStartCodeEtablissement()) + attributPersonne.getStartCodeEtablissement().length();
        			if (indexStart > -1) {
        				if (! StringUtils.isEmpty(attributPersonne.getStopCodeEtablissement())){
	        				int indexStop = attributEtablissement.indexOf(attributPersonne.getStopCodeEtablissement(),indexStart);
		        			if (indexStop > -1) {
		        				String code = attributEtablissement.substring(indexStart,indexStop);
			        			dto.getEtablissements().add(code);
		        			}
        				} else {
        					String code = attributEtablissement.substring(indexStart);
		        			dto.getEtablissements().add(code);
        				}

        			}
        		}
        	}
        	
        }
        
        Profil profil = Profil.Usager;
        
        if ((administrateurEtablissementAtt != null) ) {
        	@SuppressWarnings("unchecked")
			final NamingEnumeration<String> attributsAdministrateurEtablissement =
                    (NamingEnumeration<String>) administrateurEtablissementAtt.getAll();
        	while (attributsAdministrateurEtablissement.hasMore()) {
        		final String attributAdministrateurEtablissement = attributsAdministrateurEtablissement.next().toString();
        		if ( attributAdministrateurEtablissement.matches(regexpAdministrateurEtablissement)){
        			profil = Profil.AdministrateurEtablissement;
        			int indexStart = attributAdministrateurEtablissement.indexOf(attributPersonne.getStartCodeAdminEtablissement()) + attributPersonne.getStartCodeAdminEtablissement().length();
        			if (indexStart > -1) {
        				if (! StringUtils.isEmpty(attributPersonne.getStopCodeAdminEtablissement())){
	        				int indexStop = attributAdministrateurEtablissement.indexOf(attributPersonne.getStopCodeAdminEtablissement(),indexStart);
		        			if (indexStop > -1) {
		        				String code = attributAdministrateurEtablissement.substring(indexStart,indexStop);
			        			dto.getEtablissementsAdmin().add(code);
		        			}
        				} else {
        					String code = attributAdministrateurEtablissement.substring(indexStart);
		        			dto.getEtablissementsAdmin().add(code);
        				}
        			}
        		}
        	}
        	
        }
        
        
        if ((administrateurAtt != null) ) {
        	@SuppressWarnings("unchecked")
        	final NamingEnumeration<String> attributsAdministrateur =
                    (NamingEnumeration<String>) administrateurAtt.getAll();
        	while (attributsAdministrateur.hasMore()) {
        		final String attributAdministrateur = attributsAdministrateur.next().toString();
        		if ( attributAdministrateur.matches(regexpAdministrateur)){
        			profil = Profil.Administrateur;
        			break;
        		}
        	}
        	
        }
        
        if ((etablissmentPrincipalAtt != null) && !StringUtils.isEmpty(etablissmentPrincipalAtt.get().toString())) {
            String attributEtablissementPrincipal = etablissmentPrincipalAtt.get().toString();
            if (BooleanUtils.isTrue(attributPersonne.getUseStartStopCodeEtabPrincipal())){
	            int indexStart = attributEtablissementPrincipal.indexOf(attributPersonne.getStartCodeEtablissement()) + attributPersonne.getStartCodeEtablissement().length();
				if (indexStart > -1) {
					if (! StringUtils.isEmpty(attributPersonne.getStopCodeEtablissement())){
		    			int indexStop = attributEtablissementPrincipal.indexOf(attributPersonne.getStopCodeEtablissement(),indexStart);
		    			if (indexStop > -1) {
		    				String code = attributEtablissementPrincipal.substring(indexStart,indexStop);
		        			dto.setCodeEtablissementPrincipal(code);
		    			}
					} else {
						String code = attributEtablissementPrincipal.substring(indexStart);
	        			dto.setCodeEtablissementPrincipal(code);
					}
				}
            } else {
            	dto.setCodeEtablissementPrincipal(attributEtablissementPrincipal);
            }
        }
        
        
        dto.setProfil(profil);
        return dto;
    }

   
}
