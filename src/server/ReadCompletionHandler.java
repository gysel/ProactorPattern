package server;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ReadCompletionHandler implements CompletionHandler<Integer, SessionState> {
	private final AsynchronousSocketChannel socketChannel;
	private final ByteBuffer inputBuffer;

	public ReadCompletionHandler(AsynchronousSocketChannel socketChannel, ByteBuffer inputBuffer) {
		this.socketChannel = socketChannel;
		this.inputBuffer = inputBuffer;
	}

	@Override
	public void completed(Integer bytesRead, SessionState sessionState) {

		byte[] buffer = new byte[bytesRead];
		inputBuffer.rewind();
		// Rewind the input buffer to read from the beginning

		inputBuffer.get(buffer);
		String message = new String(buffer);

		System.out.println("Received message from client : " + message);

		// Echo the message back to client
		WriteCompletionHandler writeCompletionHandler = new WriteCompletionHandler(socketChannel);

		ByteBuffer outputBuffer = ByteBuffer.wrap(buffer);

		socketChannel.write(outputBuffer, sessionState, writeCompletionHandler);
	}

	@Override
	public void failed(Throwable exc, SessionState attachment) {
		// Handle read failure.....
	}

}
