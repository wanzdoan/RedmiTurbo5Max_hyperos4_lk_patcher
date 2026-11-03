
void FUN_ffff0000be063e40(undefined8 *param_1,undefined4 *param_2)

{
  undefined8 uVar1;
  undefined8 *puVar2;
  
  if ((param_1 != (undefined8 *)0x0) && (param_2 != (undefined4 *)0x0)) {
    puVar2 = (undefined8 *)FUN_ffff0000be01cb9c(s_minirdump_ffff0000be0b3539,0);
    if (puVar2 == (undefined8 *)0x0) {
      FUN_ffff0000be063b40
                (s__s__fail_to_get_dram_addr_of_min_ffff0000be0aef19,
                 s__MBOOT_PARAMS__ffff0000be0b7f52);
      FUN_ffff0000be05fd98
                (s__s__fail_to_get_dram_addr_of_min_ffff0000be0aef19,
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
            (s__s__get_mrdump_mini_header_info_a_ffff0000be0c6907,s__MBOOT_PARAMS__ffff0000be0b7f52)
  ;
  FUN_ffff0000be05fd98
            (s__s__get_mrdump_mini_header_info_a_ffff0000be0c6907,s__MBOOT_PARAMS__ffff0000be0b7f52)
  ;
  return;
}


