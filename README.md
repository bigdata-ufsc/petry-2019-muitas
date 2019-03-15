# Trajectory similarity

Trajectory similarity measures library. Similarity measures included:
* Edit Distance on Real sequence (EDR)
  * Chen, L., Özsu, M. T., & Oria, V. (2005, June). **Robust and fast similarity search for moving object trajectories.** In Proceedings of the 2005 ACM SIGMOD international conference on Management of data (pp. 491-502). ACM.
* Longest Common Subsequence (LCSS)
  * Vlachos, M., Kollios, G., & Gunopulos, D. (2002). **Discovering similar multidimensional trajectories.** In Data Engineering, 2002. Proceedings. 18th International Conference on (pp. 673-684). IEEE.
* Multidimensional Similarity Measure (MSM)
  * Furtado, A. S., Kopanaki, D., Alvares, L. O., & Bogorny, V. (2016). **Multidimensional similarity measuring for semantic trajectories.** Transactions in GIS, 20(2), 280-298.
* Multiple-Aspect Trajectory Similarity Measure (MUITAS)
  * Petry, L. M., Ferrero, C. A., Alvares, L. O., Renso, C., & Bogorny, V. (2019). **Towards Semantic-Aware Multiple-Aspect Trajectory Similarity Measuring.** Transactions in GIS (accepted), 23(X), XXX-XXX.


### Usage
```
usage: Trajectory Similarity [-h] [-s {LCSS,EDR,MSM,MUITAS}] [-d {true,false}] [-t THREADS] input output config

Compute distances/similarities of trajectories.

positional arguments:
  input                  trajectory file to compute distances/similarities
  output                 output file with the distance/similarity scores
  config                 configuration file

named arguments:
  -h, --help             show this help message and exit
  -s {LCSS,EDR,MSM,MUITAS}, --similarity {LCSS,EDR,MSM,MUITAS}
                         specify the similarity measure to use
  -d {true,false}, --compute-distances {true,false}
                         specify whether to compute distances (true) or similarities (false) (default: false)
  -t THREADS, --threads THREADS
                         the number of threads to be created for running the experiment (default: 1)
```

Here's a sample [config file](sample_config.json) and [data file](sample_data.csv).
