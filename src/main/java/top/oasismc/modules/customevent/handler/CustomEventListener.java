package top.oasismc.modules.customevent.handler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import top.oasismc.modules.customevent.events.AsyncDateStartEvent;

import java.util.Random;

import static top.oasismc.OasisEss.bcByKey;

public class CustomEventListener implements Listener {

    private final Random random;
    private static CustomEventListener LISTENER;

    static {
        LISTENER = new CustomEventListener();
    }

    public static CustomEventListener getListener() {
        return LISTENER;
    }
    private CustomEventListener() {
        random = new Random();
    }

    @EventHandler
    public void onDateStart(AsyncDateStartEvent event) {
        int eventId = random.nextInt(21);
        switch (eventId) {
            case 0:
                event1();
                break;
            case 5:
                event2();
                break;
            case 10:
                event3();
                break;
            case 15:
                event4();
                break;
            case 20:
                event5();
                break;
        }
        bcByKey("event.dateStart");
    }

    private void event1() {}
    private void event2() {}
    private void event3() {}
    private void event4() {}
    private void event5() {}

}
