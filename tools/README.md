# Evaluation Tools

### Usage
```
usage: Evaluation Tools [-h] [-m [{MAP,MRR,HCA} [{MAP,MRR,HCA} ...]]] [-d {true,false}] [-e EXT] [-c CLASS_FILE] input output

Evaluate results of similarity measures.

positional arguments:
  input                  folder or file to evaluate
  output                 output file with the results

named arguments:
  -h, --help             show this help message and exit
  -m [{MAP,MRR,HCA} [{MAP,MRR,HCA} ...]], --metric [{MAP,MRR,HCA} [{MAP,MRR,HCA} ...]]
                         specify the metric(s) of evaluation to compute
  -d {true,false}, --distances {true,false}
                         specify whether the matrix contains distances (true) or similarities (false) (default: true)
  -e EXT, --ext EXT      specify the input file(s) extension (default: csv)
  -c CLASS_FILE, --class-file CLASS_FILE
                         specify the file containing the class dictionary according to the matrix header
```
