// ffff000090798d50  size=344  FUN_ffff000090798d50

undefined4
FUN_ffff000090798d50
          (undefined8 param_1,undefined4 param_2,undefined8 param_3,undefined4 param_4,
          undefined8 param_5,undefined4 param_6,undefined8 param_7,undefined4 param_8)

{
  int iVar1;
  undefined8 uVar2;
  undefined4 uVar3;
  
  iVar1 = FUN_ffff0000907aa10c();
  if (iVar1 == 0) {
    iVar1 = FUN_ffff0000907b6240(param_1,param_2,param_3,param_4,param_5,param_6,param_7,param_8);
  }
  else {
    iVar1 = FUN_ffff000090799024(&PTR_s_sha256_ffff000090837b38);
    if (iVar1 == -1) {
      FUN_ffff00009077de54(s__SEC__HKDF__LTC_SHA256_register_f_ffff0000907ed4f7,0xffffffff);
      FUN_ffff00009077de54(s__SEC__HKDF__sw_hkdf_init_failed__ffff0000907ffe96,0xffffffff);
      return 0x102025;
    }
    uVar2 = FUN_ffff000090798f68(s_sha256_ffff000090801816);
    if ((int)uVar2 == -1) {
      FUN_ffff00009077de54(s__SEC__HKDF__find_hash_failed__er_ffff0000907ed52f,0xffffffff);
      return 0x102024;
    }
    iVar1 = FUN_ffff000090799428
                      (uVar2,param_1,param_2,param_3,param_4,param_5,param_6,param_7,param_8);
  }
  if (iVar1 == 0) {
    uVar3 = 0;
  }
  else {
    uVar3 = 0x102023;
    FUN_ffff00009077de54(s__SEC__HKDF__hkdf_failed__error_c_ffff0000907f73a3,iVar1);
  }
  return uVar3;
}


