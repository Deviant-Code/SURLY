In this folder are tests to be run against the SURLY interpreter. All tests and
their desired outputs are stored as <name>.surly and <name>.out respectively.

The ./test utility may be used to run tests from this folder.

    Usage: ./test <options> [name...] where name is the name of the test
    Options:
        -b, --build     Build the project before testing
        -s, --set       Set the output files of the tests run
        -a, --all       Run all tests (*.surly)
        -d, --diff      Instead of printing the test output, simply tell if
                            there is any difference between the results of the
                            tests and the desired outputs


The following is a directory of all tests found in the folder:

prelude:
    Sets up a database for testing

simple:
    Tests RELATION, INSERT, PRINT, and DESTROY commands

query:
    Tests SELECT, JOIN, and DELETE commands

project:
    Tests PROJECT

