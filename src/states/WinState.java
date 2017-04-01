package states;

import java.awt.*;

import Game.Game;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * Created by Bobby on 4/1/2017.
 */
public class WinState extends Application implements State {

	@Override
	public void start(Stage stage) throws Exception {
		WebView webview = new WebView();
		webview.getEngine().load(
				"https://www.youtube.com/embed/YG1c3Oasifc?autoplay=1"
		);
		webview.setPrefSize(Game.getGameWidth(), Game.getGameHeight());

		stage.setScene(new Scene(webview));
		stage.show();
	}

	@Override
	public void update() {

	}

	@Override
	public void draw(Graphics2D g) {
		g.setFont(g.getFont().deriveFont(80f));
		String str = "Congratulations, You Win!";
		g.drawString(str, Game.getGameWidth()/2-g.getFontMetrics().stringWidth(str)/2, Game.getGameHeight()/2-g.getFontMetrics().getHeight()/2);
	}

	@Override
	public void onEnter() {
		launch();
	}

	@Override
	public void onExit() {

	}
}