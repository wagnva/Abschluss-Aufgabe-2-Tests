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
 * AddIOCCodeTest
 *
 * @author Valentin Wagner
 *         24.02.18
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Terminal.class)
class AddIOCCodeTest {

    private TerminalMock terminalMock;

    public AddIOCCodeTest() {
    }

    @Before
    public void init() {
        this.terminalMock = new TerminalMock();
    }


    @Test
    public void testWithWorkingArgs() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 001;ger;Deutschland;1992") //should work
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isFalse();
        assertThat(terminalMock.getResult().getLast()).isEqualTo("OK");
    }

    @Test
    public void testSpecialCharsAndNumbersAllowed() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 001;ger;Deutschland8%&$;1992") //should work
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isFalse();
        assertThat(terminalMock.getResult().getLast()).isEqualTo("OK");
    }

    @Test
    public void testOnlyWorksWhenLoggedIn() throws Exception {
        terminalMock.mock(false)
                .willReturn("add-ioc-code 001;ger;Deutschland;1992") //need to be logged in for it to work
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }

    @Test
    public void testWithTooLongID() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 0011;ger;Deutschland;1992") //too long id
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }

    @Test
    public void testWith0000ID() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 000;ger;Deutschland;1992") //000 not allowed as id
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }

    @Test
    public void testWithIOCCodeUppercase() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 001;Ger;Deutschland;1992") //uppercase not allowed
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }

    @Test
    public void testWithIOCCodeTooLong() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 001;germ;Deutschland;1992") //ioc code only 3 chars allowed
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }

    @Test
    public void testWithoutName() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 001;ger;;1992") //name has to exist
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }

    @Test
    public void testTooLongYear() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 001;ger;Deutschland;19922") //too long year
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }

    @Test
    public void testTwoCountriesWithSameID() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 001;ger;Deutschland;1992")
                .willReturn("add-ioc-code 001;fra;Frankreich;1995")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }

    @Test
    public void testTwoCountriesWithSameCode() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 001;ger;Deutschland;1992")
                .willReturn("add-ioc-code 002;ger;Frankreich;1995")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }

    @Test
    public void testTwoCountriesWithSameName() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 001;ger;Deutschland;1992")
                .willReturn("add-ioc-code 002;fra;Deutschland;1995")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }
}