// ffff00009071857c  size=284  FUN_ffff00009071857c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 FUN_ffff00009071857c(long param_1,uint param_2,undefined4 param_3)

{
  long lVar1;
  long lVar2;
  int iVar3;
  uint uVar4;
  uint uVar5;
  long *plVar6;
  long *plVar7;
  long lVar8;
  undefined8 uVar9;
  undefined8 *puVar10;
  
  lVar2 = tpidr_el1;
  plVar7 = *(long **)(param_1 + 0x10);
  if (plVar7 != (long *)(param_1 + 8)) {
    plVar6 = (long *)plVar7[1];
    *plVar6 = *plVar7;
    *(long **)(*plVar7 + 8) = plVar6;
    *plVar7 = 0;
    plVar7[1] = 0;
    if (plVar7 != (long *)0x18) {
      iVar3 = *(int *)(param_1 + 0x18);
      *(undefined4 *)(plVar7 + 6) = param_3;
      plVar7[5] = 0;
      *(undefined4 *)((long)plVar7 + 0x14) = 1;
      *(int *)(param_1 + 0x18) = iVar3 + -1;
      if ((param_2 & 1) != 0) {
        uVar4 = *(uint *)(lVar2 + 0x28);
        uVar5 = *(uint *)(plVar7 + 2);
        lVar8 = (long)(int)uVar4 * 0x10;
        lVar1 = (long)(int)uVar5 * 0x10;
        uVar9 = *(undefined8 *)(lVar8 + -0xffff6f7bc900);
        plVar6 = (long *)(lVar2 + 0x18);
        *plVar6 = lVar8 + -0xffff6f7bc908;
        *(undefined8 *)(lVar2 + 0x20) = uVar9;
        puVar10 = *(undefined8 **)(lVar8 + -0xffff6f7bc900);
        *(long **)(lVar8 + -0xffff6f7bc900) = plVar6;
        lVar8 = *(long *)(lVar1 + -0xffff6f7bc900);
        *(undefined4 *)(lVar2 + 0x2c) = 1;
        *puVar10 = plVar6;
        *plVar7 = lVar1 + -0xffff6f7bc908;
        plVar7[1] = lVar8;
        puVar10 = *(undefined8 **)(lVar1 + -0xffff6f7bc900);
        _DAT_ffff000090843bc8 =
             1 << (ulong)(uVar5 & 0x1f) | _DAT_ffff000090843bc8 | 1 << (ulong)(uVar4 & 0x1f);
        *(long **)(lVar1 + -0xffff6f7bc900) = plVar7;
        *puVar10 = plVar7;
        FUN_ffff00009071812c();
        return 1;
      }
      lVar2 = (long)(int)*(uint *)(plVar7 + 2) * 0x10;
      lVar8 = *(long *)(lVar2 + -0xffff6f7bc900);
      _DAT_ffff000090843bc8 = _DAT_ffff000090843bc8 | 1 << (ulong)(*(uint *)(plVar7 + 2) & 0x1f);
      *plVar7 = lVar2 + -0xffff6f7bc908;
      plVar7[1] = lVar8;
      puVar10 = *(undefined8 **)(lVar2 + -0xffff6f7bc900);
      *(long **)(lVar2 + -0xffff6f7bc900) = plVar7;
      *puVar10 = plVar7;
      return 1;
    }
  }
  return 0;
}


