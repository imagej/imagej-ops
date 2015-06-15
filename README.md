[![](http://jenkins.imagej.net/job/ImageJ-OPS/lastBuild/badge/icon)](http://jenkins.imagej.net/job/ImageJ-OPS/)
[![Join the chat at https://gitter.im/imagej/imagej-ops](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/imagej/imagej-ops?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

ImageJ OPS
==========

ImageJ OPS is an extensible Java framework for algorithms, particularly image
processing algorithms. OPS seeks to be a unifying library for scientific image
processing. See the
[Motivation](https://github.com/imagej/imagej-ops/wiki/Motivation) page for
details.

Getting started
---------------

Each op has a list of typed input and output parameters on which it operates.
You can think of an op as a (potentially multi-variable) function:
```
c = math.add(a, b)
(phase, amplitude) = fft(image)
```

In many cases you can also pass a pre-allocated output which will be populated:
```
math.add(c, a, b)
```

Some ops take other ops as inputs, which allows for things like "execute this
op on every pixel of that image":
```
add_op = op("math.add", 5)
output_image = map(input_image, add_op)
```

For more details, see these tutorials:
* [Using OPS](https://github.com/imagej/imagej-tutorials/tree/master/using-ops)
* [Create a new OP](https://github.com/imagej/imagej-tutorials/tree/master/create-a-new-op)

Working example
---------------

Try this Jython script in ImageJ's
[Script Editor](http://imagej.net/Script_Editor)!

```python
# @ImageJ ij

# create a new blank image
from jarray import array
dims = array([150, 100], 'l')
blank = ij.op().createimg(dims)

# fill in the image with a sinusoid using a formula
formula = "10 * (Math.cos(0.3*p[0]) + Math.sin(0.3*p[1]))"
sinusoid = ij.op().equation(blank, formula)

# add a constant value to an image
ij.op().math().add(sinusoid, 13.0)

# generate a gradient image using a formula
gradient = ij.op().equation(ij.op().createimg(dims), "p[0]+p[1]")

# add the two images
composite = ij.op().createimg(dims)
ij.op().math().add(composite, sinusoid, gradient)

# display the images
ij.ui().show("sinusoid", sinusoid)
ij.ui().show("gradient", gradient)
ij.ui().show("composite", composite)
```

The output:

![sinusoid](images/sinusoid.png) ![gradient](images/gradient.png) ![composite](images/composite.png)

How to contribute
-----------------

We welcome [pull requests](https://help.github.com/articles/using-pull-requests)!
* Use an
  [existing op](https://github.com/imagej/imagej-tutorials/tree/master/create-a-new-op)
  as a starting point
* Use [ImageJ code style](http://imagej.net/Coding_style)
* Use
  [small](https://www.crealytics.de/blog/2010/07/09/5-reasons-keeping-git-commits-small-admin/),
  [well-written](http://tbaggery.com/2008/04/19/a-note-about-git-commit-messages.html)
  commits
* Use a [topic branch](http://imagej.net/Git_topic_branches)
