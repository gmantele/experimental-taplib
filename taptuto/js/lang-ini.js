// The line lexer takes an array of patterns.
// Each pattern starts with a regular expression followed by a syntax color for each captured set in the expression.
// For instance, you might capture a key followed by an operator followed by a value. So after that regular expression,
//   there would be the syntax colors for the key, operator, and value (in order).
// Anything that's not captured is then given the PR_PLAIN syntax color.
// Patterns must be designed to match whole lines and they should be listed in order of precedence, so once a pattern
//   matches a line, that line no longer checks itself against other patterns.
function createLineLexer(patterns) {
  return function(job) {
    var lines = job.sourceCode.match(/[^\r\n]*([\r\n]+|$)/g),
      pos = job.basePos,
      decorations = [ pos, PR['PR_PLAIN'] ],
      li, nLines = lines.length,
      line,
      pi, nPatterns = patterns.length,
      pattern,
      mi, nMatches, matches, match, mOffset, mIndex;
    // Iterate each line
    for ( li = 0; li < nLines; ++li ) {
      line = lines[li];
      // Iterate each pattern, seeing if the line matches the pattern
      for ( pi = 0; pi < nPatterns; ++pi ) {
        pattern = patterns[pi];
        matches = line.match(pattern[0]);
        if ( matches ) {
          nMatches = matches.length;
          mOffset = 0;
          // Iterate each captured set
          for ( mi = 1; mi < nMatches; ++mi ) {
            match = matches[mi];
            // Sets are captured in order, so we can find the position of the capture using indexOf with an offset
            mIndex = line.indexOf(match, mOffset);
            // This should never fail since the regex was passed, but just in case, ensure that we found the index
            if ( mIndex > -1 ) {
              mOffset = mIndex + match.length;
              // Add the decorator. Use the PR_PLAIN styling for the text following the capture
              decorations.push(pos + mIndex, pattern[mi]);
              decorations.push(pos + mIndex + match.length, PR['PR_PLAIN']);
            }
          }
          break;
        }
      }
      // Increment the pos, moving on to the next line
      pos += line.length;
    }
    // Send the decorations back with in the job
    job.decorations = decorations;
  }
};

PR['registerLangHandler'](
  createLineLexer([
    // If a line starts with a semicolon or hash as the first non-whitespace character then the whole line is a comment
	[ /^\s*([;#][^\n]*)/, PR['PR_COMMENT'] ],
    // If the first non-whitespace character is a left bracket and it ends with a right bracket then it's a section marker
    [ /^\s*(\[[^\]]*\])\s*$/, PR['PR_KEYWORD'] ],
    // Attributes start with a name followed by an equals sign or colon and then everything after is the value (trimmed)
    [ /^\s*([^:=]+)\s*([=:])\s*(.+)/, PR['PR_ATTRIB_NAME'], PR['PR_PUNCTUATION'], PR['PR_PLAIN'] ]
  ]),
  [ 'conf', 'ini', 'property' ]
);
