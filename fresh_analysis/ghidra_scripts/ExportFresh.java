// Export recovered pseudocode and function metadata. This is not original source.
import ghidra.app.script.GhidraScript;
import ghidra.app.decompiler.*;
import ghidra.program.model.listing.*;
import java.io.*;
public class ExportFresh extends GhidraScript {
 public void run() throws Exception {
  File out=new File(getScriptArgs()[0]);out.mkdirs();
  DecompInterface dec=new DecompInterface();dec.openProgram(currentProgram);
  PrintWriter idx=new PrintWriter(new File(out,"functions.tsv"));
  FunctionIterator fs=currentProgram.getFunctionManager().getFunctions(true);
  int n=0,ok=0;
  while(fs.hasNext()&&!monitor.isCancelled()) {
   Function f=fs.next();n++;String addr=f.getEntryPoint().toString();
   DecompileResults r=dec.decompileFunction(f,30,monitor);
   boolean success=r.decompileCompleted()&&r.getDecompiledFunction()!=null;
   idx.println(addr+"\t"+f.getName()+"\t"+f.getBody().getNumAddresses()+"\t"+success);
   if(success){ok++;PrintWriter w=new PrintWriter(new File(out,addr+".c"));w.println(r.getDecompiledFunction().getC());w.close();}
   if(n%500==0)println("EXPORT "+n+" / "+ok);
  }
  idx.close();dec.dispose();println("EXPORT COMPLETE functions="+n+" decompiled="+ok);
 }
}
