# INSTALL


1. [Recommended] Install [Anaconda Python](https://www.anaconda.com/download). Create a new conda environment and activate it:

```
conda create -n py34 python=3.4 anaconda
source activate py34
```

2. Install libraries. If you are using anaconda, double check that the environment is activated.

```
pip install -r requirements.txt
```

3. Modify `PASDAC/config.yaml` if needed.

4. [Optional] Start Spyder IDE. Make sure that correct environment (if using conda) is activated:

```
spyder 1_generate_features.py
```

5. Run ` python 1_generate_features.py` to segment data and perform feature extractions. Data will be saved as either pickle file or csv files in output folder (change the settings in `config.yaml`).

Expected output on the screen:

```
=========================================
Processing data of Subject 1
Data2R/subject1_gesture_data.csv
Data2R/subject1_gesture_segment_labels.csv
Boxcar smoothing with winsize 30
Sliding window with win size 1.00 second and step size 0.10 second
Feature type: VerySimple
Number of segments 1910
Processing 0% of segments
Processing 52% of segments
=========================================
Processing data of Subject 2
Data2R/subject2_gesture_data.csv
Data2R/subject2_gesture_segment_labels.csv
Boxcar smoothing with winsize 30
Sliding window with win size 1.00 second and step size 0.10 second
Feature type: VerySimple
Number of segments 2870
Processing 0% of segments
Processing 34% of segments
Processing 69% of segments
=========================================
Saving data to Output
```


6. Run `python 2_evaluation.py` to perform either personalized or generalized evaluation. ROC curves will be generated.

Expected output on the screen:

```
=========================================
Loading data from Output
Generalized evaluation
Leave subject 1 out
Plotting ROC
Confusion matrix for segments:
[[779   2  10   9   0 108   4   0  44   1   0   0]
 [ 40   3   4   8   8   1   5   0   3   0   0   0]
 [ 26  24  24   8   7   0   0   0   2   0   0   0]
 [ 44   4  10  21  12   3   3   0   2   0   0   0]
 [ 68   0   3  14   4   0   0   0   2   0   0   0]
 [ 53   0   0   8   0  26   3   0   1   0   0   0]
 [ 28   0   0  30   0   9  17   0  13   0   0   0]
 [ 82   0   0   0   0  15   3   6   5   0   0   0]
 [ 81   0   0   9   0   8  28   0  24   0   0   0]
 [ 25   0   0   0   0   0   0   0   0  12   0  17]
 [ 20   0   0   0   0   0   0   0   0   9  22   0]
 [ 16   0   0   0   0   0   0   0   0   7   0  23]]
Leave subject 2 out
Plotting ROC
Confusion matrix for segments:
[[836  48  13  40  77  24  44 723 132  21  17  31]
 [  2   2  36   3   4   0   0   0   1   0   0   0]
 [  6   7  62   8   6   0   3   0   0   0   0   0]
 [ 20   1   0  24   5   0  14   0  27   0   0   0]
 [  2   8  28   5   9   0   0   0   4   0   0   0]
 [ 41   0   3  12   0  20  10  14  35   0   0   0]
 [  8   4   0  15   0   0  57   2  15   0   0   0]
 [  4   0   0   0   0   0   0 114   4   0   0   0]
 [ 43   0   0   0   0   0   2   8  73   0   0   0]
 [  5   0   0   0   0   0   0   1   0  16   0  18]
 [  1   0   0   0   0   0   0   0   0   0  20   5]
 [  0   0   0   0   0   0   0   0   0   8   1  18]]
```

ROC figures are saved in output folders

7. Outputs on the terminal are logged into `features.log` and `evaluation.log` in output folder.
