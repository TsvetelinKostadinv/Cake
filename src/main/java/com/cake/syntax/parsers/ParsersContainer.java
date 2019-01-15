/*
 * 05/11/2018 at 18:15:03 �.
 * ParsersContainer.java created by Tsvetelin
 */

package com.cake.syntax.parsers;


import java.util.ArrayList;
import java.util.List;

import com.cake.compilation.tokens.Token;
import com.cake.syntax.baseElements.SyntaxElement;
import com.cake.syntax.blocks.Block;
import com.cake.syntax.blocks.parser.BlockParser;
import com.cake.syntax.controlFlowStatements.conditionals.IfStatement;
import com.cake.syntax.controlFlowStatements.conditionals.parser.IfStatementParser;
import com.cake.syntax.controlFlowStatements.loops.whileLoop.WhileLoop;
import com.cake.syntax.controlFlowStatements.loops.whileLoop.parser.WhileLoopParser;
import com.cake.syntax.methods.Method;
import com.cake.syntax.methods.parser.MethodParser;
import com.cake.syntax.methods.promise.MethodPromise;
import com.cake.syntax.methods.promise.parser.MethodPromiseParser;
import com.cake.syntax.operations.methodInvocation.MethodInvocationOperator;
import com.cake.syntax.operations.methodInvocation.parser.MethodInvokationOperatorParser;
import com.cake.syntax.operations.reassignmentOp.ReassignmentOperator;
import com.cake.syntax.operations.reassignmentOp.parser.ReassignmentOperationParser;
import com.cake.syntax.operations.returnOp.ReturnOperator;
import com.cake.syntax.operations.returnOp.parser.ReturnOperatorParser;
import com.cake.syntax.variables.Variable;
import com.cake.syntax.variables.parser.VariableDeclarationParser;
import com.cake.utils.container.Container;


/**
 * 
 * Container for all the parsers. They should be included here in order to be
 * used. <br>
 * It searches for parsers that extend
 * <code>com.cake.syntax.parsers.Parser</code> <br>
 * If they extend it they will be added here
 * 
 * @see com.cake.syntax.parsers.Parser
 * 
 * @author Tsvetelin
 *
 */
public class ParsersContainer extends Container< Parser< ? > >
{

    public static ParsersContainer INSTANCE = new ParsersContainer();

    public static final Parser< Block > BLOCK_PARSER = new BlockParser();
    public static final Parser< Method > METHOD_PARSER = new MethodParser();
    public static final Parser< MethodPromise > METHOD_PROMISE_PARSER = new MethodPromiseParser();
    public static final Parser< MethodInvocationOperator > METHOD_INVOCATION_PARSER = new MethodInvokationOperatorParser();
    public static final Parser< ReassignmentOperator > REASSIGNMENT_OPERATOR_PARSER = new ReassignmentOperationParser();
    public static final Parser< ReturnOperator > RETURN_OPERATOR_PARSER = new ReturnOperatorParser();
    public static final Parser< Variable > VARIABLE_PARSER = new VariableDeclarationParser();
    public static final Parser< IfStatement > IF_STATEMENT_PARSER = new IfStatementParser();
    public static final Parser< WhileLoop > WHILE_LOOP_PARSER = new WhileLoopParser();
    

    private ParsersContainer ()
    {
    }


    /**
     * Adds the supplied parser
     * 
     * @param parser
     */
    public void addParser ( Parser< ? > parser )
    {

        if ( !elements.contains( parser ) ) super.addElement( parser );
    }


    /**
     * Removes the supplied parser
     * 
     * @param parser
     */
    public void removeParser ( Parser< ? > parser )
    {

        super.removeElement( parser );
    }


    /**
     * 
     * Determines the parser for the supplied code
     * 
     * @param code
     * @return the parser for this sequence
     */
    public synchronized List< Parser< ? extends SyntaxElement > > getParserFor ( List< Token > code )
    {
        List< Parser< ? > > res = new ArrayList<>();
        for ( int i = 0; i< elements.size() ; i++)
        {
            if ( elements.get( i ).canParse( code ) ) res.add( elements.get( i ) );
        }
        return res;
    }

}
