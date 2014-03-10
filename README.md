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

Calling OPS from a BeanShell script:
```java
ij = new ImageJ();
seven = ij.ops().run("add", 2, 5); // add two numbers
data = ij.dataset().open("/path/to/data.tif");
result = ij.ops().run("add", data, 13); // add number to image
moredata = ij.data().open("/path/to/moredata.tif");
result = ij.ops().run("add", data, moredata); // add two images
result = ij.ops().add(data, moredata); // built-ins can be called directly
addOp = ij.ops().op("add", 5);
result = ij.map(data, addOp); // execute add op on every image pixel
```

For more details, see these tutorials:
* [Using OPS](https://github.com/imagej/imagej-tutorials/using-ops)
* [Create a new OP](https://github.com/imagej/imagej-tutorials/create-a-new-op)

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
