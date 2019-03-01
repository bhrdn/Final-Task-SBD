public class Parser {

	public String split(String __, String start, String end) {
		return __.substring(__.indexOf(start) + start.length() + 0x01, __.indexOf(end));
	}

	public void error(String msg, String reason) {
		System.out.printf("SQL Error (%s) || Reason: %s\n", msg, reason == null ? (char) 0x2d: reason);
		System.exit(0);
	}
}