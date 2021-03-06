package info.xiantang.jerrymouse.core.handler;

import info.xiantang.jerrymouse.core.reactor.MultiReactor;
import info.xiantang.jerrymouse.core.utils.NetUtils;
import info.xiantang.jerrymouses2.client.EchoClient;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class TestSampleEchoServer {
    @Test
    public void canCorrectAcceptCommend() throws IOException, InterruptedException {
        int availablePort = NetUtils.getAvailablePort();
        MultiReactor reactor = MultiReactor.newBuilder()
                .setPort(availablePort)
                .setHandlerClass(EchoHandler.class)
                .setSubReactorCount(3)
                .build();

        Thread reactorT = new Thread(reactor);
        reactorT.start();

        EchoClient echoClient = new EchoClient("localhost", availablePort);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            sb.append(echoClient.sendReceive("HELO" + i));
            TimeUnit.MILLISECONDS.sleep(50);
        }
        echoClient.send(""+(char) (3));
        assertEquals("HELO0reactor> HELO1reactor> HELO2", sb.toString());
    }
}
