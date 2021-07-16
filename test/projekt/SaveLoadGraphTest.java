package projekt;

import org.assertj.swing.core.BasicComponentFinder;
import org.assertj.swing.core.ComponentFinder;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

import java.awt.*;

import static org.assertj.swing.finder.WindowFinder.findFrame;
import static org.assertj.swing.launcher.ApplicationLauncher.application;

public class SaveLoadGraphTest extends AssertJSwingJUnitTestCase {
    private FrameFixture window;

    @Override
    protected void onSetUp() {
        application(Form.class).start();
        window = findFrame(new GenericTypeMatcher<>(Frame.class) {
            protected boolean isMatching(Frame frame) {
                return "Pathfinding visualization".equals(frame.getTitle()) && frame.isShowing();
            }
        }).using(robot());
        ComponentFinder finder = BasicComponentFinder.finderWithCurrentAwtHierarchy();
    }

    @Override
    protected void onTearDown() {
        window.cleanUp();
    }

    @Test
    public void algorithmSaved() {
        window.textBox("start").enterText("0 0");
        window.textBox("end").enterText("20 15");
        window.textBox("imeGrafaField").enterText("testGraph");
        window.button("saveGraphButton").click();

        assert DbConnection.getGraphByName("testGraph") != null;
    }

    @Test
    public void algorithmLoad() {
        window.comboBox("graphComboBox").selectItem("testGraph");
        window.button("loadGraphButton").click();
        window.textBox("start").requireText("0 0");
        window.textBox("end").requireText("20 15");
        window.textBox("imeGrafaField").requireText("testGraph");
    }
}
