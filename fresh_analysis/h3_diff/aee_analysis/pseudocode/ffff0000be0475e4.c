
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000be0475e4(undefined8 param_1)

{
  int iVar1;
  int iVar2;
  int iVar3;
  uint uVar4;
  undefined4 uVar5;
  ulong uVar6;
  undefined8 uVar7;
  long lVar8;
  undefined8 local_c8;
  long local_c0;
  undefined4 local_b8;
  undefined4 uStack_b4;
  undefined4 local_b0;
  int iStack_ac;
  undefined8 uStack_a8;
  undefined4 local_a0;
  undefined4 uStack_9c;
  undefined4 uStack_98;
  undefined4 local_94;
  undefined8 local_90;
  undefined8 local_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  long local_38;
  
  local_38 = 0;
  local_c8 = 0;
  local_c0 = 0;
  FUN_ffff0000be049d10();
  _DAT_ffff000032aa0f08 = 0;
  _DAT_ffff000032aa0f0c = 0;
  _DAT_ffff000032ab0f08 = 0;
  _DAT_ffff000032ab0f0c = 0;
  _DAT_ffff000032ac0f0c = 0;
  _DAT_ffff000032ac0f10 = 0;
  _DAT_ffff000032ac0f14 = 0;
  _DAT_ffff000032ad0f0c = 0;
  _DAT_ffff000032ad0f10 = 0;
  _DAT_ffff000032ad0f14 = 0;
  iVar1 = FUN_ffff0000be048404(0,param_1);
  if (iVar1 != 0) {
    FUN_ffff0000be05fd98(s_lcm_not_found_ffff0000be0bb308);
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000be05ee98
              (s_platform_mediatek_mt6991_disp_mt_ffff0000be0c3343,499,&DAT_ffff0000be0a553d);
  }
  DAT_ffff0000be12605c = FUN_ffff0000be0490ac();
  iVar1 = FUN_ffff0000be065774();
  if (iVar1 == 0xfc11) {
    iVar1 = FUN_ffff0000be0490ec();
    DAT_ffff0000be12605c = DAT_ffff0000be12605c + iVar1;
  }
  uVar6 = FUN_ffff0000be063c7c();
  if ((uVar6 & 1) == 0) {
    local_c0 = FUN_ffff0000be01c3b4
                         (DAT_ffff0000be12605c + 0xffffU & 0xffff0000,0x10000,0xffffffff,0,1,
                          s_framebuffer_ffff0000be0a7372);
    if (local_c0 == 0) {
      FUN_ffff0000be05fd98
                (s___s____d__mblock_allocation_fail_ffff0000be0b234f,s_disp_init_ffff0000be0a856d,
                 0x20b);
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000be05ee98
                (s_platform_mediatek_mt6991_disp_mt_ffff0000be0c3343,0x20c,&DAT_ffff0000be0a553d);
    }
  }
  else {
    iVar1 = FUN_ffff0000be065774();
    if (iVar1 != 0xfc11) {
      FUN_ffff0000be05fd98(s_not_in_full_dump_skip_disp_init_ffff0000be0adcb5);
      return;
    }
    iVar1 = FUN_ffff0000be065530(s_framebuffer_ffff0000be0a7372,&local_c0,&local_c8);
    if (iVar1 < 0) {
      FUN_ffff0000be05fd98
                (s___s____d__get_reserved_shared_dr_ffff0000be0bb317,s_disp_init_ffff0000be0a856d,
                 0x201);
    }
    if (local_c0 == 0) {
      FUN_ffff0000be05fd98
                (s___s____d_fb_mblock_query_failed_ffff0000be0bd712,s_disp_init_ffff0000be0a856d,
                 0x204);
      return;
    }
  }
  DAT_ffff0000be126068 = 1;
  DAT_ffff0000be126060 = local_c0;
  iVar1 = FUN_ffff0000be05d9a8
                    (PTR_DAT_ffff0000be0f5570,s_vm_framebuffer_ffff0000be0c336f,
                     DAT_ffff0000be12605c + 0xffffU & 0xffff0000,&local_38,0xc,local_c0,0,0);
  if (iVar1 == 0) {
    FUN_ffff0000be05fd98
              (s__s___d__fb_base_0x_llx__fb_va_0x_ffff0000be0b467b,s_disp_init_ffff0000be0a856d,
               0x21e,DAT_ffff0000be126060,local_38,DAT_ffff0000be12605c);
    lVar8 = local_38;
    DAT_ffff0000be126080 = DAT_ffff0000be126060;
    DAT_ffff0000be12607c = (int)DAT_ffff0000be126060;
    DAT_ffff0000be126070 = local_38;
    uVar6 = FUN_ffff0000be063c7c();
    if (((uVar6 & 1) != 0) && (iVar1 = FUN_ffff0000be065774(), iVar1 == 0xfc11)) {
      uVar6 = FUN_ffff0000be0490ac();
      DAT_ffff0000be126070 = lVar8 + (uVar6 & 0xffffffff);
    }
    FUN_ffff0000be05fd98
              (s_fb_va__0x_llx__fb_pa__0x_08x__fb_ffff0000be0bb340,DAT_ffff0000be126070,
               DAT_ffff0000be12607c,DAT_ffff0000be126080);
    iVar1 = FUN_ffff0000be048670();
    iVar2 = FUN_ffff0000be0487d0();
    iVar3 = FUN_ffff0000be048884();
    DAT_ffff0000be126078 = (uint)(iVar2 * iVar1 * iVar3) >> 3;
    iVar1 = DAT_ffff0000be12607c + DAT_ffff0000be126078;
    uVar6 = FUN_ffff0000be063c7c();
    if (((uVar6 & 1) != 0) &&
       (iVar3 = FUN_ffff0000be065774(), iVar2 = DAT_ffff0000be12607c, iVar3 == 0xfc11)) {
      iVar1 = FUN_ffff0000be0490ac();
      iVar1 = iVar1 + iVar2;
    }
    FUN_ffff0000be047eb0(param_1);
    uVar4 = FUN_ffff0000be047da8();
    iVar2 = FUN_ffff0000be047d98();
    DAT_ffff0000be125fd8 = uVar4 & 0xffff | iVar2 << 0x10;
    uVar7 = FUN_ffff0000be047dfc();
    uVar4 = FUN_ffff0000be0611dc(&DAT_ffff0000be125fdc,0x7f,&DAT_ffff0000be0ac42b,uVar7);
    if (0x7f < uVar4) {
      FUN_ffff0000be05fd98
                (s__s__snprintf_failed___d_ffff0000be0b2373,s_mt_disp_init_ffff0000be0b6c39,uVar4);
    }
    DAT_ffff0000be12605b = 0;
    FUN_ffff0000be05fd98
              (s__s__d__va_0x_lx__found__d__lcm___ffff0000be0a4e1e,s_mt_disp_init_ffff0000be0b6c39,
               0x145,lVar8,DAT_ffff0000be125fd8,&DAT_ffff0000be125fdc);
    uVar6 = FUN_ffff0000be063c7c();
    if (((uVar6 & 1) == 0) ||
       (iVar2 = FUN_ffff0000be065774(), lVar8 = DAT_ffff0000be126070, iVar2 != 0xfc11)) {
      lVar8 = DAT_ffff0000be126070;
      uVar5 = FUN_ffff0000be0490ac();
    }
    else {
      uVar5 = FUN_ffff0000be0490ec();
    }
    FUN_ffff0000be060ae0(lVar8,0,uVar5);
    local_60 = 0;
    uStack_58 = 0;
    local_70 = 0;
    uStack_68 = 0;
    local_80 = 0;
    uStack_78 = 0;
    local_90 = 0;
    local_88 = 0;
    local_a0 = 0;
    uStack_9c = 0;
    uStack_98 = 0;
    local_94 = 0;
    local_b0 = 0;
    iStack_ac = 0;
    uStack_a8 = 0;
    local_b8 = 0;
    uStack_b4 = 0;
    FUN_ffff0000be060ae0(&local_b8,0,0x68);
    local_b8 = 1;
    uVar6 = FUN_ffff0000be063c7c();
    if (((uVar6 & 1) != 0) && (iVar2 = FUN_ffff0000be065774(), iVar2 == 0xfc11)) {
      local_b8 = 0;
    }
    uStack_9c = 0;
    uStack_98 = 0;
    uStack_b4 = 1;
    local_b0 = 0x1002002;
    iStack_ac = iVar1;
    local_94 = FUN_ffff0000be0485c4();
    uVar6 = FUN_ffff0000be063c7c();
    if (((uVar6 & 1) == 0) || (iVar1 = FUN_ffff0000be065774(), iVar1 != 0xfc11)) {
      iVar1 = FUN_ffff0000be048724();
    }
    else {
      iVar2 = FUN_ffff0000be048724();
      iVar1 = iVar2 + 7;
      if (-1 < iVar2) {
        iVar1 = iVar2;
      }
      iVar1 = iVar1 >> 3;
    }
    local_90 = CONCAT44(local_90._4_4_,iVar1);
    iVar1 = FUN_ffff0000be0485c4();
    local_88 = 0;
    local_90 = CONCAT44(iVar1 << 2,(undefined4)local_90);
    uVar5 = FUN_ffff0000be0485c4();
    local_80 = CONCAT44(local_80._4_4_,uVar5);
    uVar6 = FUN_ffff0000be063c7c();
    if (((uVar6 & 1) == 0) || (iVar1 = FUN_ffff0000be065774(), iVar1 != 0xfc11)) {
      iVar1 = FUN_ffff0000be048724();
    }
    else {
      iVar2 = FUN_ffff0000be048724();
      iVar1 = iVar2 + 7;
      if (-1 < iVar2) {
        iVar1 = iVar2;
      }
      iVar1 = iVar1 >> 3;
    }
    local_80 = CONCAT44(iVar1,(undefined4)local_80);
    local_70 = CONCAT35(local_70._5_3_,0xff00000001);
    FUN_ffff0000be048b6c(&local_b8);
    FUN_ffff0000be060ae0(&local_b8,0,0x68);
    local_b8 = 0;
    uStack_b4 = 1;
    uVar6 = FUN_ffff0000be063c7c();
    if (((uVar6 & 1) != 0) && (iVar1 = FUN_ffff0000be065774(), iVar1 == 0xfc11)) {
      uStack_b4 = 0;
    }
    uStack_9c = 0;
    uStack_98 = 0;
    local_b0 = 0x1002002;
    iStack_ac = DAT_ffff0000be12607c;
    local_94 = FUN_ffff0000be0485c4();
    uVar6 = FUN_ffff0000be063c7c();
    if (((uVar6 & 1) == 0) || (iVar1 = FUN_ffff0000be065774(), iVar1 != 0xfc11)) {
      iVar1 = FUN_ffff0000be048724();
    }
    else {
      iVar2 = FUN_ffff0000be048724();
      iVar1 = iVar2 + 7;
      if (-1 < iVar2) {
        iVar1 = iVar2;
      }
      iVar1 = iVar1 >> 3;
    }
    local_90 = CONCAT44(local_90._4_4_,iVar1);
    iVar1 = FUN_ffff0000be048670();
    local_88 = 0;
    local_90 = CONCAT44(iVar1 << 2,(undefined4)local_90);
    uVar5 = FUN_ffff0000be0485c4();
    local_80 = CONCAT44(local_80._4_4_,uVar5);
    uVar6 = FUN_ffff0000be063c7c();
    if (((uVar6 & 1) == 0) || (iVar1 = FUN_ffff0000be065774(), iVar1 != 0xfc11)) {
      iVar1 = FUN_ffff0000be048724();
    }
    else {
      iVar2 = FUN_ffff0000be048724();
      iVar1 = iVar2 + 7;
      if (-1 < iVar2) {
        iVar1 = iVar2;
      }
      iVar1 = iVar1 >> 3;
    }
    local_80 = CONCAT44(iVar1,(undefined4)local_80);
    local_70 = CONCAT35(local_70._5_3_,0xff00000001);
    FUN_ffff0000be048b6c(&local_b8);
    FUN_ffff0000be05fd98(s_bl2_ext_write_parameter_to_lk_ffff0000be0b238c);
    FUN_ffff0000be05fd98(s_islcmfound____x_ffff0000be0a8577,DAT_ffff0000be125fd8);
    FUN_ffff0000be05fd98(s_lcm_name____s_ffff0000be0b23ab,&DAT_ffff0000be125fdc);
    FUN_ffff0000be05fd98
              (s___s___check_dsi_state__0x_x__0x__ffff0000be0aec1c,s_mt_disp_init_ffff0000be0b6c39,
               _DAT_ffff000032490400,_DAT_ffff000032490034);
    return;
  }
  FUN_ffff0000be05fd98(s_fastlogo_vmm_alloc_physical_fail_ffff0000be0b8f45);
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000be05ee98
            (s_platform_mediatek_mt6991_disp_mt_ffff0000be0c3343,0x21a,&DAT_ffff0000be0a553d);
}


