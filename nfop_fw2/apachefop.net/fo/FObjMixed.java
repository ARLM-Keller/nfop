/*
 * $Id: FObjMixed.java,v 1.2 2005/03/09 13:32:58 alan13 Exp $
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
package org.apache.fop.fo;

import org.apache.fop.layout.Area;
import org.apache.fop.layout.TextState;
import org.apache.fop.apps.FOPException;

/**
 * base class for representation of mixed content formatting objects
 * and their processing
 */
public abstract class FObjMixed extends FObj {

    // Textdecoration
    protected TextState textState;

    private StringBuffer textBuffer;

    protected FObjMixed(FObj parent, PropertyList propertyList,
                        String systemId, int line, int column)
      throws FOPException {
        super(parent, propertyList, systemId, line, column);
        textState = propMgr.getTextDecoration(parent);

    }

    public TextState getTextState() {
        return textState;
    }

    protected void addCharacters(char data[], int start, int length) {
        if ( textBuffer==null ) {
          textBuffer = new StringBuffer();
        }
        textBuffer.append(data,start,length);
    }

    private final void finalizeText() {
        if (textBuffer != null && textBuffer.length() > 0) {
            FOText ft = new FOText(textBuffer, this);
            ft.setTextState(textState);
            super.addChild(ft);
            textBuffer.setLength(0);
        }
    }

    protected void end() {
        finalizeText();
        textBuffer=null;
    }

    protected void addChild(FONode child) {
        finalizeText();
        super.addChild(child);
    }

    public int layout(Area area) throws FOPException {

        if (this.properties != null) {
            Property prop = this.properties.get("id");
            if (prop != null) {
                String id = prop.getString();

                if (this.marker == START) {
                    if (area.getIDReferences() != null) {
                        try {
                            area.getIDReferences().createID(id);
                        }
                        catch(FOPException e) {
                            if (!e.isLocationSet()) {
                                e.setLocation(systemId, line, column);
                            }
                            throw e;
                        }
                    }
                    this.marker = 0;
                }

                if (this.marker == 0) {
                    if (area.getIDReferences() != null)
                        area.getIDReferences().configureID(id, area);
                }
            }
        }

        int numChildren = this.children.size();
        for (int i = this.marker; i < numChildren; i++) {
            FONode fo = (FONode)children.get(i);
            int status;
            if (Status.isIncomplete((status = fo.layout(area)))) {
                this.marker = i;
                return status;
            }
        }
        return Status.OK;
    }

}

