// ffff000090741018  size=288  FUN_ffff000090741018

void FUN_ffff000090741018(undefined8 param_1)

{
  int iVar1;
  int iVar2;
  int iVar3;
  long lVar4;
  short local_34 [2];
  
  local_34[0] = 0;
  lVar4 = FUN_ffff000090736ed0(s_mt6379_ffff0000907ea425);
  if (lVar4 != 0) {
    iVar1 = FUN_ffff00009073732c(lVar4,2,local_34,2);
    if ((-1 < iVar1) && (local_34[0] == 0x6379)) {
      iVar2 = FUN_ffff00009076e064(param_1,0xffffffff,s_richtek_rt1711h_ffff0000907efbb9);
      if (iVar2 < 0) {
        FUN_ffff00009077de54
                  (s__s__d__Failed_to_get_compatible_r_ffff0000907dae6c,
                   s_mt6379_set_fdt_typec_info_ffff0000907f9da3,0x28);
      }
      else {
        iVar3 = FUN_ffff00009077ef88(s_disabled_ffff0000907f0e0d);
        iVar2 = FUN_ffff00009076e40c
                          (param_1,iVar2,s_status_ffff0000907edb6a,s_disabled_ffff0000907f0e0d,
                           iVar3 + 1);
        if (iVar2 < 0) {
          FUN_ffff00009077de54
                    (s__s__d__Failed_to_overwrite_statu_ffff000090800bb9,
                     s_mt6379_set_fdt_typec_info_ffff0000907f9da3,0x2b,iVar1);
        }
      }
    }
    return;
  }
  FUN_ffff00009077de54
            (s__s__d__Failed_to_get_regmap_ffff0000907dbd8a,
             s_mt6379_set_fdt_typec_info_ffff0000907f9da3,0x18);
  return;
}


