package Practica5;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "depositos")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListaDeposito {
    @XmlElement(name = "deposito")
    private List<Deposito> depositos;

    public ListaDeposito() {
    }

    public List<Deposito> getDepositos() {
        return depositos;
    }
}
