// ffff00009079835c  size=776  FUN_ffff00009079835c

undefined8 FUN_ffff00009079835c(uint param_1,undefined8 param_2,undefined4 param_3,ulong param_4)

{
  uint uVar1;
  char *pcVar2;
  undefined8 uVar3;
  int iVar4;
  ulong uVar5;
  long lVar6;
  
  uVar5 = (ulong)param_1;
  if (DAT_ffff0000908373a0 == param_1) {
    lVar6 = 0;
  }
  else if (DAT_ffff0000908373b8 == param_1) {
    lVar6 = 1;
  }
  else if (DAT_ffff0000908373d0 == param_1) {
    lVar6 = 2;
  }
  else if (DAT_ffff0000908373e8 == param_1) {
    lVar6 = 3;
  }
  else if (DAT_ffff000090837400 == param_1) {
    lVar6 = 4;
  }
  else if (DAT_ffff000090837418 == param_1) {
    lVar6 = 5;
  }
  else if (DAT_ffff000090837430 == param_1) {
    lVar6 = 6;
  }
  else if (DAT_ffff000090837448 == param_1) {
    lVar6 = 7;
  }
  else if (DAT_ffff000090837460 == param_1) {
    lVar6 = 8;
  }
  else if (DAT_ffff000090837478 == param_1) {
    lVar6 = 9;
  }
  else if (DAT_ffff000090837490 == param_1) {
    lVar6 = 10;
  }
  else {
    if (DAT_ffff0000908374a8 != param_1) {
      pcVar2 = s__s__d___CCCI_MPU__Find_blk_id__d_ffff0000907e3c99;
      uVar3 = 0xbf;
      goto LAB_ffff0000907984d8;
    }
    lVar6 = 0xb;
  }
  uVar1 = (&DAT_ffff00009083739c)[lVar6 * 6];
  if ((uVar1 >> 1 & 1) != 0) {
    pcVar2 = s__s__d___CCCI_MPU__blk_id__d__is_p_ffff0000907faa17;
    uVar3 = 0xc3;
LAB_ffff0000907984d8:
    FUN_ffff00009077de54(pcVar2,s_ccci_plat_update_md_mpu_setting_ffff0000907f392c,uVar3,uVar5);
    return 0xffffffff;
  }
  iVar4 = (int)param_4;
  if (iVar4 < 0) {
    (&DAT_ffff000090837390)[lVar6 * 3] = param_2;
    (&DAT_ffff00009083739c)[lVar6 * 6] = uVar1 | 1;
    (&DAT_ffff000090837398)[lVar6 * 6] = param_3;
    return 0;
  }
  if (DAT_ffff0000908373a4 == iVar4) {
    lVar6 = 0;
  }
  else if (DAT_ffff0000908373bc == iVar4) {
    lVar6 = 1;
  }
  else if (DAT_ffff0000908373d4 == iVar4) {
    lVar6 = 2;
  }
  else if (DAT_ffff0000908373ec == iVar4) {
    lVar6 = 3;
  }
  else if (DAT_ffff000090837404 == iVar4) {
    lVar6 = 4;
  }
  else if (DAT_ffff00009083741c == iVar4) {
    lVar6 = 5;
  }
  else if (DAT_ffff000090837434 == iVar4) {
    lVar6 = 6;
  }
  else if (DAT_ffff00009083744c == iVar4) {
    lVar6 = 7;
  }
  else if (DAT_ffff000090837464 == iVar4) {
    lVar6 = 8;
  }
  else if (DAT_ffff00009083747c == iVar4) {
    lVar6 = 9;
  }
  else if (DAT_ffff000090837494 == iVar4) {
    lVar6 = 10;
  }
  else {
    if (DAT_ffff0000908374ac != iVar4) {
      pcVar2 = s__s__d___CCCI_MPU__Find_padding_b_ffff0000907ffe62;
      uVar3 = 0xd7;
      uVar5 = param_4;
      goto LAB_ffff0000907984d8;
    }
    lVar6 = 0xb;
  }
  uVar1 = (&DAT_ffff00009083739c)[lVar6 * 6];
  (&DAT_ffff000090837390)[lVar6 * 3] = param_2;
  (&DAT_ffff000090837398)[lVar6 * 6] = param_3;
  (&DAT_ffff00009083739c)[lVar6 * 6] = uVar1 | 3;
  (&DAT_ffff0000908373a0)[lVar6 * 6] = param_1;
  return 0;
}


