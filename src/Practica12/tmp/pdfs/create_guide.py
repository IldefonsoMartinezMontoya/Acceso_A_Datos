from reportlab.lib import colors
from reportlab.lib.enums import TA_CENTER
from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.lib.units import mm
from reportlab.platypus import (
    SimpleDocTemplate, Paragraph, Spacer, Preformatted, Table, TableStyle,
    PageBreak, KeepTogether
)


OUT = "output/pdf/guia_funcion_y_procedimiento_mysql_jdbc.pdf"

NAVY = colors.HexColor("#17324D")
BLUE = colors.HexColor("#245B82")
PALE = colors.HexColor("#F1F5F8")
INK = colors.HexColor("#24313D")
MUTED = colors.HexColor("#5C6B78")
RULE = colors.HexColor("#D8E1E8")

styles = getSampleStyleSheet()
styles.add(ParagraphStyle(
    name="DocTitle", parent=styles["Title"], fontName="Helvetica-Bold",
    fontSize=23, leading=28, textColor=NAVY, alignment=TA_CENTER,
    spaceAfter=7 * mm
))
styles.add(ParagraphStyle(
    name="SubTitle", parent=styles["Normal"], fontName="Helvetica",
    fontSize=10, leading=15, textColor=MUTED, alignment=TA_CENTER,
    spaceAfter=8 * mm
))
styles.add(ParagraphStyle(
    name="Section", parent=styles["Heading2"], fontName="Helvetica-Bold",
    fontSize=15, leading=19, textColor=BLUE, spaceBefore=2 * mm,
    spaceAfter=2.5 * mm
))
styles.add(ParagraphStyle(
    name="BodyX", parent=styles["BodyText"], fontName="Helvetica",
    fontSize=9.5, leading=14, textColor=INK, spaceAfter=3 * mm
))
styles.add(ParagraphStyle(
    name="SmallNote", parent=styles["BodyText"], fontName="Helvetica",
    fontSize=8.4, leading=12, textColor=MUTED, spaceAfter=2 * mm
))
styles.add(ParagraphStyle(
    name="Callout", parent=styles["BodyText"], fontName="Helvetica",
    fontSize=9.3, leading=14, textColor=NAVY
))


def codebox(code, font_size=7.4):
    pre = Preformatted(
        code.strip("\n"),
        ParagraphStyle(
            name=f"Code{font_size}", fontName="Courier",
            fontSize=font_size, leading=font_size * 1.33,
            textColor=INK, leftIndent=0, rightIndent=0,
            spaceBefore=0, spaceAfter=0
        ),
        maxLineLength=105,
    )
    box = Table([[pre]], colWidths=[170 * mm])
    box.setStyle(TableStyle([
        ("BACKGROUND", (0, 0), (-1, -1), PALE),
        ("BOX", (0, 0), (-1, -1), 0.5, RULE),
        ("LEFTPADDING", (0, 0), (-1, -1), 8),
        ("RIGHTPADDING", (0, 0), (-1, -1), 8),
        ("TOPPADDING", (0, 0), (-1, -1), 7),
        ("BOTTOMPADDING", (0, 0), (-1, -1), 7),
    ]))
    return box


def callout(text):
    box = Table([[Paragraph(text, styles["Callout"])]], colWidths=[170 * mm])
    box.setStyle(TableStyle([
        ("BACKGROUND", (0, 0), (-1, -1), colors.HexColor("#EAF2F8")),
        ("BOX", (0, 0), (-1, -1), 0.5, colors.HexColor("#C6D8E6")),
        ("LEFTPADDING", (0, 0), (-1, -1), 9),
        ("RIGHTPADDING", (0, 0), (-1, -1), 9),
        ("TOPPADDING", (0, 0), (-1, -1), 7),
        ("BOTTOMPADDING", (0, 0), (-1, -1), 7),
    ]))
    return box


def draw_page(canvas, doc):
    canvas.saveState()
    width, height = A4
    canvas.setStrokeColor(RULE)
    canvas.setLineWidth(0.5)
    canvas.line(20 * mm, height - 14 * mm, width - 20 * mm, height - 14 * mm)
    canvas.setFont("Helvetica", 8)
    canvas.setFillColor(MUTED)
    canvas.drawString(20 * mm, height - 11 * mm, "JDBC Y rutinas almacenadas en MySQL")
    canvas.line(20 * mm, 13 * mm, width - 20 * mm, 13 * mm)
    canvas.drawString(20 * mm, 8 * mm, "Guia de CalcularSalario.java")
    canvas.drawRightString(width - 20 * mm, 8 * mm, f"Pagina {doc.page}")
    canvas.restoreState()


doc = SimpleDocTemplate(
    OUT, pagesize=A4,
    rightMargin=20 * mm, leftMargin=20 * mm,
    topMargin=20 * mm, bottomMargin=19 * mm,
    title="Funcion y procedimiento MySQL desde Java",
    author="Guia de apoyo"
)

story = []

# Page 1: purpose, connection, and program flow.
story.extend([
    Spacer(1, 5 * mm),
    Paragraph("Función y procedimiento<br/>MySQL desde Java", styles["DocTitle"]),
    Paragraph("Guía breve de las partes principales de CalcularSalario.java", styles["SubTitle"]),
    callout(
        "<b>Objetivo:</b> introducir un número de departamento y obtener el salario "
        "máximo de sus empleados. La función devuelve -1 si el departamento no "
        "existe y 0 si existe pero no tiene empleados. El procedimiento añade el "
        "recuento de empleados y el salario medio."
    ),
    Spacer(1, 6 * mm),
    Paragraph("1. Conexión y flujo principal", styles["Section"]),
    Paragraph(
        "La URL identifica la base de datos. El usuario y la contraseña deben "
        "coincidir con la configuración de MySQL. El bloque try-with-resources "
        "cierra la conexión y el Scanner automáticamente. El programa crea las "
        "rutinas, pide el departamento y llama a los métodos que las invocan.",
        styles["BodyX"]
    ),
    codebox(r'''private static final String URL =
    "jdbc:mysql://localhost:3306/practica";
private static final String USER = "TU_USUARIO";
private static final String PASS = "TU_CONTRASENA";

public static void main(String[] args) {
    try (Connection con = DriverManager.getConnection(URL, USER, PASS);
         Scanner sc = new Scanner(System.in)) {
        crearFuncion(con);
        crearProcedimiento(con);

        System.out.print("Numero de departamento: ");
        int deptNo = sc.nextInt();

        mostrarSalarioMaximo(con, deptNo);
        mostrarEstadisticas(con, deptNo);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}''', 7.5),
    Spacer(1, 4 * mm),
    Paragraph(
        "En este ejemplo las dos rutinas reciben el mismo departamento. Si el "
        "ejercicio solo pide mostrar el salario máximo, la llamada a "
        "<font name='Courier'>mostrarEstadisticas</font> es la parte adicional "
        "del procedimiento.",
        styles["SmallNote"]
    ),
    PageBreak(),
])

# Page 2: function creation and invocation.
story.extend([
    Paragraph("2. Función calcular_salario_maximo", styles["Section"]),
    Paragraph(
        "La función comprueba primero si el departamento existe. Si existe, "
        "busca el mayor salario asociado. COALESCE convierte el resultado nulo "
        "de MAX en 0 cuando no hay empleados. READS SQL DATA declara que la "
        "función consulta datos. Desde Java se envía el CREATE FUNCTION completo "
        "con Statement.execute(); no se escribe DELIMITER.",
        styles["BodyX"]
    ),
    codebox(r'''private static final String CREAR_FUNCION = """
    CREATE FUNCTION calcular_salario_maximo(pdept_no INT)
    RETURNS DECIMAL(10,2)
    READS SQL DATA
    BEGIN
        DECLARE existe INT DEFAULT 0;
        DECLARE maximo DECIMAL(10,2) DEFAULT NULL;

        SELECT COUNT(*) INTO existe
        FROM departamentos WHERE dept_no = pdept_no;

        IF existe = 0 THEN
            RETURN -1.00;
        END IF;

        SELECT MAX(salario) INTO maximo
        FROM empleados WHERE dept_no = pdept_no;

        RETURN COALESCE(maximo, 0.00);
    END
    """;

private static void crearFuncion(Connection con) throws SQLException {
    try (Statement st = con.createStatement()) {
        st.execute("DROP FUNCTION IF EXISTS calcular_salario_maximo");
        st.execute(CREAR_FUNCION);
    }
}''', 7.2),
    Spacer(1, 5 * mm),
    Paragraph("3. Llamada JDBC a la función", styles["Section"]),
    Paragraph(
        "CallableStatement permite invocar la función. El parámetro 1 es el "
        "valor de retorno DECIMAL y el parámetro 2 es el número de departamento. "
        "Después de execute(), getBigDecimal(1) recupera el salario máximo o el "
        "valor especial.",
        styles["BodyX"]
    ),
    codebox(r'''private static void mostrarSalarioMaximo(
        Connection con, int deptNo) throws SQLException {
    String llamada = "{? = call calcular_salario_maximo(?)}";

    try (CallableStatement cs = con.prepareCall(llamada)) {
        cs.registerOutParameter(1, Types.DECIMAL);
        cs.setInt(2, deptNo);
        cs.execute();

        BigDecimal maximo = cs.getBigDecimal(1);
        if (maximo.compareTo(BigDecimal.valueOf(-1)) == 0) {
            System.out.println("No existe");
        } else if (maximo.compareTo(BigDecimal.ZERO) == 0) {
            System.out.println("Sin empleados");
        } else {
            System.out.println("Salario maximo: " + maximo);
        }
    }
}''', 7.2),
    PageBreak(),
])

# Page 3: procedure, invocation, and integration notes.
story.extend([
    Paragraph("4. Procedimiento de estadísticas", styles["Section"]),
    Paragraph(
        "El procedimiento recibe el departamento como IN y entrega el número de "
        "empleados y el salario medio mediante dos parámetros OUT. Para un "
        "departamento inexistente asigna -1 a ambos; si existe sin empleados, "
        "COUNT y COALESCE devuelven 0.",
        styles["BodyX"]
    ),
    codebox(r'''private static final String CREAR_PROCEDIMIENTO = """
    CREATE PROCEDURE calcular_estadisticas_departamento(
        IN pdept_no INT, OUT pnum_empleados INT,
        OUT psalario_medio DECIMAL(10,2))
    READS SQL DATA
    BEGIN
        DECLARE existe INT DEFAULT 0;
        SELECT COUNT(*) INTO existe FROM departamentos
        WHERE dept_no = pdept_no;
        IF existe = 0 THEN
            SET pnum_empleados = -1;
            SET psalario_medio = -1.00;
        ELSE
            SELECT COUNT(*), COALESCE(AVG(salario), 0.00)
            INTO pnum_empleados, psalario_medio
            FROM empleados
            WHERE dept_no = pdept_no;
        END IF;
    END
    """;

private static void crearProcedimiento(Connection con) throws SQLException {
    try (Statement st = con.createStatement()) {
        st.execute("DROP PROCEDURE IF EXISTS calcular_estadisticas_departamento");
        st.execute(CREAR_PROCEDIMIENTO);
    }
}''', 7.0),
    Spacer(1, 4 * mm),
    Paragraph("5. Llamada JDBC al procedimiento", styles["Section"]),
    Paragraph(
        "Se asigna el departamento al parámetro 1 y se registran los parámetros "
        "2 y 3 como salidas. Tras execute(), getInt y getBigDecimal leen esos "
        "valores.",
        styles["BodyX"]
    ),
    codebox(r'''private static void mostrarEstadisticas(Connection con, int deptNo)
        throws SQLException {
    try (CallableStatement cs = con.prepareCall(
            "{call calcular_estadisticas_departamento(?, ?, ?)}")) {
        cs.setInt(1, deptNo);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.DECIMAL);
        cs.execute();
        int empleados = cs.getInt(2);
        BigDecimal media = cs.getBigDecimal(3);
        if (empleados == -1) {
            System.out.println("No existe");
        } else if (empleados == 0) {
            System.out.println("Sin empleados");
        } else {
            System.out.println("Empleados: " + empleados
                    + ", salario medio: " + media);
        }
    }
}''', 7.0),
    Spacer(1, 4 * mm),
    callout(
        "<b>Al integrar los fragmentos:</b> el nombre usado en CREATE PROCEDURE "
        "debe ser idéntico al de prepareCall. Cierra cada Statement, "
        "CallableStatement, ResultSet y Connection con try-with-resources. "
        "Los nombres de tabla y columnas del ejemplo son departamentos.dept_no, "
        "empleados.dept_no y empleados.salario."
    ),
])

doc.build(story, onFirstPage=draw_page, onLaterPages=draw_page)
print(OUT)
