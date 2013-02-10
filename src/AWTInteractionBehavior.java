/*FILE COPIED VERBATIM: http://webdocs.cs.ualberta.ca/~lihang/Share/Java3D/Pyramid/AWTInteractionBehavior.java
 * AWTInteractionBehavior.java 1/28/2003 12:49PM
 * Adapted from .\j2sdk1.4.1_01\demo\java3d\AWT_Interaction
 */

import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.util.*;

public class AWTInteractionBehavior extends Behavior
implements AdjustmentListener {

    private TransformGroup transformGroup;
    private Transform3D t3d = new Transform3D();
    private WakeupCriterion criterion;
    private double scale = 0;

    // create a new AWTInteractionBehavior
    public AWTInteractionBehavior(TransformGroup tg) {
	transformGroup = tg;
    }

    // initialize the behavior to wakeup on a behavior post with the id
    // MouseEvent.MOUSE_CLICKED
    public void initialize() {
	criterion = new WakeupOnBehaviorPost(this,
 					     MouseEvent.MOUSE_CLICKED);
	wakeupOn(criterion);
    }

    // processStimulus to transform the object
    public void processStimulus(Enumeration criteria) {
	/*
	// get the current Transform3D
	transformGroup.getTransform(t3d);	
	t3d.mul(scale);
	*/
	t3d.set(scale);
	transformGroup.setTransform(t3d);
	wakeupOn(criterion);
    }

    // when the mouse is clicked, postId for the behavior
    public void adjustmentValueChanged (AdjustmentEvent e) {
        int value = e.getValue();
	scale = value/6.0;
	postId(MouseEvent.MOUSE_CLICKED);
    }
}