// ffff000090722694  size=92  FUN_ffff000090722694

void FUN_ffff000090722694(uint param_1)

{
  int iVar1;
  undefined4 uVar2;
  
  iVar1 = FUN_ffff000090703be8();
  if (iVar1 != 0) {
    if ((param_1 & 0xff) == 10) {
      uVar2 = FUN_ffff000090703bfc();
      FUN_ffff000090742a68(uVar2,0xd);
    }
    uVar2 = FUN_ffff000090703bfc();
    FUN_ffff000090742a68(uVar2,param_1);
    return;
  }
  return;
}


