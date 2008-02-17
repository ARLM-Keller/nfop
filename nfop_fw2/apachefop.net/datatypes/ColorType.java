/*
 * $Id: ColorType.java,v 1.2 2005/03/09 13:32:55 alan13 Exp $
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
package org.apache.fop.datatypes;

import java.util.*;
import org.apache.fop.messaging.MessageHandler;

/**
 * a colour quantity in XSL
 */
public class ColorType {

    /**
     * the red component
     */
    protected float red;

    /**
     * the green component
     */
    protected float green;

    /**
     * the blue component
     */
    protected float blue;

    /**
     * the alpha component
     */
    protected float alpha = 0;

    public ColorType(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    /**
     * set the colour given a particular String specifying either a
     * colour name or #RGB or #RRGGBB
     */
    public ColorType(String value) {
        if (value.startsWith("#")) {
            try {
                if (value.length() == 4) {
                    // note: divide by 15 so F = FF = 1 and so on
                    this.red = Integer.parseInt(value.substring(1, 2), 16)
                               / 15f;
                    this.green = Integer.parseInt(value.substring(2, 3), 16)
                                 / 15f;
                    this.blue = Integer.parseInt(value.substring(3), 16)
                                / 15f;
                } else if (value.length() == 7) {
                    // note: divide by 255 so FF = 1
                    this.red = Integer.parseInt(value.substring(1, 3), 16)
                               / 255f;
                    this.green = Integer.parseInt(value.substring(3, 5), 16)
                                 / 255f;
                    this.blue = Integer.parseInt(value.substring(5), 16)
                                / 255f;
                } else {
                    this.red = 0;
                    this.green = 0;
                    this.blue = 0;
                    MessageHandler.errorln("unknown colour format. Must be #RGB or #RRGGBB");
                }
            } catch (Exception e) {
                this.red = 0;
                this.green = 0;
                this.blue = 0;
                MessageHandler.errorln("unknown colour format. Must be #RGB or #RRGGBB");
            }
        } else if (value.startsWith("rgb(")) {
            int poss = value.indexOf("(");
            int pose = value.indexOf(")");
            if (poss != -1 && pose != -1) {
                value = value.substring(poss + 1, pose);
                StringTokenizer st = new StringTokenizer(value, ",");
                try {
                    if (st.hasMoreTokens()) {
                        String str = st.nextToken().trim();
                        if (str.endsWith("%")) {
                            this.red =
                                Integer.parseInt(str.substring(0, str.length() - 1))
                                * 2.55f;
                        } else {
                            this.red = Integer.parseInt(str) / 255f;
                        }
                    }
                    if (st.hasMoreTokens()) {
                        String str = st.nextToken().trim();
                        if (str.endsWith("%")) {
                            this.green =
                                Integer.parseInt(str.substring(0, str.length() - 1))
                                * 2.55f;
                        } else {
                            this.green = Integer.parseInt(str) / 255f;
                        }
                    }
                    if (st.hasMoreTokens()) {
                        String str = st.nextToken().trim();
                        if (str.endsWith("%")) {
                            this.blue =
                                Integer.parseInt(str.substring(0, str.length() - 1))
                                * 2.55f;
                        } else {
                            this.blue = Integer.parseInt(str) / 255f;
                        }
                    }
                } catch (Exception e) {
                    this.red = 0;
                    this.green = 0;
                    this.blue = 0;
                    MessageHandler.errorln("unknown colour format. Must be #RGB or #RRGGBB");
                }
            }
        } else if (value.startsWith("url(")) {
            // refers to a gradient
        } else {
            if (value.toLowerCase().equals("transparent")) {
                this.red = 0;
                this.green = 0;
                this.blue = 0;
                this.alpha = 1;
            } else {
                boolean found = false;
                for (int count = 0; count < names.length; count++) {
                    if (value.toLowerCase().equals(names[count])) {
                        this.red = vals[count][0] / 255f;
                        this.green = vals[count][1] / 255f;
                        this.blue = vals[count][2] / 255f;
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    this.red = 0;
                    this.green = 0;
                    this.blue = 0;
                    MessageHandler.errorln("unknown colour name: "
                                           + value);
                }
            }
        }
    }

    public float blue() {
        return this.blue;
    }

    public float green() {
        return this.green;
    }

    public float red() {
        return this.red;
    }

    public float alpha() {
        return this.alpha;
    }

    static final String[] names = {
        "aliceblue", "antiquewhite", "aqua", "aquamarine", "azure", "beige",
        "bisque", "black", "blanchedalmond", "blue", "blueviolet", "brown",
        "burlywood", "cadetblue", "chartreuse", "chocolate", "coral",
        "cornflowerblue", "cornsilk", "crimson", "cyan", "darkblue",
        "darkcyan", "darkgoldenrod", "darkgray", "darkgreen", "darkgrey",
        "darkkhaki", "darkmagenta", "darkolivegreen", "darkorange",
        "darkorchid", "darkred", "darksalmon", "darkseagreen",
        "darkslateblue", "darkslategray", "darkslategrey", "darkturquoise",
        "darkviolet", "deeppink", "deepskyblue", "dimgray", "dimgrey",
        "dodgerblue", "firebrick", "floralwhite", "forestgreen", "fuchsia",
        "gainsboro", "lightpink", "lightsalmon", "lightseagreen",
        "lightskyblue", "lightslategray", "lightslategrey", "lightsteelblue",
        "lightyellow", "lime", "limegreen", "linen", "magenta", "maroon",
        "mediumaquamarine", "mediumblue", "mediumorchid", "mediumpurple",
        "mediumseagreen", "mediumslateblue", "mediumspringgreen",
        "mediumturquoise", "mediumvioletred", "midnightblue", "mintcream",
        "mistyrose", "moccasin", "navajowhite", "navy", "oldlace", "olive",
        "olivedrab", "orange", "orangered", "orchid", "palegoldenrod",
        "palegreen", "paleturquoise", "palevioletred", "papayawhip",
        "peachpuff", "peru", "pink", "plum", "powderblue", "purple", "red",
        "rosybrown", "royalblue", "saddlebrown", "salmon", "ghostwhite",
        "gold", "goldenrod", "gray", "grey", "green", "greenyellow",
        "honeydew", "hotpink", "indianred", "indigo", "ivory", "khaki",
        "lavender", "lavenderblush", "lawngreen", "lemonchiffon",
        "lightblue", "lightcoral", "lightcyan", "lightgoldenrodyellow",
        "lightgray", "lightgreen", "lightgrey", "sandybrown", "seagreen",
        "seashell", "sienna", "silver", "skyblue", "slateblue", "slategray",
        "slategrey", "snow", "springgreen", "steelblue", "tan", "teal",
        "thistle", "tomato", "turquoise", "violet", "wheat", "white",
        "whitesmoke", "yellow", "yellowgreen"
    };

    static final int[][] vals = {
         {
            240, 248, 255
        }, {
            250, 235, 215
        }, {
            0, 255, 255
        }, {
            127, 255, 212
        }, {
            240, 255, 255
        }, {
            245, 245, 220
        }, {
            255, 228, 196
        }, {
            0, 0, 0
        }, {
            255, 235, 205
        }, {
            0, 0, 255
        }, {
            138, 43, 226
        }, {
            165, 42, 42
        }, {
            222, 184, 135
        }, {
            95, 158, 160
        }, {
            127, 255, 0
        }, {
            210, 105, 30
        }, {
            255, 127, 80
        }, {
            100, 149, 237
        }, {
            255, 248, 220
        }, {
            220, 20, 60
        }, {
            0, 255, 255
        }, {
            0, 0, 139
        }, {
            0, 139, 139
        }, {
            184, 134, 11
        }, {
            169, 169, 169
        }, {
            0, 100, 0
        }, {
            169, 169, 169
        }, {
            189, 183, 107
        }, {
            139, 0, 139
        }, {
            85, 107, 47
        }, {
            255, 140, 0
        }, {
            153, 50, 204
        }, {
            139, 0, 0
        }, {
            233, 150, 122
        }, {
            143, 188, 143
        }, {
            72, 61, 139
        }, {
            47, 79, 79
        }, {
            47, 79, 79
        }, {
            0, 206, 209
        }, {
            148, 0, 211
        }, {
            255, 20, 147
        }, {
            0, 191, 255
        }, {
            105, 105, 105
        }, {
            105, 105, 105
        }, {
            30, 144, 255
        }, {
            178, 34, 34
        }, {
            255, 250, 240
        }, {
            34, 139, 34
        }, {
            255, 0, 255
        }, {
            220, 220, 220
        }, {
            255, 182, 193
        }, {
            255, 160, 122
        }, {
            32, 178, 170
        }, {
            135, 206, 250
        }, {
            119, 136, 153
        }, {
            119, 136, 153
        }, {
            176, 196, 222
        }, {
            255, 255, 224
        }, {
            0, 255, 0
        }, {
            50, 205, 50
        }, {
            250, 240, 230
        }, {
            255, 0, 255
        }, {
            128, 0, 0
        }, {
            102, 205, 170
        }, {
            0, 0, 205
        }, {
            186, 85, 211
        }, {
            147, 112, 219
        }, {
            60, 179, 113
        }, {
            123, 104, 238
        }, {
            0, 250, 154
        }, {
            72, 209, 204
        }, {
            199, 21, 133
        }, {
            25, 25, 112
        }, {
            245, 255, 250
        }, {
            255, 228, 225
        }, {
            255, 228, 181
        }, {
            255, 222, 173
        }, {
            0, 0, 128
        }, {
            253, 245, 230
        }, {
            128, 128, 0
        }, {
            107, 142, 35
        }, {
            255, 165, 0
        }, {
            255, 69, 0
        }, {
            218, 112, 214
        }, {
            238, 232, 170
        }, {
            152, 251, 152
        }, {
            175, 238, 238
        }, {
            219, 112, 147
        }, {
            255, 239, 213
        }, {
            255, 218, 185
        }, {
            205, 133, 63
        }, {
            255, 192, 203
        }, {
            221, 160, 221
        }, {
            176, 224, 230
        }, {
            128, 0, 128
        }, {
            255, 0, 0
        }, {
            188, 143, 143
        }, {
            65, 105, 225
        }, {
            139, 69, 19
        }, {
            250, 128, 114
        }, {
            248, 248, 255
        }, {
            255, 215, 0
        }, {
            218, 165, 32
        }, {
            128, 128, 128
        }, {
            128, 128, 128
        }, {
            0, 128, 0
        }, {
            173, 255, 47
        }, {
            240, 255, 240
        }, {
            255, 105, 180
        }, {
            205, 92, 92
        }, {
            75, 0, 130
        }, {
            255, 255, 240
        }, {
            240, 230, 140
        }, {
            230, 230, 250
        }, {
            255, 240, 245
        }, {
            124, 252, 0
        }, {
            255, 250, 205
        }, {
            173, 216, 230
        }, {
            240, 128, 128
        }, {
            224, 255, 255
        }, {
            250, 250, 210
        }, {
            211, 211, 211
        }, {
            144, 238, 144
        }, {
            211, 211, 211
        }, {
            244, 164, 96
        }, {
            46, 139, 87
        }, {
            255, 245, 238
        }, {
            160, 82, 45
        }, {
            192, 192, 192
        }, {
            135, 206, 235
        }, {
            106, 90, 205
        }, {
            112, 128, 144
        }, {
            112, 128, 144
        }, {
            255, 250, 250
        }, {
            0, 255, 127
        }, {
            70, 130, 180
        }, {
            210, 180, 140
        }, {
            0, 128, 128
        }, {
            216, 191, 216
        }, {
            255, 99, 71
        }, {
            64, 224, 208
        }, {
            238, 130, 238
        }, {
            245, 222, 179
        }, {
            255, 255, 255
        }, {
            245, 245, 245
        }, {
            255, 255, 0
        }, {
            154, 205, 50
        }
    };
}

/*
 * aliceblue rgb(240, 248, 255)
 * antiquewhite rgb(250, 235, 215)
 * aqua rgb( 0, 255, 255)
 * aquamarine rgb(127, 255, 212)
 * azure rgb(240, 255, 255)
 * beige rgb(245, 245, 220)
 * bisque rgb(255, 228, 196)
 * black rgb( 0, 0, 0)
 * blanchedalmond rgb(255, 235, 205)
 * blue rgb( 0, 0, 255)
 * blueviolet rgb(138, 43, 226)
 * brown rgb(165, 42, 42)
 * burlywood rgb(222, 184, 135)
 * cadetblue rgb( 95, 158, 160)
 * chartreuse rgb(127, 255, 0)
 * chocolate rgb(210, 105, 30)
 * coral rgb(255, 127, 80)
 * cornflowerblue rgb(100, 149, 237)
 * cornsilk rgb(255, 248, 220)
 * crimson rgb(220, 20, 60)
 * cyan rgb( 0, 255, 255)
 * darkblue rgb( 0, 0, 139)
 * darkcyan rgb( 0, 139, 139)
 * darkgoldenrod rgb(184, 134, 11)
 * darkgray rgb(169, 169, 169)
 * darkgreen rgb( 0, 100, 0)
 * darkgrey rgb(169, 169, 169)
 * darkkhaki rgb(189, 183, 107)
 * darkmagenta rgb(139, 0, 139)
 * darkolivegreen rgb( 85, 107, 47)
 * darkorange rgb(255, 140, 0)
 * darkorchid rgb(153, 50, 204)
 * darkred rgb(139, 0, 0)
 * darksalmon rgb(233, 150, 122)
 * darkseagreen rgb(143, 188, 143)
 * darkslateblue rgb( 72, 61, 139)
 * darkslategray rgb( 47, 79, 79)
 * darkslategrey rgb( 47, 79, 79)
 * darkturquoise rgb( 0, 206, 209)
 * darkviolet rgb(148, 0, 211)
 * deeppink rgb(255, 20, 147)
 * deepskyblue rgb( 0, 191, 255)
 * dimgray rgb(105, 105, 105)
 * dimgrey rgb(105, 105, 105)
 * dodgerblue rgb( 30, 144, 255)
 * firebrick rgb(178, 34, 34)
 * floralwhite rgb(255, 250, 240)
 * forestgreen rgb( 34, 139, 34)
 * fuchsia rgb(255, 0, 255)
 * gainsboro rgb(220, 220, 220)
 * lightpink rgb(255, 182, 193)
 * lightsalmon rgb(255, 160, 122)
 * lightseagreen rgb( 32, 178, 170)
 * lightskyblue rgb(135, 206, 250)
 * lightslategray rgb(119, 136, 153)
 * lightslategrey rgb(119, 136, 153)
 * lightsteelblue rgb(176, 196, 222)
 * lightyellow rgb(255, 255, 224)
 * lime rgb( 0, 255, 0)
 * limegreen rgb( 50, 205, 50)
 * linen rgb(250, 240, 230)
 * magenta rgb(255, 0, 255)
 * maroon rgb(128, 0, 0)
 * mediumaquamarine rgb(102, 205, 170)
 * mediumblue rgb( 0, 0, 205)
 * mediumorchid rgb(186, 85, 211)
 * mediumpurple rgb(147, 112, 219)
 * mediumseagreen rgb( 60, 179, 113)
 * mediumslateblue rgb(123, 104, 238)
 * mediumspringgreen rgb( 0, 250, 154)
 * mediumturquoise rgb( 72, 209, 204)
 * mediumvioletred rgb(199, 21, 133)
 * midnightblue rgb( 25, 25, 112)
 * mintcream rgb(245, 255, 250)
 * mistyrose rgb(255, 228, 225)
 * moccasin rgb(255, 228, 181)
 * navajowhite rgb(255, 222, 173)
 * navy rgb( 0, 0, 128)
 * oldlace rgb(253, 245, 230)
 * olive rgb(128, 128, 0)
 * olivedrab rgb(107, 142, 35)
 * orange rgb(255, 165, 0)
 * orangered rgb(255, 69, 0)
 * orchid rgb(218, 112, 214)
 * palegoldenrod rgb(238, 232, 170)
 * palegreen rgb(152, 251, 152)
 * paleturquoise rgb(175, 238, 238)
 * palevioletred rgb(219, 112, 147)
 * papayawhip rgb(255, 239, 213)
 * peachpuff rgb(255, 218, 185)
 * peru rgb(205, 133, 63)
 * pink rgb(255, 192, 203)
 * plum rgb(221, 160, 221)
 * powderblue rgb(176, 224, 230)
 * purple rgb(128, 0, 128)
 * red rgb(255, 0, 0)
 * rosybrown rgb(188, 143, 143)
 * royalblue rgb( 65, 105, 225)
 * saddlebrown rgb(139, 69, 19)
 * salmon rgb(250, 128, 114)
 * ghostwhite rgb(248, 248, 255)
 * gold rgb(255, 215, 0)
 * goldenrod rgb(218, 165, 32)
 * gray rgb(128, 128, 128)
 * grey rgb(128, 128, 128)
 * green rgb( 0, 128, 0)
 * greenyellow rgb(173, 255, 47)
 * honeydew rgb(240, 255, 240)
 * hotpink rgb(255, 105, 180)
 * indianred rgb(205, 92, 92)
 * indigo rgb( 75, 0, 130)
 * ivory rgb(255, 255, 240)
 * khaki rgb(240, 230, 140)
 * lavender rgb(230, 230, 250)
 * lavenderblush rgb(255, 240, 245)
 * lawngreen rgb(124, 252, 0)
 * lemonchiffon rgb(255, 250, 205)
 * lightblue rgb(173, 216, 230)
 * lightcoral rgb(240, 128, 128)
 * lightcyan rgb(224, 255, 255)
 * lightgoldenrodyellow rgb(250, 250, 210)
 * lightgray rgb(211, 211, 211)
 * lightgreen rgb(144, 238, 144)
 * lightgrey rgb(211, 211, 211)
 * sandybrown rgb(244, 164, 96)
 * seagreen rgb( 46, 139, 87)
 * seashell rgb(255, 245, 238)
 * sienna rgb(160, 82, 45)
 * silver rgb(192, 192, 192)
 * skyblue rgb(135, 206, 235)
 * slateblue rgb(106, 90, 205)
 * slategray rgb(112, 128, 144)
 * slategrey rgb(112, 128, 144)
 * snow rgb(255, 250, 250)
 * springgreen rgb( 0, 255, 127)
 * steelblue rgb( 70, 130, 180)
 * tan rgb(210, 180, 140)
 * teal rgb( 0, 128, 128)
 * thistle rgb(216, 191, 216)
 * tomato rgb(255, 99, 71)
 * turquoise rgb( 64, 224, 208)
 * violet rgb(238, 130, 238)
 * wheat rgb(245, 222, 179)
 * white rgb(255, 255, 255)
 * whitesmoke rgb(245, 245, 245)
 * yellow rgb(255, 255, 0)
 * yellowgreen rgb(154, 205, 50)
 */