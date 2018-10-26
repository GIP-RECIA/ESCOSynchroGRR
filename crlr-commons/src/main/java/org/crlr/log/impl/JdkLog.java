/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: JdkLog.java,v 1.1 2009/07/24 14:14:36 ent_breyton Exp $
 */

package org.crlr.log.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implémentation de {@link fr.gouv.defense.marine.dpmm.siad.log.Log} basée sur la
 * classe {@link Logger} de Java.
 *
 * @author romana
 */
public class JdkLog extends AbstractLog implements Serializable {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 9044859155055157095L;

    /** The log. */
    private Logger log;

/**
     * Constructeur requis pour la sérialisation. Ne pas utiliser.
     */
    public JdkLog() {
        this(Logger.getLogger(JdkLog.class.getName()));
    }

/**
     * The Constructor.
     * 
     * @param log
     *            the log
     */
    public JdkLog(final Logger log) {
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
        log(Level.FINE, cause, msg, args);
    }

    /**
     * {@inheritDoc}
     */
    public void info(Throwable cause, String msg, Object... args) {
        log(Level.INFO, cause, msg, args);
    }

    /**
     * {@inheritDoc}
     */
    public void warning(Throwable cause, String msg, Object... args) {
        log(Level.WARNING, cause, msg, args);
    }

    /**
     * {@inheritDoc}
     */
    public void error(Throwable cause, String msg, Object... args) {
        log(Level.SEVERE, cause, msg, args);
    }

    /**
     * Log.
     *
     * @param level the level
     * @param cause the cause
     * @param msg the msg
     * @param args arguments.
     */
    private void log(Level level, Throwable cause, String msg, Object... args) {
        log.log(level, formaterMessage(msg, args), cause);
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
        log = (Logger) in.readObject();
    }
}
