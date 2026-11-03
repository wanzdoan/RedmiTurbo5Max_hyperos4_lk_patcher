// ffff0000907184f0  size=136  FUN_ffff0000907184f0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907184f0(undefined8 param_1,undefined8 param_2,long param_3)

{
  long lVar1;
  long *plVar2;
  uint uVar3;
  int iVar4;
  long *plVar5;
  long lVar6;
  undefined8 *puVar7;
  undefined8 uVar8;
  
  plVar5 = (long *)(param_3 + 0x18);
  plVar2 = *(long **)(param_3 + 0x20);
  uVar3 = *(uint *)(param_3 + 0x28);
  lVar6 = *(long *)(param_3 + 0x40);
  *(undefined8 *)(param_3 + 0x40) = 0;
  *plVar2 = *plVar5;
  lVar1 = (long)(int)uVar3 * 0x10;
  iVar4 = *(int *)(lVar6 + 0x18);
  *(undefined4 *)(param_3 + 0x2c) = 1;
  *(long **)(*plVar5 + 8) = plVar2;
  *(undefined8 *)(param_3 + 0x20) = 0;
  uVar8 = *(undefined8 *)(lVar1 + -0xffff6f7bc900);
  *(undefined4 *)(param_3 + 0x48) = 0xfffffff3;
  *plVar5 = lVar1 + -0xffff6f7bc908;
  *(undefined8 *)(param_3 + 0x20) = uVar8;
  *(int *)(lVar6 + 0x18) = iVar4 + -1;
  puVar7 = *(undefined8 **)(lVar1 + -0xffff6f7bc900);
  *(long **)(lVar1 + -0xffff6f7bc900) = plVar5;
  *puVar7 = plVar5;
  _DAT_ffff000090843bc8 = _DAT_ffff000090843bc8 | 1 << (ulong)(uVar3 & 0x1f);
  _DAT_ffff0000908436b0 = 0;
  return;
}


