package org.trufflephp.nodes.binary.logical;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import org.trufflephp.nodes.binary.PhpBinaryNode;
import org.trufflephp.runtime.array.PhpArray;

/**
 * @author abertschi
 */
@NodeInfo(shortName = "==")
public abstract class PhpEqNode extends PhpBinaryNode {

    @Specialization
    public boolean doEqualsLong(VirtualFrame f, long a, long b) {
        return a == b;
    }

    @Specialization
    public boolean doEqualsDouble(VirtualFrame f, double a, double b) {
        return a == b;
    }

    @Specialization
    public boolean doEqArray(VirtualFrame f, PhpArray a, PhpArray b) {
        throw new UnsupportedOperationException("Array Equality not implemented");
    }

    @Specialization()
    protected Object fallback(Object left, Object right) {
        return left == right;
    }
}
