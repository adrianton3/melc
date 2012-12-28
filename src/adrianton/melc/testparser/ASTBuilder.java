/*
 * Copyright 2012 Adrian Toncean
 * 
 * This file is part of Melc.
 *
 * Melc is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * Melc is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Melc. If not, see <http://www.gnu.org/licenses/>.
 */

package adrianton.melc.testparser;

import java.util.ArrayList;

class ASTBuilder extends ASTBuilderBase {
ASTBuilder(ArrayList<RToken> token) {
super(token);
}

Node getTree() {
 return e();
}

RToken _ID = new RTermId();
RToken _NUM = new RTermNum();
RToken[][] toFirstSet = {{new RTerm("*"), new RTerm("(")}, {new RTerm("*"), new RTermNum()}};
RToken[][] fFirstSet = {{new RTerm("("), new RTermNum()}, {new RTerm("("), new RTerm("(")}, {new RTermNum()}};
RToken[][] parFirstSet = {{new RTerm("("), new RTermNum()}, {new RTerm("("), new RTerm("(")}};
RToken[][] eFirstSet = {{new RTermNum(), new RTerm("+")}, {new RTerm("("), new RTermNum()}, {new RTerm("("), new RTerm("(")}, {new RTermNum()}, {new RTermNum(), new RTerm("*")}};
RToken[][] tFirstSet = {{new RTerm("("), new RTermNum()}, {new RTerm("("), new RTerm("(")}, {new RTermNum()}, {new RTermNum(), new RTerm("*")}};
RToken[][] eoFirstSet = {{new RTerm("+"), new RTerm("(")}, {new RTerm("+"), new RTermNum()}};
RToken[][] eopFirstSet = {{}, {new RTerm("+"), new RTerm("(")}, {new RTerm("+"), new RTermNum()}};
RToken[][] topFirstSet = {{new RTerm("*"), new RTerm("(")}, {}, {new RTerm("*"), new RTermNum()}};

Node to() {
 expect("*");
 Node f1 = f();
 Node top2 = top();
 return new To(f1,top2);
}

Node f() {
 if(match(_NUM)) return next();
 else if(match(parFirstSet)) return par();
 else throw new RuntimeException("f: parsing error");
}

Node par() {
 expect("(");
 Node e1 = e();
 expect(")");
 return new Par(e1);
}

Node e() {
 Node t0 = t();
 Node eop1 = eop();
 return new E(t0,eop1);
}

Node t() {
 Node f0 = f();
 Node top1 = top();
 return new T(f0,top1);
}

Node eo() {
 expect("+");
 Node t1 = t();
 Node eop2 = eop();
 return new Eo(t1,eop2);
}

Eop eop() {
 if(match(eoFirstSet))
  return new Eop(eo());
 else return new Eop(null);
}

Top top() {
 if(match(toFirstSet))
  return new Top(to());
 else return new Top(null);
}

}

class To implements Node {
 final Node f1;
 final Node top2;

 To(Node f1, Node top2) {
  this.f1 = f1;
  this.top2 = top2;
 }

 public String toString() {
  return "to(" +
   "f1: " + f1.toString() + ", " +
   "top2: " + top2.toString() + ", " +
   ")";
 }
}

class F implements Node {
 final Node node;

 F(Node node) {
  this.node = node;
 }

 public String toString() {
  return node.toString();
 }
}

class Par implements Node {
 final Node e1;

 Par(Node e1) {
  this.e1 = e1;
 }

 public String toString() {
  return "par(" +
   "e1: " + e1.toString() + ", " +
   ")";
 }
}

class E implements Node {
 final Node t0;
 final Node eop1;

 E(Node t0, Node eop1) {
  this.t0 = t0;
  this.eop1 = eop1;
 }

 public String toString() {
  return "e(" +
   "t0: " + t0.toString() + ", " +
   "eop1: " + eop1.toString() + ", " +
   ")";
 }
}

class T implements Node {
 final Node f0;
 final Node top1;

 T(Node f0, Node top1) {
  this.f0 = f0;
  this.top1 = top1;
 }

 public String toString() {
  return "t(" +
   "f0: " + f0.toString() + ", " +
   "top1: " + top1.toString() + ", " +
   ")";
 }
}

class Eo implements Node {
 final Node t1;
 final Node eop2;

 Eo(Node t1, Node eop2) {
  this.t1 = t1;
  this.eop2 = eop2;
 }

 public String toString() {
  return "eo(" +
   "t1: " + t1.toString() + ", " +
   "eop2: " + eop2.toString() + ", " +
   ")";
 }
}

class Eop implements Node {
 final Node node;

 Eop(Node node) {
  this.node = node;
 }

 public String toString() {
  return "" + node;
 }
}

class Top implements Node {
 final Node node;

 Top(Node node) {
  this.node = node;
 }

 public String toString() {
  return "" + node;
 }
}

