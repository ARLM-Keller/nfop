/*
 * $Id: AreaTree.java,v 1.3 2005/03/09 14:54:01 alan13 Exp $
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
package org.apache.fop.layout;

// FOP
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.StreamRenderer;
import org.apache.fop.datatypes.IDReferences;
import org.apache.fop.extensions.ExtensionObj;
import org.apache.fop.fo.pagination.PageSequence;

// Java
import java.io.IOException;
import java.util.ArrayList;

/*
 * Modified by Mark Lillywhite, mark-fop@inomial.com. No longer keeps
   a list of pages in the tree, instead these are passed to the
   StreamRenderer. No longer keeps it's own list of IDReferences;
   these are handled by StreamRenderer. In many ways StreamRenderer
   has taken over from AreaTree and possibly this might be a better
   place to deal with things in the future..?<P>
   
   Any extensions added to the AreaTree while generating a page
   are given to the Page for the renderer to deal with.
  */

public class AreaTree {

    /**
     * object containing information on available fonts, including
     * metrics
     */
    FontInfo fontInfo;

    /**
     * List of root extension objects
     */
    ArrayList rootExtensions = null;

    private StreamRenderer streamRenderer;

    public AreaTree(StreamRenderer streamRenderer) {
        this.streamRenderer = streamRenderer;
    }

    public void setFontInfo(FontInfo fontInfo) {
        this.fontInfo = fontInfo;
    }

    public FontInfo getFontInfo() {
        return this.fontInfo;
    }

    public void addPage(Page page)
    throws FOPException {
        try {
            page.setExtensions(rootExtensions);
            rootExtensions = null;
            streamRenderer.queuePage(page);
        } catch (IOException e) {
            throw new FOPException(e);
        }
    }

    public IDReferences getIDReferences() {
        return streamRenderer.getIDReferences();
    }

    public void addExtension(ExtensionObj obj) {
        if(rootExtensions ==null) {
            rootExtensions = new ArrayList();
        }
        rootExtensions.add(obj);
    }

    // Auxillary function for retrieving markers.
    public ArrayList getDocumentMarkers() {
        return streamRenderer.getDocumentMarkers();
    }

    // Auxillary function for retrieving markers.
    public PageSequence getCurrentPageSequence() {
        return streamRenderer.getCurrentPageSequence();
    }

    // Auxillary function for retrieving markers.
    public ArrayList getCurrentPageSequenceMarkers() {
        return streamRenderer.getCurrentPageSequenceMarkers();
    }
}
