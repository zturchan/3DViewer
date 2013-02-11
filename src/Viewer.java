/*
 * Most of source code for .obj viewer from 
 * http://www.vrupl.evl.uic.edu/LabAccidents/java3d/lesson08/lesson08a.java
 * http://webdocs.cs.ualberta.ca/~lihang/Share/Java3D/Pyramid/ViewPyramid.java
 */

import java.applet.Applet;
import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Scrollbar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JFileChooser;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;

import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class Viewer extends Applet { 
	SimpleUniverse simpleU; 
	static boolean application = false;
	public static boolean debug = true;
	public static Frame frame;
	private static String filename = "newbunny.obj";
	// 3D Canvas
	Canvas3D           canvas;

	// UI Components
	Panel              controlPanel;
	Panel              canvasPanel;
	Scrollbar          disSlider;
	Label              disLabel;
	
	public static void setFile(String in){
		filename = in;
	}
	
	public Viewer (String name){
		setFile(name);
	}    

	public void init() { 
		this.setLayout(new FlowLayout());   

		// 1. Create the canvas and the UI
		canvasPanel = new Panel();
		controlPanel = new Panel();

		createCanvasPanel(canvasPanel); 
		this.add(canvasPanel);

		createControlPanel(controlPanel); 
		this.add(controlPanel);  

		//	Canvas3D c = new Canvas3D(SimpleUniverse.getPreferredConfiguration()); 
		//    add("Center", c);    
		BranchGroup scene = createSceneGraph(); 
		debug("CREATED SCENE GRAPH");

		simpleU= new SimpleUniverse(canvas); // setup the SimpleUniverse, attach the Canvas3D
		debug("SETUP UNIVERSE");
		simpleU.getViewingPlatform().setNominalViewingTransform();



		simpleU.getViewingPlatform().setNominalViewingTransform();
		debug("SET TRANSFORM");
		scene.compile(); 
		debug("COMPILED");
		simpleU.addBranchGraph(scene); //add your SceneGraph to the SimpleUniverse   
		debug("DONE");
	}

	public BranchGroup createSceneGraph() {      

		BranchGroup objRoot = new BranchGroup(); 
		debug("CREATED BRANCH GROUP");

		TransformGroup tg = new TransformGroup();
		Transform3D t3d = new Transform3D();
		MouseRotate    mr = new MouseRotate();
		mr.setTransformGroup(tg);
		BoundingSphere bounds = new BoundingSphere (new Point3d (0.0, 0.0, 0.0), 100.0);
        mr.setSchedulingBounds(bounds);
        objRoot.addChild(mr);
        tg.setCapability(tg.ALLOW_TRANSFORM_READ);
        tg.setCapability(tg.ALLOW_TRANSFORM_WRITE);
		try
		{	
			Scene s = null;
			ObjectFile f = new ObjectFile ();
			f.setFlags (ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY);
			debug("OBJECT FLAGS SET");

			if (application == false){
				java.net.URL objFile = new java.net.URL (getCodeBase(),filename);
				s = f.load (objFile);
				tg.addChild (s.getSceneGroup ());

			}
			else {
				s = f.load (filename);
				debug("LOADED FILE: " + filename);

				tg.addChild (s.getSceneGroup ());
				debug("ADDED CHILD");

			}

		}

		catch (java.net.MalformedURLException ex){	
		}
		catch (java.io.FileNotFoundException ex){
		}



		Color3f ambientColor = new Color3f (0.5f, 0.5f, 0.5f);

		AmbientLight ambientLightNode = new AmbientLight (ambientColor);

		ambientLightNode.setInfluencingBounds (bounds);

		objRoot.addChild (ambientLightNode);	


/*		t3d.setTranslation(new Vector3f(0f,0f,-5f));
		tg.setTransform(t3d);
		objRoot.addChild(tg);*/
		
        t3d.set(2/6.0);
        tg.setTransform(t3d);

        // 6. Add the transform group to the scene
        objRoot.addChild(tg);
        AWTInteractionBehavior awtBehavior = new AWTInteractionBehavior(tg);
        disSlider.addAdjustmentListener(awtBehavior);
        awtBehavior.setSchedulingBounds(bounds);
        objRoot.addChild(awtBehavior);
        
        // 8. Have Java 3D perform optimizations on this scene graph.
        objRoot.compile();

		return objRoot;
	}

	public void destroy() {	
		simpleU.removeAllLocales();    
	}  

	/*
	 * This creates the control panel
	 */
	private void createControlPanel(Panel p) {
		GridBagLayout      gl  = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();

		p.setLayout (gl);
		gbc.weightx = 100;  gbc.weighty = 100;
		gbc.fill = GridBagConstraints.BOTH;
		
		
		// The slider
		gbc.gridx = 0;  gbc.gridy = 0;
		gbc.gridwidth = 5;  gbc.gridheight = 1;
		disSlider = new Scrollbar(Scrollbar.HORIZONTAL, 2, 1,  0, 11);
		disSlider.setUnitIncrement(1);
		p.add(disSlider, gbc);

		// The label
		gbc.gridx = 0;  gbc.gridy = 1;
		gbc.gridwidth = 5;  gbc.gridheight = 1;
		disLabel = new Label("+  Distance  -", Label.CENTER);
		p.add(disLabel, gbc);
		
		//The button to open a file
		Button opener;
		OpenListener openListener = new OpenListener(this);
		gbc.gridx = 0;  gbc.gridy = 2;
		gbc.gridwidth = 5; gbc.gridheight = 1;
		opener = new Button("Open");
		opener.setActionCommand("Open");
		opener.addActionListener(openListener);
		p.add(opener, gbc);
	}

	/*
	 * This creates the Java3D canvas
	 */
	private void createCanvasPanel(Panel p) {
		GridBagLayout      gl  = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();

		p.setLayout(gl);
		gbc.gridx = 0;  gbc.gridy = 0;
		gbc.gridwidth = 5;  gbc.gridheight = 5;
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();

		canvas = new Canvas3D(config);
		canvas.setSize(230,210);
		p.add(canvas,gbc);

	}


	public static void main(String[] args) {
		application = true;    
		setFrame(new MainFrame(new Viewer("newbunny.obj"), 500, 220));    
	}

	public static void debug(String msg){
		if (debug){
			System.out.println(msg);
		}
	}
	
	public static void setFrame(MainFrame f){
		frame = f;
	}
}
