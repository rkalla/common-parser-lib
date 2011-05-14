/**   
 * Copyright 2011 The Buzz Media, LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.thebuzzmedia.common.parser;

import java.io.IOException;
import java.io.Reader;

public abstract class AbstractReaderParser extends
		AbstractParser<Reader, char[]> {
	public AbstractReaderParser() {
		buffer = new char[BUFFER_SIZE];
	}

	protected int refillBuffer() throws IOException {
		int bytesKept = 0;

		if (running) {
			int bytesRead = 0;

			// Calculate how many bytes are being kept
			bytesKept = bufferLength - index;

			// Are we keeping any bytes or not?
			if (bytesKept == 0) {
				// No kept bytes, try and refill the entire buffer.
				bytesRead = input.read(buffer);
			} else {
				// Move all the "kept" bytes to the beginning of the buffer.
				System.arraycopy(buffer, index, buffer, 0, bytesKept);

				// Only refill the remainder of space in the buffer
				bytesRead = input.read(buffer, bytesKept, buffer.length
						- bytesKept);
			}

			// If we hit the end of stream, stop the parser.
			if (bytesRead == -1)
				running = false;

			// Move the index back to the front of the buffer
			index = 0;

			// Calculate the buffer's new length
			bufferLength = bytesKept + (running ? bytesRead : 0);
		}

		// Return the number of bytes kept to the caller.
		return bytesKept;
	}
}