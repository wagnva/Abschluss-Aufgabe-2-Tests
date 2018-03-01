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
 * AddCompetitionTest
 *
 * @author Valentin Wagner
 *         26.02.18
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Terminal.class)
class AddCompetitionTest {

    private TerminalMock terminalMock;

    public AddCompetitionTest() {
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
                .willReturn("add-competition 0001;2018;Deutschland;eishockey;eishockey;1;0;0")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isFalse();
        assertThat(terminalMock.getResult().getResults())
                .containsExactlyElementsIn(new String[]{"OK", "OK", "OK", "OK"}).inOrder();
    }

    @Test
    public void testNoMedalWorks() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 118;ger;Deutschland;1992")
                .willReturn("add-olympic-sport eishockey;eishockey")
                .willReturn("add-athlete 0001;Max;Mustermann;Deutschland;eishockey;eishockey")
                .willReturn("add-competition 0001;2018;Deutschland;eishockey;eishockey;0;0;0")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isFalse();
        assertThat(terminalMock.getResult().getResults())
                .containsExactlyElementsIn(new String[]{"OK", "OK", "OK", "OK"}).inOrder();
    }

    @Test
    public void testSpecialCharsAndNumbersAllowed() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 118;ger;Deutschland;1992")
                .willReturn("add-olympic-sport eishockey&%426;eishockey375&/§")
                .willReturn("add-athlete 0001;Max;Mustermann;Deutschland;eishockey&%426;eishockey375&/§")
                .willReturn("add-competition 0001;2018;Deutschland;eishockey&%426;eishockey375&/§;1;0;0")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isFalse();
        assertThat(terminalMock.getResult().getResults())
                .containsExactlyElementsIn(new String[]{"OK", "OK", "OK", "OK"}).inOrder();
    }

    @Test
    public void testOnlyWorksWhenLoggedIn() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 118;ger;Deutschland;1992")
                .willReturn("add-olympic-sport eishockey;eishockey")
                .willReturn("add-athlete 0001;Max;Mustermann;Deutschland;eishockey;eishockey")
                .willReturn("logout")
                .willReturn("add-competition 0001;2018;Deutschland;eishockey;eishockey;0;1;0")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();

        for(int i = 0; i < 3; i++){
            assertThat(terminalMock.getResult().getResults().get(i)).isEqualTo("OK");
        }
    }

    @Test
    public void testWrongAthleteID() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 118;ger;Deutschland;1992")
                .willReturn("add-olympic-sport eishockey;eishockey")
                .willReturn("add-athlete 0001;Max;Mustermann;Deutschland;eishockey;eishockey")
                .willReturn("add-competition 0002;2018;Deutschland;eishockey;eishockey;1;0;0")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();

        for(int i = 0; i < 3; i++){
            assertThat(terminalMock.getResult().getResults().get(i)).isEqualTo("OK");
        }
    }

    @Test
    public void testDifferentCountryThanAthleteCountryError() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 118;ger;Deutschland;1992")
                .willReturn("add-ioc-code 120;fra;Frankreich;1990")
                .willReturn("add-olympic-sport eishockey;eishockey")
                .willReturn("add-athlete 0001;Max;Mustermann;Deutschland;eishockey;eishockey")
                .willReturn("add-competition 0001;2018;Frankreich;eishockey;eishockey;1;0;0")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();

        for(int i = 0; i < 4; i++){
            assertThat(terminalMock.getResult().getResults().get(i)).isEqualTo("OK");
        }
    }

    @Test
    public void testWrongYearNotIn4YearRythm() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 118;ger;Deutschland;1992")
                .willReturn("add-olympic-sport eishockey;eishockey")
                .willReturn("add-athlete 0001;Max;Mustermann;Deutschland;eishockey;eishockey")
                .willReturn("add-competition 0001;2017;Deutschland;eishockey;eishockey;1;0;0")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();

        for(int i = 0; i < 3; i++){
            assertThat(terminalMock.getResult().getResults().get(i)).isEqualTo("OK");
        }
    }

    @Test
    public void testYearBefore1926() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 118;ger;Deutschland;1992")
                .willReturn("add-olympic-sport eishockey;eishockey")
                .willReturn("add-athlete 0001;Max;Mustermann;Deutschland;eishockey;eishockey")
                .willReturn("add-competition 0001;1922;Deutschland;eishockey;eishockey;0;0;1")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();

        for(int i = 0; i < 3; i++){
            assertThat(terminalMock.getResult().getResults().get(i)).isEqualTo("OK");
        }
    }

    @Test
    public void testYearAfter2018() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 118;ger;Deutschland;1992")
                .willReturn("add-olympic-sport eishockey;eishockey")
                .willReturn("add-athlete 0001;Max;Mustermann;Deutschland;eishockey;eishockey")
                .willReturn("add-competition 0001;2022;Deutschland;eishockey;eishockey;0;0;1")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();

        for(int i = 0; i < 3; i++){
            assertThat(terminalMock.getResult().getResults().get(i)).isEqualTo("OK");
        }
    }

    @Test
    public void testCountryDoesntExist() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 118;ger;Deutschland;1992")
                .willReturn("add-olympic-sport eishockey;eishockey")
                .willReturn("add-athlete 0001;Max;Mustermann;Deutschland;eishockey;eishockey")
                .willReturn("add-competition 0001;2018;USA;eishockey;eishockey;0;0;1")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();

        for(int i = 0; i < 3; i++){
            assertThat(terminalMock.getResult().getResults().get(i)).isEqualTo("OK");
        }
    }

    @Test
    public void testSportDoesntExist() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 118;ger;Deutschland;1992")
                .willReturn("add-olympic-sport eishockey;eishockey")
                .willReturn("add-athlete 0001;Max;Mustermann;Deutschland;eishockey;eishockey")
                .willReturn("add-competition 0001;2018;Deutschland;Schlittschuh;laufen;0;0;1")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();

        for(int i = 0; i < 3; i++){
            assertThat(terminalMock.getResult().getResults().get(i)).isEqualTo("OK");
        }
    }

    @Test
    public void testDisciplineDoesntExist() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 118;ger;Deutschland;1992")
                .willReturn("add-olympic-sport eishockey;eishockey")
                .willReturn("add-athlete 0001;Max;Mustermann;Deutschland;eishockey;eishockey")
                .willReturn("add-competition 0001;2018;Deutschland;eishockey;schlittschuh;0;0;1")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();

        for(int i = 0; i < 3; i++){
            assertThat(terminalMock.getResult().getResults().get(i)).isEqualTo("OK");
        }
    }

    @Test
    public void testAddCompetitionTwice() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 118;ger;Deutschland;1992")
                .willReturn("add-olympic-sport eishockey;eishockey")
                .willReturn("add-athlete 0001;Max;Mustermann;Deutschland;eishockey;eishockey")
                .willReturn("add-competition 0001;2018;Deutschland;eishockey;eishockey;0;0;1") //cannot have two medals in the same competition
                .willReturn("add-competition 0001;2018;Deutschland;eishockey;eishockey;0;1;0")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();

        for(int i = 0; i < 3; i++){
            assertThat(terminalMock.getResult().getResults().get(i)).isEqualTo("OK");
        }
    }

    @Test
    public void testWrongMedalParameter() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 118;ger;Deutschland;1992")
                .willReturn("add-olympic-sport eishockey;eishockey")
                .willReturn("add-athlete 0001;Max;Mustermann;Deutschland;eishockey;eishockey")
                .willReturn("add-competition 0001;2018;Deutschland;eishockey;eishockey;0;0;-1")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();

        for(int i = 0; i < 3; i++){
            assertThat(terminalMock.getResult().getResults().get(i)).isEqualTo("OK");
        }
    }

    @Test
    public void testTwoMedals() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 118;ger;Deutschland;1992")
                .willReturn("add-olympic-sport eishockey;eishockey")
                .willReturn("add-athlete 0001;Max;Mustermann;Deutschland;eishockey;eishockey")
                .willReturn("add-competition 0001;2018;Deutschland;eishockey;eishockey;0;1;1")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();

        for(int i = 0; i < 3; i++){
            assertThat(terminalMock.getResult().getResults().get(i)).isEqualTo("OK");
        }
    }

    @Test
    public void testThreeMedals() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 118;ger;Deutschland;1992")
                .willReturn("add-olympic-sport eishockey;eishockey")
                .willReturn("add-athlete 0001;Max;Mustermann;Deutschland;eishockey;eishockey")
                .willReturn("add-competition 0001;2018;Deutschland;eishockey;eishockey;1;1;1")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();

        for(int i = 0; i < 3; i++){
            assertThat(terminalMock.getResult().getResults().get(i)).isEqualTo("OK");
        }
    }

    @Test
    public void testSpaceAtTheEnd() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 118;ger;Deutschland;1992")
                .willReturn("add-olympic-sport eishockey;eishockey")
                .willReturn("add-athlete 0001;Max;Mustermann;Deutschland;eishockey;eishockey")
                .willReturn("add-competition 0001;2018;Deutschland;eishockey;eishockey;0;0;0 ")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();

        for(int i = 0; i < 3; i++){
            assertThat(terminalMock.getResult().getResults().get(i)).isEqualTo("OK");
        }
    }

    @Test
    public void testTooManyArgs() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 118;ger;Deutschland;1992")
                .willReturn("add-olympic-sport eishockey;eishockey")
                .willReturn("add-athlete 0001;Max;Mustermann;Deutschland;eishockey;eishockey")
                .willReturn("add-competition 0001;2018;Deutschland;eishockey;eishockey;0;0;0;wuuu")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();

        for(int i = 0; i < 3; i++){
            assertThat(terminalMock.getResult().getResults().get(i)).isEqualTo("OK");
        }
    }

    @Test
    public void testTwoGoldMedalsInTheSameYearError() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 118;ger;Deutschland;1992")
                .willReturn("add-olympic-sport eishockey;eishockey")
                .willReturn("add-athlete 0001;Max;Mustermann;Deutschland;eishockey;eishockey")
                .willReturn("add-athlete 0002;Max;Mustermann;Deutschland;eishockey;eishockey")
                .willReturn("add-competition 0001;2018;Deutschland;eishockey;eishockey;1;0;0")
                .willReturn("add-competition 0002;2018;Deutschland;eishockey;eishockey;1;0;0")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();

        for(int i = 0; i < 5; i++){
            assertThat(terminalMock.getResult().getResults().get(i)).isEqualTo("OK");
        }
    }

    @Test
    public void testTwoMedalsInDifferentYearsWorks() throws Exception {
        terminalMock.mock(true)
                .willReturn("add-ioc-code 118;ger;Deutschland;1992")
                .willReturn("add-olympic-sport eishockey;eishockey")
                .willReturn("add-athlete 0001;Max;Mustermann;Deutschland;eishockey;eishockey")
                .willReturn("add-competition 0001;2018;Deutschland;eishockey;eishockey;1;0;0")
                .willReturn("add-competition 0001;2014;Deutschland;eishockey;eishockey;1;0;0")
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isFalse();

        for(int i = 0; i < 5; i++){
            assertThat(terminalMock.getResult().getResults().get(i)).isEqualTo("OK");
        }
    }
}