.SUFFIXES:	.jj .java

NAME=Example
GENERATED=${NAME}.java ${NAME}Constants.java ${NAME}TokenManager.java \
	  ParseException.java SimpleCharStream.java \
	  Token.java TokenMgrError.java

${NAME}: ${NAME}.java AST.java
	javac ${NAME}.java AST.java

${NAME}.java: ${NAME}.jj
	javacc ${NAME}.jj

clean:
	-rm *.class ${GENERATED} ast.dot
