// ffff000090725088  size=1360  FUN_ffff000090725088

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090725088(long param_1)

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
  
  FUN_ffff0000907239a4(0xffff00009086d070);
  if (_DAT_ffff00009086d114 != 0x20190831) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_common_emi_emi_ffff0000907dc557,0xb6,
               s_emi_info_arg_magic_tail____0x201_ffff0000907f455a);
  }
  if (_DAT_ffff00009086d0e8 != 0x20221221) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_common_emi_emi_ffff0000907dc557,0xb8,
               s_emi_info_arg_a2d_info_a2d_magic___ffff0000907fcfc1);
  }
  if (param_1 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_common_emi_emi_ffff0000907dc557,0xbc,&DAT_ffff0000907d2506);
  }
  iVar3 = FUN_ffff00009076da24(param_1,s__soc_emicen_ffff0000907fcff8);
  if (iVar3 < 0) {
    iVar3 = FUN_ffff00009076da24(param_1,s__emicen_ffff0000907fee11);
    if (iVar3 < 0) {
      iVar3 = FUN_ffff00009076da24(param_1,0);
      if (iVar3 < 0) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_platform_mediatek_common_emi_emi_ffff0000907dc557,0xc5,&DAT_ffff0000907d2506);
      }
    }
  }
  iStack_44 = (uint)DAT_ffff00009086d072 << 0x18;
  local_48 = (uint)DAT_ffff00009086d073 << 0x18;
  if (DAT_ffff00009086d073 == 0) {
    uStack_4c = 0;
    local_58 = 0;
    local_50 = 0;
  }
  else {
    uVar1 = (_DAT_ffff00009086d07c & 0xff00ff00) >> 8 | (_DAT_ffff00009086d07c & 0xff00ff) << 8;
    uVar2 = (_DAT_ffff00009086d078 & 0xff00ff00) >> 8 | (_DAT_ffff00009086d078 & 0xff00ff) << 8;
    local_58 = CONCAT44(uVar2 >> 0x10 | uVar2 << 0x10,uVar1 >> 0x10 | uVar1 << 0x10);
    if (DAT_ffff00009086d073 == 1) {
      local_50 = 0;
      uStack_4c = 0;
    }
    else {
      uVar1 = (_DAT_ffff00009086d084 & 0xff00ff00) >> 8 | (_DAT_ffff00009086d084 & 0xff00ff) << 8;
      local_50 = uVar1 >> 0x10 | uVar1 << 0x10;
      uVar1 = (_DAT_ffff00009086d080 & 0xff00ff00) >> 8 | (_DAT_ffff00009086d080 & 0xff00ff) << 8;
      uStack_4c = uVar1 >> 0x10 | uVar1 << 0x10;
    }
  }
  _DAT_ffff00009086d310 = (_DAT_ffff00009086d0ec & 0xf) << 0x18;
  _DAT_ffff00009086d314 = (_DAT_ffff00009086d0ec & 0xf0) << 0x14;
  uVar1 = (_DAT_ffff00009086d0f0 & 0xff00ff00) >> 8 | (_DAT_ffff00009086d0f0 & 0xff00ff) << 8;
  _DAT_ffff00009086d318 = uVar1 >> 0x10 | uVar1 << 0x10;
  uVar1 = (_DAT_ffff00009086d0f4 & 0xff00ff00) >> 8 | (_DAT_ffff00009086d0f4 & 0xff00ff) << 8;
  _DAT_ffff00009086d31c = uVar1 >> 0x10 | uVar1 << 0x10;
  uVar1 = (_DAT_ffff00009086d100 & 0xff00ff00) >> 8 | (_DAT_ffff00009086d100 & 0xff00ff) << 8;
  _DAT_ffff00009086d328 = uVar1 >> 0x10 | uVar1 << 0x10;
  uVar1 = (_DAT_ffff00009086d0f8 & 0xff00ff00) >> 8 | (_DAT_ffff00009086d0f8 & 0xff00ff) << 8;
  _DAT_ffff00009086d320 = uVar1 >> 0x10 | uVar1 << 0x10;
  uVar1 = (_DAT_ffff00009086d0fc & 0xff00ff00) >> 8 | (_DAT_ffff00009086d0fc & 0xff00ff) << 8;
  _DAT_ffff00009086d324 = uVar1 >> 0x10 | uVar1 << 0x10;
  uVar1 = (_DAT_ffff00009086d104 & 0xff00ff00) >> 8 | (_DAT_ffff00009086d104 & 0xff00ff) << 8;
  _DAT_ffff00009086d32c = uVar1 >> 0x10 | uVar1 << 0x10;
  uVar1 = (_DAT_ffff00009086d108 & 0xff00ff00) >> 8 | (_DAT_ffff00009086d108 & 0xff00ff) << 8;
  _DAT_ffff00009086d330 = uVar1 >> 0x10 | uVar1 << 0x10;
  uVar1 = (_DAT_ffff00009086d10c & 0xff00ff00) >> 8 | (_DAT_ffff00009086d10c & 0xff00ff) << 8;
  _DAT_ffff00009086d334 = uVar1 >> 0x10 | uVar1 << 0x10;
  uVar1 = (_DAT_ffff00009086d110 & 0xff00ff00) >> 8 | (_DAT_ffff00009086d110 & 0xff00ff) << 8;
  _DAT_ffff00009086d338 = uVar1 >> 0x10 | uVar1 << 0x10;
  _DAT_ffff00009086d33c = 1;
  iVar4 = FUN_ffff00009076e6d4(param_1,iVar3,s_ch_cnt_ffff0000907f6173,&iStack_44,4);
  if (iVar4 < 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_common_emi_emi_ffff0000907dc557,0xea,&DAT_ffff0000907d2506);
  }
  iVar4 = FUN_ffff00009076e6d4(param_1,iVar3,s_rk_cnt_ffff0000907d777e,&local_48,4);
  if (iVar4 < 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_common_emi_emi_ffff0000907dc557,0xee,&DAT_ffff0000907d2506);
  }
  iVar4 = FUN_ffff00009076e6d4(param_1,iVar3,s_rk_size_ffff0000907f9844,&local_58,0x10);
  if (iVar4 < 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_common_emi_emi_ffff0000907dc557,0xf2,&DAT_ffff0000907d2506);
  }
  iVar4 = FUN_ffff00009076e6d4(param_1,iVar3,s_a2d_hash_ffff0000907fcfef,&DAT_ffff00009086d310,4);
  if (iVar4 < 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_common_emi_emi_ffff0000907dc557,0xf7,&DAT_ffff0000907d2506);
  }
  iVar4 = FUN_ffff00009076e6d4(param_1,iVar3,s_a2d_disph_ffff0000907f4580,&DAT_ffff00009086d314,4);
  if (iVar4 < 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_common_emi_emi_ffff0000907dc557,0xfa,&DAT_ffff0000907d2506);
  }
  iVar4 = FUN_ffff00009076e6d4
                    (param_1,iVar3,s_a2d_emi_cen_ffff0000907d778d,&DAT_ffff00009086d318,0x18);
  if (iVar4 < 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_common_emi_emi_ffff0000907dc557,0xfd,&DAT_ffff0000907d2506);
  }
  iVar4 = FUN_ffff00009076e6d4
                    (param_1,iVar3,s_a2d_emi_chn_ffff0000907f458a,&DAT_ffff00009086d330,0xc);
  if (iVar4 < 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_common_emi_emi_ffff0000907dc557,0x100,&DAT_ffff0000907d2506);
  }
  iVar3 = FUN_ffff00009076e6d4
                    (param_1,iVar3,s_a2d_info_support_ffff0000907e0f9f,&DAT_ffff00009086d33c,4);
  if (iVar3 < 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_common_emi_emi_ffff0000907dc557,0x103,&DAT_ffff0000907d2506);
  }
  iVar3 = FUN_ffff00009076da24(param_1,s__soc_emiisu_ffff000090802377);
  if ((iVar3 < 0) && (iVar3 = FUN_ffff00009076da24(param_1,s__emiisu_ffff0000907fd004), iVar3 < 0))
  {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_common_emi_emi_ffff0000907dc557,0x61,&DAT_ffff0000907d2506);
  }
  uVar1 = (_DAT_ffff00009086d0c8 & 0xff00ff00) >> 8 | (_DAT_ffff00009086d0c8 & 0xff00ff) << 8;
  local_24 = uVar1 >> 0x10 | uVar1 << 0x10;
  uVar1 = (_DAT_ffff00009086d0d4 & 0xff00ff00) >> 8 | (_DAT_ffff00009086d0d4 & 0xff00ff) << 8;
  local_30 = uVar1 >> 0x10 | uVar1 << 0x10;
  uVar1 = (_DAT_ffff00009086d0d0 & 0xff00ff00) >> 8 | (_DAT_ffff00009086d0d0 & 0xff00ff) << 8;
  uStack_2c = uVar1 >> 0x10 | uVar1 << 0x10;
  uVar1 = (_DAT_ffff00009086d0d8 & 0xff00ff00) >> 8 | (_DAT_ffff00009086d0d8 & 0xff00ff) << 8;
  uStack_34 = uVar1 >> 0x10 | uVar1 << 0x10;
  uVar1 = (_DAT_ffff00009086d0dc & 0xff00ff00) >> 8 | (_DAT_ffff00009086d0dc & 0xff00ff) << 8;
  local_38 = uVar1 >> 0x10 | uVar1 << 0x10;
  uVar1 = (_DAT_ffff00009086d0e4 & 0xff00ff00) >> 8 | (_DAT_ffff00009086d0e4 & 0xff00ff) << 8;
  local_40 = uVar1 >> 0x10 | uVar1 << 0x10;
  uVar1 = (_DAT_ffff00009086d0e0 & 0xff00ff00) >> 8 | (_DAT_ffff00009086d0e0 & 0xff00ff) << 8;
  uStack_3c = uVar1 >> 0x10 | uVar1 << 0x10;
  iVar4 = FUN_ffff00009076e6d4(param_1,iVar3,s_buf_size_ffff0000907ec305,&local_24,4);
  if (iVar4 < 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_common_emi_emi_ffff0000907dc557,0x78,&DAT_ffff0000907d2506);
  }
  iVar4 = FUN_ffff00009076e6d4(param_1,iVar3,s_buf_addr_ffff0000907d9075,&local_30,8);
  if (iVar4 < 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_common_emi_emi_ffff0000907dc557,0x7d,&DAT_ffff0000907d2506);
  }
  iVar4 = FUN_ffff00009076e6d4(param_1,iVar3,s_ver_addr_ffff0000907f4596,&local_38,8);
  if (iVar4 < 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_common_emi_emi_ffff0000907dc557,0x82,&DAT_ffff0000907d2506);
  }
  iVar3 = FUN_ffff00009076e6d4(param_1,iVar3,s_con_addr_ffff0000907eaa96,&local_40,8);
  if (-1 < iVar3) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_platform_mediatek_common_emi_emi_ffff0000907dc557,0x87,&DAT_ffff0000907d2506);
}


