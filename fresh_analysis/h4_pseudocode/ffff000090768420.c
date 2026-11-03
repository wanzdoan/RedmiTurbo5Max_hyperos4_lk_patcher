
void FUN_ffff000090768420(void)

{
  int iVar1;
  ulong uVar2;
  uint uVar3;
  undefined4 local_4;
  
  local_4 = 0;
  FUN_ffff000090768508(&local_4);
  uVar2 = FUN_ffff000090766c30();
  if ((uVar2 & 1) == 0) {
    FUN_ffff00009077e11c(s_The_count_of_dm_verity_corruptio_ffff0000907ffc03);
    FUN_ffff000090745cec();
    iVar1 = FUN_ffff000090745cd8();
    if (iVar1 < 0) {
      iVar1 = iVar1 + 1;
    }
    FUN_ffff000090745c94(iVar1 >> 1,0);
    FUN_ffff000090745b60(s_dm_verity_corruption_ffff0000907ffc4c);
    FUN_ffff000090745b60(s_Your_device_is_corrupt__ffff0000907f8d46);
    FUN_ffff000090745b60(s_It_can_t_be_trusted_and_may_not_w_ffff0000907e9e17);
    FUN_ffff000090745b60(s_Press_power_button_to_continue__ffff0000907e8412);
    FUN_ffff000090745b60(s_Or__device_will_power_off_in__ds_ffff0000907e3862,5);
    uVar3 = 0xffffff9c;
    while (iVar1 = FUN_ffff000090736330(0), iVar1 == 0) {
      FUN_ffff00009077cea0(100000);
      uVar3 = uVar3 + 100;
      if (0x4c8 < uVar3 >> 2) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff0000907227e0(2,8);
      }
    }
  }
  return;
}


