package org.example;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Main {


    public static void main(String[] args) throws ParserConfigurationException, TransformerException, IOException, SAXException {

        Persona personaSola = new Persona("Alberto", 10);

        try (ObjectOutputStream escritor = new ObjectOutputStream(new FileOutputStream("src/main/res/persona.bin", false))) {

                escritor.writeObject(personaSola);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ArrayList<Persona> personas = new ArrayList<>();
        personas.add(new Persona("Alberto", 10));
        personas.add(new Persona("Sergio", 12));
        personas.add(new Persona("Alba", 13));
        personas.add(new Persona("Lydia", 14));

        try (ObjectOutputStream escritor = new ObjectOutputStream(new FileOutputStream("src/main/res/personas.bin", false))) {

            for (Persona persona : personas) {
                escritor.writeObject(persona);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ArrayList<Persona> arraypersonas = bynToList("src/main/res/personas.bin");
        creadorXML(arraypersonas);
        xmlAobjetos("src/main/res/personas.xml");


    }


    public static ArrayList<Persona> bynToList(String ruta){

        ArrayList<Persona> listaPersonas = new ArrayList<>();

        try (ObjectInputStream lector = new ObjectInputStream(new FileInputStream("src/main/res/personas.bin"));) {
            while (true) {
                Object o = lector.readObject();
                if (o instanceof Persona) {
                    listaPersonas.add((Persona) o);
                }
            }

        } catch (EOFException ex) {
            System.out.println("Hemos llegado al final del archivo.");
            for (Persona persona : listaPersonas) {
                System.out.println(persona);
            }
        } catch (IOException ex) {
            System.err.println(ex);
        } catch (ClassNotFoundException ex) {
            System.err.println(ex);
        }

        return listaPersonas;

    }

    public static void creadorXML(ArrayList<Persona> listaPersonas) throws ParserConfigurationException, TransformerException {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        DOMImplementation implementation = db.getDOMImplementation();
        Document documento = implementation.createDocument(null,"personas",null);
        documento.setXmlVersion("1.0");

        for (Persona persona : listaPersonas) {
            Element laPersona = documento.createElement("persona");
            documento.getDocumentElement().appendChild(laPersona);

            Element nombre = documento.createElement("nombre");
            laPersona.appendChild(nombre);

            Text nombreTxt = documento.createTextNode(persona.getNombre());
            nombre.appendChild(nombreTxt);

            Element edad = documento.createElement("edad");
            laPersona.appendChild(edad);

            Text edadTxt = documento.createTextNode(String.valueOf(persona.getEdad()));
            edad.appendChild(edadTxt);

        }

        Source domRaw = new DOMSource(documento);
        Result resultado = new StreamResult(new java.io.File("src/main/res/personas.xml"));
        Transformer transformador = TransformerFactory.newInstance().newTransformer();
        transformador.setOutputProperty(OutputKeys.INDENT, "yes");
        transformador.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformador.transform(domRaw,resultado);
    }

    public static ArrayList<Persona> xmlAobjetos(String ruta) throws ParserConfigurationException, IOException, SAXException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document documento = builder.parse(new File(ruta));
        documento.getDocumentElement().normalize();

        NodeList persona = documento.getElementsByTagName("persona");

        ArrayList<Persona> personas = new ArrayList<>();

        for (int i = 0; i < persona.getLength(); i++) {


            Node nodoPersona = persona.item(i);
            Element elementoPersona = (Element) nodoPersona;


            String elementoNombre = elementoPersona.getElementsByTagName("nombre").item(0).getTextContent();
            String elementoEdad = elementoPersona.getElementsByTagName("edad").item(0).getTextContent();


            personas.add(new Persona(elementoNombre, Integer.parseInt(elementoEdad)));

        }


        return personas;


    }



}