
bool FUN_ffff0000b800bb00(undefined8 param_1,undefined8 param_2,long param_3)

{
  long lVar1;
  long *plVar2;
  uint uVar3;
  int iVar4;
  bool bVar5;
  long *plVar6;
  long lVar7;
  undefined8 *puVar8;
  undefined8 uVar9;
  
  bVar5 = *(int *)(param_3 + 0x2c) == 3;
  if (bVar5) {
    plVar6 = (long *)(param_3 + 0x18);
    plVar2 = *(long **)(param_3 + 0x20);
    uVar3 = *(uint *)(param_3 + 0x28);
    lVar7 = *(long *)(param_3 + 0x40);
    *(undefined8 *)(param_3 + 0x40) = 0;
    *plVar2 = *plVar6;
    lVar1 = (long)(int)uVar3;
    iVar4 = *(int *)(lVar7 + 0x18);
    *(undefined4 *)(param_3 + 0x2c) = 1;
    *(long **)(*plVar6 + 8) = plVar2;
    *(undefined8 *)(param_3 + 0x20) = 0;
    uVar9 = (&DAT_ffff0000b810ff78)[lVar1 * 2];
    *(undefined4 *)(param_3 + 0x48) = 0xfffffff3;
    *plVar6 = (long)(&DAT_ffff0000b810ff70 + lVar1 * 2);
    *(undefined8 *)(param_3 + 0x20) = uVar9;
    *(int *)(lVar7 + 0x18) = iVar4 + -1;
    puVar8 = (undefined8 *)(&DAT_ffff0000b810ff78)[lVar1 * 2];
    DAT_ffff0000b8110440 = DAT_ffff0000b8110440 | 1 << (ulong)(uVar3 & 0x1f);
    (&DAT_ffff0000b810ff78)[lVar1 * 2] = plVar6;
    *puVar8 = plVar6;
  }
  DAT_ffff0000b810ff28 = 0;
  return bVar5;
}


