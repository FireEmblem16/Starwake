For some reason DisplayRunner$1 won't move or even delete using a make file so it will be up to the builder to move it into the /bin/GUI/ folder so that the GUI can run.

A know bug occurs when using the GUI. On windows when attempting to open a cs229 image file an new copy of the program instead opens and the other sits and does nothing. This appears to be an incompatability problem for the library file on Windows. Unkonwn behavior on Linux(popeye) since I couldn't filter the visual output to my monitors. Normal image files open properly though.

To run the java GUI run "java -Djava.library.path=. DisplayRunner"