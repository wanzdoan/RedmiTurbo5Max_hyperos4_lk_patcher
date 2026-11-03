
undefined4 FUN_ffff00009071857c(long param_1,uint param_2,undefined4 param_3)

{
  long lVar1;
  long lVar2;
  long *plVar3;
  int iVar4;
  uint uVar5;
  uint uVar6;
  undefined8 *puVar7;
  long *plVar8;
  long lVar9;
  undefined8 uVar10;
  undefined8 *puVar11;
  
  lVar2 = tpidr_el1;
  plVar8 = *(long **)(param_1 + 0x10);
  if (plVar8 != (long *)(param_1 + 8)) {
    plVar3 = (long *)plVar8[1];
    *plVar3 = *plVar8;
    *(long **)(*plVar8 + 8) = plVar3;
    *plVar8 = 0;
    plVar8[1] = 0;
    if (plVar8 != (long *)0x18) {
      iVar4 = *(int *)(param_1 + 0x18);
      *(undefined4 *)(plVar8 + 6) = param_3;
      plVar8[5] = 0;
      *(undefined4 *)((long)plVar8 + 0x14) = 1;
      *(int *)(param_1 + 0x18) = iVar4 + -1;
      if ((param_2 & 1) != 0) {
        uVar5 = *(uint *)(lVar2 + 0x28);
        uVar6 = *(uint *)(plVar8 + 2);
        lVar9 = (long)(int)uVar5;
        lVar1 = (long)(int)uVar6;
        uVar10 = (&DAT_ffff000090843700)[lVar9 * 2];
        puVar7 = (undefined8 *)(lVar2 + 0x18);
        *puVar7 = &DAT_ffff0000908436f8 + lVar9 * 2;
        *(undefined8 *)(lVar2 + 0x20) = uVar10;
        puVar11 = (undefined8 *)(&DAT_ffff000090843700)[lVar9 * 2];
        (&DAT_ffff000090843700)[lVar9 * 2] = puVar7;
        lVar9 = (&DAT_ffff000090843700)[lVar1 * 2];
        *(undefined4 *)(lVar2 + 0x2c) = 1;
        *puVar11 = puVar7;
        *plVar8 = (long)(&DAT_ffff0000908436f8 + lVar1 * 2);
        plVar8[1] = lVar9;
        puVar7 = (undefined8 *)(&DAT_ffff000090843700)[lVar1 * 2];
        DAT_ffff000090843bc8 =
             1 << (ulong)(uVar6 & 0x1f) | DAT_ffff000090843bc8 | 1 << (ulong)(uVar5 & 0x1f);
        (&DAT_ffff000090843700)[lVar1 * 2] = plVar8;
        *puVar7 = plVar8;
        FUN_ffff00009071812c();
        return 1;
      }
      lVar2 = (long)(int)*(uint *)(plVar8 + 2);
      lVar9 = (&DAT_ffff000090843700)[lVar2 * 2];
      DAT_ffff000090843bc8 = DAT_ffff000090843bc8 | 1 << (ulong)(*(uint *)(plVar8 + 2) & 0x1f);
      *plVar8 = (long)(&DAT_ffff0000908436f8 + lVar2 * 2);
      plVar8[1] = lVar9;
      puVar7 = (undefined8 *)(&DAT_ffff000090843700)[lVar2 * 2];
      (&DAT_ffff000090843700)[lVar2 * 2] = plVar8;
      *puVar7 = plVar8;
      return 1;
    }
  }
  return 0;
}


