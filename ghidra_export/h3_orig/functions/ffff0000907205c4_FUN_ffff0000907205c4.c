// ffff0000907205c4  size=576  FUN_ffff0000907205c4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907205c4(uint *param_1,int param_2)

{
  int iVar1;
  char *pcVar2;
  char *pcVar3;
  ushort local_34 [2];
  ushort local_18 [2];
  ushort local_14 [2];
  
  if (_DAT_ffff0000908605b8 != 0) {
    return 0xffffffff;
  }
  if (param_2 == 2) {
    local_14[0] = 0;
    iVar1 = FUN_ffff00009073732c(_DAT_ffff0000908605b0,0x17,local_14,2);
    if (iVar1 < 0) {
      FUN_ffff00009077de54(s_mt6379_2p_read__02x_block_failed_ffff0000907e276d,0x17);
      pcVar2 = s_mt6379_2p_get_ibat_ffff0000907edc5d;
      FUN_ffff00009077de54
                (s__s__Failed_to_get_ibat_adc_ffff0000907f7d5a,s_mt6379_2p_get_ibat_ffff0000907edc5d
                );
      pcVar3 = s__s__ICHG____d__ffff0000907f433d;
    }
    else {
      if (iVar1 == 0) {
        *param_1 = (((uint)(local_14[0] >> 8) | (local_14[0] & 0xff00ff) << 8) * 0xc35) / 1000;
      }
      pcVar3 = s__s__ICHG____d__ffff0000907f433d;
      pcVar2 = s_mt6379_2p_get_ibat_ffff0000907edc5d;
    }
  }
  else if (param_2 == 1) {
    local_18[0] = 0;
    iVar1 = FUN_ffff00009073732c(_DAT_ffff0000908605b0,0x19,local_18,2);
    if (iVar1 < 0) {
      FUN_ffff00009077de54(s_mt6379_2p_read__02x_block_failed_ffff0000907e276d,0x19);
      pcVar2 = s_mt6379_2p_get_vbat_ffff0000907f5f48;
      FUN_ffff00009077de54
                (s__s__Failed_to_get_vbat_adc_ffff0000907d0cec,s_mt6379_2p_get_vbat_ffff0000907f5f48
                );
      pcVar3 = s__s__VBAT____d__ffff0000907f26dd;
    }
    else {
      if (iVar1 == 0) {
        *param_1 = (((uint)(local_18[0] >> 8) | (local_18[0] & 0xff00ff) << 8) * 0x7d) / 100;
      }
      pcVar3 = s__s__VBAT____d__ffff0000907f26dd;
      pcVar2 = s_mt6379_2p_get_vbat_ffff0000907f5f48;
    }
  }
  else {
    if (param_2 != 0) {
      return 0;
    }
    local_34[0] = 0;
    iVar1 = FUN_ffff00009073732c(_DAT_ffff0000908605b0,0x1b,local_34,2);
    if (iVar1 < 0) {
      FUN_ffff00009077de54(s_mt6379_2p_read__02x_block_failed_ffff0000907e276d,0x1b);
      pcVar2 = s_mt6379_2p_get_vbus_ffff0000907ec09f;
      FUN_ffff00009077de54
                (s__s__Failed_to_get_vbus_adc_ffff0000907edc41,s_mt6379_2p_get_vbus_ffff0000907ec09f
                );
      pcVar3 = s__s__VBUS____d__ffff0000907cf3ed;
    }
    else {
      if (iVar1 == 0) {
        *param_1 = (((uint)(local_34[0] >> 8) | (local_34[0] & 0xff00ff) << 8) * 0x7d) / 100;
      }
      pcVar3 = s__s__VBUS____d__ffff0000907cf3ed;
      pcVar2 = s_mt6379_2p_get_vbus_ffff0000907ec09f;
    }
  }
  FUN_ffff00009077de54(s__s___d_ffff0000907d0ce4,pcVar2,*param_1);
  FUN_ffff00009077de54(pcVar3,s_mt6379_2p_cs_get_val_ffff0000907da76f,*param_1);
  return 0;
}


