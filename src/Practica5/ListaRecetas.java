package Practica5;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "recetas")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListaRecetas {
    protected List<Receta> receta;
    public List<Receta> getReceta() {
        return receta;
    }
}
