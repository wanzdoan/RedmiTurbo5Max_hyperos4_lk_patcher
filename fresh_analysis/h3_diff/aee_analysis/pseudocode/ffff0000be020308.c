
void FUN_ffff0000be020308(uint param_1)

{
  int iVar1;
  undefined4 uVar2;
  
  iVar1 = FUN_ffff0000be003748();
  if (iVar1 != 0) {
    if ((param_1 & 0xff) == 10) {
      uVar2 = FUN_ffff0000be00375c();
      FUN_ffff0000be0323f4(uVar2,0xd);
    }
    uVar2 = FUN_ffff0000be00375c();
    FUN_ffff0000be0323f4(uVar2,param_1);
    return;
  }
  return;
}


