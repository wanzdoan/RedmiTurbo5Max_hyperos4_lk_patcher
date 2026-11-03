
int FUN_ffff0000907cd68c(int param_1,undefined8 param_2,uint param_3)

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
  
  FUN_ffff00009077e11c(s__s_IN_ffff0000907dd40e,s_write_to_rpmb_ffff0000907d0825);
  iVar1 = FUN_ffff00009073527c();
  if (iVar1 == 2) {
    uVar2 = FUN_ffff000090796b1c();
    lVar3 = FUN_ffff000090796b40(0);
    if (uVar2 == 0x400000) {
      iVar4 = 0x3fc0;
    }
    else {
      if (uVar2 != 0x1000000) {
        FUN_ffff00009077e11c(s_Error___wrong_rpmb_size___0x_x_ffff0000907df25d,uVar2 & 0xffffffff);
        iVar1 = 7;
        goto LAB_ffff0000907cd794;
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
      iVar1 = FUN_ffff0000907cd9cc(iVar4 + param_1,&local_140);
      if (iVar1 == 0) {
        FUN_ffff00009077eb20(&local_140,param_2,param_3);
        iVar1 = FUN_ffff00009073527c();
        if (iVar1 == 2) {
          iVar1 = FUN_ffff00009079652c(0,iVar4 + param_1,&local_140,0x100,&local_24);
          if (iVar1 == 7) {
            FUN_ffff00009077e11c(s_Error___rpmb_hmac_key_hasn_t_bee_ffff0000907dbe2b);
            iVar1 = 6;
          }
          else {
            if (iVar1 == 0 && local_24 == 0) {
              FUN_ffff00009077e11c(s__s_OUT_ffff0000907fa94e,s_write_to_rpmb_ffff0000907d0825);
              return 0;
            }
            FUN_ffff00009077e11c(s_Error___rpmb_write_failed__d_ffff0000907e2429,iVar1);
            iVar1 = 4;
          }
        }
        else {
          FUN_ffff00009077e11c(s_Error___no_ufs_or_emmc_ffff000090800245);
          iVar1 = 5;
        }
      }
      else {
        FUN_ffff00009077e11c(s_Error___rpmb_read_fail_ffff0000907f91e5);
      }
    }
    else {
      FUN_ffff00009077e11c(s_Error___wrong_buf_len____d_ffff0000907d8afa,param_3);
      iVar1 = 1;
    }
    FUN_ffff00009077e11c(s_Info___rpmb_write_ret_is__d_ffff0000907e3fba,iVar1);
  }
  else {
    iVar1 = 5;
LAB_ffff0000907cd794:
    FUN_ffff00009077e11c(s_Error___rpmb_init_misecids_base__ffff0000907fc9da);
  }
  return iVar1;
}


