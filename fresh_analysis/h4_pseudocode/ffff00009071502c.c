
undefined8 FUN_ffff00009071502c(uint param_1)

{
  undefined4 uVar1;
  uint uVar2;
  int iVar3;
  long lVar4;
  long lVar5;
  long *plVar6;
  long lVar7;
  ulong uVar8;
  char *pcVar9;
  undefined8 uVar10;
  long *plVar11;
  ulong uVar12;
  ulong uVar13;
  char local_120 [8];
  undefined8 uStack_118;
  undefined8 local_110;
  undefined8 uStack_108;
  undefined8 local_100;
  undefined8 uStack_f8;
  undefined8 local_f0;
  undefined8 uStack_e8;
  undefined8 local_e0;
  undefined8 uStack_d8;
  undefined8 local_d0;
  undefined8 uStack_c8;
  undefined8 local_c0;
  undefined8 uStack_b8;
  undefined8 local_b0;
  undefined8 uStack_a8;
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  long *local_48;
  
  local_48 = (long *)0x0;
  local_70 = 0;
  uStack_68 = 0;
  local_80 = 0;
  uStack_78 = 0;
  local_90 = 0;
  uStack_88 = 0;
  local_a0 = 0;
  uStack_98 = 0;
  local_b0 = 0;
  uStack_a8 = 0;
  local_c0 = 0;
  uStack_b8 = 0;
  local_d0 = 0;
  uStack_c8 = 0;
  local_e0 = 0;
  uStack_d8 = 0;
  local_f0 = 0;
  uStack_e8 = 0;
  local_100 = 0;
  uStack_f8 = 0;
  local_110 = 0;
  uStack_108 = 0;
  local_120 = (char  [8])s_ABCDEFGHIJKLMNOPQRSTUVWXYZ456789_ffff000090803f50._32_8_;
  uStack_118 = 0x3a6d;
  FUN_ffff00009077ee50(&local_e0,0,0x80);
  if (0x1d < param_1) {
    pcVar9 = s__s__d___boot_fail_WriteErrorType_ffff0000907e43da;
    uVar10 = 0x1cf;
LAB_ffff0000907151a4:
    FUN_ffff00009077e11c(pcVar9,s_WriteErrorType_ffff0000907f5ddd,uVar10);
    return 4;
  }
  FUN_ffff00009077e11c
            (s__s__d___boot_fail_WriteErrorType_ffff0000907f0eb5,s_WriteErrorType_ffff0000907f5ddd,
             0x1d4,param_1);
  lVar4 = FUN_ffff00009077d514(0x1000);
  if (lVar4 == 0) {
    pcVar9 = s__s__d___boot_fail_WriteErrorType_ffff0000908005e9;
    uVar10 = 0x1d9;
    goto LAB_ffff0000907151a4;
  }
  FUN_ffff00009077ee50(lVar4,0,0x1000);
  lVar5 = FUN_ffff00009077d514(0x1000);
  if (lVar5 == 0) {
    FUN_ffff00009077e11c
              (s__s__d___boot_fail_WriteErrorType_ffff0000907e8eb5,s_WriteErrorType_ffff0000907f5ddd
               ,0x1e1);
    uVar10 = 4;
    goto LAB_ffff000090715218;
  }
  FUN_ffff00009077ee50(lVar5,0,0x1000);
  plVar6 = (long *)FUN_ffff00009077d514(0x1000);
  if (plVar6 == (long *)0x0) {
    pcVar9 = s__blackbox__PInfoBootFail__Alloc_h_ffff0000907da6c4;
LAB_ffff0000907151f0:
    FUN_ffff00009077e11c(pcVar9);
    pcVar9 = s__s__d___boot_fail_WriteErrorType_ffff0000907edc36;
    uVar10 = 0x1ed;
LAB_ffff000090715208:
    FUN_ffff00009077e11c(pcVar9,s_WriteErrorType_ffff0000907f5ddd,uVar10);
    uVar10 = 4;
  }
  else {
    lVar7 = FUN_ffff00009077c028(s_blackbox_ffff0000907f0d8c);
    if (lVar7 == 0) {
      pcVar9 = s__blackbox__PInfoBootFail__partit_ffff0000907fcd61;
      goto LAB_ffff0000907151f0;
    }
    lVar7 = FUN_ffff00009077c0bc(lVar7,plVar6,0x400,0x1000);
    if (lVar7 < 1) {
      pcVar9 = s__blackbox__PInfoBootFail__partit_ffff0000907f94e0;
      goto LAB_ffff0000907151f0;
    }
    DAT_ffff00009083f848 = plVar6;
    if (*plVar6 == 0x56775af41bcde0f3) {
      plVar11 = plVar6;
      if (*(int *)((long)plVar6 + 0x2c) != 0x9a00000) goto LAB_ffff000090715284;
    }
    else {
      FUN_ffff00009077e11c
                (s__s__d___boot_fail_WriteErrorType_ffff0000907d596d,
                 s_WriteErrorType_ffff0000907f5ddd,499);
      plVar11 = DAT_ffff00009083f848;
      *(undefined4 *)((long)DAT_ffff00009083f848 + 0x1c) = 0;
      *plVar11 = 0x56775af41bcde0f3;
      plVar11[1] = 0x40000001000;
      plVar11[2] = 0x40000000000;
      *(undefined4 *)((long)plVar11 + 0x24) = 0x9800000;
LAB_ffff000090715284:
      *(undefined4 *)((long)plVar11 + 0x2c) = 0x9a00000;
    }
    lVar7 = FUN_ffff00009077c028(s_blackbox_ffff0000907f0d8c);
    if (lVar7 != 0) {
      lVar7 = FUN_ffff00009077c144(lVar7,plVar6,0x400,0x1000);
      if (lVar7 < 1) {
        pcVar9 = s__blackbox__WritePartitionInfo__p_ffff0000907fcdcd;
        goto LAB_ffff000090715394;
      }
      FUN_ffff00009077e11c
                (s__s__d___boot_fail__LogTableHeade_ffff0000907e0c3e,
                 s_PrintLogTableHeaderBootFail_ffff0000907d272d,0x5f,*plVar6,(int)plVar6[1],
                 *(undefined4 *)((long)plVar6 + 0xc),*(undefined4 *)((long)plVar6 + 0x2c));
      uVar1 = *(undefined4 *)((long)DAT_ffff00009083f848 + 0x2c);
      plVar6 = (long *)FUN_ffff00009077d514(0x1000);
      if (plVar6 != (long *)0x0) {
        lVar7 = FUN_ffff00009077c028(s_blackbox_ffff0000907f0d8c);
        if (lVar7 == 0) {
          pcVar9 = s__blackbox__PInfoBootFail__partit_ffff0000907fcd61;
          goto LAB_ffff0000907153d8;
        }
        lVar7 = FUN_ffff00009077c0bc(lVar7,plVar6,uVar1,0x1000);
        if (lVar7 < 1) {
          pcVar9 = s__blackbox__PInfoBootFail__partit_ffff0000907f94e0;
          goto LAB_ffff0000907153d8;
        }
        DAT_ffff00009083f850 = plVar6;
        if (*plVar6 != 0x627759541bcde0f0) {
          FUN_ffff00009077e11c
                    (s__s__d___boot_fail_WriteErrorType_ffff0000907dc237,
                     s_WriteErrorType_ffff0000907f5ddd,0x20d);
          iVar3 = FUN_ffff000090714f54(*(undefined4 *)((long)DAT_ffff00009083f848 + 0x2c),0xa00000);
          if (iVar3 == 0) {
            FUN_ffff000090714cf0(DAT_ffff00009083f850);
            goto LAB_ffff000090715400;
          }
          pcVar9 = s__s__d___boot_fail_WriteErrorType_ffff0000907f9543;
          uVar10 = 0x210;
          goto LAB_ffff0000907153ac;
        }
LAB_ffff000090715400:
        plVar11 = DAT_ffff00009083f850;
        plVar6 = DAT_ffff00009083f848;
        lVar7 = (long)DAT_ffff00009083f850 + 0xac;
        *(undefined4 *)((long)DAT_ffff00009083f850 + 0x1c) = 0x1d;
        *(int *)(plVar11 + 2) = (int)plVar6[2];
        uVar13 = (ulong)*(uint *)(&DAT_ffff0000908044dc + (ulong)param_1 * 4);
        *(ulong *)((long)plVar11 + 0x24) = uVar13;
        FUN_ffff00009077eb20(lVar7,&DAT_ffff0000907fccd1,4);
        FUN_ffff00009077eb20
                  ((long)DAT_ffff00009083f850 + 0x2c,s_boot_error_for_hardware_ffff0000907d8dd3,0x19
                  );
        FUN_ffff00009077eb20(lVar4,DAT_ffff00009083f850,0xbc);
        iVar3 = FUN_ffff0000907156ac
                          (*(undefined4 *)((long)DAT_ffff00009083f848 + 0x2c),0x1000,lVar4);
        if (iVar3 != 0) {
          pcVar9 = s__s__d___boot_fail_WriteErrorType_ffff0000907ddaf5;
          uVar10 = 0x220;
          goto LAB_ffff0000907153ac;
        }
        FUN_ffff000090714d6c(lVar4);
        uVar2 = *(int *)((long)DAT_ffff00009083f850 + 0xc) +
                (int)DAT_ffff00009083f850[4] * (param_1 + 0x1d);
        uVar12 = (ulong)uVar2;
        FUN_ffff00009077e11c
                  (s__s__d___boot_fail_WriteErrorType_ffff0000907ef4a9,
                   s_WriteErrorType_ffff0000907f5ddd,0x227,uVar12);
        if (0xff000 < uVar2) {
          pcVar9 = s__s__d___boot_fail_WriteErrorType_ffff0000907e5cc2;
          uVar10 = 0x229;
          goto LAB_ffff0000907153ac;
        }
        uVar8 = FUN_ffff000090714eb4
                          (*(uint *)((long)DAT_ffff00009083f848 + 0x2c) + uVar12,0x1000,&local_48);
        if ((uVar8 & 1) == 0) {
          pcVar9 = s__s__d___boot_fail_WriteErrorType_ffff0000907ec002;
          uVar10 = 0x231;
          goto LAB_ffff000090715208;
        }
        DAT_ffff00009083f858 = local_48;
        if (*local_48 == -0x16644b7f22da1e10) {
          iVar3 = *(int *)((long)local_48 + 0x1c) + 1;
          plVar6 = local_48;
        }
        else {
          FUN_ffff00009077e11c
                    (s__s__d___boot_fail_WriteErrorType_ffff0000907e26ec,
                     s_WriteErrorType_ffff0000907f5ddd,0x237);
          plVar6 = DAT_ffff00009083f858;
          *DAT_ffff00009083f858 = -0x16644b7f22da1e10;
          plVar6[1] = 0x100000001000;
          iVar3 = 1;
          *(undefined4 *)(plVar6 + 2) = 0;
          *(undefined8 *)((long)plVar6 + 0x14) = 0;
          *(undefined4 *)((long)plVar6 + 0x1c) = 0;
          *(undefined1 *)(plVar6 + 4) = 0;
        }
        plVar11 = DAT_ffff00009083f848;
        *(ulong *)((long)plVar6 + 0x14) = uVar13;
        *(int *)((long)plVar6 + 0x1c) = iVar3;
        *(int *)(plVar6 + 2) = (int)plVar11[2];
        *(undefined1 *)(plVar6 + 4) = 1;
        FUN_ffff00009077f6fc(&local_100,0x20,&DAT_ffff000090801edd,uVar13);
        FUN_ffff00009077e11c
                  (s__s__d___boot_fail_WriteErrorType_ffff0000907d8dec,
                   s_WriteErrorType_ffff0000907f5ddd,0x240,&local_100);
        FUN_ffff00009077f270(&local_e0,local_120,0x20);
        FUN_ffff00009077f270(&local_e0,&local_100,0x20);
        FUN_ffff00009077eb20((long)DAT_ffff00009083f858 + 0x21,&local_e0,0x80);
        FUN_ffff00009077e11c
                  (s__s__d___boot_fail_WriteErrorType_ffff0000907f0eea,
                   s_WriteErrorType_ffff0000907f5ddd,0x244,&local_e0);
        FUN_ffff00009077eb20(lVar5,DAT_ffff00009083f858,0xa1);
        iVar3 = FUN_ffff0000907156ac
                          (*(uint *)((long)DAT_ffff00009083f848 + 0x2c) + uVar12,0x1000,lVar5);
        if (iVar3 != 0) {
          pcVar9 = s__s__d___boot_fail_WriteErrorType_ffff0000907fcd90;
          uVar10 = 0x249;
          goto LAB_ffff0000907153ac;
        }
        FUN_ffff000090714dd4(lVar5);
        FUN_ffff00009077ee50(&local_e0,0,0x80);
        DAT_ffff00009083f848 = (long *)0x0;
        DAT_ffff00009083f850 = (long *)0x0;
        DAT_ffff00009083f858 = (long *)0x0;
        FUN_ffff00009071572c();
        uVar10 = 0;
        goto LAB_ffff000090715210;
      }
      pcVar9 = s__blackbox__PInfoBootFail__Alloc_h_ffff0000907da6c4;
LAB_ffff0000907153d8:
      FUN_ffff00009077e11c(pcVar9);
      pcVar9 = s__s__d___boot_fail_WriteErrorType_ffff0000907fb2ce;
      uVar10 = 0x207;
      goto LAB_ffff000090715208;
    }
    pcVar9 = s__blackbox__WritePartitionInfo__p_ffff0000907f5dec;
LAB_ffff000090715394:
    FUN_ffff00009077e11c(pcVar9);
    pcVar9 = s__s__d___boot_fail_WriteErrorType_ffff0000907e8f08;
    uVar10 = 0x1fe;
LAB_ffff0000907153ac:
    FUN_ffff00009077e11c(pcVar9,s_WriteErrorType_ffff0000907f5ddd,uVar10);
    uVar10 = 1;
  }
LAB_ffff000090715210:
  thunk_FUN_ffff0000907c314c(lVar5);
LAB_ffff000090715218:
  thunk_FUN_ffff0000907c314c(lVar4);
  return uVar10;
}


