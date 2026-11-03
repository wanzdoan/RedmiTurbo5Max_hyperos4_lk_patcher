// ffff000090711b00  size=1156  FUN_ffff000090711b00

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

ulong FUN_ffff000090711b00(undefined8 param_1,undefined8 param_2)

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
  
  pbVar5 = (byte *)FUN_ffff00009077d24c(0x800);
  if (pbVar5 == (byte *)0x0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_app_mt_boot_bootconfig_c_ffff0000907e7418,0xa0,s_bootconfig_buf_ffff0000907f7acb);
  }
  FUN_ffff00009077eb88(pbVar5,0,0x800);
  _DAT_ffff00009083f760 = pbVar5 + 0x800;
  _DAT_ffff00009083f758 = pbVar5;
  lVar6 = FUN_ffff00009077d24c(0x800);
  if (lVar6 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_app_mt_boot_bootconfig_c_ffff0000907e7418,0xa7,
               s_non_bootconfig_buf_ffff0000907d5812);
  }
  FUN_ffff00009077eb88(lVar6,0,0x800);
  _DAT_ffff00009083f770 = lVar6 + 0x800;
  _DAT_ffff00009083f768 = lVar6;
  iVar2 = FUN_ffff00009076d75c(param_1,s__chosen_ffff0000907ea5b1);
  if (iVar2 < 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_app_mt_boot_bootconfig_c_ffff0000907e7418,0xae,s_offset_>__0_ffff0000907f3f8a);
  }
  local_64 = 0;
  lVar7 = FUN_ffff00009076dcd4(param_1,iVar2,s_bootargs_ffff0000907dd913,&local_64);
  if (lVar7 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cc18(s_get_all_cmdline_fail_in_fdt____ffff000090801d8e);
  }
  lVar8 = FUN_ffff00009077d24c(0x1000);
  if (lVar8 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_app_mt_boot_bootconfig_c_ffff0000907e7418,0x42,s_cmdline_ffff0000907fe92c);
  }
  FUN_ffff00009077eb88(lVar8,0,0x1000);
  FUN_ffff00009077efe4(lVar8,lVar7,0xfff);
  lVar7 = FUN_ffff00009077f308(lVar8,&DAT_ffff00009080031e);
  if (lVar7 != 0) {
    do {
      uVar9 = FUN_ffff00009077ef88(lVar7);
      iVar3 = FUN_ffff00009077f080(lVar7,s_androidboot__ffff0000907d741c,0xc);
      if (iVar3 == 0) {
        if (_DAT_ffff00009083f760 <= _DAT_ffff00009083f758 + (uVar9 & 0xffffffff) + 1)
        goto LAB_ffff000090711f60;
        iVar3 = FUN_ffff00009077f434
                          (_DAT_ffff00009083f758,
                           (long)_DAT_ffff00009083f760 - (long)_DAT_ffff00009083f758,
                           &DAT_ffff0000907df3fb,lVar7);
        plVar10 = (long *)&DAT_ffff00009083f758;
      }
      else {
        if (_DAT_ffff00009083f770 <= _DAT_ffff00009083f768 + (uVar9 & 0xffffffff) + 1)
        goto LAB_ffff000090711f60;
        iVar3 = FUN_ffff00009077f434
                          (_DAT_ffff00009083f768,_DAT_ffff00009083f770 - _DAT_ffff00009083f768,
                           &DAT_ffff0000907e2509,lVar7);
        plVar10 = (long *)&DAT_ffff00009083f768;
      }
      *plVar10 = *plVar10 + (long)iVar3;
      lVar7 = FUN_ffff00009077f308(0,&DAT_ffff00009080031e);
    } while (lVar7 != 0);
  }
  thunk_FUN_ffff0000907c2ad8(lVar8);
  if (_DAT_ffff00009083f770 <= _DAT_ffff00009083f768 + 0xbU) {
LAB_ffff000090711f60:
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cc18(s__ERROR__Bootconfig__cmdline_buff_ffff0000907f3f96);
  }
  iVar3 = FUN_ffff00009077f434
                    (_DAT_ffff00009083f768,_DAT_ffff00009083f770 - _DAT_ffff00009083f768,
                     &DAT_ffff0000907e2509,s_bootconfig_ffff0000907e7431);
  _DAT_ffff00009083f768 = _DAT_ffff00009083f768 + iVar3;
  iVar3 = FUN_ffff00009077ef88(lVar6);
  iVar2 = FUN_ffff00009076e40c(param_1,iVar2,s_bootargs_ffff0000907dd913,lVar6,iVar3 + 1);
  pbVar11 = _DAT_ffff00009083f758;
  lVar7 = _DAT_ffff00009083f750;
  if (iVar2 != 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cc18(s_set_cmdline_fail_from_non_bootco_ffff0000907df3ce);
  }
  if (_DAT_ffff00009083f750 != 0) {
    lVar8 = FUN_ffff00009077ef88(_DAT_ffff00009083f750);
    if (_DAT_ffff00009083f760 <= pbVar11 + lVar8 + 1) goto LAB_ffff000090711f60;
    iVar2 = FUN_ffff00009077f434
                      (pbVar11,(long)_DAT_ffff00009083f760 - (long)pbVar11,&DAT_ffff0000907df3fb,
                       lVar7);
    _DAT_ffff00009083f758 = _DAT_ffff00009083f758 + iVar2;
  }
  uVar4 = FUN_ffff00009077ef88(pbVar5);
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
      if (uVar12 == uVar9) goto LAB_ffff000090711e40;
    }
    do {
      iVar2 = iVar2 + -1;
      uVar15 = uVar15 + *pbVar11;
      pbVar11 = pbVar11 + 1;
    } while (iVar2 != 0);
  }
LAB_ffff000090711e40:
  lVar7 = FUN_ffff00009077d2e0((ulong)(uVar4 + 5) + 0x14,1);
  if (lVar7 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_app_mt_boot_bootconfig_c_ffff0000907e7418,0x76,s_bootconfig_data_ffff0000907fafb2);
  }
  FUN_ffff00009077e858(lVar7,pbVar5,uVar9);
  uVar4 = uVar4 - (uVar4 | 0xfffffffc);
  puVar1 = (uint *)(lVar7 + (ulong)uVar4);
  *puVar1 = uVar4;
  puVar1[1] = uVar15;
  FUN_ffff00009077e858(puVar1 + 2,s__BOOTCONFIG_ffff0000907e743c,0xc);
  uVar9 = (ulong)uVar4 + 0x14;
  FUN_ffff00009077e858(param_2,lVar7,uVar9 & 0xffffffff);
  thunk_FUN_ffff0000907c2ad8(lVar7);
  thunk_FUN_ffff0000907c2ad8(pbVar5);
  thunk_FUN_ffff0000907c2ad8(lVar6);
  thunk_FUN_ffff0000907c2ad8(_DAT_ffff00009083f750);
  _DAT_ffff00009083f750 = 0;
  return uVar9 & 0xffffffff;
}


