package net.ethx.switchy.win32;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.win32.W32APIOptions;

public interface User32Ext extends User32 {
    User32Ext INSTANCE = (User32Ext) com.sun.jna.Native.loadLibrary("user32", User32Ext.class, W32APIOptions.DEFAULT_OPTIONS);

    boolean IsIconic(final WinDef.HWND handle);
}
