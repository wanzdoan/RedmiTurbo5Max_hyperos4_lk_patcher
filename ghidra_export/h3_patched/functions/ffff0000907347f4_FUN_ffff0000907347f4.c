// ffff0000907347f4  size=900  FUN_ffff0000907347f4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

uint FUN_ffff0000907347f4(long param_1,long param_2,long param_3)

{
  long *plVar1;
  long *plVar2;
  uint uVar3;
  uint uVar4;
  undefined4 uVar5;
  uint uVar6;
  long lVar7;
  long lVar8;
  undefined8 uVar9;
  ulong uVar10;
  int iVar11;
  ulong uVar12;
  long lVar13;
  ulong uVar14;
  long *plVar15;
  
  uVar12 = *(ulong *)(param_2 + 0x20);
  lVar7 = FUN_ffff00009077bc50(s_bootdevice_ffff0000907d0ccc);
  if (lVar7 == 0) {
    if (uVar12 == 0xffffffffffffffed) goto code_r0xffff000090734850;
LAB_ffff000090734878:
    _DAT_ffff00009086da2c = (uint)*(undefined8 *)(param_2 + 0x20);
  }
  else {
    iVar11 = *(int *)(lVar7 + 0x38);
    FUN_ffff00009077bce4();
    if (uVar12 != iVar11 - 1) goto LAB_ffff000090734878;
code_r0xffff000090734850:
    lVar7 = FUN_ffff00009077bc50(s_bootdevice_ffff0000907d0ccc);
    if (lVar7 == 0) {
      iVar11 = -0x13;
    }
    else {
      iVar11 = *(int *)(lVar7 + 0x38) + -1;
      FUN_ffff00009077bce4();
    }
    _DAT_ffff00009086da2c = iVar11 - (int)*(undefined8 *)(param_2 + 0x30);
  }
  if (0 < (int)*(uint *)(param_2 + 0x50)) {
    uVar12 = 0xffffffffffffffff;
    lVar7 = -0x200000000;
    plVar15 = (long *)(param_3 + -0x58);
    do {
      if ((char)plVar15[0x12] == '\0') {
        if (uVar12 != 0xffffffffffffffff) {
          lVar8 = param_3 + (uVar12 & 0xffffffff) * 0x80;
          plVar1 = (long *)(lVar8 + 0x20);
          plVar2 = (long *)(lVar8 + 0x28);
          uVar10 = uVar12;
          goto LAB_ffff00009073491c;
        }
        break;
      }
      uVar12 = uVar12 + 1;
      plVar15 = plVar15 + 0x10;
      lVar7 = lVar7 + 0x100000000;
    } while (uVar12 - *(uint *)(param_2 + 0x50) != -1);
  }
LAB_ffff000090734a08:
  _DAT_ffff00009086da30 = FUN_ffff00009076c7c4(0,param_3,*(int *)(param_2 + 0x50) << 7);
  *(undefined4 *)(param_2 + 0x14) = 0;
  lVar7 = FUN_ffff00009077bc50(s_bootdevice_ffff0000907d0ccc);
  if (lVar7 == 0) {
    uVar12 = 0xffffffffffffffed;
  }
  else {
    uVar12 = (ulong)(*(int *)(lVar7 + 0x38) - 1);
    FUN_ffff00009077bce4();
  }
  *(ulong *)(param_2 + 0x20) = uVar12;
  lVar7 = FUN_ffff00009077bc50(s_bootdevice_ffff0000907d0ccc);
  if (lVar7 == 0) {
    uVar12 = 0xffffffffffffffed;
  }
  else {
    uVar12 = (ulong)(*(int *)(lVar7 + 0x38) - 1);
    FUN_ffff00009077bce4();
  }
  uVar5 = _DAT_ffff00009086da30;
  uVar10 = (ulong)_DAT_ffff00009086da2c;
  *(undefined4 *)(param_2 + 0x10) = 0;
  *(undefined4 *)(param_2 + 0x58) = uVar5;
  *(ulong *)(param_2 + 0x30) = uVar12 - uVar10;
  uVar5 = FUN_ffff00009076c7c4(0,param_2,0x5c);
  uVar9 = *(undefined8 *)(param_1 + 0x28);
  *(undefined4 *)(param_2 + 0x10) = uVar5;
  uVar6 = FUN_ffff00009077be7c(param_1,param_2,uVar9,uVar9);
  if (((int)uVar6 < 0) || (uVar12 = (ulong)uVar6, uVar12 != *(ulong *)(param_1 + 0x28))) {
    FUN_ffff00009077de54
              (s__s__d___GPT_Update_write_pheader_ffff0000907ee0ea,
               s_write_primary_gpt_ffff0000907ef9bb,0x117,uVar6);
  }
  else {
    uVar3 = 0;
    if (*(uint *)(param_2 + 0x54) != 0) {
      uVar3 = uVar6 / *(uint *)(param_2 + 0x54);
    }
    uVar4 = 0;
    if (uVar3 != 0) {
      uVar4 = ((uVar3 + *(int *)(param_2 + 0x50)) - 1) / uVar3;
    }
    uVar6 = FUN_ffff00009077be7c(param_1,param_3,uVar12 << 1,uVar12 * uVar4);
    if (((int)uVar6 < 0) || ((ulong)uVar6 != uVar12 * uVar4)) {
      FUN_ffff00009077de54
                (s__s__d___GPT_Update_write_pentrie_ffff0000907d2c7c,
                 s_write_primary_gpt_ffff0000907ef9bb,0x121,uVar6);
    }
    else {
      uVar6 = 0;
    }
  }
  return uVar6;
LAB_ffff00009073491c:
  do {
    if ((uVar12 & 0xffffffff) == uVar10) {
      if (*plVar1 == 0) {
        lVar13 = *plVar2;
        lVar8 = FUN_ffff00009077bc50(s_bootdevice_ffff0000907d0ccc);
        if (lVar13 == 0) {
          if (lVar8 == 0) {
            uVar12 = 0xffffffffffffffed;
          }
          else {
            uVar12 = (ulong)(*(int *)(lVar8 + 0x38) - 1);
            FUN_ffff00009077bce4();
          }
          *plVar2 = uVar12 - _DAT_ffff00009086da2c;
          *plVar1 = *(long *)(param_3 + (lVar7 >> 0x19) + 0x28) + 1;
          break;
        }
        if (lVar8 == 0) {
          uVar14 = 0xffffffffffffffed;
        }
        else {
          uVar14 = (ulong)(*(int *)(lVar8 + 0x38) - 1);
          FUN_ffff00009077bce4();
        }
        *plVar1 = ((uVar14 - *plVar2) - (ulong)_DAT_ffff00009086da2c) + 1;
        lVar8 = FUN_ffff00009077bc50(s_bootdevice_ffff0000907d0ccc);
        if (lVar8 == 0) {
          uVar14 = 0xffffffffffffffed;
        }
        else {
          uVar14 = (ulong)(*(int *)(lVar8 + 0x38) - 1);
          FUN_ffff00009077bce4();
        }
        *plVar2 = uVar14 - _DAT_ffff00009086da2c;
      }
    }
    else if (plVar15[-1] == 0) {
      if (*plVar15 == 0) {
        lVar13 = plVar15[0xf];
        lVar8 = plVar15[-0x10] + 1;
      }
      else {
        lVar13 = plVar15[0xf];
        lVar8 = lVar13 - *plVar15;
      }
      plVar15[-1] = lVar8;
      *plVar15 = lVar13 + -1;
    }
    uVar14 = uVar10 + 1;
    plVar15 = plVar15 + -0x10;
    uVar10 = uVar10 - 1;
  } while (1 < uVar14);
  goto LAB_ffff000090734a08;
}


