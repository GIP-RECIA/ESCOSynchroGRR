/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: CommonsLoggingLogFactory.java,v 1.1 2009/07/24 14:14:36 ent_breyton Exp $
 */

package org.crlr.log.impl;

import org.crlr.log.Log;
import org.crlr.log.LogFactory;

/**
 * Implémentation de {@link LogFactory} basée sur Commons Logging.
 *
 * @author romana
 */
public final class CommonsLoggingLogFactory extends LogFactory {
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    protected Log getLogInternal(Class clazz) {
        return new CommonsLoggingLog(org.apache.commons.logging.LogFactory.getLog(clazz));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Log getLogInternal(String nom) {
        return new CommonsLoggingLog(org.apache.commons.logging.LogFactory.getLog(nom));
    }
}
