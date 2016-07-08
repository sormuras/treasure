package com.github.sormuras.stash;

import java.nio.ByteBuffer;

import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public interface Stashlet {

	String BUFFER = "this.buffer";
	String CLOCK = "this.clock";
	String DELEGATE = "this.delegate";

	Class<?> forClass();

	TypeMirror forTypeMirror(Elements elements, Types types);

	String getGetStatement();

	String getPutStatement(String name);

	/**
	 * Returns an informative string representation of this type. The string is
	 * of a form suitable for representing this type in source code. Any names
	 * embedded in the result are qualified.
	 * 
	 * @return source code representation of this type.
	 */
	String getSourceSnippet();

	Object runtimeGet(ByteBuffer source);

	void runtimePut(ByteBuffer target, Object object);

}
