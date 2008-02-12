/*
 * $Id: BasicLink.java,v 1.2 2005/03/09 13:33:07 alan13 Exp $
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
package org.apache.fop.fo.flow;

// FOP
import org.apache.fop.fo.*;
import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.properties.*;
import org.apache.fop.layout.*;

public class BasicLink extends Inline {

    public static class Maker extends FObj.Maker {
        public FObj make(FObj parent, PropertyList propertyList,
                         String systemId, int line, int column)
            throws FOPException {
            return new BasicLink(parent, propertyList, systemId, line, column);
        }
    }

    public static FObj.Maker maker() {
        return new BasicLink.Maker();
    }

    public BasicLink(FObj parent, PropertyList propertyList,
                     String systemId, int line, int column) throws FOPException {
        super(parent, propertyList, systemId, line, column);
    }

    public String getName() {
        return "fo:basic-link";
    }

    public int layout(Area area) throws FOPException {
        String destination;
        int linkType;

        // Common Accessibility Properties
        AccessibilityProps mAccProps = propMgr.getAccessibilityProps();

        // Common Aural Properties
        AuralProps mAurProps = propMgr.getAuralProps();

        // Common Border, Padding, and Background Properties
        BorderAndPadding bap = propMgr.getBorderAndPadding();
        BackgroundProps bProps = propMgr.getBackgroundProps();

        // Common Margin Properties-Inline
        MarginInlineProps mProps = propMgr.getMarginInlineProps();

        // Common Relative Position Properties
        RelativePositionProps mRelProps = propMgr.getRelativePositionProps();

        // this.properties.get("alignment-adjust");
        // this.properties.get("alignment-baseline");
        // this.properties.get("baseline-shift");
        // this.properties.get("destination-place-offset");
        // this.properties.get("dominant-baseline");
        // this.properties.get("external-destination");        
        // this.properties.get("id");
        // this.properties.get("indicate-destination");  
        // this.properties.get("internal-destination");  
        // this.properties.get("keep-together");
        // this.properties.get("keep-with-next");
        // this.properties.get("keep-with-previous");
        // this.properties.get("line-height");
        // this.properties.get("line-height-shift-adjustment");
        // this.properties.get("show-destination");  
        // this.properties.get("target-processing-context");  
        // this.properties.get("target-presentation-context");  
        // this.properties.get("target-stylesheet");  

        if (!(destination =
                this.properties.get("internal-destination").getString()).equals("")) {
            linkType = LinkSet.INTERNAL;
        } else if (!(destination =
        this.properties.get("external-destination").getString()).equals("")) {
            linkType = LinkSet.EXTERNAL;
            if (destination.startsWith("url(") && destination.endsWith(")")) {
                destination = destination.substring(4, destination.length() - 1).trim();
                if (destination.startsWith("\"") && destination.endsWith("\"")) {
                    destination = destination.substring(1, destination.length() - 1);
                } else if (destination.startsWith("'") && destination.endsWith("'")) {
                    destination = destination.substring(1, destination.length() - 1);
                }
            }            
        } else {
            throw new FOPException("internal-destination or external-destination must be specified in basic-link", systemId, line, column);
        }

        if (this.marker == START) {
            // initialize id
            String id = this.properties.get("id").getString();
            try {
                area.getIDReferences().initializeID(id, area);
            }
            catch(FOPException e) {
                if (!e.isLocationSet()) {
                    e.setLocation(systemId, line, column);
                }
                throw e;
            }
            this.marker = 0;
        }

        // new LinkedArea to gather up inlines
        LinkSet ls = new LinkSet(destination, area, linkType);

        Page p = area.getPage();

        //AreaContainer ac = p.getBody().getCurrentColumnArea();
        AreaContainer ac = area.getNearestAncestorAreaContainer();
        while (ac!=null && ac.getPosition()!=Position.ABSOLUTE) {
            ac = ac.getNearestAncestorAreaContainer();
        }
        if (ac == null) {
            ac = p.getBody().getCurrentColumnArea();
            //System.err.println("Using currentColumnArea as AC for link");
        }
        if (ac == null) {
            throw new FOPException("Couldn't get ancestor AreaContainer when processing basic-link", systemId, line, column);
        }

        int numChildren = this.children.size();
        for (int i = this.marker; i < numChildren; i++) {
            FONode fo = (FONode)children.get(i);
            fo.setLinkSet(ls);

            int status;
            if (Status.isIncomplete((status = fo.layout(area)))) {
                this.marker = i;
                return status;
            }
        }

        ls.applyAreaContainerOffsets(ac, area);

        // pass on command line
        String mergeLinks = System.getProperty("links.merge");
        if ((null == mergeLinks) || mergeLinks.equalsIgnoreCase("yes")) {
            ls.mergeLinks();
        }

        p.addLinkSet(ls);

        return Status.OK;
    }

}
