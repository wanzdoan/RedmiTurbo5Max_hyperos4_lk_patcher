// ffff000090782e8c  size=676  FUN_ffff000090782e8c

undefined4 FUN_ffff000090782e8c(undefined8 param_1,undefined8 param_2)

{
  int iVar1;
  int *piVar2;
  long lVar3;
  int *piVar4;
  undefined8 uVar5;
  undefined4 uVar6;
  long lVar7;
  
  piVar2 = (int *)FUN_ffff00009077d24c(0x1000);
  if (piVar2 == (int *)0x0) {
    FUN_ffff00009077de54
              (s__s__d___s_malloc_for_mrdump_conf_ffff0000907d3786,s_mrdump_set_env_ffff0000907d01d0
               ,0x7f,s_mrdump_set_env_ffff0000907d01d0);
    return 0xfffffff4;
  }
  FUN_ffff00009077eb88(piVar2,0,0x1000);
  lVar3 = FUN_ffff00009077bd60(s_expdb_ffff0000907d5730);
  if (lVar3 == 0) {
    FUN_ffff00009077de54
              (s__s__d___s_partition_does_not_exi_ffff0000907e8328,s_mrdump_set_env_ffff0000907d01d0
               ,0x86,s_expdb_ffff0000907d5730);
    thunk_FUN_ffff0000907c2ad8(piVar2);
    return 0xffffffed;
  }
  iVar1 = FUN_ffff0000907816ec();
  lVar7 = *(long *)(lVar3 + 0x20) - (long)iVar1;
  iVar1 = FUN_ffff00009077bdf4(lVar3,piVar2,lVar7,0x1000);
  if (iVar1 != 0x1000) {
    FUN_ffff00009077de54
              (s__s__d___s__bio_read_fail_ffff0000907d5108,s_mrdump_set_env_ffff0000907d01d0,0x90,
               s_mrdump_set_env_ffff0000907d01d0);
    FUN_ffff00009077bce4(lVar3);
    thunk_FUN_ffff0000907c2ad8(piVar2);
    return 0xfffffffb;
  }
  if (*piVar2 != 0x3043524d) {
    FUN_ffff00009077eb88(piVar2,0,0x1000);
    *piVar2 = 0x3043524d;
  }
  iVar1 = FUN_ffff00009077f080(param_1,s_mrdump_output_ffff0000907debee,0xe);
  if (iVar1 == 0) {
    piVar4 = piVar2 + 2;
    uVar5 = 0x40;
  }
  else {
    iVar1 = FUN_ffff00009077f080(param_1,s_mrdump_allocate_size_ffff0000907eb5d9,0x15);
    if (iVar1 == 0) {
      piVar4 = piVar2 + 0x16;
    }
    else {
      iVar1 = FUN_ffff00009077f080(param_1,s_mrdump_mem_size_ffff0000907ed0db,0x10);
      if (iVar1 != 0) {
        iVar1 = FUN_ffff00009077f080(param_1,s_mrdump_lbaooo_ffff000090801506,0xe);
        if (iVar1 == 0) {
          piVar4 = piVar2 + 0x1a;
          uVar5 = 0x18;
        }
        else {
          iVar1 = FUN_ffff00009077f080(param_1,s_mrdump_dump_type_ffff0000907eebfa,0x11);
          if (iVar1 == 0) {
            piVar4 = piVar2 + 0x20;
          }
          else {
            iVar1 = FUN_ffff00009077f080(param_1,s_mrdump_fb_timeout_ffff0000907e1eba,0x12);
            if (iVar1 != 0) {
              FUN_ffff00009077de54
                        (s__s__d__unknown_mrdump_config_ffff0000907debfc,
                         s_mrdump_set_env_ffff0000907d01d0,0xa8);
              FUN_ffff00009077bce4(lVar3);
              thunk_FUN_ffff0000907c2ad8(piVar2);
              return 0xffffffff;
            }
            piVar4 = piVar2 + 0x28;
          }
          uVar5 = 0x20;
        }
        goto LAB_ffff000090783098;
      }
      piVar4 = piVar2 + 0x12;
    }
    uVar5 = 0x10;
  }
LAB_ffff000090783098:
  FUN_ffff00009077ef1c(piVar4,param_2,uVar5);
  FUN_ffff00009077de54
            (s__s__d___s_update_to__s_ffff0000907f716d,s_mrdump_set_env_ffff0000907d01d0,0xad,
             param_1,param_2);
  iVar1 = FUN_ffff00009077be7c(lVar3,piVar2,lVar7,0x1000);
  FUN_ffff00009077bce4(lVar3);
  thunk_FUN_ffff0000907c2ad8(piVar2);
  uVar6 = 0;
  if (iVar1 != 0x1000) {
    uVar6 = 0xfffffffb;
  }
  return uVar6;
}


