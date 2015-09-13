package net.ethx.switchy.win32;

import com.sun.jna.platform.win32.WinUser;
import net.ethx.switchy.Controller;
import net.ethx.switchy.model.AppSource;
import net.ethx.switchy.model.SwitchyPlatform;

import java.awt.event.KeyEvent;
import java.util.concurrent.Executors;

import static com.sun.jna.platform.win32.WinUser.MOD_WIN;
import static com.sun.jna.platform.win32.WinUser.WM_HOTKEY;
import static net.ethx.switchy.win32.User32Ext.INSTANCE;

public class WindowsSwitchyPlatform implements SwitchyPlatform {

    @Override
    public AppSource appSource() {
        return new WindowsAppSource();
    }

    @Override
    public void initialise(Controller controller) {
        Executors.newSingleThreadExecutor(r -> new Thread(r, "switchy-hotkey"))
                .execute(() -> {
                    if (!INSTANCE.RegisterHotKey(null, 1, MOD_WIN, KeyEvent.VK_G)) {
                        System.err.println("Could not register global hotkey");
                        System.exit(-1);
                    }

                    while (!Thread.currentThread().isInterrupted()) {
                        final WinUser.MSG msg = new WinUser.MSG();
                        if (INSTANCE.GetMessage(msg, null, 0, 0) != 0) {
                            if (msg.message == WM_HOTKEY && msg.wParam.intValue() == 1) {
                                controller.model().visible().set(true);
                            }
                        } else {
                            Thread.currentThread().interrupt();
                        }
                    }

                    System.out.println("Finished running key loop");
                });
    }
}
