# Switchy

## An IntelliJ inspired task switcher 
Switchy is a small application which is designed to make it more efficient to switch applications. Often when developing,
multiple windows of the same class (IDE, terminal, etc.) are open at any one time. Switching via alt-tab, then identifying
 the correct window from a tiny titles or thumbnail feels inefficient.

IntelliJ IDEA (and other Jetbrains tools) offer a much faster way to switch between processes; hitting ctrl-n allows you
to type in fragments of the window title, and narrow down the selection. For example, to switch to a window called 
'micro-service-library', the inputs 'MSL', 'mi-se-li' or many other combinations would be sufficient to narrow down the
focus.

Switchy will hide itself in the background when started, waiting for you to summon it via Alt-backtick (see todos if this
does not work for you - Switchy at the moment is a quick and dirty itch-scratching exercise, and probably only works on UK
keyboards). The main window looks like this:

![screenshot 0](/images/switchy_0.png?raw=true "Default list")

Entering some input will start to narrow down the application list. If I'm hunting for a Putty session, for example, I might
start typing 'pu' to see what windows match:

![screenshot 1](/images/switchy_1.png?raw=true "Searching for Putty")

At this point, I can probably just hit up and down to select the window I want, then hit enter to focus. But let's pretend
that there are vastly more windows open than this, and we need to match on the 10.0.1.21 session. 

![screenshot 2](/images/switchy_2.png?raw=true "Identifying a particular session")

Success! One item remains, so I can hit enter. That window will be focused, and Switchy will hide itself away in the 
background, waiting for the next efficient switch. 

## Matching rules

Keyboard input is transformed into a regular expression per the following rules:

1. Input is tokenized; word boundaries are whitespace, uppercase or numeric characters
2. Tokens are collected, quoted, and transformed into the expression `(TOKEN).*?` 
3. The overall expression is prefixed with `(?i).*?` 

Once this pattern has been compiled, all window titles are matched against the pattern. This very roughly matches what 
 IntelliJ IDEA seems to do, or at least is close enough for my purposes.  

## Todo
1. Add a UI for preferences - most notably, key-binding. Switchy at the moment is hard-coded to Alt-backtick, which only
works on a UK keyboard layout. 

1. Implement alternate platform support - Switchy at present only runs under Windows, per my needs. The interface is
broken out reasonably cleanly via the net.ethx.switchy.model pacakge though; SwitchyPlatform, App, AppSource, etc. 

1. Fit more gracefully with the platform colors, etc. 

1. Support adding shortcuts; I often find myself looking for a window, and if it doesn't exist, opening it via another 
means. It would be useful to be able to add commands where nothing matches. 

1. Support drill-down into applications; would be pretty handy to be able to switch, for example, to a specific Chrome 
tab. 