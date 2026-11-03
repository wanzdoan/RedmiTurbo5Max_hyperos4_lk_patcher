
void FUN_ffff0000be063d7c(undefined8 *param_1,undefined4 *param_2)

{
  undefined8 uVar1;
  undefined8 *puVar2;
  
  if ((param_1 != (undefined8 *)0x0) && (param_2 != (undefined4 *)0x0)) {
    puVar2 = (undefined8 *)FUN_ffff0000be01cb9c(s_pstore_ffff0000be0b0486,0);
    if (puVar2 == (undefined8 *)0x0) {
      FUN_ffff0000be063b40
                (s__s__fail_to_get_dram_addr_of_pst_ffff0000be0c11fc,
                 s__MBOOT_PARAMS__ffff0000be0b7f52);
      FUN_ffff0000be05fd98
                (s__s__fail_to_get_dram_addr_of_pst_ffff0000be0c11fc,
                 s__MBOOT_PARAMS__ffff0000be0b7f52);
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
  FUN_ffff0000be063b40
            (s__s__get_pstore_info_args_invalid_ffff0000be0b25ea,s__MBOOT_PARAMS__ffff0000be0b7f52);
  FUN_ffff0000be05fd98
            (s__s__get_pstore_info_args_invalid_ffff0000be0b25ea,s__MBOOT_PARAMS__ffff0000be0b7f52);
  return;
}


