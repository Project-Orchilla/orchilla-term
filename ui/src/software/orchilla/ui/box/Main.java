package software.orchilla.ui.box;

public class Main {
	public static void main(String[] args) {
		UiBox box = new UiBox(args);
		box.start();
		Runtime.getRuntime().addShutdownHook(new Thread(box::stop));
	}
}