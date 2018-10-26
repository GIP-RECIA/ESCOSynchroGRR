/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: CommonsLoggingLog.java,v 1.1 2009/07/24 14:14:36 ent_breyton Exp $
 */

package org.crlr.log.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Implémentation de {@link fr.gouv.defense.marine.dpmm.siad.log.Log} basée sur
 * Commons Logging.
 *
 * @author romana
 */
public class CommonsLoggingLog extends AbstractLog implements Serializable {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 9093138019509631678L;

    /** The log. */
    private Log log;

/**
     * Constructeur requis pour la sérialisation. Ne pas utiliser.
     */
    public CommonsLoggingLog() {
        this(LogFactory.getLog(CommonsLoggingLog.class));
    }

/**
     * The Constructor.
     * 
     * @param log
     *            the log
     */
    public CommonsLoggingLog(final org.apache.commons.logging.Log log) {
        super();
        if (log == null) {
            throw new NullPointerException("log est requis");
        }
        this.log = log;
    }

    /**
     * {@inheritDoc}
     */
    public void debug(Throwable cause, String msg, Object... args) {
        if (!log.isDebugEnabled()) {
            return;
        }

        log.debug(formaterMessage(msg, args), cause);
    }

    /**
     * {@inheritDoc}
     */
    public void info(Throwable cause, String msg, Object... args) {
        if (!log.isInfoEnabled()) {
            return;
        }

        log.info(formaterMessage(msg, args), cause);
    }

    /**
     * {@inheritDoc}
     */
    public void warning(Throwable cause, String msg, Object... args) {
        if (!log.isWarnEnabled()) {
            return;
        }

        log.warn(formaterMessage(msg, args), cause);
    }

    /**
     * {@inheritDoc}
     */
    public void error(Throwable cause, String msg, Object... args) {
        if (!log.isErrorEnabled()) {
            return;
        }

        log.error(formaterMessage(msg, args), cause);
    }

    /**
     * Write object.
     *
     * @param out the out
     *
     * @throws IOException the IO exception
     */
    private void writeObject(ObjectOutputStream out)
                      throws IOException {
        out.defaultWriteObject();
        out.writeObject(log);
    }

    /**
     * Read object.
     *
     * @param in the in
     *
     * @throws IOException the IO exception
     * @throws ClassNotFoundException the class not found exception
     */
    private void readObject(ObjectInputStream in)
                     throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        log = (Log) in.readObject();
    }
}
