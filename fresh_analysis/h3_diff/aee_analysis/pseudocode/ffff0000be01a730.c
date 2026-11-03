
undefined8 FUN_ffff0000be01a730(long param_1)

{
  int iVar1;
  undefined8 uVar2;
  ulong uVar3;
  undefined8 uVar4;
  undefined8 *puVar5;
  undefined8 uVar6;
  
  puVar5 = DAT_ffff0000be0f5518;
  uVar2 = daif;
  if (((uint)uVar2 >> 7 & 1) == 0) {
    uVar3 = daif;
    daif = uVar3 | 0x80;
  }
  iVar1 = *(int *)(param_1 + 0x10);
  *DAT_ffff0000be0f5518 = 1;
  *(int *)(param_1 + 0x10) = iVar1 + 1;
  if (iVar1 < 1) {
    uVar6 = 0;
  }
  else {
    uVar6 = FUN_ffff0000be01ade0(param_1 + 0x18);
    if ((int)uVar6 < 0) {
      if ((int)uVar6 == -0xd) {
        *(int *)(param_1 + 0x10) = *(int *)(param_1 + 0x10) + -1;
      }
      goto LAB_ffff0000be01a784;
    }
  }
  uVar4 = tpidr_el1;
  *(undefined8 *)(param_1 + 8) = uVar4;
LAB_ffff0000be01a784:
  *puVar5 = 0;
  if (((uint)uVar2 >> 7 & 1) == 0) {
    uVar3 = daif;
    daif = uVar3 & 0xffffffffffffff7f;
  }
  return uVar6;
}


