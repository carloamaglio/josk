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

import it.amaglio.utils.Images;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.EventListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingConstants;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.PanelUI;

import org.pushingpixels.onyx.CloseButton;
import org.pushingpixels.onyx.OnyxUtils;

public abstract class OnScreenKeyboard extends JPanel {
	private static final long serialVersionUID = 4204309939749059052L;

	protected Robot robot;
	protected ButtonCreator buttonCreator;

	protected static interface ButtonCreator<T extends JButton> {
		public T create();
	}

	public OnScreenKeyboard(Class<? extends JButton> c) {
//		setOpaque(true); // content panes must be opaque
		buttonCreator = null;

		try {
			final Constructor<? extends JButton> constructor = c.getConstructor();
			buttonCreator = new ButtonCreator() {
				@Override
				public JButton create() {
					try {
						return constructor.newInstance();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
					return null;
				}
			};
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}

		setFocusable(false);
		setOpaque(true);
		setBackground(Color.DARK_GRAY);
//	    setBackground(new Color(0f, 0f, 0f, 0.1f));
	}

	@Override
	public void setUI(final PanelUI UI) {
		super.setUI(new MPanelUI(this));
	}

	@Override
	protected void setUI(final javax.swing.plaf.ComponentUI UI) {
		super.setUI(new MPanelUI(this));
	}

	protected <T extends JButton> T add(T c, int row, int col, int w, int h) {
		add(c, new Rectangle(col, row, w, h));
		return c;
	}

	protected <T extends JButton> T add(T c, int keycode, int row, int col, int w, int h) {
		return add(ButtonDecorator.decorate(c, this, keycode), row, col, w, h);
	}

	protected JButton add(String text, int keycode, int row, int col, int w, int h) {
		return add(createButton(text), keycode, row, col, w, h);
	}

	protected JButton add(String text, String icon, int keycode, int row, int col, int w, int h) {
		return add(createButton(text, icon), keycode, row, col, w, h);
	}

	protected <T extends JButton> T add(T c, String[] texts, Integer[] keycodes, int row, int col, int w, int h) {
		return add(ButtonDecorator.decorate(c, this, texts, keycodes), row, col, w, h);
	}

	protected JButton add(String[] texts, Integer[] keycodes, int row, int col, int w, int h) {
		return add(ButtonDecorator.decorate(createButton(), this, texts, keycodes), row, col, w, h);
	}

	protected <T extends JButton> T add(T c, int row, int col, int w, int h, Object... textAndCode) {
		return add(ButtonDecorator.decorate(c, this, textAndCode), row, col, w, h);
	}

	protected JButton add(int row, int col, int w, int h, Object... textAndCode) {
		return add(ButtonDecorator.decorate(createButton(), this, textAndCode), row, col, w, h);
	}

	protected <T extends JButton> T add(T c, String command, int row, int col, int w, int h) {
		return add(ButtonDecorator.decorate(c, this, command), row, col, w, h);
	}

	private JButton createButton() {
		JButton rv = buttonCreator.create();
		rv.setMinimumSize(new Dimension(32, 32));
		return rv;
	}

	private JButton createButton(String text) {
		JButton rv = createButton();
		rv.setText(text);
		return rv;
	}

	private JButton createButton(String text, Icon icon) {
		JButton rv = createButton(text);
		rv.setIcon(icon);
		rv.setVerticalAlignment(SwingConstants.BOTTOM);
//		rv.setVerticalTextPosition(SwingConstants.BOTTOM);
//		rv.setHorizontalTextPosition(SwingConstants.LEFT);
		rv.setHorizontalAlignment(SwingConstants.LEFT);
		rv.setMargin(new Insets(20, 2, 10, 2));
		return rv;
	}

	private JButton createButton(String text, String icon) {
		return createButton(text, Images.createIcon(icon));
	}

	int shifts;

	protected void setShift(int bit) {
		setShifts(shifts | (1 << bit));
	}

	protected void resetShift(int bit) {
		setShifts(shifts & ~(1 << bit));
	}

	protected void toggleShift(int bit) {
		setShifts(shifts ^ (1 << bit));
	}

	protected void setShifts(int shifts) {
		if (shifts != this.shifts) {
			this.shifts = shifts;
			fireShiftListener();
		}
	}

	protected int getShifts() {
		return shifts;
	}

	public interface ShiftListener extends EventListener {
		public void shiftChanged();
	}

	public void addShiftListener(ShiftListener l) {
		listenerList.add(ShiftListener.class, l);
	}

	public void removeShiftListener(ShiftListener l) {
		listenerList.remove(ShiftListener.class, l);
	}

	public void fireShiftListener() {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ShiftListener.class) {
				((ShiftListener) listeners[i + 1]).shiftChanged();
			}
		}
	}

	private static final class KFrame extends JFrame {
		private static final long serialVersionUID = -448622241794179370L;
		private static KFrame instance;

		public static KFrame getInstance() {
			if (instance == null) {
				instance = new KFrame();
			}
			return instance;
		}

		private KFrame() {
			super();
//	    	setFocusable(false);
			setFocusableWindowState(false);
//		    getContentPane().add(new JLabel(" HEY!!!"));
//		    setUndecorated(true);
			setIconImage(null);
			setSize(400, 200);
			setAlwaysOnTop(true);
		}
	}

	private static final class KFrame2 extends JFrame implements MouseMotionListener, MouseListener {
		private static final long serialVersionUID = -2796177365970282370L;
		private static KFrame2 instance;
		JPanel contentPanel;
		CloseButton closeButton;
		private int topMargin = 20;
		private int closeButtonDim = 48;

		public static KFrame2 getInstance() {
			if (instance == null) {
				instance = new KFrame2();
			}
			return instance;
		}

		private KFrame2() {
			super();
			setFocusableWindowState(false);
			setUndecorated(true);
			if (true) OnyxUtils.setWindowAsNonOpaque(this);
//		    setBackground(new Color(0, 0, 0, 0));
//			setIconImage(Images.createImage("keyboard.png"));
//		    setSize(400, 200);
//		    setAlwaysOnTop(true);
			closeButton = new CloseButton();
			Container contentPane = this.getContentPane();
			if (false) contentPane.setBackground(new Color(0, 0, 0, 0.1f));
			contentPane.add(this.closeButton);
			contentPane.setComponentZOrder(this.closeButton, 0);
			contentPane.setLayout(new LayoutManager() {
				@Override
				public void addLayoutComponent(String name, Component comp) {}

				@Override
				public void removeLayoutComponent(Component comp) {}

				@Override
				public Dimension minimumLayoutSize(Container parent) {
					return null;
				}

				@Override
				public Dimension preferredLayoutSize(Container parent) {
					return null;
				}

				@Override
				public void layoutContainer(Container parent) {
//					int topMargin = 20;
//					int closeButtonDim = 48;
					closeButton.setBounds(getWidth() - closeButtonDim, 0, closeButtonDim, closeButtonDim - 16);
					if (contentPanel != null) contentPanel.setBounds(0, topMargin, getWidth() - 10, getHeight() - topMargin);
				}
			});

			addWindowFocusListener(new WindowAdapter() {
				@Override
				public void windowLostFocus(WindowEvent e) {
//					Window windowAncestor = SwingUtilities.getWindowAncestor(KFrame2.this);
//					OnyxUtils.fadeOut(windowAncestor, 500, false);
				}
			});

			this.addMouseMotionListener(this);
			this.addMouseListener(this);

			this.setSize(560, 230);
			this.setLocationRelativeTo(null);
		}

		public void setTopMargin(int topMargin) {
			this.topMargin = topMargin;
		}

		public void setCloseButtonDim(int closeButtonDim) {
			this.closeButtonDim = closeButtonDim;
		}

		public void setContentPanel(JPanel contentPanel) {
			if (this.contentPanel != contentPanel) {
				Container contentPane = this.getContentPane();
				if (this.contentPanel != null) {
					contentPane.remove(this.contentPanel);
				}
				this.contentPanel = contentPanel;
				if (contentPanel != null) {
					contentPane.add(this.contentPanel);
					contentPane.setComponentZOrder(this.contentPanel, 1);
				}
			}
		}

		private static final int RESIZEDIM = 32;
		private Point mp;
		private boolean resize = false;

		@Override
		public void mouseClicked(MouseEvent e) {}

		/**
		 * 
		 * Se il punto selezionato e' in corrispondenza dell'angolo in basso a DX allora resize, diversamente move.
		 */
		@Override
		public void mousePressed(MouseEvent e) {
			mp = e.getPoint();
			if ((mp.x > (getWidth() - RESIZEDIM)) && (mp.y > (getHeight() - RESIZEDIM))) {
				setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
				resize = true;
			} else {
				setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
				resize = false;
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			mp = null;
			resize = false;
			setCursor(null);
			repaint();
		}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {
			if (mp == null) setCursor(null);
		}

		/**
		 * Sposta o ridimensiona la tastiera.
		 */
		@Override
		public void mouseDragged(MouseEvent e) {
			if (mp != null) {
				Point p = e.getPoint();
				int x = getX() + p.x - mp.x;
				int y = getY() + p.y - mp.y;

				if (resize) {
					int newWidth = getWidth() + p.x - mp.x;
					int newHeight = getHeight() + p.y - mp.y;
					mp.x = p.x;
					mp.y = p.y;

					setSize(newWidth, newHeight);
					setVisible(true);
				} else {
					setLocation(x, y);
				}
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			Point mp = e.getPoint();
			if ((mp.x > (getWidth() - RESIZEDIM)) && (mp.y > (getHeight() - RESIZEDIM))) {
				setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
			} else {
				setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			}
		}
	}

	private int topMargin = 20;
	private int closeButtonDim = 48;

	public void setTopMargin(int topMargin) {
		this.topMargin = topMargin;
	}

	public void setCloseButtonDim(int closeButtonDim) {
		this.closeButtonDim = closeButtonDim;
	}

	public JFrame setShow(int x, int y, int w, int h) {
		final KFrame2 frame = KFrame2.getInstance();
		frame.setTopMargin(topMargin);
		frame.setCloseButtonDim(closeButtonDim);
		frame.setContentPanel(this);
		if (!frame.isVisible()) {
			// Display the window.
//			frame.pack();
			frame.setLocation(x, y);
			frame.setPreferredSize(new Dimension(w, h));
			frame.setSize(new Dimension(w, h));
		}
		frame.setVisible(true);
		frame.setAlwaysOnTop(true);
		frame.requestFocus();
		frame.setExtendedState(Frame.NORMAL);
		if (true) OnyxUtils.setWindowOpacity(frame, 1.0f);
		return frame;
	}

	public void _setShow(int x, int y, int w, int h) {
//		final JWindow frame = new JWindow();
		JFrame frame = KFrame.getInstance();
//	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		if (!frame.isDisplayable()) {
			frame.setUndecorated(true);
			frame.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
		}
		if (!frame.isVisible()) {
//			frame.setContentPane(OnScreenKeyboard.getInstance());
			boolean withBorder = false;
			boolean withCloseBtn = false;
			Container pan = withBorder ? new JPanel(new BorderLayout()) : frame.getContentPane();
			if (withBorder) frame.add(pan, "Center");
			pan.add(this, BorderLayout.CENTER);

			if (withCloseBtn) {
				final JFrame f = frame;
				JButton closeBtn = new JButton("CLOSE");
				closeBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						f.setVisible(false);
						f.dispose();
					}
				});
				pan.add(closeBtn, BorderLayout.NORTH);
			}

			// Display the window.
			frame.pack();
			frame.setLocation(x, y);
			frame.setPreferredSize(new Dimension(w, h));
			frame.setSize(new Dimension(w, h));
			frame.setVisible(true);
		}
	}

	static final class MPanelUI extends PanelUI {

		// ********************************
		// Create PLAF
		// ********************************
		public static ComponentUI createUI(JComponent c) {
			return new MPanelUI();
		}

		MPanelUI() {
			super();
		}

		MPanelUI(JPanel panel) {
			this();
		}

		@Override
		public void update(Graphics g, JComponent c) {
			if (true) {
				super.update(g, c);
			} else {
				if (c.isOpaque()) {
					Graphics2D g2 = (Graphics2D) g.create();
					Color col = c.getBackground();// Color.DARK_GRAY;
					int w = c.getWidth();
					int h = c.getHeight();
					Color sc;
					GradientPaint redtowhite;
					sc = col.brighter();
					redtowhite = new GradientPaint(0, h / 2, sc, 0, h, col);
					g2.setPaint(redtowhite);
					// g.setColor(c.getBackground());
					g2.fillRect(0, 0, w, h);
					g2.dispose();
				}
				paint(g, c);
			}
		}
	}
}
