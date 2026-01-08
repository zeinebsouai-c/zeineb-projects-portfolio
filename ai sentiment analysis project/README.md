# Reading Between The Lines: Classifying Mental States from Text

## Problem and Goal

Signs of mental health conditions are often expressed through language, in writing, tone or emotional cues. But many people
do not describe their symptoms explicitly or seek help directly. This creates a challenge: How can we detect mental health 
distress from subtle emotional signals in text or transcribed speech.

The goal of this project is to predict the mental health state or emotional context behind a person's writing, based on 
speech patterns and emotional tones.
Instead of relying on explicit mentions of diagnoses and symptoms, the system focuses on sentiment and linguistic style to 
detect subtle details of underlying mental health conditions like depression, anxiety and bipolar disorder.
The idea was to create a text classifier that can map emotional writing to a likely mental health context.

This system can serve as a supportive tool, offering insight for people who are seeking self-understanding, or for 
clinicians analyzing emotional expression in written communication.

For training data I used reddit as a real-life source of emotionally rich, natural language. It was used not as a target to
predict but as reflection of people's internal states shared online.

While this model was built as a proof of concept and not deployed in a public tool, it’s important to note that any 
emotional insights drawn from such systems should never replace professional diagnosis or treatment. If used for 
self-reflection, the results should be considered carefully and ideally discussed with a qualified mental health 
professional.