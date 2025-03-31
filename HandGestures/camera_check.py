import cv2
import mediapipe as mp
import numpy as np
import tensorflow.lite as tflite

# Load the trained TFLite model
model_path = "rock_paper_scissors_model/model.tflite"
interpreter = tflite.Interpreter(model_path=model_path)
interpreter.allocate_tensors()

# Get input and output tensors
tensor_details = interpreter.get_input_details()
input_shape = tensor_details[0]['shape']

# Initialize camera
cap = cv2.VideoCapture(0)

# MediaPipe Hands
mp_hands = mp.solutions.hands
mp_draw = mp.solutions.drawing_utils
hands = mp_hands.Hands(min_detection_confidence=0.5, min_tracking_confidence=0.5)

while cap.isOpened():
    ret, frame = cap.read()
    if not ret:
        break

    # Convert image to RGB (MediaPipe requires RGB)
    rgb_frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
    results = hands.process(rgb_frame)

    if results.multi_hand_landmarks:
        for hand_landmarks in results.multi_hand_landmarks:
            mp_draw.draw_landmarks(frame, hand_landmarks, mp_hands.HAND_CONNECTIONS)

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

            # Define gesture labels (adjust based on your dataset)
            gesture_labels = ["rock", "paper", "scissors"]  # Update with your labels
            gesture_name = gesture_labels[gesture_index] if gesture_index < len(gesture_labels) else "Unknown"

            # Display the result
            cv2.putText(frame, f'{gesture_name} ({confidence:.2f})', (50, 50),
                        cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2)

    # Show the frame
    cv2.imshow("Gesture Recognition", frame)

    # Press 'q' to exit
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()
