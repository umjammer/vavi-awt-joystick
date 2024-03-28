[![Release](https://jitpack.io/v/umjammer/vavi-awt-joystick.svg)](https://jitpack.io/#umjammer/vavi-awt-joystick)
[![Java CI](https://github.com/umjammer/vavi-awt-joystick/actions/workflows/maven.yml/badge.svg)](https://github.com/umjammer/vavi-awt-joystick/actions/workflows/maven.yml)
[![CodeQL](https://github.com/umjammer/vavi-awt-joystick/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/umjammer/vavi-awt-joystick/actions/workflows/codeql-analysis.yml)
![Java](https://img.shields.io/badge/Java-17-b07219)

# vavi-awt-joystick

<img src="https://user-images.githubusercontent.com/493908/207153859-91ad7707-d1d4-4609-bcdd-9cf992da5eec.png" width="320" alt="drive a map application by a handle controller"/> <sub>in 2003 😮</sub>

🌏 Control the world!

## Install

* https://jitpack.io/#umjammer/vavi-awt-joystick

## Usage

### example

 * [joystic mapper replacement](src/test/java/vavi/games/input/listener/MinecraftListener.java)

### graduates

* 🎓 [hid4java](https://github.com/umjammer/hid4java)
* 🎓 [purejavahidapi](https://github.com/umjammer/purejavahidapi)

## References

 * https://github.com/usb4java/usb4java
   * https://www.haljion.net/index.php/2013-04-12-08-15-44/2015-05-28-03-07-36/fpga-cpld/460-xilinx-license-configuration-manager
   * https://stackoverflow.com/questions/23796353/java-and-hid-communication#comment111359182_26327023
   * ⚠ 'javax.usb.properties' must be contained in your classpath
 * https://github.com/gary-rowe/hid4java 🎯
   * https://github.com/libusb/hidapi `$ brew install hidapi`
   * [jna version](https://github.com/umjammer/hid4java)
 * https://github.com/nyholku/purejavahidapi (🥺 but many native unused code in sources!) ... 🎯 descriptor parser
 * https://github.com/born2snipe/gamepad4j (api + impls)
 * https://github.com/jinput/jinput by sun (api + impls)
   * [jna version](https://github.com/umjammer/jinput)
   * https://sourceforge.net/projects/gamecontroller/ processing (jinput)
 * Game Controller framework (GCController)
   * https://chromium.googlesource.com/chromium/src/+/HEAD/device/gamepad/game_controller_data_fetcher_mac.mm
   * https://developer.apple.com/documentation/gamecontroller/gcdualshockgamepad gimme a sample
   * ~~https://discussionsjapan.apple.com/thread/255250239 no answer~~ macOS 14.3.1 is abel to detect ds4
   * ~~https://discussions.apple.com/thread/255260004 no answer (multipost lol)~~ ditto
 * lwjgl (glfw : IOKit(HID))
   * maven template... https://www.lwjgl.org/customize
   * https://github.com/TeamMidnightDust/MidnightControls (glfw)
   * https://github.com/isXander/Controlify
 * sdl2
   * https://fossies.org/linux/SDL2/src/joystick/iphoneos/SDL_mfijoystick.m
   * https://github.com/electronstudio/sdl2gdx (sdl2)
   * https://github.com/williamahartman/Jamepad -> sdl2gdx
   * https://gamefromscratch.com/libgdx-tutorial-part-14-gamepad-support/ (libgdx)
 * https://github.com/bwRavencl/ControllerBuddy (hid4java, sdl2, lwjgl) for flight simulator
 * https://github.com/libgdx/Jamepad (libgdx)
 * dualshock4
   * https://www.psdevwiki.com/ps4/DS4-USB
   * http://eleccelerator.com/wiki/index.php?title=DualShock_4
   * https://chromium.googlesource.com/chromium/src/+/HEAD/device/gamepad/dualshock4_controller.cc
   * https://android.googlesource.com/kernel/common.git/+/brillo-m9-release/drivers/hid/hid-sony.c
   * https://github.com/todbot/node-hid-ds4-test/blob/master/node-hid-ds4-test.js rumbling
   * https://stackoverflow.com/a/54541487
   * https://github.com/j0lama/DS4Lib
   * https://github.com/Ryochan7/DS4Windows (c#)
* html5 gamepad api
   * https://hardwaretester.com/gamepad
 * https://github.com/gurkenlabs/input4j ... JEP 442
 * mac app
   * https://github.com/florianmueller/GamepadMenu
   * https://github.com/qibinc/JoyMapperSilicon
   * https://yukkurigames.com/enjoyable/
 * cui
   * https://github.com/JetBrains/jediterm

### Tech Know

* [usb? hid?](https://github.com/umjammer/vavi-awt-joystick/wiki/Tech-Know)

## TODO

* ~~first check for a checkbox menu item~~
* ~~use standard library for Timer~~
* usb4java doesn't work for dualshock4 on mac ???
* ~~backport jinput ServiceLoader part into my [jinput](https://github.com/jinput/jinput)~~
* ~~let hid4java use mac framework directly (bypass hidapi)~~
* ~~descriptor to components~~
   * ~~how can we know descriptor length?~~ no needs to do
* ~~move hid perser to jinput~~
* ~~dualshock4 pad x, y 12bit little endian ???~~
* rename to vavi-awt-gamepad
* ~~hide os dependent implementation by interface~~
