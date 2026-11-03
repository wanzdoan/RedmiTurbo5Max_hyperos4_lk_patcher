// ffff000090717a14  size=288  FUN_ffff000090717a14

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090717a14(long param_1)

{
  long lVar1;
  undefined8 uVar2;
  undefined8 uVar3;
  ulong uVar4;
  long *plVar5;
  long *plVar6;
  long lVar7;
  long lVar8;
  undefined8 *puVar9;
  undefined8 uVar10;
  
  uVar2 = daif;
  uVar3 = daif;
  if (((uint)uVar3 >> 7 & 1) == 0) {
    uVar4 = daif;
    daif = uVar4 | 0x80;
    if (*(int *)(param_1 + 0x2c) != 0) {
      uVar4 = daif;
      daif = uVar4 & 0xffffffffffffff7f;
      _DAT_ffff0000908436b0 = 0;
      return 0;
    }
  }
  else if (*(int *)(param_1 + 0x2c) != 0) {
    _DAT_ffff0000908436b0 = 0;
    return 0;
  }
  lVar1 = (long)(int)*(uint *)(param_1 + 0x28) * 0x10;
  _DAT_ffff0000908436b0 = 0;
  uVar10 = *(undefined8 *)(lVar1 + -0xffff6f7bc900);
  _DAT_ffff000090843bc8 = _DAT_ffff000090843bc8 | 1 << (ulong)(*(uint *)(param_1 + 0x28) & 0x1f);
  plVar5 = (long *)(param_1 + 0x18);
  *plVar5 = lVar1 + -0xffff6f7bc908;
  *(undefined8 *)(param_1 + 0x20) = uVar10;
  puVar9 = *(undefined8 **)(lVar1 + -0xffff6f7bc900);
  *(undefined4 *)(param_1 + 0x2c) = 1;
  *(long **)(lVar1 + -0xffff6f7bc900) = plVar5;
  *puVar9 = plVar5;
  if (((uint)uVar3 >> 7 & 1) == 0) {
    uVar4 = daif;
    daif = uVar4 & 0xffffffffffffff7f;
  }
  if (((uint)uVar2 >> 7 & 1) == 0) {
    lVar1 = tpidr_el1;
    uVar2 = daif;
    if (((uint)uVar2 >> 7 & 1) == 0) {
      uVar4 = daif;
      daif = uVar4 | 0x80;
    }
    _DAT_ffff0000908436b0 = 1;
    *(undefined8 *)(lVar1 + 0x2c) = 1;
    if ((*(byte *)(lVar1 + 0x34) >> 4 & 1) == 0) {
      lVar8 = (long)(int)*(uint *)(lVar1 + 0x28) * 0x10;
      _DAT_ffff000090843bc8 = _DAT_ffff000090843bc8 | 1 << (ulong)(*(uint *)(lVar1 + 0x28) & 0x1f);
      plVar5 = (long *)(lVar8 + -0xffff6f7bc908);
      plVar6 = (long *)(lVar1 + 0x18);
      *plVar6 = *plVar5;
      lVar7 = *plVar5;
      *(long **)(lVar1 + 0x20) = plVar5;
      *(long **)(lVar8 + -0xffff6f7bc908) = plVar6;
      *(long **)(lVar7 + 8) = plVar6;
    }
    FUN_ffff00009071812c();
    _DAT_ffff0000908436b0 = 0;
    if (((uint)uVar2 >> 7 & 1) == 0) {
      uVar4 = daif;
      daif = uVar4 & 0xffffffffffffff7f;
    }
  }
  return 0;
}


