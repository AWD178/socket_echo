import com.gpbtest.gpbtest.ClientThead;
import com.gpbtest.gpbtest.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestApp {



    @Test
    public void givenServerClient_whenServerEchosMessage_thenCorrect() throws InterruptedException {



        List<Thread> arrThreads = new ArrayList<Thread>();

        for(int i = 0; i < 10; i ++) {
            Thread T1 = new Thread(new ClientThead("Thread" + i));
            T1.start();
            arrThreads.add(T1);
        }

        for (int i = 0; i < arrThreads.size(); i++)
        {
            arrThreads.get(i).join();
        }

    }




}
