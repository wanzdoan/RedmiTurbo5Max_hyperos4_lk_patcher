// ffff00009071d1c0  size=632  FUN_ffff00009071d1c0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009071d1c0(long param_1)

{
  uint uVar1;
  int iVar2;
  int iVar3;
  undefined4 local_50;
  uint local_4c;
  uint uStack_48;
  uint local_44;
  
  if (param_1 != 0) {
    iVar2 = FUN_ffff00009076da24(param_1,s__aod_scp_ffff0000907e9017);
    if (iVar2 < 0) {
      iVar2 = FUN_ffff00009076da24(param_1,s__soc_aod_scp_ffff0000907f284a);
      if (iVar2 < 0) {
        FUN_ffff00009077e11c
                  (s___s__get_path_offset_fail__ret___ffff0000907fed0e,
                   s_set_fdt_aod_scp_ffff0000908021a9,iVar2);
        return;
      }
    }
    uVar1 = (_DAT_ffff0000908605b4 & 0xff00ff00) >> 8 | (_DAT_ffff0000908605b4 & 0xff00ff) << 8;
    local_44 = uVar1 >> 0x10 | uVar1 << 0x10;
    uVar1 = (_DAT_ffff0000908605b8 & 0xff00ff00) >> 8 | (_DAT_ffff0000908605b8 & 0xff00ff) << 8;
    uStack_48 = uVar1 >> 0x10 | uVar1 << 0x10;
    uVar1 = (_DAT_ffff0000908605bc & 0xff00ff00) >> 8 | (_DAT_ffff0000908605bc & 0xff00ff) << 8;
    local_4c = uVar1 >> 0x10 | uVar1 << 0x10;
    local_50 = 0x803;
    FUN_ffff00009077e11c
              (s__s__d__aod_scp_offset____d_ffff0000907d408f,s_set_fdt_aod_scp_ffff0000908021a9,0x7d
               ,iVar2);
    FUN_ffff00009077e11c
              (s__s__d__aod_scp_addr___0x_x_ffff0000907d7606,s_set_fdt_aod_scp_ffff0000908021a9,0x7e
               ,_DAT_ffff0000908605b4);
    FUN_ffff00009077e11c
              (s__s__d__aod_scp_size___0x_x_ffff0000907e5da2,s_set_fdt_aod_scp_ffff0000908021a9,0x7f
               ,_DAT_ffff0000908605b8);
    FUN_ffff00009077e11c
              (s__s__d__aod_scp_pic___0x_x_ffff0000907d7622,s_set_fdt_aod_scp_ffff0000908021a9,0x80,
               _DAT_ffff0000908605bc);
    iVar3 = FUN_ffff00009076e6d4(param_1,iVar2,s_aod_scp_addr_ffff0000907e0d85,&local_44,4);
    if (iVar3 < 0) {
      FUN_ffff00009077e11c
                (s___s__failed_to_update_ado_scp_ad_ffff0000908021b9,
                 s_set_fdt_aod_scp_ffff0000908021a9,iVar3);
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc
                (s_platform_mediatek_common_aod_scp_ffff000090800752,0x87,&DAT_ffff0000907d2506);
    }
    iVar3 = FUN_ffff00009076e6d4(param_1,iVar2,s_aod_scp_size_ffff0000907ec135,&uStack_48,4);
    if (iVar3 < 0) {
      FUN_ffff00009077e11c
                (s___s__failed_to_update_ado_scp_si_ffff0000908021e5,
                 s_set_fdt_aod_scp_ffff0000908021a9,iVar3);
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc
                (s_platform_mediatek_common_aod_scp_ffff000090800752,0x8e,&DAT_ffff0000907d2506);
    }
    iVar3 = FUN_ffff00009076e6d4(param_1,iVar2,s_aod_scp_pic_ffff0000907e5dbe,&local_4c,4);
    if (iVar3 < 0) {
      FUN_ffff00009077e11c
                (s___s__failed_to_update_ado_scp_pi_ffff0000907d5be7,
                 s_set_fdt_aod_scp_ffff0000908021a9,iVar3);
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc
                (s_platform_mediatek_common_aod_scp_ffff000090800752,0x95,&DAT_ffff0000907d2506);
    }
    iVar2 = FUN_ffff00009076e6d4(param_1,iVar2,s_aod_scp_dgt_oft_ffff0000907d5c12,&local_50,4);
    if (iVar2 < 0) {
      FUN_ffff00009077e11c
                (s___s__failed_to_update_ado_scp_dg_ffff000090802211,
                 s_set_fdt_aod_scp_ffff0000908021a9,iVar2);
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc
                (s_platform_mediatek_common_aod_scp_ffff000090800752,0x9c,&DAT_ffff0000907d2506);
    }
  }
  return;
}


