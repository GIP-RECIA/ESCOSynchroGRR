/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: StringUtilsTest.java,v 1.1 2009/07/24 14:14:36 ent_breyton Exp $
 */

package org.crlr.utils;

import junit.framework.TestCase;

import org.crlr.exception.base.CrlrRuntimeException;

import org.crlr.log.Log;
import org.crlr.log.LogFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test de {@link StringUtils}.
 *
 * @author romana
 * @author breytond
 */
public class StringUtilsTest extends TestCase {
    /** DOCUMENTATION INCOMPLETE! */
    private final Log log = LogFactory.getLog(getClass());

    /**
     * 
     */
    public void testTrimToNull() {
        assertEquals("Hello", StringUtils.trimToNull("Hello"));
        assertEquals("Hello", StringUtils.trimToNull("Hello  "));
        assertEquals("Hello", StringUtils.trimToNull("  Hello"));
        assertEquals("Hello", StringUtils.trimToNull("  \tHello  \n"));
        assertNull(StringUtils.trimToNull(""));
        assertNull(StringUtils.trimToNull("   "));
        assertNull(StringUtils.trimToNull(null));
    }

    /**
     * 
     */
    public void testIsEmpty() {
        assertTrue(StringUtils.isEmpty(""));
        assertTrue(StringUtils.isEmpty(null));
        assertTrue(StringUtils.isEmpty("    "));
        assertTrue(StringUtils.isEmpty("  \t \n  "));
        assertFalse(StringUtils.isEmpty("Hello  \n"));
    }

    /**
     * 
     */
    public void testTrimToBlank() {
        assertEquals("", StringUtils.trimToBlank(""));
        assertEquals("", StringUtils.trimToBlank(null));
        assertEquals("Hello", StringUtils.trimToBlank("Hello"));
        assertEquals("Hello", StringUtils.trimToBlank("  Hello \n \r \t "));
    }

    /**
     * 
     */
    public void testConcat() {
        assertEquals("", StringUtils.concat("", ""));
        assertEquals("", StringUtils.concat("", null));
        assertEquals("", StringUtils.concat(null, ""));
        assertEquals("", StringUtils.concat(null, null));
        assertEquals("Helloworld", StringUtils.concat("Hello", "world"));
    }

    /**
     * 
     */
    public void testSplit() {
        final String regex = "[ ]*[,;][ ]*";
        final List<String> l = new ArrayList<String>();
        l.add("didi");
        l.add("dudu");
        l.add("dada");
        final List<String> l2 = StringUtils.split("didi, dudu  , dada", regex);
        assertNotNull(l2);
        assertEquals(l.size(), l2.size());
        assertEquals(l.get(0), l2.get(0));
        assertEquals(l.get(1), l2.get(1));
        assertEquals(l.get(1), l2.get(1));

        final List<String> l3 = StringUtils.split("ddd", regex);
        assertEquals(l3.size(), 1);
        assertEquals(l3.get(0), "ddd");
    }

    /**
     * 
     */
    public void testSplitLimit() {
        final String regex = ":";
        final List<String> l = new ArrayList<String>();
        l.add("didi");
        l.add("dudu:dada");
        final List<String> l2 = StringUtils.split("didi:dudu:dada", regex, 2);
        assertNotNull(l2);
        assertEquals(l.size(), l2.size());
        assertEquals(l.get(0), l2.get(0));
        assertEquals(l.get(1), l2.get(1));

        final List<String> l3 = StringUtils.split("ddd", regex, 1);
        assertEquals(l3.size(), 1);
        assertEquals(l3.get(0), "ddd");

        final String regex2 = "\\D+";
        final List<String> l4 = StringUtils.split("ddd01", regex2, 2);
        assertEquals(2, l4.size());
        assertEquals("", l4.get(0));
        assertEquals("01", l4.get(1));
    }

    /**
     * 
     */
    public void testEncodeDecode() {
        final String str = "Hello si@d! é^à$";
        final String charset = "UTF-8";
        assertEquals(str, StringUtils.decode(StringUtils.encode(str, charset), charset));
    }

    /**
     * 
     */
    public void testEncodeCharsetInconnu() {
        try {
            StringUtils.encode("hello", "abc");
            fail("CrlrRuntimeException attendue");
        } catch (final CrlrRuntimeException e) {
            log.debug(e, "Exception capturée");
        }
    }

    /**
     * 
     */
    public void testEncodeCharsetNull() {
        assertNotNull(StringUtils.encode("hello", null));
    }

    /**
     * 
     */
    public void testDecodeCharsetInconnu() {
        try {
            StringUtils.decode(new byte[] { 65 }, "abc");
            fail("CrlrRuntimeException attendue");
        } catch (final CrlrRuntimeException e) {
            log.debug(e, "Exception capturée");
        }
    }

    /**
     * 
     */
    public void testDecodeCharsetNull() {
        assertNotNull(StringUtils.decode(new byte[] { 65 }, null));
    }

    /**
     * 
     */
    public void testEncodeNull() {
        final byte[] data = StringUtils.encode(null, "UTF-8");
        assertNotNull(data);
        assertEquals(0, data.length);
    }

    /**
     * 
     */
    public void testEncodeEmpty() {
        final byte[] data = StringUtils.encode(StringUtils.EMPTY_STRING, "UTF-8");
        assertNotNull(data);
        assertEquals(0, data.length);
    }

    /**
     * 
     */
    public void testDecodeNull() {
        assertNull(StringUtils.decode(null, "UTF-8"));
    }

    /**
     * 
     */
    public void testDecodeEmpty() {
        assertEquals(StringUtils.EMPTY_STRING,
                     StringUtils.decode(new byte[] {  }, "UTF-8"));
    }

    /**
     * 
     */
    public void testValueOf() {
        assertNull(StringUtils.valueOf(null));
        assertNotNull(StringUtils.valueOf("null"));
        assertEquals("hello", StringUtils.valueOf("hello"));
    }

    /**
     * 
     */
    public void testContains() {
        assertTrue(StringUtils.contains("banane, pomme; orange", "[ ]*[,;][ ]*", "pomme"));
    }

    /**
     * 
     */
    public void testStartWith() {
        assertTrue(StringUtils.startWith("COUCOU", "COU"));
    }

    /**
     * 
     */
    public void testEndWith() {
        assertTrue(StringUtils.endWith("COCOX", "X"));
    }

    /**
     * 
     */
    public void testStripSpaces() {
        assertEquals(null, StringUtils.stripSpaces(null));
        assertEquals("", StringUtils.stripSpaces(""));
        assertEquals("Hello", StringUtils.stripSpaces("Hello"));
        assertEquals("Hello", StringUtils.stripSpaces(" Hello "));
        assertEquals("Helloworld", StringUtils.stripSpaces("Hello world"));
        assertEquals("Helloworld", StringUtils.stripSpaces("    Hello \t world   \t  "));
    }

    /**
     * 
     */
    public void testToLong() {
        final String[] values = { "1", "-1", "0" };
        final long[] longValues = { 1L, -1L, 0L };
        final long[] testLongValues = StringUtils.toLong(values);
        assertEquals(longValues.length, testLongValues.length);
        for (int i = 0; i < longValues.length; ++i) {
            assertEquals(longValues[i], testLongValues[i]);
        }

        assertTrue(StringUtils.toLong(null).length == 0);
    }

    /**
     * 
     */
    public void testToInt() {
        final String[] values = { "1", "-1", "0" };
        final int[] intValues = { 1, -1, 0 };
        final int[] testIntValues = StringUtils.toInt(values);
        assertEquals(intValues.length, testIntValues.length);
        for (int i = 0; i < intValues.length; ++i) {
            assertEquals(intValues[i], testIntValues[i]);
        }

        assertTrue(StringUtils.toInt(null).length == 0);
    }

    /**
     * 
     */
    public void testJoin() {
        final List<String> liste = Arrays.asList(new String[] { "AB", "CD", "EFGHI" });
        final String total = StringUtils.join(liste, ",");
        assertNotNull(total);
        assertEquals("AB,CD,EFGHI", total);
    }

    /**
     * 
     */
    public void testCompare() {
        assertTrue(StringUtils.compare("", null, false) == 0);
    }

    /**
     * 
     */
    public void testSansAccent() {
        String result = StringUtils.sansAccent(null);
        assertEquals(null, result);

        result = StringUtils.sansAccent("");
        assertEquals("", result);

        String chaine = "éèæàùúöÖÏïëËêôîûüÜäÄâÿýòóõöñ";
        String chaineResultat = "eeaeauuoOIieEeoiuuUaAayyoooon";
        result = StringUtils.sansAccent(chaine);
        assertEquals(chaineResultat, result);

        chaine = "éèçàîêùàÏ";
        chaineResultat = "eecaieuaI";
        result = StringUtils.sansAccent(chaine);
        assertEquals(chaineResultat, result);

        chaine = "Le java orienté objet à une mv";
        chaineResultat = "Le java oriente objet a une mv";
        result = StringUtils.sansAccent(chaine);
        assertEquals(chaineResultat, result);
    }

    /**
     * 
     */
    public void testSansAccentMajuscule() {
        String result = StringUtils.sansAccentEnMajuscule(null);
        assertEquals(null, result);

        result = StringUtils.sansAccentEnMajuscule("");
        assertEquals("", result);

        String chaine = "éèàù/*\\- java";
        String chaineResultat = "EEAU/*\\- JAVA";
        result = StringUtils.sansAccentEnMajuscule(chaine);
        assertEquals(chaineResultat, result);

        chaine = "éèçàîêùàÏïô";
        chaineResultat = "EECAIEUAIIO";
        result = StringUtils.sansAccentEnMajuscule(chaine);
        assertEquals(chaineResultat, result);
    }

    /**
     * 
     */
    public void testPadRight() {
        assertEquals("ABCD----", StringUtils.padRight("ABCD", 8, '-'));
        assertEquals("ABC", StringUtils.padRight("ABCD", 3, '-'));
        assertEquals("---", StringUtils.padRight(null, 3, '-'));
    }

    /**
     * 
     */
    public void testPadLeft() {
        assertEquals("----ABCD", StringUtils.padLeft("ABCD", 8, '-'));
        assertEquals("ABC", StringUtils.padLeft("ABCD", 3, '-'));
        assertEquals("---", StringUtils.padLeft(null, 3, '-'));
    }

    /**
     * 
     */
    public void testEquals() {
        assertTrue(StringUtils.equals("TEST     ", "TEST  "));
        assertTrue(StringUtils.equals("   ", "  "));
        assertTrue(StringUtils.equals("TEST", "TEST"));
    }

    /**
     * 
     */
    public void testSubString() {
        assertEquals(StringUtils.subString("blatliplu", 3, 1), "t");
        assertEquals(StringUtils.subString("blatliplu", 3, 2), "tl");
        assertEquals(StringUtils.subString("blatliplu", 3, 10), "tliplu");
        assertEquals(StringUtils.subString("blatliplu", -1, 1), "b");
        assertEquals(StringUtils.subString("blatliplu", -1, 10), "blatliplu");
        assertEquals(StringUtils.subString("blatliplu", 0, 0), "b");
        assertEquals(StringUtils.subString("blatliplu", 8, -2), "u");
        assertEquals(StringUtils.subString("blatliplu", 8, 3), "u");
        assertEquals(StringUtils.subString("", 20, 20), "");
        assertEquals(StringUtils.subString(" ", 20, 20), "");
    }

    /**
     * 
     */
    public void testConcatArrays() {
        final String[] s1 = new String[] { "aaa", "bbb", };
        final String[] s2 = new String[] { "ccc", "ddd", };

        final String[] s1s2 = new String[] { "aaa", "bbb", "ccc", "ddd", };
        final String[] s2s1 = new String[] { "ccc", "ddd", "aaa", "bbb", };
        final String[] s1s1 = new String[] { "aaa", "bbb", "aaa", "bbb", };
        final String[] nullnull = new String[] {  };

        final String[] s1s2Array = StringUtils.concatArrays(s1, s2);
        final String[] s2s1Array = StringUtils.concatArrays(s2, s1);
        final String[] s1s1Array = StringUtils.concatArrays(s1, s1);
        final String[] nullnullArray = StringUtils.concatArrays(null, null);

        assertTrue(testArrays(s1s2, s1s2Array));
        assertTrue(testArrays(s2s1, s2s1Array));
        assertTrue(testArrays(s1s1, s1s1Array));
        assertTrue(testArrays(nullnull, nullnullArray));
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @param s1 DOCUMENTATION INCOMPLETE!
     * @param s2 DOCUMENTATION INCOMPLETE!
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    private boolean testArrays(String[] s1, String[] s2) {
        if ((s1 == null) && (s2 == null)) {
            return true;
        }
        if ((s1 == null) || (s2 == null)) {
            return false;
        }
        if (s1.length != s2.length) {
            return false;
        }
        for (int i = 0; i < s1.length; i++) {
            final String ss1 = s1[i];
            final String ss2 = s2[i];
            if (!StringUtils.equals(ss1, ss2)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 
     */
    public void testFormatageSeulementPremiereLettreMajuscule() {
        assertEquals("Toto", StringUtils.formatageSeulementPremiereLettreMajuscule("toto"));
        assertEquals("Toto", StringUtils.formatageSeulementPremiereLettreMajuscule("toTO"));
        assertEquals("T", StringUtils.formatageSeulementPremiereLettreMajuscule("t"));
    }

    /**
     * 
     */
    public void testFormatagePremiereLettreMajuscule() {
        assertEquals("Toto", StringUtils.formatagePremiereLettreMajuscule("toto"));
        assertEquals("ToTO", StringUtils.formatagePremiereLettreMajuscule("toTO"));
        assertEquals("T", StringUtils.formatagePremiereLettreMajuscule("t"));
    }

    /**
     * 
     */
    public void testMapAccents() {
        assertEquals("è", StringUtils.MAP_ACCENTS_REPORT.get("e_grave"));
    }
}
