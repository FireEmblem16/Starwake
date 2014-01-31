#
# Generated Makefile - do not edit!
#
# Edit the Makefile in the project folder instead (../Makefile). Each target
# has a -pre and a -post target defined where you can add customized code.
#
# This makefile implements configuration specific macros and targets.


# Environment
MKDIR=mkdir
CP=cp
GREP=grep
NM=nm
CCADMIN=CCadmin
RANLIB=ranlib
CC=gcc.exe
CCC=g++.exe
CXX=g++.exe
FC=
AS=as.exe

# Macros
CND_PLATFORM=Cygwin-Windows
CND_CONF=Debug
CND_DISTDIR=dist

# Include project Makefile
include Makefile

# Object Directory
OBJECTDIR=build/${CND_CONF}/${CND_PLATFORM}

# Object Files
OBJECTFILES= \
	${OBJECTDIR}/Source/stitch.o \
	${OBJECTDIR}/Headers/Error.o \
	${OBJECTDIR}/Source/PPMImage.o \
	${OBJECTDIR}/Headers/CS229Image.o \
	${OBJECTDIR}/Source/imagestats.o \
	${OBJECTDIR}/Source/cs2ppm.o \
	${OBJECTDIR}/Headers/PPMImage.o \
	${OBJECTDIR}/Source/CS229Image.o \
	${OBJECTDIR}/Source/Defines.o \
	${OBJECTDIR}/Source/darken.o \
	${OBJECTDIR}/Source/ppm2cs.o \
	${OBJECTDIR}/Source/lighten.o \
	${OBJECTDIR}/Source/Error.o \
	${OBJECTDIR}/Headers/Defines.o \
	${OBJECTDIR}/Source/makesquare.o


# C Compiler Flags
CFLAGS=

# CC Compiler Flags
CCFLAGS=
CXXFLAGS=

# Fortran Compiler Flags
FFLAGS=

# Assembler Flags
ASFLAGS=

# Link Libraries and Options
LDLIBSOPTIONS=

# Build Targets
.build-conf: ${BUILD_SUBPROJECTS}
	"${MAKE}"  -f nbproject/Makefile-Debug.mk dist/Debug/Cygwin-Windows/c_project.exe

dist/Debug/Cygwin-Windows/c_project.exe: ${OBJECTFILES}
	${MKDIR} -p dist/Debug/Cygwin-Windows
	${LINK.c} -o ${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}/c_project ${OBJECTFILES} ${LDLIBSOPTIONS} 

${OBJECTDIR}/Source/stitch.o: Source/stitch.c 
	${MKDIR} -p ${OBJECTDIR}/Source
	${RM} $@.d
	$(COMPILE.c) -g -MMD -MP -MF $@.d -o ${OBJECTDIR}/Source/stitch.o Source/stitch.c

${OBJECTDIR}/Headers/Error.o: Headers/Error.h 
	${MKDIR} -p ${OBJECTDIR}/Headers
	${RM} $@.d
	$(COMPILE.c) -g -MMD -MP -MF $@.d -o ${OBJECTDIR}/Headers/Error.o Headers/Error.h

${OBJECTDIR}/Source/PPMImage.o: Source/PPMImage.c 
	${MKDIR} -p ${OBJECTDIR}/Source
	${RM} $@.d
	$(COMPILE.c) -g -MMD -MP -MF $@.d -o ${OBJECTDIR}/Source/PPMImage.o Source/PPMImage.c

${OBJECTDIR}/Headers/CS229Image.o: Headers/CS229Image.h 
	${MKDIR} -p ${OBJECTDIR}/Headers
	${RM} $@.d
	$(COMPILE.c) -g -MMD -MP -MF $@.d -o ${OBJECTDIR}/Headers/CS229Image.o Headers/CS229Image.h

${OBJECTDIR}/Source/imagestats.o: Source/imagestats.c 
	${MKDIR} -p ${OBJECTDIR}/Source
	${RM} $@.d
	$(COMPILE.c) -g -MMD -MP -MF $@.d -o ${OBJECTDIR}/Source/imagestats.o Source/imagestats.c

${OBJECTDIR}/Source/cs2ppm.o: Source/cs2ppm.c 
	${MKDIR} -p ${OBJECTDIR}/Source
	${RM} $@.d
	$(COMPILE.c) -g -MMD -MP -MF $@.d -o ${OBJECTDIR}/Source/cs2ppm.o Source/cs2ppm.c

${OBJECTDIR}/Headers/PPMImage.o: Headers/PPMImage.h 
	${MKDIR} -p ${OBJECTDIR}/Headers
	${RM} $@.d
	$(COMPILE.c) -g -MMD -MP -MF $@.d -o ${OBJECTDIR}/Headers/PPMImage.o Headers/PPMImage.h

${OBJECTDIR}/Source/CS229Image.o: Source/CS229Image.c 
	${MKDIR} -p ${OBJECTDIR}/Source
	${RM} $@.d
	$(COMPILE.c) -g -MMD -MP -MF $@.d -o ${OBJECTDIR}/Source/CS229Image.o Source/CS229Image.c

${OBJECTDIR}/Source/Defines.o: Source/Defines.c 
	${MKDIR} -p ${OBJECTDIR}/Source
	${RM} $@.d
	$(COMPILE.c) -g -MMD -MP -MF $@.d -o ${OBJECTDIR}/Source/Defines.o Source/Defines.c

${OBJECTDIR}/Source/darken.o: Source/darken.c 
	${MKDIR} -p ${OBJECTDIR}/Source
	${RM} $@.d
	$(COMPILE.c) -g -MMD -MP -MF $@.d -o ${OBJECTDIR}/Source/darken.o Source/darken.c

${OBJECTDIR}/Source/ppm2cs.o: Source/ppm2cs.c 
	${MKDIR} -p ${OBJECTDIR}/Source
	${RM} $@.d
	$(COMPILE.c) -g -MMD -MP -MF $@.d -o ${OBJECTDIR}/Source/ppm2cs.o Source/ppm2cs.c

${OBJECTDIR}/Source/lighten.o: Source/lighten.c 
	${MKDIR} -p ${OBJECTDIR}/Source
	${RM} $@.d
	$(COMPILE.c) -g -MMD -MP -MF $@.d -o ${OBJECTDIR}/Source/lighten.o Source/lighten.c

${OBJECTDIR}/Source/Error.o: Source/Error.c 
	${MKDIR} -p ${OBJECTDIR}/Source
	${RM} $@.d
	$(COMPILE.c) -g -MMD -MP -MF $@.d -o ${OBJECTDIR}/Source/Error.o Source/Error.c

${OBJECTDIR}/Headers/Defines.o: Headers/Defines.h 
	${MKDIR} -p ${OBJECTDIR}/Headers
	${RM} $@.d
	$(COMPILE.c) -g -MMD -MP -MF $@.d -o ${OBJECTDIR}/Headers/Defines.o Headers/Defines.h

${OBJECTDIR}/Source/makesquare.o: Source/makesquare.c 
	${MKDIR} -p ${OBJECTDIR}/Source
	${RM} $@.d
	$(COMPILE.c) -g -MMD -MP -MF $@.d -o ${OBJECTDIR}/Source/makesquare.o Source/makesquare.c

# Subprojects
.build-subprojects:

# Clean Targets
.clean-conf: ${CLEAN_SUBPROJECTS}
	${RM} -r build/Debug
	${RM} dist/Debug/Cygwin-Windows/c_project.exe

# Subprojects
.clean-subprojects:

# Enable dependency checking
.dep.inc: .depcheck-impl

include .dep.inc
