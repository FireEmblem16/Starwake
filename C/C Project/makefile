#Source directories
SOURCE = ./Source/
HEADERS = ./Headers/

#Output directories
LISTING = ./Listings/
OBJDIR = ./Objects/
BIN = ./bin/
LIBRARY = ./Library/
GUI = ./bin/GUI/

#Linking flags
ASMFLAGS = -O3 -pedantic -Wall

#Compiliation flags
CFLAGS = -c -O3 -pedantic -Wall -g -Wa,-a,-ad
JNI = -I "/usr/java/jdk1.6.0_17/include/" -I "/usr/java/jdk1.6.0_17/include/linux/"
DYNAMIC_OBJECT = -fPIC
DYNAMIC_LIB = -shared

#Execution file output file extension [.exe for Windows, nothing for Linux]
EXEC_EXTENSION = #.exe

#Build everything and report sucess to the user
all: directorys ${BIN}makesquare${EXEC_EXTENSION} ${BIN}imagestats${EXEC_EXTENSION} ${BIN}cs2ppm${EXEC_EXTENSION} ${BIN}ppm2cs${EXEC_EXTENSION} ${BIN}darken${EXEC_EXTENSION} ${BIN}lighten${EXEC_EXTENSION} ${BIN}rotate${EXEC_EXTENSION} ${BIN}flip${EXEC_EXTENSION} ${BIN}stitch${EXEC_EXTENSION} ${BIN}convolute${EXEC_EXTENSION} ${BIN}Matrix.class GUI
	@echo ""
	@echo "Build completed. Enjoy your new software."

#Create some dummy targets
makesquare: ${BIN}makesquare${EXEC_EXTENSION}
imagestats: ${BIN}imagestats${EXEC_EXTENSION}
cs2ppm: ${BIN}cs2ppm${EXEC_EXTENSION}
ppm2cs: ${BIN}ppm2cs${EXEC_EXTENSION}
darken: ${BIN}darken${EXEC_EXTENSION}
lighten: ${BIN}lighten${EXEC_EXTENSION}
rotate: ${BIN}rotate${EXEC_EXTENSION}
flip: ${BIN}flip${EXEC_EXTENSION}
stitch: ${BIN}stitch${EXEC_EXTENSION}
convolute: ${BIN}convolute${EXEC_EXTENSION}
Matrix: ${BIN}Matrix.class

#Ensure our output directories exist
directorys:
	@echo ""
	@echo "Making directory for object files to sit."
	@echo "It is your responsibility to purge the files if you do not desire them."
	mkdir -p "${OBJDIR}"
	@echo ""
	@echo "Making directory for listing files to sit."
	@echo "It is your responsibility to purge the files if you do not desire them."
	mkdir -p "${LISTING}"
	@echo ""
	@echo "Making directory for libraries to sit."
	mkdir -p "${LIBRARY}"
	@echo ""
	@echo "Making directory for executables to sit."
	mkdir -p "${BIN}"
	@echo ""
	@echo "Making directory for image viewer to sit."
	mkdir -p "${GUI}"

#Make the makesquare executable
${BIN}makesquare${EXEC_EXTENSION}: ${LIBRARY}liberror.a ${LIBRARY}libimage.a ${LIBRARY}libdefines.a ${OBJDIR}makesquare.o
	@echo ""
	@echo "Creating makesquare executable."
	gcc ${ASMFLAGS} "${OBJDIR}makesquare.o" -o "${BIN}makesquare${EXEC_EXTENSION}" -L${LIBRARY} -lerror -limage -ldefines

#Make the error library
${LIBRARY}liberror.a: ${OBJDIR}Error.o
	@echo ""
	@echo "Creating library file for error functions."
	ar rcs "${LIBRARY}liberror.a" "${OBJDIR}Error.o"

#Make Error.o
${OBJDIR}Error.o: ${SOURCE}Error.c ${HEADERS}Error.h ${HEADERS}Defines.h
	@echo ""
	@echo "Creating error object file for library."
	gcc ${CFLAGS} "${SOURCE}Error.c" -o "${OBJDIR}Error.o" > "${LISTING}Error.lst"

#Make the image library
${LIBRARY}libimage.a: ${OBJDIR}CS229Image.o ${OBJDIR}PPMImage.o
	@echo ""
	@echo "Creating library file for image functions."
	ar rcs "${LIBRARY}libimage.a" "${OBJDIR}CS229Image.o" "${OBJDIR}PPMImage.o"

#Make CS229Image.o
${OBJDIR}CS229Image.o: ${SOURCE}CS229Image.c ${HEADERS}CS229Image.h ${HEADERS}PPMImage.h ${HEADERS}Defines.h ${HEADERS}Error.h
	@echo ""
	@echo "Creating CS229Image object file for library."
	gcc ${CFLAGS} "${SOURCE}CS229Image.c" -o "${OBJDIR}CS229Image.o" > "${LISTING}CS229Image.lst"

#Make PPMImage.o
${OBJDIR}PPMImage.o: ${SOURCE}PPMImage.c ${HEADERS}PPMImage.h ${HEADERS}Defines.h ${HEADERS}Error.h
	@echo ""
	@echo "Creating PPMImage object file for library."
	gcc ${CFLAGS} "${SOURCE}PPMImage.c" -o "${OBJDIR}PPMImage.o" > "${LISTING}PPMImage.lst"

#Make the defines library
${LIBRARY}libdefines.a: ${OBJDIR}Defines.o
	@echo ""
	@echo "Creating library file for standard functions."
	ar rcs "${LIBRARY}libdefines.a" "${OBJDIR}Defines.o"

#Make Defines.o
${OBJDIR}Defines.o: ${SOURCE}Defines.c ${HEADERS}Defines.h
	@echo ""
	@echo "Creating Defines object file for library."
	gcc ${CFLAGS} "${SOURCE}Defines.c" -o "${OBJDIR}Defines.o" > "${LISTING}Defines.lst"

#Make makequare.o
${OBJDIR}makesquare.o: ${SOURCE}makesquare.c ${HEADERS}Defines.h ${HEADERS}CS229Image.h ${HEADERS}Error.h
	@echo ""
	@echo "Compiling makesquare.c into an object file and outputing its assembly listing."
	gcc ${CFLAGS} "${SOURCE}makesquare.c" -o "${OBJDIR}makesquare.o" > "${LISTING}makesquare.lst"

#Make the imagestats executable
${BIN}imagestats${EXEC_EXTENSION}: ${OBJDIR}imagestats.o
	@echo ""
	@echo "Creating imagestats executable."
	gcc ${ASMFLAGS} "${OBJDIR}imagestats.o" -o "${BIN}imagestats${EXEC_EXTENSION}" -L${LIBRARY} -lerror -limage -ldefines

#Make imagestats.o
${OBJDIR}imagestats.o: ${SOURCE}imagestats.c ${HEADERS}Defines.h ${HEADERS}CS229Image.h ${HEADERS}Error.h
	@echo ""
	@echo "Compiling imagestats.c into an object file and outputing its assembly listing."
	gcc ${CFLAGS} -c "${SOURCE}imagestats.c" -o "${OBJDIR}imagestats.o" > "${LISTING}imagestats.lst"

#Make the cs2ppm executable
${BIN}cs2ppm${EXEC_EXTENSION}: ${LIBRARY}liberror.a ${LIBRARY}libimage.a ${LIBRARY}libdefines.a ${OBJDIR}cs2ppm.o
	@echo ""
	@echo "Creating cs2ppm executable."
	gcc ${ASMFLAGS} "${OBJDIR}cs2ppm.o" -o "${BIN}cs2ppm${EXEC_EXTENSION}" -L${LIBRARY} -lerror -limage -ldefines

#Make cs2ppm.o
${OBJDIR}cs2ppm.o: ${SOURCE}cs2ppm.c ${HEADERS}Defines.h ${HEADERS}CS229Image.h ${HEADERS}PPMImage.h ${HEADERS}Error.h
	@echo ""
	@echo "Compiling cs2ppm.c into an object file and outputing its assembly listing."
	gcc ${CFLAGS} "${SOURCE}cs2ppm.c" -o "${OBJDIR}cs2ppm.o" > "${LISTING}cs2ppm.lst"

#Make the ppm2cs executable
${BIN}ppm2cs${EXEC_EXTENSION}: ${LIBRARY}liberror.a ${LIBRARY}libimage.a ${LIBRARY}libdefines.a ${OBJDIR}ppm2cs.o
	@echo ""
	@echo "Creating ppm2cs executable."
	gcc ${ASMFLAGS} "${OBJDIR}ppm2cs.o" -o "${BIN}ppm2cs${EXEC_EXTENSION}" -L${LIBRARY} -lerror -limage -ldefines

#Make ppm2cs.o
${OBJDIR}ppm2cs.o: ${SOURCE}ppm2cs.c ${HEADERS}Defines.h ${HEADERS}CS229Image.h ${HEADERS}PPMImage.h ${HEADERS}Error.h
	@echo ""
	@echo "Compiling ppm2cs.c into an object file and outputing its assembly listing."
	gcc ${CFLAGS} "${SOURCE}ppm2cs.c" -o "${OBJDIR}ppm2cs.o" > "${LISTING}ppm2cs.lst"

#Make the darken executable
${BIN}darken${EXEC_EXTENSION}: ${LIBRARY}liberror.a ${LIBRARY}libimage.a ${LIBRARY}libdefines.a ${OBJDIR}darken.o
	@echo ""
	@echo "Creating darken executable."
	gcc ${ASMFLAGS} "${OBJDIR}darken.o" -o "${BIN}darken${EXEC_EXTENSION}" -L${LIBRARY} -lerror -limage -ldefines

#Make darken.o
${OBJDIR}darken.o: ${SOURCE}darken.c ${HEADERS}Defines.h ${HEADERS}CS229Image.h ${HEADERS}Error.h
	@echo ""
	@echo "Compiling darken.c into an object file and outputing its assembly listing."
	gcc ${CFLAGS} "${SOURCE}darken.c" -o "${OBJDIR}darken.o" > "${LISTING}darken.lst"

#Make the lighten executable
${BIN}lighten${EXEC_EXTENSION}: ${LIBRARY}liberror.a ${LIBRARY}libimage.a ${LIBRARY}libdefines.a ${OBJDIR}lighten.o
	@echo ""
	@echo "Creating lighten executable."
	gcc ${ASMFLAGS} "${OBJDIR}lighten.o" -o "${BIN}lighten${EXEC_EXTENSION}" -L${LIBRARY} -lerror -limage -ldefines

#Make lighten.o
${OBJDIR}lighten.o: ${SOURCE}lighten.c ${HEADERS}Defines.h ${HEADERS}CS229Image.h ${HEADERS}Error.h
	@echo ""
	@echo "Compiling lighten.c into an object file and outputing its assembly listing."
	gcc ${CFLAGS} "${SOURCE}lighten.c" -o "${OBJDIR}lighten.o" > "${LISTING}lighten.lst"

#Make the rotate executable
${BIN}rotate${EXEC_EXTENSION}: ${LIBRARY}liberror.a ${LIBRARY}libimage.a ${LIBRARY}libdefines.a ${OBJDIR}rotate.o
	@echo ""
	@echo "Creating rotate executable."
	gcc ${ASMFLAGS} "${OBJDIR}rotate.o" -o "${BIN}rotate${EXEC_EXTENSION}" -L${LIBRARY} -lerror -limage -ldefines

#Make rotate.o
${OBJDIR}rotate.o: ${SOURCE}rotate.c ${HEADERS}Defines.h ${HEADERS}CS229Image.h ${HEADERS}Error.h
	@echo ""
	@echo "Compiling rotate.c into an object file and outputing its assembly listing."
	gcc ${CFLAGS} "${SOURCE}rotate.c" -o "${OBJDIR}rotate.o" > "${LISTING}rotate.lst"

#Make the flip executable
${BIN}flip${EXEC_EXTENSION}: ${LIBRARY}liberror.a ${LIBRARY}libimage.a ${LIBRARY}libdefines.a ${OBJDIR}flip.o
	@echo ""
	@echo "Creating flip executable."
	gcc ${ASMFLAGS} "${OBJDIR}flip.o" -o "${BIN}flip${EXEC_EXTENSION}" -L${LIBRARY} -lerror -limage -ldefines

#Make flip.o
${OBJDIR}flip.o: ${SOURCE}flip.c ${HEADERS}Defines.h ${HEADERS}CS229Image.h ${HEADERS}Error.h
	@echo ""
	@echo "Compiling flip.c into an object file and outputing its assembly listing."
	gcc ${CFLAGS} "${SOURCE}flip.c" -o "${OBJDIR}flip.o" > "${LISTING}flip.lst"

#Make the stitch executable
${BIN}stitch${EXEC_EXTENSION}: ${LIBRARY}liberror.a ${LIBRARY}libimage.a ${LIBRARY}libdefines.a ${OBJDIR}stitch.o
	@echo ""
	@echo "Creating stitch executable."
	gcc ${ASMFLAGS} "${OBJDIR}stitch.o" -o "${BIN}stitch${EXEC_EXTENSION}" -L${LIBRARY} -lerror -limage -ldefines

#Make stitch.o
${OBJDIR}stitch.o: ${SOURCE}stitch.c ${HEADERS}Defines.h ${HEADERS}CS229Image.h ${HEADERS}Error.h
	@echo ""
	@echo "Compiling stitch.c into an object file and outputing its assembly listing."
	gcc ${CFLAGS} "${SOURCE}stitch.c" -o "${OBJDIR}stitch.o" > "${LISTING}stitch.lst"

#Make the convolute executable
${BIN}convolute${EXEC_EXTENSION}: ${LIBRARY}liberror.a ${LIBRARY}libimage.a ${LIBRARY}libdefines.a ${OBJDIR}convolute.o
	@echo ""
	@echo "Creating convolute executable."
	gcc ${ASMFLAGS} "${OBJDIR}convolute.o" -o "${BIN}convolute${EXEC_EXTENSION}" -L${LIBRARY} -lerror -limage -ldefines

#Make convolute.o
${OBJDIR}convolute.o: ${SOURCE}convolute.c ${HEADERS}Defines.h ${HEADERS}CS229Image.h ${HEADERS}Error.h
	@echo ""
	@echo "Compiling convolute.c into an object file and outputing its assembly listing."
	gcc ${CFLAGS} "${SOURCE}convolute.c" -o "${OBJDIR}convolute.o" > "${LISTING}convolute.lst"

#Make the java class file if it hasn't been yet
${BIN}Matrix.class: ${SOURCE}Matrix.java ${BIN}libMatrix.so
	@echo ""
	@echo "Compiling the Matrix class file."
	cp ${SOURCE}Matrix.java Matrix.java
	javac Matrix.java
	mv Matrix.class ${BIN}Matrix.class
	rm Matrix.java

#Make the dynamic library for Matrix
${BIN}libMatrix.so: ${OBJDIR}Matrix.o
	@echo ""
	@echo "Createing the dynamic library libMatrix.so from Matrix.o and outputing into the bin folder."
	gcc ${ASMFLAGS} ${JNI} ${DYNAMIC_LIB} "${OBJDIR}Matrix.o" -o "${BIN}libMatrix.so"

#Make the Matrix object file
${OBJDIR}Matrix.o: ${SOURCE}Matrix.c ${HEADERS}Matrix.h
	@echo ""
	@echo "Compiling Matrix.c into an object file and outputing its assembly listing."
	gcc ${CFLAGS} ${DYNAMIC_OBJECT} ${JNI} "${SOURCE}Matrix.c" -o "${OBJDIR}Matrix.o" > "${LISTING}Matrix.lst"

#Create all of the class files for the GUI
GUI: copy ${GUI}CS229_Image.class ${GUI}Saver.class ${GUI}Loader.class ${GUI}Listener.class ${GUI}ImageDisplay.class ${GUI}Host.class ${GUI}DisplayRunner.class ${GUI}CS229BufferedImage.class ${GUI}Convoluter.class
	@echo ""
	@echo "Java image viewer GUI built."
	@echo "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
	@echo "YOU WILL NEED TO COPY DisplayRunner(Money Sign)1.class TO THE"
	@echo "FOLDER /bin/GUI/ BY HAND AS DOLLAR SIGNS DON'T LIKE TO"
	@echo "WORK IN MAKE FILES."
	@echo "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
	@echo ""
	@echo "Cleaning temp .java files."
	rm -f Saver.java
	rm -f Loader.java
	rm -f Listener.java
	rm -f ImageDisplay.java
	rm -f Host.java
	rm -f DisplayRunner.java
	rm -f CS229BufferedImage.java
	rm -f CS229_Image.java
	rm -f Convoluter.java
	@echo ""
	@echo "Cleaning temp .class files."
	rm -f Saver.class
	rm -f Loader.class
	rm -f Listener.class
	rm -f ImageDisplay.class
	rm -f Host.class
	rm -f DisplayRunner.class
	rm -f CS229BufferedImage.class
	rm -f CS229_Image.class
	rm -f Convoluter.class

#Copy all of the java files out of Source
copy:
	@echo ""
	@echo "Creating temporary .java files."
	cp -f ${SOURCE}Saver.java Saver.java
	cp -f ${SOURCE}Loader.java Loader.java
	cp -f ${SOURCE}Listener.java Listener.java
	cp -f ${SOURCE}ImageDisplay.java ImageDisplay.java
	cp -f ${SOURCE}Host.java Host.java
	cp -f ${SOURCE}DisplayRunner.java DisplayRunner.java
	cp -f ${SOURCE}CS229BufferedImage.java CS229BufferedImage.java
	cp -f ${SOURCE}CS229_Image.java CS229_Image.java
	cp -f ${SOURCE}Convoluter.java Convoluter.java

#Make CS229_Image.class
${GUI}CS229_Image.class: ${SOURCE}CS229_Image.java ${GUI}libCS229_Image.so
	@echo ""
	@echo "Compiling the CS229_Image class file."
	javac CS229_Image.java
	mv -f CS229_Image.class ${GUI}CS229_Image.class

#Create the dynamic library for image loading
${GUI}libCS229_Image.so: ${GUI}liberrorshared.a ${GUI}libimageshared.a ${GUI}libdefinesshared.a ${OBJDIR}CS229_Image.o
	@echo ""
	@echo "Creating the shared library libCS229_Image.so from CS229_Image.o and outputing into the GUI folder."
	gcc ${ASMFLAGS} ${JNI} ${DYNAMIC_LIB} "${OBJDIR}CS229_Image.o" -o "${GUI}libCS229_Image.so" -L${GUI} -lerrorshared -limageshared -ldefinesshared

#Make the CS229_Image object file
${OBJDIR}CS229_Image.o: ${SOURCE}CS229_Image.c ${HEADERS}CS229_Image.h
	@echo ""
	@echo "Compiling CS229_Image.c into an object file and outputing its assembly listing."
	gcc ${CFLAGS} ${DYNAMIC_OBJECT} ${JNI} "${SOURCE}CS229_Image.c" -o "${OBJDIR}CS229_Image.o" > "${LISTING}CS229_Image.lst"

#Make the shared error library
${GUI}liberrorshared.a: ${OBJDIR}Errorshared.o
	@echo ""
	@echo "Creating shared library file for error functions."
	gcc ${ASMFLAGS} ${DYNAMIC_LIB} "${OBJDIR}Errorshared.o" -o "${GUI}liberrorshared.a"

#Make Errorshared.o
${OBJDIR}Errorshared.o: ${SOURCE}Error.c ${HEADERS}Error.h ${HEADERS}Defines.h
	@echo ""
	@echo "Creating shared error object file for library."
	gcc ${CFLAGS} ${DYNAMIC_OBJECT} "${SOURCE}Error.c" -o "${OBJDIR}Errorshared.o" > "${LISTING}Errorshared.lst"

#Make the shared image library
${GUI}libimageshared.a: ${OBJDIR}CS229Imageshared.o ${OBJDIR}PPMImageshared.o
	@echo ""
	@echo "Creating shared library file for image functions."
	gcc ${ASMFLAGS} ${DYNAMIC_LIB} "${OBJDIR}CS229Imageshared.o" "${OBJDIR}PPMImageshared.o" -o "${GUI}libimageshared.a"

#Make CS229Imageshared.o
${OBJDIR}CS229Imageshared.o: ${SOURCE}CS229Image.c ${HEADERS}CS229Image.h ${HEADERS}PPMImage.h ${HEADERS}Defines.h ${HEADERS}Error.h
	@echo ""
	@echo "Creating shared CS229Image object file for library."
	gcc ${CFLAGS} ${DYNAMIC_OBJECT} "${SOURCE}CS229Image.c" -o "${OBJDIR}CS229Imageshared.o" > "${LISTING}CS229Imageshared.lst"

#Make PPMImageshared.o
${OBJDIR}PPMImageshared.o: ${SOURCE}PPMImage.c ${HEADERS}PPMImage.h ${HEADERS}Defines.h ${HEADERS}Error.h
	@echo ""
	@echo "Creating shared PPMImage object file for library."
	gcc ${CFLAGS} ${DYNAMIC_OBJECT} "${SOURCE}PPMImage.c" -o "${OBJDIR}PPMImageshared.o" > "${LISTING}PPMImageshared.lst"

#Make the shared defines library
${GUI}libdefinesshared.a: ${OBJDIR}Definesshared.o
	@echo ""
	@echo "Creating shared library file for standard functions."
	gcc ${ASMFLAGS} ${DYNAMIC_LIB} "${OBJDIR}Definesshared.o" -o "${GUI}libdefinesshared.a"

#Make Definesshared.o
${OBJDIR}Definesshared.o: ${SOURCE}Defines.c ${HEADERS}Defines.h
	@echo ""
	@echo "Creating shared Defines object file for library."
	gcc ${CFLAGS} ${DYNAMIC_OBJECT} "${SOURCE}Defines.c" -o "${OBJDIR}Definesshared.o" > "${LISTING}Definesshared.lst"

#Make Saver.class
${GUI}Saver.class: ${SOURCE}Saver.java
	@echo ""
	@echo "Compiling the Saver class file."
	javac Saver.java
	mv -f Saver.class ${GUI}Saver.class

#Make Loader.class
${GUI}Loader.class: ${SOURCE}Loader.java
	@echo ""
	@echo "Compiling the Loader class file."
	javac Loader.java
	mv -f Loader.class ${GUI}Loader.class

#Make Listener.class
${GUI}Listener.class: ${SOURCE}Listener.java
	@echo ""
	@echo "Compiling the Listener class file."
	javac Listener.java
	mv -f Listener.class ${GUI}Listener.class

#Make ImageDisplay.class
${GUI}ImageDisplay.class: ${SOURCE}ImageDisplay.java
	@echo ""
	@echo "Compiling the ImageDisplay class file."
	javac ImageDisplay.java
	mv -f ImageDisplay.class ${GUI}ImageDisplay.class
	
#Make Host.class
${GUI}Host.class: ${SOURCE}Host.java
	@echo ""
	@echo "Compiling the Host class file."
	javac Host.java
	mv -f Host.class ${GUI}Host.class

#Make DisplayRunner.class
${GUI}DisplayRunner.class: ${SOURCE}DisplayRunner.java
	@echo ""
	@echo "Compiling the DisplayRunner class file."
	javac DisplayRunner.java
	mv -f DisplayRunner.class ${GUI}DisplayRunner.class

#Make CS229BufferedImage.class
${GUI}CS229BufferedImage.class: ${SOURCE}CS229BufferedImage.java
	@echo ""
	@echo "Compiling the CS229BufferedImage class file."
	javac CS229BufferedImage.java
	mv -f CS229BufferedImage.class ${GUI}CS229BufferedImage.class

#Make Convoluter.class
${GUI}Convoluter.class: ${SOURCE}Convoluter.java
	@echo ""
	@echo "Compiling the Convoluter class file."
	javac Convoluter.java
	mv -f Convoluter.class ${GUI}Convoluter.class

#Do any cleanup that is required
clean:
	@echo ""
	@echo "Removing all object files and listing files."
	rm -rf ${OBJDIR}
	rm -rf ${LISTING}

#Remove everything that was generated
cleandist:
	@echo ""
	@echo "Removing all files generated by this makefile."
	rm -rf ${OBJDIR}
	rm -rf ${LISTING}
	rm -rf ${LIBRARY}
	rm -rf ${BIN}