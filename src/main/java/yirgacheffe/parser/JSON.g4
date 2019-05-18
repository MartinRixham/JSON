grammar JSON;

json: object | array;

object: '{' property (',' property)* '}' | '{' '}';

property: STRING ':' value;

array: '[' value (',' value)* ']' | '[' ']';

value: STRING | NUMBER | object | array | TRUE | FALSE | NULL;

TRUE: 'true';

FALSE: 'false';

NULL: 'null';

STRING: '"' (ESC | SAFECODEPOINT)* '"';

fragment ESC: '\\' (["\\/bfnrt] | UNICODE);

fragment UNICODE: 'u' HEX HEX HEX HEX;

fragment HEX: [0-9a-fA-F];

fragment SAFECODEPOINT: ~ ["\\\u0000-\u001F];

NUMBER: '-'? INT ('.' [0-9] +)? EXP?;

fragment INT: '0' | [1-9] [0-9]*;

fragment EXP: [Ee] [+\-]? INT;

WS: [ \t\n\r] + -> skip;