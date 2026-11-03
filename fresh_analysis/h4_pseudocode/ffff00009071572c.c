
void FUN_ffff00009071572c(void)

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
  lVar6 = FUN_ffff00009071935c();
  lVar7 = FUN_ffff000090719370();
  if ((lVar6 == 0) || (lVar7 == 0)) {
    FUN_ffff00009077e11c(s__blackbox__pllk_log_header_is_nu_ffff0000907f4332,lVar6,lVar7);
    iVar5 = 0;
    lVar7 = 0;
  }
  else {
    iVar5 = *(int *)(lVar6 + 0x4c) + *(int *)(lVar6 + 0x48) + *(int *)(lVar6 + 0x58);
  }
  lVar6 = FUN_ffff00009077d514(0x1000);
  if (lVar6 == 0) {
    FUN_ffff00009077e11c
              (s__s__d___boot_fail_WriteUefiLog___ffff0000907d59fd,s_WritePLLKLog_ffff0000907edc7a,
               299);
    return;
  }
  FUN_ffff00009077ee50(lVar6,0,0x1000);
  lVar8 = FUN_ffff00009077d514(0x1000);
  if (lVar8 == 0) {
    FUN_ffff00009077e11c
              (s__s__d___boot_fail_WriteUefiLog___ffff0000907d5a48,s_WritePLLKLog_ffff0000907edc7a,
               0x131);
    goto LAB_ffff000090715a28;
  }
  FUN_ffff00009077ee50(lVar8,0,0x1000);
  plVar9 = (long *)FUN_ffff00009077d514(0x1000);
  if (plVar9 == (long *)0x0) {
    pcVar13 = s__blackbox__PInfoBootFail__Alloc_h_ffff0000907da6c4;
LAB_ffff000090715a04:
    FUN_ffff00009077e11c(pcVar13);
    pcVar13 = s__s__d___boot_fail_WriteUefiLog___ffff0000907f4222;
    uVar14 = 0x13d;
LAB_ffff000090715a1c:
    FUN_ffff00009077e11c(pcVar13,s_WritePLLKLog_ffff0000907edc7a,uVar14);
  }
  else {
    lVar10 = FUN_ffff00009077c028(s_blackbox_ffff0000907f0d8c);
    if (lVar10 == 0) {
      pcVar13 = s__blackbox__PInfoBootFail__partit_ffff0000907fcd61;
      goto LAB_ffff000090715a04;
    }
    lVar10 = FUN_ffff00009077c0bc(lVar10,plVar9,0x400,0x1000);
    if (lVar10 < 1) {
      pcVar13 = s__blackbox__PInfoBootFail__partit_ffff0000907f94e0;
      goto LAB_ffff000090715a04;
    }
    DAT_ffff00009083f848 = plVar9;
    FUN_ffff00009077e11c
              (s__s__d___boot_fail__LogTableHeade_ffff0000907e0c3e,
               s_PrintLogTableHeaderBootFail_ffff0000907d272d,0x5f,*plVar9,(int)plVar9[1],
               *(undefined4 *)((long)plVar9 + 0xc),*(undefined4 *)((long)plVar9 + 0x2c));
    if (*DAT_ffff00009083f848 != 0x56775af41bcde0f3) {
      pcVar13 = s__s__d___boot_fail__WriteUefiLog__ffff0000907f5e20;
      uVar14 = 0x144;
      goto LAB_ffff000090715a1c;
    }
    uVar3 = *(undefined4 *)((long)DAT_ffff00009083f848 + 0x2c);
    plVar9 = (long *)FUN_ffff00009077d514(0x1000);
    if (plVar9 == (long *)0x0) {
      pcVar13 = s__blackbox__PInfoBootFail__Alloc_h_ffff0000907da6c4;
LAB_ffff000090715a88:
      FUN_ffff00009077e11c(pcVar13);
      pcVar13 = s__s__d___boot_fail__WriteUefiLog___ffff0000907f5e6c;
      uVar14 = 0x14d;
      goto LAB_ffff000090715a1c;
    }
    lVar10 = FUN_ffff00009077c028(s_blackbox_ffff0000907f0d8c);
    if (lVar10 == 0) {
      pcVar13 = s__blackbox__PInfoBootFail__partit_ffff0000907fcd61;
      goto LAB_ffff000090715a88;
    }
    lVar10 = FUN_ffff00009077c0bc(lVar10,plVar9,uVar3,0x1000);
    if (lVar10 < 1) {
      pcVar13 = s__blackbox__PInfoBootFail__partit_ffff0000907f94e0;
      goto LAB_ffff000090715a88;
    }
    DAT_ffff00009083f850 = plVar9;
    FUN_ffff00009077e11c
              (s__s__d___bootfail_error_type_head_ffff0000907df519,
               s_PrintErrorTypeHeader_ffff0000907dc212,0x71,*plVar9,(int)plVar9[1],
               *(undefined4 *)((long)plVar9 + 0xc),*(undefined8 *)((long)plVar9 + 0x14),
               (int)plVar9[2],*(undefined4 *)((long)plVar9 + 0x1c),(int)plVar9[4],
               *(undefined8 *)((long)plVar9 + 0x24),(long)plVar9 + 0x2c,(long)plVar9 + 0xac);
    if (*DAT_ffff00009083f850 != 0x627759541bcde0f0) {
      pcVar13 = s__s__d___boot_fail_WriteUefiLog___ffff0000907cf24f;
      uVar14 = 0x154;
      goto LAB_ffff000090715a1c;
    }
    lVar10 = *(long *)((long)DAT_ffff00009083f850 + 0x14);
    uVar15 = *(uint *)((long)DAT_ffff00009083f848 + 0x2c);
    plVar9 = (long *)FUN_ffff00009077d514(0x1000);
    if (plVar9 == (long *)0x0) {
      pcVar13 = s__blackbox__PInfoBootFail__Alloc_h_ffff0000907da6c4;
LAB_ffff000090715adc:
      FUN_ffff00009077e11c(pcVar13);
      pcVar13 = s__s__d___boot_fail_WriteUefiLog___ffff0000907d8e17;
      uVar14 = 0x15d;
      goto LAB_ffff000090715a1c;
    }
    lVar11 = FUN_ffff00009077c028(s_blackbox_ffff0000907f0d8c);
    if (lVar11 == 0) {
      pcVar13 = s__blackbox__PInfoBootFail__partit_ffff0000907fcd61;
      goto LAB_ffff000090715adc;
    }
    lVar10 = FUN_ffff00009077c0bc(lVar11,plVar9,lVar10 + (ulong)uVar15,0x1000);
    if (lVar10 < 1) {
      pcVar13 = s__blackbox__PInfoBootFail__partit_ffff0000907f94e0;
      goto LAB_ffff000090715adc;
    }
    DAT_ffff00009083f860 = plVar9;
    if (*plVar9 == -0x6b97daaf22da1e10) {
      uVar15 = *(uint *)((long)plVar9 + 0x1c);
      if (2 < uVar15) {
        pcVar13 = s__s__d___boot_fail__WriteUefiLog___ffff0000907e0c98;
        uVar14 = 0x169;
        goto LAB_ffff000090715a1c;
      }
    }
    else {
      FUN_ffff00009077e11c
                (s__s__d___boot_fail_WriteUefiLog___ffff0000907fce02,s_WritePLLKLog_ffff0000907edc7a
                 ,0x163);
      plVar9 = DAT_ffff00009083f860;
      uVar15 = 0;
      *DAT_ffff00009083f860 = -0x6b97daaf22da1e10;
      plVar9[1] = 0x100000001000;
      plVar9[2] = 0x300000000;
      plVar9[3] = 0;
    }
    lVar10 = (ulong)*(uint *)((long)DAT_ffff00009083f848 + 0x2c) + (ulong)(uVar15 * 0x41000) +
             *(long *)((long)DAT_ffff00009083f850 + 0x14);
    uVar2 = lVar10 + 0x1000;
    FUN_ffff00009077e11c
              (s__s__d___boot_fail__WriteUefiLog___ffff0000907d3fc5,s_WritePLLKLog_ffff0000907edc7a,
               0x16f,uVar2);
    iVar4 = FUN_ffff000090714f54(uVar2 & 0xffffffff,0x41000);
    if (iVar4 != 0) {
      pcVar13 = s__s__d___boot_fail_WriteUefiLog___ffff0000907f4264;
      uVar14 = 0x172;
      goto LAB_ffff000090715a1c;
    }
    uVar12 = FUN_ffff000090714eb4(uVar2,0x1000,&local_68);
    if ((uVar12 & 1) == 0) {
      pcVar13 = s__s__d___boot_fail_WriteUefiLog___ffff0000907d7567;
      uVar14 = 0x178;
      goto LAB_ffff000090715a1c;
    }
    DAT_ffff00009083f868 = local_68;
    plVar9 = local_68;
    if (*local_68 != 0x768b480dd48a6592) {
      FUN_ffff00009077e11c
                (s__s__d___boot_fail__WriteUefiLog___ffff0000907f42ab,
                 s_WritePLLKLog_ffff0000907edc7a,0x17e);
      plVar9 = DAT_ffff00009083f868;
      *DAT_ffff00009083f868 = 0x768b480dd48a6592;
      plVar9[1] = 0x100000001000;
      *(undefined4 *)(plVar9 + 2) = 0;
    }
    *(int *)(plVar9 + 2) = (int)DAT_ffff00009083f848[2];
    FUN_ffff00009077eb20(lVar8,plVar9,0x14);
    FUN_ffff000090714e84(lVar8);
    iVar4 = FUN_ffff0000907156ac(uVar2,0x1000,lVar8);
    if (iVar4 != 0) {
      pcVar13 = s__s__d___boot_fail_WriteUefiLog___ffff0000907f42f2;
      uVar14 = 0x188;
      goto LAB_ffff000090715a1c;
    }
    iVar5 = FUN_ffff0000907156ac(lVar10 + 0x2000,iVar5,lVar7);
    plVar9 = DAT_ffff00009083f860;
    if (iVar5 != 0) {
      pcVar13 = s__s__d___boot_fail_WriteUefiLog___ffff0000908020ec;
      uVar14 = 0x191;
      goto LAB_ffff000090715a1c;
    }
    puVar1 = (uint *)(DAT_ffff00009083f860 + 3);
    *(int *)(DAT_ffff00009083f860 + 2) = (int)DAT_ffff00009083f848[2];
    if (*puVar1 < 3) {
      *(uint *)(plVar9 + 3) = *puVar1 + 1;
    }
    iVar5 = 0;
    if (*(uint *)((long)plVar9 + 0x1c) < 2) {
      iVar5 = *(uint *)((long)plVar9 + 0x1c) + 1;
    }
    *(int *)((long)plVar9 + 0x1c) = iVar5;
    FUN_ffff00009077eb20(lVar6,plVar9,0x20);
    FUN_ffff000090714e34(lVar6);
    iVar5 = FUN_ffff0000907156ac
                      (*(long *)((long)DAT_ffff00009083f850 + 0x14) +
                       (ulong)*(uint *)((long)DAT_ffff00009083f848 + 0x2c),0x1000,lVar6);
    if (iVar5 != 0) {
      pcVar13 = s__s__d___boot_fail_WriteUefiLog___ffff0000907f42f2;
      uVar14 = 0x1a4;
      goto LAB_ffff000090715a1c;
    }
    DAT_ffff00009083f848 = (long *)0x0;
    DAT_ffff00009083f850 = (long *)0x0;
    DAT_ffff00009083f860 = (long *)0x0;
    DAT_ffff00009083f858 = 0;
    DAT_ffff00009083f868 = (long *)0x0;
  }
  thunk_FUN_ffff0000907c314c(lVar8);
LAB_ffff000090715a28:
  thunk_FUN_ffff0000907c314c(lVar6);
  return;
}


