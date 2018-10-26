/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: LogFactory.java,v 1.1 2009/07/24 14:14:36 ent_breyton Exp $
 */

package org.crlr.log;

import org.crlr.log.impl.CommonsLoggingLogFactory;
import org.crlr.log.impl.JdkLogFactory;
import org.crlr.log.impl.Log4jLogFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Fabrique d'objets {@link Log}. Pour obtenir une instance {@link Log}, appelez la
 * méthode {@link #getLog(Class)} ou la méthode {@link #getLog(String)}.<p>Exemple :
 * <code>Log log = LogFactory.getLog(UneClasse.class);</code>.</p>
 *
 * @author romana
 */
public abstract class LogFactory {
    /** The log factory impl. */
    private static LogFactory logFactoryImpl;

    static {
        final Map<String, Class<?extends LogFactory>> logFactories;
        logFactories = new LinkedHashMap<String, Class<?extends LogFactory>>();
        logFactories.put("org.apache.commons.logging.Log", CommonsLoggingLogFactory.class);
        logFactories.put("org.apache.log4j.Logger", Log4jLogFactory.class);

        for (final Map.Entry<String, Class<?extends LogFactory>> entry : logFactories.entrySet()) {
            try {
                Class.forName(entry.getKey());
                logFactoryImpl = entry.getValue().newInstance();
            } catch (Exception e) {
                continue;
            }
        }

        if (logFactoryImpl == null) {
            logFactoryImpl = new JdkLogFactory();
        }
    }

/**
     * The Constructor.
     */
    protected LogFactory() {
    }

    /**
     * Retourne un {@link Log} à partir d'un nom.
     *
     * @param nom the nom
     *
     * @return the log
     */
    public static Log getLog(String nom) {
        return logFactoryImpl.getLogInternal(nom);
    }

    /**
     * Retourne un {@link Log} à partir d'une classe.
     *
     * @param clazz the clazz
     *
     * @return the log
     */
    @SuppressWarnings("unchecked")
    public static Log getLog(Class clazz) {
        return logFactoryImpl.getLogInternal(clazz);
    }

    /**
     * Une implémentation concrète fournit une instance de {@link Log}.
     *
     * @param nom the nom
     *
     * @return the log internal
     */
    protected abstract Log getLogInternal(String nom);

    /**
     * Une implémentation concrète fournit une instance de {@link Log}.
     *
     * @param clazz the clazz
     *
     * @return the log internal
     */
    @SuppressWarnings("unchecked")
    protected abstract Log getLogInternal(Class clazz);
}
