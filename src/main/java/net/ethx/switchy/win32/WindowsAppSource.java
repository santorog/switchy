package net.ethx.switchy.win32;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import net.ethx.switchy.model.App;
import net.ethx.switchy.model.AppSource;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static net.ethx.switchy.win32.User32Ext.INSTANCE;

public class WindowsAppSource implements AppSource {
    @Override
    public List<App> get() {
        final List<App> ret = new ArrayList<>();
        INSTANCE.EnumWindows(new WinUser.WNDENUMPROC() {
            final char[] buf = new char[512];
            @Override
            public boolean callback(WinDef.HWND handle, Pointer data) {
                if (INSTANCE.IsWindowVisible(handle)) {
                    INSTANCE.GetWindowText(handle, buf, buf.length);
                    final String title = Native.toString(buf).trim();
                    if (!title.isEmpty()) {
                        ret.add(new WindowsApp(title, handle));
                    }
                }

                return true;
            }
        }, null);

        ret.sort(Comparator.comparing(App::name, String.CASE_INSENSITIVE_ORDER));
        return ret;
    }
}
