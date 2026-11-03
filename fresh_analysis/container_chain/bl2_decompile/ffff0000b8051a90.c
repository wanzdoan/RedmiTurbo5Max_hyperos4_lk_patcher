
void FUN_ffff0000b8051a90(undefined8 *param_1)

{
  undefined4 uVar1;
  int iVar2;
  
  uVar1 = FUN_ffff0000b805136c();
  *(undefined4 *)(param_1 + 4) = uVar1;
  *(undefined4 *)((long)param_1 + 0x24) = 0;
  *param_1 = 1;
  param_1[1] = 0;
  *(undefined8 *)((long)param_1 + 0x14) = 1;
  *(undefined4 *)((long)param_1 + 0x1c) = 1;
  iVar2 = FUN_ffff0000b804d4c4(&DAT_ffff0000b808dbfe,&DAT_ffff0000b8098f2a,3);
  if (iVar2 == 0) {
    uVar1 = 0x10e;
  }
  else {
    iVar2 = FUN_ffff0000b804d4c4(&DAT_ffff0000b808dbfe,&DAT_ffff0000b808ab6e,2);
    if (iVar2 == 0) {
      uVar1 = 0x5a;
    }
    else {
      iVar2 = FUN_ffff0000b804d4c4(&DAT_ffff0000b808dbfe,&DAT_ffff0000b809aad9,3);
      if (iVar2 != 0) {
        *(undefined4 *)(param_1 + 2) = 0;
        return;
      }
      uVar1 = 0xb4;
    }
  }
  *(undefined4 *)(param_1 + 2) = uVar1;
  return;
}


