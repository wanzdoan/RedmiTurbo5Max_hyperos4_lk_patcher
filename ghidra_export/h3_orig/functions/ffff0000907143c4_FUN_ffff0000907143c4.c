// ffff0000907143c4  size=504  FUN_ffff0000907143c4

void FUN_ffff0000907143c4(undefined8 param_1)

{
  int iVar1;
  uint uVar2;
  int iVar3;
  int iVar4;
  uint local_14;
  
  iVar1 = FUN_ffff00009076d75c(param_1,&DAT_ffff0000907ea66c);
  if (iVar1 < 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_app_mt_boot_mt_boot_c_ffff0000907f92d4,0xa8,s_offset_>__0_ffff0000907f3f8a);
  }
  iVar1 = FUN_ffff00009076e878(param_1,iVar1,s_bootprof_ffff000090801e4d);
  if (-1 < iVar1) {
    uVar2 = FUN_ffff0000907035c4();
    uVar2 = (uVar2 & 0xff00ff00) >> 8 | (uVar2 & 0xff00ff) << 8;
    local_14 = uVar2 >> 0x10 | uVar2 << 0x10;
    iVar3 = FUN_ffff00009076e40c(param_1,iVar1,&DAT_ffff0000907d26eb,&local_14,4);
    if (-1 < iVar3) {
      local_14 = 0;
      uVar2 = FUN_ffff00009070363c();
      uVar2 = (uVar2 & 0xff00ff00) >> 8 | (uVar2 & 0xff00ff) << 8;
      local_14 = uVar2 >> 0x10 | uVar2 << 0x10;
      iVar3 = FUN_ffff00009076e40c(param_1,iVar1,s_logo_t_ffff0000907f933d,&local_14,4);
      if (-1 < iVar3) {
        local_14 = 0;
        iVar3 = FUN_ffff0000907167d4();
        iVar4 = FUN_ffff000090703628();
        uVar2 = (iVar3 - iVar4 & 0xff00ff00U) >> 8 | (iVar3 - iVar4 & 0xff00ffU) << 8;
        local_14 = uVar2 >> 0x10 | uVar2 << 0x10;
        iVar3 = FUN_ffff00009076e40c(param_1,iVar1,&DAT_ffff0000907dc118,&local_14,4);
        if (-1 < iVar3) {
          local_14 = 0;
          uVar2 = FUN_ffff0000907035d8();
          uVar2 = (uVar2 & 0xff00ff00) >> 8 | (uVar2 & 0xff00ff) << 8;
          local_14 = uVar2 >> 0x10 | uVar2 << 0x10;
          iVar3 = FUN_ffff00009076e40c(param_1,iVar1,s_bl2_ext_t_ffff0000907ebe75,&local_14,4);
          if (-1 < iVar3) {
            local_14 = 0;
            uVar2 = FUN_ffff0000907035ec();
            uVar2 = (uVar2 & 0xff00ff00) >> 8 | (uVar2 & 0xff00ff) << 8;
            local_14 = uVar2 >> 0x10 | uVar2 << 0x10;
            iVar3 = FUN_ffff00009076e40c(param_1,iVar1,s_tfa_t_ffff000090801e47,&local_14,4);
            if (-1 < iVar3) {
              local_14 = 0;
              uVar2 = FUN_ffff000090703600();
              uVar2 = (uVar2 & 0xff00ff00) >> 8 | (uVar2 & 0xff00ff) << 8;
              local_14 = uVar2 >> 0x10 | uVar2 << 0x10;
              iVar3 = FUN_ffff00009076e40c(param_1,iVar1,s_sec_os_t_ffff0000907edabc,&local_14,4);
              if (-1 < iVar3) {
                local_14 = 0;
                uVar2 = FUN_ffff000090703614();
                uVar2 = (uVar2 & 0xff00ff00) >> 8 | (uVar2 & 0xff00ff) << 8;
                local_14 = uVar2 >> 0x10 | uVar2 << 0x10;
                iVar1 = FUN_ffff00009076e40c(param_1,iVar1,&DAT_ffff0000907d0b98,&local_14,4);
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
    FUN_ffff00009077cd04(s_app_mt_boot_mt_boot_c_ffff0000907f92d4,0xb2,s_ret_>__0_ffff0000907da5a0);
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04
            (s_app_mt_boot_mt_boot_c_ffff0000907f92d4,0xab,s_nodeoffset_>__0_ffff0000907f261e);
}


