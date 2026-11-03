
int FUN_ffff0000be07fe60(long param_1,undefined8 param_2,long param_3)

{
  int iVar1;
  undefined8 uVar2;
  
  iVar1 = 0x100006;
  if ((param_1 != 0) && (param_3 != 0)) {
    iVar1 = FUN_ffff0000be07fc44();
    if (iVar1 == 0) {
      FUN_ffff0000be091d70();
      FUN_ffff0000be007328(0xffff000088410000,0x10000);
      FUN_ffff0000be007328(param_1,param_2);
      uVar2 = FUN_ffff0000be05d228(param_1);
      iVar1 = FUN_ffff0000be07fbc4(uVar2,param_2);
      if (iVar1 == 0) {
        FUN_ffff0000be0607b0(param_3,DAT_ffff0000be0f3e58,0x20);
      }
      FUN_ffff0000be091d88();
    }
  }
  return iVar1;
}


