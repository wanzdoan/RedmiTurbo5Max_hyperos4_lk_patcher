
void FUN_ffff000090786684(undefined4 *param_1)

{
  undefined4 uVar1;
  undefined4 uVar2;
  int iVar3;
  
  uVar1 = FUN_ffff00009075c61c();
  *param_1 = 0;
  uVar2 = thunk_FUN_ffff00009075d768();
  param_1[1] = uVar2;
  uVar2 = FUN_ffff00009075c8fc();
  param_1[8] = uVar1;
  param_1[2] = uVar2;
  uVar1 = thunk_FUN_ffff00009075da28();
  param_1[9] = uVar1;
  uVar1 = thunk_FUN_ffff00009075da28();
  param_1[3] = uVar1;
  param_1[5] = 1;
  uVar1 = thunk_FUN_ffff00009075d814();
  param_1[6] = uVar1;
  param_1[7] = 1;
  iVar3 = FUN_ffff00009077f348(&DAT_ffff0000907df338,&DAT_ffff0000907f2fb1,3);
  if (iVar3 == 0) {
    uVar1 = 0x10e;
  }
  else {
    iVar3 = FUN_ffff00009077f348(&DAT_ffff0000907df338,&DAT_ffff0000907d9514,2);
    if (iVar3 == 0) {
      uVar1 = 0x5a;
    }
    else {
      iVar3 = FUN_ffff00009077f348(&DAT_ffff0000907df338,&DAT_ffff0000907f6757,3);
      if (iVar3 != 0) {
        param_1[4] = 0;
        return;
      }
      uVar1 = 0xb4;
    }
  }
  param_1[4] = uVar1;
  return;
}


