package org.lice.ast;

import org.lice.runtime.LiceFunction;
import org.lice.runtime.Unit;

import javax.script.Bindings;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Glavo on 17-9-22.
 *
 * @author Glavo
 * @since 4.0.0
 */
@SuppressWarnings("WeakerAccess")
public class ApplyNode extends Node {
    public final List<Node> nodes;

    public ApplyNode() {
        this.nodes = new ArrayList<>();
    }

    public ApplyNode(List<Node> nodes) {
        this.nodes = nodes;
    }

    public ApplyNode(Node... nodes) {
        this.nodes = Arrays.asList(nodes);
    }

    @Override
    public Object eval(Bindings bindings) throws ScriptException {
        if (nodes == null || nodes.isEmpty()) return Unit.unit;
        Object fun = nodes.get(0).eval(bindings);
        List<Node> args = nodes.subList(1, nodes.size());

        if (fun instanceof LiceFunction) {
            LiceFunction fun1 = (LiceFunction) fun;
            fun1.apply(this.getEngine().getContext(), args);
        }

        return null; //TODO
    }
}
