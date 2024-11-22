package Amazon.enumFile;

public enum LocatorType {
	XPATH("xpath"),LINKTEXT("linkText"),CLASSNAME("className"),ID("id");

	public final String label;
	
	private LocatorType(String label) {
		this.label=label;
	}
	
}
