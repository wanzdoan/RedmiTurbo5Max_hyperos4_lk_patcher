
undefined8 FUN_ffff000090715d30(long *param_1,undefined4 *param_2)

{
  undefined4 uVar1;
  long *plVar2;
  long lVar3;
  long lVar4;
  char *pcVar5;
  undefined8 uVar6;
  
  plVar2 = (long *)FUN_ffff00009077d514(0x1000);
  if (plVar2 == (long *)0x0) {
    pcVar5 = s__blackbox__PInfoBootFail__Alloc_h_ffff0000907da6c4;
  }
  else {
    lVar3 = FUN_ffff00009077c028(s_blackbox_ffff0000907f0d8c);
    if (lVar3 == 0) {
      pcVar5 = s__blackbox__PInfoBootFail__partit_ffff0000907fcd61;
    }
    else {
      lVar3 = FUN_ffff00009077c0bc(lVar3,plVar2,0x400,0x1000);
      if (0 < lVar3) {
        DAT_ffff00009083f848 = plVar2;
        FUN_ffff00009077e11c
                  (s__s__d___boot_fail__LogTableHeade_ffff0000907e0c3e,
                   s_PrintLogTableHeaderBootFail_ffff0000907d272d,0x5f,*plVar2,(int)plVar2[1],
                   *(undefined4 *)((long)plVar2 + 0xc),*(undefined4 *)((long)plVar2 + 0x2c));
        if (*DAT_ffff00009083f848 == 0x56775af41bcde0f3) {
          uVar1 = *(undefined4 *)((long)DAT_ffff00009083f848 + 0x2c);
          plVar2 = (long *)FUN_ffff00009077d514(0x1000);
          if (plVar2 == (long *)0x0) {
            pcVar5 = s__blackbox__PInfoBootFail__Alloc_h_ffff0000907da6c4;
          }
          else {
            lVar3 = FUN_ffff00009077c028(s_blackbox_ffff0000907f0d8c);
            if (lVar3 == 0) {
              pcVar5 = s__blackbox__PInfoBootFail__partit_ffff0000907fcd61;
            }
            else {
              lVar3 = FUN_ffff00009077c0bc(lVar3,plVar2,uVar1,0x1000);
              if (0 < lVar3) {
                DAT_ffff00009083f850 = plVar2;
                if (*plVar2 == 0x627759541bcde0f0) {
                  FUN_ffff00009077e11c
                            (s__s__d___boot_fail__readlen_is_0x_ffff0000907ec04b,
                             s_GetBootFailPartition_ffff0000907f0f17,0x287,0xa00000);
                  uVar1 = *(undefined4 *)((long)DAT_ffff00009083f848 + 0x2c);
                  lVar3 = FUN_ffff00009077d514(0xa00000);
                  if (lVar3 == 0) {
                    pcVar5 = s__blackbox__PInfoBootFail__Alloc_h_ffff0000907da6c4;
                  }
                  else {
                    lVar4 = FUN_ffff00009077c028(s_blackbox_ffff0000907f0d8c);
                    if (lVar4 == 0) {
                      pcVar5 = s__blackbox__PInfoBootFail__partit_ffff0000907fcd61;
                    }
                    else {
                      lVar4 = FUN_ffff00009077c0bc(lVar4,lVar3,uVar1,0xa00000);
                      if (0 < lVar4) {
                        FUN_ffff00009077e11c
                                  (s__s__d___boot_fail__readlen_is__x_ffff0000907d2749,
                                   s_GetBootFailPartition_ffff0000907f0f17,0x291,0xa00000);
                        FUN_ffff00009077e11c
                                  (s__s__d___boot_fail__header_buffer_ffff0000907f27ba,
                                   s_GetBootFailPartition_ffff0000907f0f17,0x292,lVar3);
                        *param_1 = lVar3;
                        *param_2 = 0xa00000;
                        return 0;
                      }
                      pcVar5 = s__blackbox__PInfoBootFail__partit_ffff0000907f94e0;
                    }
                  }
                  FUN_ffff00009077e11c(pcVar5);
                  FUN_ffff00009077e11c
                            (s__s__d___boot_fail__get_aftersale_ffff0000907f7d28,
                             s_GetBootFailPartition_ffff0000907f0f17,0x28c);
                  return 1;
                }
                pcVar5 = s__s__d___boot_fail__record_error__ffff0000907d59bc;
                uVar6 = 0x281;
                goto LAB_ffff000090715f14;
              }
              pcVar5 = s__blackbox__PInfoBootFail__partit_ffff0000907f94e0;
            }
          }
          FUN_ffff00009077e11c(pcVar5);
          pcVar5 = s__s__d___boot_fail__get_bootfail___ffff0000907da6fa;
          uVar6 = 0x27b;
        }
        else {
          pcVar5 = s__s__d___boot_fail__blackbox_log__ffff0000907e4426;
          uVar6 = 0x272;
        }
        goto LAB_ffff000090715f14;
      }
      pcVar5 = s__blackbox__PInfoBootFail__partit_ffff0000907f94e0;
    }
  }
  FUN_ffff00009077e11c(pcVar5);
  pcVar5 = s__s__d___boot_fail__get_blackbox_p_ffff0000907f958d;
  uVar6 = 0x26b;
LAB_ffff000090715f14:
  FUN_ffff00009077e11c(pcVar5,s_GetBootFailPartition_ffff0000907f0f17,uVar6);
  return 3;
}


