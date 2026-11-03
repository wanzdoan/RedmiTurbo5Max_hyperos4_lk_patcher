
undefined8 FUN_ffff0000be088120(undefined8 param_1,int param_2)

{
  undefined *puVar1;
  undefined8 uVar2;
  
  if (param_2 == 0x200) {
    puVar1 = &DAT_ffff0000be1b68c0;
    uVar2 = 0x200;
  }
  else {
    if (param_2 != 0x180) {
      if (param_2 == 0x100) {
        FUN_ffff0000be0607b0(&DAT_ffff0000be1b6640,param_1,0x100);
        DAT_ffff0000be1b6b00 = &DAT_ffff0000be1b6640;
        return 0;
      }
      return 0x10101e;
    }
    puVar1 = &DAT_ffff0000be1b6740;
    uVar2 = 0x180;
  }
  FUN_ffff0000be0607b0(puVar1,param_1,uVar2);
  return 0;
}


