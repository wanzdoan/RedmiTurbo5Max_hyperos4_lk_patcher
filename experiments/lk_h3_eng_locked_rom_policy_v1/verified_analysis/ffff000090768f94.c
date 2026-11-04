
void FUN_ffff000090768f94(void)

{
  int iVar1;
  ulong uVar2;
  uint uVar3;
  undefined4 local_4;

  local_4 = 0;
  FUN_ffff00009076907c(&local_4);
  uVar2 = FUN_ffff000090767ccc();
  if ((uVar2 & 1) == 0) {
    FUN_ffff00009077ea84(s_The_count_of_dm_verity_corruptio_ffff0000907f8a40);
    FUN_ffff000090746a7c();
    iVar1 = FUN_ffff000090746a68();
    if (iVar1 < 0) {
      iVar1 = iVar1 + 1;
    }
    FUN_ffff000090746a24(iVar1 >> 1,0);
    FUN_ffff0000907468f0(s_dm_verity_corruption_ffff0000907f8a89);
    FUN_ffff0000907468f0(s_Your_device_is_corrupt__ffff0000907f1a18);
    FUN_ffff0000907468f0(s_It_can_t_be_trusted_and_may_not_w_ffff0000907e2763);
    FUN_ffff0000907468f0(s_Press_power_button_to_continue__ffff0000907e0c8b);
    FUN_ffff0000907468f0(s_Or__device_will_power_off_in__ds_ffff0000907dbf92,5);
    uVar3 = 0xffffff9c;
    while (iVar1 = FUN_ffff00009073693c(0), iVar1 == 0) {
      FUN_ffff00009077d808(100000);
      uVar3 = uVar3 + 100;
      if (0x4c8 < uVar3 >> 2) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009072272c(2,8);
      }
    }
  }
  return;
}
