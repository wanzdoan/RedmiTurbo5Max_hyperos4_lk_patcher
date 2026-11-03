// File offset entry: 0x3dd94

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000b803dd94(undefined8 param_1)

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
  FUN_ffff0000b80404dc();
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
  iVar1 = FUN_ffff0000b803ebc0(0,param_1);
  if (iVar1 != 0) {
    FUN_ffff0000b804beb0(s_lcm_not_found_ffff0000b80973ac);
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b3a4
              (s_platform_mediatek_mt6991_disp_mt_ffff0000b809db72,499,&DAT_ffff0000b8085ec6);
  }
  DAT_ffff0000b81287fc = FUN_ffff0000b803f9c0();
  iVar1 = FUN_ffff0000b804fef8();
  if (iVar1 == 0xfc11) {
    iVar1 = FUN_ffff0000b803fa00();
    DAT_ffff0000b81287fc = DAT_ffff0000b81287fc + iVar1;
  }
  uVar6 = FUN_ffff0000b804f0c8();
  if ((uVar6 & 1) == 0) {
    local_c0 = FUN_ffff0000b800c9f4
                         (DAT_ffff0000b81287fc + 0xffffU & 0xffff0000,0x10000,0xffffffff,0,1,
                          s_framebuffer_ffff0000b808766d);
    if (local_c0 == 0) {
      FUN_ffff0000b804beb0
                (s___s____d__mblock_allocation_fail_ffff0000b809000a,s_disp_init_ffff0000b808841d,
                 0x20b);
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000b804b3a4
                (s_platform_mediatek_mt6991_disp_mt_ffff0000b809db72,0x20c,&DAT_ffff0000b8085ec6);
    }
  }
  else {
    iVar1 = FUN_ffff0000b804fef8();
    if (iVar1 != 0xfc11) {
      FUN_ffff0000b804beb0(s_not_in_full_dump_skip_disp_init_ffff0000b808c5a1);
      return;
    }
    iVar1 = FUN_ffff0000b804fd24(s_framebuffer_ffff0000b808766d,&local_c0,&local_c8);
    if (iVar1 < 0) {
      FUN_ffff0000b804beb0
                (s___s____d__get_reserved_shared_dr_ffff0000b80973bb,s_disp_init_ffff0000b808841d,
                 0x201);
    }
    if (local_c0 == 0) {
      FUN_ffff0000b804beb0
                (s___s____d_fb_mblock_query_failed_ffff0000b8099064,s_disp_init_ffff0000b808841d,
                 0x204);
      return;
    }
  }
  DAT_ffff0000b8128808 = 1;
  DAT_ffff0000b8128800 = local_c0;
  iVar1 = FUN_ffff0000b804a16c
                    (PTR_DAT_ffff0000b8107610,s_vm_framebuffer_ffff0000b809db9e,
                     DAT_ffff0000b81287fc + 0xffffU & 0xffff0000,&local_38,0xc,local_c0,0,0);
  if (iVar1 == 0) {
    FUN_ffff0000b804beb0
              (s__s___d__fb_base_0x_llx__fb_va_0x_ffff0000b8091b00,s_disp_init_ffff0000b808841d,
               0x21e,DAT_ffff0000b8128800,local_38,DAT_ffff0000b81287fc);
    lVar8 = local_38;
    DAT_ffff0000b8128820 = DAT_ffff0000b8128800;
    DAT_ffff0000b812881c = (int)DAT_ffff0000b8128800;
    DAT_ffff0000b8128810 = local_38;
    uVar6 = FUN_ffff0000b804f0c8();
    if (((uVar6 & 1) != 0) && (iVar1 = FUN_ffff0000b804fef8(), iVar1 == 0xfc11)) {
      uVar6 = FUN_ffff0000b803f9c0();
      DAT_ffff0000b8128810 = lVar8 + (uVar6 & 0xffffffff);
    }
    FUN_ffff0000b804beb0
              (s_fb_va__0x_llx__fb_pa__0x_08x__fb_ffff0000b80973e4,DAT_ffff0000b8128810,
               DAT_ffff0000b812881c,DAT_ffff0000b8128820);
    iVar1 = FUN_ffff0000b803ee2c();
    iVar2 = FUN_ffff0000b803ef8c();
    iVar3 = FUN_ffff0000b803f040();
    DAT_ffff0000b8128818 = (uint)(iVar2 * iVar1 * iVar3) >> 3;
    iVar1 = DAT_ffff0000b812881c + DAT_ffff0000b8128818;
    uVar6 = FUN_ffff0000b804f0c8();
    if (((uVar6 & 1) != 0) &&
       (iVar3 = FUN_ffff0000b804fef8(), iVar2 = DAT_ffff0000b812881c, iVar3 == 0xfc11)) {
      iVar1 = FUN_ffff0000b803f9c0();
      iVar1 = iVar1 + iVar2;
    }
    FUN_ffff0000b803e66c(param_1);
    uVar4 = FUN_ffff0000b803e564();
    iVar2 = FUN_ffff0000b803e554();
    DAT_ffff0000b8128778 = uVar4 & 0xffff | iVar2 << 0x10;
    uVar7 = FUN_ffff0000b803e5b8();
    uVar4 = FUN_ffff0000b804d2c0(&DAT_ffff0000b812877c,0x7f,&DAT_ffff0000b808b4ba,uVar7);
    if (0x7f < uVar4) {
      FUN_ffff0000b804beb0
                (s__s__snprintf_failed___d_ffff0000b809002e,s_mt_disp_init_ffff0000b8093b31,uVar4);
    }
    DAT_ffff0000b81287fb = 0;
    FUN_ffff0000b804beb0
              (s__s__d__va_0x_lx__found__d__lcm___ffff0000b80858fc,s_mt_disp_init_ffff0000b8093b31,
               0x145,lVar8,DAT_ffff0000b8128778,&DAT_ffff0000b812877c);
    uVar6 = FUN_ffff0000b804f0c8();
    if (((uVar6 & 1) == 0) ||
       (iVar2 = FUN_ffff0000b804fef8(), lVar8 = DAT_ffff0000b8128810, iVar2 != 0xfc11)) {
      lVar8 = DAT_ffff0000b8128810;
      uVar5 = FUN_ffff0000b803f9c0();
    }
    else {
      uVar5 = FUN_ffff0000b803fa00();
    }
    FUN_ffff0000b804cbe4(lVar8,0,uVar5);
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
    FUN_ffff0000b804cbe4(&local_b8,0,0x68);
    local_b8 = 1;
    uVar6 = FUN_ffff0000b804f0c8();
    if (((uVar6 & 1) != 0) && (iVar2 = FUN_ffff0000b804fef8(), iVar2 == 0xfc11)) {
      local_b8 = 0;
    }
    uStack_9c = 0;
    uStack_98 = 0;
    uStack_b4 = 1;
    local_b0 = 0x1002002;
    iStack_ac = iVar1;
    local_94 = FUN_ffff0000b803ed80();
    uVar6 = FUN_ffff0000b804f0c8();
    if (((uVar6 & 1) == 0) || (iVar1 = FUN_ffff0000b804fef8(), iVar1 != 0xfc11)) {
      iVar1 = FUN_ffff0000b803eee0();
    }
    else {
      iVar2 = FUN_ffff0000b803eee0();
      iVar1 = iVar2 + 7;
      if (-1 < iVar2) {
        iVar1 = iVar2;
      }
      iVar1 = iVar1 >> 3;
    }
    local_90 = CONCAT44(local_90._4_4_,iVar1);
    iVar1 = FUN_ffff0000b803ed80();
    local_88 = 0;
    local_90 = CONCAT44(iVar1 << 2,(undefined4)local_90);
    uVar5 = FUN_ffff0000b803ed80();
    local_80 = CONCAT44(local_80._4_4_,uVar5);
    uVar6 = FUN_ffff0000b804f0c8();
    if (((uVar6 & 1) == 0) || (iVar1 = FUN_ffff0000b804fef8(), iVar1 != 0xfc11)) {
      iVar1 = FUN_ffff0000b803eee0();
    }
    else {
      iVar2 = FUN_ffff0000b803eee0();
      iVar1 = iVar2 + 7;
      if (-1 < iVar2) {
        iVar1 = iVar2;
      }
      iVar1 = iVar1 >> 3;
    }
    local_80 = CONCAT44(iVar1,(undefined4)local_80);
    local_70 = CONCAT35(local_70._5_3_,0xff00000001);
    FUN_ffff0000b803f328(&local_b8);
    FUN_ffff0000b804cbe4(&local_b8,0,0x68);
    local_b8 = 0;
    uStack_b4 = 1;
    uVar6 = FUN_ffff0000b804f0c8();
    if (((uVar6 & 1) != 0) && (iVar1 = FUN_ffff0000b804fef8(), iVar1 == 0xfc11)) {
      uStack_b4 = 0;
    }
    uStack_9c = 0;
    uStack_98 = 0;
    local_b0 = 0x1002002;
    iStack_ac = DAT_ffff0000b812881c;
    local_94 = FUN_ffff0000b803ed80();
    uVar6 = FUN_ffff0000b804f0c8();
    if (((uVar6 & 1) == 0) || (iVar1 = FUN_ffff0000b804fef8(), iVar1 != 0xfc11)) {
      iVar1 = FUN_ffff0000b803eee0();
    }
    else {
      iVar2 = FUN_ffff0000b803eee0();
      iVar1 = iVar2 + 7;
      if (-1 < iVar2) {
        iVar1 = iVar2;
      }
      iVar1 = iVar1 >> 3;
    }
    local_90 = CONCAT44(local_90._4_4_,iVar1);
    iVar1 = FUN_ffff0000b803ee2c();
    local_88 = 0;
    local_90 = CONCAT44(iVar1 << 2,(undefined4)local_90);
    uVar5 = FUN_ffff0000b803ed80();
    local_80 = CONCAT44(local_80._4_4_,uVar5);
    uVar6 = FUN_ffff0000b804f0c8();
    if (((uVar6 & 1) == 0) || (iVar1 = FUN_ffff0000b804fef8(), iVar1 != 0xfc11)) {
      iVar1 = FUN_ffff0000b803eee0();
    }
    else {
      iVar2 = FUN_ffff0000b803eee0();
      iVar1 = iVar2 + 7;
      if (-1 < iVar2) {
        iVar1 = iVar2;
      }
      iVar1 = iVar1 >> 3;
    }
    local_80 = CONCAT44(iVar1,(undefined4)local_80);
    local_70 = CONCAT35(local_70._5_3_,0xff00000001);
    FUN_ffff0000b803f328(&local_b8);
    FUN_ffff0000b804beb0(s_bl2_ext_write_parameter_to_lk_ffff0000b8090047);
    FUN_ffff0000b804beb0(s_islcmfound____x_ffff0000b8088427,DAT_ffff0000b8128778);
    FUN_ffff0000b804beb0(s_lcm_name____s_ffff0000b8090066,&DAT_ffff0000b812877c);
    FUN_ffff0000b804beb0
              (s___s___check_dsi_state__0x_x__0x__ffff0000b808d2ee,s_mt_disp_init_ffff0000b8093b31,
               _DAT_ffff000032490400,_DAT_ffff000032490034);
    return;
  }
  FUN_ffff0000b804beb0(s_fastlogo_vmm_alloc_physical_fail_ffff0000b80957b8);
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000b804b3a4
            (s_platform_mediatek_mt6991_disp_mt_ffff0000b809db72,0x21a,&DAT_ffff0000b8085ec6);
}


