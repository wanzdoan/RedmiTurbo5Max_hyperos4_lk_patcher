// ffff0000907a4b34  size=120  FUN_ffff0000907a4b34

int FUN_ffff0000907a4b34(long param_1)

{
  int iVar1;
  
  if (param_1 == 0) {
    iVar1 = 0x100006;
  }
  else {
    FUN_ffff00009077eb88(param_1,0,0x20);
    FUN_ffff000090707ce4(0xffff000088410000,0x10000);
    iVar1 = FUN_ffff0000907a4848();
    if (iVar1 == 0) {
      FUN_ffff00009077e858(param_1,DAT_ffff000090837c60,0x20);
    }
  }
  FUN_ffff0000907b78f0();
  return iVar1;
}


