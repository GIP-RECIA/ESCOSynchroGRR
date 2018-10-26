/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: JdkLogFactory.java,v 1.1 2009/07/24 14:14:36 ent_breyton Exp $
 */

package org.crlr.log.impl;

import org.crlr.log.Log;
import org.crlr.log.LogFactory;

import java.util.logging.Logger;

/**
 * Implémentation de {@link LogFactory} basée sur la classe {@link Logger} de Java.
 *
 * @author romana
 */
public class JdkLogFactory extends LogFactory {
    /**
     * {@inheritDoc}
     */
    @Override
    protected Log getLogInternal(String nom) {
        return new JdkLog(Logger.getLogger(nom));
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    protected Log getLogInternal(Class clazz) {
        return new JdkLog(Logger.getLogger(clazz.getName()));
    }
}
