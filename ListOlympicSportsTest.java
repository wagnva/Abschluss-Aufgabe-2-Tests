package external;

import static org.junit.Assert.*;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import static base.Start.start;

import base.TerminalMock;
import edu.kit.informatik.Main;
import edu.kit.informatik.Terminal;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.stream.Collectors;

/**
 * ListOlympicSportsTest
 *
 * @author Valentin Wagner
 *         24.02.18
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Terminal.class)
class ListOlympicSportsTest {

    private TerminalMock terminalMock;

    public ListOlympicSportsTest() {
    }

    @Before
    public void init() {
        this.terminalMock = new TerminalMock();
    }


    @Test
    public void testExampleFromPDF() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-olympic-sport curling;curling")
                .willReturn("add-olympic-sport eishockey;eishockey")
                .willReturn("add-olympic-sport biathlon;biathlon")
                .willReturn("add-olympic-sport eislauf;eisschnelllauf")
                .willReturn("add-olympic-sport bobsport;skeleton")
                .willReturn("add-olympic-sport eislauf;eiskunstlauf")
                .willReturn("add-olympic-sport bobsport;bob")
                .willReturn("list-olympic-sports")
                .willReturn("quit");

        start();

        for(int i = 0; i < 7; i++){
            assertThat(terminalMock.getResult().getResults().get(0)).isEqualTo("OK");
            terminalMock.getResult().getResults().remove(0);
        }

        String joinedString = terminalMock.getResult().getResults()
                .stream()
                .collect(Collectors.joining("\n"));

        assertThat(joinedString)
                .isEqualTo("biathlon biathlon\n" +
                        "bobsport bob\n" +
                        "bobsport skeleton\n" +
                        "curling curling\n" +
                        "eishockey eishockey\n" +
                        "eislauf eiskunstlauf\n" +
                        "eislauf eisschnelllauf");
    }

    @Test
    public void testEmpty() throws Exception {
        terminalMock.mock(true)
                .willReturn("list-olympic-sports")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isFalse();
        assertThat(terminalMock.getResult().getLast()).isNull();
    }

    @Test
    public void testOnlyWorksWhenLoggedIn() throws Exception {
        terminalMock.mock()
                .willReturn("list-olympic-sports")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }

    @Test
    public void testErrorWhenExtraSpace() throws Exception {
        terminalMock.mock()
                .willReturn("list-olympic-sports ")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }

    @Test
    public void testErrorWhenArgs() throws Exception {
        terminalMock.mock()
                .willReturn("list-olympic-sports jawg;sle")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }
}