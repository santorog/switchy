package net.ethx.switchy.win32;

import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import net.ethx.switchy.model.App;

import static net.ethx.switchy.win32.User32Ext.INSTANCE;

public class WindowsApp extends App {
    private final WinDef.HWND handle;

    public WindowsApp(final String name, final WinDef.HWND handle) {
        super(name);
        this.handle = handle;
    }

    @Override
    public void activate() {
        if (INSTANCE.IsIconic(handle)) {
            INSTANCE.ShowWindow(handle, WinUser.SW_RESTORE);
        }
        INSTANCE.SetForegroundWindow(handle);
    }
}
