import os
import math
from PIL import Image, ImageDraw

def ensure_dir(d):
    if not os.path.exists(d):
        os.makedirs(d)

def draw_brain_mascot(size, state="happy"):
    img = Image.new("RGBA", (size, size), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)
    
    scale = size / 200.0
    
    if state == "happy":
        brain_color = (255, 160, 185)
        shadow_color = (235, 130, 155)
        fold_color = (215, 105, 135)
        blush_color = (255, 120, 150, 180)
    else:
        brain_color = (210, 175, 180)
        shadow_color = (185, 145, 150)
        fold_color = (160, 120, 125)
        blush_color = (180, 130, 140, 100)

    lobes = [
        (35 * scale, 45 * scale, 105 * scale, 115 * scale),
        (25 * scale, 85 * scale, 100 * scale, 160 * scale),
        (55 * scale, 125 * scale, 115 * scale, 175 * scale),
        (95 * scale, 45 * scale, 165 * scale, 115 * scale),
        (100 * scale, 85 * scale, 175 * scale, 160 * scale),
        (85 * scale, 125 * scale, 145 * scale, 175 * scale),
        (60 * scale, 30 * scale, 110 * scale, 90 * scale),
        (90 * scale, 30 * scale, 140 * scale, 90 * scale),
        (45 * scale, 55 * scale, 155 * scale, 165 * scale)
    ]
    
    for b in lobes:
        draw.ellipse(b, fill=shadow_color)
    
    for b in lobes:
        shrink = 4 * scale
        inner = (b[0] + shrink, b[1] + shrink, b[2] - shrink, b[3] - shrink)
        draw.ellipse(inner, fill=brain_color)
        
    folds = [
        [(100 * scale, 40 * scale), (98 * scale, 80 * scale), (102 * scale, 120 * scale)],
        [(65 * scale, 55 * scale), (80 * scale, 75 * scale), (60 * scale, 95 * scale)],
        [(45 * scale, 90 * scale), (65 * scale, 115 * scale)],
        [(55 * scale, 135 * scale), (80 * scale, 150 * scale)],
        [(135 * scale, 55 * scale), (120 * scale, 75 * scale), (140 * scale, 95 * scale)],
        [(155 * scale, 90 * scale), (135 * scale, 115 * scale)],
        [(145 * scale, 135 * scale), (120 * scale, 150 * scale)]
    ]
    for fold in folds:
        draw.line(fold, fill=fold_color, width=int(max(2, 4 * scale)), joint="curve")
        
    if state == "happy":
        def draw_star(cx, cy, r):
            pts = []
            for i in range(10):
                angle = i * math.pi / 5 - math.pi / 2
                rad = r if i % 2 == 0 else r * 0.45
                pts.append((cx + rad * math.cos(angle), cy + rad * math.sin(angle)))
            draw.polygon(pts, fill=(255, 215, 0), outline=(218, 165, 32))
            
        draw_star(75 * scale, 115 * scale, 15 * scale)
        draw_star(125 * scale, 115 * scale, 15 * scale)
        
        draw.ellipse((52 * scale, 125 * scale, 72 * scale, 137 * scale), fill=blush_color)
        draw.ellipse((128 * scale, 125 * scale, 148 * scale, 137 * scale), fill=blush_color)
        
        draw.arc((85 * scale, 120 * scale, 115 * scale, 145 * scale), start=0, end=180, fill=(70, 30, 40), width=int(max(2, 3 * scale)))
        
        draw.ellipse((82 * scale, 152 * scale, 98 * scale, 168 * scale), fill=shadow_color)
        draw.ellipse((102 * scale, 152 * scale, 118 * scale, 168 * scale), fill=shadow_color)
        draw.ellipse((84 * scale, 154 * scale, 96 * scale, 166 * scale), fill=brain_color)
        draw.ellipse((104 * scale, 154 * scale, 116 * scale, 166 * scale), fill=brain_color)
    else:
        draw.arc((60 * scale, 115 * scale, 88 * scale, 135 * scale), start=0, end=180, fill=(130, 90, 100), width=int(max(1, 3 * scale)))
        draw.arc((112 * scale, 115 * scale, 140 * scale, 135 * scale), start=0, end=180, fill=(130, 90, 100), width=int(max(1, 3 * scale)))
        
        draw.ellipse((65 * scale, 108 * scale, 83 * scale, 122 * scale), fill=(60, 45, 50))
        draw.ellipse((117 * scale, 108 * scale, 135 * scale, 122 * scale), fill=(60, 45, 50))
        draw.chord((64 * scale, 106 * scale, 84 * scale, 120 * scale), start=180, end=360, fill=brain_color)
        draw.chord((116 * scale, 106 * scale, 136 * scale, 120 * scale), start=180, end=360, fill=brain_color)
        draw.line([(64 * scale, 114 * scale), (84 * scale, 114 * scale)], fill=fold_color, width=int(max(1, 2 * scale)))
        draw.line([(116 * scale, 114 * scale), (136 * scale, 114 * scale)], fill=fold_color, width=int(max(1, 2 * scale)))
        
        draw.arc((86 * scale, 132 * scale, 114 * scale, 150 * scale), start=190, end=350, fill=(70, 40, 45), width=int(max(2, 3 * scale)))
        
        draw.ellipse((70 * scale, 158 * scale, 86 * scale, 172 * scale), fill=shadow_color)
        draw.ellipse((114 * scale, 158 * scale, 130 * scale, 172 * scale), fill=shadow_color)
        
    return img

def create_launcher_icon(size, is_round=False):
    img = Image.new("RGBA", (size, size), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)
    
    pad = int(size * 0.04)
    if is_round:
        draw.ellipse((pad, pad, size - pad, size - pad), fill=(26, 20, 18), outline=(217, 119, 6), width=int(max(1, size * 0.03)))
    else:
        radius = int(size * 0.22)
        draw.rounded_rectangle((pad, pad, size - pad, size - pad), radius=radius, fill=(26, 20, 18), outline=(217, 119, 6), width=int(max(1, size * 0.03)))
    
    mascot_size = int(size * 0.76)
    mascot = draw_brain_mascot(mascot_size, state="happy")
    offset = int((size - mascot_size) / 2)
    img.paste(mascot, (offset, offset + int(size * 0.02)), mascot)
    return img

def main():
    res_dir = r"D:\vibe coding project\ScrollBattle\app\src\main\res"
    
    drawable_dir = os.path.join(res_dir, "drawable")
    ensure_dir(drawable_dir)
    
    happy_img = draw_brain_mascot(256, state="happy")
    happy_img.save(os.path.join(drawable_dir, "brain_happy.png"))
    
    tired_img = draw_brain_mascot(256, state="tired")
    tired_img.save(os.path.join(drawable_dir, "brain_tired.png"))
    
    icon_img = draw_brain_mascot(128, state="happy")
    icon_img.save(os.path.join(drawable_dir, "brain_icon.png"))
    
    tired_icon = draw_brain_mascot(128, state="tired")
    tired_icon.save(os.path.join(drawable_dir, "brain_icon_tired.png"))
    
    print("Mascot drawables saved.")
    
    densities = {
        "mipmap-mdpi": 48,
        "mipmap-hdpi": 72,
        "mipmap-xhdpi": 96,
        "mipmap-xxhdpi": 144,
        "mipmap-xxxhdpi": 192
    }
    
    for folder, size in densities.items():
        target_dir = os.path.join(res_dir, folder)
        ensure_dir(target_dir)
        
        launcher = create_launcher_icon(size, is_round=False)
        launcher.save(os.path.join(target_dir, "ic_launcher.png"))
        
        launcher_round = create_launcher_icon(size, is_round=True)
        launcher_round.save(os.path.join(target_dir, "ic_launcher_round.png"))
        
    print("Launcher icons generated across all mipmap densities.")

if __name__ == "__main__":
    main()
