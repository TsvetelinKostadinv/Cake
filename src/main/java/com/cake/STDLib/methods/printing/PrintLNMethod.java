/*
 * 09/01/2019 21:37:33
 * PrintMethod.java created by Tsvetelin
 */

package com.cake.STDLib.methods.printing;


import com.cake.STDLib.block.STDLibBlock;
import com.cake.STDLib.methods.STDLibMethod;
import com.cake.compilation.tokenizer.tokenizers.temporary.TempTokenizer;
import com.cake.running.runtime.CakeRuntime;
import com.cake.syntax.baseElements.Result;
import com.cake.syntax.methods.promise.MethodPromise;
import com.cake.syntax.parsers.ParsersContainer;
import com.cake.syntax.variables.values.Value;


/**
 * @author Tsvetelin
 *
 */
public class PrintLNMethod extends STDLibMethod
{

    public static final String PROMISE = "global int println = ( ANYTHING str )";

    private static final MethodPromise promise = ParsersContainer.METHOD_PROMISE_PARSER
            .parse( STDLibBlock.STD_LIB_BLOCK , new TempTokenizer().tokenize( PROMISE ) ).getValue();


    /**
     * @param promise
     * @param superBlock
     */
    public PrintLNMethod ()
    {
        super( promise );
    }


    /*
     * (non-Javadoc)
     * 
     * @see com.cake.syntax.methods.Method#run(com.cake.running.runtime.CakeRuntime,
     * com.cake.syntax.variables.values.Value[])
     */
    @Override
    public Result run ( CakeRuntime runtime , Value... values )
    {
        System.out.println( values[0].getValue() );
        return null;
    }

}
