package com.github.sormuras.stash;

public class Implementation implements Interface {
	
	public int sumA;
	public int sumO;

	@Override
	public int simple(int alpha, Integer omega) {
		sumA += alpha;
		sumO += omega;
		return alpha - omega;
	}

}
