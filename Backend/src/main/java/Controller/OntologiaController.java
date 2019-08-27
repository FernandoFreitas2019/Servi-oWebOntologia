package Controller;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.model.*;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.semanticweb.owlapi.util.OWLEntityRemover;
import uk.ac.manchester.cs.owlapi.dlsyntax.DLSyntaxObjectRenderer;


public class OntologiaController {
    
    static String owlName="OntologiaBd.owl";
    static String path = "D:\\Mestrado em Engenharia de Software\\Banco de Dados";
    static String uri = "http://www.semanticweb.org/fernandofreitas/ontologies/2019/7/unititle-ontology-41#";
    static File file = new File(path+owlName);
    
    static OWLOntologyManager manager;
    static OWLOntology ontology;
    static OWLReasonerFactory reasonerFactory;
    static OWLObjectRenderer renderer;
    static OWLReasoner reasoner;
    static OWLDataFactory factory;
    
    static boolean prepareOwlApi = false;
    
    private static void prepareOWLApi() throws OWLOntologyCreationException {
        manager = OWLManager.createOWLOntologyManager();
        ontology = manager.loadOntologyFromOntologyDocument(file);
        reasonerFactory = PelletReasonerFactory.getInstance();
        renderer = new DLSyntaxObjectRenderer();
        reasoner = reasonerFactory.createReasoner(ontology, new SimpleConfiguration());
        factory = manager.getOWLDataFactory();
        
        prepareOwlApi = true;
    }
    
    public static List<String> getAllIndividualOfClass(String className) throws OWLOntologyCreationException{
        
        if(!prepareOwlApi) prepareOWLApi();
        
        List<String> indClass= new ArrayList<>();

        OWLClass topicClass = factory.getOWLClass(IRI.create(uri + className));

        for (OWLNamedIndividual topicInd : reasoner.getInstances(topicClass, false).getFlattened()) {
            indClass.add(renderer.render(topicInd));
        }

        return indClass;
        
    }
    
    public static List<String> getAllTargetOfIndividualOP(String indName, String objectProperty) throws OWLOntologyCreationException{
        
        if(!prepareOwlApi) prepareOWLApi();
        
        OWLNamedIndividual ind = factory.getOWLNamedIndividual(IRI.create(uri + indName));
        OWLObjectProperty p = factory.getOWLObjectProperty(IRI.create(uri + objectProperty));

        List<String> temp = new ArrayList<>();
        for (OWLNamedIndividual indTarget : reasoner.getObjectPropertyValues(ind, p).getFlattened()) {
            temp.add(renderer.render(indTarget));
        }
        return temp;
    }
    
    public static String getFirstTargetOfIndividual(String indName, String objectProperty) throws OWLOntologyCreationException{
        
        if(!prepareOwlApi) prepareOWLApi();
        
        OWLNamedIndividual ind = factory.getOWLNamedIndividual(IRI.create(uri + indName));
        OWLObjectProperty p = factory.getOWLObjectProperty(IRI.create(uri + objectProperty));

        if(reasoner.getObjectPropertyValues(ind, p).getFlattened().iterator().hasNext()){
            return renderer.render(reasoner.getObjectPropertyValues(ind, p).getFlattened().iterator().next());
        }else{
            return "";
        }
        
    }
    
    public static List<String> getAllTargetOfIndividualDP(String indName, String dataProperty) throws OWLOntologyCreationException{
        
        if(!prepareOwlApi) prepareOWLApi();
        
        OWLNamedIndividual ind = factory.getOWLNamedIndividual(IRI.create(uri + indName));
        OWLDataProperty d = factory.getOWLDataProperty(IRI.create(uri + dataProperty));

        List<String> temp = new ArrayList<>();
        for (OWLLiteral indTarget : reasoner.getDataPropertyValues(ind, d)) {
            temp.add(renderer.render(indTarget));
        }
        return temp;
    }
    
    public static String getFirstTargetOfIndividualDP(String indName, String dataProperty) throws OWLOntologyCreationException{
        
        if(!prepareOwlApi) prepareOWLApi();
        
        OWLNamedIndividual ind = factory.getOWLNamedIndividual(IRI.create(uri + indName));
        OWLDataProperty d = factory.getOWLDataProperty(IRI.create(uri + dataProperty));

        if(reasoner.getDataPropertyValues(ind, d).iterator().hasNext()){
            return renderer.render(reasoner.getDataPropertyValues(ind, d).iterator().next());
        }else{
            return "";
        }
        
    }
    
    public static void saveInd(String className, String indName) throws OWLOntologyCreationException, OWLOntologyStorageException{
        
        if(!prepareOwlApi) prepareOWLApi();
        
        OWLClass classOwl = factory.getOWLClass(IRI.create(uri + className));
        OWLNamedIndividual ind = factory.getOWLNamedIndividual(IRI.create(uri + indName));
        
        OWLClassAssertionAxiom axiom = factory.getOWLClassAssertionAxiom(classOwl, ind);
        AddAxiom addAxiom = new AddAxiom(ontology, axiom);
        manager.applyChange(addAxiom);
        
        manager.saveOntology(ontology);
        
    }
    
    public static void createObjectPropertybetweenTwoIndividuals(String indName1, String objectProperty, String indName2) throws OWLOntologyCreationException, OWLOntologyStorageException{
        
        if(!prepareOwlApi) prepareOWLApi();
        
        OWLNamedIndividual ind1 = factory.getOWLNamedIndividual(IRI.create(uri + indName1));
        OWLNamedIndividual ind2 = factory.getOWLNamedIndividual(IRI.create(uri + indName2));
        
        OWLObjectProperty p = factory.getOWLObjectProperty(IRI.create(uri + objectProperty));
        
        OWLObjectPropertyAssertionAxiom axiom = factory.getOWLObjectPropertyAssertionAxiom(p, ind1, ind2);
        AddAxiom addAxiom = new AddAxiom(ontology, axiom);
        manager.applyChange(addAxiom);
        
        manager.saveOntology(ontology);
        
    }
    
    
    public static void createDataPropertybetweenOneIndividualAndOneValue(String indName1, String dataProperty, String value) throws OWLOntologyCreationException, OWLOntologyStorageException{
        
        if(!prepareOwlApi) prepareOWLApi();
        
        OWLNamedIndividual ind = factory.getOWLNamedIndividual(IRI.create(uri + indName1));
        
        OWLDataProperty d = factory.getOWLDataProperty(IRI.create(uri + dataProperty));
        
        OWLDataPropertyAssertionAxiom axiom = factory.getOWLDataPropertyAssertionAxiom(d, ind, value); // A tipagem do "value" depende do tipo colocado na ontologia
        AddAxiom addAxiom = new AddAxiom(ontology, axiom);
        manager.applyChange(addAxiom);
        
        manager.saveOntology(ontology);
        
    }
    
    
    
    
    
    
    
    
    
    
    
}
