// ffff000090717ee0  size=292  FUN_ffff000090717ee0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff000090717ee0(long param_1,uint param_2,undefined4 param_3)

{
  long lVar1;
  uint uVar2;
  long *plVar3;
  undefined8 uVar4;
  undefined8 *puVar5;
  long lVar6;
  long *plVar7;
  int iVar8;
  
  lVar1 = tpidr_el1;
  if (((param_2 & 1) != 0) && (0 < *(int *)(param_1 + 0x18))) {
    lVar6 = (long)(int)*(uint *)(lVar1 + 0x28) * 0x10;
    uVar4 = *(undefined8 *)(lVar6 + -0xffff6f7bc900);
    _DAT_ffff000090843bc8 = _DAT_ffff000090843bc8 | 1 << (ulong)(*(uint *)(lVar1 + 0x28) & 0x1f);
    plVar3 = (long *)(lVar1 + 0x18);
    *plVar3 = lVar6 + -0xffff6f7bc908;
    *(undefined8 *)(lVar1 + 0x20) = uVar4;
    puVar5 = *(undefined8 **)(lVar6 + -0xffff6f7bc900);
    *(undefined4 *)(lVar1 + 0x2c) = 1;
    *(long **)(lVar6 + -0xffff6f7bc900) = plVar3;
    *puVar5 = plVar3;
  }
  if (*(long **)(param_1 + 0x10) == (long *)(param_1 + 8)) {
    iVar8 = 0;
  }
  else {
    iVar8 = 0;
    plVar3 = *(long **)(param_1 + 0x10);
    do {
      uVar2 = _DAT_ffff000090843bc8;
      plVar7 = (long *)plVar3[1];
      *plVar7 = *plVar3;
      *(long **)(*plVar3 + 8) = plVar7;
      *plVar3 = 0;
      plVar3[1] = 0;
      if (plVar3 == (long *)0x18) break;
      iVar8 = iVar8 + 1;
      lVar1 = (long)(int)*(uint *)(plVar3 + 2) * 0x10;
      _DAT_ffff000090843bc8 = uVar2 | 1 << (ulong)(*(uint *)(plVar3 + 2) & 0x1f);
      *(int *)(param_1 + 0x18) = *(int *)(param_1 + 0x18) + -1;
      lVar6 = *(long *)(lVar1 + -0xffff6f7bc900);
      *plVar3 = lVar1 + -0xffff6f7bc908;
      *(undefined4 *)((long)plVar3 + 0x14) = 1;
      plVar3[1] = lVar6;
      puVar5 = *(undefined8 **)(lVar1 + -0xffff6f7bc900);
      *(long **)(lVar1 + -0xffff6f7bc900) = plVar3;
      plVar7 = *(long **)(param_1 + 0x10);
      *(undefined4 *)(plVar3 + 6) = param_3;
      plVar3[5] = 0;
      *puVar5 = plVar3;
      plVar3 = plVar7;
    } while (plVar7 != (long *)(param_1 + 8));
  }
  if ((iVar8 != 0) && ((param_2 & 1) != 0)) {
    FUN_ffff00009071812c();
  }
  return iVar8;
}


