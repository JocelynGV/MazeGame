package mazeGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.media.j3d.PhysicalBody;
import javax.media.j3d.PhysicalEnvironment;
import javax.media.j3d.PointLight;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.media.j3d.ViewPlatform;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.TexCoord2f;
import javax.vecmath.Vector3d;

import javax.sound.sampled.*;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;

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
	            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9},
	            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
	    };

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
        
        // create jPanel to hold the button and text area
        JPanel bottomPanel = new JPanel();
        
        // create restart button
        JButton restartBtn = new JButton("Restart");
        restartBtn.setPreferredSize(new Dimension(100, 30)); // Set button size
        restartBtn.setFocusPainted(false);
        restartBtn.setFont(new Font("Arial", Font.BOLD, 12));
        restartBtn.addActionListener(new ActionListener(){  
        	public void actionPerformed(ActionEvent e){  
            	customizeView(new Point3d (17.0,0,-17.0), new Point3d (7,0,1000), new Vector3d (0,1,0));
            	//recenter orbit
                orbit.setRotationCenter(new Point3d (17.0,0,-17.0));

            }  
        });  
        
        bottomPanel.add(restartBtn, BorderLayout.EAST);

        // add game Instructions
        TextArea ta = new TextArea("",3,30,TextArea.SCROLLBARS_NONE);
        ta.setText("UP and DOWN arrows to move forward and backward\n");
        ta.append("LEFT and RIGHT arrows to move left and right\n");
        ta.append("Drag mouse to look around");
//        ta.setBounds(200, 300, 100, 100);
        ta.setEditable(false);
        bottomPanel.add(ta, BorderLayout.CENTER);
        
        //add jpanel to JFrame
        add(bottomPanel, BorderLayout.SOUTH);

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
    
    private BranchGroup createView(Point3d eye, Point3d center, Vector3d vup) {
        View view = new View();
//      view.setProjectionPolicy(View.PARALLEL_PROJECTION);
        ViewPlatform vp = new ViewPlatform();
        view.addCanvas3D(c3d);
        view.attachViewPlatform(vp);
        view.setPhysicalBody (new PhysicalBody());
        view.setPhysicalEnvironment(new PhysicalEnvironment ()); 
        Transform3D trans = new Transform3D(); 
        trans.lookAt(eye, center, vup);
        trans.invert(); 
        TransformGroup tg = new TransformGroup(trans);
        tg.addChild (vp);
        BranchGroup bgView = new BranchGroup();
        bgView.addChild(tg); 
        
        return bgView;
    }
    
    private void customizeView(Point3d eye, Point3d center, Vector3d vup) {
        Transform3D trans = new Transform3D();
        trans.lookAt(eye, center, vup);
        trans.invert();

        su.getViewingPlatform().getViewPlatformTransform().setTransform(trans);

        View view = su.getViewer().getView();
//      view.setBackClipDistance(100.0); 
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
        orbit.setRotXFactor(0.35);
        
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
    	
        URL filename = getClass().getResource("sky.jpg");
        TextureLoader txld = new TextureLoader(filename, this);
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
        
        // create texture mapping on image
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
            getClass().getResource("carpet3.jpg");
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
//                  verts[count++] = new Point3f(mazeX, baseY, mazeZ); 
//                  verts[count++] = new Point3f(mazeX + mazeW, baseY, mazeZ);
//                  verts[count++] = new Point3f(mazeX + mazeW, baseY, mazeZ + mazeW);
//                  verts[count++] = new Point3f(mazeX, baseY, mazeZ + mazeW);
//                  verts[count++] = new Point3f(mazeX, baseY + mazeH, mazeZ); 
//                  verts[count++] = new Point3f(mazeX + mazeW, baseY + mazeH, mazeZ);
//                  verts[count++] = new Point3f(mazeX + mazeW, baseY + mazeH, mazeZ + mazeW);
//                  verts[count++] = new Point3f(mazeX, baseY + mazeH, mazeZ + mazeW);
                    
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
        
        ArrayList<Integer> indsList = new ArrayList<>();
        count = 0;
        for (int i = 0; i < wallCount; i++) {
            indsList.addAll(Arrays.asList(count, count + 1, count + 2, count + 3)); //bottom
            indsList.addAll(Arrays.asList(count, count + 1, count + 5, count + 4)); // front face
            indsList.addAll(Arrays.asList(count + 1, count + 5, count + 6, count + 2)); // left face
            indsList.addAll(Arrays.asList(count + 2, count + 6, count + 7, count + 3)); // back face
            indsList.addAll(Arrays.asList(count + 3, count + 7, count + 4, count)); // right faec
            indsList.addAll(Arrays.asList(count + 4, count + 5, count + 6, count + 7)); // top
             
            count += 8;
        }
        System.out.println(indsList.size());
                
        int[] inds = new int[indsList.size()];
        int index = 0;
        for (Integer num : indsList) {
            inds[index++] = num;
        }
        
        System.out.println(Arrays.toString(inds));
        
        // create texture mapping on image
        ArrayList<TexCoord2f> texCoordsList = new ArrayList<>();
        for (int i = 0; i < wallCount; i++) {
//            for (int j = 0; j < 6; j++) { 
                texCoordsList.add(new TexCoord2f(0.0f, 0.0f));
                texCoordsList.add(new TexCoord2f(1.0f, 0.0f));
                texCoordsList.add(new TexCoord2f(1.0f, 1.0f));
                texCoordsList.add(new TexCoord2f(0.0f, 1.0f));
                texCoordsList.add(new TexCoord2f(0.0f, 0.0f));
                texCoordsList.add(new TexCoord2f(1.0f, 0.0f));
                texCoordsList.add(new TexCoord2f(1.0f, 1.0f));
                texCoordsList.add(new TexCoord2f(0.0f, 1.0f));
//            }
        }

//        TexCoord2f[] texCoords = texCoordsList.toArray(new TexCoord2f[0]);
        TexCoord2f[] texCoords = new TexCoord2f[texCoordsList.size()];
        
//        int[] inds = new int[indsList.size()];
        index = 0;
        for (TexCoord2f coord : texCoordsList) {
        	texCoords[index++] = coord;
        }
        
        System.out.println("texture coordinates: " + Arrays.toString(texCoords));

        int[] texIndsPerStructure = {
        		1,0,3,2, // maps to bottom of the rectangular prism
        		1,0,3,2, // maps to front face
        		1,2,3,0, // maps to left
        		1,2,3,0, // back face
        		1,2,3,0, // right
        		1,0,3,2 // top
        };
        
        // Create texture indices
        int[] texInds = new int[wallCount * 24];
        index = 0;
        for (int i = 0; i < wallCount; i++) {
            for (int j = 0; j < texIndsPerStructure.length; j++) {
            	texInds[index++] = texIndsPerStructure[j];
            }

        }
        
        System.out.println("texInds count: " + texInds.length);
        System.out.println("texInds: " + Arrays.toString(texInds));
        
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
    
    private Appearance createWallAppearance() {
        Appearance ap = new Appearance();
        URL filename = 
            getClass().getResource("wallpaper.jpeg");
        if (filename == null) { 
            System.out.println("Texture file not found!"); 
            return ap; // Return an empty appearance to avoid further errors
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

        // Configure polygon attributes to render both sides
        PolygonAttributes polyAttr = new PolygonAttributes();
        polyAttr.setCullFace(PolygonAttributes.CULL_NONE); // Render both sides
        ap.setPolygonAttributes(polyAttr);

        return ap;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
//        Transform3D currentTransform = new Transform3D();
//        su.getViewingPlatform().getViewPlatformTransform().getTransform(currentTransform);

        TransformGroup viewTG = orbit.getViewingPlatform().getMultiTransformGroup().getTransformGroup(0);
        Transform3D transform = new Transform3D();
        viewTG.getTransform(transform);
        //get position
        Vector3d translation = new Vector3d();
        transform.get(translation);
        
        // get rotation matrix
        Matrix3d rotationMatrix = new Matrix3d();
        transform.get(rotationMatrix);
        System.out.println("Rotation matrix: " + rotationMatrix);
        
        // perform the matrix transform to get the 'looking' vector
        Vector3d newForward = new Vector3d(0.0, 0.0, -1.0); // Default forward
        rotationMatrix.transform(newForward);
        System.out.println("newForward: " + newForward);
        
        // Convert to Point3d
//        Point3d cameraPosition = new Point3d(translation.x, translation.y, translation.z);
        
        double tan = newForward.x / newForward.z;
    	double relativeTheta = Math.atan(tan);
    	double rotTheta = 0.0;
    	System.out.println("tan " + tan);
    	System.out.println("theta " + relativeTheta);
    	
    	double rot = Math.PI / 45;
    	
    	boolean step = false;
        
        switch(key) {
        case KeyEvent.VK_UP:
//            cameraPosition.z += 0.5;
         // Move forward by scaling the forward vector
        	translation.x += newForward.x * .5;
        	translation.y += newForward.y * .5;
        	translation.z += newForward.z * .5;
        	step = true;
            break;
        case KeyEvent.VK_DOWN:
//            cameraPosition.z -= 0.5;
            translation.x += -newForward.x * .5;
        	translation.y += -newForward.y * .5;
        	translation.z += -newForward.z * .5;
        	step = true;
            break;
        case KeyEvent.VK_RIGHT:
//            cameraPosition.x -= 0.5;
        	
        	// turn relative angle to absolute angle
        	rotTheta = getTheta(relativeTheta, newForward);
        	// add rotation
        	rotTheta -= rot;
            transform.setRotation(new AxisAngle4d(0, 1, 0, rotTheta));
            break;
        case KeyEvent.VK_LEFT:
//            cameraPosition.x += 0.5;
        	
        	rotTheta = getTheta(relativeTheta, newForward);
        	// add rotation
        	rotTheta += rot;
            transform.setRotation(new AxisAngle4d(0, 1, 0, rotTheta));

            break;
        }

        // I added cameraPosition to the "look" so we are looking in the correct x direction when we move left or right
//      customizeView(cameraPosition, new Point3d(cameraPosition.x,0,1000), new Vector3d(0,1,0));
        
        //Walking audio
        
        URL filename = getClass().getResource("footstep.wav");
        Clip clip = null;
		try {
			clip = AudioSystem.getClip();
			AudioInputStream ais = AudioSystem.getAudioInputStream(
					filename);
			clip.open(ais);
		} catch (LineUnavailableException ex) {
			ex.printStackTrace();
		} catch (UnsupportedAudioFileException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
        
        
        if (isWalkable(translation.x, translation.z)) {
          transform.setTranslation(translation);
          orbit.getViewingPlatform().getMultiTransformGroup().getTransformGroup(0).setTransform(transform);
          if(step) {
          	clip.start();
          	step = false;
          }
        }

        
        // center the orbit at the new translated location
        orbit.setRotationCenter(new Point3d(translation.x, translation.y, translation.z));
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }
    
    private double getTheta(double relativeTheta, Vector3d forward) {
    	double newTheta = 0.0;
    	double rot = Math.PI/24;
    	
    	// quadrant "4" (assuming the z direction is at the top) 
    	if (forward.x <= 0 && forward.z < 0) {
    		newTheta = relativeTheta;
    	} else if (forward.x <= 0 && forward.z > 0) { // quadrant "1" (honestly just look at my sketch to make sense of this)
    		newTheta = Math.PI - Math.abs(relativeTheta);
    	} else if (forward.x >= 0 && forward.z > 0) {
    		newTheta = Math.PI + relativeTheta;
    	} else {
    		newTheta = (2 * Math.PI) - Math.abs(relativeTheta);
    	}
    	
    	return newTheta;
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

        // Check bounds and victory
        if (mapLayout[row][col] == 9) {
        	int choice = JOptionPane.showOptionDialog(
        	        this,
        	        "Congratulations! You Win!\nWould you like to play again?",
        	        "Victory",
        	        JOptionPane.YES_NO_OPTION,
        	        JOptionPane.INFORMATION_MESSAGE,
        	        null,
        	        new String[] {"Restart", "Exit"},
        	        "Restart"
        	    );

        	    if (choice == JOptionPane.YES_OPTION) {
        	        customizeView(new Point3d(17.0, 0, -17.0), new Point3d(7, 0, 1000), new Vector3d(0, 1, 0));
                    orbit.setRotationCenter(new Point3d (17.0,0,-17.0));
        	    } else if (choice == JOptionPane.NO_OPTION) {
        	        System.exit(0); // Exit the application
        	    }
        } else if (row < 0 || row >= mapLayout.length || col < 0 || col >= mapLayout[0].length) {
            return false;
        }

	    // Return if the cell is walkable
	    return mapLayout[row][col] == 0;
	}

}

