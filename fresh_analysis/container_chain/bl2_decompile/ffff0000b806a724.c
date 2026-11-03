
undefined8 FUN_ffff0000b806a724(undefined8 param_1,int param_2)

{
  undefined *puVar1;
  undefined8 uVar2;
  
  if (param_2 == 0x200) {
    puVar1 = &DAT_ffff0000b81b8600;
    uVar2 = 0x200;
  }
  else {
    if (param_2 != 0x180) {
      if (param_2 == 0x100) {
        FUN_ffff0000b804cd40(&DAT_ffff0000b81b8380,param_1,0x100);
        DAT_ffff0000b81b8840 = &DAT_ffff0000b81b8380;
        return 0;
      }
      return 0x10101e;
    }
    puVar1 = &DAT_ffff0000b81b8480;
    uVar2 = 0x180;
  }
  FUN_ffff0000b804cd40(puVar1,param_1,uVar2);
  return 0;
}


