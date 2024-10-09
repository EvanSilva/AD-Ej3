package org.example;

import org.w3c.dom.DOMImplementation;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Main {


    public static void main(String[] args) {

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


    }

    public ArrayList<Persona> bynToList(String ruta){

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

    public void creadorXML(ArrayList<Persona> listaPersonas) throws ParserConfigurationException {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        DOMImplementation implementation = db.getDOMImplementation();
        documento = implementation.createDocument(null,"src/main/java/res/",null);
        documento.setXmlVersion("1.0");

    }



}