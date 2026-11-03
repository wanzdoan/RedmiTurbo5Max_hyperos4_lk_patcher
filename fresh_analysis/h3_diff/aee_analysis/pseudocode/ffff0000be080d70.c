
int FUN_ffff0000be080d70(long param_1)

{
  int iVar1;
  
  if (param_1 == 0) {
    iVar1 = 0x100006;
  }
  else {
    FUN_ffff0000be060ae0(param_1,0,0x30);
    FUN_ffff0000be007328(0xffff000088410000,0x10000);
    iVar1 = FUN_ffff0000be080c50();
    if (iVar1 != 0) {
      return iVar1;
    }
    FUN_ffff0000be0607b0(param_1,*(undefined8 *)PTR_DAT_ffff0000be0f5718,0x30);
    iVar1 = 0;
  }
  FUN_ffff0000be091d88();
  return iVar1;
}


