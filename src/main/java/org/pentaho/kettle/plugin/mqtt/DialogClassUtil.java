package org.pentaho.kettle.plugin.mqtt;

/**
 * Created by dams on 02-08-2016.
 */
public class DialogClassUtil {

    private static final String PKG_NAME=DialogClassUtil.class.getPackage().getName();
    private static final String UI_PKG_NAME = PKG_NAME + ".ui";

    public static String getDialogClassName( Class<?> clazz ) {
        String className = clazz.getCanonicalName().replace( PKG_NAME, UI_PKG_NAME );
        if ( className.endsWith( "Meta" ) ) {
            className = className.substring( 0, className.length() - 4 );
        }

        className = className + "Dialog";
        return className;
    }
}
