// ffff000090768188  size=212  FUN_ffff000090768188

void FUN_ffff000090768188(void)

{
  int iVar1;
  ulong uVar2;
  uint uVar3;
  undefined4 local_4;
  
  local_4 = 0;
  FUN_ffff000090768270(&local_4);
  uVar2 = FUN_ffff000090766998();
  if ((uVar2 & 1) == 0) {
    FUN_ffff00009077de54(s_The_count_of_dm_verity_corruptio_ffff0000907ffa5b);
    FUN_ffff000090745a54();
    iVar1 = FUN_ffff000090745a40();
    if (iVar1 < 0) {
      iVar1 = iVar1 + 1;
    }
    FUN_ffff0000907459fc(iVar1 >> 1,0);
    FUN_ffff0000907458c8(s_dm_verity_corruption_ffff0000907ffaa4);
    FUN_ffff0000907458c8(s_Your_device_is_corrupt__ffff0000907f8baa);
    FUN_ffff0000907458c8(s_It_can_t_be_trusted_and_may_not_w_ffff0000907e9cbc);
    FUN_ffff0000907458c8(s_Press_power_button_to_continue__ffff0000907e82b7);
    FUN_ffff0000907458c8(s_Or__device_will_power_off_in__ds_ffff0000907e377a,5);
    uVar3 = 0xffffff9c;
    while (iVar1 = FUN_ffff000090736098(0), iVar1 == 0) {
      FUN_ffff00009077cbd8(100000);
      uVar3 = uVar3 + 100;
      if (0x4c8 < uVar3 >> 2) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009072259c(2,8);
      }
    }
  }
  return;
}


