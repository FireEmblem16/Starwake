#Source directories
SOURCE = ./Source/
HEADERS = ./Headers/

#Output directories
OBJDIR = ./Objects/
BIN = ./bin/

#Special Lists
SIMOBJS = ${OBJDIR}Geometry.o ${OBJDIR}Item.o ${OBJDIR}Object.o ${OBJDIR}Property.o ${OBJDIR}Rock.o ${OBJDIR}Ball.o ${OBJDIR}Block.o ${OBJDIR}EarthRock.o ${OBJDIR}EnergyPill.o ${OBJDIR}Robot.o ${OBJDIR}RumulanRock.o ${OBJDIR}Hole.o ${OBJDIR}Jam.o ${OBJDIR}Lava.o ${OBJDIR}Mud.o ${OBJDIR}Water.o ${OBJDIR}RollingPlace.o ${OBJDIR}SoftPlace.o ${OBJDIR}Consumable.o ${OBJDIR}Unprobeable.o
SIMHDRS = ${HEADERS}Geometry.h ${HEADERS}Item.h ${HEADERS}Object.h ${HEADERS}Property.h ${HEADERS}Rock.h ${HEADERS}Ball.h ${HEADERS}Block.h ${HEADERS}EarthRock.h ${HEADERS}EnergyPill.h ${HEADERS}Robot.h ${HEADERS}RumulanRock.h ${HEADERS}Hole.h ${HEADERS}Jam.h ${HEADERS}Lava.h ${HEADERS}Mud.h ${HEADERS}Water.h ${HEADERS}RollingPlace.h ${HEADERS}SoftPlace.h ${HEADERS}Consumable.h ${HEADERS}Unprobeable.h

#Linking flags
ASMFLAGS = -O2

#Compiliation flags
CFLAGS = -c -O2

#Execution file output file extension [.exe for Windows, nothing for Linux]
EXEC_EXTENSION = 

#Build everything and report sucess to the user
all: directorys ${BIN}checkconf${EXEC_EXTENSION} ${BIN}PillAddict${EXEC_EXTENSION} ${BIN}generateconf${EXEC_EXTENSION} ${BIN}simulate${EXEC_EXTENSION} ${BIN}Sentry${EXEC_EXTENSION}
	@echo ""
	@echo "Build completed. Enjoy your new software."

#Create some dummy targets
checkconf: ${BIN}checkconf${EXEC_EXTENSION}
PillAddict: ${BIN}PillAddict${EXEC_EXTENSION}
Sentry: ${BIN}Sentry${EXEC_EXTENSION}
TriBot: ${BIN}TriBot${EXEC_EXTENSION}
generateconf: ${BIN}generateconf${EXEC_EXTENSION}
simulate: ${BIN}simulate${EXEC_EXTENSION}

#Ensure our output directories exist
directorys:
	mkdir -p "${OBJDIR}"
	mkdir -p "${BIN}"

#Make the checkconf executable
${BIN}checkconf${EXEC_EXTENSION}: directorys ${OBJDIR}checkconf.o ${OBJDIR}Entry.o ${OBJDIR}Simulation.o ${OBJDIR}XMLParser.o ${OBJDIR}Behaviour.o ${OBJDIR}Probeable.o ${OBJDIR}Stationary.o ${OBJDIR}HardPlace.o ${SIMOBJS}
	g++ ${ASMFLAGS} -o "${BIN}checkconf${EXEC_EXTENSION}" ${OBJDIR}checkconf.o ${OBJDIR}Entry.o ${OBJDIR}Simulation.o ${OBJDIR}XMLParser.o ${OBJDIR}Behaviour.o ${OBJDIR}Probeable.o ${OBJDIR}Stationary.o ${OBJDIR}HardPlace.o ${SIMOBJS}

${OBJDIR}checkconf.o: ${SOURCE}checkconf.cpp ${HEADERS}Defines.h ${HEADERS}Simulation.h ${HEADERS}XMLParser.h ${SIMHDRS}
	g++ ${CFLAGS} "${SOURCE}checkconf.cpp" -o "${OBJDIR}checkconf.o"
	
#Make the PillAddict executable
${BIN}PillAddict${EXEC_EXTENSION}: directorys ${OBJDIR}PillAddictExe.o ${OBJDIR}Robot.o ${OBJDIR}Target.o ${OBJDIR}PillAddict.o ${OBJDIR}MoveInfo.o ${OBJDIR}MessageReader.o ${OBJDIR}Command.o ${OBJDIR}Item.o ${OBJDIR}Behaviour.o ${OBJDIR}Probeable.o ${OBJDIR}Stationary.o ${OBJDIR}HardPlace.o ${OBJDIR}PillSeeker.o ${OBJDIR}Object.o
	g++ ${ASMFLAGS} -o "${BIN}PillAddict${EXEC_EXTENSION}" ${OBJDIR}PillAddictExe.o ${OBJDIR}Robot.o ${OBJDIR}Target.o ${OBJDIR}PillAddict.o ${OBJDIR}MoveInfo.o ${OBJDIR}MessageReader.o ${OBJDIR}Command.o ${OBJDIR}Item.o ${OBJDIR}Behaviour.o ${OBJDIR}Probeable.o ${OBJDIR}Stationary.o ${OBJDIR}HardPlace.o ${OBJDIR}SoftPlace.o ${OBJDIR}PillSeeker.o ${OBJDIR}Object.o

${OBJDIR}PillAddictExe.o: ${SOURCE}pill_addict.cpp ${HEADERS}Command.h ${HEADERS}MessageReader.h ${HEADERS}MoveInfo.h ${HEADERS}PillAddict.h ${HEADERS}Robot.h ${HEADERS}Target.h
	g++ ${CFLAGS} "${SOURCE}pill_addict.cpp" -o "${OBJDIR}PillAddictExe.o"

#Make the Sentry executable
${BIN}Sentry${EXEC_EXTENSION}: directorys ${OBJDIR}SentryBot.o ${OBJDIR}Robot.o ${OBJDIR}Target.o ${OBJDIR}Sentry.o ${OBJDIR}MoveInfo.o ${OBJDIR}MessageReader.o ${OBJDIR}Command.o ${OBJDIR}Item.o ${OBJDIR}Behaviour.o ${OBJDIR}Probeable.o ${OBJDIR}Stationary.o ${OBJDIR}HardPlace.o ${OBJDIR}Centurion.o ${OBJDIR}Object.o
	g++ ${ASMFLAGS} -o "${BIN}Sentry${EXEC_EXTENSION}" ${OBJDIR}SentryBot.o ${OBJDIR}Robot.o ${OBJDIR}Target.o ${OBJDIR}Sentry.o ${OBJDIR}MoveInfo.o ${OBJDIR}MessageReader.o ${OBJDIR}Command.o ${OBJDIR}Item.o ${OBJDIR}Behaviour.o ${OBJDIR}Probeable.o ${OBJDIR}Stationary.o ${OBJDIR}HardPlace.o ${OBJDIR}SoftPlace.o ${OBJDIR}Centurion.o ${OBJDIR}Object.o

${OBJDIR}SentryBot.o: ${SOURCE}sentrybot.cpp ${HEADERS}Command.h ${HEADERS}MessageReader.h ${HEADERS}MoveInfo.h ${HEADERS}Sentry.h ${HEADERS}Centurion.h ${HEADERS}Robot.h ${HEADERS}Target.h
	g++ ${CFLAGS} "${SOURCE}sentrybot.cpp" -o "${OBJDIR}SentryBot.o"
	
#Make the TriBot executable
${BIN}TriBot${EXEC_EXTENSION}: directorys

#Make the generateconf executable
${BIN}generateconf${EXEC_EXTENSION}: directorys ${OBJDIR}generateconf.o ${OBJDIR}Simulation.o ${OBJDIR}Entry.o ${OBJDIR}Behaviour.o ${OBJDIR}Probeable.o ${OBJDIR}Stationary.o ${OBJDIR}HardPlace.o ${SIMOBJS}
	g++ ${ASMFLAGS} -o "${BIN}generateconf${EXEC_EXTENSION}" ${OBJDIR}generateconf.o ${OBJDIR}Simulation.o ${OBJDIR}Entry.o ${OBJDIR}Behaviour.o ${OBJDIR}Probeable.o ${OBJDIR}Stationary.o ${OBJDIR}HardPlace.o ${SIMOBJS}

${OBJDIR}generateconf.o: ${SOURCE}generateconf.cpp ${HEADERS}Defines.h ${HEADERS}Entry.h ${HEADERS}Entry.h ${HEADERS}Simulation.h ${HEADERS}Probeable.h ${HEADERS}Stationary.h ${HEADERS}HardPlace.h ${SIMHDRS}
	g++ ${CFLAGS} "${SOURCE}generateconf.cpp" -o "${OBJDIR}generateconf.o"

#Make the simulate executable
${BIN}simulate${EXEC_EXTENSION}: directorys ${OBJDIR}simulate.o ${OBJDIR}Entry.o ${OBJDIR}Simulation.o ${OBJDIR}XMLParser.o ${OBJDIR}MoveInfo.o ${OBJDIR}MessageReader.o ${OBJDIR}Command.o ${OBJDIR}Behaviour.o ${OBJDIR}Probeable.o ${OBJDIR}Stationary.o ${OBJDIR}HardPlace.o ${OBJDIR}Target.o ${SIMOBJS}
	g++ ${ASMFLAGS} -o "${BIN}simulate${EXEC_EXTENSION}" ${OBJDIR}simulate.o ${OBJDIR}Entry.o ${OBJDIR}Simulation.o ${OBJDIR}XMLParser.o ${OBJDIR}MoveInfo.o ${OBJDIR}MessageReader.o ${OBJDIR}Command.o ${OBJDIR}Behaviour.o ${OBJDIR}Probeable.o ${OBJDIR}Stationary.o ${OBJDIR}HardPlace.o ${OBJDIR}Target.o ${SIMOBJS}

${OBJDIR}simulate.o: ${SOURCE}simulate.cpp ${HEADERS}Entry.h ${HEADERS}Simulation.h ${HEADERS}XMLParser.h ${HEADERS}MoveInfo.h ${HEADERS}MessageReader.h ${HEADERS}Command.h ${HEADERS}Item.h ${HEADERS}Behaviour.h ${HEADERS}Probeable.h ${HEADERS}Stationary.h ${HEADERS}HardPlace.h ${HEADERS}Target.h ${SIMHDRS}
	g++ ${CFLAGS} "${SOURCE}simulate.cpp" -o "${OBJDIR}simulate.o"
	
#Make object files
${OBJDIR}Entry.o: ${SOURCE}Entry.cpp ${HEADERS}Defines.h ${HEADERS}Item.h ${HEADERS}Entry.h
	g++ ${CFLAGS} "${SOURCE}Entry.cpp" -o "${OBJDIR}Entry.o"
	
${OBJDIR}Simulation.o: ${SOURCE}Simulation.cpp ${HEADERS}Defines.h ${HEADERS}Entry.h ${HEADERS}Simulation.h
	g++ ${CFLAGS} "${SOURCE}Simulation.cpp" -o "${OBJDIR}Simulation.o"
	
${OBJDIR}XMLParser.o: ${SOURCE}XMLParser.cpp ${SIMOBJS} ${HEADERS}XMLParser.h
	g++ ${CFLAGS} "${SOURCE}XMLParser.cpp" -o "${OBJDIR}XMLParser.o"

${OBJDIR}Geometry.o: ${SOURCE}Geometry.cpp ${HEADERS}Defines.h ${HEADERS}Object.h ${HEADERS}Geometry.h
	g++ ${CFLAGS} "${SOURCE}Geometry.cpp" -o "${OBJDIR}Geometry.o"
	
${OBJDIR}Item.o: ${SOURCE}Item.cpp ${HEADERS}Defines.h ${HEADERS}Item.h ${HEADERS}Behaviour.h
	g++ ${CFLAGS} "${SOURCE}Item.cpp" -o "${OBJDIR}Item.o"

${OBJDIR}Object.o: ${SOURCE}Object.cpp ${HEADERS}Defines.h ${HEADERS}Item.h ${HEADERS}Object.h
	g++ ${CFLAGS} "${SOURCE}Object.cpp" -o "${OBJDIR}Object.o"

${OBJDIR}Property.o: ${SOURCE}Property.cpp ${HEADERS}Defines.h ${HEADERS}Item.h ${HEADERS}Property.h ${HEADERS}Unprobeable.h
	g++ ${CFLAGS} "${SOURCE}Property.cpp" -o "${OBJDIR}Property.o"

${OBJDIR}Rock.o: ${SOURCE}Rock.cpp ${HEADERS}Defines.h ${HEADERS}Object.h ${HEADERS}Rock.h
	g++ ${CFLAGS} "${SOURCE}Rock.cpp" -o "${OBJDIR}Rock.o"

${OBJDIR}Ball.o: ${SOURCE}Ball.cpp ${HEADERS}Defines.h ${HEADERS}Geometry.h ${HEADERS}Ball.h ${HEADERS}RollingPlace.h
	g++ ${CFLAGS} "${SOURCE}Ball.cpp" -o "${OBJDIR}Ball.o"

${OBJDIR}Block.o: ${SOURCE}Block.cpp ${HEADERS}Defines.h ${HEADERS}Geometry.h ${HEADERS}Block.h ${HEADERS}SoftPlace.h
	g++ ${CFLAGS} "${SOURCE}Block.cpp" -o "${OBJDIR}Block.o"

${OBJDIR}EarthRock.o: ${SOURCE}EarthRock.cpp ${HEADERS}Defines.h ${HEADERS}Rock.h ${HEADERS}EarthRock.h
	g++ ${CFLAGS} "${SOURCE}EarthRock.cpp" -o "${OBJDIR}EarthRock.o"

${OBJDIR}EnergyPill.o: ${SOURCE}EnergyPill.cpp ${HEADERS}Defines.h ${HEADERS}Object.h ${HEADERS}EnergyPill.h ${HEADERS}Consumable.h
	g++ ${CFLAGS} "${SOURCE}EnergyPill.cpp" -o "${OBJDIR}EnergyPill.o"

${OBJDIR}Robot.o: ${SOURCE}Robot.cpp ${HEADERS}Defines.h ${HEADERS}Object.h ${HEADERS}Robot.h ${HEADERS}Behaviour.h ${HEADERS}SoftPlace.h
	g++ ${CFLAGS} "${SOURCE}Robot.cpp" -o "${OBJDIR}Robot.o"

${OBJDIR}RumulanRock.o: ${SOURCE}RumulanRock.cpp ${HEADERS}Defines.h ${HEADERS}Rock.h ${HEADERS}RumulanRock.h ${HEADERS}Unprobeable.h
	g++ ${CFLAGS} "${SOURCE}RumulanRock.cpp" -o "${OBJDIR}RumulanRock.o"

${OBJDIR}Hole.o: ${SOURCE}Hole.cpp ${HEADERS}Defines.h ${HEADERS}Property.h ${HEADERS}Hole.h
	g++ ${CFLAGS} "${SOURCE}Hole.cpp" -o "${OBJDIR}Hole.o"

${OBJDIR}Jam.o: ${SOURCE}Jam.cpp ${HEADERS}Defines.h ${HEADERS}Property.h ${HEADERS}Jam.h
	g++ ${CFLAGS} "${SOURCE}Jam.cpp" -o "${OBJDIR}Jam.o"

${OBJDIR}Lava.o: ${SOURCE}Lava.cpp ${HEADERS}Defines.h ${HEADERS}Property.h ${HEADERS}Lava.h
	g++ ${CFLAGS} "${SOURCE}Lava.cpp" -o "${OBJDIR}Lava.o"

${OBJDIR}Mud.o: ${SOURCE}Mud.cpp ${HEADERS}Defines.h ${HEADERS}Property.h ${HEADERS}Mud.h
	g++ ${CFLAGS} "${SOURCE}Mud.cpp" -o "${OBJDIR}Mud.o"

${OBJDIR}Water.o: ${SOURCE}Water.cpp ${HEADERS}Defines.h ${HEADERS}Property.h ${HEADERS}Water.h
	g++ ${CFLAGS} "${SOURCE}Water.cpp" -o "${OBJDIR}Water.o"

${OBJDIR}Behaviour.o: ${SOURCE}Behaviour.cpp ${HEADERS}Behaviour.h ${HEADERS}ProbeBehaviour.h ${HEADERS}Probeable.h ${HEADERS}MoveBehaviour.h ${HEADERS}Stationary.h ${HEADERS}CollisionBehaviour.h ${HEADERS}HardPlace.h ${HEADERS}MoveInfo.h
	g++ ${CFLAGS} "${SOURCE}Behaviour.cpp" -o "${OBJDIR}Behaviour.o"
	
${OBJDIR}Probeable.o: ${SOURCE}Probeable.cpp ${HEADERS}Probeable.h ${HEADERS}ProbeBehaviour.h
	g++ ${CFLAGS} "${SOURCE}Probeable.cpp" -o "${OBJDIR}Probeable.o"

${OBJDIR}Unprobeable.o: ${SOURCE}Unprobeable.cpp ${HEADERS}Unprobeable.h ${HEADERS}ProbeBehaviour.h
	g++ ${CFLAGS} "${SOURCE}Unprobeable.cpp" -o "${OBJDIR}Unprobeable.o"
	
${OBJDIR}Stationary.o: ${SOURCE}Stationary.cpp ${HEADERS}Stationary.h ${HEADERS}MoveBehaviour.h ${HEADERS}MoveInfo.h
	g++ ${CFLAGS} "${SOURCE}Stationary.cpp" -o "${OBJDIR}Stationary.o"
	
${OBJDIR}PillAddict.o: ${SOURCE}PillAddict.cpp ${HEADERS}PillAddict.h ${HEADERS}Behaviour.h ${HEADERS}PillSeeker.h
	g++ ${CFLAGS} "${SOURCE}PillAddict.cpp" -o "${OBJDIR}PillAddict.o"
	
${OBJDIR}PillSeeker.o: ${SOURCE}PillSeeker.cpp ${HEADERS}PillSeeker.h ${HEADERS}MoveBehaviour.h ${HEADERS}MoveInfo.h ${HEADERS}Target.h ${HEADERS}Defines.h
	g++ ${CFLAGS} "${SOURCE}PillSeeker.cpp" -o "${OBJDIR}PillSeeker.o"

${OBJDIR}Sentry.o: ${SOURCE}Sentry.cpp ${HEADERS}Sentry.h ${HEADERS}Behaviour.h ${HEADERS}Centurion.h
	g++ ${CFLAGS} "${SOURCE}Sentry.cpp" -o "${OBJDIR}Sentry.o"
	
${OBJDIR}Centurion.o: ${SOURCE}Centurion.cpp ${HEADERS}Centurion.h ${HEADERS}MoveBehaviour.h ${HEADERS}MoveInfo.h ${HEADERS}Target.h ${HEADERS}Defines.h
	g++ ${CFLAGS} "${SOURCE}Centurion.cpp" -o "${OBJDIR}Centurion.o"
	
${OBJDIR}HardPlace.o: ${SOURCE}HardPlace.cpp ${HEADERS}HardPlace.h ${HEADERS}CollisionBehaviour.h
	g++ ${CFLAGS} "${SOURCE}HardPlace.cpp" -o "${OBJDIR}HardPlace.o"
	
${OBJDIR}SoftPlace.o: ${SOURCE}SoftPlace.cpp ${HEADERS}SoftPlace.h ${HEADERS}CollisionBehaviour.h
	g++ ${CFLAGS} "${SOURCE}SoftPlace.cpp" -o "${OBJDIR}SoftPlace.o"
	
${OBJDIR}RollingPlace.o: ${SOURCE}RollingPlace.cpp ${HEADERS}RollingPlace.h ${HEADERS}CollisionBehaviour.h
	g++ ${CFLAGS} "${SOURCE}RollingPlace.cpp" -o "${OBJDIR}RollingPlace.o"
	
${OBJDIR}Consumable.o: ${SOURCE}Consumable.cpp ${HEADERS}Consumable.h ${HEADERS}CollisionBehaviour.h
	g++ ${CFLAGS} "${SOURCE}Consumable.cpp" -o "${OBJDIR}Consumable.o"
	
${OBJDIR}MessageReader.o: ${SOURCE}MessageReader.cpp ${HEADERS}MessageReader.h ${HEADERS}Command.h ${HEADERS}MoveInfo.h ${HEADERS}Defines.h
	g++ ${CFLAGS} "${SOURCE}MessageReader.cpp" -o "${OBJDIR}MessageReader.o"
	
${OBJDIR}Command.o: ${SOURCE}Command.cpp ${HEADERS}Command.h
	g++ ${CFLAGS} "${SOURCE}Command.cpp" -o "${OBJDIR}Command.o"
	
${OBJDIR}MoveInfo.o: ${SOURCE}MoveInfo.cpp ${HEADERS}MoveInfo.h ${HEADERS}Defines.h ${HEADERS}Target.h
	g++ ${CFLAGS} "${SOURCE}MoveInfo.cpp" -o "${OBJDIR}MoveInfo.o"
	
${OBJDIR}Target.o: ${SOURCE}Target.cpp ${HEADERS}Target.h ${HEADERS}Defines.h
	g++ ${CFLAGS} "${SOURCE}Target.cpp" -o "${OBJDIR}Target.o"
	
#Do any cleanup that is required
clean:
	@echo ""
	@echo "Removing all object files."
	rm -rf ${OBJDIR}

#Remove everything that was generated
cleandist:
	@echo ""
	@echo "Removing all files generated by this makefile."
	rm -rf ${OBJDIR}
	rm -rf ${BIN}