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
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;

import layouts.KeyboardLayout;

import org.pushingpixels.trident.Timeline;

import eu.hansolo.custom.mbutton.MButton;

public class AlphanumericOnScreenKeyboard extends OnScreenKeyboard {
	private static final long serialVersionUID = 7051878406929402228L;

	protected JButton k;
	protected JButton esc, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12;
	protected JButton backSlash, one, two, three, four, five, six, seven, eight, nine, zero;
	protected JButton win;
	protected JButton ctrl;
	protected JButton alt;

	private boolean withFunctionKeys;
	private boolean full;

	private LinkedList<JButton> functionKeys;

	public AlphanumericOnScreenKeyboard(Class<? extends JButton> c, boolean withFunctionKeys, boolean full) {
		super(c);
		this.withFunctionKeys = withFunctionKeys;
		this.full = full;
		init();
	}

	public AlphanumericOnScreenKeyboard(boolean withFunctionKeys, boolean full) {
		this(MButton.class, withFunctionKeys, full);
	}

	private void init() {
		int rows = 5*4 + (withFunctionKeys?3:0);
		int cols = 4*(full ? 18 : 15)+2;
		JButton b;
		int g = 5;
		setLayout(new KeyboardLayout(rows, cols, g, g));
		setBorder(BorderFactory.createEmptyBorder(g, g, g, g));
		int r;
		int c;
		int h;

		functionKeys = new LinkedList<JButton>();

		r = 0;

		if (withFunctionKeys) {
			c = 0;
			h = 3;
			esc = add("ESC", KeyEvent.VK_ESCAPE, r, c, 4, h); c+=4;
			c+=4;
			functionKeys.add(f1 = add("F1", KeyEvent.VK_F1, r, c, 4, h)); c+=4;
			functionKeys.add(f2 = add("F2", KeyEvent.VK_F2, r, c, 4, h)); c+=4;
			functionKeys.add(f3 = add("F3", KeyEvent.VK_F3, r, c, 4, h)); c+=4;
			functionKeys.add(f4 = add("F4", KeyEvent.VK_F4, r, c, 4, h)); c+=4;
			c+=2;
			functionKeys.add(f5 = add("F5", KeyEvent.VK_F5, r, c, 4, h)); c+=4;
			functionKeys.add(f6 = add("F6", KeyEvent.VK_F6, r, c, 4, h)); c+=4;
			functionKeys.add(f7 = add("F7", KeyEvent.VK_F7, r, c, 4, h)); c+=4;
			functionKeys.add(f8 = add("F8", KeyEvent.VK_F8, r, c, 4, h)); c+=4;
			c+=2;
			functionKeys.add(f9 = add("F9", KeyEvent.VK_F9, r, c, 4, h)); c+=4;
			functionKeys.add(f10 = add("F10", KeyEvent.VK_F10, r, c, 4, h)); c+=4;
			functionKeys.add(f11 = add("F11", KeyEvent.VK_F11, r, c, 4, h)); c+=4;
			functionKeys.add(f12 = add("F12", KeyEvent.VK_F12, r, c, 4, h)); c+=4;
			r+=h;
		}

		h = 4;
		c = 0;
		add(new String[]{"\\", "|"}, new Integer[]{KeyEvent.VK_BACK_SLASH, KeyEvent.VK_BACK_SLASH}, r, c, 4, h); c+=4;
		add(new String[]{"1", "!"}, new Integer[]{KeyEvent.VK_1, KeyEvent.VK_1}, r, c, 4, h); c+=4;
		add(new String[]{"2", "\""}, new Integer[]{KeyEvent.VK_2, KeyEvent.VK_2}, r, c, 4, h); c+=4;
		add(new String[]{"3", "£"}, new Integer[]{KeyEvent.VK_3, KeyEvent.VK_3}, r, c, 4, h); c+=4;
		add(new String[]{"4", "$"}, new Integer[]{KeyEvent.VK_4, KeyEvent.VK_4}, r, c, 4, h); c+=4;
		add(new String[]{"5", "%"}, new Integer[]{KeyEvent.VK_5, KeyEvent.VK_5}, r, c, 4, h); c+=4;
		add(new String[]{"6", "&"}, new Integer[]{KeyEvent.VK_6, KeyEvent.VK_6}, r, c, 4, h); c+=4;
		add(new String[]{"7", "/"}, new Integer[]{KeyEvent.VK_7, KeyEvent.VK_7}, r, c, 4, h); c+=4;
		add(new String[]{"8", "("}, new Integer[]{KeyEvent.VK_8, KeyEvent.VK_8}, r, c, 4, h); c+=4;
		add(new String[]{"9", ")"}, new Integer[]{KeyEvent.VK_9, KeyEvent.VK_9}, r, c, 4, h); c+=4;
		add(new String[]{"0", "="}, new Integer[]{KeyEvent.VK_0, KeyEvent.VK_0}, r, c, 4, h); c+=4;
		add(new String[]{"'", "?"}, new Integer[]{KeyEvent.VK_QUOTE, KeyEvent.VK_QUOTE}, r, c, 4, h); c+=4;
		add(r, c, 4, h, "ì", "0236", "^", "0094"); c+=4;
//		add("<html>\u25C4\u2500\u2500<br><small>Backspace", KeyEvent.VK_BACK_SPACE, r, c, 8, 1); c+=8;
		add("", "KbdArrowBS.gif", KeyEvent.VK_BACK_SPACE, r, c, 8, h); c+=8;
		if (full) {
			add("INS", KeyEvent.VK_INSERT, r, c, 4, h); c+=4;
			add("HOME", KeyEvent.VK_HOME, r, c, 4, h); c+=4;
			add("PAG \u2191", KeyEvent.VK_PAGE_UP, r, c, 4, h); c+=4;
		}
		r+=h;

		c = 0;
//		add("TAB", KeyEvent.VK_TAB, r, c, 6, h); c+=6;
		add("", "KbdArrowTab.gif", KeyEvent.VK_TAB, r, c, 6, h); c+=6;
		add("Q", KeyEvent.VK_Q, r, c, 4, h); c+=4;
		add("W", KeyEvent.VK_W, r, c, 4, h); c+=4;
		add("E", KeyEvent.VK_E, r, c, 4, h); c+=4;
		add("R", KeyEvent.VK_R, r, c, 4, h); c+=4;
		add("T", KeyEvent.VK_T, r, c, 4, h); c+=4;
		add("Y", KeyEvent.VK_Y, r, c, 4, h); c+=4;
		add("U", KeyEvent.VK_U, r, c, 4, h); c+=4;
		add("I", KeyEvent.VK_I, r, c, 4, h); c+=4;
		add("O", KeyEvent.VK_O, r, c, 4, h); c+=4;
		add("P", KeyEvent.VK_P, r, c, 4, h); c+=4;
		add(r, c, 4, h, "è", "0232", "é", "0233"); c+=4;
		add(buttonCreator.create(), new String[]{"+", "*"}, new Integer[]{KeyEvent.VK_PLUS, KeyEvent.VK_PLUS}, r, c, 4, h); c+=4;
		c+=1;
//		b = add("<html>\u25c4\u2500\u2518<br><small>Invio", KeyEvent.VK_ENTER, r, c, 6, 2*h); c+=6;
		b = add("", "KbdArrowEnter.gif", KeyEvent.VK_ENTER, r, c, 5, 2*h); c+=5;
//		b.setFont(new Font("Courier New", 1, 20));
//		b.setForeground(Color.green);
		if (full) {
			add("DEL", KeyEvent.VK_DELETE, r, c, 4, h); c+=4;
			add("END", KeyEvent.VK_END, r, c, 4, h); c+=4;
			add("PAG \u2193", KeyEvent.VK_PAGE_DOWN, r, c, 4, h); c+=4;
		}
		r+=h;

		c = 0;
//		add("<html><small><p><div align=left><font size=5>ALOCK", KeyEvent.VK_CAPS_LOCK, r, c, 7, h); c+=7;
		add("", "KbdCapsLock.gif", KeyEvent.VK_CAPS_LOCK, r, c, 7, h); c+=7;
		add("A", KeyEvent.VK_A, r, c, 4, h); c+=4;
		add("S", KeyEvent.VK_S, r, c, 4, h); c+=4;
		add("D", KeyEvent.VK_D, r, c, 4, h); c+=4;
		add("F", KeyEvent.VK_F, r, c, 4, h); c+=4;
		add("G", KeyEvent.VK_G, r, c, 4, h); c+=4;
		add("H", KeyEvent.VK_H, r, c, 4, h); c+=4;
		add("J", KeyEvent.VK_J, r, c, 4, h); c+=4;
		add("K", KeyEvent.VK_K, r, c, 4, h); c+=4;
		add("L", KeyEvent.VK_L, r, c, 4, h); c+=4;
		add(r, c, 4, h, "ò", "0242", "ç", "0231"); c+=4;
		add(r, c, 4, h, "à", "0224", "°", "0176"); c+=4;
		add(r, c, 4, h, "ù", "0249", "§", "0167"); c+=4;
		r+=h;

		c = 0;
//		add("<html><small><p><div align=left><font size=4>SHIFT", "KbdArrowShift.gif", KeyEvent.VK_SHIFT, r, c, 5, h); c+=5;
		add("", "KbdArrowShift.gif", KeyEvent.VK_SHIFT, r, c, 5, h); c+=5;
		add(new String[]{"<", ">"}, new Integer[]{KeyEvent.VK_LESS, KeyEvent.VK_LESS}, r, c, 4, h); c+=4;
		add("Z", KeyEvent.VK_Z, r, c, 4, h); c+=4;
		add("X", KeyEvent.VK_X, r, c, 4, h); c+=4;
		add("C", KeyEvent.VK_C, r, c, 4, h); c+=4;
		add("V", KeyEvent.VK_V, r, c, 4, h); c+=4;
		add("B", KeyEvent.VK_B, r, c, 4, h); c+=4;
		add("N", KeyEvent.VK_N, r, c, 4, h); c+=4;
		add("M", KeyEvent.VK_M, r, c, 4, h); c+=4;
		add(new String[]{",", ";"}, new Integer[]{KeyEvent.VK_COMMA, KeyEvent.VK_COMMA}, r, c, 4, h); c+=4;
		add(new String[]{".", ":"}, new Integer[]{KeyEvent.VK_PERIOD, KeyEvent.VK_PERIOD}, r, c, 4, h); c+=4;
		add(new String[]{"-", "_"}, new Integer[]{KeyEvent.VK_MINUS, KeyEvent.VK_MINUS}, r, c, 4, h); c+=4;
		c += (full ? 15 : 3);
//		add("\u2191", KeyEvent.VK_UP, r, c, 4, h); c+=4;
		add("", "KbdArrowUp.gif", KeyEvent.VK_UP, r, c, 4, h); c+=4;
		r+=h;

		c = 0;
		ctrl = add("Ctrl", "", KeyEvent.VK_CONTROL, r, c, 6, h); c+=6;
		win = add("Win", KeyEvent.VK_WINDOWS, r, c, 5, h); c+=5;
//		win.setVisible(false);
		alt = add("Alt", KeyEvent.VK_ALT, r, c, 5, h); c+=5;
//		c = 4*4;
		add("", KeyEvent.VK_SPACE, r, c, 23, h); c+=23;
		c = full ? 15*4 : 12*4-0;
//		add("\u2190", KeyEvent.VK_LEFT, r, c, 4, h); c++;
		add("", "KbdArrowLeft.gif", KeyEvent.VK_LEFT, r, c, 4, h); c+=4;
//		add("\u2193", KeyEvent.VK_DOWN, r, c, 4, h); c++;
		add("", "KbdArrowDown.gif", KeyEvent.VK_DOWN, r, c, 4, h); c+=4;
//		add("\u2192", KeyEvent.VK_RIGHT, r, c, 4, h); c++;
		add("", "KbdArrowRight.gif", KeyEvent.VK_RIGHT, r, c, 4, h); c+=4;
		r+=h;

		if (full) {
			setTopMargin(5);
//			setCloseButtonDim(48);
		}
	}

	public void setWindowsKeyVisible(boolean visible) {
		win.setVisible(visible);
	}

	public void setFunctionKeysVisible(boolean visible) {
		for (JButton b : functionKeys) {
			b.setVisible(visible);
		}
	}

	private static final class Btn extends JButton {
		private static final long serialVersionUID = 3601572004722509229L;

		/**
		 * The alpha value of this button. Is updated in the fade-in timeline
		 * which starts when this button becomes a part of the host window
		 * hierarchy.
		 */
		float alpha;

		Btn() {
			super();
			setOpaque(true);
			alpha = 0.0f;
			Color c1 = Color.white;

			this.setMinimumSize(new Dimension(32, 32));
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

		Btn(String text, Icon icon) {
			this();
			init(text, icon);
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
