// ffff000090781ca0  size=196  FUN_ffff000090781ca0

void FUN_ffff000090781ca0(undefined8 *param_1,undefined4 *param_2)

{
  undefined8 uVar1;
  undefined8 *puVar2;
  
  if ((param_1 != (undefined8 *)0x0) && (param_2 != (undefined4 *)0x0)) {
    puVar2 = (undefined8 *)FUN_ffff00009071a348(s_pstore_ffff0000907e1f2c,0);
    if (puVar2 == (undefined8 *)0x0) {
      FUN_ffff000090781ae8
                (s__s__fail_to_get_dram_addr_of_pst_ffff0000907fa87e,
                 s__MBOOT_PARAMS__ffff0000907ed1f2);
      FUN_ffff00009077e11c
                (s__s__fail_to_get_dram_addr_of_pst_ffff0000907fa87e,
                 s__MBOOT_PARAMS__ffff0000907ed1f2);
      *param_1 = 0;
      *param_2 = 0;
    }
    else {
      uVar1 = puVar2[1];
      *param_1 = *puVar2;
      *param_2 = (int)uVar1;
    }
    return;
  }
  FUN_ffff000090781ae8
            (s__s__get_pstore_info_args_invalid_ffff0000907e547e,s__MBOOT_PARAMS__ffff0000907ed1f2);
  FUN_ffff00009077e11c
            (s__s__get_pstore_info_args_invalid_ffff0000907e547e,s__MBOOT_PARAMS__ffff0000907ed1f2);
  return;
}


