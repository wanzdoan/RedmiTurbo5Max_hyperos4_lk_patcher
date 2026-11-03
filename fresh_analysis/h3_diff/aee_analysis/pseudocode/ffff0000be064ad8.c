
undefined8 FUN_ffff0000be064ad8(undefined8 param_1,undefined8 param_2,int param_3)

{
  int iVar1;
  int *piVar2;
  long lVar3;
  char *pcVar4;
  int *piVar5;
  undefined8 uVar6;
  long lVar7;
  
  piVar2 = (int *)FUN_ffff0000be05f250(1,0x1000);
  if (piVar2 == (int *)0x0) {
    FUN_ffff0000be05fd98
              (s__s__d___s_malloc_for_mrdump_conf_ffff0000be0a6390,s_mrdump_get_env_ffff0000be0a3c0d
               ,0x39,s_mrdump_get_env_ffff0000be0a3c0d);
    return 0xfffffff4;
  }
  lVar3 = FUN_ffff0000be05df6c(s_expdb_ffff0000be0a79cf);
  if (lVar3 == 0) {
    FUN_ffff0000be05fd98
              (s__s__d___s_partition_does_not_exi_ffff0000be0b48ce,s_mrdump_get_env_ffff0000be0a3c0d
               ,0x40,s_expdb_ffff0000be0a79cf);
    thunk_FUN_ffff0000be09937c(piVar2);
    return 0xffffffed;
  }
  lVar7 = *(long *)(lVar3 + 0x20);
  iVar1 = FUN_ffff0000be063170();
  iVar1 = FUN_ffff0000be05e000(lVar3,piVar2,lVar7 - iVar1,0x1000);
  if (iVar1 != 0x1000) {
    FUN_ffff0000be05fd98
              (s__s__d___s__bio_read_fail_ffff0000be0a76ae,s_mrdump_get_env_ffff0000be0a3c0d,0x4c,
               s_mrdump_get_env_ffff0000be0a3c0d);
    FUN_ffff0000be05def0(lVar3);
    thunk_FUN_ffff0000be09937c(piVar2);
    return 0xfffffffb;
  }
  if (*piVar2 == 0x3043524d) {
    iVar1 = FUN_ffff0000be060f54(param_1,s_mrdump_output_ffff0000be0ade98,0xe);
    if (iVar1 == 0) {
      piVar5 = piVar2 + 2;
    }
    else {
      iVar1 = FUN_ffff0000be060f54(param_1,s_mrdump_allocate_size_ffff0000be0b6d8a,0x14);
      if (iVar1 == 0) {
        piVar5 = piVar2 + 0x16;
      }
      else {
        iVar1 = FUN_ffff0000be060f54(param_1,s_mrdump_mem_size_ffff0000be0b72e7,0x10);
        if (iVar1 == 0) {
          piVar5 = piVar2 + 0x12;
        }
        else {
          iVar1 = FUN_ffff0000be060f54(param_1,s_mrdump_lbaooo_ffff0000be0c5911,0xe);
          if (iVar1 == 0) {
            piVar5 = piVar2 + 0x1a;
          }
          else {
            iVar1 = FUN_ffff0000be060f54(param_1,s_mrdump_dump_type_ffff0000be0b854a,0x11);
            if (iVar1 == 0) {
              piVar5 = piVar2 + 0x20;
            }
            else {
              iVar1 = FUN_ffff0000be060f54(param_1,s_mrdump_fb_timeout_ffff0000be0b04c0,0x12);
              if (iVar1 == 0) {
                piVar5 = piVar2 + 0x28;
              }
              else {
                iVar1 = FUN_ffff0000be060f54
                                  (param_1,s_mrdump_compression_method_ffff0000be0bccd2,0x1a);
                if (iVar1 != 0) {
                  pcVar4 = s__s__d__unknown_mrdump_config_ffff0000be0adea6;
                  uVar6 = 0x69;
                  goto LAB_ffff0000be064ccc;
                }
                piVar5 = piVar2 + 0x30;
              }
            }
          }
        }
      }
    }
    FUN_ffff0000be060df0(param_2,piVar5,(long)param_3);
    FUN_ffff0000be05fd98
              (s__s__d___s___s_ffff0000be0c591f,s_mrdump_get_env_ffff0000be0a3c0d,0x6f,param_1,
               param_2);
    FUN_ffff0000be05def0(lVar3);
    thunk_FUN_ffff0000be09937c(piVar2);
    uVar6 = 0;
  }
  else {
    FUN_ffff0000be060df0(param_2,&DAT_ffff0000be0ae340,(long)param_3);
    pcVar4 = s__s__d__unknown_mrdump_config_sig_ffff0000be0bed11;
    uVar6 = 0x54;
LAB_ffff0000be064ccc:
    FUN_ffff0000be05fd98(pcVar4,s_mrdump_get_env_ffff0000be0a3c0d,uVar6);
    FUN_ffff0000be05def0(lVar3);
    thunk_FUN_ffff0000be09937c(piVar2);
    uVar6 = 0xffffffff;
  }
  return uVar6;
}


