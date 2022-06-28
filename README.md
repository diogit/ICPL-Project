# Implementing a programming language and its interpreter and compiler

### Made by:

* [Daniel Flamino](https://github.com/DanielFlamino)
* Diogo Silvério (Self)

### Folder Content:
```
bin -> project files

src -> project files

tests -> project tests

HOALL.pdf -> language specification

TypecheckerTPC.pdf -> language specification

README.md -> this file

jasmin.jar -> assembler for the Java Virtual Machine
```


## Built With

* [Jasmin](http://jasmin.sourceforge.net) - an assembler for the Java Virtual Machine.


### Notes:

There are two modes: console and file.
* Console mode: Evaluates one program inputted into the console at a time
* File mode: Evaluates multiple programs from a single file

To enter file mode just provide a path to a test file. A premade file has been 
provided in /tests. Otherwise the program starts in console mode by default.

Usage: java Main "../tests/MultipleTests.txt"
