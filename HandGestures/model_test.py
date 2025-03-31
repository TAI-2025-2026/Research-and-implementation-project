import cv2
import mediapipe as mp
import numpy as np
import tensorflow.lite as tflite
import os

# Load the trained TFLite model
model_path = "rock_paper_scissors_model/model.tflite"
interpreter = tflite.Interpreter(model_path=model_path)
interpreter.allocate_tensors()

# Get input and output tensors
tensor_details = interpreter.get_input_details()

# MediaPipe Hands
mp_hands = mp.solutions.hands
mp_draw = mp.solutions.drawing_utils
hands = mp_hands.Hands(min_detection_confidence=0.5, min_tracking_confidence=0.5)

# Define gesture labels
gesture_labels = ["Kamien", "Nozyce", "Papier", "none"]  # Update with your labels

# Folder containing gesture subfolders
IMAGES_PATH = "Fotki"
correct_predictions = 0
total_predictions = 0

# Iterate over each folder (representing gestures)
for gesture_folder in os.listdir(IMAGES_PATH):
    gesture_folder_path = os.path.join(IMAGES_PATH, gesture_folder)

    # Continue only if the folder is valid (skip files)
    if not os.path.isdir(gesture_folder_path):
        continue

    # Iterate over all images in the folder
    for image_name in os.listdir(gesture_folder_path):
        image_path = os.path.join(gesture_folder_path, image_name)
        frame = cv2.imread(image_path)
        if frame is None:
            continue

        # Convert image to RGB (MediaPipe requires RGB)
        rgb_frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
        results = hands.process(rgb_frame)

        if results.multi_hand_landmarks:
            for hand_landmarks in results.multi_hand_landmarks:
                # Extract hand landmarks
                hand_data = np.array([[lm.x, lm.y, lm.z] for lm in hand_landmarks.landmark], dtype=np.float32).flatten()

                # Pad with zeros if needed to match the expected input shape
                if len(hand_data) < 128:
                    hand_data = np.pad(hand_data, (0, 128 - len(hand_data)), mode='constant')

                # Reshape and prepare input data
                input_data = np.expand_dims(hand_data, axis=0).astype(np.float32)

                # Perform gesture recognition
                interpreter.set_tensor(tensor_details[0]['index'], input_data)
                interpreter.invoke()
                output_details = interpreter.get_output_details()
                output_data = interpreter.get_tensor(output_details[0]['index'])

                # Get the predicted gesture
                gesture_index = np.argmax(output_data)
                confidence = np.max(output_data)
                gesture_name = gesture_labels[gesture_index] if gesture_index < len(gesture_labels) else "Unknown"

                # Check if prediction is correct (compare folder name with predicted label)
                if gesture_name.lower() == gesture_folder.lower():
                    correct_predictions += 1
                total_predictions += 1

# Print accuracy
accuracy = (correct_predictions / total_predictions) * 100 if total_predictions > 0 else 0
print(f"Model Accuracy: {accuracy:.2f}%")
