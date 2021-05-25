Rory Little, Jesse Ericksen
CSCI 330, Summer 2018
Western Washington University

README.txt*

Installation Instructions:
-- Unzip contents of surly.zip into an empty folder
-- Open a terminal window from that folder
-- To build:  run `make`, or if GNU make is not installed,
    run the following commands:
        mkdir bin
        javac -d . src/*.java
        jar -cfm bin/Surly.jar src/manifest.txt *.class
        rm *.class
-- To test, place a file into the tests directory and run
    ./test <file name>
   Note that tests/prelude.surly is loaded in before any test files

Demo:
Our program will take a SURLY file as input (See below for SURLY commands)
To Run:
-- `./run <scripts>` or `java -jar bin/Surly.jar <scripts>`
     Scripts will be interpreted in the order listed in the command
     Options:
        -s, --silent    run the next file with non-verbose output
        --repl          execute the REPL, keeping the environment from the
                            scripts

-- To run on stdin, run with no args
-- Results will output to stdout
-- Errors will output to stderr

We have provided multiple test files in the tests folder for use. 

Additional/altered SURLY features:

Nested    Any time a non-altering operation requires a Relation as input, a
Queries:  query may be nested.
          
          Example: Gpas = SELECT (JOIN Student, Takes
                                  ON Student.id = Takes.id)
                          WHERE gpa > 3;



Aggregate   In a PROJECT query, any attribute may be wrapped by an aggrevate
Functions:  function to make it an aggregate project. Note that doing so will
            disallow any non-aggregate attribute from being included in the
            project. All aggregate functions output a value of type NUM 4.

            Example: AvgGpa = PROJECT COUNT(id), AVG(gpa) FROM Student;

            This will result in a relation having a schema of
            
            id.COUNT NUM 4, gpa.AVG NUM 4


Native    This SURLY implementation natively supports numbers, rather than
Numbers:  storing them as a String. Every number is stored as a 64 bit floating
          point number, regardless of type size information. The size of the
          type specifies how many decimals to round the number to when
          displaying it. Note that this means that even if a number displays as
          a whole integer, internally it may not be a whole integer.

