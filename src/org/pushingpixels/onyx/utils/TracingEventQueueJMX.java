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

package org.pushingpixels.onyx.utils;

import java.awt.AWTEvent;
import java.awt.EventQueue;

public final class TracingEventQueueJMX extends EventQueue {

	private TracingEventQueueThreadJMX tracingThread;

	public TracingEventQueueJMX() {
		this.tracingThread = new TracingEventQueueThreadJMX(500);
		this.tracingThread.start();
	}

	@Override
	protected void dispatchEvent(AWTEvent event) {
		this.tracingThread.eventDispatched(event);
		super.dispatchEvent(event);
		this.tracingThread.eventProcessed(event);
	}
}
