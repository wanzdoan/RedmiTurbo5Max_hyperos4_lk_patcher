// ffff000090715bec  size=692  FUN_ffff000090715bec

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090715bec(long *param_1,undefined4 *param_2)

{
  undefined4 uVar1;
  long *plVar2;
  long lVar3;
  long lVar4;
  char *pcVar5;
  undefined8 uVar6;
  
  plVar2 = (long *)FUN_ffff00009077d24c(0x1000);
  if (plVar2 == (long *)0x0) {
    pcVar5 = s__blackbox__PInfoBootFail__Alloc_h_ffff0000907da5a2;
  }
  else {
    lVar3 = FUN_ffff00009077bd60(s_blackbox_ffff0000907f0c04);
    if (lVar3 == 0) {
      pcVar5 = s__blackbox__PInfoBootFail__partit_ffff0000907fcbb9;
    }
    else {
      lVar3 = FUN_ffff00009077bdf4(lVar3,plVar2,0x400,0x1000);
      if (0 < lVar3) {
        _DAT_ffff00009083f810 = plVar2;
        FUN_ffff00009077de54
                  (s__s__d___boot_fail__LogTableHeade_ffff0000907e0b84,
                   s_PrintLogTableHeaderBootFail_ffff0000907d26f0,0x5f,*plVar2,(int)plVar2[1],
                   *(undefined4 *)((long)plVar2 + 0xc),*(undefined4 *)((long)plVar2 + 0x2c));
        if (*_DAT_ffff00009083f810 == 0x56775af41bcde0f3) {
          uVar1 = *(undefined4 *)((long)_DAT_ffff00009083f810 + 0x2c);
          plVar2 = (long *)FUN_ffff00009077d24c(0x1000);
          if (plVar2 == (long *)0x0) {
            pcVar5 = s__blackbox__PInfoBootFail__Alloc_h_ffff0000907da5a2;
          }
          else {
            lVar3 = FUN_ffff00009077bd60(s_blackbox_ffff0000907f0c04);
            if (lVar3 == 0) {
              pcVar5 = s__blackbox__PInfoBootFail__partit_ffff0000907fcbb9;
            }
            else {
              lVar3 = FUN_ffff00009077bdf4(lVar3,plVar2,uVar1,0x1000);
              if (0 < lVar3) {
                _DAT_ffff00009083f818 = plVar2;
                if (*plVar2 == 0x627759541bcde0f0) {
                  FUN_ffff00009077de54
                            (s__s__d___boot_fail__readlen_is_0x_ffff0000907ebeb1,
                             s_GetBootFailPartition_ffff0000907f0d8f,0x287,0xa00000);
                  uVar1 = *(undefined4 *)((long)_DAT_ffff00009083f810 + 0x2c);
                  lVar3 = FUN_ffff00009077d24c(0xa00000);
                  if (lVar3 == 0) {
                    pcVar5 = s__blackbox__PInfoBootFail__Alloc_h_ffff0000907da5a2;
                  }
                  else {
                    lVar4 = FUN_ffff00009077bd60(s_blackbox_ffff0000907f0c04);
                    if (lVar4 == 0) {
                      pcVar5 = s__blackbox__PInfoBootFail__partit_ffff0000907fcbb9;
                    }
                    else {
                      lVar4 = FUN_ffff00009077bdf4(lVar4,lVar3,uVar1,0xa00000);
                      if (0 < lVar4) {
                        FUN_ffff00009077de54
                                  (s__s__d___boot_fail__readlen_is__x_ffff0000907d270c,
                                   s_GetBootFailPartition_ffff0000907f0d8f,0x291,0xa00000);
                        FUN_ffff00009077de54
                                  (s__s__d___boot_fail__header_buffer_ffff0000907f2617,
                                   s_GetBootFailPartition_ffff0000907f0d8f,0x292,lVar3);
                        *param_1 = lVar3;
                        *param_2 = 0xa00000;
                        return 0;
                      }
                      pcVar5 = s__blackbox__PInfoBootFail__partit_ffff0000907f9344;
                    }
                  }
                  FUN_ffff00009077de54(pcVar5);
                  FUN_ffff00009077de54
                            (s__s__d___boot_fail__get_aftersale_ffff0000907f7b8c,
                             s_GetBootFailPartition_ffff0000907f0d8f,0x28c);
                  return 1;
                }
                pcVar5 = s__s__d___boot_fail__record_error__ffff0000907d596c;
                uVar6 = 0x281;
                goto LAB_ffff000090715dd0;
              }
              pcVar5 = s__blackbox__PInfoBootFail__partit_ffff0000907f9344;
            }
          }
          FUN_ffff00009077de54(pcVar5);
          pcVar5 = s__s__d___boot_fail__get_bootfail___ffff0000907da5d8;
          uVar6 = 0x27b;
        }
        else {
          pcVar5 = s__s__d___boot_fail__blackbox_log__ffff0000907e430b;
          uVar6 = 0x272;
        }
        goto LAB_ffff000090715dd0;
      }
      pcVar5 = s__blackbox__PInfoBootFail__partit_ffff0000907f9344;
    }
  }
  FUN_ffff00009077de54(pcVar5);
  pcVar5 = s__s__d___boot_fail__get_blackbox_p_ffff0000907f93f1;
  uVar6 = 0x26b;
LAB_ffff000090715dd0:
  FUN_ffff00009077de54(pcVar5,s_GetBootFailPartition_ffff0000907f0d8f,uVar6);
  return 3;
}


