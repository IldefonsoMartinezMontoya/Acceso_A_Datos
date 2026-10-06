package Practica5;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "pizzas")
@XmlAccessorType(XmlAccessType.FIELD)
public class Contenedor {
    protected List<Pizza> pizza;
    public Contenedor() {}

    public List<Pizza> getPizza() {
        return pizza;
    }
}
