package Practica5;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.SchemaOutputResolver;
import org.glassfish.jaxb.runtime.v2.runtime.output.StAXExStreamWriterOutput;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CrearXMLJAXB {
    private List<Deposito> depositos = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) throws JAXBException {
        ListaDeposito lista = new ListaDeposito();
        System.out.println("Dime el diametro");
        float diametro = sc.nextFloat();
        System.out.println("Dime la altura");
        float altura = sc.nextFloat();
        System.out.println("Dime el id");
        String id = sc.nextLine();
        lista.getDepositos().add(new Deposito(diametro, altura, id));

        JAXBContext contexto = JAXBContext.newInstance(ListaDeposito.class);
        Marshaller marshaller = contexto.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(lista, new File("src/Practica5/CrearXMLJAXB.java"));
    }
}
