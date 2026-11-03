
undefined8 FUN_ffff0000b800b06c(long param_1)

{
  long lVar1;
  long *plVar2;
  uint uVar3;
  undefined8 uVar4;
  undefined8 uVar5;
  ulong uVar6;
  undefined8 *puVar7;
  long *plVar8;
  long lVar9;
  undefined8 *puVar10;
  undefined8 uVar11;
  
  uVar4 = daif;
  uVar5 = daif;
  if (((uint)uVar5 >> 7 & 1) == 0) {
    uVar6 = daif;
    daif = uVar6 | 0x80;
    if (*(int *)(param_1 + 0x2c) != 0) {
      uVar6 = daif;
      daif = uVar6 & 0xffffffffffffff7f;
      DAT_ffff0000b810ff28 = 0;
      return 0;
    }
  }
  else if (*(int *)(param_1 + 0x2c) != 0) {
    DAT_ffff0000b810ff28 = 0;
    return 0;
  }
  lVar1 = (long)(int)*(uint *)(param_1 + 0x28);
  DAT_ffff0000b810ff28 = 0;
  uVar11 = (&DAT_ffff0000b810ff78)[lVar1 * 2];
  DAT_ffff0000b8110440 = DAT_ffff0000b8110440 | 1 << (ulong)(*(uint *)(param_1 + 0x28) & 0x1f);
  puVar7 = (undefined8 *)(param_1 + 0x18);
  *puVar7 = &DAT_ffff0000b810ff70 + lVar1 * 2;
  *(undefined8 *)(param_1 + 0x20) = uVar11;
  puVar10 = (undefined8 *)(&DAT_ffff0000b810ff78)[lVar1 * 2];
  *(undefined4 *)(param_1 + 0x2c) = 1;
  (&DAT_ffff0000b810ff78)[lVar1 * 2] = puVar7;
  *puVar10 = puVar7;
  if (((uint)uVar5 >> 7 & 1) == 0) {
    uVar6 = daif;
    daif = uVar6 & 0xffffffffffffff7f;
  }
  if (((uint)uVar4 >> 7 & 1) == 0) {
    lVar1 = tpidr_el1;
    uVar4 = daif;
    if (((uint)uVar4 >> 7 & 1) == 0) {
      uVar6 = daif;
      daif = uVar6 | 0x80;
    }
    DAT_ffff0000b810ff28 = 1;
    *(undefined8 *)(lVar1 + 0x2c) = 1;
    if ((*(byte *)(lVar1 + 0x34) >> 4 & 1) == 0) {
      uVar3 = *(uint *)(lVar1 + 0x28);
      DAT_ffff0000b8110440 = DAT_ffff0000b8110440 | 1 << (ulong)(uVar3 & 0x1f);
      plVar2 = &DAT_ffff0000b810ff70 + (long)(int)uVar3 * 2;
      plVar8 = (long *)(lVar1 + 0x18);
      *plVar8 = *plVar2;
      lVar9 = *plVar2;
      *(long **)(lVar1 + 0x20) = plVar2;
      (&DAT_ffff0000b810ff70)[(long)(int)uVar3 * 2] = plVar8;
      *(long **)(lVar9 + 8) = plVar8;
    }
    FUN_ffff0000b800b74c();
    DAT_ffff0000b810ff28 = 0;
    if (((uint)uVar4 >> 7 & 1) == 0) {
      uVar6 = daif;
      daif = uVar6 & 0xffffffffffffff7f;
    }
  }
  return 0;
}


