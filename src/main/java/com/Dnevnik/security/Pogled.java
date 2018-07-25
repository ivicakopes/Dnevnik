package com.Dnevnik.security;

public class Pogled {

	public static class Uc {}

	public static class Rod extends Uc {}
	
	public static class Nas extends Rod{ }

	public static class Ad extends Nas {}
	
	public static class Razredni extends Uc {}
}
