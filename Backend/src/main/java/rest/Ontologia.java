package rest;

import Controller.OntologiaController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

@Path("/ontologia")
public class Ontologia {

    @GET
    @Path("/individuosByClass")
    @Produces("application/json")
    public List<String> getIndividuosClass() {
        //OntologiaController.getAllIndividualOfClass("pessoa");
        return Arrays.asList("a", "b");
    }
}
