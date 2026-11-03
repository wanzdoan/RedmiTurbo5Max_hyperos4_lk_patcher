
undefined4 FUN_ffff0000be063f24(void)

{
  undefined4 uVar1;
  
  if (DAT_ffff0000be1321c0 == 0) {
    FUN_ffff0000be063b40
              (s__s__mboot_params_not_ready_ffff0000be0ade50,s__MBOOT_PARAMS__ffff0000be0b7f52);
    FUN_ffff0000be05fd98
              (s__s__mboot_params_not_ready_ffff0000be0ade50,s__MBOOT_PARAMS__ffff0000be0b7f52);
    uVar1 = 0;
  }
  else {
    uVar1 = *(undefined4 *)(DAT_ffff0000be1321c0 + 0x24);
  }
  return uVar1;
}


