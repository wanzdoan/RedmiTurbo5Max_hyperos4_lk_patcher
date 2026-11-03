// ffff00009071d034  size=632  FUN_ffff00009071d034

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009071d034(long param_1)

{
  uint uVar1;
  int iVar2;
  int iVar3;
  undefined4 local_50;
  uint local_4c;
  uint uStack_48;
  uint local_44;
  
  if (param_1 != 0) {
    iVar2 = FUN_ffff00009076d75c(param_1,s__aod_scp_ffff0000907e8ed3);
    if (iVar2 < 0) {
      iVar2 = FUN_ffff00009076d75c(param_1,s__soc_aod_scp_ffff0000907f26be);
      if (iVar2 < 0) {
        FUN_ffff00009077de54
                  (s___s__get_path_offset_fail__ret___ffff0000907feb66,
                   s_set_fdt_aod_scp_ffff000090801fc3,iVar2);
        return;
      }
    }
    uVar1 = (_DAT_ffff000090860518 & 0xff00ff00) >> 8 | (_DAT_ffff000090860518 & 0xff00ff) << 8;
    local_44 = uVar1 >> 0x10 | uVar1 << 0x10;
    uVar1 = (_DAT_ffff00009086051c & 0xff00ff00) >> 8 | (_DAT_ffff00009086051c & 0xff00ff) << 8;
    uStack_48 = uVar1 >> 0x10 | uVar1 << 0x10;
    uVar1 = (_DAT_ffff000090860520 & 0xff00ff00) >> 8 | (_DAT_ffff000090860520 & 0xff00ff) << 8;
    local_4c = uVar1 >> 0x10 | uVar1 << 0x10;
    local_50 = 0x803;
    FUN_ffff00009077de54
              (s__s__d__aod_scp_offset____d_ffff0000907d4052,s_set_fdt_aod_scp_ffff000090801fc3,0x7d
               ,iVar2);
    FUN_ffff00009077de54
              (s__s__d__aod_scp_addr___0x_x_ffff0000907d7570,s_set_fdt_aod_scp_ffff000090801fc3,0x7e
               ,_DAT_ffff000090860518);
    FUN_ffff00009077de54
              (s__s__d__aod_scp_size___0x_x_ffff0000907e5c8e,s_set_fdt_aod_scp_ffff000090801fc3,0x7f
               ,_DAT_ffff00009086051c);
    FUN_ffff00009077de54
              (s__s__d__aod_scp_pic___0x_x_ffff0000907d758c,s_set_fdt_aod_scp_ffff000090801fc3,0x80,
               _DAT_ffff000090860520);
    iVar3 = FUN_ffff00009076e40c(param_1,iVar2,s_aod_scp_addr_ffff0000907e0ce2,&local_44,4);
    if (iVar3 < 0) {
      FUN_ffff00009077de54
                (s___s__failed_to_update_ado_scp_ad_ffff000090801fd3,
                 s_set_fdt_aod_scp_ffff000090801fc3,iVar3);
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04
                (s_platform_mediatek_common_aod_scp_ffff000090800576,0x87,&DAT_ffff0000907d24c9);
    }
    iVar3 = FUN_ffff00009076e40c(param_1,iVar2,s_aod_scp_size_ffff0000907ebfb2,&uStack_48,4);
    if (iVar3 < 0) {
      FUN_ffff00009077de54
                (s___s__failed_to_update_ado_scp_si_ffff000090801fff,
                 s_set_fdt_aod_scp_ffff000090801fc3,iVar3);
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04
                (s_platform_mediatek_common_aod_scp_ffff000090800576,0x8e,&DAT_ffff0000907d24c9);
    }
    iVar3 = FUN_ffff00009076e40c(param_1,iVar2,s_aod_scp_pic_ffff0000907e5caa,&local_4c,4);
    if (iVar3 < 0) {
      FUN_ffff00009077de54
                (s___s__failed_to_update_ado_scp_pi_ffff0000907d5b97,
                 s_set_fdt_aod_scp_ffff000090801fc3,iVar3);
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04
                (s_platform_mediatek_common_aod_scp_ffff000090800576,0x95,&DAT_ffff0000907d24c9);
    }
    iVar2 = FUN_ffff00009076e40c(param_1,iVar2,s_aod_scp_dgt_oft_ffff0000907d5bc2,&local_50,4);
    if (iVar2 < 0) {
      FUN_ffff00009077de54
                (s___s__failed_to_update_ado_scp_dg_ffff00009080202b,
                 s_set_fdt_aod_scp_ffff000090801fc3,iVar2);
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04
                (s_platform_mediatek_common_aod_scp_ffff000090800576,0x9c,&DAT_ffff0000907d24c9);
    }
  }
  return;
}


