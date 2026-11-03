
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009075c408(undefined8 param_1)

{
  int iVar1;
  int iVar2;
  int iVar3;
  undefined8 *puVar4;
  undefined8 local_38;
  
  local_38 = 0;
  if (DAT_ffff00009087be90 == 0) {
    FUN_ffff00009077e11c(s_no_lcm_info_from_bl2_ext_ffff0000907e35d4);
  }
  iVar1 = FUN_ffff00009075d590(&DAT_ffff00009087be94,param_1);
  if (iVar1 != 0) {
    FUN_ffff00009077e11c(s_no_lcm_found_by_idx_ffff0000907fc061);
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_mt6991_disp_mt_ffff0000907fde28,0x28b,&DAT_ffff0000907d2506);
  }
  DAT_ffff00009087bf14 = FUN_ffff00009075e390();
  puVar4 = (undefined8 *)FUN_ffff00009071a348(s_framebuffer_ffff0000907d4d02,0);
  if (puVar4 == (undefined8 *)0x0) {
    FUN_ffff00009077e11c
              (s___s____d__fb_mblock_query_failed_ffff0000907ee95a,s_disp_sw_init_ffff0000907ece3e,
               0x291);
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_mt6991_disp_mt_ffff0000907fde28,0x292,&DAT_ffff0000907d2506);
  }
  _DAT_ffff00009087bf18 = *puVar4;
  DAT_ffff00009087bf20 = 1;
  iVar1 = FUN_ffff00009077ba64
                    (PTR_DAT_ffff000090839928,s_vm_framebuffer_ffff0000907fde54,
                     DAT_ffff00009087bf14 + 0xffffU & 0xffff0000,&local_38,0xc,_DAT_ffff00009087bf18
                     ,0,0);
  if (iVar1 == 0) {
    DAT_ffff00009087bf40 = _DAT_ffff00009087bf18;
    DAT_ffff00009087bf34 = (undefined4)_DAT_ffff00009087bf18;
    DAT_ffff00009087bf28 = local_38;
    FUN_ffff00009077e11c
              (s_fb_va__0x_llx__fb_pa__0x_08x__fb_ffff0000907f1a76,local_38,_DAT_ffff00009087bf18,
               _DAT_ffff00009087bf18);
    FUN_ffff00009077e11c
              (s___s___check_dsi_state__0x_x__0x__ffff0000907cfebf,
               s_mt_disp_sw_init_ffff0000907e81e1,_DAT_ffff000032490400,_DAT_ffff000032490034);
    iVar1 = FUN_ffff00009075d814();
    iVar2 = FUN_ffff00009075d974();
    iVar3 = FUN_ffff00009075da28();
    DAT_ffff00009087bf30 = (uint)(iVar2 * iVar1 * iVar3) >> 3;
    FUN_ffff00009075d054(DAT_ffff00009087be90 != 0,param_1,&DAT_ffff00009087be94);
    FUN_ffff00009077e11c
              (s___s___check_dsi_state__0x_x__0x__ffff0000907dffac,
               s_mt_disp_sw_init_ffff0000907e81e1,_DAT_ffff000032490400,_DAT_ffff000032490034);
    return;
  }
  FUN_ffff00009077e11c(s_fastlogo_vmm_alloc_physical_fail_ffff0000907ee936);
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_platform_mediatek_mt6991_disp_mt_ffff0000907fde28,0x29f,&DAT_ffff0000907d2506);
}


