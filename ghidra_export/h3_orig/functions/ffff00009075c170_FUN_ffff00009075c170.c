// ffff00009075c170  size=512  FUN_ffff00009075c170

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009075c170(undefined8 param_1)

{
  int iVar1;
  int iVar2;
  int iVar3;
  undefined8 *puVar4;
  undefined8 local_38;
  
  local_38 = 0;
  if (_DAT_ffff00009087bdf8 == 0) {
    FUN_ffff00009077de54(s_no_lcm_info_from_bl2_ext_ffff0000907e3503);
  }
  iVar1 = FUN_ffff00009075d2f8(0xffff00009087bdfc,param_1);
  if (iVar1 != 0) {
    FUN_ffff00009077de54(s_no_lcm_found_by_idx_ffff0000907fbec5);
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_mt6991_disp_mt_ffff0000907fdc80,0x28b,&DAT_ffff0000907d24c9);
  }
  _DAT_ffff00009087be7c = FUN_ffff00009075e0f8();
  puVar4 = (undefined8 *)FUN_ffff00009071a204(s_framebuffer_ffff0000907d4cc5,0);
  if (puVar4 == (undefined8 *)0x0) {
    FUN_ffff00009077de54
              (s___s____d__fb_mblock_query_failed_ffff0000907ee7e9,s_disp_sw_init_ffff0000907eccbb,
               0x291);
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_mt6991_disp_mt_ffff0000907fdc80,0x292,&DAT_ffff0000907d24c9);
  }
  _DAT_ffff00009087be80 = *puVar4;
  DAT_ffff00009087be88 = 1;
  iVar1 = FUN_ffff00009077b79c
                    (DAT_ffff000090839908,s_vm_framebuffer_ffff0000907fdcac,
                     _DAT_ffff00009087be7c + 0xffffU & 0xffff0000,&local_38,0xc,
                     _DAT_ffff00009087be80,0,0);
  if (iVar1 == 0) {
    _DAT_ffff00009087bea8 = _DAT_ffff00009087be80;
    _DAT_ffff00009087be9c = (undefined4)_DAT_ffff00009087be80;
    _DAT_ffff00009087be90 = local_38;
    FUN_ffff00009077de54
              (s_fb_va__0x_llx__fb_pa__0x_08x__fb_ffff0000907f1905,local_38,_DAT_ffff00009087be80,
               _DAT_ffff00009087be80);
    FUN_ffff00009077de54
              (s___s___check_dsi_state__0x_x__0x__ffff0000907cfea0,
               s_mt_disp_sw_init_ffff0000907e809d,_DAT_ffff000032490400,_DAT_ffff000032490034);
    iVar1 = FUN_ffff00009075d57c();
    iVar2 = FUN_ffff00009075d6dc();
    iVar3 = FUN_ffff00009075d790();
    _DAT_ffff00009087be98 = (uint)(iVar2 * iVar1 * iVar3) >> 3;
    FUN_ffff00009075cdbc(_DAT_ffff00009087bdf8 != 0,param_1,0xffff00009087bdfc);
    FUN_ffff00009077de54
              (s___s___check_dsi_state__0x_x__0x__ffff0000907dff09,
               s_mt_disp_sw_init_ffff0000907e809d,_DAT_ffff000032490400,_DAT_ffff000032490034);
    return;
  }
  FUN_ffff00009077de54(s_fastlogo_vmm_alloc_physical_fail_ffff0000907ee7c5);
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04
            (s_platform_mediatek_mt6991_disp_mt_ffff0000907fdc80,0x29f,&DAT_ffff0000907d24c9);
}


