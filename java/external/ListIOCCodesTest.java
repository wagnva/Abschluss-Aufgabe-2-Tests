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
 * ListIOCCodesTest
 *
 * @author Valentin Wagner
 *         24.02.18
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Terminal.class)
class ListIOCCodesTest {

    private TerminalMock terminalMock;

    public ListIOCCodesTest() {
    }

    @Before
    public void init() {
        this.terminalMock = new TerminalMock();
    }


    @Test
    public void testExampleFromPDF() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 111;arg;argentinien;1920")
                .willReturn("add-ioc-code 112;bhu;bhutan;1984")
                .willReturn("add-ioc-code 113;bul;bulgarien;1896")
                .willReturn("add-ioc-code 114;chi;chile;1896")
                .willReturn("add-ioc-code 115;cze;tschechien;1992")
                .willReturn("add-ioc-code 116;ecu;ecuador;1924")
                .willReturn("add-ioc-code 117;esp;spanien;1900")
                .willReturn("add-ioc-code 118;ger;deutschland;1992")
                .willReturn("add-ioc-code 119;can;kanada;1900")
                .willReturn("list-ioc-codes")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isFalse();

        for(int i = 0; i < 9; i++){
            assertThat(terminalMock.getResult().getResults().get(0)).isEqualTo("OK");
            terminalMock.getResult().getResults().remove(0);
        }

        String joinedString = terminalMock.getResult().getResults()
                .stream()
                .collect(Collectors.joining("\n"));

        assertThat(joinedString)
                .isEqualTo("1896 113 bul bulgarien\n" +
                        "1896 114 chi chile\n" +
                        "1900 117 esp spanien\n" +
                        "1900 119 can kanada\n" +
                        "1920 111 arg argentinien\n" +
                        "1924 116 ecu ecuador\n" +
                        "1984 112 bhu bhutan\n" +
                        "1992 115 cze tschechien\n" +
                        "1992 118 ger deutschland");
    }

    @Test
    public void testWithArgsError() throws Exception{
        terminalMock.mock(true)
                .willReturn("list-ioc-codes ")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }

    @Test
    public void testOnlyWorksWhenLoggedIn() throws Exception{
        terminalMock.mock()
                .willReturn("list-ioc-codes")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }
}