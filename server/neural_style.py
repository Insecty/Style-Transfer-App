import os

import numpy as np
import scipy.misc

from stylize import stylize

import math
from argparse import ArgumentParser

# default arguments
CONTENT_WEIGHT = 5e0
STYLE_WEIGHT = 5e1
TV_WEIGHT = 1e2
LEARNING_RATE = 1e1
STYLE_SCALE = 1.0
ITERATIONS = 1000
VGG_PATH = 'imagenet-vgg-verydeep-19.mat'



def train(content_pic,style_pic,train_iterations,output_pic):
    content_image = imread(content_pic)
    style_images = [imread(style_pic)]

    target_shape = content_image.shape

    for i in range(len(style_images)):
        style_scale = STYLE_SCALE
        style_images[i] = scipy.misc.imresize(style_images[i], style_scale *
                target_shape[1] / style_images[i].shape[1])

    style_blend_weights = [1.0/len(style_images) for _ in style_images]
    
    initial = None

    for iteration, image in stylize(
        network=VGG_PATH,  #
        initial=initial,
        content=content_image,
        styles=style_images,
        iterations=train_iterations,
        content_weight=CONTENT_WEIGHT,
        style_weight=STYLE_WEIGHT,
        style_blend_weights=style_blend_weights,
        tv_weight=TV_WEIGHT,
        learning_rate=LEARNING_RATE,
        print_iterations=None,
        checkpoint_iterations=None
    ):
        output_file = None
        if iteration is None:
            output_file = output_pic
        if output_file:
            imsave(output_file, image)


def imread(path):
    img = scipy.misc.imread(path).astype(np.float)
    if len(img.shape) == 2:
        # grayscale
        img = np.dstack((img,img,img))
    return img


def imsave(path, img):
    img = np.clip(img, 0, 255).astype(np.uint8)
    scipy.misc.imsave(path, img)


#if __name__ == '__main__':
#    train("./examples/1-content.jpg","./examples/1-style.jpg",500,"1-output.jpg")
