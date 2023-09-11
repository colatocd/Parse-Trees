package cmsc256;

/**
 * CMSC 256
 * Project5
 * [Colato], [Cesar]
 */

import bridges.connect.Bridges;
import bridges.base.BinTreeElement;

import java.util.Stack;

public class ParseTrees {

    public static void main(String[] args) {
        String ex1 = "( ( 7 + 3 ) * ( 5 - 2 ) )";
        BinTreeElement<String> parseTree1 = buildParseTree(ex1);
        double answer1 = evaluate(parseTree1);
        System.out.println(answer1);
        System.out.println(getEquation(parseTree1));

        String ex2 = "( ( 10 + 5 ) * 3 )";
        BinTreeElement<String> parseTree2 = buildParseTree(ex2);
        double answer2 = evaluate(parseTree2);
        System.out.println(answer2);
        System.out.println(getEquation(parseTree2));

        String ex3 = "( ( ( ( ( 2 * 12 ) / 6 ) + 3 ) - 17 ) + ( 2 * 0 ) )";
        BinTreeElement<String> parseTree3 = buildParseTree(ex3);
        double answer3 = evaluate(parseTree3);
        System.out.println(answer3);
        System.out.println(getEquation(parseTree3));

        String ex4 = "( 3 + ( 4 * 5 ) )";
        BinTreeElement<String> parseTree4 = buildParseTree(ex4);
        double answer4 = evaluate(parseTree4);
        System.out.println(answer4);
        System.out.println(getEquation(parseTree4));

        /* Initialize a Bridges connection */
        Bridges bridges = new Bridges(5, "cesarcolato", "41388393710");

        /* Set an assignment title */
        bridges.setTitle("Arithmetic Parse Tree Project - Debra Duke");
        bridges.setDescription("CMSC 256, Spring 2022");

        try {
            /* Tell BRIDGES which data structure to visualize */
            bridges.setDataStructure(parseTree1);
            /* Visualize the Array */
            bridges.visualize();

            /* Tell BRIDGES which data structure to visualize */
            bridges.setDataStructure(parseTree2);
            /* Visualize the Array */
            bridges.visualize();

            /* Tell BRIDGES which data structure to visualize */
            bridges.setDataStructure(parseTree3);
            /* Visualize the Array */
            bridges.visualize();

            /* Tell BRIDGES which data structure to visualize */
            bridges.setDataStructure(parseTree4);
            /* Visualize the Array */
            bridges.visualize();
        } catch (Exception ex) {
            System.out.println("Error connecting to Bridges server.");
        }
    }

    //The method accepts a mathematical expression as a string parameter and returns the root of the parse tree.
    public static bridges.base.BinTreeElement<String> buildParseTree(String expression) {
        //root of the expression tree
        BinTreeElement<String> parseTree = new BinTreeElement<>("root", "");
        BinTreeElement<String> current = parseTree;
        Stack<BinTreeElement> treeElements = new Stack<>();
        //break up String expression into list of tokens
        String[] tokens = expression.split(" ");

        //iterate through list of tokens
        for (String token : tokens) {
            //removes white spaces
            token = token.trim();

            if (token.equals("(")) {
                //add new node as left child
                current.setLeft(new BinTreeElement<>("left", "e"));
                //descend to left child
                treeElements.push(current);
                current = current.getLeft();
            } else if (token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/")) {
                //set root value of the current node to the operator represented by current token
                current.setValue(token);
                current.setLabel(token);
                //add new node as right child
                current.setRight(new BinTreeElement<>("right", "e"));
                //descend to right child
                treeElements.push(current);
                current = current.getRight();
            } else if (isNumeric(token)) {
                //set root value of current node to the number
                current.setValue(token);
                current.setLabel(token);
                //return to parent
                if (!treeElements.empty()) {
                    current = treeElements.pop();
                }
            } else if (token.equals(")")) {
                //return to parent
                if (!treeElements.empty()) {
                    current = treeElements.pop();
                }
            } else {
                //if
                throw new IllegalArgumentException("Expression cannot be parsed. Current token is " + token);
            }
        }
        return parseTree;
    }

    //This method evaluates a parse tree by recursively evaluating each subtree.
    public static double evaluate(bridges.base.BinTreeElement<String> tree) {
        //if root is empty
        if (tree.getValue() == null) {
            return Double.NaN;
        }
        //if left and right child are null then return that value as subtree
        if ((tree.getLeft() == null) && (tree.getRight() == null)) {
            return Double.parseDouble(tree.getValue());
        }
        //recursively evaluate each subtree
        double x = evaluate(tree.getLeft());
        double y = evaluate(tree.getRight());
        //call process to get back answer to equation
        return process(tree.getValue(), x, y);
    }

    //The method accepts the root of the parse tree parameter and returns a mathematical expression as a string.
    public static String getEquation(bridges.base.BinTreeElement<String> tree) {
        //inorder traversal to get original equation, recursion
        String leftNode = "";
        String rightNode = "";
        String fullEquation = "";
        //base case, returns root of parse tree
        if (tree.getLeft() == null && tree.getRight() == null) {
            return tree.getValue();
        }
        //if left node is occupied, call getEquation on left node
        if (tree.getLeft() != null) {
            leftNode = getEquation(tree.getLeft());
        }
        //if right node is occupied, call getEquation on right node
        if (tree.getRight() != null) {
            rightNode = getEquation(tree.getRight());
        }
        //write out full equation
        fullEquation = "(" + " " + leftNode + " " + tree.getValue() + " " + rightNode + " )";

        //return full equation string
        return fullEquation;
    }

    //checks if String is numeric
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    //method that processes and does the math between operators and operands
    public static double process(String op, double x, double y) {
        //throw exception if dividing by zero
        if (op.equals("/") && y == 0) {
            throw new ArithmeticException("Unable to divide by 0.");
        }
        if (op.equals("+")) {
            return x + y;
        }
        if (op.equals("-")) {
            return x - y;
        }
        if (op.equals("/")) {
            return x / y;
        }
        if (op.equals("*")) {
            return x * y;
        }
        return 0;
    }
}

