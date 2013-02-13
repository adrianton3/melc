Melc
====

Melc is a compiler-compiler designed to make the translation from a language's grammar to its parser as simple and straightforward as possible. 
It currently generates only Java parsers but support for other languages is under development.

Melc understands languages written in the in the SEBNF (Simplified Extended Backus-Naur Form), which is a long name for a very 
simple and intuitive variation on the original EBNF. Here's a brief description of it:


The concatenation rule type (-c>)
---------------------------------

Here's a simple rule for parsing constant declarations:

    constant_declaration -c> "const" _identifier "=" #init_value ";"

This can parse anything along the lines of:

    const alpha = 02 ;
    const pi = 90 ;
    const length = 22 ;

The ASTNode derived from this rule can be the following:

    class Constant_declaration extends ASTNode {
     identifier: Identifier
     init_value: Number
    }


The switch rule type (-s>)
--------------------------

Let's now define rules for parsing a condition which might appear inside an if-statement.

    condition -c> expression relation expression
    relation -s> "<" "<=" "==" ">=" ">"

Relation can be any of the terminals stated after `-s>`

The ASTNodes for this stub grammar may look like this:

    class Condition extends ASTNode {
     expression1: Expression
     relation: Relation
     expression2: Expression
    }
    
    class Relation extends ASTNode {
     content: StringTerminal;
    }


The list rule type (-l>)
------------------------

Turning back to the constant declarations, let's say we want to have more than one. Then we must employ some sort of list:

    constant_list -l> constant_declaration
    constant_declaration -c> "const" _identifier "=" #number ";"

The ASTNodes for this stub grammar may look like:

    class Constant_list extends ASTNode {
     list: List[Constant_declaration]
    }
    
    class Constant_declaration extends ASTNode {
     identifier: Identifier
     init_value: Number
    }


The option rule type (-o>)
--------------------------

Maybe we want to be able to declare constants, but have them initialized at a later time. For this we can consider using an optional rule:

    constant_list -l> constant_declaration
    constant_declaration -c> "const" _identifier initialization_maybe ";"
    initialization_maybe -o> initialization
    initialization -c> "=" #number

Sure the grammar in EBNF form would have had just one rule, but there is not enough naming information in that form.


Why even consider this SEBNF monstruosity?
-----------------------------------------

Take for example the following rule in EBNF:

    const_var_list -> {("const" | "var") identifier ["=" number] ";"}

This describes a list of constants or variables which may or may not be initialized at declaration. The problem with this is that there is not enough information in the notation to build an AST from this. 

Consider the following attempt to come up with a structure to represent this production:

    class Const_var_list extends ASTNode {
     list: List[Con_var_list_element]
    }

    class Const_var_list_element {
     isConst: Boolean
     identifierName: Identifier
     isInitialized: Boolean
     initializationValue: Number
    }

The compiler-compiler isn't smart enough to come up with nice and suggestive names for ASTNodes' fields. Therefore, SEBNF has been developed to explicitly state the ASTNodes' fields and structure.



Version history:
================

0.1.2
-----
Added grammar files
Added support for checking if a grammar is left-recursive
Added support for checking if all rules are reachable

0.1.1
-----
Added this README

0.1
---
Initial release