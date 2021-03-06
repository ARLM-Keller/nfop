/*
 * $Id: TraxTransform.java,v 1.3 2005/03/09 15:50:37 alan13 Exp $
 * ============================================================================
 *                    The Apache Software License, Version 1.1
 * ============================================================================
 * 
 * Copyright (C) 1999-2003 The Apache Software Foundation. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modifica-
 * tion, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment: "This product includes software
 *    developed by the Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 
 * 4. The names "FOP" and "Apache Software Foundation" must not be used to
 *    endorse or promote products derived from this software without prior
 *    written permission. For written permission, please contact
 *    apache@apache.org.
 * 
 * 5. Products derived from this software may not be called "Apache", nor may
 *    "Apache" appear in their name, without prior written permission of the
 *    Apache Software Foundation.
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * APACHE SOFTWARE FOUNDATION OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLU-
 * DING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * ============================================================================
 * 
 * This software consists of voluntary contributions made by many individuals
 * on behalf of the Apache Software Foundation and was originally created by
 * James Tauber <jtauber@jtauber.com>. For more information on the Apache
 * Software Foundation, please see <http://www.apache.org/>.
 */ 
package org.apache.fop.tools.xslt;

import javax.xml.transform.*;

import java.io.InputStream;
import java.io.Writer;

import java.util.HashMap;
import org.w3c.dom.Document;

/**
 * Handles xslt tranformations via Trax (xalan2)
 */

public class TraxTransform {

    /**
     * Cache of compiled stylesheets (filename, StylesheetRoot)
     */
    private static HashMap _stylesheetCache = new HashMap();

    public static Transformer getTransformer(String xsltFilename,
                                             boolean cache) {
        try {
            if (cache && _stylesheetCache.containsKey(xsltFilename)) {
                Templates cachedStylesheet =
                    (Templates)_stylesheetCache.get(xsltFilename);
                return cachedStylesheet.newTransformer();
            }

            Source xslSheet =
                new javax.xml.transform.stream.StreamSource(xsltFilename);


            /*
             * System.out.println("****************************");
             * System.out.println("trax compile \nin: " + xsltFilename);
             * System.out.println("****************************");
             */
            TransformerFactory factory = TransformerFactory.newInstance();

            Templates compiledSheet = factory.newTemplates(xslSheet);
            if (cache) {
                _stylesheetCache.put(xsltFilename, compiledSheet);
            }
            return compiledSheet.newTransformer();
        } catch (TransformerConfigurationException ex) {
            ex.printStackTrace();
        }
        return null;

    }

    public static void transform(String xmlSource, String xslURL,
                                 String outputFile) {
        transform(new javax.xml.transform.stream.StreamSource(xmlSource),
                  new javax.xml.transform.stream.StreamSource(xslURL),
                  new javax.xml.transform.stream.StreamResult(outputFile));
    }

    public static void transform(Document xmlSource, String xslURL,
                                 String outputFile) {

        transform(new javax.xml.transform.dom.DOMSource(xmlSource),
                  new javax.xml.transform.stream.StreamSource(xslURL),
                  new javax.xml.transform.stream.StreamResult(outputFile));

    }

    public static void transform(String xmlSource, String xslURL,
                                 Writer output) {
        transform(new javax.xml.transform.stream.StreamSource(xmlSource),
                  new javax.xml.transform.stream.StreamSource(xslURL),
                  new javax.xml.transform.stream.StreamResult(output));
    }

    public static void transform(Document xmlSource, InputStream xsl,
                                 Document outputDoc) {
        transform(new javax.xml.transform.dom.DOMSource(xmlSource),
                  new javax.xml.transform.stream.StreamSource(xsl),
                  new javax.xml.transform.dom.DOMResult(outputDoc));
    }

    public static void transform(Source xmlSource, Source xslSource,
                                 Result result) {
        try {
            Transformer transformer;
            if (xslSource.getSystemId() == null) {
                TransformerFactory factory = TransformerFactory.newInstance();
                transformer = factory.newTransformer(xslSource);
            } else {
                transformer = getTransformer(xslSource.getSystemId(), true);
            }
            transformer.transform(xmlSource, result);
        } catch (TransformerConfigurationException ex) {
            ex.printStackTrace();
        } catch (TransformerException ex) {
            ex.printStackTrace();
        }

    }

}
