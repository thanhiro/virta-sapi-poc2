package fi.csc.virta.julkaisut;

import fi.csc.virta.julkaisut.resource.MyResource;
import fi.csc.virta.julkaisut.resource.Root;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class App extends ResourceConfig {
    public App() {
        super(
            Root.class,
            MyResource.class,
            JacksonFeature.class
        );
    }
}
