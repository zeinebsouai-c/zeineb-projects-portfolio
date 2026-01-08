#!/usr/bin/env python
# coding: utf-8

# # Feature Engineering

# In[22]:


# loading cleaned data and filling missing values

import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.preprocessing import LabelEncoder
from scipy.sparse import hstack
import joblib

# loading the cleaned dataset (with sentiment already included)
df = pd.read_csv("../data/mental_health_data_with_sentiment.csv")
df.head()

# replacing NaNs with empty strings (in case any snuck through)
df["selftext"] = df["selftext"].fillna("")
df["length"] = df["selftext"].str.len()
df["sentiment"] = df["sentiment"].fillna(0)


# In[23]:


# TF-IDF Vectorization

vectorizer = TfidfVectorizer(
    max_features=3000,
    stop_words="english",
    ngram_range=(1, 2),
)

X_text = vectorizer.fit_transform(df["selftext"])


# In[24]:


# combining TF-IDF with extra features (length and sentiment)

from scipy.sparse import hstack

# creating a matrix of non-text features
X_meta = df[["length", "sentiment"]].values
X_full = hstack([X_text, X_meta])


# In[25]:


# encoding labels (subreddit category)
le = LabelEncoder()
y = le.fit_transform(df["label"])


# In[26]:


# saving features for modeling

joblib.dump(vectorizer, "../data/tfidf_vectorizer.pkl")
joblib.dump(X_full, "../data/features_sparse.pkl")
joblib.dump(y, "../data/labels.pkl")

print("Feature matrix and labels saved.")


# In[27]:


# inspecting shape

print("TF-IDF matrix shape:", X_text.shape)
print("Final feature matrix shape:", X_full.shape)
print("Labels shape:", y.shape)

