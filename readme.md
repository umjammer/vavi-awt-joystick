[![Release](https://jitpack.io/v/umjammer/vavi-awt-joystick.svg)](https://jitpack.io/#umjammer/vavi-awt-joystick)
[![Java CI](https://github.com/umjammer/vavi-awt-joystick/actions/workflows/maven.yml/badge.svg)](https://github.com/umjammer/vavi-awt-joystick/actions/workflows/maven.yml)
[![CodeQL](https://github.com/umjammer/vavi-awt-joystick/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/umjammer/vavi-awt-joystick/actions/workflows/codeql-analysis.yml)
![Java](https://img.shields.io/badge/Java-17-b07219)

# vavi-awt-joystick

<img src="https://user-images.githubusercontent.com/493908/207153859-91ad7707-d1d4-4609-bcdd-9cf992da5eec.png" width="320" alt="drive a map application by a handle controller"/> <sub>in 2003 😮</sub>

🌏 Control the world!

## Install

* https://jitpack.io/#umjammer/vavi-awt-joystick

## TODO

 * ~~first check for a checkbox menu item~~
 * ~~use standard library for Timer~~
 * usb4java doesn't work for dualshock4 on mac ???
 * backport jinput ServiceLoader part into my [jinput](https://github.com/jinput/jinput)
 * ~~let hid4java use mac framework directly (bypass hidapi)~~

## References

 * https://github.com/usb4java/usb4java
   * https://www.haljion.net/index.php/2013-04-12-08-15-44/2015-05-28-03-07-36/fpga-cpld/460-xilinx-license-configuration-manager
   * https://stackoverflow.com/questions/23796353/java-and-hid-communication#comment111359182_26327023
   * ⚠ 'javax.usb.properties' must be contained in your classpath
 * https://github.com/gary-rowe/hid4java 🎯
   * https://github.com/libusb/hidapi `$ brew install hidapi`
   * https://fossies.org/linux/SDL2/src/joystick/iphoneos/SDL_mfijoystick.m
   * [jna version](wip) wip
 * https://github.com/nyholku/purejavahidapi (🥺 but many native unused code in sources) ... 🎯 descriptor parser
 * https://github.com/born2snipe/gamepad4j
 * https://github.com/jinput/jinput
   * [jna version](https://github.com/umjammer/jinput) wip
 * https://sourceforge.net/projects/gamecontroller/
 * Game Controller framework
   * https://chromium.googlesource.com/chromium/src/+/HEAD/device/gamepad/game_controller_data_fetcher_mac.mm
 * lwjgl
   * maven template... https://www.lwjgl.org/customize
   * https://github.com/TeamMidnightDust/MidnightControls
   * https://github.com/isXander/Controlify
 * https://gamefromscratch.com/libgdx-tutorial-part-14-gamepad-support/ (libgdx)
 * https://github.com/electronstudio/sdl2gdx
 * https://github.com/williamahartman/Jamepad
 * https://github.com/Ryochan7/DS4Windows (c#)
 * https://yukkurigames.com/enjoyable/
 * dualshock4
   * http://eleccelerator.com/wiki/index.php?title=DualShock_4
   * https://chromium.googlesource.com/chromium/src/+/HEAD/device/gamepad/dualshock4_controller.cc
   * https://android.googlesource.com/kernel/common.git/+/brillo-m9-release/drivers/hid/hid-sony.c
   * https://github.com/todbot/node-hid-ds4-test/blob/master/node-hid-ds4-test.js rumbling
   * https://stackoverflow.com/a/54541487
   * https://github.com/j0lama/DS4Lib
 * html5 gamepad api
   * https://hardwaretester.com/gamepad 

## Tech Know

```
  +---+---------+---------------+-------------------+
  |   |         |               | USB-IF            | <-> USB
  | J | Windows |     HID       +-------------------+
  | i |         |               | Bluetooth Profile | <-> Bluetooth
  | n +---------+-----+         +-------------------+
  | p |         |     |         |    :              |
  | u |  MacOS  |     +---------+-------------------+
  | t |         |               | USB               |
  |   |         |     IOKit     +-------------------+
  |   |         |               | FireWire          |
  |   |         |               +-------------------+
  |   |         |               | ATSMART           |
  |   |         |               +-------------------+
  |   |         |               |     :             |
  +---+---------+---------------+-------------------+
 ```