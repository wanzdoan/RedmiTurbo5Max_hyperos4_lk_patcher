
int FUN_ffff0000907a5d20(long param_1,undefined8 param_2)

{
  int iVar1;
  undefined8 uVar2;
  
  if (param_1 == 0) {
    iVar1 = 0x100006;
  }
  else {
    FUN_ffff000090707c70();
    uVar2 = FUN_ffff00009077b334(param_1);
    iVar1 = FUN_ffff0000907a5bdc(uVar2,param_2);
    if (iVar1 != 0) {
      FUN_ffff0000907b7f64();
    }
  }
  return iVar1;
}


