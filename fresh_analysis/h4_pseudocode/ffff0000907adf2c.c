
undefined8 FUN_ffff0000907adf2c(undefined8 param_1,int param_2)

{
  undefined *puVar1;
  undefined8 uVar2;
  
  if (param_2 == 0x200) {
    puVar1 = &DAT_ffff00009090c3c8;
    uVar2 = 0x200;
  }
  else {
    if (param_2 != 0x180) {
      if (param_2 == 0x100) {
        FUN_ffff00009077eb20(&DAT_ffff00009090c148,param_1,0x100);
        DAT_ffff00009090c608 = &DAT_ffff00009090c148;
        return 0;
      }
      return 0x10101e;
    }
    puVar1 = &DAT_ffff00009090c248;
    uVar2 = 0x180;
  }
  FUN_ffff00009077eb20(puVar1,param_1,uVar2);
  return 0;
}


