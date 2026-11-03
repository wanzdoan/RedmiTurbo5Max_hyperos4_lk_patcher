
uint FUN_ffff0000be027c4c(long param_1,long param_2,undefined8 param_3)

{
  int iVar1;
  uint uVar2;
  undefined4 uVar3;
  uint uVar4;
  long lVar5;
  ulong uVar6;
  
  *(undefined4 *)(param_2 + 0x10) = 0;
  *(undefined8 *)(param_2 + 0x18) = *(undefined8 *)(param_2 + 0x20);
  *(undefined8 *)(param_2 + 0x20) = 1;
  lVar5 = FUN_ffff0000be05de5c(s_bootdevice_ffff0000be0a45a8);
  if (lVar5 == 0) {
    lVar5 = -0x12;
  }
  else {
    iVar1 = *(int *)(lVar5 + 0x38);
    FUN_ffff0000be05def0();
    lVar5 = (ulong)(iVar1 - 1) + 1;
  }
  uVar3 = DAT_ffff0000be117d8c;
  *(ulong *)(param_2 + 0x48) = lVar5 - (ulong)DAT_ffff0000be117d88;
  *(undefined4 *)(param_2 + 0x58) = uVar3;
  uVar3 = FUN_ffff0000be0553a4(0,param_2,0x5c);
  *(undefined4 *)(param_2 + 0x10) = uVar3;
  lVar5 = FUN_ffff0000be05de5c(s_bootdevice_ffff0000be0a45a8);
  if (lVar5 == 0) {
    uVar6 = 0xffffffffffffffed;
  }
  else {
    uVar6 = (ulong)(*(int *)(lVar5 + 0x38) - 1);
    FUN_ffff0000be05def0();
  }
  uVar4 = FUN_ffff0000be05e088(param_1,param_2,*(long *)(param_1 + 0x28) * uVar6);
  if (((int)uVar4 < 0) || (*(ulong *)(param_1 + 0x28) != (ulong)uVar4)) {
    FUN_ffff0000be05fd98
              (s__s__d___GPT_Update_write_sheader_ffff0000be0c6265,
               s_write_secondary_gpt_ffff0000be0b9c9f,0x135,uVar4);
  }
  else {
    lVar5 = FUN_ffff0000be05de5c(s_bootdevice_ffff0000be0a45a8);
    if (lVar5 == 0) {
      lVar5 = -0x12;
    }
    else {
      iVar1 = *(int *)(lVar5 + 0x38);
      FUN_ffff0000be05def0();
      lVar5 = (ulong)(iVar1 - 1) + 1;
    }
    uVar6 = *(ulong *)(param_1 + 0x28);
    uVar4 = 0;
    if ((ulong)*(uint *)(param_2 + 0x54) != 0) {
      uVar4 = (uint)(uVar6 / *(uint *)(param_2 + 0x54));
    }
    uVar2 = 0;
    if (uVar4 != 0) {
      uVar2 = ((uVar4 + *(int *)(param_2 + 0x50)) - 1) / uVar4;
    }
    uVar4 = FUN_ffff0000be05e088
                      (param_1,param_3,(lVar5 - (ulong)DAT_ffff0000be117d88) * uVar6,uVar6 * uVar2);
    if (((int)uVar4 < 0) || ((ulong)uVar4 != uVar6 * uVar2)) {
      FUN_ffff0000be05fd98
                (s__s__d___GPT_Update_write_pentrie_ffff0000be0a5ae3,
                 s_write_secondary_gpt_ffff0000be0b9c9f,0x141,uVar4);
    }
    else {
      uVar4 = 0;
    }
  }
  return uVar4;
}


