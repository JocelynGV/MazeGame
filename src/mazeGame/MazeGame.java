package mazeGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GraphicsConfiguration;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.media.j3d.ViewPlatform;
import javax.swing.JFrame;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.media.j3d.Texture2D;

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
        su = new SimpleUniverse(c3d);
//        su.getViewingPlatform().setNominalViewingTransform();
        
        // customize view
    	customizeView(new Point3d (0,0,-3.5), new Point3d (0,0,0), new Vector3d (0,1,0));
        viewTransformGroup = su.getViewingPlatform().getViewPlatformTransform();

        c3d.addKeyListener(this);
        this.addKeyListener(this);
    	
        BranchGroup bg = createSceneGraph();
        bg.compile();
        su.addBranchGraph(bg);
    }
    
	private BranchGroup createView(Point3d eye, Point3d center, Vector3d vup) {
		View view = new View();
//		view.setProjectionPolicy(View.PARALLEL_PROJECTION);
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
//	    view.setBackClipDistance(100.0); 
	}

    public BranchGroup createSceneGraph() {
        BranchGroup root = new BranchGroup();
        TransformGroup spin = new TransformGroup();
        spin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        root.addChild(spin);
        // create floor 
        Appearance floorAp = new Appearance();
        floorAp.setMaterial(new Material());
        PolygonAttributes pa = new PolygonAttributes();
        pa.setBackFaceNormalFlip(true);
        pa.setCullFace(PolygonAttributes.CULL_NONE);
        floorAp.setPolygonAttributes(pa);
        Shape3D shape = new Shape3D(createFloor(), floorAp);
        //transformation
        Transform3D tr = new Transform3D();
        tr.setScale(0.4);
        TransformGroup tg = new TransformGroup(tr);
        spin.addChild(tg);
        tg.addChild(shape);
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
        orbit.setRotYFactor(0.5);
        orbit.setRotXFactor(0.5);
        
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
			// TODO Auto-generated catch block
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
    	float w = 4f, z = -0.5f, h = 0.1f;
        Point3f[] verts = new Point3f[8];
        verts[0] = new Point3f(w,z,w);
        verts[1] = new Point3f(-w,z,w);
        verts[2] = new Point3f(-w,z,-w);
        verts[3] = new Point3f(w,z,-w);
        verts[4] = new Point3f(w,z+h,w);
        verts[5] = new Point3f(-w,z+h,w);
        verts[6] = new Point3f(-w,z+h,-w);
        verts[7] = new Point3f(w,z+h,-w);
        
        // indices
        int[] inds = {
        		0, 1, 2, 3,
        		0, 1, 5, 4,
        		1, 5, 6, 2,
        		2, 6, 7, 3,
        		3, 7, 4, 0,
        		4, 5, 6, 7
        };
        
        GeometryInfo gi = new GeometryInfo(GeometryInfo.QUAD_ARRAY);
        gi.setCoordinates(verts);
        gi.setCoordinateIndices(inds);
        NormalGenerator ng = new NormalGenerator();
        ng.generateNormals(gi);
        return gi.getGeometryArray();


    }

    @Override
    public void keyPressed(KeyEvent e) {
    	int key = e.getKeyCode();
    	
    	Transform3D currentTransform = new Transform3D();
        su.getViewingPlatform().getViewPlatformTransform().getTransform(currentTransform);

        // Extract the translation vector (position)
        Vector3d translation = new Vector3d();
        currentTransform.get(translation);

        // Convert to Point3d
        Point3d cameraPosition = new Point3d(translation.x, translation.y, translation.z);
        System.out.println("new Camera Position: " + cameraPosition);
    	
    	switch(key) {
    	case KeyEvent.VK_UP:
    		cameraPosition.z += 0.5;
    		customizeView(cameraPosition, new Point3d(0,0,1000), new Vector3d(0,1,0));
    		break;
    	case KeyEvent.VK_DOWN:
    		cameraPosition.z -= 0.5;
    		customizeView(cameraPosition, new Point3d(0,0,1000), new Vector3d(0,1,0));
    		break;
    	}
    	
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
}
