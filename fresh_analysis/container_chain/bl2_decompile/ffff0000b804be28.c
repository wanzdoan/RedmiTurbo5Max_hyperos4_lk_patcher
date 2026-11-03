
void FUN_ffff0000b804be28(undefined8 *param_1)

{
  undefined8 uVar1;
  ulong uVar2;
  undefined *puVar3;
  undefined8 *puVar4;
  
  puVar3 = PTR_LOOP_ffff0000b8102108;
  uVar1 = daif;
  if (((uint)uVar1 >> 7 & 1) == 0) {
    uVar2 = daif;
    daif = uVar2 | 0x80;
    *param_1 = &PTR_LOOP_ffff0000b8102100;
    DAT_ffff0000b8129c50 = 0;
    param_1[1] = puVar3;
    puVar4 = param_1;
    *(undefined8 **)PTR_LOOP_ffff0000b8102108 = param_1;
    PTR_LOOP_ffff0000b8102108 = (undefined *)puVar4;
    uVar2 = daif;
    daif = uVar2 & 0xffffffffffffff7f;
    return;
  }
  *param_1 = &PTR_LOOP_ffff0000b8102100;
  DAT_ffff0000b8129c50 = 0;
  param_1[1] = puVar3;
  puVar4 = param_1;
  *(undefined8 **)PTR_LOOP_ffff0000b8102108 = param_1;
  PTR_LOOP_ffff0000b8102108 = (undefined *)puVar4;
  return;
}


