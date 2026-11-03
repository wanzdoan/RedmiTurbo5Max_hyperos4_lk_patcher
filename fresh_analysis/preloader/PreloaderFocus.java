import ghidra.app.script.GhidraScript;
import ghidra.app.decompiler.*;
import ghidra.program.model.listing.*;
import ghidra.program.model.address.*;
import java.io.*;
import java.util.*;
public class PreloaderFocus extends GhidraScript {
 public void run() throws Exception {
  long delta=0x1ffff00L;
  long[] seeds={0x7ec00,0x7ed00,0x7f300,0xa5200,0xa55d0,0x8c934,0x8cc18};
  ArrayDeque<Address> queue=new ArrayDeque<>();
  for(long x:seeds)queue.add(toAddr(x+delta));
  // First seed known exact function starts by PACIASP in bounded auth region.
  for(long off=0x7e400;off<0x7f800;off+=4)if(getInt(toAddr(delta+off))==0xd503233f)queue.add(toAddr(delta+off));
  for(long off=0xa4800;off<0xa5800;off+=4)if(getInt(toAddr(delta+off))==0xd503233f)queue.add(toAddr(delta+off));
  Set<Long> seen=new HashSet<>();
  DecompInterface decomp=new DecompInterface();decomp.openProgram(currentProgram);
  File dir=new File(getScriptArgs()[0]);dir.mkdirs();
  while(!queue.isEmpty() && seen.size()<220){
   Address addr=queue.removeFirst();if(!seen.add(addr.getOffset()))continue;
   disassemble(addr); Function f=getFunctionAt(addr);if(f==null)f=createFunction(addr,null);if(f==null)continue;
   DecompileResults r=decomp.decompileFunction(f,30,monitor);
   try(PrintWriter w=new PrintWriter(new File(dir,String.format("%08x.c",addr.getOffset()-delta)))){
    w.println("// File offset entry: "+String.format("0x%x",addr.getOffset()-delta));
    w.println(r.decompileCompleted()?r.getDecompiledFunction().getC():r.getErrorMessage());
   }
   for(Function callee:f.getCalledFunctions(monitor))queue.add(callee.getEntryPoint());
   InstructionIterator ii=currentProgram.getListing().getInstructions(f.getBody(),true);
   while(ii.hasNext()) {Instruction i=ii.next();if(i.getMnemonicString().equals("bl"))for(Address target:i.getFlows())if(target.getOffset()>=delta+0x100 && target.getOffset()<delta+0xaf400)queue.add(target);}
  }
  decomp.dispose();println("Exported functions: "+seen.size());
 }
}
