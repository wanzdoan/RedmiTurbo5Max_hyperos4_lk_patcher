// ffff0000907184e0  size=16  FUN_ffff0000907184e0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

bool FUN_ffff0000907184e0(undefined8 param_1,undefined8 param_2,long param_3)

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
    lVar1 = (long)(int)uVar3 * 0x10;
    iVar4 = *(int *)(lVar7 + 0x18);
    *(undefined4 *)(param_3 + 0x2c) = 1;
    *(long **)(*plVar6 + 8) = plVar2;
    *(undefined8 *)(param_3 + 0x20) = 0;
    uVar9 = *(undefined8 *)(lVar1 + -0xffff6f7bc900);
    *(undefined4 *)(param_3 + 0x48) = 0xfffffff3;
    *plVar6 = lVar1 + -0xffff6f7bc908;
    *(undefined8 *)(param_3 + 0x20) = uVar9;
    *(int *)(lVar7 + 0x18) = iVar4 + -1;
    puVar8 = *(undefined8 **)(lVar1 + -0xffff6f7bc900);
    _DAT_ffff000090843bc8 = _DAT_ffff000090843bc8 | 1 << (ulong)(uVar3 & 0x1f);
    *(long **)(lVar1 + -0xffff6f7bc900) = plVar6;
    *puVar8 = plVar6;
  }
  _DAT_ffff0000908436b0 = 0;
  return bVar5;
}


