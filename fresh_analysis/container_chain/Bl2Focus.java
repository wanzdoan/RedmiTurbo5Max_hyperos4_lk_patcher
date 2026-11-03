import ghidra.app.script.GhidraScript;
import ghidra.app.decompiler.*;
import ghidra.program.model.listing.*;
import ghidra.program.model.address.*;
import java.io.*;
import java.util.*;
public class Bl2Focus extends GhidraScript {
 public void run() throws Exception {
  long delta=0xffff0000b8000000L;
  long[] seeds={0x6e584,0x6cf8c,0x6c4c4,0x9174,0x3dd94};
  ArrayDeque<Address> queue=new ArrayDeque<>();
  for(long x:seeds)queue.add(toAddr(x+delta));
  // First seed known exact function starts by PACIASP in bounded auth region.
  for(long off=0;off<0;off+=4)if(getInt(toAddr(delta+off))==0xd503233f)queue.add(toAddr(delta+off));
  for(long off=0;off<0;off+=4)if(getInt(toAddr(delta+off))==0xd503233f)queue.add(toAddr(delta+off));
  Set<Long> seen=new HashSet<>();
  DecompInterface decomp=new DecompInterface();decomp.openProgram(currentProgram);
  File dir=new File(getScriptArgs()[0]);dir.mkdirs();
  while(!queue.isEmpty() && seen.size()<130){
   Address addr=queue.removeFirst();if(!seen.add(addr.getOffset()))continue;
   disassemble(addr); Function f=getFunctionAt(addr);if(f==null)f=createFunction(addr,null);if(f==null)continue;
   DecompileResults r=decomp.decompileFunction(f,30,monitor);
   try(PrintWriter w=new PrintWriter(new File(dir,String.format("%08x.c",addr.getOffset()-delta)))){
    w.println("// File offset entry: "+String.format("0x%x",addr.getOffset()-delta));
    w.println(r.decompileCompleted()?r.getDecompiledFunction().getC():r.getErrorMessage());
   }
   for(Function callee:f.getCalledFunctions(monitor))queue.add(callee.getEntryPoint());
   InstructionIterator ii=currentProgram.getListing().getInstructions(f.getBody(),true);
   while(ii.hasNext()) {Instruction i=ii.next();if(i.getMnemonicString().equals("bl"))for(Address target:i.getFlows())if(target.getOffset()>=delta && target.getOffset()<delta+0x85000)queue.add(target);}
  }
  decomp.dispose();println("Exported functions: "+seen.size());
 }
}
