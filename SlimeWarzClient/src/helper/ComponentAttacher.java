package helper;

import javax.swing.*;
import java.awt.*;

public class ComponentAttacher {
	public static void attach(Container caller, JComponent callee, int x, int y, int width, int height) {
		caller.add(callee);
		callee.setBounds(x, y, width, height);
	}

	public static void attach(Container caller, JComponent callee, Rectangle r) {
		caller.add(callee);
		callee.setBounds(r);
	}
}
