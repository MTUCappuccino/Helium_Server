
public class Ghost {

	private String handle;
	private int lastKnown;

	public Ghost (Person person, int last) {
		handle = person.getHandle();
		lastKnown = last;
	}
	
	/**
	 * getHandle gets the handle
	 * 
	 * @return this persons handle
	 */
	public String getHandle() {
		return handle;
	}
	
	public int getLastKnown() {
		return lastKnown;
	}
}
