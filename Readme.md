## Running

python init.py

For a 512×512 pixel content file, 200 iterations take 5 seconds on a GTX 1080 Ti, 10 seconds on a Maxwell Titan X, or 10 minutes on an Intel Core i7-5930K. Using a GPU is highly recommended due to the huge speedup.



## Requirements

### Data Files

- [Pre-trained VGG network](http://www.vlfeat.org/matconvnet/models/beta16/imagenet-vgg-verydeep-19.mat) (MD5 `8ee3263992981a1d26e73b3ca028a123`) - put it in the "server" directory of this repository.

### Dependencies

Please Install Python dependencies:

- TensorFlow
- NumPy
- SciPy
- Pillow
- flask