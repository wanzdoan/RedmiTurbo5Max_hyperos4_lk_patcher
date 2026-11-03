// ffff0000907a5abc  size=124  FUN_ffff0000907a5abc

int FUN_ffff0000907a5abc(long param_1)

{
  int iVar1;
  
  if (param_1 == 0) {
    iVar1 = 0x100006;
  }
  else {
    FUN_ffff00009077eb88(param_1,0,0x30);
    FUN_ffff000090707ce4(0xffff000088410000,0x10000);
    iVar1 = FUN_ffff0000907a599c();
    if (iVar1 != 0) {
      return iVar1;
    }
    FUN_ffff00009077e858(param_1,*(undefined8 *)PTR_DAT_ffff000090839a78,0x30);
    iVar1 = 0;
  }
  FUN_ffff0000907b78f0();
  return iVar1;
}


