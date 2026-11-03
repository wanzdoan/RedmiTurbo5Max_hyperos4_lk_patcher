import ghidra.app.script.GhidraScript;
import ghidra.app.decompiler.*;
import ghidra.program.model.listing.*;
import ghidra.program.model.address.*;
import ghidra.program.model.symbol.*;
import java.io.*;
import java.util.*;
public class RadioFocus extends GhidraScript {
 public void run() throws Exception {
  File out=new File(getScriptArgs()[0]);out.mkdirs();Set<Function> chosen=new LinkedHashSet<>();
  FunctionIterator funcs=currentProgram.getFunctionManager().getFunctions(true);
  PrintWriter idx=new PrintWriter(new File(out,"all_functions.tsv"));
  while(funcs.hasNext()) {Function f=funcs.next();String n=f.getName().toLowerCase();idx.println(f.getEntryPoint()+"\t"+f.getName());if(n.matches(".*(nr|5g|nsa|ratconfig|build_region|networktype|preferrednetwork|radiocapability|sbp|mcf).*"))chosen.add(f);}
  idx.close();List<Function> initial=new ArrayList<>(chosen);
  // One layer of callers for selected NR and region functions; bounded to 300 functions.
  for(Function f:initial) {for(Function c:f.getCallingFunctions(monitor)){if(chosen.size()<300)chosen.add(c);}}
  DecompInterface di=new DecompInterface();di.openProgram(currentProgram);PrintWriter selected=new PrintWriter(new File(out,"selected.tsv"));int ok=0;
  for(Function f:chosen){if(monitor.isCancelled())break;DecompileResults r=di.decompileFunction(f,25,monitor);selected.println(f.getEntryPoint()+"\t"+f.getName()+"\t"+r.decompileCompleted());if(r.decompileCompleted()&&r.getDecompiledFunction()!=null){PrintWriter w=new PrintWriter(new File(out,f.getEntryPoint()+".c"));w.println(r.getDecompiledFunction().getC());w.close();ok++;}}
  selected.close();di.dispose();println("NR FOCUS functions="+chosen.size()+" exports="+ok);
 }
}
