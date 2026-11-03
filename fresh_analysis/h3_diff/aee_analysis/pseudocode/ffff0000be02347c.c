
undefined8 FUN_ffff0000be02347c(long param_1,uint param_2,uint param_3,int *param_4,int *param_5)

{
  char *pcVar1;
  uint uVar2;
  uint uVar3;
  uint uVar4;
  int iVar5;
  int iVar6;
  uint uVar7;
  uint uVar8;
  uint uVar9;
  uint uVar10;
  uint uVar11;
  
  if (0xd48 < param_3) {
    return 0xfffffff9;
  }
  uVar3 = 0;
  if (param_3 != 0) {
    uVar3 = ((param_3 + (param_2 >> 1)) - 1) / param_3;
  }
  uVar4 = 8;
  if (param_3 < 0x191) {
    uVar4 = 0x40;
  }
  uVar2 = uVar4 << 3;
  if (uVar4 < uVar3 || uVar2 <= uVar3) {
    uVar11 = uVar3 + 1 >> 1;
    uVar8 = uVar3 + 1 & 0xfffffffe;
    if (uVar4 < uVar11 || uVar2 <= uVar8) {
      iVar6 = 8;
      uVar11 = uVar4;
      uVar8 = uVar2;
    }
    else {
      iVar6 = 2;
      iVar5 = 2;
      if (uVar8 == uVar3) goto LAB_ffff0000be0234f0;
    }
    uVar9 = (uVar3 + 2) / 3;
    uVar2 = uVar9 * 3;
    uVar10 = (uVar3 + 2) / 3;
    uVar7 = uVar11;
    if (uVar10 <= uVar4 && uVar2 < uVar8) {
      iVar6 = 3;
      uVar7 = uVar10;
      uVar8 = uVar2;
      iVar5 = 3;
      uVar11 = uVar9;
      if (uVar2 == uVar3) goto LAB_ffff0000be0234f0;
    }
    uVar11 = uVar3 + 3 >> 2;
    if ((uVar11 <= uVar4) && (uVar2 = uVar3 + 3 & 0xfffffffc, uVar2 < uVar8)) {
      iVar6 = 4;
      uVar7 = uVar11;
      uVar8 = uVar2;
      iVar5 = 4;
      if (uVar2 == uVar3) goto LAB_ffff0000be0234f0;
    }
    uVar11 = (uVar3 + 4) / 5;
    if (uVar11 <= uVar4) {
      uVar9 = (uVar3 + 4) / 5;
      uVar2 = uVar9 * 5;
      if (uVar2 < uVar8) {
        iVar6 = 5;
        uVar7 = uVar9;
        uVar8 = uVar2;
        iVar5 = 5;
        if (uVar2 == uVar3) goto LAB_ffff0000be0234f0;
      }
    }
    uVar11 = (uVar3 + 5) / 6;
    if (uVar11 <= uVar4) {
      uVar9 = (uVar3 + 5) / 6;
      uVar2 = uVar9 * 6;
      if (uVar2 < uVar8) {
        iVar6 = 6;
        uVar7 = uVar9;
        uVar8 = uVar2;
        iVar5 = 6;
        if (uVar2 == uVar3) goto LAB_ffff0000be0234f0;
      }
    }
    uVar11 = (uVar3 + 6) / 7;
    if ((uVar11 <= uVar4) && (uVar2 = uVar11 * 7, uVar2 < uVar8)) {
      iVar6 = 7;
      uVar7 = uVar11;
      uVar8 = uVar2;
      iVar5 = 7;
      if (uVar2 == uVar3) goto LAB_ffff0000be0234f0;
    }
    uVar11 = uVar3 + 7 >> 3;
    iVar5 = 8;
    if (uVar4 < uVar11 || uVar8 <= (uVar3 + 7 & 0xfffffff8)) {
      iVar5 = iVar6;
      uVar11 = uVar7;
    }
  }
  else {
    iVar5 = 1;
    uVar11 = uVar3;
  }
LAB_ffff0000be0234f0:
  uVar3 = iVar5 * uVar11 * 2;
  uVar4 = 0;
  if (uVar3 != 0) {
    uVar4 = param_2 / uVar3;
  }
  if (uVar4 <= param_3) {
    *param_4 = uVar11 - (iVar5 + -1 != 0);
    *param_5 = iVar5 + -1;
    return 0;
  }
  pcVar1 = &DAT_ffff0000be0a596d;
  if (param_3 < 0x191) {
    pcVar1 = s_ST_FT_ffff0000be0b2e2d;
  }
  FUN_ffff0000be05fd98
            (s__s__d__i2c_d__s_mode__unsupporte_ffff0000be0bd09b,
             s_mtk_i2c_calculate_speed_ffff0000be0a5955,0x6b,*(undefined2 *)(param_1 + 8),pcVar1);
  return 0xfffffff9;
}


