import cv2
import numpy as np
import os

def process_image(image_path, width, height):
    img = cv2.imread(image_path)
    img = cv2.resize(img, (width, height))
    blue, green, red = cv2.split(img) 
    red_array = red / 255.0
    green_array = green / 255.0
    blue_array = blue / 255.0
    

    return red_array, green_array, blue_array 
    

def save_rgb_arrays_to_file(red_array, green_array, blue_array, file_path, index):
    with open(file_path, "w") as f:
        
        f.write(f"{index} 1\n") #1 if bourbon, 0 if not

        for row in red_array:
            f.write(" ".join(f"{value:.5f}" for value in row) + "\n")
        f.write("\n")

        for row in green_array:
            f.write(" ".join(f"{value:.5f}" for value in row) + "\n")
        f.write("\n")

        for row in blue_array:
            f.write(" ".join(f"{value:.5f}" for value in row) + "\n")

def process_images_in_folder(folder_path, width, height, output_folder):
    image_files = [f for f in os.listdir(folder_path) if f.lower().endswith(('.png', '.jpg', '.jpeg'))]
    start_index = 0
    for i, image_file in enumerate(image_files):
        image_path = os.path.join(folder_path, image_file)
        red_array, green_array, blue_array = process_image(image_path, width, height)
        output_file_path = os.path.join(output_folder, f"TrainingElement{start_index + i}.txt")
        
        save_rgb_arrays_to_file(red_array, green_array, blue_array, output_file_path, start_index + i)
        print(f"RGB arrays saved to: {output_file_path}")

folder_path = "/Users/yourname/Downloads/bourbon"
width, height = 500, 500  # Standard dimensions
downloads_folder = os.path.expanduser("~/Downloads/bourbonarrays")

process_images_in_folder(folder_path, width, height, downloads_folder)


