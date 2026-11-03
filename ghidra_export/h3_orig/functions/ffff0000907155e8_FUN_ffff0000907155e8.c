// ffff0000907155e8  size=1540  FUN_ffff0000907155e8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907155e8(void)

{
  uint *puVar1;
  ulong uVar2;
  undefined4 uVar3;
  int iVar4;
  int iVar5;
  long lVar6;
  long lVar7;
  long lVar8;
  long *plVar9;
  long lVar10;
  long lVar11;
  ulong uVar12;
  char *pcVar13;
  undefined8 uVar14;
  uint uVar15;
  long *local_68;
  
  local_68 = (long *)0x0;
  lVar6 = FUN_ffff000090719218();
  lVar7 = FUN_ffff00009071922c();
  if ((lVar6 == 0) || (lVar7 == 0)) {
    FUN_ffff00009077de54(s__blackbox__pllk_log_header_is_nu_ffff0000907f41c5,lVar6,lVar7);
    iVar5 = 0;
    lVar7 = 0;
  }
  else {
    iVar5 = *(int *)(lVar6 + 0x4c) + *(int *)(lVar6 + 0x48) + *(int *)(lVar6 + 0x58);
  }
  lVar6 = FUN_ffff00009077d24c(0x1000);
  if (lVar6 == 0) {
    FUN_ffff00009077de54
              (s__s__d___boot_fail_WriteUefiLog___ffff0000907d59ad,s_WritePLLKLog_ffff0000907edb09,
               299);
    return;
  }
  FUN_ffff00009077eb88(lVar6,0,0x1000);
  lVar8 = FUN_ffff00009077d24c(0x1000);
  if (lVar8 == 0) {
    FUN_ffff00009077de54
              (s__s__d___boot_fail_WriteUefiLog___ffff0000907d59f8,s_WritePLLKLog_ffff0000907edb09,
               0x131);
    goto LAB_ffff0000907158e4;
  }
  FUN_ffff00009077eb88(lVar8,0,0x1000);
  plVar9 = (long *)FUN_ffff00009077d24c(0x1000);
  if (plVar9 == (long *)0x0) {
    pcVar13 = s__blackbox__PInfoBootFail__Alloc_h_ffff0000907da5b9;
LAB_ffff0000907158c0:
    FUN_ffff00009077de54(pcVar13);
    pcVar13 = s__s__d___boot_fail_WriteUefiLog___ffff0000907f40b5;
    uVar14 = 0x13d;
LAB_ffff0000907158d8:
    FUN_ffff00009077de54(pcVar13,s_WritePLLKLog_ffff0000907edb09,uVar14);
  }
  else {
    lVar10 = FUN_ffff00009077bd60(s_blackbox_ffff0000907f0c1b);
    if (lVar10 == 0) {
      pcVar13 = s__blackbox__PInfoBootFail__partit_ffff0000907fcbb9;
      goto LAB_ffff0000907158c0;
    }
    lVar10 = FUN_ffff00009077bdf4(lVar10,plVar9,0x400,0x1000);
    if (lVar10 < 1) {
      pcVar13 = s__blackbox__PInfoBootFail__partit_ffff0000907f9344;
      goto LAB_ffff0000907158c0;
    }
    _DAT_ffff00009083f810 = plVar9;
    FUN_ffff00009077de54
              (s__s__d___boot_fail__LogTableHeade_ffff0000907e0b9b,
               s_PrintLogTableHeaderBootFail_ffff0000907d26f0,0x5f,*plVar9,(int)plVar9[1],
               *(undefined4 *)((long)plVar9 + 0xc),*(undefined4 *)((long)plVar9 + 0x2c));
    if (*_DAT_ffff00009083f810 != 0x56775af41bcde0f3) {
      pcVar13 = s__s__d___boot_fail__WriteUefiLog__ffff0000907f5ca9;
      uVar14 = 0x144;
      goto LAB_ffff0000907158d8;
    }
    uVar3 = *(undefined4 *)((long)_DAT_ffff00009083f810 + 0x2c);
    plVar9 = (long *)FUN_ffff00009077d24c(0x1000);
    if (plVar9 == (long *)0x0) {
      pcVar13 = s__blackbox__PInfoBootFail__Alloc_h_ffff0000907da5b9;
LAB_ffff000090715944:
      FUN_ffff00009077de54(pcVar13);
      pcVar13 = s__s__d___boot_fail__WriteUefiLog___ffff0000907f5cf5;
      uVar14 = 0x14d;
      goto LAB_ffff0000907158d8;
    }
    lVar10 = FUN_ffff00009077bd60(s_blackbox_ffff0000907f0c1b);
    if (lVar10 == 0) {
      pcVar13 = s__blackbox__PInfoBootFail__partit_ffff0000907fcbb9;
      goto LAB_ffff000090715944;
    }
    lVar10 = FUN_ffff00009077bdf4(lVar10,plVar9,uVar3,0x1000);
    if (lVar10 < 1) {
      pcVar13 = s__blackbox__PInfoBootFail__partit_ffff0000907f9344;
      goto LAB_ffff000090715944;
    }
    _DAT_ffff00009083f818 = plVar9;
    FUN_ffff00009077de54
              (s__s__d___bootfail_error_type_head_ffff0000907df476,
               s_PrintErrorTypeHeader_ffff0000907dc13c,0x71,*plVar9,(int)plVar9[1],
               *(undefined4 *)((long)plVar9 + 0xc),*(undefined8 *)((long)plVar9 + 0x14),
               (int)plVar9[2],*(undefined4 *)((long)plVar9 + 0x1c),(int)plVar9[4],
               *(undefined8 *)((long)plVar9 + 0x24),(long)plVar9 + 0x2c,(long)plVar9 + 0xac);
    if (*_DAT_ffff00009083f818 != 0x627759541bcde0f0) {
      pcVar13 = s__s__d___boot_fail_WriteUefiLog___ffff0000907cf230;
      uVar14 = 0x154;
      goto LAB_ffff0000907158d8;
    }
    lVar10 = *(long *)((long)_DAT_ffff00009083f818 + 0x14);
    uVar15 = *(uint *)((long)_DAT_ffff00009083f810 + 0x2c);
    plVar9 = (long *)FUN_ffff00009077d24c(0x1000);
    if (plVar9 == (long *)0x0) {
      pcVar13 = s__blackbox__PInfoBootFail__Alloc_h_ffff0000907da5b9;
LAB_ffff000090715998:
      FUN_ffff00009077de54(pcVar13);
      pcVar13 = s__s__d___boot_fail_WriteUefiLog___ffff0000907d8d80;
      uVar14 = 0x15d;
      goto LAB_ffff0000907158d8;
    }
    lVar11 = FUN_ffff00009077bd60(s_blackbox_ffff0000907f0c1b);
    if (lVar11 == 0) {
      pcVar13 = s__blackbox__PInfoBootFail__partit_ffff0000907fcbb9;
      goto LAB_ffff000090715998;
    }
    lVar10 = FUN_ffff00009077bdf4(lVar11,plVar9,lVar10 + (ulong)uVar15,0x1000);
    if (lVar10 < 1) {
      pcVar13 = s__blackbox__PInfoBootFail__partit_ffff0000907f9344;
      goto LAB_ffff000090715998;
    }
    _DAT_ffff00009083f828 = plVar9;
    if (*plVar9 == -0x6b97daaf22da1e10) {
      uVar15 = *(uint *)((long)plVar9 + 0x1c);
      if (2 < uVar15) {
        pcVar13 = s__s__d___boot_fail__WriteUefiLog___ffff0000907e0bf5;
        uVar14 = 0x169;
        goto LAB_ffff0000907158d8;
      }
    }
    else {
      FUN_ffff00009077de54
                (s__s__d___boot_fail_WriteUefiLog___ffff0000907fcc5a,s_WritePLLKLog_ffff0000907edb09
                 ,0x163);
      plVar9 = _DAT_ffff00009083f828;
      uVar15 = 0;
      *_DAT_ffff00009083f828 = -0x6b97daaf22da1e10;
      plVar9[1] = 0x100000001000;
      plVar9[2] = 0x300000000;
      plVar9[3] = 0;
    }
    lVar10 = (ulong)*(uint *)((long)_DAT_ffff00009083f810 + 0x2c) + (ulong)(uVar15 * 0x41000) +
             *(long *)((long)_DAT_ffff00009083f818 + 0x14);
    uVar2 = lVar10 + 0x1000;
    FUN_ffff00009077de54
              (s__s__d___boot_fail__WriteUefiLog___ffff0000907d3f88,s_WritePLLKLog_ffff0000907edb09,
               0x16f,uVar2);
    iVar4 = FUN_ffff000090714e10(uVar2 & 0xffffffff,0x41000);
    if (iVar4 != 0) {
      pcVar13 = s__s__d___boot_fail_WriteUefiLog___ffff0000907f40f7;
      uVar14 = 0x172;
      goto LAB_ffff0000907158d8;
    }
    uVar12 = FUN_ffff000090714d70(uVar2,0x1000,&local_68);
    if ((uVar12 & 1) == 0) {
      pcVar13 = s__s__d___boot_fail_WriteUefiLog___ffff0000907d74d1;
      uVar14 = 0x178;
      goto LAB_ffff0000907158d8;
    }
    _DAT_ffff00009083f830 = local_68;
    plVar9 = local_68;
    if (*local_68 != 0x768b480dd48a6592) {
      FUN_ffff00009077de54
                (s__s__d___boot_fail__WriteUefiLog___ffff0000907f413e,
                 s_WritePLLKLog_ffff0000907edb09,0x17e);
      plVar9 = _DAT_ffff00009083f830;
      *_DAT_ffff00009083f830 = 0x768b480dd48a6592;
      plVar9[1] = 0x100000001000;
      *(undefined4 *)(plVar9 + 2) = 0;
    }
    *(int *)(plVar9 + 2) = (int)_DAT_ffff00009083f810[2];
    FUN_ffff00009077e858(lVar8,plVar9,0x14);
    FUN_ffff000090714d40(lVar8);
    iVar4 = FUN_ffff000090715568(uVar2,0x1000,lVar8);
    if (iVar4 != 0) {
      pcVar13 = s__s__d___boot_fail_WriteUefiLog___ffff0000907f4185;
      uVar14 = 0x188;
      goto LAB_ffff0000907158d8;
    }
    iVar5 = FUN_ffff000090715568(lVar10 + 0x2000,iVar5,lVar7);
    plVar9 = _DAT_ffff00009083f828;
    if (iVar5 != 0) {
      pcVar13 = s__s__d___boot_fail_WriteUefiLog___ffff000090801f06;
      uVar14 = 0x191;
      goto LAB_ffff0000907158d8;
    }
    puVar1 = (uint *)(_DAT_ffff00009083f828 + 3);
    *(int *)(_DAT_ffff00009083f828 + 2) = (int)_DAT_ffff00009083f810[2];
    if (*puVar1 < 3) {
      *(uint *)(plVar9 + 3) = *puVar1 + 1;
    }
    iVar5 = 0;
    if (*(uint *)((long)plVar9 + 0x1c) < 2) {
      iVar5 = *(uint *)((long)plVar9 + 0x1c) + 1;
    }
    *(int *)((long)plVar9 + 0x1c) = iVar5;
    FUN_ffff00009077e858(lVar6,plVar9,0x20);
    FUN_ffff000090714cf0(lVar6);
    iVar5 = FUN_ffff000090715568
                      (*(long *)((long)_DAT_ffff00009083f818 + 0x14) +
                       (ulong)*(uint *)((long)_DAT_ffff00009083f810 + 0x2c),0x1000,lVar6);
    if (iVar5 != 0) {
      pcVar13 = s__s__d___boot_fail_WriteUefiLog___ffff0000907f4185;
      uVar14 = 0x1a4;
      goto LAB_ffff0000907158d8;
    }
    _DAT_ffff00009083f810 = (long *)0x0;
    _DAT_ffff00009083f818 = (long *)0x0;
    _DAT_ffff00009083f828 = (long *)0x0;
    _DAT_ffff00009083f820 = 0;
    _DAT_ffff00009083f830 = (long *)0x0;
  }
  thunk_FUN_ffff0000907c2ad8(lVar8);
LAB_ffff0000907158e4:
  thunk_FUN_ffff0000907c2ad8(lVar6);
  return;
}


