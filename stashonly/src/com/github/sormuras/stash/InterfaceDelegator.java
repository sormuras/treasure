package com.github.sormuras.stash;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class InterfaceDelegator implements Interface {

	protected final ByteBuffer buffer;
	protected int counter;
	protected final Interface delegate;

	public InterfaceDelegator(ByteBuffer buffer, Interface delegate) {
		this.delegate = delegate;
		this.buffer = buffer;
		this.counter = buffer.getInt();
		Map<Long, Callable<?>> map = new HashMap<>();
		map.put(0x8D2CEDD3L, this::simple$0x8D2CEDD3L);
		map.put(0x00000000L, this::$this);
		for (long index = 0; index < counter; index++) {
			long hash = buffer.getLong();
			Callable<?> runnable = map.get(hash);
			try {
				runnable.call();
			} catch (Exception e) {
				throw new AssertionError(String.format("#%d (hash=0x%08X) failed!", index, hash));
			}
		}
		buffer.limit(buffer.capacity());
	}

	@Override
	public int simple(int alpha, Integer omega) throws InterruptedException {
		this.buffer.putLong(0x8D2CEDD3L);
		this.buffer.mark();
		this.buffer.putInt(alpha);
		this.buffer.putInt(omega.intValue());
		int $expectedPosition = this.buffer.position();
		this.buffer.reset();
		int $result = simple$0x8D2CEDD3L();
		assert $expectedPosition == this.buffer.position() : "expected position " + $expectedPosition + ", but it is "
				+ this.buffer.position();
		this.buffer.putInt(0, ++counter);
		return $result;
	}

	protected Object $this() {
		return this;
	}

	protected int simple$0x8D2CEDD3L() throws InterruptedException {
		int alpha = this.buffer.getInt();
		Integer omega = Integer.valueOf(this.buffer.getInt());
		return this.delegate.simple(alpha, omega);
	}

}
