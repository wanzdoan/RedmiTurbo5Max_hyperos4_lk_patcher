// ffff000090784210  size=420  FUN_ffff000090784210

void FUN_ffff000090784210(long param_1)

{
  int iVar1;
  uint uVar2;
  int iVar3;
  char *pcVar4;
  undefined8 uVar5;
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined8 uStack_38;
  undefined8 local_30;
  undefined8 uStack_28;
  
  if (param_1 == 0) {
    pcVar4 = s__s__d__fdt_is_NULL_ffff0000907f7d9d;
    uVar5 = 0x94;
  }
  else {
    iVar1 = FUN_ffff00009076da24(param_1,&DAT_ffff0000907ea7ef);
    if (-1 < iVar1) {
      iVar1 = FUN_ffff00009076eb40(param_1,iVar1,s_debug_kinfo_ffff0000907fe295);
      if (-1 < iVar1) {
        local_30 = 0;
        uStack_28 = 0;
        local_40 = 0;
        uStack_38 = 0;
        local_50 = 0;
        uStack_48 = 0;
        local_60 = 0;
        uStack_58 = 0;
        local_70 = 0;
        uStack_68 = 0;
        local_80 = 0;
        uStack_78 = 0;
        local_90 = 0;
        uStack_88 = 0;
        local_a0 = 0;
        uStack_98 = 0;
        uVar2 = FUN_ffff00009077f6fc
                          (&local_a0,0x80,&DAT_ffff0000907dbf7c,
                           s_google_debug_kinfo_ffff0000907ed28f);
        if (uVar2 < 0x80) {
          iVar3 = FUN_ffff00009077f250(&local_a0);
          iVar1 = FUN_ffff00009076e6d4
                            (param_1,iVar1,s_compatible_ffff0000907d74bf,&local_a0,iVar3 + 1);
          if (iVar1 == 0) {
            iVar1 = FUN_ffff00009071a14c
                              (s__debug_kinfo_ffff000090802f29,s_aee_debug_kinfo_ffff0000907f8d95);
            if (iVar1 != 0) {
              FUN_ffff00009077e11c
                        (s__s__d__pre_alloc_phandle_failed_ffff0000907d518a,
                         s_mrdump_debug_kinfo_fdt_setup_ffff0000907db91d,0xb2);
            }
          }
          else {
            FUN_ffff00009077e11c
                      (s__s__d__Error__fdt_setprop_string_ffff0000907e5d56,
                       s_mrdump_debug_kinfo_fdt_setup_ffff0000907db91d,0xac,iVar1);
          }
        }
        else {
          FUN_ffff00009077e11c
                    (s__s__d__Error__snprintf_failed_re_ffff0000907d1eda,
                     s_mrdump_debug_kinfo_fdt_setup_ffff0000907db91d,0xa6,uVar2);
        }
        return;
      }
      FUN_ffff00009077e11c
                (s__s__d__Error__fdt_add_subnode_fa_ffff0000907cf32d,
                 s_mrdump_debug_kinfo_fdt_setup_ffff0000907db91d,0xa0,iVar1);
      return;
    }
    pcVar4 = s__s__d__fdt_offset_is_invalid_ffff0000907fc534;
    uVar5 = 0x9a;
  }
  FUN_ffff00009077e11c(pcVar4,s_mrdump_debug_kinfo_fdt_setup_ffff0000907db91d,uVar5);
  return;
}


