# Simple export text to excel
File transformation with separator in xlsx

### Motivation
Being able to automatically export in Excel formatted and thus being able to visualize it without the need to import and add the common properties in almost all Excel files

### Features
The project performs the file transformation with separator in Excel formatted adding the following properties:
- Auto filter on all columns
- Transformation of the first line into a header
- Convert text to number automatically
- Removal of spaces from the text between separators
- Auto format the file

### Usage: Simple export text to excel
Options:

``` usage
--input, -i -> Input file (always required) { String }

--output, -o -> Output file name (always required) { String }

--separator, -s [;] -> Separator of file { String }

--withoutData, -w [Without data] -> Value of empty input file { String }

--sheetName, -n [DATA] -> Name of sheet { String }

--locale, -l [pt_BR] -> Location of file for numbers { String }

--encoding, -e [Cp1252] -> Encoding of input file { String }

--debug, -d [true] -> Turn on debug mode

--help, -h -> Usage info
```

### Tecnologies
* kotlin
  * 1.6.10
* Apache Poi
  * 5.0.0
* Commons lang
  * 2.6
* kotlinx-cli
  * 0.3.4