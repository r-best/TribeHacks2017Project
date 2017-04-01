package states;

import java.awt.*;

/*
* This would really do better as an interface
 */
public interface State {

	void update();

	void draw(Graphics2D graphics);

	void onEnter();

	void onExit();
}