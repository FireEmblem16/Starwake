all: bin Objects bin/sinfo bin/gendtmf bin/gentest1.exe bin/gentest2.exe bin/gentest3.exe bin/gentest4.exe bin/info.exe bin/gensine.exe bin/split.exe bin/combine.exe bin/static.exe bin/mix.exe bin/mixstaticvolume.exe bin/convertsu.exe bin/convertwav.exe bin/append.exe bin/ftransform.exe bin/rms.exe bin/scale.exe bin/GentestBattery.exe bin/MixBattery.exe bin/GensineBattery.exe bin/analysis.exe bin/Part1Battery.exe bin/Part2Battery.exe
	
clean:
	rm -rf Objects

cleandist:
	rm -rf Objects
	rm -rf bin

bin:
	mkdir bin
	
Objects:
	mkdir Objects
	
bin/sinfo: sinfo
	cp sinfo bin/sinfo
	
bin/gendtmf: gendtmf
	cp gendtmf bin/gendtmf

bin/gentest1.exe: Objects/gentest1.o Objects/suio.o Objects/util.o
	gcc -pedantic -o bin/gentest1.exe Objects/gentest1.o Objects/suio.o Objects/util.o
	
bin/gentest2.exe: Objects/gentest2.o Objects/suio.o Objects/util.o
	gcc -pedantic -o bin/gentest2.exe Objects/gentest2.o Objects/suio.o Objects/util.o
	
bin/gentest3.exe: Objects/gentest3.o Objects/suio.o Objects/util.o
	gcc -pedantic -o bin/gentest3.exe Objects/gentest3.o Objects/suio.o Objects/util.o

bin/gentest4.exe: Objects/gentest4.o Objects/suio.o Objects/util.o
	gcc -pedantic -o bin/gentest4.exe Objects/gentest4.o Objects/suio.o Objects/util.o

bin/info.exe: Objects/info.o Objects/suio.o Objects/wavio.o Objects/util.o
	gcc -pedantic -o bin/info.exe Objects/info.o Objects/suio.o Objects/wavio.o Objects/util.o

bin/gensine.exe: Objects/gensine.o Objects/suio.o Objects/util.o
	gcc -pedantic -o bin/gensine.exe Objects/gensine.o Objects/suio.o Objects/util.o
	
bin/split.exe: Objects/split.o Objects/suio.o Objects/util.o
	gcc -pedantic -o bin/split.exe Objects/split.o Objects/suio.o Objects/util.o
	
bin/combine.exe: Objects/combine.o Objects/suio.o Objects/util.o
	gcc -pedantic -o bin/combine.exe Objects/combine.o Objects/suio.o Objects/util.o
	
bin/static.exe: Objects/static.o Objects/suio.o Objects/util.o
	gcc -pedantic -o bin/static.exe Objects/static.o Objects/suio.o Objects/util.o
	
bin/mix.exe: Objects/mix.o Objects/suio.o Objects/util.o
	gcc -pedantic -o bin/mix.exe Objects/mix.o Objects/suio.o Objects/util.o
	
bin/mixstaticvolume.exe: Objects/mixstaticvolume.o Objects/suio.o Objects/util.o
	gcc -pedantic -o bin/mixstaticvolume.exe Objects/mixstaticvolume.o Objects/suio.o Objects/util.o
	
bin/convertsu.exe: Objects/convertsu.o Objects/suio.o Objects/wavio.o Objects/soundconvert.o Objects/util.o
	gcc -pedantic -o bin/convertsu.exe Objects/convertsu.o Objects/suio.o Objects/wavio.o Objects/soundconvert.o Objects/util.o
	
bin/convertwav.exe: Objects/convertwav.o Objects/suio.o Objects/wavio.o Objects/soundconvert.o Objects/util.o
	gcc -pedantic -o bin/convertwav.exe Objects/convertwav.o Objects/suio.o Objects/wavio.o Objects/soundconvert.o Objects/util.o

bin/append.exe: Objects/append.o Objects/suio.o Objects/util.o
	gcc -pedantic -o bin/append.exe Objects/append.o Objects/suio.o Objects/util.o
	
bin/ftransform.exe: Objects/ftransform.o Objects/suio.o Objects/util.o
	gcc -pedantic -o bin/ftransform.exe Objects/ftransform.o Objects/suio.o Objects/util.o
	
bin/rms.exe: Objects/rms.o Objects/suio.o Objects/util.o
	gcc -pedantic -o bin/rms.exe Objects/rms.o Objects/suio.o Objects/util.o
	
bin/scale.exe: Objects/scale.o Objects/suio.o Objects/util.o
	gcc -pedantic -o bin/scale.exe Objects/scale.o Objects/suio.o Objects/util.o
	
bin/GentestBattery.exe: Objects/battery.o Objects/suio.o Objects/util.o
	gcc -pedantic -o bin/GentestBattery.exe Objects/battery.o Objects/suio.o Objects/util.o
	
bin/MixBattery.exe: Objects/mbattery.o Objects/suio.o Objects/util.o
	gcc -pedantic -o bin/MixBattery.exe Objects/mbattery.o Objects/suio.o Objects/util.o
	
bin/GensineBattery.exe: Objects/gbattery.o Objects/suio.o Objects/util.o
	gcc -pedantic -o bin/GensineBattery.exe Objects/gbattery.o Objects/suio.o Objects/util.o

bin/analysis.exe: Objects/analysis.o Objects/suio.o Objects/wavio.o Objects/soundanalysis.o Objects/util.o
	gcc -pedantic -o bin/analysis.exe Objects/analysis.o Objects/suio.o Objects/wavio.o Objects/soundanalysis.o Objects/util.o
	
bin/Part1Battery.exe: Objects/Part1Battery.o Objects/util.o Objects/crawler.o Objects/datastructures.o
	gcc -pedantic -o bin/Part1Battery.exe Objects/Part1Battery.o Objects/util.o Objects/crawler.o Objects/datastructures.o
	
bin/Part2Battery.exe: Objects/Part2Battery.o Objects/util.o Objects/crawler.o Objects/datastructures.o
	gcc -pedantic -o bin/Part2Battery.exe Objects/Part2Battery.o Objects/util.o Objects/crawler.o Objects/datastructures.o

Objects/gentest1.o: Source/gentest1.c Headers/defines.h Headers/suio.h
	gcc -pedantic -c -o Objects/gentest1.o Source/gentest1.c
	
Objects/gentest2.o: Source/gentest2.c Headers/defines.h Headers/suio.h
	gcc -pedantic -c -o Objects/gentest2.o Source/gentest2.c
		
Objects/gentest3.o: Source/gentest3.c Headers/defines.h Headers/suio.h
	gcc -pedantic -c -o Objects/gentest3.o Source/gentest3.c
	
Objects/gentest4.o: Source/gentest4.c Headers/defines.h Headers/suio.h
	gcc -pedantic -c -o Objects/gentest4.o Source/gentest4.c
	
Objects/info.o: Source/info.c Headers/defines.h Headers/suio.h
	gcc -pedantic -c -o Objects/info.o Source/info.c
	
Objects/gensine.o: Source/gensine.c Headers/defines.h Headers/suio.h Headers/util.h
	gcc -pedantic -c -o Objects/gensine.o Source/gensine.c
	
Objects/split.o: Source/split.c Headers/defines.h Headers/suio.h
	gcc -pedantic -c -o Objects/split.o Source/split.c	
	
Objects/combine.o: Source/combine.c Headers/defines.h Headers/suio.h
	gcc -pedantic -c -o Objects/combine.o Source/combine.c
	
Objects/static.o: Source/static.c Headers/defines.h Headers/suio.h
	gcc -pedantic -c -o Objects/static.o Source/static.c
	
Objects/mix.o: Source/mix.c Headers/defines.h Headers/suio.h
	gcc -pedantic -c -o Objects/mix.o Source/mix.c
	
Objects/mixstaticvolume.o: Source/mixstaticvolume.c Headers/defines.h Headers/suio.h
	gcc -pedantic -c -o Objects/mixstaticvolume.o Source/mixstaticvolume.c
	
Objects/convertsu.o: Source/convertsu.c Headers/defines.h Headers/suio.h Headers/wavio.h Headers/soundconvert.h
	gcc -pedantic -c -o Objects/convertsu.o Source/convertsu.c
	
Objects/convertwav.o: Source/convertwav.c Headers/defines.h Headers/suio.h Headers/wavio.h Headers/soundconvert.h
	gcc -pedantic -c -o Objects/convertwav.o Source/convertwav.c

Objects/append.o: Source/append.c Headers/defines.h Headers/suio.h
	gcc -pedantic -c -o Objects/append.o Source/append.c
	
Objects/ftransform.o: Source/ftransform.c Headers/defines.h Headers/suio.h Headers/util.h
	gcc -pedantic -c -o Objects/ftransform.o Source/ftransform.c
	
Objects/rms.o: Source/rms.c Headers/defines.h Headers/suio.h Headers/util.h
	gcc -pedantic -c -o Objects/rms.o Source/rms.c
	
Objects/scale.o: Source/scale.c Headers/defines.h Headers/suio.h Headers/util.h
	gcc -pedantic -c -o Objects/scale.o Source/scale.c

Objects/battery.o: Source/battery.c Headers/defines.h Headers/suio.h Headers/util.h
	gcc -pedantic -c -o Objects/battery.o Source/battery.c
	
Objects/mbattery.o: Source/mbattery.c Headers/defines.h Headers/suio.h Headers/util.h
	gcc -pedantic -c -o Objects/mbattery.o Source/mbattery.c

Objects/gbattery.o: Source/gbattery.c Headers/defines.h Headers/suio.h Headers/util.h
	gcc -pedantic -c -o Objects/gbattery.o Source/gbattery.c
	
Objects/analysis.o: Source/analysis.c Headers/defines.h Headers/suio.h Headers/wavio.h Headers/soundanalysis.h Headers/util.h
	gcc -pedantic -c -o Objects/analysis.o Source/analysis.c
	
Objects/Part1Battery.o: Source/Part1Battery.c Headers/defines.h Headers/util.h Headers/crawler.h Headers/datastructures.h
	gcc -pedantic -c -o Objects/Part1Battery.o Source/Part1Battery.c
	
Objects/Part2Battery.o: Source/Part2Battery.c Headers/defines.h Headers/util.h Headers/crawler.h Headers/datastructures.h
	gcc -pedantic -c -o Objects/Part2Battery.o Source/Part2Battery.c
	
Objects/suio.o: Source/suio.c Headers/suio.h Headers/defines.h Headers/util.h
	gcc -pedantic -c -o Objects/suio.o Source/suio.c

Objects/wavio.o: Source/wavio.c Headers/wavio.h Headers/defines.h Headers/util.h
	gcc -pedantic -c -o Objects/wavio.o Source/wavio.c
	
Objects/util.o: Source/util.c Headers/util.h Headers/defines.h
	gcc -pedantic -c -o Objects/util.o Source/util.c
	
Objects/soundconvert.o: Source/soundconvert.c Headers/soundconvert.h Headers/suio.h Headers/wavio.h
	gcc -pedantic -c -o Objects/soundconvert.o Source/soundconvert.c
	
Objects/soundanalysis.o: Source/soundanalysis.c Headers/defines.h Headers/util.h
	gcc -pedantic -c -o Objects/soundanalysis.o Source/soundanalysis.c
	
Objects/crawler.o: Source/crawler.c Headers/defines.h
	gcc -pedantic -c -o Objects/crawler.o Source/crawler.c
	
Objects/datastructures.o: Source/datastructures.c Headers/defines.h
	gcc -pedantic -c -o Objects/datastructures.o Source/datastructures.c