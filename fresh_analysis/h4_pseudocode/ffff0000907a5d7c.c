
int FUN_ffff0000907a5d7c(long param_1)

{
  int iVar1;
  
  if (param_1 == 0) {
    iVar1 = 0x100006;
  }
  else {
    FUN_ffff00009077ee50(param_1,0,0x30);
    FUN_ffff000090707c70(0xffff000088410000,0x10000);
    iVar1 = FUN_ffff0000907a5c5c();
    if (iVar1 != 0) {
      return iVar1;
    }
    FUN_ffff00009077eb20(param_1,*(undefined8 *)PTR_DAT_ffff000090839a98,0x30);
    iVar1 = 0;
  }
  FUN_ffff0000907b7f64();
  return iVar1;
}


