
undefined4 FUN_ffff0000be064d74(undefined8 param_1,undefined8 param_2)

{
  int iVar1;
  int *piVar2;
  long lVar3;
  int *piVar4;
  undefined8 uVar5;
  undefined4 uVar6;
  long lVar7;
  
  piVar2 = (int *)FUN_ffff0000be05f1bc(0x1000);
  if (piVar2 == (int *)0x0) {
    FUN_ffff0000be05fd98
              (s__s__d___s_malloc_for_mrdump_conf_ffff0000be0a6390,s_mrdump_set_env_ffff0000be0a3c1c
               ,0x7f,s_mrdump_set_env_ffff0000be0a3c1c);
    return 0xfffffff4;
  }
  FUN_ffff0000be060ae0(piVar2,0,0x1000);
  lVar3 = FUN_ffff0000be05df6c(s_expdb_ffff0000be0a79cf);
  if (lVar3 == 0) {
    FUN_ffff0000be05fd98
              (s__s__d___s_partition_does_not_exi_ffff0000be0b48ce,s_mrdump_set_env_ffff0000be0a3c1c
               ,0x86,s_expdb_ffff0000be0a79cf);
    thunk_FUN_ffff0000be09937c(piVar2);
    return 0xffffffed;
  }
  iVar1 = FUN_ffff0000be063170();
  lVar7 = *(long *)(lVar3 + 0x20) - (long)iVar1;
  iVar1 = FUN_ffff0000be05e000(lVar3,piVar2,lVar7,0x1000);
  if (iVar1 != 0x1000) {
    FUN_ffff0000be05fd98
              (s__s__d___s__bio_read_fail_ffff0000be0a76ae,s_mrdump_set_env_ffff0000be0a3c1c,0x90,
               s_mrdump_set_env_ffff0000be0a3c1c);
    FUN_ffff0000be05def0(lVar3);
    thunk_FUN_ffff0000be09937c(piVar2);
    return 0xfffffffb;
  }
  if (*piVar2 != 0x3043524d) {
    FUN_ffff0000be060ae0(piVar2,0,0x1000);
    *piVar2 = 0x3043524d;
  }
  iVar1 = FUN_ffff0000be060f54(param_1,s_mrdump_output_ffff0000be0ade98,0xe);
  if (iVar1 == 0) {
    piVar4 = piVar2 + 2;
    uVar5 = 0x40;
  }
  else {
    iVar1 = FUN_ffff0000be060f54(param_1,s_mrdump_allocate_size_ffff0000be0b6d8a,0x15);
    if (iVar1 == 0) {
      piVar4 = piVar2 + 0x16;
    }
    else {
      iVar1 = FUN_ffff0000be060f54(param_1,s_mrdump_mem_size_ffff0000be0b72e7,0x10);
      if (iVar1 != 0) {
        iVar1 = FUN_ffff0000be060f54(param_1,s_mrdump_lbaooo_ffff0000be0c5911,0xe);
        if (iVar1 == 0) {
          piVar4 = piVar2 + 0x1a;
          uVar5 = 0x18;
        }
        else {
          iVar1 = FUN_ffff0000be060f54(param_1,s_mrdump_dump_type_ffff0000be0b854a,0x11);
          if (iVar1 == 0) {
            piVar4 = piVar2 + 0x20;
          }
          else {
            iVar1 = FUN_ffff0000be060f54(param_1,s_mrdump_fb_timeout_ffff0000be0b04c0,0x12);
            if (iVar1 != 0) {
              FUN_ffff0000be05fd98
                        (s__s__d__unknown_mrdump_config_ffff0000be0adea6,
                         s_mrdump_set_env_ffff0000be0a3c1c,0xa8);
              FUN_ffff0000be05def0(lVar3);
              thunk_FUN_ffff0000be09937c(piVar2);
              return 0xffffffff;
            }
            piVar4 = piVar2 + 0x28;
          }
          uVar5 = 0x20;
        }
        goto LAB_ffff0000be064f80;
      }
      piVar4 = piVar2 + 0x12;
    }
    uVar5 = 0x10;
  }
LAB_ffff0000be064f80:
  FUN_ffff0000be060df0(piVar4,param_2,uVar5);
  FUN_ffff0000be05fd98
            (s__s__d___s_update_to__s_ffff0000be0bed39,s_mrdump_set_env_ffff0000be0a3c1c,0xad,
             param_1,param_2);
  iVar1 = FUN_ffff0000be05e088(lVar3,piVar2,lVar7,0x1000);
  FUN_ffff0000be05def0(lVar3);
  thunk_FUN_ffff0000be09937c(piVar2);
  uVar6 = 0;
  if (iVar1 != 0x1000) {
    uVar6 = 0xfffffffb;
  }
  return uVar6;
}


