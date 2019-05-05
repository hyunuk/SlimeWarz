package model;

import javax.swing.*;
import java.awt.*;

public class Helper {
	private Helper() {}
	private static class Singleton {
		private static final Helper instance = new Helper();
	}
	public static Helper getInstance() {
		return Singleton.instance;
	}

	public void attach(Container caller, JComponent callee, int x, int y, int width, int height) {
		caller.add(callee);
		callee.setBounds(x, y, width, height);
	}

	public void attach(Container caller, JComponent callee, Rectangle r) {
		caller.add(callee);
		callee.setBounds(r);
	}
}
