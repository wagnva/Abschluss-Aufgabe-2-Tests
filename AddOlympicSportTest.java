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

/**
 * AddOlympicSportTest
 *
 * @author Valentin Wagner
 *         24.02.18
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Terminal.class)
class AddOlympicSportTest {

    private TerminalMock terminalMock;

    public AddOlympicSportTest() {
    }

    @Before
    public void init() {
        this.terminalMock = new TerminalMock();
    }


    @Test
    public void testItWorks() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-olympic-sport Eis;hockey")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isFalse();
        assertThat(terminalMock.getResult().getLast()).isEqualTo("OK");
    }

    @Test
    public void testSpecialCharsAndNumbersAllowed() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-olympic-sport Eis56%&$;hockey7%9$")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isFalse();
        assertThat(terminalMock.getResult().getLast()).isEqualTo("OK");
    }

    @Test
    public void testMultipleDisciplinesForSameSport() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-olympic-sport Eis;hockey")
                .willReturn("add-olympic-sport Eis;irwas")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isFalse();
        assertThat(terminalMock.getResult().getLast()).isEqualTo("OK");
    }

    @Test
    public void testSameDisciplineForDifferentSports() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-olympic-sport Eis;hockey")
                .willReturn("add-olympic-sport Irwas;hockey")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isFalse();
        assertThat(terminalMock.getResult().getLast()).isEqualTo("OK");
    }

    @Test
    public void testOnlyWorksWhenLoggedIn() throws Exception {
        terminalMock.mock()
                .willReturn("add-olympic-sport Eis;hockey")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }

    @Test
    public void testCannotAddSameDisciplineTwice() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-olympic-sport Eis;hockey")
                .willReturn("add-olympic-sport Eis;hockey")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }

}