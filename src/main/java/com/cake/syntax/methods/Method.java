/*
 * 24/12/2018 14:21:07
 * Method.java created by Tsvetelin
 */

package com.cake.syntax.methods;


import java.util.List;

import com.cake.running.runtime.CakeRuntime;
import com.cake.syntax.baseElements.Result;
import com.cake.syntax.baseElements.SyntaxElement;
import com.cake.syntax.blocks.Block;
import com.cake.syntax.blocks.scopes.Scope;
import com.cake.syntax.methods.promise.MethodPromise;
import com.cake.syntax.operations.returnOp.ReturnOperator;
import com.cake.syntax.variables.Variable;
import com.cake.syntax.variables.values.Value;


/**
 * @author Tsvetelin
 *
 */
public class Method extends Block
{

    private final MethodPromise promise;

    /**
     * @param promise
     *            - the promise for the method
     * @param body
     *            - the body of the method
     */
    public Method ( MethodPromise promise , Block body , Block superBlock )
    {
        super( promise.getName() , promise.getAccessModifier() , superBlock );
        this.promise = promise;
        this.addSubCommands( body != null ?body.getSubcommands().toArray( new SyntaxElement[0] ) : null );
    }


    /*
     * (non-Javadoc)
     * 
     * @see com.cake.syntax.baseElements.RunnableSyntaxElement#run(com.cake.running.
     * runtime.CakeRuntime, com.cake.syntax.variables.values.Value[])
     */
    @Override
    public Result run ( CakeRuntime runtime , Value... values )
    {
        
        if( promise.canRunWithValues( values ) )
        {
            List< Variable > input = promise.constructInputVariablesList( values );
            Scope scope = new Scope( this );
            
            List< Variable > exitVars = scope.evaluate( runtime , values , input );
            
            Variable retVar = null;

            for ( SyntaxElement syntaxElement : this.getSubcommands() )
            {
                if ( syntaxElement instanceof ReturnOperator )
                {
//                    System.out.println( "Calculating return" );
                    retVar = ( (ReturnOperator) syntaxElement ).calculate( runtime );
                }
            }
            if( retVar != null)
            {
                return new Result( this , retVar.getValue() , null , exitVars );
            }else {
                return new Result( this , null , null , exitVars );
            }
        }
        
        throw new IllegalArgumentException( "Method with declaration: " + promise.toString() + "cannot run with the supplied values" );
        
    }
    
   

    /**
     * @return the promise
     */
    public MethodPromise getPromise ()
    {
        return promise;
    }
    
    /* (non-Javadoc)
     * @see com.cake.syntax.blocks.Block#toString()
     */
    @Override
    public String toString ()
    {
        return promise.toString() + " with body " + this.getSubcommands();
    }
}
