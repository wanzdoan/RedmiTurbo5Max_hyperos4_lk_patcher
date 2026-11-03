
int FUN_ffff0000be062c40(undefined8 param_1)

{
  long lVar1;
  int iVar2;
  int iVar3;
  undefined1 auStack_240 [512];
  
  lVar1 = FUN_ffff0000be05de5c();
  if (lVar1 == 0) {
    iVar2 = 0;
  }
  else {
    FUN_ffff0000be060ae0(auStack_240,0,0x200);
    iVar2 = 0;
    iVar3 = 0;
    do {
      FUN_ffff0000be061134(auStack_240,s__sp_d_ffff0000be0a98ff,param_1,iVar3);
      lVar1 = FUN_ffff0000be05de5c(auStack_240);
      if (lVar1 != 0) {
        FUN_ffff0000be05e9a4();
        FUN_ffff0000be05def0(lVar1);
        FUN_ffff0000be05edb4(lVar1);
        iVar2 = iVar2 + 1;
      }
      iVar3 = iVar3 + 1;
    } while (iVar3 != 0xc0);
  }
  return iVar2;
}


