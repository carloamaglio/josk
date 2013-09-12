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
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;

import layouts.KeyboardLayout;

import org.pushingpixels.trident.Timeline;

import eu.hansolo.custom.mbutton.MButton;

public class NumericOnScreenKeyboard extends OnScreenKeyboard {
	private static final long serialVersionUID = 2921912326049778249L;

	protected JButton[][] buttons;

	public NumericOnScreenKeyboard(Class<? extends JButton> c) {
		super(c);
		init();
	}

	public NumericOnScreenKeyboard() {
		this(MButton.class);
	}

	private void init() {
		int rows = 5;
		int cols = 4;
		buttons = new JButton[rows][cols];
		JButton b;
		int g = 3;
		setLayout(new KeyboardLayout(rows, cols, g, g));
		setBorder(BorderFactory.createEmptyBorder(g, g, g, g));
		int r;
		int c;

		r = 0;
		c = 0;
		c++;
		buttons[r][c] = add("/", KeyEvent.VK_DIVIDE, r, c, 1, 1); c++;
		buttons[r][c] = add("*", KeyEvent.VK_MULTIPLY, r, c, 1, 1); c++;
		buttons[r][c] = add("-", KeyEvent.VK_MINUS, r, c, 1, 1); c++;
		r++;

		c = 0;
		buttons[r][c] = add("7", KeyEvent.VK_7, r, c, 1, 1); c++;
		buttons[r][c] = add("8", KeyEvent.VK_8, r, c, 1, 1); c++;
		buttons[r][c] = add("9", KeyEvent.VK_9, r, c, 1, 1); c++;
		buttons[r][c] = add("+", KeyEvent.VK_PLUS, r, c, 1, 2); c++;
		r++;

		c = 0;
		buttons[r][c] = add("4", KeyEvent.VK_4, r, c, 1, 1); c++;
		buttons[r][c] = add("5", KeyEvent.VK_5, r, c, 1, 1); c++;
		buttons[r][c] = add("6", KeyEvent.VK_6, r, c, 1, 1); c++;
		r++;

		c = 0;
		buttons[r][c] = add("1", KeyEvent.VK_1, r, c, 1, 1); c++;
		buttons[r][c] = add("2", KeyEvent.VK_2, r, c, 1, 1); c++;
		buttons[r][c] = add("3", KeyEvent.VK_3, r, c, 1, 1); c++;
		buttons[r][c] = b = add("<html>\u25c4\u2500\u2518<br><small>Invio", KeyEvent.VK_ENTER, r, c, 1, 2); c++;
		b.setFont(new Font("Courier New", 1, 20));
		b.setForeground(Color.green);
		b.setEnabled(false);
//		buttons[r][c] = b = add(buttonCreator.create("<html>\u21B5<br><small>Invio", null), KeyEvent.VK_ENTER, r, c, 1, 2); c++;
//		b.setFont(new Font("OpenSymbol", 1, 32));
		r++;

		c = 0;
		buttons[r][c] = add("0", KeyEvent.VK_0, r, c, 2, 1); c+=2;
		buttons[r][c] = b = add(".", KeyEvent.VK_PERIOD, r, c, 1, 1); c++;
		b.setEnabled(false);
		r++;
	}

	private static final class Btn extends JButton {
		private static final long serialVersionUID = 3601572004722509229L;

		/**
		 * The alpha value of this button. Is updated in the fade-in timeline
		 * which starts when this button becomes a part of the host window
		 * hierarchy.
		 */
		float alpha;

		Btn(String text, Icon icon) {
			super(text, icon);
			setOpaque(true);
			alpha = 0.0f;
			Color c1 = Color.white;

			// timeline for the rollover effect (interpolating the
			// button's foreground color)
			final Timeline rolloverTimeline = new Timeline(this);
			rolloverTimeline.addPropertyToInterpolate("background", c1, new Color(64, 140, 255));
			rolloverTimeline.setDuration(200);
			this.setBackground(c1);

			// and register a mouse listener to play the rollover
			// timeline
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					rolloverTimeline.play();
				}

				@Override
				public void mouseExited(MouseEvent e) {
					rolloverTimeline.playReverse();
				}
			});

			// fade in the component once it's part of the window
			// hierarchy
//			this.addHierarchyListener(new HierarchyListener() {
//				@Override
//				public void hierarchyChanged(HierarchyEvent e) {
//					Timeline shownTimeline = new Timeline(Btn.this);
//					shownTimeline.addPropertyToInterpolate("alpha", 0.0f, 1.0f);
//					shownTimeline.addCallback(new Repaint(Btn.this));
//					shownTimeline.setDuration(500);
//					shownTimeline.play();
//				}
//			});
		}

		/**
		 * Sets the alpha value. Used by the fade-in timeline.
		 * 
		 * @param alpha
		 *            Alpha value for this button.
		 */
		public void setAlpha(float alpha) {
			this.alpha = alpha;
		}
	}
}
