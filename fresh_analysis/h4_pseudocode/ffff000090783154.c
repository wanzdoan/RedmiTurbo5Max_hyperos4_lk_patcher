
undefined4 FUN_ffff000090783154(undefined8 param_1,undefined8 param_2)

{
  int iVar1;
  int *piVar2;
  long lVar3;
  int *piVar4;
  undefined8 uVar5;
  undefined4 uVar6;
  long lVar7;
  
  piVar2 = (int *)FUN_ffff00009077d514(0x1000);
  if (piVar2 == (int *)0x0) {
    FUN_ffff00009077e11c
              (s__s__d___s_malloc_for_mrdump_conf_ffff0000907d37c3,s_mrdump_set_env_ffff0000907d01ef
               ,0x7f,s_mrdump_set_env_ffff0000907d01ef);
    return 0xfffffff4;
  }
  FUN_ffff00009077ee50(piVar2,0,0x1000);
  lVar3 = FUN_ffff00009077c028(s_expdb_ffff0000907d5780);
  if (lVar3 == 0) {
    FUN_ffff00009077e11c
              (s__s__d___s_partition_does_not_exi_ffff0000907e846c,s_mrdump_set_env_ffff0000907d01ef
               ,0x86,s_expdb_ffff0000907d5780);
    thunk_FUN_ffff0000907c314c(piVar2);
    return 0xffffffed;
  }
  iVar1 = FUN_ffff0000907819b4();
  lVar7 = *(long *)(lVar3 + 0x20) - (long)iVar1;
  iVar1 = FUN_ffff00009077c0bc(lVar3,piVar2,lVar7,0x1000);
  if (iVar1 != 0x1000) {
    FUN_ffff00009077e11c
              (s__s__d___s__bio_read_fail_ffff0000907d5145,s_mrdump_set_env_ffff0000907d01ef,0x90,
               s_mrdump_set_env_ffff0000907d01ef);
    FUN_ffff00009077bfac(lVar3);
    thunk_FUN_ffff0000907c314c(piVar2);
    return 0xfffffffb;
  }
  if (*piVar2 != 0x3043524d) {
    FUN_ffff00009077ee50(piVar2,0,0x1000);
    *piVar2 = 0x3043524d;
  }
  iVar1 = FUN_ffff00009077f348(param_1,s_mrdump_output_ffff0000907dec91,0xe);
  if (iVar1 == 0) {
    piVar4 = piVar2 + 2;
    uVar5 = 0x40;
  }
  else {
    iVar1 = FUN_ffff00009077f348(param_1,s_mrdump_allocate_size_ffff0000907eb75c,0x15);
    if (iVar1 == 0) {
      piVar4 = piVar2 + 0x16;
    }
    else {
      iVar1 = FUN_ffff00009077f348(param_1,s_mrdump_mem_size_ffff0000907ed25e,0x10);
      if (iVar1 != 0) {
        iVar1 = FUN_ffff00009077f348(param_1,s_mrdump_lbaooo_ffff0000908016e2,0xe);
        if (iVar1 == 0) {
          piVar4 = piVar2 + 0x1a;
          uVar5 = 0x18;
        }
        else {
          iVar1 = FUN_ffff00009077f348(param_1,s_mrdump_dump_type_ffff0000907eed6b,0x11);
          if (iVar1 == 0) {
            piVar4 = piVar2 + 0x20;
          }
          else {
            iVar1 = FUN_ffff00009077f348(param_1,s_mrdump_fb_timeout_ffff0000907e1f8b,0x12);
            if (iVar1 != 0) {
              FUN_ffff00009077e11c
                        (s__s__d__unknown_mrdump_config_ffff0000907dec9f,
                         s_mrdump_set_env_ffff0000907d01ef,0xa8);
              FUN_ffff00009077bfac(lVar3);
              thunk_FUN_ffff0000907c314c(piVar2);
              return 0xffffffff;
            }
            piVar4 = piVar2 + 0x28;
          }
          uVar5 = 0x20;
        }
        goto LAB_ffff000090783360;
      }
      piVar4 = piVar2 + 0x12;
    }
    uVar5 = 0x10;
  }
LAB_ffff000090783360:
  FUN_ffff00009077f1e4(piVar4,param_2,uVar5);
  FUN_ffff00009077e11c
            (s__s__d___s_update_to__s_ffff0000907f72f7,s_mrdump_set_env_ffff0000907d01ef,0xad,
             param_1,param_2);
  iVar1 = FUN_ffff00009077c144(lVar3,piVar2,lVar7,0x1000);
  FUN_ffff00009077bfac(lVar3);
  thunk_FUN_ffff0000907c314c(piVar2);
  uVar6 = 0;
  if (iVar1 != 0x1000) {
    uVar6 = 0xfffffffb;
  }
  return uVar6;
}


