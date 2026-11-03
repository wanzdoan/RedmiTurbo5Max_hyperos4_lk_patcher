
int FUN_ffff0000b8063354(long param_1,undefined8 param_2)

{
  int iVar1;
  undefined8 uVar2;
  
  if (param_1 == 0) {
    iVar1 = 0x100006;
  }
  else {
    FUN_ffff0000b80075ac();
    uVar2 = FUN_ffff0000b8049ec8(param_1);
    iVar1 = FUN_ffff0000b8063210(uVar2,param_2);
    if (iVar1 != 0) {
      FUN_ffff0000b807312c();
    }
  }
  return iVar1;
}


