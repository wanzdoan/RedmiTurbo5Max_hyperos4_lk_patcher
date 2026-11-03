// Focused static exports. Run on a disposable database or with -readOnly.
import ghidra.app.script.GhidraScript;
import ghidra.app.decompiler.*;
import ghidra.program.model.listing.*;
import ghidra.program.model.symbol.*;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class TraceRadioNative extends GhidraScript {
    public void run() throws Exception {
        String[] args = getScriptArgs();
        File out = new File(args[0]);
        out.mkdirs();
        Pattern pattern = Pattern.compile(String.join("|", Arrays.copyOfRange(args, 1, args.length)).replace(',', '|'), Pattern.CASE_INSENSITIVE);
        Set<Function> seeds = new LinkedHashSet<>();
        for (Function f : currentProgram.getFunctionManager().getFunctions(true)) {
            if (pattern.matcher(f.getName()).find()) seeds.add(f);
        }
        Set<Function> selected = new LinkedHashSet<>(seeds);
        for (Function f : seeds) {
            selected.addAll(f.getCallingFunctions(monitor));
            selected.addAll(f.getCalledFunctions(monitor));
        }
        DecompInterface di = new DecompInterface();
        di.openProgram(currentProgram);
        try (PrintWriter index = new PrintWriter(new File(out, "functions.tsv"));
             PrintWriter edges = new PrintWriter(new File(out, "calls.tsv"))) {
            index.println("address\tname\tseed\tdecompiled");
            edges.println("caller\tcallee\tkind");
            for (Function f : selected) {
                if (monitor.isCancelled()) break;
                DecompileResults result = di.decompileFunction(f, 30, monitor);
                index.println(f.getEntryPoint() + "\t" + f.getName() + "\t" + seeds.contains(f) + "\t" + result.decompileCompleted());
                for (Function target : f.getCalledFunctions(monitor)) {
                    edges.println(f.getEntryPoint() + "\t" + target.getEntryPoint() + "\tdirect");
                }
                if (result.decompileCompleted() && result.getDecompiledFunction() != null) {
                    try (PrintWriter writer = new PrintWriter(new File(out, f.getEntryPoint() + ".c"))) {
                        writer.print(result.getDecompiledFunction().getC());
                    }
                }
                try (PrintWriter writer = new PrintWriter(new File(out, f.getEntryPoint() + ".asm"))) {
                    for (Instruction ins : currentProgram.getListing().getInstructions(f.getBody(), true)) {
                        writer.println(ins.getAddress() + "\t" + ins);
                    }
                }
            }
        } finally { di.dispose(); }
        println("CAUSAL TRACE: seeds=" + seeds.size() + " exported=" + selected.size());
    }
}
