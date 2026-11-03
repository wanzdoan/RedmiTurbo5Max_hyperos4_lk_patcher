// ffff0000907a6da4  size=1004  FUN_ffff0000907a6da4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8
FUN_ffff0000907a6da4(long param_1,long param_2,long param_3,long param_4,long param_5,uint param_6)

{
  ulong uVar1;
  ulong uVar2;
  uint uVar3;
  uint uVar4;
  ulong uVar5;
  long *plVar6;
  int iVar7;
  ulong *puVar8;
  ulong uVar9;
  ulong uVar10;
  ulong uVar11;
  uint uVar12;
  int iVar13;
  ulong uVar14;
  ulong local_158 [31];
  
  local_158[0x1c] = 0;
  local_158[0x1d] = 0;
  local_158[0x1a] = 0;
  local_158[0x1b] = 0;
  local_158[0x18] = 0;
  local_158[0x19] = 0;
  local_158[0x16] = 0;
  local_158[0x17] = 0;
  local_158[0x14] = 0;
  local_158[0x15] = 0;
  local_158[0x12] = 0;
  local_158[0x13] = 0;
  local_158[0x10] = 0;
  local_158[0x11] = 0;
  local_158[0xe] = 0;
  local_158[0xf] = 0;
  local_158[0xc] = 0;
  local_158[0xd] = 0;
  local_158[10] = 0;
  local_158[0xb] = 0;
  local_158[8] = 0;
  local_158[9] = 0;
  local_158[6] = 0;
  local_158[7] = 0;
  local_158[4] = 0;
  local_158[5] = 0;
  local_158[2] = 0;
  local_158[3] = 0;
  local_158[0] = 0;
  local_158[1] = 0;
  if (2 < param_6) {
    return 0x101023;
  }
  uVar5 = -(ulong)(param_6 >> 0x1f) & 0xfffffff800000000 | (ulong)param_6 << 3;
  uVar3 = *(uint *)(&DAT_ffff000090812e34 + (long)(int)param_6 * 4);
  uVar11 = (ulong)uVar3;
  _DAT_ffff00009090afc0 = *(undefined8 *)((long)&PTR_DAT_ffff000090837e38 + uVar5);
  _DAT_ffff00009090afc8 = *(undefined8 *)((long)&PTR_DAT_ffff000090837e50 + uVar5);
  _DAT_ffff00009090afd0 = *(undefined8 *)((long)&PTR_DAT_ffff000090837e68 + uVar5);
  uVar12 = uVar3 * 8 - 8;
  plVar6 = *(long **)((long)&PTR_DAT_ffff000090837e80 + uVar5);
  uVar5 = 0;
  uVar14 = (ulong)uVar3;
  uVar4 = uVar12;
  do {
    uVar9 = (ulong)uVar4;
    uVar4 = uVar4 - 8;
    uVar9 = (*(ulong *)(param_3 + uVar9) & 0xff00ff00ff00ff00) >> 8 |
            (*(ulong *)(param_3 + uVar9) & 0xff00ff00ff00ff) << 8;
    uVar9 = (uVar9 & 0xffff0000ffff0000) >> 0x10 | (uVar9 & 0xffff0000ffff) << 0x10;
    local_158[uVar5 + 0x18] = uVar9 >> 0x20 | uVar9 << 0x20;
    uVar5 = uVar5 + 1;
  } while (uVar11 != uVar5);
  iVar13 = 1;
  _DAT_ffff00009090afd8 = plVar6;
LAB_ffff0000907a6ec4:
  uVar5 = 0;
  do {
    uVar9 = uVar14;
    if (local_158[uVar5 + 0x18] != 0) goto LAB_ffff0000907a6ee4;
    uVar5 = uVar5 + 1;
  } while (uVar11 != uVar5);
  goto LAB_ffff0000907a6fb4;
  while( true ) {
    if (local_158[uVar5 + 0x18] < (ulong)plVar6[uVar5]) goto LAB_ffff0000907a6f38;
    uVar9 = uVar5;
    if ((ulong)plVar6[uVar5] < local_158[uVar5 + 0x18]) break;
LAB_ffff0000907a6ee4:
    uVar5 = (ulong)((int)uVar9 - 1);
    if ((int)uVar9 < 1) break;
  }
  uVar5 = 0;
  puVar8 = local_158 + 0x18;
  uVar9 = uVar11;
  do {
    uVar1 = *plVar6 + uVar5;
    if (uVar1 != 0) {
      uVar5 = (ulong)(*puVar8 < uVar1);
    }
    uVar9 = uVar9 - 1;
    *puVar8 = *puVar8 - uVar1;
    plVar6 = plVar6 + 1;
    puVar8 = puVar8 + 1;
  } while (uVar9 != 0);
LAB_ffff0000907a6f38:
  FUN_ffff0000907a66c0(local_158,_DAT_ffff00009090afd0,local_158 + 0x18,uVar3);
  uVar5 = uVar14;
  do {
    iVar7 = (int)uVar5;
    uVar5 = (ulong)(iVar7 - 1);
    plVar6 = _DAT_ffff00009090afd8;
    if (iVar7 < 1) break;
    if (local_158[uVar5] < (ulong)_DAT_ffff00009090afd8[uVar5]) goto LAB_ffff0000907a6fb4;
  } while (local_158[uVar5] <= (ulong)_DAT_ffff00009090afd8[uVar5]);
  uVar5 = 0;
  uVar9 = 0;
  do {
    uVar1 = uVar9 + 1;
    uVar2 = _DAT_ffff00009090afd8[uVar9] + uVar5;
    if (uVar2 != 0) {
      uVar5 = (ulong)(local_158[uVar9] < uVar2);
    }
    local_158[uVar9] = local_158[uVar9] - uVar2;
    uVar9 = uVar1;
  } while (uVar11 != uVar1);
LAB_ffff0000907a6fb4:
  uVar5 = 0;
  do {
    if (local_158[uVar5] != 0) {
      uVar5 = 0;
      uVar4 = uVar12;
      do {
        puVar8 = local_158 + uVar5;
        uVar5 = uVar5 + 1;
        uVar9 = (*puVar8 & 0xff00ff00ff00ff00) >> 8 | (*puVar8 & 0xff00ff00ff00ff) << 8;
        uVar9 = (uVar9 & 0xffff0000ffff0000) >> 0x10 | (uVar9 & 0xffff0000ffff) << 0x10;
        *(ulong *)(param_4 + (ulong)uVar4) = uVar9 >> 0x20 | uVar9 << 0x20;
        uVar4 = uVar4 - 8;
      } while (uVar11 != uVar5);
      uVar5 = 0;
      uVar4 = uVar12;
      do {
        uVar9 = (ulong)uVar4;
        uVar4 = uVar4 - 8;
        uVar9 = (*(ulong *)(param_2 + uVar9) & 0xff00ff00ff00ff00) >> 8 |
                (*(ulong *)(param_2 + uVar9) & 0xff00ff00ff00ff) << 8;
        uVar9 = (uVar9 & 0xffff0000ffff0000) >> 0x10 | (uVar9 & 0xffff0000ffff) << 0x10;
        local_158[uVar5 + 0x12] = uVar9 >> 0x20 | uVar9 << 0x20;
        uVar5 = uVar5 + 1;
      } while (uVar11 != uVar5);
      FUN_ffff0000907a7190(local_158 + 0xc,local_158,local_158 + 0x12,_DAT_ffff00009090afd8,uVar3);
      uVar5 = 0;
      uVar4 = uVar12;
      do {
        uVar9 = (ulong)uVar4;
        uVar4 = uVar4 - 8;
        uVar9 = (*(ulong *)(param_1 + uVar9) & 0xff00ff00ff00ff00) >> 8 |
                (*(ulong *)(param_1 + uVar9) & 0xff00ff00ff00ff) << 8;
        uVar9 = (uVar9 & 0xffff0000ffff0000) >> 0x10 | (uVar9 & 0xffff0000ffff) << 0x10;
        local_158[uVar5 + 0x12] = uVar9 >> 0x20 | uVar9 << 0x20;
        uVar5 = uVar5 + 1;
      } while (uVar11 != uVar5);
      uVar5 = 0;
      uVar9 = 0;
      do {
        uVar1 = uVar9 + 1;
        uVar10 = local_158[uVar9 + 0x12];
        uVar2 = uVar10 + uVar5 + local_158[uVar9 + 0xc];
        local_158[uVar9 + 0xc] = uVar2;
        if (uVar2 != uVar10) {
          uVar5 = (ulong)(uVar2 < uVar10);
        }
        uVar9 = uVar1;
      } while (uVar11 != uVar1);
      if (uVar5 == 0) {
        while ((iVar13 = (int)uVar14, uVar14 = (ulong)(iVar13 - 1), 0 < iVar13 &&
               (local_158[uVar14 + 0xc] <= (ulong)_DAT_ffff00009090afd8[uVar14]))) {
          if (local_158[uVar14 + 0xc] < (ulong)_DAT_ffff00009090afd8[uVar14])
          goto LAB_ffff0000907a70fc;
        }
      }
      uVar5 = 0;
      puVar8 = local_158 + 0xc;
      plVar6 = _DAT_ffff00009090afd8;
      uVar14 = uVar11;
      do {
        uVar9 = *plVar6 + uVar5;
        if (uVar9 != 0) {
          uVar5 = (ulong)(*puVar8 < uVar9);
        }
        uVar14 = uVar14 - 1;
        *puVar8 = *puVar8 - uVar9;
        puVar8 = puVar8 + 1;
        plVar6 = plVar6 + 1;
      } while (uVar14 != 0);
LAB_ffff0000907a70fc:
      FUN_ffff0000907a7860(local_158 + 0x18,local_158 + 0x18,_DAT_ffff00009090afd8,uVar3);
      FUN_ffff0000907a7190
                (local_158 + 0xc,local_158 + 0xc,local_158 + 0x18,_DAT_ffff00009090afd8,uVar3);
      puVar8 = local_158 + 0xc;
      do {
        uVar11 = uVar11 - 1;
        uVar5 = (*puVar8 & 0xff00ff00ff00ff00) >> 8 | (*puVar8 & 0xff00ff00ff00ff) << 8;
        uVar5 = (uVar5 & 0xffff0000ffff0000) >> 0x10 | (uVar5 & 0xffff0000ffff) << 0x10;
        *(ulong *)(param_5 + (ulong)uVar12) = uVar5 >> 0x20 | uVar5 << 0x20;
        uVar12 = uVar12 - 8;
        puVar8 = puVar8 + 1;
      } while (uVar11 != 0);
      return 0;
    }
    uVar5 = uVar5 + 1;
  } while (uVar11 != uVar5);
  iVar13 = iVar13 + 1;
  if (iVar13 == 0x11) {
    return 0x102017;
  }
  goto LAB_ffff0000907a6ec4;
}


