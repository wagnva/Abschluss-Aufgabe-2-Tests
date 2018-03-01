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
 * SummaryAthletesTest
 *
 * @author Valentin Wagner
 *         26.02.18
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Terminal.class)
class SummaryAthletesTest {

    private TerminalMock terminalMock;

    public SummaryAthletesTest() {
    }

    @Before
    public void init() {
        this.terminalMock = new TerminalMock();
    }


    @Test
    public void testItWorks() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 118;ger;Deutschland;1992")
                .willReturn("add-olympic-sport eishockey;eishockey")
                .willReturn("add-athlete 0001;Max;Mustermann;Deutschland;eishockey;eishockey")
                .willReturn("add-athlete 0002;Hans;Mustermann;Deutschland;eishockey;eishockey")
                .willReturn("add-athlete 0003;Dieter;Mustermann;Deutschland;eishockey;eishockey")
                .willReturn("add-competition 0001;2018;Deutschland;eishockey;eishockey;1;0;0")
                .willReturn("add-competition 0001;2014;Deutschland;eishockey;eishockey;1;0;0")
                .willReturn("add-competition 0001;2010;Deutschland;eishockey;eishockey;1;0;0")
                .willReturn("add-competition 0003;2018;Deutschland;eishockey;eishockey;0;0;1")
                .willReturn("add-competition 0003;2014;Deutschland;eishockey;eishockey;0;0;1")
                .willReturn("add-competition 0002;2018;Deutschland;eishockey;eishockey;0;1;0")
                .willReturn("add-competition 0002;2014;Deutschland;eishockey;eishockey;0;1;0")
                .willReturn("summary-athletes eishockey")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isFalse();

        assertThat(terminalMock.getResult().getResults()
                .stream()
                .collect(Collectors.joining("\n")))
                .isEqualTo("OK\n" +
                        "OK\n" +
                        "OK\n" +
                        "OK\n" +
                        "OK\n" +
                        "OK\n" +
                        "OK\n" +
                        "OK\n" +
                        "OK\n" +
                        "OK\n" +
                        "OK\n" +
                        "OK\n" +
                        "0001 Max Mustermann 3\n" +
                        "0002 Hans Mustermann 2\n" +
                        "0003 Dieter Mustermann 2");
    }

    @Test
    public void testSpecialCharsAndNumbersAllowed() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 118;ger;Deutschland;1992")
                .willReturn("add-olympic-sport eishockey57$&#;eishockey57$&#")
                .willReturn("add-athlete 0001;Max;Mustermann;Deutschland;eishockey57$&#;eishockey57$&#")
                .willReturn("add-competition 0001;2014;Deutschland;eishockey57$&#;eishockey57$&#;0;1;0")
                .willReturn("summary-athletes eishockey57$&#")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isFalse();

        assertThat(terminalMock.getResult().getResults()
                .stream()
                .collect(Collectors.joining("\n")))
                .isEqualTo("OK\n" +
                        "OK\n" +
                        "OK\n" +
                        "OK\n" +
                        "0001 Max Mustermann 1"
                );
    }

    @Test
    public void testErrorWhenDisciplineDoesntExist() throws Exception{
        terminalMock.mock(true)
                .willReturn("summary-athletes eishockey")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }

    @Test
    public void testOnlyWorksWhenLoggedIn() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 118;ger;Deutschland;1992")
                .willReturn("add-olympic-sport eishockey;eishockey")
                .willReturn("add-athlete 0001;Max;Mustermann;Deutschland;eishockey;eishockey")
                .willReturn("logout-admin")
                .willReturn("summary-athletes eishockey")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();

        assertThat(terminalMock.getResult().getResults()
                .stream()
                .collect(Collectors.joining("\n")))
                .isEqualTo("OK\n" +
                        "OK\n" +
                        "OK\n" +
                        "OK"
                );
    }

    @Test
    public void testErrorWithExtraSpace() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 118;ger;Deutschland;1992")
                .willReturn("add-olympic-sport eishockey;eishockey")
                .willReturn("add-athlete 0001;Max;Mustermann;Deutschland;eishockey;eishockey")
                .willReturn("summary-athletes eishockey ")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();

        assertThat(terminalMock.getResult().getResults()
                .stream()
                .collect(Collectors.joining("\n")))
                .isEqualTo("OK\n" +
                        "OK\n" +
                        "OK"
                );
    }

}