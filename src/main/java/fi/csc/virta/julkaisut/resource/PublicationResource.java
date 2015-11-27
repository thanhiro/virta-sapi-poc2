package fi.csc.virta.julkaisut.resource;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Test resource
 */
@Path("publications")
public class PublicationResource {

    protected Connection getConnection() throws SQLException, NamingException {
        InitialContext ic = new InitialContext();
        DataSource ds = (DataSource) ic.lookup("java:comp/env/jdbc/mssqlDS");
        return ds.getConnection();
    }

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return Response that will be returned as a stream of application/json.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() throws SQLException, NamingException {
        Connection db = getConnection();

        final Map<Integer, String> publications = new HashMap<>();
        publications.put(1, "Design Patterns");
        publications.put(2, "Java for Dummies");

        StreamingOutput stream = os -> {
            JsonGenerator jg = new ObjectMapper().getFactory()
                    .createGenerator(os, JsonEncoding.UTF8);
            jg.writeStartArray();
            for (Map.Entry<Integer, String> person : publications.entrySet()) {
                jg.writeStartObject();
                jg.writeFieldName("id");
                jg.writeString(person.getKey().toString());
                jg.writeFieldName("name");
                jg.writeString(person.getValue());
                jg.writeEndObject();
            }
            jg.writeEndArray();

            jg.flush();
            jg.close();
        };

        return Response.ok().entity(stream)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
