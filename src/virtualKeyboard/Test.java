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

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.UIManager;

public class Test {

	/**
	 * Create the GUI and show it. For thread safety, this method should be invoked from the event-dispatching thread.
	 */
	public static void createAndShowGUI() {
		if (false) {
			JWindow win = new JWindow();

			JPanel pan = new JPanel();
			win.add(pan, "Center");

			pan.setLayout(new FlowLayout());
			pan.add(new JButton("Hello"));

			win.setSize(200, 200);
			win.setVisible(true);
			return;
		}

		if (false) UIManager.put("ButtonUI", "virtualKeyboard.BtnUI");

		// Create and set up the window.
		AlphanumericOnScreenKeyboard/* OnScreenKeyboard */vk = new AlphanumericOnScreenKeyboard(true, true);
//	    vk.setOpaque(true);
//	    vk.setBackground(Color.DARK_GRAY);
//	    vk.setFunctionKeysVisible(false);

		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x, y, w, h;
		if (true) {
			w = (int) (24 * dimension.getWidth()  / 30);
			h = (int) (dimension.getHeight() / 3);
			x = (int) (dimension.getWidth() - w - 24);
			y = (int) 24;
		} else {
			w = 800;
			h = 320;
			x = 200;
			y = 24;
		}
		JFrame frame = vk.setShow(x, y, w, h);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGUI();
			}
		});
	}

}
