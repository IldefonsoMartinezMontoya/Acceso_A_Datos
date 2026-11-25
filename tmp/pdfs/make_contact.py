from pathlib import Path
from PIL import Image, ImageDraw

folder = Path(r"C:\Users\estudiante\IdeaProjects\JavaClase2\tmp\pdfs")
files = [folder / f"page-{i:02}.png" for i in range(1, 11)]
thumb_w = 320
gap = 14
label_h = 25
thumbs = []
for i, path in enumerate(files, 1):
    im = Image.open(path).convert("RGB")
    thumb_h = round(im.height * thumb_w / im.width)
    im = im.resize((thumb_w, thumb_h), Image.Resampling.LANCZOS)
    thumbs.append((i, im))
cell_h = max(im.height for _, im in thumbs) + label_h
sheet = Image.new("RGB", (5 * thumb_w + 6 * gap, 2 * cell_h + 3 * gap), "white")
draw = ImageDraw.Draw(sheet)
for idx, im in thumbs:
    row = (idx - 1) // 5
    col = (idx - 1) % 5
    x = gap + col * (thumb_w + gap)
    y = gap + row * (cell_h + gap)
    draw.text((x, y), f"Page {idx}", fill="black")
    sheet.paste(im, (x, y + label_h))
sheet.save(folder / "submission_contact.png")
