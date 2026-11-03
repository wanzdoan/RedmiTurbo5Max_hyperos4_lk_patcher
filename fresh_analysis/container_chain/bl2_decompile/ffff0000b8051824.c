
void FUN_ffff0000b8051824(undefined4 *param_1)

{
  undefined4 uVar1;
  undefined4 uVar2;
  int iVar3;
  
  uVar1 = FUN_ffff0000b803e8ac();
  *param_1 = 0;
  uVar2 = thunk_FUN_ffff0000b803f20c();
  param_1[1] = uVar2;
  uVar2 = FUN_ffff0000b803e984();
  param_1[8] = uVar1;
  param_1[2] = uVar2;
  uVar1 = thunk_FUN_ffff0000b803f4cc();
  param_1[9] = uVar1;
  uVar1 = thunk_FUN_ffff0000b803f4cc();
  param_1[3] = uVar1;
  param_1[5] = 1;
  uVar1 = thunk_FUN_ffff0000b803f2b8();
  param_1[6] = uVar1;
  param_1[7] = 1;
  iVar3 = FUN_ffff0000b804d4c4(&DAT_ffff0000b808dbfe,&DAT_ffff0000b8098f2a,3);
  if (iVar3 == 0) {
    uVar1 = 0x10e;
  }
  else {
    iVar3 = FUN_ffff0000b804d4c4(&DAT_ffff0000b808dbfe,&DAT_ffff0000b808ab6e,2);
    if (iVar3 == 0) {
      uVar1 = 0x5a;
    }
    else {
      iVar3 = FUN_ffff0000b804d4c4(&DAT_ffff0000b808dbfe,&DAT_ffff0000b809aad9,3);
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


