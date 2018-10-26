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

import org.springframework.ldap.core.AttributesMapper;

/**
 * Mapper des données provenant du LDAP vers un objet de type groupeDTO.
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class EntryAttributesMapperGroupe implements AttributesMapper {
    /**
     * Contructeur de la classe. Ne fait rien.
     */
    public EntryAttributesMapperGroupe() {
    }

    /**
     *
     *
     * @param attrs le mapper de groupes.
     *
     * @return le cn du groupe 
     *
     * @throws NamingException .
     */
    public String mapFromAttributes(final Attributes attrs)
    throws NamingException {
        final Attribute cnAtt = attrs.get("cn");
        return cnAtt.get().toString();
        
    }
}

