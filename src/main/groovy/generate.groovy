templateDirectory = project.properties['templateDirectory']
outputDirectory = project.properties['outputDirectory']

/*
 * Translates a template into many files in the outputDirectory,
 * given translations where each line has the form:
 *
 * [filename]~[pattern]=[replacement~[pattern2]=[replacement2]...
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
      split = pairs[i].split('=');
      if (split.length != 2) {
        throw new IllegalArgumentException("Illegal pair: '${pairs[i]}' in line '$line'");
      }
      translated = translated.replaceAll(split[0], split[1]);
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

translate('Arithmetic.template', 'Arithmetic.list');
