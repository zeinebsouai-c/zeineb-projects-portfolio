# Vintage Living Room in Three.js

**Created by:** Zeineb Souiai
**Email:** zeineb.souiai@student.htw-berlin.de

This project immerses viewers in a meticulously designed vintage living room, brought to life with Three.js. The goal is to recreate the cozy ambiance of a 1970s living space while showcasing the functionality of iconic devices from the era. Interactive elements such as softly glowing lamps, a flickering television with animated static, and a spinning record player synced to music invite users to engage with the scene, creating an authentic and nostalgic experience.

## Features

1) Scene Setup:
- Utilized THREE.Scene for creating the 3D space.
- PerspectiveCamera was set up for realistic viewing angles.
- WebGLRenderer renders the scene with shadow support.

2) Interactive Elements:
- Hover highlights and pointer cursor for interactive objects.
- Toggle functionality for lamps, stereo music, and a television with static noise.

3) Dynamic Animations:
- Record player spins when music is toggled.
- Television displays animated static noise.
- Lamps emit ambient light when turned on.

4) Custom Materials:
- Sofa Upholstery:
    - Custom textures and repeating patterns applied to sofas using THREE.TextureLoader and MeshStandardMaterial.
    - Texture: textures/sofa_pattern.png
- Wallpaper:
    - Used THREE.TextureLoader to apply repeating patterns on walls.
    - Texture: textures/pattern5.jpg (back wall) and textures/pattern9.jpg (side walls).
- Flooring:
    - High-quality wooden floor texture with shadow reception for added depth.
    - Texture: textures/wooden_floor3.jpg
- Rug:
    - Blended two textures (alphaMap and map) for a fabric-like appearance.
    - Textures: textures/brug.jpg (pattern) and textures/carpet_texture.jpg (fabric).


## Technologies Used

### Three.js
- Core 3D rendering and animation.
Source: Three.js Official Website: https://threejs.org/

### Shader Material
- Custom THREE.ShaderMaterial for animated TV static.
- Fragment Shader Logic:
    - Noise generation via sin and dot functions.
    - Used uniform values (time and active) for interactivity.
Source: https://gist.github.com/zouloux/dddd0c48f632077a20dc?utm_source=chatgpt.com

### Texture Loading
Used THREE.TextureLoader to apply textures to walls, floor, and props.
Example textures:
- Walls: textures/pattern5.jpg
- Floor: textures/wooden_floor3.jpg
- Rug: textures/brug.jpg
Source: Three.js TextureLoader Documentation: https://threejs.org/docs/#api/en/loaders/TextureLoader

### Lighting
- Directional and ambient lighting for realistic shadows and ambient glow.
- Shadow settings optimized for performance.
Source: 
- Three.js Ambient Lights: https://threejs.org/docs/#api/en/lights/AmbientLight
- Three.js Directional Light: https://threejs.org/docs/#api/en/lights/DirectionalLight

### Raycaster
Used for detecting mouse interactions with objects.
Example:
- Hover effects (highlight and pointer cursor).
- Click functionality for toggling lamps, TV, and stereo.
Source: 
- Three.js Raycaster Documentation: https://threejs.org/docs/#api/en/core/Raycaster
- Raycasting and Hover Effect: https://stackoverflow.com/questions/78775542/raycasting-and-hover-effect

### OrbitControls
- Enabled smooth panning, zooming, and rotation.
- Damping for smoother transitions.
Source: OrbitControls in Three.js: https://threejs.org/docs/#examples/en/controls/OrbitControls

### GLTFLoader
Imported furniture and props from .gltf models:
- Sofa: furniture/sofa_gltf/scene.gltf
- Floor Lamp 1 and 2: furniture/floor lamp/Wooden_Floor_Lamp_vi5hbjgga_Raw.gltf
- Stereo: furniture/vintage_stereo_hi_fi_stack_w_speakers_gltf/scene.gltf
- Egg Chair: furniture/egg_chair/scene.gltf
- Corner Sofa: furniture/vintage_sofa_23mb_gltf/scene.gltf
- TV: furniture/1970s_vintage_television/scene.gltf
- TV Table: furniture/table3/scene.gltf
- Books: furniture/books/scene.gltf
- Center Table: furniture/table4/scene.gltf
Source: GLTFLoader Documentation: https://threejs.org/docs/#examples/en/loaders/GLTFLoader

### Posters and Decorative Elements
- Loaded multiple textures for posters on walls.
- Positioned them dynamically using THREE.PlaneGeometry.


## Interactivity Details

1) Hover Highlight Effect:
- Subtle white highlight using emissive and opacity in MeshStandardMaterial.
- Dynamically updated materials using raycaster.

2) Click Actions:
- TV toggles static animation.
- Lamps toggle light intensity and visibility.
- Stereo toggles music and record spinning.

## Challenges and Limitations

During the development of the project, one notable challenge was rendering a high number of detailed objects with interactive features. While designing the vintage living room, the combination of multiple 3D models, textures, lighting effects, and dynamic animations created a highly immersive scene. However, due to the limited hardware capabilities of the development machine, the rendering process occasionally caused a lower frame rate, which is reflected in the recorded video.

This limitation primarily resulted from:
- The size and complexity of the imported GLTF objects, which included high-polygon models and detailed materials.
- The computational demands of rendering shadows, dynamic lighting, and high-resolution textures simultaneously.
- Real-time raycasting for interactive elements, adding to the processing load.




## Credits

### Three.js Libraries:
Core: Three.js
Controls: OrbitControls
GLTF Loader: GLTFLoader

### Inspiration and Community Resources:
Github snippets for shader customization: https://gist.github.com/zouloux/dddd0c48f632077a20dc?utm_source=chatgpt.com

### Assets:

#### Models downloaded from:
All 3D models were downloaded for free from Fab: https://www.fab.com/, verified for personal and educational use.

#### Audio: 
"Pink Floyd: Any Colour You Like" (1973) sourced from Internet Archive: https://archive.org/details/07-us-and-them/08+Any+Colour+You+Like.mp3, ensuring compliance with its usage policies.

#### Posters:
Posters: Textures sourced from Pinterest.com, used for educational purposes in accordance with their guidelines.

**Developer:** Zeineb Souiai
