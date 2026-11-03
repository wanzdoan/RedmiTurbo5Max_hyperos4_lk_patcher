// ffff000090782bf0  size=668  FUN_ffff000090782bf0

undefined8 FUN_ffff000090782bf0(undefined8 param_1,undefined8 param_2,int param_3)

{
  int iVar1;
  int *piVar2;
  long lVar3;
  char *pcVar4;
  int *piVar5;
  undefined8 uVar6;
  long lVar7;
  
  piVar2 = (int *)FUN_ffff00009077d2e0(1,0x1000);
  if (piVar2 == (int *)0x0) {
    FUN_ffff00009077de54
              (s__s__d___s_malloc_for_mrdump_conf_ffff0000907d3786,s_mrdump_get_env_ffff0000907d01c1
               ,0x39,s_mrdump_get_env_ffff0000907d01c1);
    return 0xfffffff4;
  }
  lVar3 = FUN_ffff00009077bd60(s_expdb_ffff0000907d5730);
  if (lVar3 == 0) {
    FUN_ffff00009077de54
              (s__s__d___s_partition_does_not_exi_ffff0000907e8328,s_mrdump_get_env_ffff0000907d01c1
               ,0x40,s_expdb_ffff0000907d5730);
    thunk_FUN_ffff0000907c2ad8(piVar2);
    return 0xffffffed;
  }
  lVar7 = *(long *)(lVar3 + 0x20);
  iVar1 = FUN_ffff0000907816ec();
  iVar1 = FUN_ffff00009077bdf4(lVar3,piVar2,lVar7 - iVar1,0x1000);
  if (iVar1 != 0x1000) {
    FUN_ffff00009077de54
              (s__s__d___s__bio_read_fail_ffff0000907d5108,s_mrdump_get_env_ffff0000907d01c1,0x4c,
               s_mrdump_get_env_ffff0000907d01c1);
    FUN_ffff00009077bce4(lVar3);
    thunk_FUN_ffff0000907c2ad8(piVar2);
    return 0xfffffffb;
  }
  if (*piVar2 == 0x3043524d) {
    iVar1 = FUN_ffff00009077f080(param_1,s_mrdump_output_ffff0000907debee,0xe);
    if (iVar1 == 0) {
      piVar5 = piVar2 + 2;
    }
    else {
      iVar1 = FUN_ffff00009077f080(param_1,s_mrdump_allocate_size_ffff0000907eb5d9,0x14);
      if (iVar1 == 0) {
        piVar5 = piVar2 + 0x16;
      }
      else {
        iVar1 = FUN_ffff00009077f080(param_1,s_mrdump_mem_size_ffff0000907ed0db,0x10);
        if (iVar1 == 0) {
          piVar5 = piVar2 + 0x12;
        }
        else {
          iVar1 = FUN_ffff00009077f080(param_1,s_mrdump_lbaooo_ffff000090801506,0xe);
          if (iVar1 == 0) {
            piVar5 = piVar2 + 0x1a;
          }
          else {
            iVar1 = FUN_ffff00009077f080(param_1,s_mrdump_dump_type_ffff0000907eebfa,0x11);
            if (iVar1 == 0) {
              piVar5 = piVar2 + 0x20;
            }
            else {
              iVar1 = FUN_ffff00009077f080(param_1,s_mrdump_fb_timeout_ffff0000907e1eba,0x12);
              if (iVar1 == 0) {
                piVar5 = piVar2 + 0x28;
              }
              else {
                iVar1 = FUN_ffff00009077f080
                                  (param_1,s_mrdump_compression_method_ffff0000907f53c3,0x1a);
                if (iVar1 != 0) {
                  pcVar4 = s__s__d__unknown_mrdump_config_ffff0000907debfc;
                  uVar6 = 0x69;
                  goto LAB_ffff000090782de4;
                }
                piVar5 = piVar2 + 0x30;
              }
            }
          }
        }
      }
    }
    FUN_ffff00009077ef1c(param_2,piVar5,(long)param_3);
    FUN_ffff00009077de54
              (s__s__d___s___s_ffff000090801514,s_mrdump_get_env_ffff0000907d01c1,0x6f,param_1,
               param_2);
    FUN_ffff00009077bce4(lVar3);
    thunk_FUN_ffff0000907c2ad8(piVar2);
    uVar6 = 0;
  }
  else {
    FUN_ffff00009077ef1c(param_2,&DAT_ffff0000907df295,(long)param_3);
    pcVar4 = s__s__d__unknown_mrdump_config_sig_ffff0000907f7145;
    uVar6 = 0x54;
LAB_ffff000090782de4:
    FUN_ffff00009077de54(pcVar4,s_mrdump_get_env_ffff0000907d01c1,uVar6);
    FUN_ffff00009077bce4(lVar3);
    thunk_FUN_ffff0000907c2ad8(piVar2);
    uVar6 = 0xffffffff;
  }
  return uVar6;
}


