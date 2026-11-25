from pathlib import Path

from reportlab.lib.enums import TA_LEFT
from reportlab.lib.pagesizes import A4, landscape
from reportlab.lib.styles import ParagraphStyle
from reportlab.platypus import Image, PageBreak, Paragraph, Preformatted, SimpleDocTemplate

ROOT = Path(r"C:\Users\estudiante\IdeaProjects\JavaClase2")
OUT = ROOT / "output" / "pdf" / "Apellidos_Nombre_P3_1.pdf"
CAPTURES = ROOT / "output" / "pdf" / "captures"

page_w, page_h = landscape(A4)
left = right = 30
top = bottom = 28
usable_w = page_w - left - right
usable_h = page_h - top - bottom

doc = SimpleDocTemplate(
    str(OUT),
    pagesize=landscape(A4),
    rightMargin=right,
    leftMargin=left,
    topMargin=top,
    bottomMargin=bottom,
    title="Práctica 3.1 - Configuración de Hibernate",
    author="",
)

heading = ParagraphStyle(
    "heading",
    fontName="Helvetica",
    fontSize=13,
    leading=16,
    textColor="#111111",
    spaceAfter=9,
)
code_style = ParagraphStyle(
    "code",
    fontName="Courier",
    fontSize=9,
    leading=10.5,
    textColor="#111111",
    alignment=TA_LEFT,
    leftIndent=0,
    rightIndent=0,
    spaceAfter=0,
)

story = []
screens = [
    ("Configuración del proyecto", "01_project_config.jpg"),
    ("Dependencias del pom.xml", "04_pom_dependencies.jpg"),
    ("Test Connection - PostgreSQL (escuela)", "02_test_postgres.jpg"),
    ("Test Connection - MySQL (practica)", "03_test_mysql.jpg"),
    ("Ejecución - MySQL y PostgreSQL", "05_ejecucion_dos_bases.jpg"),
]
for title, filename in screens:
    story.append(Paragraph(title, heading))
    img = Image(str(CAPTURES / filename))
    scale = min(usable_w / img.imageWidth, (usable_h - 28) / img.imageHeight)
    img.drawWidth = img.imageWidth * scale
    img.drawHeight = img.imageHeight * scale
    img.hAlign = "CENTER"
    story.append(img)
    story.append(PageBreak())

pom = (ROOT / "pom.xml").read_text(encoding="utf-8-sig").rstrip().splitlines()
story.append(Paragraph("pom.xml", heading))
story.append(Preformatted("\n".join(pom[:40]), code_style))
story.append(PageBreak())
story.append(Paragraph("pom.xml (continuación)", heading))
story.append(Preformatted("\n".join(pom[40:]), code_style))
story.append(PageBreak())

sources = [
    ("hibernate-mysql.cfg.xml", ROOT / "src" / "main" / "resources" / "hibernate-mysql.cfg.xml"),
    ("hibernate-postgres.cfg.xml", ROOT / "src" / "main" / "resources" / "hibernate-postgres.cfg.xml"),
    ("PruebaConexion.java", ROOT / "src" / "PruebaConexion" / "PruebaConexion.java"),
]
for index, (title, path) in enumerate(sources):
    story.append(Paragraph(title, heading))
    story.append(Preformatted(path.read_text(encoding="utf-8-sig").rstrip(), code_style))
    if index != len(sources) - 1:
        story.append(PageBreak())

doc.build(story)
print(OUT)
