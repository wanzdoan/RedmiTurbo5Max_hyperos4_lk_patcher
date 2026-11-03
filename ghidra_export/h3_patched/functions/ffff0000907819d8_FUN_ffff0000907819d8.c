// ffff0000907819d8  size=196  FUN_ffff0000907819d8

void FUN_ffff0000907819d8(undefined8 *param_1,undefined4 *param_2)

{
  undefined8 uVar1;
  undefined8 *puVar2;
  
  if ((param_1 != (undefined8 *)0x0) && (param_2 != (undefined4 *)0x0)) {
    puVar2 = (undefined8 *)FUN_ffff00009071a204(s_pstore_ffff0000907e1e44,0);
    if (puVar2 == (undefined8 *)0x0) {
      FUN_ffff000090781820
                (s__s__fail_to_get_dram_addr_of_pst_ffff0000907fa6e2,
                 s__MBOOT_PARAMS__ffff0000907ed058);
      FUN_ffff00009077de54
                (s__s__fail_to_get_dram_addr_of_pst_ffff0000907fa6e2,
                 s__MBOOT_PARAMS__ffff0000907ed058);
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
  FUN_ffff000090781820
            (s__s__get_pstore_info_args_invalid_ffff0000907e5363,s__MBOOT_PARAMS__ffff0000907ed058);
  FUN_ffff00009077de54
            (s__s__get_pstore_info_args_invalid_ffff0000907e5363,s__MBOOT_PARAMS__ffff0000907ed058);
  return;
}


