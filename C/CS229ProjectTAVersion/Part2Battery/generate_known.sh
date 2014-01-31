#!/bin/bash
./known_gensine.exe MONO 440 8 8000 101 2.78 > known1.su
./known_gensine.exe MONO 777 8 8000 101 1.75 > known2.su
./known_gensine.exe MONO 1234 8 8000 101 .4123 > known3.su
./known_gensine.exe MONO 500 8 8000 50 1.0 > known4.su
./known_gensine.exe MONO 500 16 8000 50 1.0 > known5.su
./known_gensine.exe MONO 500 32 8000 50 1.0 > known6.su
./known_gensine.exe STEREO 500 8 8000 50 1.0 > known7.su
./known_gensine.exe STEREO 500 16 8000 50 1.0 > known8.su
./known_gensine.exe STEREO 500 32 8000 50 1.0 > known9.su
./known_gensine.exe MONO 500 8 8000 50 1.0 > temp.su
./convertsu.exe < temp.su > known10.wav
rm temp.su
./known_gensine.exe MONO 500 16 8000 50 1.0 > temp.su
./convertsu.exe < temp.su > known11.wav
rm temp.su
./known_gensine.exe MONO 500 32 8000 50 1.0 > temp.su
./convertsu.exe < temp.su > known12.wav
rm temp.su