package projekt;

import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

import java.awt.*;

import static org.assertj.swing.finder.WindowFinder.findFrame;
import static org.assertj.swing.launcher.ApplicationLauncher.application;

public class ComponentVisibilityTest extends AssertJSwingJUnitTestCase {
    private FrameFixture window;

    @Override
    protected void onSetUp() {
        application(Form.class).start();
        window = findFrame(new GenericTypeMatcher<>(Frame.class) {
            protected boolean isMatching(Frame frame) {
                return "Pathfinding visualization".equals(frame.getTitle()) && frame.isShowing();
            }
        }).using(robot());
    }

    @Test
    public void allComponentsAreVisible() {
        assert window.panel("grid").requireVisible() != null;
        assert window.comboBox("algorithmComboBox").requireVisible() != null;
        assert window.textBox("start").requireVisible() != null;
        assert window.textBox("end").requireVisible() != null;
        assert window.panel("menu").requireVisible() != null;
        assert window.radioButton("izvrednjavanjeSvihButton").requireVisible() != null;
        assert window.comboBox("graphComboBox").requireVisible() != null;
        assert window.toolBar("toolBar").requireVisible() != null;
        assert window.button("startButton").requireVisible() != null;
        assert window.button("newButton").requireVisible() != null;
        assert window.button("clearButton").requireVisible() != null;
        assert window.button("saveGraphButton").requireVisible() != null;
        assert window.button("loadGraphButton").requireVisible() != null;
        assert window.radioButton("vizualizacijaButton").requireVisible() != null;
    }
}
