ImageJ OPS
==========

ImageJ OPS is an extensible Java framework for algorithms, particularly image
processing algorithms.

Getting started
---------------

Each op has a list of typed input and output parameters on which it operates.
You can think of an op as a (potentially multi-variable) function:
```
c = add(a, b)
(phase, amplitude) = fft(image)
```

In many cases you can also pass a pre-allocated output which will be populated:
```
add(c, a, b)
```

Some ops take other ops as inputs, which allows for things like "execute this
op on every pixel of that image":
```
add_op = op("add", 5)
output_image = map(input_image, add_op)
```

For more details, see these tutorials:
* [Using OPS](https://github.com/imagej/imagej-tutorials/tree/master/using-ops)
* [Create a new OP](https://github.com/imagej/imagej-tutorials/tree/master/create-a-new-op)

Working example
---------------

Try this Jython script in
[ImageJ2's Script Editor](http://developer.imagej.net/downloads)!
```python
# @ImageJ ij

# create a new blank image
from jarray import array
dims = array([78, 23], 'l')
blank = ij.op().create(dims)

# fill in the image with a sinusoid using a formula
formula = "10 * (Math.cos(0.3*p[0]) + Math.sin(0.3*p[1]))"
sinusoid = ij.op().equation(blank, formula)

# add a constant value to an image
ij.op().add(sinusoid, 13.0)

# generate a gradient image using a formula
gradient = ij.op().equation(ij.op().create(dims), "p[0]+p[1]")

# add the two images
composite = ij.op().add(sinusoid, gradient)

# dump the image to the console
ascii = ij.op().ascii(composite)
print("Composite sinusoidal gradient image:\n" + ascii)
```
The output:
```
Composite sinusoidal gradient image:
               ...,,,,,,,,,,,...,,,,---++++++++++-----+++ooo**********ooooo***
.....         ...,,,-----,,,,,,,,,,---++++oooo++++++++++ooo****OOO************
.......      ...,,,--------,,,,,,,---+++oooooooo+++++++ooo***OOOOOOO********OO
......... .....,,,-----------,,,----+++oooooooooo+++++ooo***OOOOOOOOOO*****OOO
,,,,..........,,,----+++------------++ooooo*ooooooooooooo***OOOOOOOOOOOO**OOOO
,,,,,.........,,,---+++++----------+++oooo****oooooooooo***OOOO###OOOOOOOOOOOO
,,,,,.........,,,---++++++---------+++ooo*****oooooooooo***OOOO###OOOOOOOOOOO#
,,,,,.........,,,---++++++---------+++ooo*****oooooooooo***OOOO###OOOOOOOOOOO#
,,,,,.........,,,---+++++----------+++oooo****oooooooooo***OOOO###OOOOOOOOOOOO
,,,,..........,,,----+++------------++ooooo*ooooooooooooo***OOOOOOOOOOO***OOOO
.,.............,,,------------,,----+++ooooooooooo++++ooo***OOOOOOOOOO*****OOO
........    ...,,,----------,,,,,----++oooooooooo++++++ooo***OOOOOOOO*******OO
.......      ...,,,--------,,,,,,,---+++oooooooo+++++++ooo***OOOOOOO********OO
......       ...,,,-------,,,,,,,,---++++oooooo+++++++++ooo***OOOOO**********O
......       ...,,,,------,,,,,,,,,---+++ooooo++++++++++ooo***OOOOO**********O
......       ...,,,,------,,,,,,,,,---+++ooooo++++++++++ooo***OOOOO**********O
......       ...,,,-------,,,,,,,,---++++oooooo+++++++++ooo***OOOOO**********O
.......      ...,,,--------,,,,,,,---+++oooooooo+++++++ooo***OOOOOOO********OO
........   ....,,,-----------,,,,---+++oooooooooo+++++oooo**OOOOOOOOO*******OO
,,,,..........,,,----+++------------++ooooo*ooooooooooooo***OOOOOOOOOOOO**OOOO
,,,,,,.......,,,---+++++++--------+++oooo******ooooooooo***OOO#####OOOOOOOOOO#
,,,,,,,,...,,,,---+++++++++++----+++ooo**********ooooo****OO#########OOOOOOO##
----,,,,,,,,,,---+++ooooo++++++++++ooo****OOO************OO###################
```

How to contribute
-----------------

We welcome [pull requests](https://help.github.com/articles/using-pull-requests)!
* Use an
  [existing op](https://github.com/imagej/imagej-tutorials/create-a-new-op)
  as a starting point
* Use [ImageJ code style](http://developer.imagej.net/coding-style)
* Use
  [small](https://www.crealytics.de/blog/2010/07/09/5-reasons-keeping-git-commits-small-admin/),
  [well-written](http://tbaggery.com/2008/04/19/a-note-about-git-commit-messages.html)
  commits
* Use a [topic branch](http://fiji.sc/Git_topic_branches)
