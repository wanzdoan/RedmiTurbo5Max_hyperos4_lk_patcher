// ffff000090714508  size=504  FUN_ffff000090714508

void FUN_ffff000090714508(undefined8 param_1)

{
  int iVar1;
  uint uVar2;
  int iVar3;
  int iVar4;
  uint local_14;
  
  iVar1 = FUN_ffff00009076da24(param_1,&DAT_ffff0000907ea7ef);
  if (iVar1 < 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_app_mt_boot_mt_boot_c_ffff0000907f9470,0xa9,s_offset_>__0_ffff0000907f4116);
  }
  iVar1 = FUN_ffff00009076eb40(param_1,iVar1,s_bootprof_ffff000090802033);
  if (-1 < iVar1) {
    uVar2 = FUN_ffff00009070353c();
    uVar2 = (uVar2 & 0xff00ff00) >> 8 | (uVar2 & 0xff00ff) << 8;
    local_14 = uVar2 >> 0x10 | uVar2 << 0x10;
    iVar3 = FUN_ffff00009076e6d4(param_1,iVar1,&DAT_ffff0000907d2728,&local_14,4);
    if (-1 < iVar3) {
      local_14 = 0;
      uVar2 = FUN_ffff0000907035b4();
      uVar2 = (uVar2 & 0xff00ff00) >> 8 | (uVar2 & 0xff00ff) << 8;
      local_14 = uVar2 >> 0x10 | uVar2 << 0x10;
      iVar3 = FUN_ffff00009076e6d4(param_1,iVar1,s_logo_t_ffff0000907f94d9,&local_14,4);
      if (-1 < iVar3) {
        local_14 = 0;
        iVar3 = FUN_ffff000090716918();
        iVar4 = FUN_ffff0000907035a0();
        uVar2 = (iVar3 - iVar4 & 0xff00ff00U) >> 8 | (iVar3 - iVar4 & 0xff00ffU) << 8;
        local_14 = uVar2 >> 0x10 | uVar2 << 0x10;
        iVar3 = FUN_ffff00009076e6d4(param_1,iVar1,&DAT_ffff0000907dc1ee,&local_14,4);
        if (-1 < iVar3) {
          local_14 = 0;
          uVar2 = FUN_ffff000090703550();
          uVar2 = (uVar2 & 0xff00ff00) >> 8 | (uVar2 & 0xff00ff) << 8;
          local_14 = uVar2 >> 0x10 | uVar2 << 0x10;
          iVar3 = FUN_ffff00009076e6d4(param_1,iVar1,s_bl2_ext_t_ffff0000907ebff8,&local_14,4);
          if (-1 < iVar3) {
            local_14 = 0;
            uVar2 = FUN_ffff000090703564();
            uVar2 = (uVar2 & 0xff00ff00) >> 8 | (uVar2 & 0xff00ff) << 8;
            local_14 = uVar2 >> 0x10 | uVar2 << 0x10;
            iVar3 = FUN_ffff00009076e6d4(param_1,iVar1,s_tfa_t_ffff00009080202d,&local_14,4);
            if (-1 < iVar3) {
              local_14 = 0;
              uVar2 = FUN_ffff000090703578();
              uVar2 = (uVar2 & 0xff00ff00) >> 8 | (uVar2 & 0xff00ff) << 8;
              local_14 = uVar2 >> 0x10 | uVar2 << 0x10;
              iVar3 = FUN_ffff00009076e6d4(param_1,iVar1,s_sec_os_t_ffff0000907edc2d,&local_14,4);
              if (-1 < iVar3) {
                local_14 = 0;
                uVar2 = FUN_ffff00009070358c();
                uVar2 = (uVar2 & 0xff00ff00) >> 8 | (uVar2 & 0xff00ff) << 8;
                local_14 = uVar2 >> 0x10 | uVar2 << 0x10;
                iVar1 = FUN_ffff00009076e6d4(param_1,iVar1,&DAT_ffff0000907d0bcd,&local_14,4);
                if (-1 < iVar1) {
                  return;
                }
              }
            }
          }
        }
      }
    }
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc(s_app_mt_boot_mt_boot_c_ffff0000907f9470,0xb3,s_ret_>__0_ffff0000907da66e);
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_app_mt_boot_mt_boot_c_ffff0000907f9470,0xac,s_nodeoffset_>__0_ffff0000907f27aa);
}


