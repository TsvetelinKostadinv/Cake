/*
 * 05/01/2019 13:07:45
 * MethodInvokationParser.java created by Tsvetelin
 */

package com.cake.syntax.operations.methodInvocation.parser;


import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;
import com.cake.compilation.tokens.Token;
import com.cake.running.runtime.CakeRuntime;
import com.cake.syntax.blocks.Block;
import com.cake.syntax.methods.Method;
import com.cake.syntax.operations.methodInvocation.MethodInvocationOperator;
import com.cake.syntax.parsers.Parser;
import com.cake.syntax.parsers.checkers.Checker;
import com.cake.syntax.variables.values.Value;
import com.cake.utils.expressions.evaluation.ExpressionEvaluator;

import javafx.util.Pair;


/**
 * @author Tsvetelin
 *
 */
public class MethodInvokationOperatorParser extends Parser< MethodInvocationOperator > implements Checker
{

    private static final Token OPENING_BRACE = new Token( "(" , OPERATOR_TYPE );

    private static final Token CLOSING_BRACE = new Token( ")" , OPERATOR_TYPE );

    private static final Token COMMA = new Token( "," , OPERATOR_TYPE );


    /*
     * (non-Javadoc)
     * 
     * @see com.cake.syntax.parsers.Parser#canParse(java.util.List)
     */
    @Override
    public boolean canParse ( List< Token > sequence )
    {

        Objects.requireNonNull( sequence );

        if ( sequence.size() < 3 ) return false;
        
        boolean first = sequence.get( 0 ).getTokenType().equals( IDENTIFIER_TYPE );

        boolean openingBrace = sequence.get( 1 ).equals( OPENING_BRACE );
        boolean closingBrace = sequence.get( sequence.size() - 1 ).equals( CLOSING_BRACE );
        
        if( !( first&&openingBrace&&closingBrace ) ) return false;
        
        boolean paramListCorrect = true;

        List< Token > paramList = sequence.subList( 2 , sequence.size() - 1 );
        
        if ( paramList.size() == 0 ) paramListCorrect = false;
        else if ( paramList.size() % 2 == 0 ) paramListCorrect = true;
        else for ( int i = 0 ; i < paramList.size() ; i += 2 )
        {
            if ( ! ( paramList.get( i ).getTokenType().equals( IDENTIFIER_TYPE )
                    || paramList.get( i ).getTokenType().equals( NUMBER_LITERAL_TYPE )
                    || paramList.get( i ).getTokenType().equals( REAL_NUMBER_LITERAL_TYPE )
                    || paramList.get( i ).getTokenType().equals( INTEGER_NUMBER_LITERAL_TYPE ) ) )
            {
                if ( ! ( paramList.get( i + 1 ).equals( COMMA ) ) )
                {
                    paramListCorrect = false;
                }
            }
        }

        return first && openingBrace && closingBrace & paramListCorrect;
    }


    /*
     * (non-Javadoc)
     * 
     * @see com.cake.syntax.parsers.Parser#parse(com.cake.syntax.blocks.Block,
     * java.util.List)
     */
    @Override
    public Pair< String , MethodInvocationOperator > parse ( Block superblock , List< Token > tokens )
    {
        if ( this.canParse( tokens ) )
        {
            String methodName = tokens.get( 0 ).getToken();
            Method exec = null;
            try
            {
                exec = superblock.getSubcommands().stream().filter( x -> x instanceof Method ).map( x -> (Method) x )
                        .filter( x -> x.getName().equals( methodName ) ).findFirst().get();
            } catch ( NoSuchElementException e )
            {
                throw new NoSuchElementException( "No such method exists in the superblock of the invokation" );
            }

            List< Token > paramList = tokens.subList( 3 , tokens.size() );

            boolean containsIdentifiers = paramList.stream().filter( x -> x.getTokenType().equals( IDENTIFIER_TYPE ) )
                    .count() != 0;

            if ( containsIdentifiers )
                throw new UnsupportedOperationException( "Cannot invokate with the parameters without a runtime" );

            Value [] values = paramList.stream().filter( x -> !x.getTokenType().equals( OPERATOR_TYPE ) ).map( x -> {
                List< Token > list = new ArrayList<>();
                list.add( x );
                return list;
            } ).map( x -> ExpressionEvaluator.evaluate( null , x ) ).collect( Collectors.toList() )
                    .toArray( new Value[0] );

            MethodInvocationOperator op = new MethodInvocationOperator( exec , values );

            String address = Block.joinNames( superblock , op );

            return new Pair< String , MethodInvocationOperator >( address , op );

        }
        throw new UnsupportedOperationException( "Cannot parse the sequence" );
    }


    /*
     * (non-Javadoc)
     * 
     * @see
     * com.cake.syntax.parsers.Parser#parseWithRuntime(com.cake.running.runtime.
     * CakeRuntime, com.cake.syntax.blocks.Block, java.util.List)
     */
    @Override
    public Pair< String , MethodInvocationOperator > parseWithRuntime ( CakeRuntime runtime , Block superblock ,
            List< Token > tokens )
    {
        if ( this.canParse( tokens ) )
        {
            String methodName = tokens.get( 0 ).getToken();
            Method exec = (Method) runtime.getElement( methodName );

            List< Token > paramList = tokens.subList( 3 , tokens.size() );

            Value [] values = paramList.stream().filter( x -> !x.getTokenType().equals( OPERATOR_TYPE ) ).map( x -> {
                List< Token > list = new ArrayList<>();
                list.add( x );
                return list;
            } ).map( x -> ExpressionEvaluator.evaluate( runtime , x ) ).collect( Collectors.toList() )
                    .toArray( new Value[0] );

            MethodInvocationOperator op = new MethodInvocationOperator( exec , values );

            String address = Block.joinNames( superblock , op );

            return new Pair< String , MethodInvocationOperator >( address , op );

        }
        throw new UnsupportedOperationException( "Cannot parse the sequence" );
    }

}
