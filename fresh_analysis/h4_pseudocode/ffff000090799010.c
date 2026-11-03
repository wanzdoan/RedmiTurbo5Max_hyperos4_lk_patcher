
undefined4
FUN_ffff000090799010
          (undefined8 param_1,undefined4 param_2,undefined8 param_3,undefined4 param_4,
          undefined8 param_5,undefined4 param_6,undefined8 param_7,undefined4 param_8)

{
  int iVar1;
  undefined8 uVar2;
  undefined4 uVar3;
  
  iVar1 = FUN_ffff0000907aa3cc();
  if (iVar1 == 0) {
    iVar1 = FUN_ffff0000907b68b4(param_1,param_2,param_3,param_4,param_5,param_6,param_7,param_8);
  }
  else {
    iVar1 = FUN_ffff0000907992e4(&PTR_s_sha256_ffff000090837b58);
    if (iVar1 == -1) {
      FUN_ffff00009077e11c(s__SEC__HKDF__LTC_SHA256_register_f_ffff0000907ed67a,0xffffffff);
      FUN_ffff00009077e11c(s__SEC__HKDF__sw_hkdf_init_failed__ffff00009080003e,0xffffffff);
      return 0x102025;
    }
    uVar2 = FUN_ffff000090799228(s_sha256_ffff0000908019f2);
    if ((int)uVar2 == -1) {
      FUN_ffff00009077e11c(s__SEC__HKDF__find_hash_failed__er_ffff0000907ed6b2,0xffffffff);
      return 0x102024;
    }
    iVar1 = FUN_ffff0000907996e8
                      (uVar2,param_1,param_2,param_3,param_4,param_5,param_6,param_7,param_8);
  }
  if (iVar1 == 0) {
    uVar3 = 0;
  }
  else {
    uVar3 = 0x102023;
    FUN_ffff00009077e11c(s__SEC__HKDF__hkdf_failed__error_c_ffff0000907f752d,iVar1);
  }
  return uVar3;
}


