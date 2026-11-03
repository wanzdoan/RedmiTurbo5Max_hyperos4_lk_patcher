// ffff000090783f48  size=420  FUN_ffff000090783f48

void FUN_ffff000090783f48(long param_1)

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
    pcVar4 = s__s__d__fdt_is_NULL_ffff0000907f7c01;
    uVar5 = 0x94;
  }
  else {
    iVar1 = FUN_ffff00009076d75c(param_1,&DAT_ffff0000907ea66c);
    if (-1 < iVar1) {
      iVar1 = FUN_ffff00009076e878(param_1,iVar1,s_debug_kinfo_ffff0000907fe0ed);
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
        uVar2 = FUN_ffff00009077f434
                          (&local_a0,0x80,&DAT_ffff0000907dbe71,
                           s_google_debug_kinfo_ffff0000907ed10c);
        if (uVar2 < 0x80) {
          iVar3 = FUN_ffff00009077ef88(&local_a0);
          iVar1 = FUN_ffff00009076e40c
                            (param_1,iVar1,s_compatible_ffff0000907d7429,&local_a0,iVar3 + 1);
          if (iVar1 == 0) {
            iVar1 = FUN_ffff00009071a008
                              (s__debug_kinfo_ffff000090802d25,s_aee_debug_kinfo_ffff0000907f8bf9);
            if (iVar1 != 0) {
              FUN_ffff00009077de54
                        (s__s__d__pre_alloc_phandle_failed_ffff0000907d514d,
                         s_mrdump_debug_kinfo_fdt_setup_ffff0000907db812,0xb2);
            }
          }
          else {
            FUN_ffff00009077de54
                      (s__s__d__Error__fdt_setprop_string_ffff0000907e5c42,
                       s_mrdump_debug_kinfo_fdt_setup_ffff0000907db812,0xac,iVar1);
          }
        }
        else {
          FUN_ffff00009077de54
                    (s__s__d__Error__snprintf_failed_re_ffff0000907d1e9d,
                     s_mrdump_debug_kinfo_fdt_setup_ffff0000907db812,0xa6,uVar2);
        }
        return;
      }
      FUN_ffff00009077de54
                (s__s__d__Error__fdt_add_subnode_fa_ffff0000907cf30e,
                 s_mrdump_debug_kinfo_fdt_setup_ffff0000907db812,0xa0,iVar1);
      return;
    }
    pcVar4 = s__s__d__fdt_offset_is_invalid_ffff0000907fc398;
    uVar5 = 0x9a;
  }
  FUN_ffff00009077de54(pcVar4,s_mrdump_debug_kinfo_fdt_setup_ffff0000907db812,uVar5);
  return;
}


