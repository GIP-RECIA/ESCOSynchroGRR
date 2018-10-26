/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: LdapUtils.java,v 1.1 2010/05/04 15:52:11 weberent Exp $
 */

package org.crlr.synchronisationGrr.LDAP;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.PagedResultsControl;
import javax.naming.ldap.PagedResultsResponseControl;

/**
 * LdapUtils : Une méthode qui permet de créer une connexion puis d'effectuer une recherche paginée,
 * et enfin fermer la connexion puis retourner le resultat.
 *
 * @author breytond.
 */
public final class LdapUtils {
    /**
     * PAGE_SIZE.
     */
    private final static int PAGE_SIZE = 300;

    /**
     * Default constructor.
     */
    private LdapUtils() {};
    
    /**
     * Recherche paginée avec une connexion dédié. 
     * @param url l'url.
     * @param userDn le login.
     * @param password le mot de passe.
     * @param dn le dn.
     * @param filter le filtre.
     * @return la liste de résultat.
     */
    public static List<Attributes> searchWithPaged(final String url, final String userDn,
                                       final String password, final String dn,
                                       final String filter) {
        final List<Attributes> resultatRequete = new ArrayList<Attributes>();

        final Hashtable<String, Object> env = new Hashtable<String, Object>(11);
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, url);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, userDn);
        env.put(Context.SECURITY_CREDENTIALS, password);
        
        LdapContext ctx = null;
        
        try {
            ctx = new InitialLdapContext(env, null);

            // Activate paged results
            byte[] cookie = null;
            ctx.setRequestControls(new Control[] {
                                       new PagedResultsControl(PAGE_SIZE,
                                                               Control.NONCRITICAL)
                                   });

            do {
                /* perform the search */
                final NamingEnumeration<SearchResult> results =
                    ctx.search(dn, filter, new SearchControls());

                /* for each entry print out name + all attrs and values */
                while ((results != null) && results.hasMore()) {
                    final SearchResult entry = (SearchResult) results.next();
                    resultatRequete.add(entry.getAttributes());
                }

                // Examine the paged results control response
                final Control[] controls = ctx.getResponseControls();
                if (controls != null) {
                    for (int i = 0; i < controls.length; i++) {
                        if (controls[i] instanceof PagedResultsResponseControl) {
                            final PagedResultsResponseControl prrc =
                                (PagedResultsResponseControl) controls[i];
                            cookie = prrc.getCookie();
                        }
                    }
                } else {
                    throw new RuntimeException("Le contrôleur de pagination n'est pas disponible pour ce serveur LDAP.");
                }
                // Re-activate paged results
                ctx.setRequestControls(new Control[] {
                                           new PagedResultsControl(PAGE_SIZE, cookie,
                                                                   Control.CRITICAL)
                                       });
            } while (cookie != null);

          
        } catch (NamingException e) {   
            throw new RuntimeException("La pagination LDAP n'a pas aboutie : " +
                                       e.getMessage());
        } catch (IOException ie) {
            throw new RuntimeException("La pagination LDAP n'a pas aboutie : " +
                                       ie.getMessage());
        } finally {
            try {
                if (ctx!= null) {
                    ctx.close();
                }
            } catch (NamingException e) {
                throw new RuntimeException("Echec de la fermeture de la connexion : " +
                        e.getMessage());
            }
        }
        
        return resultatRequete; 
    }
    
    /**
     * Recherche paginée avec une connexion dédié permettant d'extraire l'ensemble des uids concernés par un filtre. 
     * @param url l'url.
     * @param userDn le login.
     * @param password le mot de passe.
     * @param dn le dn. Doit imérativement être la branche personne.
     * @param filter le filtre.
     * @return la liste de résultat.
     */
    public static List<String> searchUIDWithPaged(final String url, final String userDn,
                                       final String password, final String dn,
                                       final String filter) {
        final List<String> resultatRequete = new ArrayList<String>();

        final Hashtable<String, Object> env = new Hashtable<String, Object>(11);
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, url);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, userDn);
        env.put(Context.SECURITY_CREDENTIALS, password);
        
        LdapContext ctx = null;
        
        try {
            ctx = new InitialLdapContext(env, null);

            // Activate paged results
            byte[] cookie = null;
            ctx.setRequestControls(new Control[] {
                                       new PagedResultsControl(PAGE_SIZE,
                                                               Control.NONCRITICAL)
                                   });

            do {
                /* perform the search */
                final NamingEnumeration<SearchResult> results =
                    ctx.search(dn, filter, new SearchControls());

                /* for each entry print out name + all attrs and values */
                while ((results != null) && results.hasMore()) {
                    final SearchResult entry = (SearchResult) results.next();
                    final Attributes tmp = entry.getAttributes();
                    resultatRequete.add(tmp.get("uid").get().toString());
                }

                // Examine the paged results control response
                final Control[] controls = ctx.getResponseControls();
                if (controls != null) {
                    for (int i = 0; i < controls.length; i++) {
                        if (controls[i] instanceof PagedResultsResponseControl) {
                            final PagedResultsResponseControl prrc =
                                (PagedResultsResponseControl) controls[i];
                            cookie = prrc.getCookie();
                        }
                    }
                } else {
                    throw new RuntimeException("Le contrôleur de pagination n'est pas disponible pour ce serveur LDAP.");
                }
                // Re-activate paged results
                ctx.setRequestControls(new Control[] {
                                           new PagedResultsControl(PAGE_SIZE, cookie,
                                                                   Control.CRITICAL)
                                       });
            } while (cookie != null);

          
        } catch (NamingException e) {   
            throw new RuntimeException("La pagination LDAP n'a pas aboutie : " +
                                       e.getMessage());
        } catch (IOException ie) {
            throw new RuntimeException("La pagination LDAP n'a pas aboutie : " +
                                       ie.getMessage());
        } finally {
            try {
                if (ctx!= null) {
                    ctx.close();
                }
            } catch (NamingException e) {
                throw new RuntimeException("Echec de la fermeture de la connexion : " +
                        e.getMessage());
            }
        }
        
        return resultatRequete; 
    }
}
