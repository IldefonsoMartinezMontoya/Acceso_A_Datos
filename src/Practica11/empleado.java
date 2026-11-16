package Practica11;

public class empleado {
    private Integer dept_no;
    private String oficio;
    private Double maxSalario;
    private Double minSalario;
    private String fechaAlta;

    public empleado(Integer dept_no, String oficio, Double maxSalario,
                    Double minSalario, String fechaAlta) {
        this.dept_no = dept_no;
        this.oficio = oficio;
        this.maxSalario = maxSalario;
        this.minSalario = minSalario;
        this.fechaAlta = fechaAlta;
    }

    public Integer getDept_no() {
        return dept_no;
    }

    public String getOficio() {
        return oficio;
    }

    public Double getMaxSalario() {
        return maxSalario;
    }

    public Double getMinSalario() {
        return minSalario;
    }

    public String getFechaAlta() {
        return fechaAlta;
    }
}
