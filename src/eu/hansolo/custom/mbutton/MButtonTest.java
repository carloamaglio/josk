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

package eu.hansolo.custom.mbutton;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MButtonTest extends JPanel {
	private static final long serialVersionUID = 3660543287947621067L;

	public MButtonTest() {
		this.setLayout(new GridLayout(4, 4, 3, 3));
		this.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		this.setBackground(Color.blue);

		add(new MButton("7"));
		add(new MButton("8"));
		add(new MButton("7"));
		add(new MButton("X"));

		add(new MButton("4"));
		add(new MButton("5"));
		add(new MButton("6"));
		add(new MButton("C"));

		add(new MButton("1"));
		add(new MButton("2"));
		add(new MButton("3"));
		add(new MButton("+"));

		add(new MButton("0"));
		add(new MButton("."));
		add(new MButton("+/-"));
		add(new MButton("="));
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {

		// Create and set up the window.
		JFrame frame = new JFrame("ButtonDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and set up the content pane.
		MButtonTest newContentPane = new MButtonTest();
		newContentPane.setOpaque(true); // content panes must be opaque
		frame.setContentPane(newContentPane);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
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
