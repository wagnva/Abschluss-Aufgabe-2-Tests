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
 * AddSportsVenueTest
 *
 * @author Valentin Wagner
 *         24.02.18
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Terminal.class)
class AddSportsVenueTest {

    private TerminalMock terminalMock;

    public AddSportsVenueTest() {
    }

    @Before
    public void init() {
        this.terminalMock = new TerminalMock();
    }

    @Test
    public void testItWorks() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 001;ger;Deutschland;1992")
                .willReturn("add-sports-venue 001;Deutschland;Karlsruhe;Name;2000;500")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isFalse();
        assertThat(terminalMock.getResult().getLast()).isEqualTo("OK");
    }

    @Test
    public void testSpecialCharsAndNumbersAllowed() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 001;ger;Deutschland;1992")
                .willReturn("add-sports-venue 001;Deutschland;Karlsruhe6%&$7;Name745&%$;2000;500")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isFalse();
        assertThat(terminalMock.getResult().getLast()).isEqualTo("OK");
    }

    @Test
    public void testErrorIfIOCCodeDoesntExist() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-sports-venue 001;Deutschland;Karlsruhe;Name;2000;500")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }

    @Test
    public void test000ID() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 001;ger;Deutschland;1992")
                .willReturn("add-sports-venue 000;Deutschland;Karlsruhe;Name;2000;500") //000 id not allowed
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }

    @Test
    public void testMissingPlace() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 001;ger;Deutschland;1992")
                .willReturn("add-sports-venue 001;Deutschland;;Name;2000;500") //no place
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }

    @Test
    public void testsMissingName() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 001;ger;Deutschland;1992")
                .willReturn("add-sports-venue 001;Deutschland;Karlsruhe;;2000;500") //no name
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }

    @Test
    public void testsTooLongYear() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 001;ger;Deutschland;1992")
                .willReturn("add-sports-venue 001;Deutschland;Karlsruhe;Name;20009;500") //too long year
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }

    @Test
    public void testsNegativeSeatNumber() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 001;ger;Deutschland;1992")
                .willReturn("add-sports-venue 001;Deutschland;Karlsruhe;Name;2000;-1") //negative seat number
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }
}