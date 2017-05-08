
/**
 * @fileoverview
 * Registers a language handler for ADQL.
 *
 *
 * To use, include prettify.js and this file in your HTML page.
 * Then put your code in an HTML tag like
 *      <pre class="prettyprint lang-adql">(my ADQL code)</pre>
 *
 */

PR['registerLangHandler'](
    PR['createSimpleLexer'](
        [
         // Whitespace
         [PR['PR_PLAIN'],       /^[\t\n\r \xA0]+/, null, '\t\n\r \xA0'],
         // A double or single quoted, possibly multi-line, string.
         [PR['PR_STRING'],      /^(?:"(?:[^\"\\]|\\.)*"|'(?:[^\'\\]|\\.)*')/, null,
          '"\'']
        ],
        [
         // A comment is either a line comment that starts with two dashes, or
         // two dashes preceding a long bracketed block.
         [PR['PR_COMMENT'], /^(?:--[^\r\n]*|\/\*[\s\S]*?(?:\*\/|$))/],
         [PR['PR_KEYWORD'], /^(?:ALL|AND|AS|ASC|BETWEEN|BY|CONTAINS|CROSS|DESC|DISTINCT|EXISTS|FROM|FULL|GROUP|HAVING|IN|INNER|INTERSECTS|IS|JOIN|LEFT|LIKE|NOT|NULL|ON|OR|ORDER|OUTER|RIGHT|SELECT|LIMIT|USING|WHERE|ABS|CEILING|DEGREES|EXP|FLOOR|LOG|LOG10|MOD|PI|POWER|RADIANS|SQRT|RAND|ROUND|TRUNCATE|ACOS|ASIN|ATAN|ATAN2|COS|SIN|TAN|AREA|BOX|CENTROID|CIRCLE|COORD1|COORD2|COORDSYS|DISTANCE|POINT|POLYGON|REGION)(?=[^\w-]|$)/i, null],
         // A number is a hex integer literal, a decimal real literal, or in
         // scientific notation.
         [PR['PR_LITERAL'],
          /^[+-]?(?:0x[\da-f]+|(?:(?:\.\d+|\d+(?:\.\d*)?)(?:e[+\-]?\d+)?))/i],
         // An identifier
         [PR['PR_PLAIN'], /^[a-z_][\w-]*/i],
         // A run of punctuation
         [PR['PR_PUNCTUATION'], /^[^\w\t\n\r \xA0\"\'][^\w\t\n\r \xA0+\-\"\']*/]
        ]),
    ['adql']);
