
int FUN_ffff0000be0a233c(int param_1,undefined8 param_2,uint param_3)

{
  int iVar1;
  ulong uVar2;
  long lVar3;
  int iVar4;
  undefined8 local_140;
  undefined8 uStack_138;
  undefined8 local_130;
  undefined8 uStack_128;
  undefined8 local_120;
  undefined8 uStack_118;
  undefined8 local_110;
  undefined8 uStack_108;
  undefined8 local_100;
  undefined8 uStack_f8;
  undefined8 local_f0;
  undefined8 uStack_e8;
  undefined8 local_e0;
  undefined8 uStack_d8;
  undefined8 local_d0;
  undefined8 uStack_c8;
  undefined8 local_c0;
  undefined8 uStack_b8;
  undefined8 local_b0;
  undefined8 uStack_a8;
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  int local_24;
  
  FUN_ffff0000be05fd98(s__s_IN_ffff0000be0acfc9,s_write_to_rpmb_ffff0000be0a409a);
  iVar1 = FUN_ffff0000be028018();
  if (iVar1 == 2) {
    uVar2 = FUN_ffff0000be074290();
    lVar3 = FUN_ffff0000be0742b4(0);
    if (uVar2 == 0x400000) {
      iVar4 = 0x3fc0;
    }
    else {
      if (uVar2 != 0x1000000) {
        FUN_ffff0000be05fd98(s_Error___wrong_rpmb_size___0x_x_ffff0000be0ae27a,uVar2 & 0xffffffff);
        iVar1 = 7;
        goto LAB_ffff0000be0a2444;
      }
      iVar4 = (int)((ulong)(lVar3 + 0xffffffc000) >> 8);
    }
    local_24 = 0;
    local_50 = 0;
    uStack_48 = 0;
    local_60 = 0;
    uStack_58 = 0;
    local_70 = 0;
    uStack_68 = 0;
    local_80 = 0;
    uStack_78 = 0;
    local_90 = 0;
    uStack_88 = 0;
    local_a0 = 0;
    uStack_98 = 0;
    local_b0 = 0;
    uStack_a8 = 0;
    local_c0 = 0;
    uStack_b8 = 0;
    local_d0 = 0;
    uStack_c8 = 0;
    local_e0 = 0;
    uStack_d8 = 0;
    local_f0 = 0;
    uStack_e8 = 0;
    local_100 = 0;
    uStack_f8 = 0;
    local_110 = 0;
    uStack_108 = 0;
    local_120 = 0;
    uStack_118 = 0;
    local_130 = 0;
    uStack_128 = 0;
    local_140 = 0;
    uStack_138 = 0;
    if ((param_3 < 0x101) || ((param_3 & 0xff) == 0)) {
      iVar1 = FUN_ffff0000be0a267c(iVar4 + param_1,&local_140);
      if (iVar1 == 0) {
        FUN_ffff0000be0607b0(&local_140,param_2,param_3);
        iVar1 = FUN_ffff0000be028018();
        if (iVar1 == 2) {
          iVar1 = FUN_ffff0000be073ca0(0,iVar4 + param_1,&local_140,0x100,&local_24);
          if (iVar1 == 7) {
            FUN_ffff0000be05fd98(s_Error___rpmb_hmac_key_hasn_t_bee_ffff0000be0ac2e6);
            iVar1 = 6;
          }
          else {
            if (iVar1 == 0 && local_24 == 0) {
              FUN_ffff0000be05fd98(s__s_OUT_ffff0000be0c1274,s_write_to_rpmb_ffff0000be0a409a);
              return 0;
            }
            FUN_ffff0000be05fd98(s_Error___rpmb_write_failed__d_ffff0000be0b0786,iVar1);
            iVar1 = 4;
          }
        }
        else {
          FUN_ffff0000be05fd98(s_Error___no_ufs_or_emmc_ffff0000be0c4be1);
          iVar1 = 5;
        }
      }
      else {
        FUN_ffff0000be05fd98(s_Error___rpmb_read_fail_ffff0000be0c0299);
      }
    }
    else {
      FUN_ffff0000be05fd98(s_Error___wrong_buf_len____d_ffff0000be0a9dc9,param_3);
      iVar1 = 1;
    }
    FUN_ffff0000be05fd98(s_Info___rpmb_write_ret_is__d_ffff0000be0b18ad,iVar1);
  }
  else {
    iVar1 = 5;
LAB_ffff0000be0a2444:
    FUN_ffff0000be05fd98(s_Error___rpmb_init_misecids_base__ffff0000be0c2774);
  }
  return iVar1;
}


