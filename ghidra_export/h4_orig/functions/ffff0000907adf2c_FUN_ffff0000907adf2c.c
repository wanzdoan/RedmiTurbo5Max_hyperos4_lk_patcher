// ffff0000907adf2c  size=156  FUN_ffff0000907adf2c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907adf2c(undefined8 param_1,int param_2)

{
  undefined8 uVar1;
  undefined8 uVar2;
  
  if (param_2 == 0x200) {
    uVar1 = 0xffff00009090c3c8;
    uVar2 = 0x200;
  }
  else {
    if (param_2 != 0x180) {
      if (param_2 == 0x100) {
        FUN_ffff00009077eb20(0xffff00009090c148,param_1,0x100);
        _DAT_ffff00009090c608 = 0xffff00009090c148;
        return 0;
      }
      return 0x10101e;
    }
    uVar1 = 0xffff00009090c248;
    uVar2 = 0x180;
  }
  FUN_ffff00009077eb20(uVar1,param_1,uVar2);
  return 0;
}


