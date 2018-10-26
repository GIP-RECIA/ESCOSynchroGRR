/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: CrlrRuntimeException.java,v 1.1 2009/07/24 14:14:36 ent_breyton Exp $
 */

package org.crlr.exception.base;

import java.text.MessageFormat;

/**
 * Exception "runtime" de base. Toutes les exceptions de type "unchecked" (runtime)
 * doivent hériter de cette classe.
 *
 * @author breytond
 */
public class CrlrRuntimeException extends RuntimeException {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1019775442652749695L;

/**
     * The Constructor.
     * 
     * @param msg
     *            the msg
     * @param args
     *            the args
     * @param cause
     *            the cause
     */
    public CrlrRuntimeException(final Throwable cause, final String msg,
                                final Object... args) {
        super(MessageFormat.format(msg.replaceAll("'", "''"), args), cause);
    }

/**
     * The Constructor.
     * 
     * @param msg
     *            the msg
     * @param args
     *            the args
     */
    public CrlrRuntimeException(final String msg, final Object... args) {
        this(null, msg, args);
    }
}
