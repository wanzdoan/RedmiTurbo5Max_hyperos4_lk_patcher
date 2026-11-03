// ffff000090717ac4  size=260  FUN_ffff000090717ac4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090717ac4(long param_1)

{
  long lVar1;
  uint uVar2;
  ulong uVar3;
  long *plVar4;
  undefined8 uVar5;
  undefined8 *puVar6;
  uint uVar7;
  long lVar8;
  long *plVar9;
  
  uVar5 = daif;
  uVar7 = (uint)uVar5;
  if ((uVar7 >> 7 & 1) == 0) {
    uVar3 = daif;
    daif = uVar3 | 0x80;
  }
  uVar5 = tpidr_el1;
  plVar4 = *(long **)(param_1 + 0x2a0);
  uVar2 = _DAT_ffff000090843b90;
  while (_DAT_ffff000090843b90 = uVar2, plVar4 != (long *)(param_1 + 0x298)) {
    plVar9 = (long *)plVar4[1];
    *plVar9 = *plVar4;
    *(long **)(*plVar4 + 8) = plVar9;
    *plVar4 = 0;
    plVar4[1] = 0;
    if (plVar4 == (long *)0x18) break;
    lVar1 = (long)(int)*(uint *)(plVar4 + 2) * 0x10;
    uVar2 = 1 << (ulong)(*(uint *)(plVar4 + 2) & 0x1f) | uVar2;
    *(int *)(param_1 + 0x2a8) = *(int *)(param_1 + 0x2a8) + -1;
    lVar8 = *(long *)(lVar1 + -0xffff6f7bc938);
    *plVar4 = lVar1 + -0xffff6f7bc940;
    *(undefined4 *)((long)plVar4 + 0x14) = 1;
    plVar4[1] = lVar8;
    puVar6 = *(undefined8 **)(lVar1 + -0xffff6f7bc938);
    *(long **)(lVar1 + -0xffff6f7bc938) = plVar4;
    plVar9 = *(long **)(param_1 + 0x2a0);
    *(undefined4 *)(plVar4 + 6) = 0xffffffde;
    plVar4[5] = 0;
    *puVar6 = plVar4;
    plVar4 = plVar9;
  }
  if (*(int *)(param_1 + 0x2c) == 5) {
    _DAT_ffff000090843678 = 0;
    *(uint *)(param_1 + 0x34) = *(uint *)(param_1 + 0x34) & 0xfffffffe;
    if ((uVar7 >> 7 & 1) == 0) {
      uVar3 = daif;
      daif = uVar3 & 0xffffffffffffff7f;
    }
    uVar5 = FUN_ffff000090717bc8(param_1,0,0);
    return uVar5;
  }
  _DAT_ffff000090843678 = 0;
  *(uint *)(param_1 + 0x34) = *(uint *)(param_1 + 0x34) | 1;
  if ((uVar7 >> 7 & 1) == 0) {
    uVar3 = daif;
    daif = uVar3 & 0xffffffffffffff7f;
  }
  return 0;
}


