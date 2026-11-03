// ffff00009072074c  size=576  FUN_ffff00009072074c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009072074c(uint *param_1,int param_2)

{
  int iVar1;
  char *pcVar2;
  char *pcVar3;
  ushort local_34 [2];
  ushort local_18 [2];
  ushort local_14 [2];
  
  if (_DAT_ffff000090860650 != 0) {
    return 0xffffffff;
  }
  if (param_2 == 2) {
    local_14[0] = 0;
    iVar1 = FUN_ffff0000907375c4(_DAT_ffff000090860648,0x17,local_14,2);
    if (iVar1 < 0) {
      FUN_ffff00009077e11c(s_mt6379_2p_read__02x_block_failed_ffff0000907e283e,0x17);
      pcVar2 = s_mt6379_2p_get_ibat_ffff0000907eddce;
      FUN_ffff00009077e11c
                (s__s__Failed_to_get_ibat_adc_ffff0000907f7ef6,s_mt6379_2p_get_ibat_ffff0000907eddce
                );
      pcVar3 = s__s__ICHG____d__ffff0000907f44aa;
    }
    else {
      if (iVar1 == 0) {
        *param_1 = (((uint)(local_14[0] >> 8) | (local_14[0] & 0xff00ff) << 8) * 0xc35) / 1000;
      }
      pcVar3 = s__s__ICHG____d__ffff0000907f44aa;
      pcVar2 = s_mt6379_2p_get_ibat_ffff0000907eddce;
    }
  }
  else if (param_2 == 1) {
    local_18[0] = 0;
    iVar1 = FUN_ffff0000907375c4(_DAT_ffff000090860648,0x19,local_18,2);
    if (iVar1 < 0) {
      FUN_ffff00009077e11c(s_mt6379_2p_read__02x_block_failed_ffff0000907e283e,0x19);
      pcVar2 = s_mt6379_2p_get_vbat_ffff0000907f60d2;
      FUN_ffff00009077e11c
                (s__s__Failed_to_get_vbat_adc_ffff0000907d0d29,s_mt6379_2p_get_vbat_ffff0000907f60d2
                );
      pcVar3 = s__s__VBAT____d__ffff0000907f2869;
    }
    else {
      if (iVar1 == 0) {
        *param_1 = (((uint)(local_18[0] >> 8) | (local_18[0] & 0xff00ff) << 8) * 0x7d) / 100;
      }
      pcVar3 = s__s__VBAT____d__ffff0000907f2869;
      pcVar2 = s_mt6379_2p_get_vbat_ffff0000907f60d2;
    }
  }
  else {
    if (param_2 != 0) {
      return 0;
    }
    local_34[0] = 0;
    iVar1 = FUN_ffff0000907375c4(_DAT_ffff000090860648,0x1b,local_34,2);
    if (iVar1 < 0) {
      FUN_ffff00009077e11c(s_mt6379_2p_read__02x_block_failed_ffff0000907e283e,0x1b);
      pcVar2 = s_mt6379_2p_get_vbus_ffff0000907ec222;
      FUN_ffff00009077e11c
                (s__s__Failed_to_get_vbus_adc_ffff0000907eddb2,s_mt6379_2p_get_vbus_ffff0000907ec222
                );
      pcVar3 = s__s__VBUS____d__ffff0000907cf40c;
    }
    else {
      if (iVar1 == 0) {
        *param_1 = (((uint)(local_34[0] >> 8) | (local_34[0] & 0xff00ff) << 8) * 0x7d) / 100;
      }
      pcVar3 = s__s__VBUS____d__ffff0000907cf40c;
      pcVar2 = s_mt6379_2p_get_vbus_ffff0000907ec222;
    }
  }
  FUN_ffff00009077e11c(s__s___d_ffff0000907d0d21,pcVar2,*param_1);
  FUN_ffff00009077e11c(pcVar3,s_mt6379_2p_cs_get_val_ffff0000907da87a,*param_1);
  return 0;
}


