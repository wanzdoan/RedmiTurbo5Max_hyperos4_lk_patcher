// File offset entry: 0x287e0

void FUN_ffff0000b80287e0(undefined8 *param_1,long param_2,long param_3)

{
  int iVar1;
  long lVar2;
  char *pcVar3;
  undefined8 local_130;
  undefined8 local_128;
  undefined8 local_120;
  undefined8 local_118;
  undefined8 local_110;
  undefined8 local_108;
  undefined8 uStack_100;
  undefined8 local_f8;
  undefined8 uStack_f0;
  undefined8 local_e8;
  undefined8 uStack_e0;
  undefined8 local_d8;
  undefined8 uStack_d0;
  undefined8 local_c8;
  undefined8 uStack_c0;
  undefined8 local_b8;
  undefined8 uStack_b0;
  undefined8 local_a8;
  undefined8 uStack_a0;
  undefined8 local_98;
  undefined8 uStack_90;
  undefined8 local_88;
  undefined8 uStack_80;
  undefined2 local_78;
  undefined4 local_70;
  undefined4 local_6c;
  undefined4 local_68;
  undefined4 local_64;
  undefined8 local_60;
  undefined8 uStack_58;
  
  if (param_1 == (undefined8 *)0x0) {
    pcVar3 = s_tee_boot_arg_addr_is_NULL__ffff0000b8088fd5;
  }
  else {
    FUN_ffff0000b804d070(param_1,0,0xa4);
    param_1[2] = param_2;
    param_1[3] = param_3 + -0x200000;
    *param_1 = 0xa4434d4254;
    param_1[1] = 0x10000;
    *(long *)((long)param_1 + 0x44) = param_3 + -0x200000 + param_2;
    *(undefined8 *)((long)param_1 + 0x4c) = 0x200000;
    *(undefined4 *)((long)param_1 + 0x54) = 0x100;
    lVar2 = FUN_ffff0000b800c9f4(0x400000,0x200000,0x100000000,0,0,s_tee_shm_ffff0000b808fb49);
    if (lVar2 != 0) {
      *(long *)((long)param_1 + 0x34) = lVar2;
      local_78 = 0;
      local_88 = 0;
      uStack_80 = 0;
      *(undefined8 *)((long)param_1 + 0x3c) = 0x400000;
      local_98 = 0;
      uStack_90 = 0;
      local_a8 = 0;
      uStack_a0 = 0;
      local_b8 = 0;
      uStack_b0 = 0;
      local_c8 = 0;
      uStack_c0 = 0;
      local_d8 = 0;
      uStack_d0 = 0;
      local_e8 = 0;
      uStack_e0 = 0;
      local_f8 = 0;
      uStack_f0 = 0;
      local_108 = 0;
      uStack_100 = 0;
      local_110 = 0;
      FUN_ffff0000b804d070(param_1 + 0x47,0,0x20);
      FUN_ffff0000b804d070(param_1 + 0x41,0,0x20);
      FUN_ffff0000b804d070(param_1 + 0x45,0,0x10);
      *(undefined4 *)(param_1 + 0x40) = 0x434d4254;
      *(undefined4 *)((long)param_1 + 0x204) = 0x10000;
      local_70 = thunk_FUN_ffff0000b802ce0c(0xc);
      local_6c = thunk_FUN_ffff0000b802ce0c(0xd);
      local_68 = thunk_FUN_ffff0000b802ce0c(0xe);
      local_64 = thunk_FUN_ffff0000b802ce0c(0xf);
      local_60 = 0;
      uStack_58 = 0;
      *(undefined4 *)(param_1 + 0x4b) = 0x10;
      FUN_ffff0000b804cd40(param_1 + 0x47,&local_70,0x10);
      *(undefined4 *)((long)param_1 + 0x25c) = 0x10;
      iVar1 = FUN_ffff0000b8052b84(param_1 + 0x45,0x10);
      if (iVar1 == 0) {
        *(undefined4 *)(param_1 + 0x4c) = 4;
        iVar1 = FUN_ffff0000b8058540(&local_110);
        if (iVar1 == 0) {
          *(undefined4 *)(param_1 + 0x4c) = 0x40;
          FUN_ffff0000b804d070((long)param_1 + 0x264,0,0x40);
          local_120 = 0;
          local_118 = 0;
          local_130 = 0;
          local_128 = 0;
          lVar2 = FUN_ffff0000b8049540((long)&uStack_100 + 3,0x81,&local_130);
          if (lVar2 == 0) {
            FUN_ffff0000b804c33c(s_ERROR__cannot_sha256_ufsid__ffff0000b809272d);
          }
          else {
            *(undefined1 *)((long)param_1 + 0x264) = local_130._3_1_;
            *(undefined1 *)((long)param_1 + 0x265) = local_130._2_1_;
            *(undefined1 *)((long)param_1 + 0x266) = local_130._1_1_;
            *(undefined1 *)((long)param_1 + 0x267) = (undefined1)local_130;
            *(undefined1 *)(param_1 + 0x4d) = local_130._7_1_;
            *(undefined1 *)((long)param_1 + 0x269) = local_130._6_1_;
            *(undefined1 *)((long)param_1 + 0x26a) = local_130._5_1_;
            *(undefined1 *)((long)param_1 + 0x26b) = local_130._4_1_;
            *(undefined1 *)((long)param_1 + 0x26c) = local_128._3_1_;
            *(undefined1 *)((long)param_1 + 0x26d) = local_128._2_1_;
            *(undefined1 *)((long)param_1 + 0x26e) = local_128._1_1_;
            *(undefined1 *)((long)param_1 + 0x26f) = (undefined1)local_128;
            *(undefined1 *)(param_1 + 0x4e) = local_128._7_1_;
            *(undefined1 *)((long)param_1 + 0x271) = local_128._6_1_;
            *(undefined1 *)((long)param_1 + 0x272) = local_128._5_1_;
            *(undefined1 *)((long)param_1 + 0x273) = local_128._4_1_;
            *(undefined1 *)((long)param_1 + 0x274) = local_120._3_1_;
            *(undefined1 *)((long)param_1 + 0x275) = local_120._2_1_;
            *(undefined1 *)((long)param_1 + 0x276) = local_120._1_1_;
            *(undefined1 *)((long)param_1 + 0x277) = (undefined1)local_120;
            *(undefined1 *)(param_1 + 0x4f) = local_120._7_1_;
            *(undefined1 *)((long)param_1 + 0x279) = local_120._6_1_;
            *(undefined1 *)((long)param_1 + 0x27a) = local_120._5_1_;
            *(undefined1 *)((long)param_1 + 0x27b) = local_120._4_1_;
            *(undefined1 *)((long)param_1 + 0x27c) = local_118._3_1_;
            *(undefined1 *)((long)param_1 + 0x27d) = local_118._2_1_;
            *(undefined1 *)((long)param_1 + 0x27e) = local_118._1_1_;
            *(undefined1 *)((long)param_1 + 0x27f) = (undefined1)local_118;
            *(undefined1 *)(param_1 + 0x50) = local_118._7_1_;
            *(undefined1 *)((long)param_1 + 0x281) = local_118._6_1_;
            *(undefined1 *)((long)param_1 + 0x282) = local_118._5_1_;
            *(undefined1 *)((long)param_1 + 0x283) = local_118._4_1_;
          }
        }
        else {
          FUN_ffff0000b804c33c(s_get_unique_id_failed___d_ffff0000b8098e80);
        }
        iVar1 = thunk_FUN_ffff0000b80729e0(0,param_1 + 0x41,0x20);
        if (iVar1 == 0) {
          return;
        }
        pcVar3 = s_RPMB_Key_derive_from_seclib_fail_ffff0000b8090cf0;
      }
      else {
        pcVar3 = s_Get_MEID_from_pl_boottags_failed_ffff0000b809e748;
      }
      FUN_ffff0000b804c33c(pcVar3,iVar1);
      FUN_ffff0000b804c33c(s_ERROR__Prepare_TEEI_key_paramete_ffff0000b80919da);
      FUN_ffff0000b804c33c(s_Please_check_the_keys_for_teei__ffff0000b8088295);
      FUN_ffff0000b804c33c(s_Or_there_will_be_unexpected_fail_ffff0000b809b98f);
      return;
    }
    pcVar3 = s_Failed_to_alloc_the_shared_memor_ffff0000b808c7a3;
  }
  FUN_ffff0000b804c33c(pcVar3);
  return;
}


