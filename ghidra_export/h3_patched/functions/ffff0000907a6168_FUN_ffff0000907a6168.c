// ffff0000907a6168  size=560  FUN_ffff0000907a6168

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 FUN_ffff0000907a6168(long param_1,long param_2,long param_3,ulong param_4)

{
  uint uVar1;
  uint uVar2;
  long *plVar3;
  ulong *puVar4;
  ulong *puVar5;
  int iVar6;
  ulong uVar7;
  ulong uVar8;
  ulong uVar9;
  ulong *puVar10;
  ulong uVar11;
  undefined4 uVar12;
  uint uVar13;
  int iVar14;
  ulong local_f0 [18];
  
  uVar12 = 0x101023;
  local_f0[0x10] = 0;
  local_f0[0x11] = 0;
  local_f0[0xe] = 0;
  local_f0[0xf] = 0;
  local_f0[0xc] = 0;
  local_f0[0xd] = 0;
  local_f0[10] = 0;
  local_f0[0xb] = 0;
  local_f0[8] = 0;
  local_f0[9] = 0;
  local_f0[6] = 0;
  local_f0[7] = 0;
  local_f0[4] = 0;
  local_f0[5] = 0;
  local_f0[2] = 0;
  local_f0[3] = 0;
  local_f0[0] = 0;
  local_f0[1] = 0;
  if ((uint)param_4 < 3) {
    uVar7 = -(param_4 >> 0x1f & 1) & 0xfffffff800000000 | (param_4 & 0xffffffff) << 3;
    uVar1 = *(uint *)(&DAT_ffff000090812c0c + (long)(int)(uint)param_4 * 4);
    uVar11 = (ulong)uVar1;
    _DAT_ffff00009090af00 = *(undefined8 *)((long)&PTR_DAT_ffff000090837e18 + uVar7);
    _DAT_ffff00009090af08 = *(undefined8 *)((long)&PTR_DAT_ffff000090837e30 + uVar7);
    _DAT_ffff00009090af10 = *(undefined8 *)((long)&PTR_DAT_ffff000090837e48 + uVar7);
    uVar13 = uVar1 * 8 - 8;
    _DAT_ffff00009090af18 = *(long **)((long)&PTR_DAT_ffff000090837e60 + uVar7);
    uVar7 = 0;
    uVar2 = uVar13;
    do {
      uVar9 = (ulong)uVar2;
      uVar2 = uVar2 - 8;
      uVar9 = (*(ulong *)(param_1 + uVar9) & 0xff00ff00ff00ff00) >> 8 |
              (*(ulong *)(param_1 + uVar9) & 0xff00ff00ff00ff) << 8;
      uVar9 = (uVar9 & 0xffff0000ffff0000) >> 0x10 | (uVar9 & 0xffff0000ffff) << 0x10;
      local_f0[uVar7 + 0xc] = uVar9 >> 0x20 | uVar9 << 0x20;
      uVar7 = uVar7 + 1;
    } while (uVar11 != uVar7);
    iVar14 = 1;
    do {
      uVar7 = (ulong)uVar1;
      do {
        iVar6 = (int)uVar7;
        uVar7 = (ulong)(iVar6 - 1);
        if (iVar6 < 1) break;
        if (local_f0[uVar7 + 0xc] < (ulong)_DAT_ffff00009090af18[uVar7]) goto LAB_ffff0000907a62c4;
      } while (local_f0[uVar7 + 0xc] <= (ulong)_DAT_ffff00009090af18[uVar7]);
      uVar7 = 0;
      plVar3 = _DAT_ffff00009090af18;
      puVar10 = local_f0 + 0xc;
      uVar9 = uVar11;
      do {
        uVar8 = *plVar3 + uVar7;
        if (uVar8 != 0) {
          uVar7 = (ulong)(*puVar10 < uVar8);
        }
        uVar9 = uVar9 - 1;
        *puVar10 = *puVar10 - uVar8;
        plVar3 = plVar3 + 1;
        puVar10 = puVar10 + 1;
      } while (uVar9 != 0);
LAB_ffff0000907a62c4:
      FUN_ffff0000907a6400(local_f0,_DAT_ffff00009090af10,local_f0 + 0xc,uVar11);
      uVar7 = uVar11;
      puVar10 = local_f0;
      do {
        puVar5 = local_f0;
        uVar9 = uVar11;
        uVar2 = uVar13;
        if (*puVar10 != 0) goto LAB_ffff0000907a633c;
        uVar7 = uVar7 - 1;
        puVar10 = puVar10 + 1;
        puVar4 = local_f0 + 6;
        uVar8 = uVar11;
      } while (uVar7 != 0);
      do {
        puVar5 = local_f0;
        if (*puVar4 != 0) {
LAB_ffff0000907a633c:
          do {
            uVar9 = uVar9 - 1;
            uVar7 = (*puVar5 & 0xff00ff00ff00ff00) >> 8 | (*puVar5 & 0xff00ff00ff00ff) << 8;
            uVar7 = (uVar7 & 0xffff0000ffff0000) >> 0x10 | (uVar7 & 0xffff0000ffff) << 0x10;
            *(ulong *)(param_2 + (ulong)uVar2) = uVar7 >> 0x20 | uVar7 << 0x20;
            puVar5 = puVar5 + 1;
            puVar10 = local_f0 + 6;
            uVar2 = uVar2 - 8;
          } while (uVar9 != 0);
          do {
            uVar11 = uVar11 - 1;
            uVar7 = (*puVar10 & 0xff00ff00ff00ff00) >> 8 | (*puVar10 & 0xff00ff00ff00ff) << 8;
            uVar7 = (uVar7 & 0xffff0000ffff0000) >> 0x10 | (uVar7 & 0xffff0000ffff) << 0x10;
            *(ulong *)(param_3 + (ulong)uVar13) = uVar7 >> 0x20 | uVar7 << 0x20;
            uVar13 = uVar13 - 8;
            puVar10 = puVar10 + 1;
          } while (uVar11 != 0);
          return 0;
        }
        uVar8 = uVar8 - 1;
        puVar4 = puVar4 + 1;
      } while (uVar8 != 0);
      iVar14 = iVar14 + 1;
    } while (iVar14 != 0x11);
    uVar12 = 0x102017;
  }
  return uVar12;
}


