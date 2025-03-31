from mediapipe_model_maker import gesture_recognizer
import tensorflow as tf
import matplotlib.pyplot as plt
import cv2
import os

NUM_EXAMPLES = 5
IMAGES_PATH = 'Fotki'

labels = []
for i in os.listdir(IMAGES_PATH):
    if os.path.isdir(os.path.join(IMAGES_PATH, i)):
        labels.append(i)

# for label in labels:
#     label_dir = os.path.join(IMAGES_PATH, label)
#     example_filenames = os.listdir(label_dir)[:NUM_EXAMPLES]
#     fig, axs = plt.subplots(1, NUM_EXAMPLES, figsize=(10, 2))
#     for i in range(NUM_EXAMPLES):
#         axs[i].imshow(plt.imread(os.path.join(label_dir, example_filenames[i])))
#         axs[i].get_xaxis().set_visible(False)
#         axs[i].get_yaxis().set_visible(False)
#     fig.suptitle(f'{label} examples')
#
# plt.show()

data = gesture_recognizer.Dataset.from_folder(
    dirname=IMAGES_PATH,
    hparams=gesture_recognizer.HandDataPreprocessingParams()
)

train_data, rest_data = data.split(0.8)
validation_data, test_data = rest_data.split(0.5)

hparams = gesture_recognizer.HParams(export_dir="rock_paper_scissors_model")
options = gesture_recognizer.GestureRecognizerOptions(hparams=hparams)
model = gesture_recognizer.GestureRecognizer.create(
    train_data=train_data,
    validation_data=validation_data,
    options=options
)

loss, acc = model.evaluate(test_data, batch_size=1)
print(f"Test loss:{loss}, Test accuracy:{acc}")

# After training your model
# Save the model using TensorFlow's SavedModel format
model_path = "rock_paper_scissors_model/saved_model"
model._model.save(model_path)

# For TFLite conversion (which would be useful for mobile deployment)
converter = tf.lite.TFLiteConverter.from_saved_model(model_path)
tflite_model = converter.convert()

# Save the TFLite model
with open('rock_paper_scissors_model/model.tflite', 'wb') as f:
    f.write(tflite_model)