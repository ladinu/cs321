#!/bin/sh

jflex --nobak MiniColor.jflex;
javac MiniColor.java;
java MiniColor test.mini > out.html;
