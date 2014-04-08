templateDirectory = project.properties['templateDirectory']
outputDirectory = project.properties['outputDirectory']

/* Processes a template using Apache Velocity. */
def processTemplate(engine, context, templateFile, outFile) {
  t = engine.getTemplate(templateFile);
  writer = new StringWriter();
  t.merge(context, writer);
  out = new PrintWriter(outFile, "UTF-8");
  out.print(writer.toString());
  out.close();
}

/*
 * Translates a template into many files in the outputDirectory,
 * given translations where each line has the form:
 *
 * [filename]~[pattern]=[replacement~[pattern2]=[replacement2]...
 */
def translate(templateFile, translations) {
  // initialize the Velocity engine
  engine = new org.apache.velocity.app.VelocityEngine();
  p = new java.util.Properties();
  p.setProperty("file.resource.loader.path", "$templateDirectory");
  // tell Velocity to log to stderr rather than to a velocity.log file
  p.setProperty(org.apache.velocity.runtime.RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
    "org.apache.velocity.runtime.log.SystemLogChute");
  engine.init(p);

  // read translation lines
  reader = new java.io.BufferedReader(new java.io.FileReader("$templateDirectory/$translations"));
  for (;;) {
    // read the line
    line = reader.readLine();
    if (line == null) break;
    pairs = line.split('~');
    outputFilename = pairs[0];

    // parse the patterns
    context = new org.apache.velocity.VelocityContext();
    for (i = 1; i < pairs.length; i++) {
      split = pairs[i].split('=');
      if (split.length != 2) {
        throw new IllegalArgumentException("Illegal pair: '${pairs[i]}' in line '$line'");
      }
      context.put(split[0], split[1]);
    }

    // process the template
    outputFile = new java.io.File(outputDirectory, outputFilename);
    if (outputFile.getParentFile() != null) outputFile.getParentFile().mkdirs();
    processTemplate(engine, context, templateFile, outputFile);
  }
  reader.close();
}

// translate all templates in the template directory
for (file in new java.io.File(templateDirectory).listFiles()) {
  name = file.getName();
  if (!name.endsWith('.vm')) continue;
  prefix = name.substring(0, name.lastIndexOf('.'));
  translate(name, prefix + '.list');
}
