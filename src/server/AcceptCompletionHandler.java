package server;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, SessionState> {
	private final AsynchronousServerSocketChannel listener;

	public AcceptCompletionHandler(AsynchronousServerSocketChannel listener) {
		this.listener = listener;
	}

	@Override
	public void completed(AsynchronousSocketChannel socketChannel, SessionState sessionState) {
		// accept the next connection
		SessionState newSessionState = new SessionState();
		listener.accept(newSessionState, this);

		// handle this connection
		ByteBuffer inputBuffer = ByteBuffer.allocate(2048);
		ReadCompletionHandler readCompletionHandler = new ReadCompletionHandler(socketChannel, inputBuffer);
		socketChannel.read(inputBuffer, sessionState, readCompletionHandler);
	}

	@Override
	public void failed(Throwable exc, SessionState sessionState) {
		// Handle connection failure...
	}

}
