SRC = $(wildcard src/*.java)
MANIFEST = src/manifest.txt

$(shell mkdir -p bin)

bin/Surly.jar : $(SRC) $(MANIFEST)
	javac -d . src/*.java 
	jar cfm bin/Surly.jar src/manifest.txt *.class
	rm *.class

clean :
	rm bin/Surly.jar
