from joblib import Parallel, delayed
import numpy as np


def msm(t1, t2, dist_functions, thresholds, features=None, weights=None):
    pass


def lcss(t1, t2, dist_functions, thresholds, features=None):
    pass


def edr(t1, t2, dist_functions, thresholds, features=None):
    pass


def pairwise_similarity(X, Y=None, measure=msm, n_jobs=1, **kwargs):
    """Compute the similarity between trajectories in X and Y.

    Parameters
    ----------
    X : ndarray, shape: (n_trajectories_X, n_features)
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
        An array with shape (n_trajectories_X, n_trajectories_Y).
    """
    Y = X if not Y else Y
    similarity = np.zeros(shape=(len(X), len(Y)))

    for i, t1 in enumerate(X):
        similarity[i] = Parallel(n_jobs=n_jobs)(
            delayed(measure)(t1, t2, **kwargs) for t2 in Y)

    return similarity
