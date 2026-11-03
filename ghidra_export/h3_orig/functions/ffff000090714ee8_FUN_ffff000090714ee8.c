// ffff000090714ee8  size=1664  FUN_ffff000090714ee8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090714ee8(uint param_1)

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
  local_120 = (char  [8])s_ABCDEFGHIJKLMNOPQRSTUVWXYZ456789_ffff000090803d28._32_8_;
  uStack_118 = 0x3a6d;
  FUN_ffff00009077eb88(&local_e0,0,0x80);
  if (0x1d < param_1) {
    pcVar9 = s__s__d___boot_fail_WriteErrorType_ffff0000907e42d6;
    uVar10 = 0x1cf;
LAB_ffff000090715060:
    FUN_ffff00009077de54(pcVar9,s_WriteErrorType_ffff0000907f5c66,uVar10);
    return 4;
  }
  FUN_ffff00009077de54
            (s__s__d___boot_fail_WriteErrorType_ffff0000907f0d44,s_WriteErrorType_ffff0000907f5c66,
             0x1d4,param_1);
  lVar4 = FUN_ffff00009077d24c(0x1000);
  if (lVar4 == 0) {
    pcVar9 = s__s__d___boot_fail_WriteErrorType_ffff00009080040d;
    uVar10 = 0x1d9;
    goto LAB_ffff000090715060;
  }
  FUN_ffff00009077eb88(lVar4,0,0x1000);
  lVar5 = FUN_ffff00009077d24c(0x1000);
  if (lVar5 == 0) {
    FUN_ffff00009077de54
              (s__s__d___boot_fail_WriteErrorType_ffff0000907e8d71,s_WriteErrorType_ffff0000907f5c66
               ,0x1e1);
    uVar10 = 4;
    goto LAB_ffff0000907150d4;
  }
  FUN_ffff00009077eb88(lVar5,0,0x1000);
  plVar6 = (long *)FUN_ffff00009077d24c(0x1000);
  if (plVar6 == (long *)0x0) {
    pcVar9 = s__blackbox__PInfoBootFail__Alloc_h_ffff0000907da5b9;
LAB_ffff0000907150ac:
    FUN_ffff00009077de54(pcVar9);
    pcVar9 = s__s__d___boot_fail_WriteErrorType_ffff0000907edac5;
    uVar10 = 0x1ed;
LAB_ffff0000907150c4:
    FUN_ffff00009077de54(pcVar9,s_WriteErrorType_ffff0000907f5c66,uVar10);
    uVar10 = 4;
  }
  else {
    lVar7 = FUN_ffff00009077bd60(s_blackbox_ffff0000907f0c1b);
    if (lVar7 == 0) {
      pcVar9 = s__blackbox__PInfoBootFail__partit_ffff0000907fcbb9;
      goto LAB_ffff0000907150ac;
    }
    lVar7 = FUN_ffff00009077bdf4(lVar7,plVar6,0x400,0x1000);
    if (lVar7 < 1) {
      pcVar9 = s__blackbox__PInfoBootFail__partit_ffff0000907f9344;
      goto LAB_ffff0000907150ac;
    }
    _DAT_ffff00009083f810 = plVar6;
    if (*plVar6 == 0x56775af41bcde0f3) {
      plVar11 = plVar6;
      if (*(int *)((long)plVar6 + 0x2c) != 0x9a00000) goto LAB_ffff000090715140;
    }
    else {
      FUN_ffff00009077de54
                (s__s__d___boot_fail_WriteErrorType_ffff0000907d591d,
                 s_WriteErrorType_ffff0000907f5c66,499);
      plVar11 = _DAT_ffff00009083f810;
      *(undefined4 *)((long)_DAT_ffff00009083f810 + 0x1c) = 0;
      *plVar11 = 0x56775af41bcde0f3;
      plVar11[1] = 0x40000001000;
      plVar11[2] = 0x40000000000;
      *(undefined4 *)((long)plVar11 + 0x24) = 0x9800000;
LAB_ffff000090715140:
      *(undefined4 *)((long)plVar11 + 0x2c) = 0x9a00000;
    }
    lVar7 = FUN_ffff00009077bd60(s_blackbox_ffff0000907f0c1b);
    if (lVar7 != 0) {
      lVar7 = FUN_ffff00009077be7c(lVar7,plVar6,0x400,0x1000);
      if (lVar7 < 1) {
        pcVar9 = s__blackbox__WritePartitionInfo__p_ffff0000907fcc25;
        goto LAB_ffff000090715250;
      }
      FUN_ffff00009077de54
                (s__s__d___boot_fail__LogTableHeade_ffff0000907e0b9b,
                 s_PrintLogTableHeaderBootFail_ffff0000907d26f0,0x5f,*plVar6,(int)plVar6[1],
                 *(undefined4 *)((long)plVar6 + 0xc),*(undefined4 *)((long)plVar6 + 0x2c));
      uVar1 = *(undefined4 *)((long)_DAT_ffff00009083f810 + 0x2c);
      plVar6 = (long *)FUN_ffff00009077d24c(0x1000);
      if (plVar6 != (long *)0x0) {
        lVar7 = FUN_ffff00009077bd60(s_blackbox_ffff0000907f0c1b);
        if (lVar7 == 0) {
          pcVar9 = s__blackbox__PInfoBootFail__partit_ffff0000907fcbb9;
          goto LAB_ffff000090715294;
        }
        lVar7 = FUN_ffff00009077bdf4(lVar7,plVar6,uVar1,0x1000);
        if (lVar7 < 1) {
          pcVar9 = s__blackbox__PInfoBootFail__partit_ffff0000907f9344;
          goto LAB_ffff000090715294;
        }
        _DAT_ffff00009083f818 = plVar6;
        if (*plVar6 != 0x627759541bcde0f0) {
          FUN_ffff00009077de54
                    (s__s__d___boot_fail_WriteErrorType_ffff0000907dc161,
                     s_WriteErrorType_ffff0000907f5c66,0x20d);
          iVar3 = FUN_ffff000090714e10(*(undefined4 *)((long)_DAT_ffff00009083f810 + 0x2c),0xa00000)
          ;
          if (iVar3 == 0) {
            FUN_ffff000090714bac(_DAT_ffff00009083f818);
            goto LAB_ffff0000907152bc;
          }
          pcVar9 = s__s__d___boot_fail_WriteErrorType_ffff0000907f93a7;
          uVar10 = 0x210;
          goto LAB_ffff000090715268;
        }
LAB_ffff0000907152bc:
        plVar11 = _DAT_ffff00009083f818;
        plVar6 = _DAT_ffff00009083f810;
        lVar7 = (long)_DAT_ffff00009083f818 + 0xac;
        *(undefined4 *)((long)_DAT_ffff00009083f818 + 0x1c) = 0x1d;
        *(int *)(plVar11 + 2) = (int)plVar6[2];
        uVar13 = (ulong)*(uint *)(&DAT_ffff0000908042b4 + (ulong)param_1 * 4);
        *(ulong *)((long)plVar11 + 0x24) = uVar13;
        FUN_ffff00009077e858(lVar7,&DAT_ffff0000907fcb29,4);
        FUN_ffff00009077e858
                  ((long)_DAT_ffff00009083f818 + 0x2c,s_boot_error_for_hardware_ffff0000907d8d3c,
                   0x19);
        FUN_ffff00009077e858(lVar4,_DAT_ffff00009083f818,0xbc);
        iVar3 = FUN_ffff000090715568
                          (*(undefined4 *)((long)_DAT_ffff00009083f810 + 0x2c),0x1000,lVar4);
        if (iVar3 != 0) {
          pcVar9 = s__s__d___boot_fail_WriteErrorType_ffff0000907dda52;
          uVar10 = 0x220;
          goto LAB_ffff000090715268;
        }
        FUN_ffff000090714c28(lVar4);
        uVar2 = *(int *)((long)_DAT_ffff00009083f818 + 0xc) +
                (int)_DAT_ffff00009083f818[4] * (param_1 + 0x1d);
        uVar12 = (ulong)uVar2;
        FUN_ffff00009077de54
                  (s__s__d___boot_fail_WriteErrorType_ffff0000907ef338,
                   s_WriteErrorType_ffff0000907f5c66,0x227,uVar12);
        if (0xff000 < uVar2) {
          pcVar9 = s__s__d___boot_fail_WriteErrorType_ffff0000907e5bae;
          uVar10 = 0x229;
          goto LAB_ffff000090715268;
        }
        uVar8 = FUN_ffff000090714d70
                          (*(uint *)((long)_DAT_ffff00009083f810 + 0x2c) + uVar12,0x1000,&local_48);
        if ((uVar8 & 1) == 0) {
          pcVar9 = s__s__d___boot_fail_WriteErrorType_ffff0000907ebe7f;
          uVar10 = 0x231;
          goto LAB_ffff0000907150c4;
        }
        _DAT_ffff00009083f820 = local_48;
        if (*local_48 == -0x16644b7f22da1e10) {
          iVar3 = *(int *)((long)local_48 + 0x1c) + 1;
          plVar6 = local_48;
        }
        else {
          FUN_ffff00009077de54
                    (s__s__d___boot_fail_WriteErrorType_ffff0000907e261b,
                     s_WriteErrorType_ffff0000907f5c66,0x237);
          plVar6 = _DAT_ffff00009083f820;
          *_DAT_ffff00009083f820 = -0x16644b7f22da1e10;
          plVar6[1] = 0x100000001000;
          iVar3 = 1;
          *(undefined4 *)(plVar6 + 2) = 0;
          *(undefined8 *)((long)plVar6 + 0x14) = 0;
          *(undefined4 *)((long)plVar6 + 0x1c) = 0;
          *(undefined1 *)(plVar6 + 4) = 0;
        }
        plVar11 = _DAT_ffff00009083f810;
        *(ulong *)((long)plVar6 + 0x14) = uVar13;
        *(int *)((long)plVar6 + 0x1c) = iVar3;
        *(int *)(plVar6 + 2) = (int)plVar11[2];
        *(undefined1 *)(plVar6 + 4) = 1;
        FUN_ffff00009077f434(&local_100,0x20,&DAT_ffff000090801d01,uVar13);
        FUN_ffff00009077de54
                  (s__s__d___boot_fail_WriteErrorType_ffff0000907d8d55,
                   s_WriteErrorType_ffff0000907f5c66,0x240,&local_100);
        FUN_ffff00009077efa8(&local_e0,local_120,0x20);
        FUN_ffff00009077efa8(&local_e0,&local_100,0x20);
        FUN_ffff00009077e858((long)_DAT_ffff00009083f820 + 0x21,&local_e0,0x80);
        FUN_ffff00009077de54
                  (s__s__d___boot_fail_WriteErrorType_ffff0000907f0d79,
                   s_WriteErrorType_ffff0000907f5c66,0x244,&local_e0);
        FUN_ffff00009077e858(lVar5,_DAT_ffff00009083f820,0xa1);
        iVar3 = FUN_ffff000090715568
                          (*(uint *)((long)_DAT_ffff00009083f810 + 0x2c) + uVar12,0x1000,lVar5);
        if (iVar3 != 0) {
          pcVar9 = s__s__d___boot_fail_WriteErrorType_ffff0000907fcbe8;
          uVar10 = 0x249;
          goto LAB_ffff000090715268;
        }
        FUN_ffff000090714c90(lVar5);
        FUN_ffff00009077eb88(&local_e0,0,0x80);
        _DAT_ffff00009083f810 = (long *)0x0;
        _DAT_ffff00009083f818 = (long *)0x0;
        _DAT_ffff00009083f820 = (long *)0x0;
        FUN_ffff0000907155e8();
        uVar10 = 0;
        goto LAB_ffff0000907150cc;
      }
      pcVar9 = s__blackbox__PInfoBootFail__Alloc_h_ffff0000907da5b9;
LAB_ffff000090715294:
      FUN_ffff00009077de54(pcVar9);
      pcVar9 = s__s__d___boot_fail_WriteErrorType_ffff0000907fb132;
      uVar10 = 0x207;
      goto LAB_ffff0000907150c4;
    }
    pcVar9 = s__blackbox__WritePartitionInfo__p_ffff0000907f5c75;
LAB_ffff000090715250:
    FUN_ffff00009077de54(pcVar9);
    pcVar9 = s__s__d___boot_fail_WriteErrorType_ffff0000907e8dc4;
    uVar10 = 0x1fe;
LAB_ffff000090715268:
    FUN_ffff00009077de54(pcVar9,s_WriteErrorType_ffff0000907f5c66,uVar10);
    uVar10 = 1;
  }
LAB_ffff0000907150cc:
  thunk_FUN_ffff0000907c2ad8(lVar5);
LAB_ffff0000907150d4:
  thunk_FUN_ffff0000907c2ad8(lVar4);
  return uVar10;
}


