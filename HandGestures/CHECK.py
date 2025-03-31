import cv2
import torch
import os
from pathlib import Path
from models.common import DetectMultiBackend
import pathlib
temp = pathlib.PosixPath
pathlib.PosixPath = pathlib.WindowsPath

model_path = r"C:\Users\magda\OneDrive - Politechnika Wroclawska\Pulpit\Sem7\przemysl\lab5\Models\best3.pt"

if not os.path.exists(model_path):
    raise FileNotFoundError(f"Model file not found: {model_path}")

model = torch.hub.load('ultralytics/yolov5', 'custom', path=str(model_path), force_reload = True)


cap = cv2.VideoCapture(0)
if not cap.isOpened():
    print("Nie można otworzyć kamery.")
    exit()


while True:
    ret, frame = cap.read()
    if not ret:
        print("Nie można odczytać klatki z kamery.")
        break

    results = model(frame) 
    detections = results.xyxy[0]

    for *box, conf, cls in detections:
        x1, y1, x2, y2 = map(int, box)
        label = f"{model.names[int(cls)]} {conf:.2f}"

        cv2.rectangle(frame, (x1, y1), (x2, y2), (255, 0, 0), 2)
        cv2.putText(frame, label, (x1, y1 - 10), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 0, 0), 2)

    # Show the frame
    cv2.imshow('Frame', frame)

    # Exit on pressing 'q'
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

# Release resources
cap.release()
cv2.destroyAllWindows()
