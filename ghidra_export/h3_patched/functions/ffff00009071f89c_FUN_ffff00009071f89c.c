// ffff00009071f89c  size=332  FUN_ffff00009071f89c

uint FUN_ffff00009071f89c(uint param_1,uint param_2)

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
    uVar1 = FUN_ffff00009071f6f0(&local_50,0);
    if ((int)uVar1 < 0) {
      FUN_ffff00009077de54
                (s__s__d___LK_AB__read_partition_in_ffff0000907da6eb,
                 s_get_bctrl_mdata_ffff0000907d5c1d,0x145,uVar1);
      return uVar1;
    }
    if (local_50._4_4_ != 0x42414342) {
      FUN_ffff00009077de54
                (s__s__d___LK_AB__boot_ctrl_magic_n_ffff0000907ef3ef,
                 s_get_bctrl_mdata_ffff0000907d5c1d,0x14c,local_50._4_4_,0x42414342);
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
    pcVar2 = s__s__d___LK_AB__Invalid_field__d_ffff0000907f4305;
    uVar3 = 0x158;
  }
  else {
    pcVar2 = s__s__d___LK_AB__wrong_slot_number_ffff0000907f9532;
    uVar3 = 0x13f;
    param_2 = param_1;
  }
  FUN_ffff00009077de54(pcVar2,s_get_bctrl_mdata_ffff0000907d5c1d,uVar3,param_2);
  return 0xffffffea;
}


