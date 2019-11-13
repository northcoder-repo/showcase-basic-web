package org.northcoder.titlewebdemo.util;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Accesses the webdemo.properties file.  The location of the file is provided on
 * start-up by a Java '-D' command line parameter - for example:
 * 
 *   -Dwebdemo.properties=/your/path/to/TitleWebDemo/webdemo.properties
 */
public enum DemoProperties {

    INSTANCE;

    private Properties props;

    public Properties getProps() {
        if (props == null) {
            props = new Properties();
            try (FileInputStream fis = new FileInputStream(System
                    .getProperty("webdemo.properties"))) {
                props.load(fis);
            } catch (IOException ex) {
                LoggerUtils.LOGGER.error("Error loading properties file.", ex);
            }
        }
        return props;
    }
}
