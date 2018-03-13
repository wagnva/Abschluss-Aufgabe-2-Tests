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
 * MedalTableTest
 *
 * @author Valentin Wagner
 *         01.03.18
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Terminal.class)
class MedalTableTest {

    private TerminalMock terminalMock;

    public MedalTableTest() {
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
                .willReturn("add-olympic-sport curling;curling")
                .willReturn("add-ioc-code 005;fra;Frankreich;1995")
                .willReturn("add-ioc-code 003;ita;Italien;1992")
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
                .willReturn("add-athlete 0004;Max;Frankreichmann;Frankreich;eishockey;eishockey")
                .willReturn("add-athlete 0005;Hans;Frankreichmann;Frankreich;eishockey;eishockey")
                .willReturn("add-athlete 0006;Dieter;Frankreichmann;Frankreich;eishockey;eishockey")
                .willReturn("add-competition 0004;2010;Frankreich;eishockey;eishockey;0;0;1")
                .willReturn("add-competition 0004;2006;Frankreich;eishockey;eishockey;1;0;0")
                .willReturn("add-competition 0006;2002;Frankreich;eishockey;eishockey;0;0;1")
                .willReturn("add-competition 0005;1998;Frankreich;eishockey;eishockey;0;1;0")
                .willReturn("add-athlete 0007;Max;Italiener;Italien;curling;curling")
                .willReturn("add-athlete 0008;Hans;Italiener;Italien;curling;curling")
                .willReturn("add-athlete 0009;Dieter;Italiener;Italien;curling;curling")
                .willReturn("add-competition 0007;2010;Italien;curling;curling;0;0;1")
                .willReturn("add-competition 0007;2006;Italien;curling;curling;1;0;0")
                .willReturn("add-competition 0008;2002;Italien;curling;curling;0;0;1")
                .willReturn("add-competition 0009;1998;Italien;curling;curling;0;1;0")
                .willReturn("olympic-medal-table")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isFalse();

        System.out.println(terminalMock.getResult().getResults()
                .stream()
                .collect(Collectors.joining("\n")));

        for(int i = 0; i < 29; i++){
            assertThat(terminalMock.getResult().getResults().get(0)).isEqualTo("OK");
            terminalMock.getResult().getResults().remove(0);
        }

        assertThat(terminalMock.getResult().getResults()
                .stream()
                .collect(Collectors.joining("\n")))
                .isEqualTo(
                        "(1,118,ger,Deutschland,3,2,2,7)\n" +
                        "(2,003,ita,Italien,1,1,2,4)\n" +
                        "(3,005,fra,Frankreich,1,1,2,4)");
    }

    @Test
    public void testSortingByGoldMedals() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 118;ger;germany;1992")
                .willReturn("add-ioc-code 119;ita;italien;1992")
                .willReturn("add-ioc-code 001;spa;spanien;1992")
                .willReturn("add-ioc-code 005;usa;usa;1992")
                .willReturn("add-olympic-sport bob;bob")
                .willReturn("add-athlete 0001;ger;mustermann;germany;bob;bob")
                .willReturn("add-athlete 0002;ita;mustermann;italien;bob;bob")
                .willReturn("add-athlete 0003;spa;mustermann;spanien;bob;bob")
                .willReturn("add-athlete 0004;usa;mustermann;usa;bob;bob")
                .willReturn("add-competition 0001;2018;germany;bob;bob;1;0;0")
                .willReturn("add-competition 0002;2018;italien;bob;bob;1;0;0")
                .willReturn("add-competition 0003;2018;spanien;bob;bob;1;0;0")
                .willReturn("add-competition 0004;2018;usa;bob;bob;1;0;0")
                .willReturn("add-competition 0004;2010;usa;bob;bob;1;0;0")
                .willReturn("add-competition 0001;2014;germany;bob;bob;1;0;0")
                .willReturn("add-competition 0001;2010;germany;bob;bob;1;0;0")
                .willReturn("olympic-medal-table")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isFalse();

        System.out.println(terminalMock.getResult().getResults()
                .stream()
                .collect(Collectors.joining("\n")));

        for(int i = 0; i < 16; i++){
            assertThat(terminalMock.getResult().getResults().get(0)).isEqualTo("OK");
            terminalMock.getResult().getResults().remove(0);
        }

        assertThat(terminalMock.getResult().getResults()
                .stream()
                .collect(Collectors.joining("\n")))
                .isEqualTo(
                        "(1,118,ger,germany,3,0,0,3)\n" +
                                "(2,005,usa,usa,2,0,0,2)\n" +
                                "(3,001,spa,spanien,1,0,0,1)\n" +
                                "(4,119,ita,italien,1,0,0,1)");
    }

    @Test
    public void testSortingBySilverMedals() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 118;ger;germany;1992")
                .willReturn("add-ioc-code 119;ita;italien;1992")
                .willReturn("add-ioc-code 001;spa;spanien;1992")
                .willReturn("add-ioc-code 005;usa;usa;1992")
                .willReturn("add-olympic-sport bob;bob")
                .willReturn("add-athlete 0001;ger;mustermann;germany;bob;bob")
                .willReturn("add-athlete 0002;ita;mustermann;italien;bob;bob")
                .willReturn("add-athlete 0003;spa;mustermann;spanien;bob;bob")
                .willReturn("add-athlete 0004;usa;mustermann;usa;bob;bob")
                .willReturn("add-competition 0001;2018;germany;bob;bob;0;1;0")
                .willReturn("add-competition 0002;2018;italien;bob;bob;0;1;0")
                .willReturn("add-competition 0003;2018;spanien;bob;bob;0;1;0")
                .willReturn("add-competition 0004;2018;usa;bob;bob;0;1;0")
                .willReturn("add-competition 0004;2010;usa;bob;bob;0;1;0")
                .willReturn("add-competition 0001;2014;germany;bob;bob;0;1;0")
                .willReturn("add-competition 0001;2010;germany;bob;bob;0;1;0")
                .willReturn("olympic-medal-table")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isFalse();

        System.out.println(terminalMock.getResult().getResults()
                .stream()
                .collect(Collectors.joining("\n")));

        for(int i = 0; i < 16; i++){
            assertThat(terminalMock.getResult().getResults().get(0)).isEqualTo("OK");
            terminalMock.getResult().getResults().remove(0);
        }

        assertThat(terminalMock.getResult().getResults()
                .stream()
                .collect(Collectors.joining("\n")))
                .isEqualTo(
                        "(1,118,ger,germany,0,3,0,3)\n" +
                                "(2,005,usa,usa,0,2,0,2)\n" +
                                "(3,001,spa,spanien,0,1,0,1)\n" +
                                "(4,119,ita,italien,0,1,0,1)");
    }

    @Test
    public void testSortingByBronzeMedals() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 118;ger;germany;1992")
                .willReturn("add-ioc-code 119;ita;italien;1992")
                .willReturn("add-ioc-code 001;spa;spanien;1992")
                .willReturn("add-ioc-code 005;usa;usa;1992")
                .willReturn("add-olympic-sport bob;bob")
                .willReturn("add-athlete 0001;ger;mustermann;germany;bob;bob")
                .willReturn("add-athlete 0002;ita;mustermann;italien;bob;bob")
                .willReturn("add-athlete 0003;spa;mustermann;spanien;bob;bob")
                .willReturn("add-athlete 0004;usa;mustermann;usa;bob;bob")
                .willReturn("add-competition 0001;2018;germany;bob;bob;0;0;1")
                .willReturn("add-competition 0002;2018;italien;bob;bob;0;0;1")
                .willReturn("add-competition 0003;2018;spanien;bob;bob;0;0;1")
                .willReturn("add-competition 0004;2018;usa;bob;bob;0;0;1")
                .willReturn("add-competition 0004;2010;usa;bob;bob;0;0;1")
                .willReturn("add-competition 0001;2014;germany;bob;bob;0;0;1")
                .willReturn("add-competition 0001;2010;germany;bob;bob;0;0;1")
                .willReturn("olympic-medal-table")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isFalse();

        System.out.println(terminalMock.getResult().getResults()
                .stream()
                .collect(Collectors.joining("\n")));

        for(int i = 0; i < 16; i++){
            assertThat(terminalMock.getResult().getResults().get(0)).isEqualTo("OK");
            terminalMock.getResult().getResults().remove(0);
        }

        assertThat(terminalMock.getResult().getResults()
                .stream()
                .collect(Collectors.joining("\n")))
                .isEqualTo(
                        "(1,118,ger,germany,0,0,3,3)\n" +
                                "(2,005,usa,usa,0,0,2,2)\n" +
                                "(3,001,spa,spanien,0,0,1,1)\n" +
                                "(4,119,ita,italien,0,0,1,1)");
    }

    @Test
    public void testSomeSorting() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 118;ger;germany;1992")
                .willReturn("add-ioc-code 119;ita;italien;1992")
                .willReturn("add-ioc-code 001;spa;spanien;1992")
                .willReturn("add-ioc-code 005;usa;usa;1992")
                .willReturn("add-ioc-code 006;fra;frankreich;1992")
                .willReturn("add-ioc-code 008;pol;polen;1992")
                .willReturn("add-ioc-code 007;eng;england;1992")
                .willReturn("add-olympic-sport bob;bob")
                .willReturn("add-athlete 0001;ger;mustermann;germany;bob;bob")
                .willReturn("add-athlete 0007;fra;mustermann;frankreich;bob;bob")
                .willReturn("add-athlete 0002;ita;mustermann;italien;bob;bob")
                .willReturn("add-athlete 0003;spa;mustermann;spanien;bob;bob")
                .willReturn("add-athlete 0004;usa;mustermann;usa;bob;bob")
                .willReturn("add-athlete 0006;pol;mustermann;polen;bob;bob")
                .willReturn("add-athlete 0005;eng;mustermann;england;bob;bob")
                .willReturn("add-competition 0001;2018;germany;bob;bob;1;0;0")
                .willReturn("add-competition 0002;2018;italien;bob;bob;0;0;1")
                .willReturn("add-competition 0003;2018;spanien;bob;bob;0;0;1")
                .willReturn("add-competition 0004;2018;usa;bob;bob;0;0;1")
                .willReturn("add-competition 0005;2018;england;bob;bob;0;0;1")
                .willReturn("add-competition 0006;2018;polen;bob;bob;0;0;1")
                .willReturn("add-competition 0007;2018;frankreich;bob;bob;0;0;1")
                .willReturn("add-competition 0001;2014;germany;bob;bob;1;0;0")
                .willReturn("add-competition 0004;2014;usa;bob;bob;0;0;1")
                .willReturn("add-competition 0007;2014;frankreich;bob;bob;0;1;0")
                .willReturn("add-competition 0002;2014;italien;bob;bob;1;0;0")
                .willReturn("add-competition 0003;2014;spanien;bob;bob;1;0;0")
                .willReturn("add-competition 0001;2010;germany;bob;bob;1;0;0")
                .willReturn("add-competition 0004;2010;usa;bob;bob;1;0;0")
                .willReturn("add-competition 0007;2010;frankreich;bob;bob;0;1;0")
                .willReturn("add-competition 0003;2010;spanien;bob;bob;1;0;0")
                .willReturn("add-competition 0004;2006;usa;bob;bob;1;0;0")
                .willReturn("add-competition 0007;2006;frankreich;bob;bob;1;0;0")
                .willReturn("olympic-medal-table")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isFalse();

        System.out.println(terminalMock.getResult().getResults()
                .stream()
                .collect(Collectors.joining("\n")));

        for(int i = 0; i < 33; i++){
            assertThat(terminalMock.getResult().getResults().get(0)).isEqualTo("OK");
            terminalMock.getResult().getResults().remove(0);
        }

        assertThat(terminalMock.getResult().getResults()
                .stream()
                .collect(Collectors.joining("\n")))
                .isEqualTo(
                        "(1,118,ger,germany,3,0,0,3)\n" +
                                "(2,005,usa,usa,2,0,2,4)\n" +
                                "(3,001,spa,spanien,2,0,1,3)\n" +
                                "(4,006,fra,frankreich,1,2,1,4)\n" +
                                "(5,119,ita,italien,1,0,1,2)\n" +
                                "(6,007,eng,england,0,0,1,1)\n" +
                                "(7,008,pol,polen,0,0,1,1)");
    }

    @Test
    public void testNoOutputWhenNoContestants() throws Exception{
        terminalMock.mock(true)
                .willReturn("olympic-medal-table")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isFalse();
        assertThat(terminalMock.getResult().getLast()).isNull();
    }

    @Test
    public void testOnlyWorkWhenLoggedIn() throws Exception{
        terminalMock.mock(false)
                .willReturn("olympic-medal-table")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }

    @Test
    public void testExtraSpace() throws Exception{
        terminalMock.mock(true)
                .willReturn("olympic-medal-table ")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }
}