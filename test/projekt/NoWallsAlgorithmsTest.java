package projekt;

import org.assertj.swing.core.BasicComponentFinder;
import org.assertj.swing.core.ComponentFinder;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.assertj.swing.timing.Condition;
import org.junit.Test;
import src.main.java.Form;
import src.main.java.Grid;

import java.awt.*;

import static org.assertj.swing.edt.GuiActionRunner.execute;
import static org.assertj.swing.finder.WindowFinder.findFrame;
import static org.assertj.swing.launcher.ApplicationLauncher.application;
import static org.assertj.swing.timing.Pause.pause;
import static org.assertj.swing.timing.Timeout.timeout;

public class NoWallsAlgorithmsTest extends AssertJSwingJUnitTestCase {
    private FrameFixture window;
    private Grid grid;

    @Override
    protected void onSetUp() {
        application(Form.class).start();
        window = findFrame(new GenericTypeMatcher<>(Frame.class) {
            protected boolean isMatching(Frame frame) {
                return "Pathfinding visualization".equals(frame.getTitle()) && frame.isShowing();
            }
        }).using(robot());
        ComponentFinder finder = BasicComponentFinder.finderWithCurrentAwtHierarchy();
        grid = (Grid) finder.findByName("grid", true);
    }

    @Override
    protected void onTearDown() {
        window.cleanUp();
    }

    @Test
    public void pathFindAlgorithmFoundEnd() {
        window.textBox("start").enterText("0 0");
        window.textBox("end").enterText("10 10");
        window.button("startButton").click();

        pause(new Condition("Algorithm finished") {
            public boolean test() {
                return execute(grid::isFound);
            }
        }, timeout(10000));
    }

    @Test
    public void BFSAlgorithmFoundEnd() {
        window.comboBox("algorithmComboBox").selectItem("BFS");
        window.textBox("start").enterText("0 0");
        window.textBox("end").enterText("8 8");
        window.button("startButton").click();

        pause(new Condition("Algorithm finished") {
            public boolean test() {
                return execute(grid::isFound);
            }
        }, timeout(12000));
    }

    @Test
    public void DFSAlgorithmFoundEnd() {
        window.comboBox("algorithmComboBox").selectItem("DFS");
        window.textBox("start").enterText("0 0");
        window.textBox("end").enterText("79 8");
        window.button("startButton").click();

        pause(new Condition("Algorithm finished") {
            public boolean test() {
                return execute(grid::isFound);
            }
        }, timeout(12000));
    }

    @Test
    public void aStarAlgorithmFoundEnd() {
        window.comboBox("algorithmComboBox").selectItem("A*");
        window.textBox("start").enterText("0 0");
        window.textBox("end").enterText("10 10");
        window.button("startButton").click();

        pause(new Condition("Algorithm finished") {
            public boolean test() {
                return execute(grid::isFound);
            }
        }, timeout(10000));
    }
}
