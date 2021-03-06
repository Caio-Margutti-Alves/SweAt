#!/bin/bash

# Fix the CircleCI path
export PATH="$ANDROID_HOME/platform-tools:$ANDROID_HOME/tools:$PATH"

DEPS="$ANDROID_HOME/installed-dependencies"

if [ ! -e $DEPS ]; then
  cp -r /usr/local/android-sdk-linux $ANDROID_HOME &&
  echo y | android update sdk -u -a -t build-tools-23.0.1 &&
  echo y | android update sdk -u -a -t android-23 &&
  echo y | android update sdk -u -a -t extra-android-m2repository &&
  echo y | android update sdk -u -a -t extra-google-m2repository &&
  echo y | android update sdk --no-ui --all --filter "platform-tools" &&
  echo y | android update sdk --no-ui --all --filter "tools" &&
  echo y | android update sdk --no-ui --all --filter "extra-google-google_play_services" &&
  echo y | android update sdk --no-ui --all --filter "extra-android-support" &&
  touch $DEPS
fi