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
 * ListSportsVenuesTest
 *
 * @author Valentin Wagner
 *         01.03.18
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Terminal.class)
class ListSportsVenuesTest {

    private TerminalMock terminalMock;

    public ListSportsVenuesTest() {
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
                .willReturn("list-sports-venues Deutschland")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isFalse();
        assertThat(terminalMock.getResult().getResults())
                .containsExactlyElementsIn(new String[]{
                    "OK", "OK", "(1 001 Karlsruhe 500)"
                });
    }

    @Test
    public void testEmpty() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 001;ger;Deutschland;1992")
                .willReturn("list-sports-venues Deutschland")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isFalse();
        assertThat(terminalMock.getResult().getResults())
                .containsExactlyElementsIn(new String[]{
                        "OK"
                });
    }

    @Test
    public void testSpecialCharsAndNumbersAllowed() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 001;ger;Deutschland56&%$#;1992")
                .willReturn("add-sports-venue 001;Deutschland56&%$#;Karlsruhe;Name;2000;500")
                .willReturn("list-sports-venues Deutschland56&%$#")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isFalse();
        assertThat(terminalMock.getResult().getResults())
                .containsExactlyElementsIn(new String[]{
                        "OK", "OK", "(1 001 Karlsruhe 500)"
                });
    }

    @Test
    public void testOnlyWorksWhenLoggedIn() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 001;ger;Deutschland;1992")
                .willReturn("add-sports-venue 001;Deutschland;Karlsruhe;Name;2000;500")
                .willReturn("logout-admin")
                .willReturn("list-sports-venues Deutschland")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
        assertThat(terminalMock.getResult().getResults())
                .containsExactlyElementsIn(new String[]{
                        "OK", "OK", "OK"
                });
    }

    @Test
    public void testNoCountryFound() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 001;ger;Deutschland;1992")
                .willReturn("add-sports-venue 001;Deutschland;Karlsruhe;Name;2000;500")
                .willReturn("list-sports-venues Frankreich")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
        assertThat(terminalMock.getResult().getResults())
                .containsExactlyElementsIn(new String[]{
                        "OK", "OK"
                });
    }

    @Test
    public void testExtraSpace() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 001;ger;Deutschland;1992")
                .willReturn("add-sports-venue 001;Deutschland;Karlsruhe;Name;2000;500")
                .willReturn("list-sports-venues Deutschland ")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
        assertThat(terminalMock.getResult().getResults())
                .containsExactlyElementsIn(new String[]{
                        "OK", "OK"
                });
    }

    @Test
    public void testCorrectSortingBasedOnSeats() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 001;ger;Deutschland;1992")
                .willReturn("add-sports-venue 001;Deutschland;Karlsruhe;Name;2000;800")
                .willReturn("add-sports-venue 002;Deutschland;Berlin;Stadium;2005;500")
                .willReturn("list-sports-venues Deutschland")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isFalse();

        for(int i = 0; i < 3; i++){
            assertThat(terminalMock.getResult().getResults().get(0)).isEqualTo("OK");
            terminalMock.getResult().getResults().remove(0);
        }

        assertThat(terminalMock.getResult().getResults().stream().collect(Collectors.joining("\n")))
                .isEqualTo("(1 002 Berlin 500)\n(2 001 Karlsruhe 800)");
    }

    @Test
    public void testCorrectSortingWhenSameAmountOfSeats() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 001;ger;Deutschland;1992")
                .willReturn("add-sports-venue 001;Deutschland;Karlsruhe;Name;2000;800")
                .willReturn("add-sports-venue 002;Deutschland;Berlin;Stadium;2005;500")
                .willReturn("add-sports-venue 003;Deutschland;Frankfurt;Stadium2;2007;500")
                .willReturn("list-sports-venues Deutschland")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isFalse();

        for(int i = 0; i < 4; i++){
            assertThat(terminalMock.getResult().getResults().get(0)).isEqualTo("OK");
            terminalMock.getResult().getResults().remove(0);
        }

        assertThat(terminalMock.getResult().getResults().stream().collect(Collectors.joining("\n")))
                .isEqualTo("(1 002 Berlin 500)\n(2 003 Frankfurt 500)\n(3 001 Karlsruhe 800)");
    }

}