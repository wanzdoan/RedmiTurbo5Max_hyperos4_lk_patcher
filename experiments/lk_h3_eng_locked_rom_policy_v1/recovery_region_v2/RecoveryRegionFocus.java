import ghidra.app.script.GhidraScript;
import ghidra.app.decompiler.*;
import ghidra.program.model.listing.*;
import java.io.*;
import java.util.*;
public class RecoveryRegionFocus extends GhidraScript {
 public void run() throws Exception {
  File out=new File(getScriptArgs()[0]);out.mkdirs();
  Set<Function> seeds=new LinkedHashSet<>();
  String[] terms={"ro.boot.hwc","ro.product.mod_device","ro.boot.verifiedbootstate","device rom zone invalid","rom zone mismatch","Get device rom zone."};
  try(PrintWriter refs=new PrintWriter(new File(out,"string_refs.tsv"))){
   var it=currentProgram.getListing().getDefinedData(true);
   while(it.hasNext()){
    Data d=it.next();Object v=d.getValue();if(!(v instanceof String))continue;
    String s=(String)v;boolean match=false;for(String term:terms)if(s.contains(term))match=true;
    if(!match)continue;
    for(var r:getReferencesTo(d.getAddress())){
     Function f=getFunctionContaining(r.getFromAddress());
     refs.println(d.getAddress()+"\t"+r.getFromAddress()+"\t"+(f==null?"none":f.getEntryPoint())+"\t"+s.strip());
     if(f!=null)seeds.add(f);
    }
   }
  }
  for(int i=1;i<getScriptArgs().length;i++){
   Function f=getFunctionAt(toAddr(getScriptArgs()[i]));if(f!=null)seeds.add(f);
  }
  Set<Function> selected=new LinkedHashSet<>(seeds);
  for(Function f:seeds){selected.addAll(f.getCallingFunctions(monitor));selected.addAll(f.getCalledFunctions(monitor));}
  DecompInterface di=new DecompInterface();di.openProgram(currentProgram);
  try(PrintWriter idx=new PrintWriter(new File(out,"functions.tsv"))){
   for(Function f:selected){
    DecompileResults r=di.decompileFunction(f,30,monitor);idx.println(f.getEntryPoint()+"\t"+f.getName()+"\t"+seeds.contains(f)+"\t"+r.decompileCompleted());
    if(r.decompileCompleted()&&r.getDecompiledFunction()!=null){try(PrintWriter w=new PrintWriter(new File(out,f.getEntryPoint()+".c"))){w.print(r.getDecompiledFunction().getC());}}
    try(PrintWriter w=new PrintWriter(new File(out,f.getEntryPoint()+".asm"))){for(Instruction ins:currentProgram.getListing().getInstructions(f.getBody(),true))w.println(ins.getAddress()+"\t"+ins.toString());}
   }
  }finally{di.dispose();}println("region seeds="+seeds.size()+" selected="+selected.size());
 }
}
