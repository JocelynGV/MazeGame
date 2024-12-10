package mazeGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GraphicsConfiguration;

import java.awt.TextArea;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.media.j3d.Alpha;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Geometry;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Material;
import javax.media.j3d.PointLight;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.swing.JFrame;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.TexCoord2f;
import javax.vecmath.Vector3d;


import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;

public class MazeGame extends JFrame implements KeyListener {
	Canvas3D c3d;
	SimpleUniverse su;
	private TransformGroup viewTransformGroup;
	OrbitBehavior orbit;
	

	float w = 20f, y = -0.75f, h = 0.1f;
	
	 public static final int[][] mapLayout = {
	            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
	            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
	            {1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1},
	            {1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1},
	            {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1},
	            {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 1},
	            {1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1},
	            {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1},
	            {1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1},
	            {1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
	            {1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1},
	            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
	            {1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1},
	            {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
	            {1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
	            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
	            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
	    };
	
	int currCol = 1, currRow = 1;

	
    public static void main(String[] args) {
        System.setProperty("sun.awt.noerasebackground", "true");
        JFrame frame = new MazeGame();
        frame.setBounds(100, 100, 1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public MazeGame() {
        Container cp = this.getContentPane();
        cp.setLayout(new BorderLayout());
        GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
        c3d = new Canvas3D(gc);
        cp.add(c3d, BorderLayout.CENTER);

        // add game Instructions
        TextArea ta = new TextArea("",3,30,TextArea.SCROLLBARS_NONE);
        ta.setText("UP and DOWN arrows to move forward and backward\n");
        ta.append("LEFT and RIGHT arrows to move left and right\n");
        ta.append("Drag mouse to look around");
        ta.setEditable(false);
        add(ta, BorderLayout.SOUTH);

        su = new SimpleUniverse(c3d);
//        su.getViewingPlatform().setNominalViewingTransform();
        
        // customize view
    	customizeView(new Point3d (17.0,0,-17.0), new Point3d (7,0,1000), new Vector3d (0,1,0));

        viewTransformGroup = su.getViewingPlatform().getViewPlatformTransform();

        c3d.addKeyListener(this);
        this.addKeyListener(this);
    	
        BranchGroup bg = createSceneGraph();
        bg.compile();
        su.addBranchGraph(bg);
	}
	
	private void customizeView(Point3d eye, Point3d center, Vector3d vup) {
	    Transform3D trans = new Transform3D();
	    trans.lookAt(eye, center, vup);
	    trans.invert();

	    su.getViewingPlatform().getViewPlatformTransform().setTransform(trans);

//	    View view = su.getViewer().getView();
//	    view.setBackClipDistance(100.0); 
	}

    public BranchGroup createSceneGraph() {
        BranchGroup root = new BranchGroup();
        TransformGroup spin = new TransformGroup();
        spin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        root.addChild(spin);
        // create floor 

        Appearance floorAp = createFloorTextureAppearance();
        floorAp.setMaterial(new Material());
        PolygonAttributes pa = new PolygonAttributes();
        pa.setBackFaceNormalFlip(true);
        pa.setCullFace(PolygonAttributes.CULL_NONE);
        floorAp.setPolygonAttributes(pa);
        Shape3D shape = new Shape3D(createFloor(), floorAp);
        //transformation
        Transform3D tr = new Transform3D();

//        tr.setScale(0.4);
        TransformGroup tg = new TransformGroup(tr);
        spin.addChild(tg);
        tg.addChild(shape);
        
        // Create the maze
        Appearance mazeAp = createWallAppearance(); 
        Shape3D maze = new Shape3D(createMaze(), mazeAp);
        Transform3D mazeTransform = new Transform3D();
//        mazeTransform.setScale(0.4); // Ensure the maze scales along with the floor
        TransformGroup mazeGroup = new TransformGroup(mazeTransform);
        mazeGroup.addChild(maze);
        spin.addChild(mazeGroup);
        
        Alpha alpha = new Alpha(-1, 10000);
//        RotationInterpolator rotator = new RotationInterpolator(alpha, spin);
        BoundingSphere bounds = new BoundingSphere();
        bounds.setRadius(100);
//        rotator.setSchedulingBounds(bounds);
//        spin.addChild(rotator);
        
        // allow user to look around
        orbit = new OrbitBehavior(c3d);
        System.out.println(orbit.getTranslateEnable());
        orbit.setTranslateEnable(false);
        System.out.println(orbit.getTranslateEnable());
        orbit.setZoomEnable(false);
        orbit.setRotYFactor(0);
        orbit.setRotXFactor(.5);

        
        Transform3D currentTransform = new Transform3D();
        su.getViewingPlatform().getViewPlatformTransform().getTransform(currentTransform);

        // Extract the translation vector (position)
        Vector3d translation = new Vector3d();
        currentTransform.get(translation);

        // Convert to Point3d
        Point3d cameraPosition = new Point3d(translation.x, translation.y, translation.z);
        System.out.println("Camera Position " + cameraPosition);

        orbit.setRotationCenter(cameraPosition);
        
        orbit.setSchedulingBounds(new BoundingSphere());
        su.getViewingPlatform().setViewPlatformBehavior(orbit);

        //light
        PointLight light = new PointLight(new Color3f(Color.white),
                new Point3f(0.5f,0.5f,1f),
                new Point3f(1f,0.2f,0f));
        light.setInfluencingBounds(bounds);
        root.addChild(light);
        //background
        Background background = new Background();
        background.setApplicationBounds(bounds);
        background.setGeometry(bgBranch());
        root.addChild(background);
        return root;
    }
    
    private BranchGroup bgBranch() {
    	BranchGroup background = new BranchGroup();
    	Appearance sphereAp = new Appearance();
        BufferedImage bgImg = null;
		try {
			bgImg = ImageIO.read(new File("src/FlowersMeadow.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		TextureLoader txld = new TextureLoader(bgImg);
		ImageComponent2D img2D = txld.getImage();
		Texture2D tex = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, 
				img2D.getWidth(), img2D.getHeight());
		tex.setImage(0, img2D);
		tex.setEnable(true);
		tex.setMagFilter(Texture.BASE_LEVEL_LINEAR);
		tex.setMinFilter(Texture.BASE_LEVEL_LINEAR);
		sphereAp.setTexture(tex);
    	Sphere skybox = new Sphere(1.0f, 
    			Sphere.GENERATE_NORMALS_INWARD | 
    			Sphere.GENERATE_TEXTURE_COORDS, 
    			sphereAp);
    	background.addChild(skybox);
    	return background;
    }
  
    private Geometry createFloor() {
        Point3f[] verts = new Point3f[8];
        verts[0] = new Point3f(w,y,w);
        verts[1] = new Point3f(-w,y,w);
        verts[2] = new Point3f(-w,y,-w);
        verts[3] = new Point3f(w,y,-w);
        verts[4] = new Point3f(w,y+h,w);
        verts[5] = new Point3f(-w,y+h,w);
        verts[6] = new Point3f(-w,y+h,-w);
        verts[7] = new Point3f(w,y+h,-w);
        
        // indices
        int[] inds = {
        		0, 1, 2, 3, //bottom
        		0, 1, 5, 4, // front face
        		1, 5, 6, 2, // left
        		2, 6, 7, 3, // back side
        		3, 7, 4, 0, // right 
        		4, 5, 6, 7 // top
        };
        
        // create texture mapping
        TexCoord2f[] texCoords = new TexCoord2f[8];
        // regular mapping
        texCoords[0] = new TexCoord2f(0.0f, 0.0f); 
        texCoords[1] = new TexCoord2f(1.0f, 0.0f);
        texCoords[2] = new TexCoord2f(1.0f, 1.0f);
        texCoords[3] = new TexCoord2f(0.0f, 1.0f);
        // skinny mapping (doesnt really work currently i think)
        texCoords[4] = new TexCoord2f(0.0f, 0.0f); 
        texCoords[5] = new TexCoord2f(1.0f, 0.0f); 
        texCoords[6] = new TexCoord2f(1.0f, 0.025f); 
        texCoords[7] = new TexCoord2f(0.0f, 0.025f);   
        
        // map texture to correct side of shape (also not sure why its not working)
        int[] texInds = {
        		0, 1, 2, 3,
        		4, 5, 6, 7,
        	    4, 5, 6, 7,
        	    4, 5, 6, 7,
        	    4, 5, 6, 7,
        	    0, 1, 2, 3
        };
        
        GeometryInfo gi = new GeometryInfo(GeometryInfo.QUAD_ARRAY);
        gi.setCoordinates(verts);
        gi.setCoordinateIndices(inds);
        gi.setTextureCoordinateParams(1, 2);
        gi.setTextureCoordinates(0, texCoords); 
        gi.setTextureCoordinateIndices(0, texInds);
        NormalGenerator ng = new NormalGenerator();
        ng.generateNormals(gi);
        return gi.getGeometryArray();


    }
    
    Appearance createFloorTextureAppearance(){    
        Appearance ap = new Appearance();
        URL filename = 
            getClass().getResource("cobblestone1.jpeg");
        if (filename == null) { 
        	System.out.println("Texture file not found!"); return ap; // Return an empty appearance to avoid further errors
        }
        TextureLoader loader = new TextureLoader(filename, this);
        ImageComponent2D image = loader.getImage();
        if(image == null) {
          System.out.println("can't find texture file.");
        }
        System.out.println("Texture file found at: " + filename);
        Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA,
        image.getWidth(), image.getHeight());
        texture.setImage(0, image);
        texture.setEnable(true);
        texture.setMagFilter(Texture.BASE_LEVEL_LINEAR);
        texture.setMinFilter(Texture.BASE_LEVEL_LINEAR);
        ap.setTexture(texture);
        return ap;
    }
    
    public Geometry createMaze() {
    	float baseY = y+h;
    	float cellSize = w*2/mapLayout[0].length;
    	float mazeX = -w;
    	float mazeZ = -w;
    	float mazeH = 1.5f;
    	
    	int wallCount = 0;
    	
    	for (int i = 0; i < mapLayout.length; i++) {
    		for (int j = 0; j < mapLayout[0].length; j++) {
    			if (mapLayout[i][j] == 1) {
    				wallCount++;
    			}
    		}
    	}
    	
        Point3f[] verts = new Point3f[wallCount * 8];
        int count = 0;
        for (int row = 0; row < mapLayout.length; row++) {
    		for (int col = 0; col < mapLayout[row].length; col++) {
    			if (mapLayout[row][col] == 1) {
//    				verts[count++] = new Point3f(mazeX, baseY, mazeZ); 
//    		        verts[count++] = new Point3f(mazeX + mazeW, baseY, mazeZ);
//    		        verts[count++] = new Point3f(mazeX + mazeW, baseY, mazeZ + mazeW);
//    		        verts[count++] = new Point3f(mazeX, baseY, mazeZ + mazeW);
//    		        verts[count++] = new Point3f(mazeX, baseY + mazeH, mazeZ); 
//    		        verts[count++] = new Point3f(mazeX + mazeW, baseY + mazeH, mazeZ);
//    		        verts[count++] = new Point3f(mazeX + mazeW, baseY + mazeH, mazeZ + mazeW);
//    		        verts[count++] = new Point3f(mazeX, baseY + mazeH, mazeZ + mazeW);
    		        
    				verts[count++] = new Point3f(mazeX + cellSize, baseY, mazeZ); //0
    		        verts[count++] = new Point3f(mazeX, baseY, mazeZ);  //1
    		        verts[count++] = new Point3f(mazeX, baseY, mazeZ + cellSize); //2
    		        verts[count++] = new Point3f(mazeX + cellSize, baseY, mazeZ + cellSize); //3
    		        verts[count++] = new Point3f(mazeX + cellSize, baseY + mazeH, mazeZ); //4
    		        verts[count++] = new Point3f(mazeX, baseY + mazeH, mazeZ); //5
    		        verts[count++] = new Point3f(mazeX, baseY + mazeH, mazeZ + cellSize); //6
    		        verts[count++] = new Point3f(mazeX + cellSize, baseY + mazeH, mazeZ + cellSize); //7
    			}
    			mazeX += cellSize;
    		}
    		mazeX = -w;
    		mazeZ += cellSize;
    	}
        
        System.out.println(Arrays.toString(verts));
        
        // indices
//        int[] inds = {
//        		0, 1, 2, 3, //bottom
//        		0, 1, 5, 4, // front face
//        		1, 5, 6, 2, // left
//        		2, 6, 7, 3, // back side
//        		3, 7, 4, 0, // right 
//        		4, 5, 6, 7 // top
//        };
        
        ArrayList<Integer> indsList = new ArrayList<>();
        count = 0;
        for (int i = 0; i < wallCount; i++) {
    		indsList.addAll(Arrays.asList(count, count + 1, count + 2, count + 3)); 
    		indsList.addAll(Arrays.asList(count, count + 1, count + 5, count + 4));
    		indsList.addAll(Arrays.asList(count + 1, count + 5, count + 6, count + 2));
    		indsList.addAll(Arrays.asList(count + 2, count + 6, count + 7, count + 3));
    		indsList.addAll(Arrays.asList(count + 3, count + 7, count + 4, count));
    		indsList.addAll(Arrays.asList(count + 4, count + 5, count + 6, count + 7));
    		
    		count += 8;
    	}
        System.out.println(indsList.size());
                
        int[] inds = new int[indsList.size()];
	    int i = 0;
	    for (Integer num : indsList) {
	    	inds[i++] = num;
	    }
	    
	    System.out.println(Arrays.toString(inds));
        
        GeometryInfo gi = new GeometryInfo(GeometryInfo.QUAD_ARRAY);
        gi.setCoordinates(verts);
        gi.setCoordinateIndices(inds);
//        gi.setTextureCoordinateParams(1, 2);
//        gi.setTextureCoordinates(0, texCoords); 
//        gi.setTextureCoordinateIndices(0, texInds);
        NormalGenerator ng = new NormalGenerator();
        ng.generateNormals(gi);
        return gi.getGeometryArray();
    }
    
    private Appearance createWallAppearance() {
        Appearance appearance = new Appearance();

        //tried to put an image on the walls, but it was too zoomed in
//        BufferedImage bgImg = null;
//		try {
//			bgImg = ImageIO.read(new File("src/NevadaMountains.jpg"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		TextureLoader txld = new TextureLoader(bgImg);
//		ImageComponent2D img2D = txld.getImage();
//		Texture2D tex = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, 
//				img2D.getWidth(), img2D.getHeight());
//		tex.setImage(0, img2D);
//		tex.setEnable(true);
//		tex.setMagFilter(Texture.BASE_LEVEL_LINEAR);
//		tex.setMinFilter(Texture.BASE_LEVEL_LINEAR);
//        appearance.setTexture(tex);
        appearance.setMaterial(new Material());

        // Configure polygon attributes to render both sides
        PolygonAttributes polyAttr = new PolygonAttributes();
        polyAttr.setCullFace(PolygonAttributes.CULL_NONE); // Render both sides
        appearance.setPolygonAttributes(polyAttr);
        
        return appearance;

    }

    @Override
    public void keyPressed(KeyEvent e) {
    	int key = e.getKeyCode();
    	
    	Transform3D currentTransform = new Transform3D();
        su.getViewingPlatform().getViewPlatformTransform().getTransform(currentTransform);

        // Extract the translation vector (position)
        Vector3d translation = new Vector3d();

        Vector3d rotation = new Vector3d();
        
        currentTransform.get(translation);
        currentTransform.get(rotation);
        
        Vector3d forward = new Vector3d(0.0, 0.0, -1.0); // Initially pointing along the negative z-axis
        currentTransform.transform(forward);

        // Convert to Point3d
        Point3d cameraPosition = new Point3d(translation.x, translation.y, translation.z);
        Point3d cameraOrientation = new Point3d(forward.x, forward.y, forward.z);
    	
    	switch(key) {
    	case KeyEvent.VK_UP:
    		// change z position (forward)
    		cameraPosition.z += 0.5;
    		break;
    	case KeyEvent.VK_DOWN:
    		cameraPosition.z -= 0.5;
    		break;
    	case KeyEvent.VK_RIGHT:
    		cameraPosition.x -= 0.5;
    		break;
    	case KeyEvent.VK_LEFT:
    		cameraPosition.x += 0.5;
    		break;
    	}

    	// I added cameraPosition to the "look" so we are looking in the correct x direction when we move left or right
//    	customizeView(cameraPosition, new Point3d(cameraPosition.x,0,1000), new Vector3d(0,1,0));
    	
    	if (isWalkable(cameraPosition.x, cameraPosition.z)) {
    		customizeView(cameraPosition, new Point3d(cameraPosition.x,0,1000), new Vector3d(0,1,0));
    	}

    	
//    	System.out.println("new Camera Position: " + cameraPosition);
//    	System.out.println("new Camera orientation: " + cameraOrientation);
        orbit.setRotationCenter(cameraPosition);
    }

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	// collision detection
	private boolean isWalkable(double x, double z) {
	    // calculate  grid size
	    double cellSize = (w * 2) / mapLayout[0].length;

	    // Translate world coordinates to grid indices
	    int col = (int) Math.floor((x + w) / cellSize);
	    int row = (int) Math.floor((z + w) / cellSize);
	    
	    System.out.println("x " + x);
	    System.out.println("z " + z);
	    System.out.println("row " + row);
	    System.out.println("col " + col);

	    // Check bounds
	    if (row < 0 || row >= mapLayout.length || col < 0 || col >= mapLayout[0].length) {
	        return false;
	    }

	    // Return if the cell is walkable
	    return mapLayout[row][col] == 0;
	}
}
