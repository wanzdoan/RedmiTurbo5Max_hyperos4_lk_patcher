// ffff0000907412b0  size=288  FUN_ffff0000907412b0

void FUN_ffff0000907412b0(undefined8 param_1)

{
  int iVar1;
  int iVar2;
  int iVar3;
  long lVar4;
  short local_34 [2];
  
  local_34[0] = 0;
  lVar4 = FUN_ffff000090737168(s_mt6379_ffff0000907ea569);
  if (lVar4 != 0) {
    iVar1 = FUN_ffff0000907375c4(lVar4,2,local_34,2);
    if ((-1 < iVar1) && (local_34[0] == 0x6379)) {
      iVar2 = FUN_ffff00009076e32c(param_1,0xffffffff,s_richtek_rt1711h_ffff0000907efd2a);
      if (iVar2 < 0) {
        FUN_ffff00009077e11c
                  (s__s__d__Failed_to_get_compatible_r_ffff0000907daf77,
                   s_mt6379_set_fdt_typec_info_ffff0000907f9f3f,0x28);
      }
      else {
        iVar3 = FUN_ffff00009077f250(s_disabled_ffff0000907f0f7e);
        iVar2 = FUN_ffff00009076e6d4
                          (param_1,iVar2,s_status_ffff0000907edcdb,s_disabled_ffff0000907f0f7e,
                           iVar3 + 1);
        if (iVar2 < 0) {
          FUN_ffff00009077e11c
                    (s__s__d__Failed_to_overwrite_statu_ffff000090800d95,
                     s_mt6379_set_fdt_typec_info_ffff0000907f9f3f,0x2b,iVar1);
        }
      }
    }
    return;
  }
  FUN_ffff00009077e11c
            (s__s__d__Failed_to_get_regmap_ffff0000907dbe95,
             s_mt6379_set_fdt_typec_info_ffff0000907f9f3f,0x18);
  return;
}


