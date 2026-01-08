// setting up scene

const scene = new THREE.Scene();

// camera setup
const camera = new THREE.PerspectiveCamera(
    72, window.innerWidth / window.innerHeight, 0.5, 100
);
camera.position.set(0,5,15); // adjust to view scene



//renderer setup
const renderer = new THREE.WebGLRenderer({
    antialias: false, // smoother rendering
    logarithmicDepthBuffer: false // improved depth precision
});
renderer.setSize(window.innerWidth, window.innerHeight);
renderer.shadowMap.enabled = true;
document.body.appendChild(renderer.domElement);

let recordSpinning = false;


// lights
const directionalLight = new THREE.DirectionalLight(0xffffff, 0.1);
directionalLight.position.set(2, 8, 6); // adjust as needed
directionalLight.castShadow = true; 
scene.add(directionalLight);

const ambientLight = new THREE.AmbientLight(0xffffff, 0.6); // soft ambient light
scene.add(ambientLight);

// floor
const textureLoader = new THREE.TextureLoader();
const floorTexture = textureLoader.load('textures/wooden_floor3.jpg');
const floorMaterial = new THREE.MeshStandardMaterial({map: floorTexture});
const floorGeometry = new THREE.PlaneGeometry(20,16);
const floor = new THREE.Mesh(floorGeometry, floorMaterial)
floor.rotation.x = -Math.PI / 2;
floor.position.y = -0.2; // slightly below objects
floor.receiveShadow = true;
scene.add(floor);

// walls

const backWallTexture = textureLoader.load('textures/pattern5.jpg');
backWallTexture.wrapS = THREE.RepeatWrapping; // allows horizontal repetition
backWallTexture.wrapT = THREE.RepeatWrapping; // allows vertical repetition
backWallTexture.repeat.set(4, 2); 
const backWallMaterial = new THREE.MeshStandardMaterial({map: backWallTexture});
const backWall = new THREE.Mesh(
    new THREE.PlaneGeometry(20, 10),
    backWallMaterial
);
backWall.position.z = -8;
backWall.position.y = 4.75; // raising it above floor level
backWall.receiveShadow = true;
scene.add(backWall);


const leftWall = new THREE.Mesh(
    new THREE.PlaneGeometry(16, 10),
    backWallMaterial
);

leftWall.rotation.y = Math.PI/2 ;
leftWall.position.x = -10;
leftWall.position.y = 4.75; // raising it above floor level
leftWall.receiveShadow = true;
scene.add(leftWall);


const rightWall = new THREE.Mesh(
    new THREE.PlaneGeometry(16, 10),
    backWallMaterial
);

rightWall.rotation.y = -Math.PI/2 ;
rightWall.position.x = 10;
rightWall.position.y = 4.75; // raising it above floor level
rightWall.receiveShadow = true;
scene.add(rightWall);



// roof

const roofTexture = textureLoader.load('textures/burgundywp2.jpeg')
const roofMaterial = new THREE.MeshStandardMaterial({
     map: roofTexture,
    side: THREE.DoubleSide,
});
const roofGeometry = new THREE.PlaneGeometry(20,16);
const roof = new THREE.Mesh(roofGeometry, roofMaterial);

roof.rotation.x = Math.PI / 2;
roof.position.y = 9.8;
roof.receiveShadow = true;
scene.add(roof);

const gltfLoader = new THREE.GLTFLoader();


// rug

// loading rug texture

const patternTexture = textureLoader.load('textures/brug.jpg');
const rugFabricTexture = textureLoader.load('textures/carpet_texture.jpg');

patternTexture.wrapS = THREE.RepeatWrapping;
patternTexture.wrapT = THREE.RepeatWrapping;
patternTexture.repeat.set(1,1); // adjust to control pattern size



// rug material
const rugMaterial = new THREE.MeshStandardMaterial({
    map: patternTexture, // texture dominates
    alphaMap: rugFabricTexture, // pattern is blended in
    transparent: true, // ensuring alpha map is respected
    roughness: 0.5,
    metalness: 0.6,
});

// creating rug geometry
const rugGeometry = new THREE.PlaneGeometry(19,14); // adjust to fit room 

// creating the rug mesh
const rug = new THREE.Mesh(rugGeometry, rugMaterial);
rug.rotation.x = -Math.PI /2;
rug.position.z= -1;
rug.position.y = 0.01;
rug.receiveShadow = true;

scene.add(rug);

// floor lamp 1
let floorLamp;
let lampLight;

gltfLoader.load(
    'furniture/floor lamp/Wooden_Floor_Lamp_vi5hbjgga_Raw.gltf',
    (gltf) => {
        floorLamp = gltf.scene;

        // positioning at the back left corner
        floorLamp.scale.set(4, 4, 4);
        floorLamp.position.set(8, 0, 6);
        floorLamp.castShadow = true;

        scene.add(floorLamp);

        lampLight = new THREE.PointLight(0xff4d4d, 0.75,  10);
        lampLight.position.set(0, 1.5 ,0);
        lampLight.castShadow = true;
        lampLight.visible = false; // initially off
        floorLamp.add(lampLight);
    },
    (xhr) => {
        console.log((xhr.loaded / xhr.total) * 100 + '% loaded');
    },
    (error) => {
        console.error('Error loading floor lamp model:', error);
    }
);

// floor lamp 2
let floorLamp2;
let lampLight2;

gltfLoader.load(
    'furniture/floor lamp/Wooden_Floor_Lamp_vi5hbjgga_Raw.gltf',
    (gltf) => {
        floorLamp2 = gltf.scene;

        // positioning at the back left corner
        floorLamp2.scale.set(4, 4, 4);
        floorLamp2.position.set(8, 0, -6);
        floorLamp2.castShadow = true;

        scene.add(floorLamp2);

        lampLight2 = new THREE.PointLight(0xff4d4d, 0.75,  15);
        lampLight2.position.set(0, 1.5 ,0);
        lampLight2.castShadow = true;
        lampLight2.visible = false; // initially off
        floorLamp2.add(lampLight2);
    },
    (xhr) => {
        console.log((xhr.loaded / xhr.total) * 100 + '% loaded');
    },
    (error) => {
        console.error('Error loading floor lamp 2 model:', error);
    }
);



// sofa 

gltfLoader.load(
    'furniture/sofa_gltf/scene.gltf',
    (gltf) => {
        const sofa = gltf.scene;

        // positioning at the back 
        sofa.scale.set(10, 15, 16);
        sofa.position.set(8, 1, 0);
        sofa.castShadow = true;
        sofa.rotation.y = Math.PI/2;

        const fabricTexture2 = textureLoader.load('textures/sofa_pattern.png');
        fabricTexture2.wrapS = THREE.RepeatWrapping;
        fabricTexture2.wrapT = THREE.RepeatWrapping;
        fabricTexture2.repeat.set(2,4);

        // changing color to orange
        sofa.traverse((child => {
            if (child.isMesh && child.material){
                child.material.map =null;
                //child.material.color.set(0xff0000);
                child.material.roughness = 0.8;
                child.material.metalness = 0.5;
                child.material.map = fabricTexture2;
                child.material.needsUpdate = true;
            }
        }));

        scene.add(sofa);

    },
    (xhr) => {
        console.log((xhr.loaded / xhr.total) * 100 + '% loaded');
    },
    (error) => {
        console.error('Error loading sofa model:', error);
    }
);


// loading  chair
gltfLoader.load(
    'furniture/egg_chair/scene.gltf',
    (gltf) => {
        chair = gltf.scene;

        // positioning at the back 
        chair.scale.set(4, 4, 4);
        chair.position.set(-7, 0, -5);
        chair.castShadow = true;
        chair.rotation.y = Math.PI + 0.1;

        chair.traverse((child) => {
            if (child.isMesh && child.material) {
                child.material.map = null;
                child.material.color.set(0xd32f2f); // Red-orange hex color
                child.material.roughness = 0.9;    // Adds depth to the material
                child.material.metalness = 0.0;    // Slight metallic sheen
                child.material.needsUpdate = true; // Ensure material updates
            }
        });
        

        scene.add(chair);
        

    },
    (xhr) => {
        console.log('chair '+(xhr.loaded / xhr.total) * 100 + '% loaded');
    },
    (error) => {
        console.error('Error loading chair model:', error);
    }
);




let steroReady = false;

// loading record player and stereo
gltfLoader.load(
    'furniture/vintage_stereo_hi_fi_stack_w_speakers_gltf/scene.gltf',
    (gltf) => {
        stereo = gltf.scene;

        // positioning at the back 
        stereo.scale.set(0.007, 0.007, 0.007);
        stereo.position.set(-8, 0, 2.9);
        stereo.castShadow = true;
        stereo.rotation.y = Math.PI/2;

        

        scene.add(stereo);
        stereoReady = true;

    },
    (xhr) => {
        console.log('stereo '+(xhr.loaded / xhr.total) * 100 + '% loaded');
    },
    (error) => {
        console.error('Error loading stereo model:', error);
    }
);



// 2nd sofa 

gltfLoader.load(
    'furniture/vintage_sofa_23mb_gltf/scene.gltf',
    (gltf) => {
        const cornerSofa = gltf.scene;

        // positioning at the back 
        cornerSofa.scale.set(0.5, 0.5, 0.35);
        cornerSofa.position.set(1.5, 0 , -5);
        cornerSofa.castShadow = true;
        cornerSofa.rotation.y = 0 ;

        const fabricTexture = textureLoader.load('textures/carpet_texture.jpg');
        fabricTexture.wrapS = THREE.RepeatWrapping;
        fabricTexture.wrapT = THREE.RepeatWrapping;
        fabricTexture.repeat.set(1,1);

        // changing color to orange
        cornerSofa.traverse((child => {
            if (child.isMesh && child.material){
                //child.material.map =null;
                child.material.color.set(0xff0000);
                child.material.roughness = 0.8;
                child.material.metalness = 0.1;
                //child.material.aoMap = fabricTexture;
                child.material.needsUpdate = true;
            }
        }));

        scene.add(cornerSofa);

    },
    (xhr) => {
        console.log('couch ' + ( xhr.loaded / xhr.total) * 100 + '% loaded');
    },
    (error) => {
        console.error('Error loading corner sofa model:', error);
    }
);

// table 

gltfLoader.load(
    'furniture/table4/scene.gltf',
    (gltf) => {
        const table = gltf.scene;

        // positioning at the center
        table.scale.set(2.75, 2, 2.5);
        table.position.set(1, -0.5, 0);
        table.castShadow = true;
        table.rotation.x = 0 ;

        scene.add(table);

    },
    (xhr) => {
        console.log((xhr.loaded / xhr.total) * 100 + '% loaded table');
    },
    (error) => {
        console.error('Error loading table model:', error);
    }
);

// books 

gltfLoader.load(
    'furniture/books/scene.gltf',
    (gltf) => {
        const books = gltf.scene;

        // positioning at the center
        books.scale.set(0.6, 0.4, 0.6);
        books.position.set(1.4, 1.3, 0);
        books.castShadow = true;
        books.rotation.x = 0 ;

        scene.add(books);

    },
    (xhr) => {
        console.log((xhr.loaded / xhr.total) * 100 + '% loaded books');
    },
    (error) => {
        console.error('Error loading books model:', error);
    }
);

// tv table 

gltfLoader.load(
    'furniture/table3/scene.gltf',
    (gltf) => {
        const tvTable = gltf.scene;

        // positioning at the center
        tvTable.scale.set(0.015, 0.02, 0.0175);
        tvTable.position.set(0, 0, 6);
        tvTable.castShadow = true;
        tvTable.rotation.x = 0 ;

        scene.add(tvTable);

    },
    (xhr) => {
        console.log((xhr.loaded / xhr.total) * 100 + '% loaded tv table');
    },
    (error) => {
        console.error('Error loading tv table model:', error);
    }
);

// tv  

let tv; // declaring tv so it's gloabally accessible

gltfLoader.load(
    'furniture/1970s_vintage_television/scene.gltf',
    (gltf) => {
        tv = gltf.scene;

        // positioning at the center
        tv.scale.set(0.2, 0.2, 0.25);
        tv.position.set(0,3.75, 6);
        tv.castShadow = true;
        tv.rotation.x = Math.PI ;

        scene.add(tv);

    },
    (xhr) => {
        console.log((xhr.loaded / xhr.total) * 100 + '% loaded tv ');
    },
    (error) => {
        console.error('Error loading tv model:', error);
    }
);

 // tv screen


 const tvScreenGeometry = new THREE.PlaneGeometry(2, 1.5); // Width and height
 const tvScreenMaterial = new THREE.ShaderMaterial({
    uniforms: {
        time: { value: 0.0 },
        active: {value: false},
    },
    vertexShader: `
        varying vec2 vUv;
        void main() {
            vUv = uv;
            gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 1.0);
        }
    `,
    fragmentShader: `
        varying vec2 vUv;
        uniform float time;
        uniform bool active;

        float random(vec2 st) {
            return fract(sin(dot(st.xy, vec2(12.9898, 78.233))) * 43758.5453123);
        }

        void main() {
            if (active) {
                vec2 st = vUv * 10.0; // Increase noise scale
                float noise = random(st + time * 0.05); // time-based noise
                gl_FragColor = vec4(vec3(noise), 1.0); // displaying static noise
            } else {
                gl_FragColor = vec4(0.0, 0.0, 0.0, 1.0); // rendering black screen when off
            }
        }
    `,
});

const tvScreen = new THREE.Mesh(tvScreenGeometry, tvScreenMaterial);
tvScreen.position.set(-0.25, 2.7, 5.15); // adjust to fit TV
tvScreen.rotation.y = Math.PI; // adjust facing
scene.add(tvScreen);

let tvOn = false;
 


// loading audio file
const stereoAudio = new Audio('audio/08 Any Colour You Like.mp3');
stereoAudio.volume = 0.7;

// toggling play/pause
function toggleMusic(){
    if (stereoAudio.paused){
        stereoAudio.play();
    } else {
        stereoAudio.pause();
    }
}

const recordGeometry = new THREE.CylinderGeometry(0.9,0.9,0.05,32); // radius, radius, thickness, segments
const recordMaterial = new THREE.MeshStandardMaterial({ 
    color: 0x222222,
    roughness: 0.4,
    metalness: 0.7
});
const record = new THREE.Mesh(recordGeometry, recordMaterial);

// positioning the record on the stereo
record.position.set(-7.95, 3.9, 3.4); 
record.rotation.x = 0; // lays flat
record.castShadow = true;
scene.add(record);

// Sticker geometry
const stickerGeometry = new THREE.CylinderGeometry(0.3, 0.3, 0.01, 32); 
const stickerTexture = textureLoader.load('textures/tdsotm.jpg'); 
const stickerMaterial = new THREE.MeshStandardMaterial({
    map: stickerTexture,
    roughness: 0.5
});

// Sticker mesh
const sticker = new THREE.Mesh(stickerGeometry, stickerMaterial);
sticker.position.set(0, 0.022, 0); // slightly above the record to avoid z-fighting
record.add(sticker); // adding sticker to the record

function spinRecord(){
    if (recordSpinning) {
        record.rotation.y += 0.05;
    }
}




// loading the first poster texture
const poster1Texture = textureLoader.load('textures/pf1.jpg');
const poster1Material = new THREE.MeshStandardMaterial({ map: poster1Texture });
const poster1Geometry = new THREE.PlaneGeometry(3, 4); // Width, Height
const poster1 = new THREE.Mesh(poster1Geometry, poster1Material);

// positioning the first poster on the left wall
poster1.position.set(-9.9, 7.5,-4); 
poster1.rotation.y = Math.PI / 2; 
scene.add(poster1);

// loding the second poster texture
const poster2Texture = textureLoader.load('textures/ledzep2.jpg');
const poster2Material = new THREE.MeshStandardMaterial({ map: poster2Texture });
const poster2Geometry = new THREE.PlaneGeometry(3, 4); 
const poster2 = new THREE.Mesh(poster2Geometry, poster2Material);

// positioning the second poster 
poster2.position.set(-9.9, 3, -3); 
poster2.rotation.y = Math.PI / 2;
scene.add(poster2);

// loading the first poster for the back wall
const backPoster1Texture = textureLoader.load('textures/fzappaa.jpg');
const backPoster1Material = new THREE.MeshStandardMaterial({ map: backPoster1Texture });
const backPoster1Geometry = new THREE.PlaneGeometry(3, 4); // Width, Height
const backPoster1 = new THREE.Mesh(backPoster1Geometry, backPoster1Material);

// positioning the first poster on the back wall in the left corner
backPoster1.position.set(-8, 7.5, -7.99);
scene.add(backPoster1);

// loading the second poster for the back wall
const backPoster2Texture = textureLoader.load('textures/beat.jpg');
const backPoster2Material = new THREE.MeshStandardMaterial({ map: backPoster2Texture });
const backPoster2Geometry = new THREE.PlaneGeometry(3, 4); // Same size as the first poster
const backPoster2 = new THREE.Mesh(backPoster2Geometry, backPoster2Material);

// positioning the second poster on back wall
backPoster2.position.set(-6, 3, -7.99); 
scene.add(backPoster2);

const highlightMaterial = new THREE.MeshStandardMaterial({
    color: 0xffffff, // white color
    emissive: 0xffffff, // white emissive light
    emissiveIntensity: 0.01, // subtle brightness
    opacity: 0.01,
    roughness: 0.6, 
    metalness: 0.1, 
    
});

let highlightedObject = null;
let originalMaterial = null;




// controls

// adding orbit controls
const controls = new THREE.OrbitControls(camera, renderer.domElement);



// customizing controls
controls.enableDamping = true; // smooth damping effect
controls.dampingFactor = 0.15;
controls.enableZoom = true;
controls.minDistance = 0.01;
controls.maxDistance = 50;
controls.enablePan = true; 
controls.enableRotate = true;
controls.target.set(0, 5, 0); // focus on the center of the room 
controls.rotateSpeed = 0.09;
controls.zoomSpeed= 0.4;
controls.panSpeed = 0.3;




// initializing raycaster and mouse
const raycaster = new THREE.Raycaster();
const mouse = new THREE.Vector2();


// adding the interactions


//  event listener for mouse clicks
window.addEventListener('click', (event) => {
    // converting mouse position to normalized device coordinates (-1 to +1)
    mouse.x = (event.clientX / window.innerWidth) * 2 - 1;
    mouse.y = -(event.clientY / window.innerHeight) * 2 + 1;

    // updating the raycaster with the camera and mouse position
    raycaster.setFromCamera(mouse, camera);



    if (floorLamp) {
        const floorLampIntersects = raycaster.intersectObject(floorLamp, true);
    
        if (floorLampIntersects.length > 0) {
            // toggling the lamp light state
            if (lampLight) {
                lampLight.visible = !lampLight.visible;
                lampLight.intensity = lampLight.visible ? 0.75 : 0; 
            }
        
        }
    }

    if (floorLamp2) {
        const floorLamp2Intersects = raycaster.intersectObject(floorLamp2, true);
    
        if (floorLamp2Intersects.length > 0) {
            // toggling the lamp light state
            if (lampLight2) {
                lampLight2.visible = !lampLight2.visible;
                lampLight2.intensity = lampLight2.visible ? 0.75 : 0; 
            }
        
        }
    }

    // checking for intersection with stereo
    if (stereoReady){
        const stereoIntersects = raycaster.intersectObject(stereo, true);
        if (stereoIntersects.length > 0) {
            
            toggleMusic(); 
            recordSpinning = !recordSpinning;
        }
    }

    

    if (tv) {
        const screenIntersects = raycaster.intersectObject(tv, true);
        if (screenIntersects.length > 0) {
            tvOn = !tvOn;
            tvScreenMaterial.uniforms.active.value = tvOn ? 1.0 : 0.0; // toggling as float
            console.log("TV toggled:", tvOn);
        }
    }

    
    

});

function switchOnTV(){
    if (tvOn == true){
        tvScreenMaterial.uniforms.time.value += 0.05; // updating time for static animation
    }
}

window.addEventListener('mousemove', (event) => {
    // converting mouse position to normalized device coordinates
    mouse.x = (event.clientX / window.innerWidth) * 2 - 1;
    mouse.y = -(event.clientY / window.innerHeight) * 2 + 1;

    // updating raycaster
    raycaster.setFromCamera(mouse, camera);

   
    const intersects = raycaster.intersectObjects([floorLamp, floorLamp2, stereo, tv], true);

    if (intersects.length > 0) {
        const intersectedObject = intersects[0].object;

        // changing cursor to pointer
        document.body.style.cursor = 'pointer';

        // if a new object is hovered over
        if (highlightedObject !== intersectedObject) {
            // restoring the original material of the previously highlighted object
            if (highlightedObject) {
                highlightedObject.material = originalMaterial;
            }

            // storing the original material of the newly highlighted object
            highlightedObject = intersectedObject;
            originalMaterial = intersectedObject.material;

            // applying the highlight material
            intersectedObject.material = highlightMaterial;
        }
    } else if (highlightedObject) {
        // if no objects are hovered over, restoring the original material

        document.body.style.cursor = 'default';
        highlightedObject.material = originalMaterial;
        highlightedObject = null;
        originalMaterial = null;
    }
});





// rendering loop
function animate() {
    requestAnimationFrame(animate);

    spinRecord();

    switchOnTV();

    // updating controls
    controls.update();

    renderer.render(scene, camera);
}

animate();
