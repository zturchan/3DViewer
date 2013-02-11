import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;

import com.sun.j3d.utils.applet.MainFrame;

class OpenListener implements ActionListener { 
	  Viewer viewer;
	  OpenListener(Viewer v){
		  viewer = v;
	  }
	  public void actionPerformed(ActionEvent ae) {
	  String s = ae.getActionCommand(); 
	  if (s.equals("Exit")) { 
	  System.exit(0); 
	  } 
	  else if (s.equals("Open")) { 
		  //Filechooser code from http://docs.oracle.com/javase/1.4.2/docs/api/javax/swing/JFileChooser.html

		  JFileChooser chooser = new JFileChooser();
		  // Note: source for ExampleFileFilter can be found in FileChooserDemo,
		  // under the demo/jfc directory in the Java 2 SDK, Standard Edition.
		  /*FileFilter filter = new FileFilter();
		  filter.addExtension("jpg");
		  filter.addExtension("gif");
		  filter.setDescription("JPG & GIF Images");
		  chooser.setFileFilter(filter);*/
		  chooser.addChoosableFileFilter(new OBJfilter());
		  int returnVal = chooser.showOpenDialog(null);
		  if(returnVal == JFileChooser.APPROVE_OPTION) {
			  //System.out.println("You chose to open this file: "+chooser.getSelectedFile().getName());
			 // viewer.setFile(chooser.getSelectedFile().getName());
			  viewer.frame.dispose();		 
			  viewer.setFrame(new MainFrame(new Viewer(chooser.getSelectedFile().getName()), 500, 220));    
		  }	  
	  } 
	  } 
	} 