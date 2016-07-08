package com.github.sormuras.stash.stashlet;

import java.nio.ByteBuffer;

import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import com.github.sormuras.stash.Stashlet;

public interface Primitives {

	class IntStashlet implements Stashlet {

		@Override
		public Class<?> forClass() {
			return int.class;
		}

		@Override
		public TypeMirror forTypeMirror(Elements elements, Types types) {
			return types.getPrimitiveType(TypeKind.INT);
		}

		@Override
		public String getGetStatement() {
			return BUFFER + ".getInt()";
		}

		@Override
		public String getPutStatement(String name) {
			return BUFFER + ".putInt(" + name + ")";
		}

		/**
		 * @return "int"
		 */
		@Override
		public String getSourceSnippet() {
			return "int";
		}

		@Override
		public Object runtimeGet(ByteBuffer source) {
			return source.getInt();
		}

		@Override
		public void runtimePut(ByteBuffer target, Object object) {
			target.putInt((int) object);
		}

	}

}
