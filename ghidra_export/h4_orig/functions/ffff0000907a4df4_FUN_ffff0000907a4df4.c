// ffff0000907a4df4  size=120  FUN_ffff0000907a4df4

int FUN_ffff0000907a4df4(long param_1)

{
  int iVar1;
  
  if (param_1 == 0) {
    iVar1 = 0x100006;
  }
  else {
    FUN_ffff00009077ee50(param_1,0,0x20);
    FUN_ffff000090707c70(0xffff000088410000,0x10000);
    iVar1 = FUN_ffff0000907a4b08();
    if (iVar1 == 0) {
      FUN_ffff00009077eb20(param_1,DAT_ffff000090837c80,0x20);
    }
  }
  FUN_ffff0000907b7f64();
  return iVar1;
}


