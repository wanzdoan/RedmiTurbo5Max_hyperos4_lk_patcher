
ulong FUN_ffff000090711be4(undefined8 param_1,undefined8 param_2)

{
  uint *puVar1;
  int iVar2;
  int iVar3;
  uint uVar4;
  byte *pbVar5;
  long lVar6;
  long lVar7;
  long lVar8;
  ulong uVar9;
  long *plVar10;
  byte *pbVar11;
  ulong uVar12;
  int iVar13;
  ulong uVar14;
  uint uVar15;
  undefined4 local_64;
  
  pbVar5 = (byte *)FUN_ffff00009077d514(0x800);
  if (pbVar5 == (byte *)0x0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_app_mt_boot_bootconfig_c_ffff0000907e755c,0xa0,s_bootconfig_buf_ffff0000907f7c89);
  }
  FUN_ffff00009077ee50(pbVar5,0,0x800);
  DAT_ffff00009083f798 = pbVar5 + 0x800;
  DAT_ffff00009083f790 = pbVar5;
  lVar6 = FUN_ffff00009077d514(0x800);
  if (lVar6 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_app_mt_boot_bootconfig_c_ffff0000907e755c,0xa7,
               s_non_bootconfig_buf_ffff0000907d5862);
  }
  FUN_ffff00009077ee50(lVar6,0,0x800);
  DAT_ffff00009083f7a8 = lVar6 + 0x800;
  DAT_ffff00009083f7a0 = lVar6;
  iVar2 = FUN_ffff00009076da24(param_1,s__chosen_ffff0000907ea70b);
  if (iVar2 < 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_app_mt_boot_bootconfig_c_ffff0000907e755c,0xae,s_offset_>__0_ffff0000907f4116);
  }
  local_64 = 0;
  lVar7 = FUN_ffff00009076df9c(param_1,iVar2,s_bootargs_ffff0000907dd9b6,&local_64);
  if (lVar7 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cee0(s_get_all_cmdline_fail_in_fdt____ffff000090801f74);
  }
  lVar8 = FUN_ffff00009077d514(0x1000);
  if (lVar8 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_app_mt_boot_bootconfig_c_ffff0000907e755c,0x42,s_cmdline_ffff0000907fead4);
  }
  FUN_ffff00009077ee50(lVar8,0,0x1000);
  FUN_ffff00009077f2ac(lVar8,lVar7,0xfff);
  lVar7 = FUN_ffff00009077f5d0(lVar8,&DAT_ffff0000908004fa);
  if (lVar7 != 0) {
    do {
      uVar9 = FUN_ffff00009077f250(lVar7);
      iVar3 = FUN_ffff00009077f348(lVar7,s_androidboot__ffff0000907d74b2,0xc);
      if (iVar3 == 0) {
        if (DAT_ffff00009083f798 <= DAT_ffff00009083f790 + (uVar9 & 0xffffffff) + 1)
        goto LAB_ffff000090712044;
        iVar3 = FUN_ffff00009077f6fc
                          (DAT_ffff00009083f790,
                           (long)DAT_ffff00009083f798 - (long)DAT_ffff00009083f790,
                           &DAT_ffff0000907df49e,lVar7);
        plVar10 = (long *)&DAT_ffff00009083f790;
      }
      else {
        if (DAT_ffff00009083f7a8 <= DAT_ffff00009083f7a0 + (uVar9 & 0xffffffff) + 1)
        goto LAB_ffff000090712044;
        iVar3 = FUN_ffff00009077f6fc
                          (DAT_ffff00009083f7a0,DAT_ffff00009083f7a8 - DAT_ffff00009083f7a0,
                           &DAT_ffff0000907e25da,lVar7);
        plVar10 = &DAT_ffff00009083f7a0;
      }
      *plVar10 = *plVar10 + (long)iVar3;
      lVar7 = FUN_ffff00009077f5d0(0,&DAT_ffff0000908004fa);
    } while (lVar7 != 0);
  }
  thunk_FUN_ffff0000907c314c(lVar8);
  if (DAT_ffff00009083f7a8 <= DAT_ffff00009083f7a0 + 0xbU) {
LAB_ffff000090712044:
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cee0(s__ERROR__Bootconfig__cmdline_buff_ffff0000907f4122);
  }
  iVar3 = FUN_ffff00009077f6fc
                    (DAT_ffff00009083f7a0,DAT_ffff00009083f7a8 - DAT_ffff00009083f7a0,
                     &DAT_ffff0000907e25da,s_bootconfig_ffff0000907e7575);
  DAT_ffff00009083f7a0 = DAT_ffff00009083f7a0 + iVar3;
  iVar3 = FUN_ffff00009077f250(lVar6);
  iVar2 = FUN_ffff00009076e6d4(param_1,iVar2,s_bootargs_ffff0000907dd9b6,lVar6,iVar3 + 1);
  pbVar11 = DAT_ffff00009083f790;
  lVar7 = DAT_ffff00009083f788;
  if (iVar2 != 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cee0(s_set_cmdline_fail_from_non_bootco_ffff0000907df471);
  }
  if (DAT_ffff00009083f788 != 0) {
    lVar8 = FUN_ffff00009077f250(DAT_ffff00009083f788);
    if (DAT_ffff00009083f798 <= pbVar11 + lVar8 + 1) goto LAB_ffff000090712044;
    iVar2 = FUN_ffff00009077f6fc
                      (pbVar11,(long)DAT_ffff00009083f798 - (long)pbVar11,&DAT_ffff0000907df49e,
                       lVar7);
    DAT_ffff00009083f790 = DAT_ffff00009083f790 + iVar2;
  }
  uVar4 = FUN_ffff00009077f250(pbVar5);
  uVar9 = (ulong)(uVar4 + 1);
  if (uVar4 == 0xffffffff) {
    uVar15 = 0;
  }
  else {
    if (uVar4 == 0) {
      uVar15 = 0;
      iVar2 = 1;
      pbVar11 = pbVar5;
    }
    else {
      uVar12 = uVar9 & 0xfffffffe;
      iVar3 = 0;
      iVar13 = 0;
      iVar2 = (uVar4 + 1) - (int)uVar12;
      pbVar11 = pbVar5 + 1;
      uVar14 = uVar12;
      do {
        uVar14 = uVar14 - 2;
        iVar3 = iVar3 + (uint)pbVar11[-1];
        iVar13 = iVar13 + (uint)*pbVar11;
        pbVar11 = pbVar11 + 2;
      } while (uVar14 != 0);
      uVar15 = iVar13 + iVar3;
      pbVar11 = pbVar5 + uVar12;
      if (uVar12 == uVar9) goto LAB_ffff000090711f24;
    }
    do {
      iVar2 = iVar2 + -1;
      uVar15 = uVar15 + *pbVar11;
      pbVar11 = pbVar11 + 1;
    } while (iVar2 != 0);
  }
LAB_ffff000090711f24:
  lVar7 = FUN_ffff00009077d5a8((ulong)(uVar4 + 5) + 0x14,1);
  if (lVar7 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_app_mt_boot_bootconfig_c_ffff0000907e755c,0x76,s_bootconfig_data_ffff0000907fb14e);
  }
  FUN_ffff00009077eb20(lVar7,pbVar5,uVar9);
  uVar4 = uVar4 - (uVar4 | 0xfffffffc);
  puVar1 = (uint *)(lVar7 + (ulong)uVar4);
  *puVar1 = uVar4;
  puVar1[1] = uVar15;
  FUN_ffff00009077eb20(puVar1 + 2,s__BOOTCONFIG_ffff0000907e7580,0xc);
  uVar9 = (ulong)uVar4 + 0x14;
  FUN_ffff00009077eb20(param_2,lVar7,uVar9 & 0xffffffff);
  thunk_FUN_ffff0000907c314c(lVar7);
  thunk_FUN_ffff0000907c314c(pbVar5);
  thunk_FUN_ffff0000907c314c(lVar6);
  thunk_FUN_ffff0000907c314c(DAT_ffff00009083f788);
  DAT_ffff00009083f788 = 0;
  return uVar9 & 0xffffffff;
}


