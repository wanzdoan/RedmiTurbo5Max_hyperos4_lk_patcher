
undefined8 FUN_ffff000090782eb8(undefined8 param_1,undefined8 param_2,int param_3)

{
  int iVar1;
  int *piVar2;
  long lVar3;
  char *pcVar4;
  int *piVar5;
  undefined8 uVar6;
  long lVar7;
  
  piVar2 = (int *)FUN_ffff00009077d5a8(1,0x1000);
  if (piVar2 == (int *)0x0) {
    FUN_ffff00009077e11c
              (s__s__d___s_malloc_for_mrdump_conf_ffff0000907d37c3,s_mrdump_get_env_ffff0000907d01e0
               ,0x39,s_mrdump_get_env_ffff0000907d01e0);
    return 0xfffffff4;
  }
  lVar3 = FUN_ffff00009077c028(s_expdb_ffff0000907d5780);
  if (lVar3 == 0) {
    FUN_ffff00009077e11c
              (s__s__d___s_partition_does_not_exi_ffff0000907e846c,s_mrdump_get_env_ffff0000907d01e0
               ,0x40,s_expdb_ffff0000907d5780);
    thunk_FUN_ffff0000907c314c(piVar2);
    return 0xffffffed;
  }
  lVar7 = *(long *)(lVar3 + 0x20);
  iVar1 = FUN_ffff0000907819b4();
  iVar1 = FUN_ffff00009077c0bc(lVar3,piVar2,lVar7 - iVar1,0x1000);
  if (iVar1 != 0x1000) {
    FUN_ffff00009077e11c
              (s__s__d___s__bio_read_fail_ffff0000907d5145,s_mrdump_get_env_ffff0000907d01e0,0x4c,
               s_mrdump_get_env_ffff0000907d01e0);
    FUN_ffff00009077bfac(lVar3);
    thunk_FUN_ffff0000907c314c(piVar2);
    return 0xfffffffb;
  }
  if (*piVar2 == 0x3043524d) {
    iVar1 = FUN_ffff00009077f348(param_1,s_mrdump_output_ffff0000907dec91,0xe);
    if (iVar1 == 0) {
      piVar5 = piVar2 + 2;
    }
    else {
      iVar1 = FUN_ffff00009077f348(param_1,s_mrdump_allocate_size_ffff0000907eb75c,0x14);
      if (iVar1 == 0) {
        piVar5 = piVar2 + 0x16;
      }
      else {
        iVar1 = FUN_ffff00009077f348(param_1,s_mrdump_mem_size_ffff0000907ed25e,0x10);
        if (iVar1 == 0) {
          piVar5 = piVar2 + 0x12;
        }
        else {
          iVar1 = FUN_ffff00009077f348(param_1,s_mrdump_lbaooo_ffff0000908016e2,0xe);
          if (iVar1 == 0) {
            piVar5 = piVar2 + 0x1a;
          }
          else {
            iVar1 = FUN_ffff00009077f348(param_1,s_mrdump_dump_type_ffff0000907eed6b,0x11);
            if (iVar1 == 0) {
              piVar5 = piVar2 + 0x20;
            }
            else {
              iVar1 = FUN_ffff00009077f348(param_1,s_mrdump_fb_timeout_ffff0000907e1f8b,0x12);
              if (iVar1 == 0) {
                piVar5 = piVar2 + 0x28;
              }
              else {
                iVar1 = FUN_ffff00009077f348
                                  (param_1,s_mrdump_compression_method_ffff0000907f5530,0x1a);
                if (iVar1 != 0) {
                  pcVar4 = s__s__d__unknown_mrdump_config_ffff0000907dec9f;
                  uVar6 = 0x69;
                  goto LAB_ffff0000907830ac;
                }
                piVar5 = piVar2 + 0x30;
              }
            }
          }
        }
      }
    }
    FUN_ffff00009077f1e4(param_2,piVar5,(long)param_3);
    FUN_ffff00009077e11c
              (s__s__d___s___s_ffff0000908016f0,s_mrdump_get_env_ffff0000907d01e0,0x6f,param_1,
               param_2);
    FUN_ffff00009077bfac(lVar3);
    thunk_FUN_ffff0000907c314c(piVar2);
    uVar6 = 0;
  }
  else {
    FUN_ffff00009077f1e4(param_2,&DAT_ffff0000907df338,(long)param_3);
    pcVar4 = s__s__d__unknown_mrdump_config_sig_ffff0000907f72cf;
    uVar6 = 0x54;
LAB_ffff0000907830ac:
    FUN_ffff00009077e11c(pcVar4,s_mrdump_get_env_ffff0000907d01e0,uVar6);
    FUN_ffff00009077bfac(lVar3);
    thunk_FUN_ffff0000907c314c(piVar2);
    uVar6 = 0xffffffff;
  }
  return uVar6;
}


