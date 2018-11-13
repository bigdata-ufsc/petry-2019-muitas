from functools import lru_cache
from joblib import Parallel, delayed
import numpy as np


@lru_cache(maxsize=5)
def check_args(num_features, dist_functions, thresholds, weights=None,
               features=None):
    if len(dist_functions) != num_features:
        raise None
    elif len(thresholds) != num_features:
        raise None
    elif weights and len(weights) != num_features:
        raise None
    elif features and len(features) != num_features:
        raise None

    return True


def msm(t1, t2, dist_functions, thresholds, weights, features=None):
    check_args(num_features=len(t1[0]), dist_functions=dist_functions,
               weights=weights, thresholds=thresholds, features=features)

    def score(p1, p2):
        matches = np.zeros(len(p1))
        for i, _ in enumerate(p1):
            matches[i] = dist_functions[i](p1[i], p2[i]) <= thresholds[i]
        return np.sum(matches * weights)

    matrix = np.zeros(shape=(len(t1), len(t2)))
    compute_score = np.vectorize(score)

    for i, p1 in enumerate(t1):
        matrix[i] = compute_score(p1, t2)

    parity1 = np.sum(np.amax(matrix, axis=1))
    parity2 = np.sum(np.amax(np.transpose(matrix), axis=1))
    return (parity1 + parity2) / (len(t1) + len(t2))


def lcss(t1, t2, dist_functions, thresholds, features=None):
    check_args(num_features=len(t1[0]), dist_functions=dist_functions,
               thresholds=thresholds, features=features)

    def match(p1, p2):
        for i, _ in enumerate(p1):
            d = dist_functions[i](p1[i], p2[i])
            if d > thresholds[i]:
                break
        else:
            return True
        return False

    matrix = np.zeros(shape=(len(t1) + 1, len(t2) + 1))

    for i, p1 in enumerate(t1):
        for j, p2 in enumerate(t2):
            if match(p1, p2):
                matrix[i+1][j+1] = matrix[i][j] + 1
            else:
                matrix[i+1][j+1] = max(matrix[i+1][j], matrix[i][j+1])

    return matrix[len(t1)][len(t2)] / min(len(t1), len(t2))


def edr(t1, t2, dist_functions, thresholds, features=None):
    check_args(num_features=len(t1[0]), dist_functions=dist_functions,
               thresholds=thresholds, features=features)

    def match_cost(p1, p2):
        for i, _ in enumerate(p1):
            d = dist_functions[i](p1[i], p2[i])
            if d > thresholds[i]:
                break
        else:
            return 0
        return 1

    matrix = np.zeros(shape=(len(t1) + 1, len(t2) + 1))
    matrix[:, 0] = np.r_[0:len(t1)+1]
    matrix[0] = np.r_[0:len(t2)+1]

    for i, p1 in enumerate(t1):
        for j, p2 in enumerate(t2):
            cost = match_cost(p1, p2)
            matrix[i+1][j+1] = min(matrix[i][j] + cost,
                                   min(matrix[i+1][j] + 1, matrix[i][j+1] + 1))

    return 1 - matrix[len(t1)][len(t2)] / max(len(t1), len(t2))


def pairwise_similarity(X, Y=None, measure=msm, n_jobs=1, **kwargs):
    """Compute the similarity between trajectories in X and Y.

    Parameters
    ----------
    X : ndarray, shape: (n_trajectories_X, n_points, n_features)
        Input data.
    Y : ndarray, shape: (n_trajectories_Y, n_features)
        Input data. If ``None``, the output will be the pairwise
        similarities between all samples in ``X``.
    measure : callable (default=msm)
        The similarity measure to use for computing similarities.
    n_jobs : int (default=1)
        The number of parallel jobs.
    **kwargs : optional measure parameters
        Parameters to pass to the similarity measure.

    Returns
    -------
    similarities : array
        An array with shape (n_trajectories_X, n_points, n_trajectories_Y).
    """
    check_args(num_features=len(X[0][0]), **kwargs)

    Y = X if not Y else Y
    similarity = np.zeros(shape=(len(X), len(Y)))

    for i, t1 in enumerate(X):
        similarity[i] = Parallel(n_jobs=n_jobs)(
            delayed(measure)(t1, t2, **kwargs) for t2 in Y)

    return similarity
