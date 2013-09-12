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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;

import virtualKeyboard.OnScreenKeyboard.ShiftListener;

public class ButtonDecorator<T extends AbstractButton> implements ActionListener, ShiftListener {

	protected final OnScreenKeyboard keyboard;

	private final T button;

	private int keycode;
	private String commands;

	private String[] texts;
	private Object[] keycodes;

	private ActionListener listener;
	private JLabel led;
	private boolean ledState;
	private static final Icon ledOnIcon = Images.createIcon("led_green12x12.png");
	private static final Icon ledOffIcon = Images.createIcon("led_green12x12_off.png");

    private ButtonDecorator(T button, OnScreenKeyboard keyboard) {
		this.button = button;
		this.keyboard = keyboard;
		this.button.addActionListener(this);
    }

    public ButtonDecorator(T button, OnScreenKeyboard keyboard, int keycode) {
    	this(button, keyboard);
		this.keycode = keycode;
		this.commands = null;
		if (keycode==KeyEvent.VK_SHIFT || keycode==KeyEvent.VK_CAPS_LOCK || keycode==KeyEvent.VK_CONTROL || keycode==KeyEvent.VK_ALT) {
			button.setLayout(null);
			led = new JLabel();
			button.add(led);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ledState = !ledState;
					buttonActioned();
				}
			});
			button.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent e) {
					buttonResized();
				}
			});
			buttonActioned();
			buttonResized();
		}
	}

	public void buttonActioned() {
		led.setIcon(ledState ? ledOnIcon : ledOffIcon);
	}

    public void buttonResized() {
		Dimension size = led.getPreferredSize();
		int w = button.getWidth();
		if (w>0) led.setBounds(w-5-size.width, 5, size.width, size.height);
	}

	public ButtonDecorator(T button, OnScreenKeyboard keyboard, String[] texts, Integer[] keycodes) {
		this(button, keyboard);
		this.texts = texts;
		this.keycodes = keycodes;
		button.setText(texts[0]);
		keyboard.addShiftListener(this);
	}

	public ButtonDecorator(T button, OnScreenKeyboard keyboard,
			Object... textAndCode) {
		this(button, keyboard);
		this.texts = new String[textAndCode.length / 2];
		this.keycodes = new Object[textAndCode.length / 2];
		for (int i = 0; i < texts.length; i++) {
			this.texts[i] = (String) textAndCode[2 * i];
			this.keycodes[i] = textAndCode[2 * i + 1];
		}
		button.setText((String) textAndCode[0]);
		keyboard.addShiftListener(this);
	}

	public static <T extends JButton> T decorate(T button,
			OnScreenKeyboard keyboard, int keycode) {
		new ButtonDecorator(button, keyboard, keycode);
		return button;
	}

	public static <T extends JButton> T decorate(T button,
			OnScreenKeyboard keyboard, String[] texts, Integer[] keycodes) {
		new ButtonDecorator(button, keyboard, texts, keycodes);
		return button;
	}

	public static <T extends JButton> T decorate(T button,
			OnScreenKeyboard keyboard, Object... textAndCode) {
		new ButtonDecorator(button, keyboard, textAndCode);
		return button;
	}

	public ButtonDecorator(T button, OnScreenKeyboard keyboard, String commands) {
		this(button, keyboard);
		this.keycode = 0;
		this.commands = commands;
	}

	public static <T extends JButton> T decorate(T button,
			OnScreenKeyboard keyboard, String commands) {
		new ButtonDecorator(button, keyboard, commands);
		return button;
	}

	public ButtonDecorator(T button, OnScreenKeyboard keyboard,
			ActionListener listener) {
		this(button, keyboard);
		this.listener = listener;
	}

	private void press(int keycode) {
		keyboard.robot.keyPress(keycode);
	}

	private void release(int keycode) {
		keyboard.robot.keyRelease(keycode);
	}

	private void type(int keycode) {
		press(keycode);
		release(keycode);
	}

	private static final int SHIFT = 0;
	private static final int CTRL = 1;
	private static final int ALT = 2;

	private void doType(int keycode) {
		boolean shift = (keycode != KeyEvent.VK_ENTER && ((keyboard.getShifts() & (1 << SHIFT)) != 0));
		boolean ctrl = (keycode != KeyEvent.VK_ENTER && ((keyboard.getShifts() & (1 << CTRL)) != 0));
		boolean alt = (keycode != KeyEvent.VK_ENTER && ((keyboard.getShifts() & (1 << ALT)) != 0));
		if (shift)
			press(KeyEvent.VK_SHIFT);
		if (ctrl)
			press(KeyEvent.VK_CONTROL);
		if (alt)
			press(KeyEvent.VK_ALT);

		type(keycode);

		if (shift)
			release(KeyEvent.VK_SHIFT);
		if (ctrl)
			release(KeyEvent.VK_CONTROL);
		if (alt)
			release(KeyEvent.VK_ALT);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Btn b = (Btn)e.getSource();
		// int keycode = b.getMnemonic();
		if (listener != null) {
			listener.actionPerformed(e);
		} else if (commands != null) {
			handleKeycode(commands);
		} else {
			if (keycode == KeyEvent.VK_SHIFT) {
				keyboard.toggleShift(SHIFT);
			} else if (keycode == KeyEvent.VK_CONTROL) {
				keyboard.toggleShift(CTRL);
			} else if (keycode == KeyEvent.VK_ALT) {
				keyboard.toggleShift(ALT);
			} else {
				if (keycodes != null) {
					handleKeycode(keycodes[keyboard.getShifts()]);
				} else {
					doType(keycode);
				}
			}
		}
	}

	private void handleKeycode(Object keycode) {
		if (keycode instanceof String) {
			handleKeycode((String) keycode);
		} else if (keycode instanceof Integer) {
			handleKeycode((Integer) keycode);
		}
	}

	private void handleKeycode(String commands) {
		press(KeyEvent.VK_ALT);
		for (int i = 0; i < commands.length(); i++) {
			char c = commands.charAt(i);
			switch (c) {
			case '0':
				type(KeyEvent.VK_NUMPAD0);
				break;
			case '1':
				type(KeyEvent.VK_NUMPAD1);
				break;
			case '2':
				type(KeyEvent.VK_NUMPAD2);
				break;
			case '3':
				type(KeyEvent.VK_NUMPAD3);
				break;
			case '4':
				type(KeyEvent.VK_NUMPAD4);
				break;
			case '5':
				type(KeyEvent.VK_NUMPAD5);
				break;
			case '6':
				type(KeyEvent.VK_NUMPAD6);
				break;
			case '7':
				type(KeyEvent.VK_NUMPAD7);
				break;
			case '8':
				type(KeyEvent.VK_NUMPAD8);
				break;
			case '9':
				type(KeyEvent.VK_NUMPAD9);
				break;
			}
		}
		release(KeyEvent.VK_ALT);
	}

	private void handleKeycode(Integer keycode) {
		doType(keycode);
	}

	@Override
	public void shiftChanged() {
		int shifts = keyboard.getShifts();
		if (texts!=null && texts.length>shifts) button.setText(texts[shifts]);
	}
}
