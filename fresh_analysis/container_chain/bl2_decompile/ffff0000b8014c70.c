
void FUN_ffff0000b8014c70(uint param_1)

{
  int iVar1;
  undefined4 uVar2;
  
  iVar1 = FUN_ffff0000b8003af8();
  if (iVar1 != 0) {
    if ((param_1 & 0xff) == 10) {
      uVar2 = FUN_ffff0000b8003b0c();
      FUN_ffff0000b80295cc(uVar2,0xd);
    }
    uVar2 = FUN_ffff0000b8003b0c();
    FUN_ffff0000b80295cc(uVar2,param_1);
    return;
  }
  return;
}


