// ffff00009076b674  size=460  FUN_ffff00009076b674

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009076b674(long param_1)

{
  ulong uVar1;
  undefined8 local_88;
  undefined4 local_80;
  undefined4 local_7c;
  undefined8 local_78;
  undefined8 uStack_70;
  undefined8 local_68;
  undefined8 uStack_60;
  undefined8 local_58;
  undefined8 uStack_50;
  undefined8 local_48;
  undefined8 uStack_40;
  undefined8 local_38;
  undefined8 uStack_30;
  undefined8 local_28;
  
  local_7c = 0x23700;
  local_28 = 0;
  local_38 = 0;
  uStack_30 = 0;
  local_88 = DAT_ffff00009080e33c;
  local_48 = 0;
  uStack_40 = 0;
  local_58 = 0;
  uStack_50 = 0;
  local_80 = 8;
  local_68 = 0;
  uStack_60 = 0;
  local_78 = 0;
  uStack_70 = 0;
  (*_DAT_ffff00009087ccd0)(&local_88,3,1);
  if (*(int *)(param_1 + 0x1c) == 1) {
    (*_DAT_ffff00009087ccd0)(&local_7c,1,1);
    (*_DAT_ffff00009087cd00)(0xeb,&local_28,8);
    uVar1 = local_28 & 0xff;
  }
  else {
    uVar1 = 0;
  }
  FUN_ffff00009077de54
            (s__LK_LCM__LCM__s__rx_buf__d_____0_ffff0000907e0243,
             s_lcm_get_tplockdown_info_ffff0000907fc268,0,uVar1);
  FUN_ffff00009077de54
            (s__LK_LCM__LCM__s__rx_buf__d_____0_ffff0000907e0243,
             s_lcm_get_tplockdown_info_ffff0000907fc268,1,local_28._1_1_);
  FUN_ffff00009077de54
            (s__LK_LCM__LCM__s__rx_buf__d_____0_ffff0000907e0243,
             s_lcm_get_tplockdown_info_ffff0000907fc268,2,local_28._2_1_);
  FUN_ffff00009077de54
            (s__LK_LCM__LCM__s__rx_buf__d_____0_ffff0000907e0243,
             s_lcm_get_tplockdown_info_ffff0000907fc268,3,local_28._3_1_);
  FUN_ffff00009077de54
            (s__LK_LCM__LCM__s__rx_buf__d_____0_ffff0000907e0243,
             s_lcm_get_tplockdown_info_ffff0000907fc268,4,local_28._4_1_);
  FUN_ffff00009077de54
            (s__LK_LCM__LCM__s__rx_buf__d_____0_ffff0000907e0243,
             s_lcm_get_tplockdown_info_ffff0000907fc268,5,local_28._5_1_);
  FUN_ffff00009077de54
            (s__LK_LCM__LCM__s__rx_buf__d_____0_ffff0000907e0243,
             s_lcm_get_tplockdown_info_ffff0000907fc268,6,local_28._6_1_);
  FUN_ffff00009077de54
            (s__LK_LCM__LCM__s__rx_buf__d_____0_ffff0000907e0243,
             s_lcm_get_tplockdown_info_ffff0000907fc268,7,local_28._7_1_);
  FUN_ffff00009077eb88(&local_78,0,0x50);
  FUN_ffff00009077f434
            (&local_78,0x50,s_mediatek_drm_panel_opt_0x_02hhx__ffff0000907dea6a,local_28 & 0xff,
             local_28._1_1_,local_28._2_1_,local_28._3_1_,local_28._4_1_,local_28._5_1_,
             local_28._6_1_,local_28._7_1_);
  FUN_ffff000090718f34(&local_78);
  return;
}


