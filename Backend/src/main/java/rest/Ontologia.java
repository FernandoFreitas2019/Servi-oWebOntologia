package rest;

import Controller.OntologiaController;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

@Path("/teste")
public class Ontologia {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIndividuosClass() throws OWLOntologyCreationException {
        //OntologiaController.getAllIndividualOfClass("pessoa");

        return "asd";
    }
}
