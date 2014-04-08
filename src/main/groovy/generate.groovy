templateDirectory = project.properties['templateDirectory']
outputDirectory = project.properties['outputDirectory']

/*
 * Unescapes a string
 * "\\n" => '\n'
 * "\\\\" => '\\'
 */
def unescape(string) {	
	output = "";
	
	escaped = false;
	for (int i = 0; i < string.length(); ++i) {
		c = string.getAt( i );
		if (c == '\\' && !escaped) {
			escaped = true;
		} else {
			if (escaped && c == 'n') {
				c = '\n';
			}
			escaped = false;
			output += c;
		}
	}
	
	return output;
}

/*
 * Finds a character c in str and returns the index this character was
 * first found in str.
 */
def firstIndexOf(str, c) {
	int index = -1;
	for (int i = 0; i < str.length(); ++i) {
		if (str.getAt(i) == c) {
			index = i;
			break;
		}
	}
	
	return index;
}

/*
 * Translates a template into many files in the outputDirectory,
 * given translations where each line has the form:
 *
 * [filename]~[pattern]=[replacement]~[pattern2]=[replacement2]...
 */
def translate(template, translations) {
  /* read template */
  builder = new java.lang.StringBuilder()
    reader = new java.io.BufferedReader(new java.io.FileReader("$templateDirectory/$template"));
  for (;;) {
    line = reader.readLine();
    if (line == null) break;
    builder.append(line).append('\n');
  }
  reader.close();
  untranslated = builder.toString();

  /* read translation lines */
  reader = new java.io.BufferedReader(new java.io.FileReader("$templateDirectory/$translations"));
  for (;;) {
    line = reader.readLine();
    if (line == null) break;
    translated = untranslated;
    pairs = line.split('~');
    for (i = 1; i < pairs.length; i++) {
      split = firstIndexOf(pairs[i], '=');
      if (split == -1) {
        throw new IllegalArgumentException("Illegal pair: '" + pairs[i] + "' in line '" + line + "'");
      }
	  curPair = pairs[i]; //for convenience
      translated = translated.replaceAll(curPair.substring(0, split), unescape(curPair.substring(split+1, curPair.length())));
    }

    // write out the file
    outputFile = new java.io.File(outputDirectory, pairs[0]);
    if (outputFile.getParentFile() != null) {
      outputFile.getParentFile().mkdirs();
    }
    out = new java.io.FileOutputStream(outputFile);
    out.write(translated.getBytes("UTF-8"));
    out.close();
  }
  reader.close();
}

<<<<<<< HEAD
/*
 * Generate classes from templates and lists
 */
translate('Arithmetic.template', 'Arithmetic.list');
translate('RealUnary.template', 'RealUnary_implementations.list');
translate('RealUnaryInterface.template', 'RealUnary_interfaces.list');
=======
// translate all templates in the template directory
for (file in new java.io.File(templateDirectory).listFiles()) {
  name = file.getName();
  if (!name.endsWith('.template')) continue;
  prefix = name.substring(0, name.length() - 9);
  translate(name, prefix + '.list');
}
>>>>>>> d3760a48927c61546f9cb582b2265148b8f46b04
