/**
 * 
 * Copyright 2013 Dott. Ing. Carlo Amaglio - Via Emigli, 10 - 25081 Bedizzole (BS) - Italy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * 
 */

package virtualKeyboard;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;

public class BtnUI extends BasicButtonUI {
	private final static BtnUI ui = new BtnUI();

	// private final static Color selectColor = new Color(200, 100, 0);

	// ********************************
	// Create PLAF
	// ********************************
	public static ComponentUI createUI(JComponent c) {
		return ui;
	}

	// @Override
	// protected Color getSelectColor() {
	// return selectColor;
	// }

	@Override
	public void paint(Graphics g, JComponent c) {
		if (true) {
			super.paint(g, c);
		} else {
			AbstractButton b = (AbstractButton) c;
			ButtonModel model = b.getModel();
			Graphics2D g2 = (Graphics2D) g.create();
			boolean raised;
			if (model.isArmed() && model.isPressed()) {
				raised = false;
			} else {
				raised = true;
			}
			g2.setColor(Color.red);
			g2.draw3DRect(10, 10, b.getWidth()-20, b.getHeight()-20, raised);
			g2.dispose();
		}
	}
}
