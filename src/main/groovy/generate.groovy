templateDirectory = project.properties['templateDirectory']
outputDirectory = project.properties['outputDirectory']

/* Processes a template using Apache Velocity. */
def processTemplate(engine, context, templateFile, outFilename) {
  if (outFilename == null) return; // nothing to do

  // create output directory if it does not already exist
  outFile = new java.io.File(outputDirectory, outFilename);
  if (outFile.getParentFile() != null) outFile.getParentFile().mkdirs();

  // apply the template and write out the result
  t = engine.getTemplate(templateFile);
  writer = new StringWriter();
  t.merge(context, writer);
  out = new PrintWriter(outFile, "UTF-8");
  out.print(writer.toString());
  out.close();
}

/*
 * Translates a template into many files in the outputDirectory,
 * given a translations file in INI style; e.g.:
 *
 * [filename1]
 * variable1 = value1
 * variable2 = value2
 * ...
 * [filename2]
 * variable1 = value3
 * variable2 = value4
 * ...
 */
def translate(templateFile, translationsFile) {
  // initialize the Velocity engine
  engine = new org.apache.velocity.app.VelocityEngine();
  p = new java.util.Properties();
  p.setProperty("file.resource.loader.path", "$templateDirectory");
  // tell Velocity to log to stderr rather than to a velocity.log file
  p.setProperty(org.apache.velocity.runtime.RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
    "org.apache.velocity.runtime.log.SystemLogChute");
  engine.init(p);

  // read translation lines
  context = outputFilename = null;
  reader = new java.io.BufferedReader(new java.io.FileReader("$templateDirectory/$translationsFile"));
  for (;;) {
    // read the line
    line = reader.readLine();
    if (line == null) break;

    // check if the line starts a new section
    if (line.startsWith("[") && line.endsWith("]")) {
      // write out the previous file
      processTemplate(engine, context, templateFile, outputFilename);

      // start a new file
      outputFilename = line.substring(1, line.length() - 1);
      context = new org.apache.velocity.VelocityContext();
      continue;
    }

    // ignore blank lines
    if (line.trim().isEmpty()) continue;

    // parse key/value pair lines separate by equals
    if (!line.contains('=')) {
      print("[WARNING] $translationsFile: Ignoring spurious line: $line");
      continue;
    }
    pair = line.split('\\s*=\\s*', 2);
    context.put(pair[0], pair[1]);
  }
  reader.close();

  // process the template
  processTemplate(engine, context, templateFile, outputFilename);
}

// translate all templates in the template directory
for (file in new java.io.File(templateDirectory).listFiles()) {
  name = file.getName();
  if (!name.endsWith('.vm')) continue;
  prefix = name.substring(0, name.lastIndexOf('.'));
  translate(name, prefix + '.list');
}
