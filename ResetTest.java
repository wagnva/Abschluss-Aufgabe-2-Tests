package external;

import static com.google.common.truth.Truth.assertThat;
import static base.Start.start;

import base.TerminalMock;
import edu.kit.informatik.Terminal;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * ResetTest
 *
 * @author Valentin Wagner
 *         01.03.18
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Terminal.class)
class ResetTest {

    private TerminalMock terminalMock;

    public ResetTest() {
    }

    @Before
    public void init() {
        this.terminalMock = new TerminalMock();
    }


    @Test
    public void testItWorks() throws Exception {
        terminalMock.mock(true)
                .willReturn("reset")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isFalse();
        assertThat(terminalMock.getResult().getLast()).isEqualTo("OK");
    }

    @Test
    public void testOnlyWorksWhenLoggedIn() throws Exception {
        terminalMock.mock()
                .willReturn("reset")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }

    @Test
    public void testErrorWithExtraSpace() throws Exception {
        terminalMock.mock(true)
                .willReturn("reset ")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }

    @Test
    public void testAdminAccountStillExists() throws Exception {
        terminalMock.mock(true)
                .willReturn("reset")
                .willReturn("logout-admin")
                .willReturn("login-admin hans;password123")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isFalse();
        assertThat(terminalMock.getResult().getResults())
                .containsExactlyElementsIn(new String[]{
                    "OK", "OK", "OK"
                });
    }

    @Test
    public void testNoCountriesExistAnymore() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 111;arg;argentinien;1920")
                .willReturn("reset")
                .willReturn("list-ioc-codes")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isFalse();
        assertThat(terminalMock.getResult().getResults())
                .containsExactlyElementsIn(new String[]{
                        "OK", "OK"
                });
    }

    @Test
    public void testNoSportsExistAnymore() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-olympic-sport curling;curling")
                .willReturn("reset")
                .willReturn("list-olympic-sports")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isFalse();
        assertThat(terminalMock.getResult().getResults())
                .containsExactlyElementsIn(new String[]{
                        "OK", "OK"
                });
    }

    @Test
    public void testOlympicMedalTableEmpty() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 118;ger;germany;1992")
                .willReturn("reset")
                .willReturn("olympic-medal-table")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isFalse();
        assertThat(terminalMock.getResult().getResults())
                .containsExactlyElementsIn(new String[]{
                        "OK", "OK"
                });
    }

    @Test
    public void testNoSportVenuesExistAnymore() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 001;ger;Deutschland;1992")
                .willReturn("add-sports-venue 001;Deutschland;Karlsruhe;Name;2000;500")
                .willReturn("reset")
                .willReturn("add-ioc-code 001;ger;Deutschland;1992")
                .willReturn("list-sports-venues Deutschland")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isFalse();
        assertThat(terminalMock.getResult().getResults())
                .containsExactlyElementsIn(new String[]{
                        "OK", "OK", "OK", "OK"
                });
    }

    @Test
    public void testNoAthletesExistAnymore() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 118;ger;germany;1992")
                .willReturn("add-olympic-sport bob;bob")
                .willReturn("add-athlete 0001;max;mustermann;germany;bob;bob")
                .willReturn("reset")
                .willReturn("add-ioc-code 118;ger;germany;1992")
                .willReturn("add-olympic-sport bob;bob")
                .willReturn("summary-athletes bob;bob")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isFalse();
        assertThat(terminalMock.getResult().getResults())
                .containsExactlyElementsIn(new String[]{
                        "OK", "OK", "OK", "OK", "OK", "OK"
                });
    }
}

