// ffff00009079861c  size=776  FUN_ffff00009079861c

undefined8 FUN_ffff00009079861c(uint param_1,undefined8 param_2,undefined4 param_3,ulong param_4)

{
  uint uVar1;
  char *pcVar2;
  undefined8 uVar3;
  int iVar4;
  ulong uVar5;
  long lVar6;
  
  uVar5 = (ulong)param_1;
  if (DAT_ffff0000908373c0 == param_1) {
    lVar6 = 0;
  }
  else if (DAT_ffff0000908373d8 == param_1) {
    lVar6 = 1;
  }
  else if (DAT_ffff0000908373f0 == param_1) {
    lVar6 = 2;
  }
  else if (DAT_ffff000090837408 == param_1) {
    lVar6 = 3;
  }
  else if (DAT_ffff000090837420 == param_1) {
    lVar6 = 4;
  }
  else if (DAT_ffff000090837438 == param_1) {
    lVar6 = 5;
  }
  else if (DAT_ffff000090837450 == param_1) {
    lVar6 = 6;
  }
  else if (DAT_ffff000090837468 == param_1) {
    lVar6 = 7;
  }
  else if (DAT_ffff000090837480 == param_1) {
    lVar6 = 8;
  }
  else if (DAT_ffff000090837498 == param_1) {
    lVar6 = 9;
  }
  else if (DAT_ffff0000908374b0 == param_1) {
    lVar6 = 10;
  }
  else {
    if (DAT_ffff0000908374c8 != param_1) {
      pcVar2 = s__s__d___CCCI_MPU__Find_blk_id__d_ffff0000907e3d6a;
      uVar3 = 0xbf;
      goto LAB_ffff000090798798;
    }
    lVar6 = 0xb;
  }
  uVar1 = (&DAT_ffff0000908373bc)[lVar6 * 6];
  if ((uVar1 >> 1 & 1) != 0) {
    pcVar2 = s__s__d___CCCI_MPU__blk_id__d__is_p_ffff0000907fabb3;
    uVar3 = 0xc3;
LAB_ffff000090798798:
    FUN_ffff00009077e11c(pcVar2,s_ccci_plat_update_md_mpu_setting_ffff0000907f3ab8,uVar3,uVar5);
    return 0xffffffff;
  }
  iVar4 = (int)param_4;
  if (iVar4 < 0) {
    (&DAT_ffff0000908373b0)[lVar6 * 3] = param_2;
    (&DAT_ffff0000908373bc)[lVar6 * 6] = uVar1 | 1;
    (&DAT_ffff0000908373b8)[lVar6 * 6] = param_3;
    return 0;
  }
  if (DAT_ffff0000908373c4 == iVar4) {
    lVar6 = 0;
  }
  else if (DAT_ffff0000908373dc == iVar4) {
    lVar6 = 1;
  }
  else if (DAT_ffff0000908373f4 == iVar4) {
    lVar6 = 2;
  }
  else if (DAT_ffff00009083740c == iVar4) {
    lVar6 = 3;
  }
  else if (DAT_ffff000090837424 == iVar4) {
    lVar6 = 4;
  }
  else if (DAT_ffff00009083743c == iVar4) {
    lVar6 = 5;
  }
  else if (DAT_ffff000090837454 == iVar4) {
    lVar6 = 6;
  }
  else if (DAT_ffff00009083746c == iVar4) {
    lVar6 = 7;
  }
  else if (DAT_ffff000090837484 == iVar4) {
    lVar6 = 8;
  }
  else if (DAT_ffff00009083749c == iVar4) {
    lVar6 = 9;
  }
  else if (DAT_ffff0000908374b4 == iVar4) {
    lVar6 = 10;
  }
  else {
    if (DAT_ffff0000908374cc != iVar4) {
      pcVar2 = s__s__d___CCCI_MPU__Find_padding_b_ffff00009080000a;
      uVar3 = 0xd7;
      uVar5 = param_4;
      goto LAB_ffff000090798798;
    }
    lVar6 = 0xb;
  }
  uVar1 = (&DAT_ffff0000908373bc)[lVar6 * 6];
  (&DAT_ffff0000908373b0)[lVar6 * 3] = param_2;
  (&DAT_ffff0000908373b8)[lVar6 * 6] = param_3;
  (&DAT_ffff0000908373bc)[lVar6 * 6] = uVar1 | 3;
  (&DAT_ffff0000908373c0)[lVar6 * 6] = param_1;
  return 0;
}


