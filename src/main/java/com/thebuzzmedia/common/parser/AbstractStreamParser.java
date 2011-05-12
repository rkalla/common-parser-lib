package com.thebuzzmedia.common.parser;

import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractStreamParser extends
		AbstractParser<InputStream, byte[]> {
	public AbstractStreamParser() {
		buffer = new byte[BUFFER_SIZE];
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