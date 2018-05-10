## Requirements

### Data Files

- [Pre-trained VGG network](http://www.vlfeat.org/matconvnet/models/beta16/imagenet-vgg-verydeep-19.mat) (MD5 `8ee3263992981a1d26e73b3ca028a123`) - put it in the top level of this repository, or specify its location using the `--network` option.

### Dependencies

You can install Python dependencies using `pip install -r requirements.txt`, and it should just work. If you want to install the packages manually, here's a list:

- [TensorFlow](https://www.tensorflow.org/versions/master/get_started/os_setup.html#download-and-setup)
- [NumPy](https://github.com/numpy/numpy/blob/master/INSTALL.rst.txt)
- [SciPy](https://github.com/scipy/scipy/blob/master/INSTALL.rst.txt)
- [Pillow](http://pillow.readthedocs.io/en/3.3.x/installation.html#installation)