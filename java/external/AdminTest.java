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
 * AdminTest
 *
 * @author Valentin Wagner
 *         24.02.18
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Terminal.class)
class AdminTest {

    private TerminalMock terminalMock;

    public AdminTest() {
    }

    @Before
    public void init() {
        this.terminalMock = new TerminalMock();
    }


    @Test
    public void testCreateAdminWorks() throws Exception {
        terminalMock.mock()
                .willReturn("add-admin hans;maier;hans;hahahaha") //should work
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isFalse();
        assertThat(terminalMock.getResult().getLast()).isEqualTo("OK");
    }

    @Test
    public void testSpecialCharsAndNumbersAllowed() throws Exception {
        terminalMock.mock()
                .willReturn("add-admin hans59784%&$;maier76%$&;hans%4#;hahahaha") //should work
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isFalse();
        assertThat(terminalMock.getResult().getLast()).isEqualTo("OK");
    }

    @Test
    public void testCreateAdminShortUsername() throws Exception {
        terminalMock.mock()
                .willReturn("add-admin hans;maier;han;hahahaha") //3char username
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }

    @Test
    public void testCreateAdminLongUsername() throws Exception {
        terminalMock.mock()
                .willReturn("add-admin hans;maier;hanshanss;hahahaha") //9char username
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }

    @Test
    public void testCreateAdminShortPassword() throws Exception {
        terminalMock.mock()
                .willReturn("add-admin hans;maier;hans;h") //1char password
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }

    @Test
    public void testCreateAdminLongPassword() throws Exception {
        terminalMock.mock()
                .willReturn("add-admin hans;maier;hans;hahahahahahaha") //14char password
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }

    @Test
    public void testCreateAdminWithSameUsername() throws Exception {
        terminalMock.mock()
                .willReturn("add-admin hans;maier;hans;hahahaha") //should work
                .willReturn("add-admin hans;maier;hans;hahahaha") //should not work
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
        assertThat(terminalMock.getResult().getLast()).isEqualTo("OK");
    }

    @Test
    public void testCreateAdminMissingArgs() throws Exception {
        terminalMock.mock()
                .willReturn("add-admin hans;;hans;hahahaha") //missing last name
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }

    @Test
    public void testCreateAdminSpacesWork() throws Exception {
        terminalMock.mock()
                .willReturn("add-admin hans;ma ier;hans;hahahahaha") //should work
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isFalse();
        assertThat(terminalMock.getResult().getLast()).isEqualTo("OK");
    }

    @Test
    public void testCreateAdminExtraArg() throws Exception {
        terminalMock.mock()
                .willReturn("add-admin hans;maier;hans;hahahahaha;awgohaesg") //should not work
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }

    @Test
    public void testLoginWorks() throws Exception {
        terminalMock.mock()
                .willReturn("add-admin hans;ma ier;hans;hahahahaha")
                .willReturn("login-admin hans;hahahahaha") //should work
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isFalse();
        assertThat(terminalMock.getResult().getLast()).isEqualTo("OK");
    }

    @Test
    public void testLoginWrongUserName() throws Exception {
        terminalMock.mock()
                .willReturn("add-admin hans;ma ier;hans;hahahahaha")
                .willReturn("login-admin hanss;hahahahaha") //wrong username
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }

    @Test
    public void testLoginWrongPassword() throws Exception {
        terminalMock.mock()
                .willReturn("add-admin hans;ma ier;hans;hahahahaha")
                .willReturn("login-admin hans;hahahahahaa") //wrong password
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }

    @Test
    public void testLoginExtraArgs() throws Exception {
        terminalMock.mock()
                .willReturn("add-admin hans;ma ier;hans;hahahahaha")
                .willReturn("login-admin hans;hahahahaha;iag") //extra arg
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }

    @Test
    public void testLoginWhileAlreadyLoggedIn() throws Exception {
        terminalMock.mock(true)
                .willReturn("login-admin hans;password123") //already logged in
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }

    @Test
    public void testCreateAdminWhileAlreadyLoggedIn() throws Exception {
        terminalMock.mock(true)
                .willReturn("create-admin Hans2;Maier;hans2;password123") //already logged in
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }

    @Test
    public void testLogout() throws Exception {
        terminalMock.mock(true)
                .willReturn("logout-admin") //should work
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isFalse();
        assertThat(terminalMock.getResult().getLast()).isEqualTo("OK");
    }

    @Test
    public void testLogoutWhenNotLoggedIn() throws Exception {
        terminalMock.mock()
                .willReturn("logout-admin") //not logged in -> should not work
                .willReturn("quit");

        start();

        assertThat(terminalMock.isError()).isTrue();
    }
}