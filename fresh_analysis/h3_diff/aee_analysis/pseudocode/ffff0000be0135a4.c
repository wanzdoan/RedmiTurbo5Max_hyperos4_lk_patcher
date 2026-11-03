
void FUN_ffff0000be0135a4(undefined8 param_1,undefined8 param_2)

{
  int iVar1;
  long *plVar2;
  
  for (plVar2 = (long *)DAT_ffff0000be107958; plVar2 != (long *)0x0; plVar2 = (long *)*plVar2) {
    iVar1 = FUN_ffff0000be060cb8(param_1,plVar2[1]);
    if (iVar1 == 0) {
      plVar2[2] = param_2;
    }
  }
  return;
}


