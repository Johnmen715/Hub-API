package Utilities.Util;

import Utilities.Base;
import org.jboss.arquillian.graphene.page.Page;

public class TakeScreenShot {

    @Page
    private Base base;

    public void getScreen(String filePath) {

        base.getScreen(filePath);
    }

}
