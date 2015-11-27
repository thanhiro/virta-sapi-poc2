package fi.csc.virta.julkaisut;

import javax.servlet.ServletException;
import javax.sql.DataSource;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.Context;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.glassfish.jersey.servlet.ServletContainer;

import java.io.File;
import java.net.MalformedURLException;

/**
 * Main bootstrap of application.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        start();
    }

    public static Tomcat start() throws ServletException, LifecycleException,
            MalformedURLException {

        // Define a folder to hold web application contents.
        String webappDirLocation = "src/main/webapp/";
        Tomcat tomcat = new Tomcat();

        tomcat.enableNaming();

        // Define port number for the web application
        String webPort = System.getenv("PORT");
        if (webPort == null || webPort.isEmpty()) {
            webPort = "8080";
        }
        // Bind the port to Tomcat server
        tomcat.setPort(Integer.valueOf(webPort));

        // Define a web application context.
        Context context = tomcat.addWebapp("/", new File(
                webappDirLocation).getAbsolutePath());

        context.getNamingResources().addResource(dataSourceResource());

        // Add servlet that will register Jersey REST resources
        Tomcat.addServlet(context, "jersey-servlet", resourceConfig());
        context.addServletMapping("/*", "jersey-servlet");

        tomcat.start();
        tomcat.getServer().await();

        return tomcat;
    }

    /**
     * Defining DataSource programmatically
     *
     * @return
     */
    private static ContextResource dataSourceResource() {
        ContextResource resource = new ContextResource();
        resource.setName("jdbc/mssqlDS");
        resource.setType(DataSource.class.getName());
        resource.setProperty("driverClassName",
                "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        resource.setProperty("url", "jdbc:sqlserver://<ip>/<db>");
        resource.setProperty("username", "<username>");
        resource.setProperty("password", "<password>");
        return resource;
    }

    private static ServletContainer resourceConfig() {
        return new ServletContainer(new App());
    }
}
