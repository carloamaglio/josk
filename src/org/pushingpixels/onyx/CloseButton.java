/*
 * Copyright (c) 2009-2010 Onyx Kirill Grouchnikov. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  o Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer. 
 *     
 *  o Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution. 
 *     
 *  o Neither the name of Onyx Kirill Grouchnikov nor the names of 
 *    its contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission. 
 *     
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package org.pushingpixels.onyx;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.swing.SwingRepaintCallback;

/**
 * The close button of the Onyx demo.
 * 
 * @author Kirill Grouchnikov
 */
public class CloseButton extends JButton {
	private static final long serialVersionUID = 5418676616086623103L;

	/**
	 * The alpha value of this button. Is updated in the fade-in timeline which
	 * starts when this button becomes a part of the host window hierarchy.
	 */
	float alpha;

//	Color crossColor;

	/**
	 * Creates a new close button.
	 */
	public CloseButton() {
		// mark the button as non-opaque since it will be
		// round shaped and translucent
		this.setOpaque(false);
		this.setForeground(new Color(158, 205, 255));
//		this.setForeground(Color.blue);
		this.alpha = 0.0f;
		this.setPreferredSize(new Dimension(32, 32));

		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						// dispose the host window
						Window windowAncestor = SwingUtilities.getWindowAncestor(CloseButton.this);
						boolean dispose = (windowAncestor instanceof JFrame) ? ((JFrame)windowAncestor).getDefaultCloseOperation()==JFrame.EXIT_ON_CLOSE : false;
						OnyxUtils.fadeOut(windowAncestor, 300, dispose);
					}
				});
			}
		});

		// timeline for the rollover effect (interpolating the
		// button's foreground color)
		final Timeline rolloverTimeline = new Timeline(this);
		rolloverTimeline.addPropertyToInterpolate("foreground", new Color(158, 205, 255), new Color(64, 155, 255));
//		Color c = getForeground();
//		float[] hsbvals = new float[3];
//		Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsbvals);
//		if (hsbvals[2]>0.5) hsbvals[2] *= 0.78; else {
//			hsbvals[2] = 0.9f;
//			if (hsbvals[2]>1) hsbvals[2]=1;
//		}
//		Color c2 = Color.getHSBColor(hsbvals[0], hsbvals[1], hsbvals[2]);
//		rolloverTimeline.addPropertyToInterpolate("crossColor", c, c2);
		rolloverTimeline.setDuration(200);

		// and register a mouse listener to play the rollover
		// timeline
		this.addMouseListener(new MouseAdapter() {
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
		this.addHierarchyListener(new HierarchyListener() {
			@Override
			public void hierarchyChanged(HierarchyEvent e) {
				Timeline shownTimeline = new Timeline(CloseButton.this);
				shownTimeline.addPropertyToInterpolate("alpha", 0.0f, 1.0f);
				shownTimeline.addCallback(new SwingRepaintCallback(CloseButton.this));
				shownTimeline.setDuration(500);
				shownTimeline.play();
			}
		});
	}

//	@Override
//    public void setForeground(Color fg) {
//	    super.setForeground(this.crossColor = fg);
//    }

//	public void setCrossColor(Color color) {
//		this.crossColor = color;
//	}

	/**
	 * Sets the alpha value. Used by the fade-in timeline.
	 * 
	 * @param alpha
	 *            Alpha value for this button.
	 */
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	@Override
	protected void paintBorder(Graphics g) {
		// overriden to remove the default border painting
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		Color c = this.getForeground();
//		Color c = this.crossColor;

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// use the current alpha
		g2d.setComposite(AlphaComposite.SrcOver.derive(this.alpha));

		// paint the background - black fill and a dark outline
		// based on the current foreground color
//		Shape contour = new Ellipse2D.Double(1, 1, getWidth() - 3, getHeight() - 3);
		Shape contour = new java.awt.geom.RoundRectangle2D.Double(1, 1, getWidth() - 3, getHeight() - 3, 10, 10);
		g2d.setColor(Color.black);
		g2d.setStroke(new BasicStroke(2.0f));
		g2d.fill(contour);
		g2d.setColor(c.darker().darker());
		g2d.draw(contour);

//		// use the current alpha
//		g2d.setComposite(AlphaComposite.SrcOver.derive(this.alpha));

		// paint the outer cross (always white)
		g2d.setColor(Color.white);
		g2d.setStroke(new BasicStroke(6.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		int w = (1 * getWidth()) / 4;
		int h = w;//getHeight();
		int offset = (getWidth()-w) / 2;
		int x1 = (getWidth()-w) / 2;
		int y1 = (getHeight()-h) / 2;
		int x2 = x1 + w - 1;
		int y2 = y1 + h - 1;
		g2d.drawLine(x1, y1, x2, y2);
		g2d.drawLine(x2, y1, x1, y2);

		// paint the inner cross (using the current foreground color)
		g2d.setColor(c);
		g2d.setStroke(new BasicStroke(4.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2d.drawLine(x1, y1, x2, y2);
		g2d.drawLine(x2, y1, x1, y2);

		g2d.dispose();
	}
}
