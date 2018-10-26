/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: Log4jLogFactory.java,v 1.1 2009/07/24 14:14:36 ent_breyton Exp $
 */

package org.crlr.log.impl;

import org.apache.log4j.Logger;

import org.crlr.log.Log;
import org.crlr.log.LogFactory;

/**
 * Implémentation de {@link LogFactory} basée sur Log4j.
 *
 * @author romana
 */
public class Log4jLogFactory extends LogFactory {
    /**
     * {@inheritDoc}
     */
    @Override
    protected Log getLogInternal(String nom) {
        return new Log4jLog(Logger.getLogger(nom));
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    protected Log getLogInternal(Class clazz) {
        return new Log4jLog(Logger.getLogger(clazz));
    }
}
