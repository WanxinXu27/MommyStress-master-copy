import os
import sys
import numpy as np
import pandas as pd
from sklearn.neighbors import KNeighborsClassifier
from PASDAC.settings import SETTINGS


def fillinMatrix(smallArr, colIndicesArr, nCols):
    """
    In case prediction does not have contain all classes
    """
    
    nRows = smallArr.shape[0]
    fullArr = np.zeros([nRows, nCols])

    for i in range(colIndicesArr.size):
        fullArr[:,colIndicesArr[i]] = smallArr[:,i]

    return fullArr


def classification(trainData, trainLabels, testData):
    """Train model with trainData and trainLabels, then predict testLabels given testData.
    Output one hot representation and probability

    Parameters
    ----------
        trainingData:               dataFrame
        trainLabels:                dataFrame
        testData:                   dataFrame

    Return
    ------
        result:                     dataFrame
        probaDf:                    dataFrame

    """
    method = SETTINGS['CLASSIFIER']
    nClass = len(SETTINGS['CLASS_LABELS'])
    classLabels = SETTINGS['CLASS_LABELS']

    trainLabelsUnqArr = np.unique(trainLabels)

    if method == 'NaiveBayes':
        pass

    elif method == 'knnVoting':

        classifier = KNeighborsClassifier(5)
        model = classifier.fit(trainData, trainLabels)

        result = model.predict(testData)

        proba = model.predict_proba(testData)
        proba = fillinMatrix(proba, trainLabelsUnqArr, nClass)
        probaDf = pd.DataFrame(data=proba, columns=classLabels)

    return result, probaDf