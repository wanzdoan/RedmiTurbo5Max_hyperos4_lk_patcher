// Export focused functions from the new ENG binary, never original source.
import ghidra.app.script.GhidraScript;
import ghidra.app.decompiler.*;
import ghidra.program.model.listing.*;
import java.io.*;
import java.util.*;

public class EngUnlockFocus extends GhidraScript {
    public void run() throws Exception {
        String[] args = getScriptArgs();
        File out = new File(args[0]); out.mkdirs();
        Set<Function> seeds = new LinkedHashSet<>();
        for (int i=1; i<args.length; i++) {
            var address = toAddr(args[i]);
            Function f = getFunctionAt(address);
            if (f == null) { disassemble(address); f = createFunction(address, null); }
            if (f != null) seeds.add(f);
        }
        Set<Function> selected = new LinkedHashSet<>(seeds);
        for (Function f : seeds) {
            selected.addAll(f.getCallingFunctions(monitor));
            selected.addAll(f.getCalledFunctions(monitor));
        }
        DecompInterface di = new DecompInterface(); di.openProgram(currentProgram);
        try (PrintWriter idx = new PrintWriter(new File(out, "functions.tsv"))) {
            idx.println("address\tname\tseed\tdecompiled");
            for (Function f : selected) {
                if (monitor.isCancelled()) break;
                DecompileResults result = di.decompileFunction(f,30,monitor);
                idx.println(f.getEntryPoint()+"\t"+f.getName()+"\t"+seeds.contains(f)+"\t"+result.decompileCompleted());
                if (result.decompileCompleted() && result.getDecompiledFunction()!=null) {
                    String code = result.getDecompiledFunction().getC();
                    code = code.replaceAll("(?m)[ \\t]+$", "").stripTrailing()+"\n";
                    try (PrintWriter w = new PrintWriter(new File(out,f.getEntryPoint()+".c"))) { w.print(code); }
                }
                try (PrintWriter w = new PrintWriter(new File(out,f.getEntryPoint()+".asm"))) {
                    for (Instruction ins : currentProgram.getListing().getInstructions(f.getBody(),true))
                        w.println(ins.getAddress()+"\t"+ins.toString().stripTrailing());
                }
            }
        } finally { di.dispose(); }
        println("ENG FOCUS seeds="+seeds.size()+" functions="+selected.size());
    }
}
