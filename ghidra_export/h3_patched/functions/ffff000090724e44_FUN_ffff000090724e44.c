// ffff000090724e44  size=1360  FUN_ffff000090724e44

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090724e44(long param_1)

{
  uint uVar1;
  uint uVar2;
  int iVar3;
  int iVar4;
  undefined8 local_58;
  uint local_50;
  uint uStack_4c;
  int local_48;
  int iStack_44;
  uint local_40;
  uint uStack_3c;
  uint local_38;
  uint uStack_34;
  uint local_30;
  uint uStack_2c;
  uint local_24;
  
  FUN_ffff000090723760(0xffff00009086cfd8);
  if (_DAT_ffff00009086d07c != 0x20190831) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_common_emi_emi_ffff0000907dc46a,0xb6,
               s_emi_info_arg_magic_tail____0x201_ffff0000907f43d6);
  }
  if (_DAT_ffff00009086d050 != 0x20221221) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_common_emi_emi_ffff0000907dc46a,0xb8,
               s_emi_info_arg_a2d_info_a2d_magic___ffff0000907fce19);
  }
  if (param_1 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_common_emi_emi_ffff0000907dc46a,0xbc,&DAT_ffff0000907d24c9);
  }
  iVar3 = FUN_ffff00009076d75c(param_1,s__soc_emicen_ffff0000907fce50);
  if (iVar3 < 0) {
    iVar3 = FUN_ffff00009076d75c(param_1,s__emicen_ffff0000907fec69);
    if (iVar3 < 0) {
      iVar3 = FUN_ffff00009076d75c(param_1,0);
      if (iVar3 < 0) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cd04
                  (s_platform_mediatek_common_emi_emi_ffff0000907dc46a,0xc5,&DAT_ffff0000907d24c9);
      }
    }
  }
  iStack_44 = (uint)DAT_ffff00009086cfda << 0x18;
  local_48 = (uint)DAT_ffff00009086cfdb << 0x18;
  if (DAT_ffff00009086cfdb == 0) {
    uStack_4c = 0;
    local_58 = 0;
    local_50 = 0;
  }
  else {
    uVar1 = (_DAT_ffff00009086cfe4 & 0xff00ff00) >> 8 | (_DAT_ffff00009086cfe4 & 0xff00ff) << 8;
    uVar2 = (_DAT_ffff00009086cfe0 & 0xff00ff00) >> 8 | (_DAT_ffff00009086cfe0 & 0xff00ff) << 8;
    local_58 = CONCAT44(uVar2 >> 0x10 | uVar2 << 0x10,uVar1 >> 0x10 | uVar1 << 0x10);
    if (DAT_ffff00009086cfdb == 1) {
      local_50 = 0;
      uStack_4c = 0;
    }
    else {
      uVar1 = (_DAT_ffff00009086cfec & 0xff00ff00) >> 8 | (_DAT_ffff00009086cfec & 0xff00ff) << 8;
      local_50 = uVar1 >> 0x10 | uVar1 << 0x10;
      uVar1 = (_DAT_ffff00009086cfe8 & 0xff00ff00) >> 8 | (_DAT_ffff00009086cfe8 & 0xff00ff) << 8;
      uStack_4c = uVar1 >> 0x10 | uVar1 << 0x10;
    }
  }
  _DAT_ffff00009086d278 = (_DAT_ffff00009086d054 & 0xf) << 0x18;
  _DAT_ffff00009086d27c = (_DAT_ffff00009086d054 & 0xf0) << 0x14;
  uVar1 = (_DAT_ffff00009086d058 & 0xff00ff00) >> 8 | (_DAT_ffff00009086d058 & 0xff00ff) << 8;
  _DAT_ffff00009086d280 = uVar1 >> 0x10 | uVar1 << 0x10;
  uVar1 = (_DAT_ffff00009086d05c & 0xff00ff00) >> 8 | (_DAT_ffff00009086d05c & 0xff00ff) << 8;
  _DAT_ffff00009086d284 = uVar1 >> 0x10 | uVar1 << 0x10;
  uVar1 = (_DAT_ffff00009086d068 & 0xff00ff00) >> 8 | (_DAT_ffff00009086d068 & 0xff00ff) << 8;
  _DAT_ffff00009086d290 = uVar1 >> 0x10 | uVar1 << 0x10;
  uVar1 = (_DAT_ffff00009086d060 & 0xff00ff00) >> 8 | (_DAT_ffff00009086d060 & 0xff00ff) << 8;
  _DAT_ffff00009086d288 = uVar1 >> 0x10 | uVar1 << 0x10;
  uVar1 = (_DAT_ffff00009086d064 & 0xff00ff00) >> 8 | (_DAT_ffff00009086d064 & 0xff00ff) << 8;
  _DAT_ffff00009086d28c = uVar1 >> 0x10 | uVar1 << 0x10;
  uVar1 = (_DAT_ffff00009086d06c & 0xff00ff00) >> 8 | (_DAT_ffff00009086d06c & 0xff00ff) << 8;
  _DAT_ffff00009086d294 = uVar1 >> 0x10 | uVar1 << 0x10;
  uVar1 = (_DAT_ffff00009086d070 & 0xff00ff00) >> 8 | (_DAT_ffff00009086d070 & 0xff00ff) << 8;
  _DAT_ffff00009086d298 = uVar1 >> 0x10 | uVar1 << 0x10;
  uVar1 = (_DAT_ffff00009086d074 & 0xff00ff00) >> 8 | (_DAT_ffff00009086d074 & 0xff00ff) << 8;
  _DAT_ffff00009086d29c = uVar1 >> 0x10 | uVar1 << 0x10;
  uVar1 = (_DAT_ffff00009086d078 & 0xff00ff00) >> 8 | (_DAT_ffff00009086d078 & 0xff00ff) << 8;
  _DAT_ffff00009086d2a0 = uVar1 >> 0x10 | uVar1 << 0x10;
  _DAT_ffff00009086d2a4 = 1;
  iVar4 = FUN_ffff00009076e40c(param_1,iVar3,s_ch_cnt_ffff0000907f5fd2,&iStack_44,4);
  if (iVar4 < 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_common_emi_emi_ffff0000907dc46a,0xea,&DAT_ffff0000907d24c9);
  }
  iVar4 = FUN_ffff00009076e40c(param_1,iVar3,s_rk_cnt_ffff0000907d76e8,&local_48,4);
  if (iVar4 < 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_common_emi_emi_ffff0000907dc46a,0xee,&DAT_ffff0000907d24c9);
  }
  iVar4 = FUN_ffff00009076e40c(param_1,iVar3,s_rk_size_ffff0000907f96a8,&local_58,0x10);
  if (iVar4 < 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_common_emi_emi_ffff0000907dc46a,0xf2,&DAT_ffff0000907d24c9);
  }
  iVar4 = FUN_ffff00009076e40c(param_1,iVar3,s_a2d_hash_ffff0000907fce47,&DAT_ffff00009086d278,4);
  if (iVar4 < 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_common_emi_emi_ffff0000907dc46a,0xf7,&DAT_ffff0000907d24c9);
  }
  iVar4 = FUN_ffff00009076e40c(param_1,iVar3,s_a2d_disph_ffff0000907f43fc,&DAT_ffff00009086d27c,4);
  if (iVar4 < 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_common_emi_emi_ffff0000907dc46a,0xfa,&DAT_ffff0000907d24c9);
  }
  iVar4 = FUN_ffff00009076e40c
                    (param_1,iVar3,s_a2d_emi_cen_ffff0000907d76f7,&DAT_ffff00009086d280,0x18);
  if (iVar4 < 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_common_emi_emi_ffff0000907dc46a,0xfd,&DAT_ffff0000907d24c9);
  }
  iVar4 = FUN_ffff00009076e40c
                    (param_1,iVar3,s_a2d_emi_chn_ffff0000907f4406,&DAT_ffff00009086d298,0xc);
  if (iVar4 < 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_common_emi_emi_ffff0000907dc46a,0x100,&DAT_ffff0000907d24c9);
  }
  iVar3 = FUN_ffff00009076e40c
                    (param_1,iVar3,s_a2d_info_support_ffff0000907e0eb7,&DAT_ffff00009086d2a4,4);
  if (iVar3 < 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_common_emi_emi_ffff0000907dc46a,0x103,&DAT_ffff0000907d24c9);
  }
  iVar3 = FUN_ffff00009076d75c(param_1,s__soc_emiisu_ffff000090802173);
  if ((iVar3 < 0) && (iVar3 = FUN_ffff00009076d75c(param_1,s__emiisu_ffff0000907fce5c), iVar3 < 0))
  {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_common_emi_emi_ffff0000907dc46a,0x61,&DAT_ffff0000907d24c9);
  }
  uVar1 = (_DAT_ffff00009086d030 & 0xff00ff00) >> 8 | (_DAT_ffff00009086d030 & 0xff00ff) << 8;
  local_24 = uVar1 >> 0x10 | uVar1 << 0x10;
  uVar1 = (_DAT_ffff00009086d03c & 0xff00ff00) >> 8 | (_DAT_ffff00009086d03c & 0xff00ff) << 8;
  local_30 = uVar1 >> 0x10 | uVar1 << 0x10;
  uVar1 = (_DAT_ffff00009086d038 & 0xff00ff00) >> 8 | (_DAT_ffff00009086d038 & 0xff00ff) << 8;
  uStack_2c = uVar1 >> 0x10 | uVar1 << 0x10;
  uVar1 = (_DAT_ffff00009086d040 & 0xff00ff00) >> 8 | (_DAT_ffff00009086d040 & 0xff00ff) << 8;
  uStack_34 = uVar1 >> 0x10 | uVar1 << 0x10;
  uVar1 = (_DAT_ffff00009086d044 & 0xff00ff00) >> 8 | (_DAT_ffff00009086d044 & 0xff00ff) << 8;
  local_38 = uVar1 >> 0x10 | uVar1 << 0x10;
  uVar1 = (_DAT_ffff00009086d04c & 0xff00ff00) >> 8 | (_DAT_ffff00009086d04c & 0xff00ff) << 8;
  local_40 = uVar1 >> 0x10 | uVar1 << 0x10;
  uVar1 = (_DAT_ffff00009086d048 & 0xff00ff00) >> 8 | (_DAT_ffff00009086d048 & 0xff00ff) << 8;
  uStack_3c = uVar1 >> 0x10 | uVar1 << 0x10;
  iVar4 = FUN_ffff00009076e40c(param_1,iVar3,s_buf_size_ffff0000907ec16b,&local_24,4);
  if (iVar4 < 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_common_emi_emi_ffff0000907dc46a,0x78,&DAT_ffff0000907d24c9);
  }
  iVar4 = FUN_ffff00009076e40c(param_1,iVar3,s_buf_addr_ffff0000907d8fde,&local_30,8);
  if (iVar4 < 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_common_emi_emi_ffff0000907dc46a,0x7d,&DAT_ffff0000907d24c9);
  }
  iVar4 = FUN_ffff00009076e40c(param_1,iVar3,s_ver_addr_ffff0000907f4412,&local_38,8);
  if (iVar4 < 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_common_emi_emi_ffff0000907dc46a,0x82,&DAT_ffff0000907d24c9);
  }
  iVar3 = FUN_ffff00009076e40c(param_1,iVar3,s_con_addr_ffff0000907ea8fc,&local_40,8);
  if (-1 < iVar3) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04
            (s_platform_mediatek_common_emi_emi_ffff0000907dc46a,0x87,&DAT_ffff0000907d24c9);
}


