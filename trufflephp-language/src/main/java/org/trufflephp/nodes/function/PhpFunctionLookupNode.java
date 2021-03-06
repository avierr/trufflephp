package org.trufflephp.nodes.function;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.frame.VirtualFrame;
import org.trufflephp.exception.PhpUndefFunctionException;
import org.trufflephp.nodes.PhpExprNode;
import org.trufflephp.parser.ParseScope;
import org.trufflephp.types.PhpFunction;

/**
 * Function implementation may only be known after a function call
 * in terms of location in source code.
 * <p>
 * During runtime, this node looks up the implementation of the function.
 *
 * @author abertschi
 */
public final class PhpFunctionLookupNode extends PhpExprNode {

    private final String name;

    // lazy load function because not yet available when this node is created
    @CompilationFinal
    private PhpFunction function;

    private final ParseScope scope;

    public PhpFunctionLookupNode(String name, ParseScope scope) {
        this.name = name;
        this.scope = scope;
    }

    @Override
    public PhpFunction executeGeneric(VirtualFrame frame) {
        if (function == null) {
            // We are about to change a @CompilationFinal field.
            CompilerDirectives.transferToInterpreterAndInvalidate();

            PhpFunction fn = this.scope.resolveFunction(this.name);
            if (fn == null) {
                StringBuilder buf = new StringBuilder()
                        .append("Function ")
                        .append(name)
                        .append(" not found.")
                        .append(" scope: ")
                        .append(scope.getGlobal() == scope ? "<global>" : "<function>");
                throw new PhpUndefFunctionException(buf.toString(), this);
            }
            this.function = fn;
        }
        return function;
    }
}
