
undefined4
FUN_ffff0000b805b4e8
          (undefined8 param_1,undefined4 param_2,undefined8 param_3,undefined4 param_4,
          undefined8 param_5,undefined4 param_6,undefined8 param_7,undefined4 param_8)

{
  int iVar1;
  undefined8 uVar2;
  undefined4 uVar3;
  
  iVar1 = FUN_ffff0000b8067a00();
  if (iVar1 == 0) {
    iVar1 = FUN_ffff0000b8072bac(param_1,param_2,param_3,param_4,param_5,param_6,param_7,param_8);
  }
  else {
    iVar1 = FUN_ffff0000b805b7bc(&PTR_s_sha256_ffff0000b8107388);
    if (iVar1 == -1) {
      FUN_ffff0000b804c33c(s__SEC__HKDF__LTC_SHA256_register_f_ffff0000b8095c32,0xffffffff);
      FUN_ffff0000b804c33c(s__SEC__HKDF__sw_hkdf_init_failed__ffff0000b809fea8,0xffffffff);
      return 0x102025;
    }
    uVar2 = FUN_ffff0000b805b700(s_sha256_ffff0000b80a0ac0);
    if ((int)uVar2 == -1) {
      FUN_ffff0000b804c33c(s__SEC__HKDF__find_hash_failed__er_ffff0000b8095c6a,0xffffffff);
      return 0x102024;
    }
    iVar1 = FUN_ffff0000b805bbc0
                      (uVar2,param_1,param_2,param_3,param_4,param_5,param_6,param_7,param_8);
  }
  if (iVar1 == 0) {
    uVar3 = 0;
  }
  else {
    uVar3 = 0x102023;
    FUN_ffff0000b804c33c(s__SEC__HKDF__hkdf_failed__error_c_ffff0000b809b134,iVar1);
  }
  return uVar3;
}


