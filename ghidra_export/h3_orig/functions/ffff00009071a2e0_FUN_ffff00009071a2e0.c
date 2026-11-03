// ffff00009071a2e0  size=972  FUN_ffff00009071a2e0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009071a2e0(ulong param_1,ulong param_2,ulong param_3)

{
  ulong uVar1;
  long lVar2;
  ulong uVar3;
  long lVar4;
  int iVar5;
  uint uVar6;
  uint uVar7;
  ulong *puVar8;
  uint uVar9;
  ulong *puVar10;
  ulong *puVar11;
  uint *puVar12;
  ulong uVar13;
  
  if ((DAT_ffff000090848c44 & 1) == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_lib_mblock_mblock_c_ffff0000907f7bed,0x2ef,s_mblock_init_done_ffff0000907f5d68);
  }
  lVar2 = param_2 - param_3;
  if (param_2 < param_3 || lVar2 == 0) {
    FUN_ffff00009077de54
              (s__s__d__error__mblock__llx_oldsiz_ffff0000907dc211,s_mblock_resize_ffff0000907df570,
               0x2f4,param_1,param_2,param_3);
    return 0xffffffff;
  }
  if ((((uint)param_3 | (uint)param_2) & 0xffff) != 0) {
    FUN_ffff00009077de54
              (s__s__d__alignment_error__oldsize__ffff0000907fccb5,s_mblock_resize_ffff0000907df570,
               0x2fc,param_2,param_3);
    return 0xffffffff;
  }
  FUN_ffff0000907175f4(&LAB_ffff0000908181f8,0xffffffff);
  iVar5 = _DAT_ffff00009085ac90;
  lVar4 = _DAT_ffff000090850c50;
  uVar6 = _DAT_ffff00009085ac90 - 1;
  puVar8 = (ulong *)(_DAT_ffff000090850c50 + 8);
  puVar10 = puVar8 + (ulong)uVar6 * 3;
  if (puVar8 <= puVar10) {
    do {
      if ((puVar8[1] != 0) &&
         (((uVar3 = *puVar8, param_1 <= uVar3 && (uVar3 < param_2 + param_1)) ||
          ((uVar3 < param_1 && (param_1 < puVar8[1] + uVar3)))))) {
        FUN_ffff00009077de54
                  (s__s__d__addr__llx__oldsize___llx_s_ffff0000907d8de8,
                   s_mblock_resize_ffff0000907df570,0x30d,param_1,param_2);
        FUN_ffff0000907195cc(0);
        FUN_ffff00009071768c(&LAB_ffff0000908181f8);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cc18
                  (s__s__invalid_resize_mblock_ffff0000907e8e1f,s_mblock_resize_ffff0000907df570);
      }
      puVar8 = puVar8 + 3;
    } while (puVar8 <= puVar10);
  }
  uVar7 = 0;
  if (_DAT_ffff00009085b8a0 != 0) {
    puVar8 = (ulong *)(_DAT_ffff000090850c50 + 0xc18);
    uVar7 = 0;
    do {
      if (((*puVar8 != 0) && (*puVar8 == param_1)) && (uVar9 = uVar7, puVar8[1] == param_2))
      goto LAB_ffff00009071a498;
      uVar9 = uVar7 + 1;
      puVar8 = puVar8 + 0x13;
      uVar7 = uVar9;
    } while (puVar8 <= (ulong *)(_DAT_ffff000090850c50 + 0x5780U));
    uVar7 = 0;
LAB_ffff00009071a498:
    if (uVar9 == 0x80) {
      FUN_ffff00009077de54
                (s__s__d__error__mblock_addr___llx__ffff0000907e8e3a,
                 s_mblock_resize_ffff0000907df570,0x329,param_1,param_2);
      FUN_ffff0000907195cc(0);
      FUN_ffff00009071768c(&LAB_ffff0000908181f8);
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cc18(s__s__failed_ffff000090801f62,s_mblock_resize_ffff0000907df570);
    }
  }
  uVar3 = param_3 + param_1;
  if (*_DAT_ffff000090850c58 != 0) {
    uVar13 = 0;
    puVar12 = _DAT_ffff000090850c58;
    do {
      if ((*(ulong *)(puVar12 + 2) <= uVar3) &&
         (param_2 + param_1 <= *(long *)(puVar12 + 4) + *(ulong *)(puVar12 + 2)))
      goto joined_r0xffff00009071a510;
      uVar13 = uVar13 + 1;
      puVar12 = puVar12 + 4;
    } while (*_DAT_ffff000090850c58 != uVar13);
  }
  uVar13 = 0;
joined_r0xffff00009071a510:
  if (param_3 == 0) {
    FUN_ffff00009077eb88(_DAT_ffff000090850c50 + (ulong)uVar7 * 0x98 + 0xc18,0,0x98);
    _DAT_ffff00009085b8a0 = _DAT_ffff00009085b8a0 + -1;
    uVar6 = _DAT_ffff00009085ac90 - 1;
    lVar4 = _DAT_ffff000090850c50;
    iVar5 = _DAT_ffff00009085ac90;
  }
  else {
    *(ulong *)(_DAT_ffff000090850c50 + (ulong)uVar7 * 0x98 + 0xc20) = param_3;
  }
  puVar8 = (ulong *)(lVar4 + 8);
  puVar10 = puVar8 + (ulong)uVar6 * 3;
  if (puVar10 < puVar8) {
    if (param_2 == param_3) goto code_r0xffff00009071a624;
LAB_ffff00009071a568:
    uVar6 = 0;
  }
  else {
    puVar11 = puVar8;
    do {
      uVar1 = puVar11[1];
      if (uVar3 == uVar1 + *puVar11) {
        puVar11[1] = uVar1 + lVar2;
        goto code_r0xffff00009071a624;
      }
      if (*puVar11 == param_2 + param_1) {
        *puVar11 = uVar3;
        puVar11[1] = uVar1 + lVar2;
        goto code_r0xffff00009071a624;
      }
      puVar11 = puVar11 + 3;
    } while (puVar11 <= puVar10);
    if (param_2 == param_3) goto code_r0xffff00009071a624;
    if (puVar10 < puVar8) goto LAB_ffff00009071a568;
    uVar6 = 0;
    do {
      if (uVar3 < *puVar8) break;
      uVar6 = uVar6 + 1;
      puVar8 = puVar8 + 3;
    } while (puVar8 <= puVar10);
  }
  FUN_ffff00009077e970
            (lVar4 + 8 + (ulong)(uVar6 + 1) * 0x18,lVar4 + 8 + (ulong)uVar6 * 0x18,
             (ulong)(iVar5 - uVar6) * 0x18);
  lVar4 = _DAT_ffff000090850c50 + (ulong)uVar6 * 0x18;
  *(ulong *)(lVar4 + 8) = uVar3;
  *(long *)(lVar4 + 0x10) = lVar2;
  *(int *)(lVar4 + 0x18) = (int)uVar13;
  *(undefined4 *)(lVar4 + 0x1c) = 0;
  _DAT_ffff00009085ac90 = _DAT_ffff00009085ac90 + 1;
code_r0xffff00009071a624:
  FUN_ffff00009071768c(&LAB_ffff0000908181f8);
  return 0;
}


