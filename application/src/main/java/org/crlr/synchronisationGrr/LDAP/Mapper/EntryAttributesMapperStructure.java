/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: EntryAttributesMapperStructure.java,v 1.2 2009/09/16 08:46:39 weberent Exp $
 */

package org.crlr.synchronisationGrr.LDAP.Mapper;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.crlr.synchronisationGrr.DTO.EtablissementDTO;
import org.crlr.synchronisationGrr.LDAP.AttributStructure;
import org.crlr.utils.StringUtils;
import org.springframework.ldap.core.AttributesMapper;

/**
 * Mapper des données provenant du LDAP vers un objet de type StructureDTO.
 */
public class EntryAttributesMapperStructure implements AttributesMapper {
	
	AttributStructure attributStructure;
	
    /**
     * Contructeur de la classe. Ne fait rien.
     */
    public EntryAttributesMapperStructure(AttributStructure attributStructure) {
    		this.attributStructure = attributStructure;
    }
    


    /**
     * Mapper des structuresDTO.
     *
     * @param attrs Les attributs a mapper.
     *
     * @return le dto resultat.
     *
     * @throws NamingException .
     */
    public EtablissementDTO mapFromAttributes(final Attributes attrs)
    throws NamingException {
        final Attribute codeAtt = attrs.get(attributStructure.getAttributCode());
        final Attribute nomLongAtt = attrs.get(attributStructure.getAttributNomLong());
        
        final Attribute nomCourtAtt;
        if (StringUtils.isEmpty(attributStructure.getAttributNomCourt())){
        	nomCourtAtt = nomLongAtt;
        } else {
            nomCourtAtt = attrs.get(attributStructure.getAttributNomCourt());
        }
        Attribute villeAtt = null;
        if (!StringUtils.isEmpty(attributStructure.getAttributVille())){
        	villeAtt = attrs.get(attributStructure.getAttributVille());
        }
        Attribute cpAtt = null;
        if (!StringUtils.isEmpty(attributStructure.getAttributCodePostal())){
        	cpAtt = attrs.get(attributStructure.getAttributCodePostal());
        }
        Attribute adresseAtt = null;
        if (!StringUtils.isEmpty(attributStructure.getAttributAdresse())){
        	adresseAtt = attrs.get(attributStructure.getAttributAdresse());
        }
        
        final EtablissementDTO dto = new EtablissementDTO();
        
        if ((codeAtt != null) && !StringUtils.isEmpty(codeAtt.get().toString())) {
            dto.setCode(codeAtt.get().toString());
        }
        
        if ((nomLongAtt != null) && !StringUtils.isEmpty(nomLongAtt.get().toString())) {
            dto.setNomLong(nomLongAtt.get().toString());
        }
        
        if ((nomCourtAtt != null) && !StringUtils.isEmpty(nomCourtAtt.get().toString())) {
            dto.setNomCourt(nomCourtAtt.get().toString());
        } else {
        	dto.setNomCourt(dto.getNomLong());
        }
        
        if ((villeAtt != null) && !StringUtils.isEmpty(villeAtt.get().toString())) {
            dto.setVille(villeAtt.get().toString());
        }
        
        if ((cpAtt != null) && !StringUtils.isEmpty(cpAtt.get().toString())) {
            dto.setCodePostal(cpAtt.get().toString());
        }
        
        if ((adresseAtt != null) && !StringUtils.isEmpty(adresseAtt.get().toString())) {
            dto.setAdresse(adresseAtt.get().toString());
        }
        return dto;
    }
}

