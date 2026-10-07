package Practica5;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "pizzas")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListaPizzas {
    protected List<Pizza> pizza;
    public ListaPizzas() {}

    public List<Pizza> getPizza() {
        return pizza;
    }
}
