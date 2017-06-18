# poker-hand-sorter

Documentation

## Prerequisites
- Oracle JDK 1.8.0
- Leiningen 2.7.1

## Installation

- Clone this repository

## Usage
- Change the current directory to the project directory
- Run the following command in the linux shell

```bash
$cat poker-hands.txt | lein run
```

### Bugs
Currently it produces the following output against the test data file provided

```
Player 1: 256
Player 2: 244 
```

After fixing the high-card logic it should be:

```
Player 1: 263
Player 2: 237
```
