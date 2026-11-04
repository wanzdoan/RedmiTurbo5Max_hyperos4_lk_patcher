import ghidra.app.script.GhidraScript;
import ghidra.app.decompiler.*;
import ghidra.program.model.listing.*;
import java.io.*;
import java.util.*;
public class PolicyFocus extends GhidraScript {
 public void run() throws Exception {
  File out=new File(getScriptArgs()[0]);out.mkdirs();
  Set<Function> selected=new LinkedHashSet<>();
  String[] terms={"CN devices", "userdebug", "not allowed", "not allow", "china", "global", "Is_Secure_ID", "EP ROM", "ep rom", "region img", "lock_state"};
  try(PrintWriter refs=new PrintWriter(new File(out,"string_refs.tsv"))){
   var it=currentProgram.getListing().getDefinedData(true);
   while(it.hasNext()){
    Data d=it.next();Object val=d.getValue();if(!(val instanceof String))continue;
    String s=(String)val;boolean match=false;
    for(String term:terms)if(s.toLowerCase().contains(term.toLowerCase()))match=true;
    if(!match)continue;
    var rs=getReferencesTo(d.getAddress());
    for(var r:rs){Function f=getFunctionContaining(r.getFromAddress());
     refs.println(d.getAddress()+"\t"+r.getFromAddress()+"\t"+(f==null?"none":f.getEntryPoint())+"\t"+s.replace("\n"," "));
     if(f!=null)selected.add(f);
    }
   }
  }
  for(int i=1;i<getScriptArgs().length;i++){
   Function f=getFunctionAt(toAddr(getScriptArgs()[i]));if(f!=null)selected.add(f);
  }
  DecompInterface di=new DecompInterface();di.openProgram(currentProgram);
  try(PrintWriter idx=new PrintWriter(new File(out,"functions.tsv"))){
   for(Function f:selected){
    DecompileResults r=di.decompileFunction(f,30,monitor);
    idx.println(f.getEntryPoint()+"\t"+f.getName()+"\t"+r.decompileCompleted());
    if(r.decompileCompleted()&&r.getDecompiledFunction()!=null){
     try(PrintWriter w=new PrintWriter(new File(out,f.getEntryPoint()+".c"))){w.print(r.getDecompiledFunction().getC());}
    }
    try(PrintWriter w=new PrintWriter(new File(out,f.getEntryPoint()+".asm"))){
     for(Instruction ins:currentProgram.getListing().getInstructions(f.getBody(),true))w.println(ins.getAddress()+"\t"+ins.toString());
    }
   }
  }finally{di.dispose();}println("selected="+selected.size());
 }
}
