// ffff000090709acc  size=96  FUN_ffff000090709acc

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090709acc(undefined8 param_1,undefined8 param_2)

{
  int iVar1;
  long *plVar2;
  
  for (plVar2 = (long *)_DAT_ffff00009083d168; plVar2 != (long *)0x0; plVar2 = (long *)*plVar2) {
    iVar1 = FUN_ffff00009077ede4(param_1,plVar2[1]);
    if (iVar1 == 0) {
      plVar2[2] = param_2;
    }
  }
  return;
}


