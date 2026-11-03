
uint FUN_ffff0000be01e3cc(uint param_1,uint param_2)

{
  uint uVar1;
  char *pcVar2;
  undefined8 uVar3;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined8 uStack_38;
  
  if (param_1 < 2) {
    local_40 = 0;
    uStack_38 = 0;
    local_50 = 0;
    uStack_48 = 0;
    uVar1 = FUN_ffff0000be01e220(&local_50,0);
    if ((int)uVar1 < 0) {
      FUN_ffff0000be05fd98
                (s__s__d___LK_AB__read_partition_in_ffff0000be0ab42d,
                 s_get_bctrl_mdata_ffff0000be0a7d02,0x145,uVar1);
      return uVar1;
    }
    if (local_50._4_4_ != 0x42414342) {
      FUN_ffff0000be05fd98
                (s__s__d___LK_AB__boot_ctrl_magic_n_ffff0000be0b9a51,
                 s_get_bctrl_mdata_ffff0000be0a7d02,0x14c,local_50._4_4_,0x42414342);
      return 0xfffffffa;
    }
    if (param_2 == 2) {
      return *(ushort *)((long)&uStack_48 + (ulong)param_1 * 2 + 4) >> 4 & 7;
    }
    if (param_2 == 1) {
      return *(ushort *)((long)&uStack_48 + (ulong)param_1 * 2 + 4) >> 7 & 1;
    }
    if (param_2 == 0) {
      return (uint)((*(ushort *)((long)&uStack_48 + (ulong)param_1 * 2 + 4) & 0xf) == 0);
    }
    pcVar2 = s__s__d___LK_AB__Invalid_field__d_ffff0000be0bcf95;
    uVar3 = 0x158;
  }
  else {
    pcVar2 = s__s__d___LK_AB__wrong_slot_number_ffff0000be0c05ec;
    uVar3 = 0x13f;
    param_2 = param_1;
  }
  FUN_ffff0000be05fd98(pcVar2,s_get_bctrl_mdata_ffff0000be0a7d02,uVar3,param_2);
  return 0xffffffea;
}


