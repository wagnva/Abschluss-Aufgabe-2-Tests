package base;

import edu.kit.informatik.Terminal;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;

/**
 * Created by wagnva on 09.02.18.
 */
public class TerminalMock {

    private Result<String> result;

    private boolean errorOccurred = false;

    public TerminalMock(){
        this.result = new Result<>();
    }

    public BDDMockito.BDDMyOngoingStubbing<String> mock(boolean startLoggedIn) throws Exception{
        PowerMockito.mockStatic(Terminal.class);

        //PowerMockito.doNothing().when(Terminal.class, "printLine", captor.capture());
        PowerMockito.when(Terminal.class, "printLine", Mockito.any())
                .then(invocation -> {
                    result.addResult(invocation.getArgument(0).toString());
                    return null;
                });
        PowerMockito.when(Terminal.class, "printError", Mockito.anyString())
                .then((invocation -> {
                    errorOccurred = true;
                    System.out.println(invocation.getArguments()[0].toString());
                    return null;
                }));

        BDDMockito.BDDMyOngoingStubbing<String> ongoingStubbing = BDDMockito.given(Terminal.readLine());

        if(startLoggedIn){
            ongoingStubbing = ongoingStubbing
                    .willReturn("add-admin hans;maier;hans;password123")
                    .willReturn("login-admin hans;password123");
            result.setIgnoreFirstEntries(2);
        }

        return ongoingStubbing;
    }

    public BDDMockito.BDDMyOngoingStubbing<String> mock() throws Exception{
        return mock(false);
    }

    public Result<String> getResult(){
        return this.result;
    }

    public boolean isError(){
        if(errorOccurred){
            errorOccurred = false;
            return true;
        }
        return false;
    }

}
