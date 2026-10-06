package Practica5;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Pizza {
    private String nombre;
    private double precio;
    private String[] ingrediente;

    public Pizza() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String[] getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(String[] ingrediente) {
        this.ingrediente = ingrediente;
    }
}
