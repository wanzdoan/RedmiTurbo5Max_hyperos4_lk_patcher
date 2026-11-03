
int FUN_ffff0000b80633b0(long param_1)

{
  int iVar1;
  
  if (param_1 == 0) {
    iVar1 = 0x100006;
  }
  else {
    FUN_ffff0000b804d070(param_1,0,0x30);
    FUN_ffff0000b80075ac(0xffff000088410000,0x10000);
    iVar1 = FUN_ffff0000b8063290();
    if (iVar1 != 0) {
      return iVar1;
    }
    FUN_ffff0000b804cd40(param_1,*(undefined8 *)PTR_DAT_ffff0000b81087e8,0x30);
    iVar1 = 0;
  }
  FUN_ffff0000b807312c();
  return iVar1;
}


