/*
 * The Apache Software License, Version 1.1
 *
 *
 * Copyright (c) 2000-2002 The Apache Software Foundation.  All rights 
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:  
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Xerces" and "Apache Software Foundation" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written 
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    nor may "Apache" appear in their name, without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation and was
 * originally based on software copyright (c) 1999, International
 * Business Machines, Inc., http://www.apache.org.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */


package org.apache.xml.serialize;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

/**
 * This class represents an encoding.
 *
 * @version $Id: EncodingInfo.java,v 1.1.1.1 2002/10/31 15:40:38 pettys Exp $
 */
public class EncodingInfo {

    String name;
    String javaName;
    int lastPrintable;

    /**
     * Creates new <code>EncodingInfo</code> instance.
     */
    public EncodingInfo(String mimeName, String javaName, int lastPrintable) {
        this.name = mimeName;
        this.javaName = javaName == null ? mimeName : javaName;
        this.lastPrintable = lastPrintable;
    }

    /**
     * Creates new <code>EncodingInfo</code> instance.
     */
    public EncodingInfo(String mimeName, int lastPrintable) {
        this(mimeName, mimeName, lastPrintable);
    }

    /**
     * Returns a MIME charset name of this encoding.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns a writer for this encoding based on
     * an output stream.
     *
     * @return A suitable writer
     * @exception UnsupportedEncodingException There is no convertor
     *  to support this encoding
     */
    public Writer getWriter(OutputStream output)
        throws UnsupportedEncodingException {
        if (this.javaName == null)
            return new OutputStreamWriter(output);
        return new OutputStreamWriter(output, this.javaName);
    }
    /**
     * Checks whether the specified character is printable or not.
     *
     * @param ch a code point (0-0x10ffff)
     */
    public boolean isPrintable(int ch) {
        return ch <= this.lastPrintable;
    }
}
