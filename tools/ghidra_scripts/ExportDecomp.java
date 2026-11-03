// ExportDecomp.java - dump full decompiled source, strings, symbols and call graph.
//@category LK

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ghidra.app.decompiler.DecompInterface;
import ghidra.app.decompiler.DecompileResults;
import ghidra.app.script.GhidraScript;
import ghidra.program.model.address.Address;
import ghidra.program.model.listing.Data;
import ghidra.program.model.listing.DataIterator;
import ghidra.program.model.listing.Function;
import ghidra.program.model.listing.FunctionIterator;
import ghidra.program.model.listing.Instruction;
import ghidra.program.model.listing.InstructionIterator;
import ghidra.program.model.symbol.Reference;
import ghidra.program.model.symbol.ReferenceIterator;
import ghidra.program.model.symbol.Symbol;
import ghidra.program.model.symbol.SymbolIterator;

public class ExportDecomp extends GhidraScript {

    private static String sanitize(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (Character.isLetterOrDigit(c) || c == '_' || c == '.' || c == '-') {
                sb.append(c);
            } else {
                sb.append('_');
            }
        }
        String r = sb.toString();
        if (r.length() > 80) {
            r = r.substring(0, 80);
        }
        return r;
    }

    private static String hex(long v) {
        return String.format("%08x", v);
    }

    @Override
    public void run() throws Exception {
        String[] args = getScriptArgs();
        String outDir = (args.length > 0) ? args[0] : ".";
        File dir = new File(outDir);
        dir.mkdirs();

        String progName = currentProgram.getName().replace(".bin", "");
        File fnDir = new File(dir, "functions");
        fnDir.mkdirs();

        DecompInterface ifc = new DecompInterface();
        ifc.toggleCCode(true);
        ifc.toggleSyntaxTree(false);
        ifc.setSimplificationStyle("decompile");
        if (!ifc.openProgram(currentProgram)) {
            println("decompiler failed to open program");
            return;
        }

        PrintWriter all = new PrintWriter(new File(dir, "all_decompiled.c"), "UTF-8");
        PrintWriter fns = new PrintWriter(new File(dir, "functions.tsv"), "UTF-8");
        PrintWriter cg = new PrintWriter(new File(dir, "callgraph.tsv"), "UTF-8");
        PrintWriter js = new PrintWriter(new File(dir, "functions.json"), "UTF-8");

        all.println("// " + progName + " : full decompiled source");
        all.println("// image base 0x" + hex(currentProgram.getImageBase().getOffset()));
        all.println();

        js.println("[");
        boolean firstJson = true;
        int total = 0;
        int failed = 0;

        FunctionIterator fit = currentProgram.getFunctionManager().getFunctions(true);
        List<Function> funcs = new ArrayList<>();
        while (fit.hasNext()) {
            funcs.add(fit.next());
        }
        println("functions: " + funcs.size());

        for (Function f : funcs) {
            if (monitor.isCancelled()) {
                break;
            }
            long ea = f.getEntryPoint().getOffset();
            String name = f.getName();
            long size = f.getBody().getNumAddresses();
            String sig = f.getPrototypeString(false, false);

            fns.println(hex(ea) + "\t" + size + "\t" + name + "\t" + sig.replace('\t', ' '));

            // call graph edges
            Set<Long> callees = new HashSet<>();
            InstructionIterator iit = currentProgram.getListing().getInstructions(f.getBody(), true);
            while (iit.hasNext()) {
                Instruction ins = iit.next();
                if (!ins.getFlowType().isCall()) {
                    continue;
                }
                for (Address t : ins.getFlows()) {
                    callees.add(t.getOffset());
                }
            }
            for (Long t : callees) {
                Function cf = currentProgram.getFunctionManager().getFunctionAt(
                        currentProgram.getAddressFactory().getDefaultAddressSpace().getAddress(t));
                cg.println(hex(ea) + "\t" + hex(t) + "\t" + (cf != null ? cf.getName() : "?"));
            }

            DecompileResults res = ifc.decompileFunction(f, 90, monitor);
            String body;
            boolean ok = res != null && res.decompileCompleted() && res.getDecompiledFunction() != null;
            if (ok) {
                body = res.getDecompiledFunction().getC();
            } else {
                body = "// DECOMPILE FAILED: " + (res == null ? "null" : res.getErrorMessage());
                failed++;
            }
            total++;

            String header = "// " + hex(ea) + "  size=" + size + "  " + name;
            all.println();
            all.println("/* ============================================================ */");
            all.println(header);
            all.println("/* ============================================================ */");
            all.println(body);

            File one = new File(fnDir, hex(ea) + "_" + sanitize(name) + ".c");
            PrintWriter pw = new PrintWriter(one, "UTF-8");
            pw.println(header);
            pw.println(body);
            pw.close();

            if (!firstJson) {
                js.println(",");
            }
            firstJson = false;
            js.print("{\"ea\":\"" + hex(ea) + "\",\"size\":" + size + ",\"name\":\"" +
                    name.replace("\\", "\\\\").replace("\"", "\\\"") + "\",\"ok\":" + ok + "}");
        }
        js.println();
        js.println("]");
        js.close();
        all.close();
        fns.close();
        cg.close();

        // ---- strings ----
        PrintWriter str = new PrintWriter(new File(dir, "strings.tsv"), "UTF-8");
        DataIterator dit = currentProgram.getListing().getDefinedData(true);
        int nstr = 0;
        while (dit.hasNext()) {
            Data d = dit.next();
            Object v = d.getValue();
            if (v instanceof String) {
                String s = (String) v;
                StringBuilder xr = new StringBuilder();
                ReferenceIterator rit = currentProgram.getReferenceManager()
                        .getReferencesTo(d.getAddress());
                while (rit.hasNext()) {
                    Reference r = rit.next();
                    Function cf = currentProgram.getFunctionManager()
                            .getFunctionContaining(r.getFromAddress());
                    xr.append(hex(r.getFromAddress().getOffset()));
                    if (cf != null) {
                        xr.append("(").append(cf.getName()).append(")");
                    }
                    xr.append(" ");
                }
                str.println(hex(d.getAddress().getOffset()) + "\t" +
                        s.replace("\\", "\\\\").replace("\t", "\\t").replace("\n", "\\n")
                         .replace("\r", "\\r") + "\t" + xr.toString().trim());
                nstr++;
            }
        }
        str.close();

        // ---- symbols ----
        PrintWriter sym = new PrintWriter(new File(dir, "symbols.tsv"), "UTF-8");
        SymbolIterator sit = currentProgram.getSymbolTable().getAllSymbols(true);
        int nsym = 0;
        while (sit.hasNext()) {
            Symbol s = sit.next();
            sym.println(hex(s.getAddress().getOffset()) + "\t" + s.getSymbolType() + "\t" +
                    s.getName(true));
            nsym++;
        }
        sym.close();

        // ---- memory blocks ----
        PrintWriter mem = new PrintWriter(new File(dir, "memory.tsv"), "UTF-8");
        for (ghidra.program.model.mem.MemoryBlock b : currentProgram.getMemory().getBlocks()) {
            mem.println(b.getName() + "\t" + hex(b.getStart().getOffset()) + "\t" +
                    hex(b.getEnd().getOffset()) + "\t" + b.getSize() + "\t" +
                    "R=" + b.isRead() + " W=" + b.isWrite() + " X=" + b.isExecute());
        }
        mem.close();

        println("exported functions=" + total + " failed=" + failed + " strings=" + nstr +
                " symbols=" + nsym);
        ifc.dispose();
    }
}
