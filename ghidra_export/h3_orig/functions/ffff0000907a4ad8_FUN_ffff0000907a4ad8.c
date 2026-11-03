// ffff0000907a4ad8  size=92  FUN_ffff0000907a4ad8

int FUN_ffff0000907a4ad8(long param_1,undefined8 param_2)

{
  int iVar1;
  undefined8 uVar2;
  
  if (param_1 == 0) {
    iVar1 = 0x100006;
  }
  else {
    FUN_ffff000090707ce4();
    uVar2 = FUN_ffff00009077b06c(param_1);
    iVar1 = FUN_ffff0000907a47c8(uVar2,param_2);
    if (iVar1 == 0) {
      return 0;
    }
  }
  FUN_ffff0000907b78f0();
  return iVar1;
}


