// MarkCode.java - recover function starts in a PAC-protected AArch64 raw binary.
// Scans executable memory for paciasp / pacibsp / bti+paciasp prologues and
// creates functions there so the decompiler can see them.
//@category LK

import ghidra.app.script.GhidraScript;
import ghidra.app.cmd.disassemble.DisassembleCommand;
import ghidra.app.cmd.function.CreateFunctionCmd;
import ghidra.program.model.address.Address;
import ghidra.program.model.address.AddressSet;
import ghidra.program.model.address.AddressSetView;
import ghidra.program.model.listing.Function;
import ghidra.program.model.mem.MemoryAccessException;

public class MarkCode extends GhidraScript {

    private static final int PACIASP = 0xD503233F;
    private static final int PACIBSP = 0xD503237F;
    private static final int BTI_C   = 0xD503245F;
    private static final int BTI_J   = 0xD503249F;

    @Override
    public void run() throws Exception {
        AddressSetView exec = currentProgram.getMemory().getExecuteSet();
        println("executable set: " + exec.getNumAddresses() + " bytes");

        AddressSet todo = new AddressSet();
        int hits = 0;
        for (Address a : exec.getAddresses(true)) {
            long off = a.getOffset();
            if ((off & 3) != 0) {
                continue;
            }
            int w;
            try {
                w = currentProgram.getMemory().getInt(a);
            } catch (MemoryAccessException e) {
                continue;
            }
            if (w == PACIASP || w == PACIBSP) {
                Address start = a;
                try {
                    int prev = currentProgram.getMemory().getInt(a.subtract(4));
                    if (prev == BTI_C || prev == BTI_J) {
                        start = a.subtract(4);
                    }
                } catch (Exception e) {
                    // ignore
                }
                if (currentProgram.getFunctionManager().getFunctionAt(start) == null) {
                    todo.add(start);
                    hits++;
                }
            }
        }
        println("prologue candidates without function: " + hits);

        // pass 1: disassemble (recursive flow)
        for (Address a : todo.getAddresses(true)) {
            DisassembleCommand cmd = new DisassembleCommand(a, null, true);
            cmd.applyTo(currentProgram, monitor);
        }
        // pass 2: create functions
        int made = 0;
        for (Address a : todo.getAddresses(true)) {
            if (currentProgram.getFunctionManager().getFunctionAt(a) != null) {
                continue;
            }
            CreateFunctionCmd cmd = new CreateFunctionCmd(a);
            if (cmd.applyTo(currentProgram, monitor)) {
                made++;
            }
        }
        println("functions created: " + made);
        println("total functions now: " + currentProgram.getFunctionManager().getFunctionCount());
    }
}
