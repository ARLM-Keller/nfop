
package org.apache.fop.fo.properties;

import java.util.Hashtable;
import org.apache.fop.datatypes.*;
import org.apache.fop.fo.*;
import org.apache.fop.apps.FOPException;
import org.apache.fop.messaging.MessageHandler;

public class PaddingBottomMaker extends  GenericPadding {




    static public Property.Maker maker(String propName) {
	return new PaddingBottomMaker(propName);
    }

    protected PaddingBottomMaker(String name) {
	super(name);

    }



    public Property compute(PropertyList propertyList) throws FOPException {
      FObj parentFO = propertyList.getParentFObj();
      StringBuffer sbExpr=new StringBuffer();
      Property p=null;
      sbExpr.append("padding-");
   sbExpr.append(propertyList.wmAbsToRel(PropertyList.BOTTOM));

      p= propertyList.getExplicitOrShorthand(sbExpr.toString());
      
      if (p != null) {
          p = convertProperty(p, propertyList, parentFO );
      }
      
      return p;
    }

}
