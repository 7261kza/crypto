package sample;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;


public class AnimationShake {
    private TranslateTransition tt;

    public AnimationShake(Node node) {
        tt = new TranslateTransition(Duration.millis(70), node);
        tt.setFromX(-10f);
        tt.setByX(10f);
        tt.setCycleCount(3);
        tt.setAutoReverse(true);
    }

    public void playAnim() {
        tt.playFromStart();
    }
}
