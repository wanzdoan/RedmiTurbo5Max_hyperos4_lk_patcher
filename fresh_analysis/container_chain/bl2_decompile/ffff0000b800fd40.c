
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000b800fd40(long param_1)

{
  uint uVar1;
  int iVar2;
  long lVar3;
  uint *puVar4;
  int local_34;
  
  iVar2 = FUN_ffff0000b8012700();
  if (iVar2 < 0) {
    FUN_ffff0000b804c33c
              (s__s__d___s__mtk_charger_init_fail_ffff0000b8086f98,s_charger_init_ffff0000b808f8ae,
               0x1d2,s_charger_init_ffff0000b808f8ae,iVar2);
  }
  DAT_ffff0000b811aea0._0_1_ = 0;
  DAT_ffff0000b811aea0._1_1_ = 1;
  DAT_ffff0000b811aea4 = 0xc80;
  DAT_ffff0000b811af1c = 0xc80;
  _DAT_ffff0000b811af20 = 1;
  DAT_ffff0000b811aea2 = 1;
  DAT_ffff0000b811aeb8 = 0xc1c;
  DAT_ffff0000b811aebc = 500;
  _DAT_ffff0000b811aec0 = 0x64000000640;
  DAT_ffff0000b811aefc = 1;
  _DAT_ffff0000b811af18 = 0;
  DAT_ffff0000b811aed8 = 3000;
  DAT_ffff0000b811aedc = 500;
  _DAT_ffff0000b811aee0 = 0xbb8000000fa;
  _DAT_ffff0000b811aec8 = 0xbb800000640;
  _DAT_ffff0000b811aed0 = 0x5dc000001f4;
  DAT_ffff0000b811aef8 = 0xfffffff8;
  _DAT_ffff0000b811af10 = 0xfa000000027;
  _DAT_ffff0000b811af00 = 0x500000066;
  _DAT_ffff0000b811af08 = 0x14a000001f4;
  DAT_ffff0000b811af1a = 0;
  DAT_ffff0000b811aea3 = 0;
  DAT_ffff0000b811aee8 = 0x3c;
  DAT_ffff0000b811aeec = 0x2d;
  _DAT_ffff0000b811aef0 = 0x38fffffff6;
  DAT_ffff0000b811aea8 = 0x32;
  DAT_ffff0000b811aeac = 0xe10;
  _DAT_ffff0000b811aeb0 = 0xfa000001964;
  if (param_1 == 0) {
    FUN_ffff0000b804c33c(s__chg__fdt_null__fall_back_to_def_ffff0000b808f8bb);
  }
  else {
    iVar2 = FUN_ffff0000b80470b4(param_1);
    if (iVar2 != 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000b804b744(s_Bad_DTB_header__ffff0000b8086070);
    }
    iVar2 = FUN_ffff0000b80483cc(param_1,0xffffffff,s_mediatek_lk_charger_ffff0000b809a774);
    if (-1 < iVar2) {
      local_34 = 0;
      lVar3 = FUN_ffff0000b804803c(param_1,iVar2,s_disable_charger_ffff0000b80a0ea5,&local_34);
      local_34 = 0;
      DAT_ffff0000b811aea0 = CONCAT11(DAT_ffff0000b811aea0._1_1_,lVar3 != 0);
      lVar3 = FUN_ffff0000b804803c(param_1,iVar2,s_enable_pe_plus_ffff0000b8086fc6,&local_34);
      DAT_ffff0000b811aea2 = lVar3 != 0;
      local_34 = 0;
      lVar3 = FUN_ffff0000b804803c(param_1,iVar2,s_power_path_support_ffff0000b8097da6,&local_34);
      local_34 = 0;
      DAT_ffff0000b811aea0._1_1_ = lVar3 != 0;
      puVar4 = (uint *)FUN_ffff0000b804803c
                                 (param_1,iVar2,s_boot_battery_voltage_ffff0000b8088b65,&local_34);
      if ((0 < local_34) && (puVar4 != (uint *)0x0)) {
        uVar1 = *puVar4;
        if (uVar1 != 0) {
          uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
          DAT_ffff0000b811aea4 = (int)(uVar1 >> 0x10 | uVar1 << 0x10) / 1000;
        }
      }
      local_34 = 0;
      puVar4 = (uint *)FUN_ffff0000b804803c
                                 (param_1,iVar2,s_max_charger_voltage_ffff0000b80a00f2,&local_34);
      if ((0 < local_34) && (puVar4 != (uint *)0x0)) {
        uVar1 = *puVar4;
        if (uVar1 != 0) {
          uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
          _DAT_ffff0000b811aeb0 =
               CONCAT44(DAT_ffff0000b811aeb4,(int)(uVar1 >> 0x10 | uVar1 << 0x10) / 1000);
        }
      }
      local_34 = 0;
      puVar4 = (uint *)FUN_ffff0000b804803c
                                 (param_1,iVar2,s_min_charger_voltage_ffff0000b809239f,&local_34);
      if ((0 < local_34) && (puVar4 != (uint *)0x0)) {
        uVar1 = *puVar4;
        if (uVar1 != 0) {
          uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
          _DAT_ffff0000b811aeb0 =
               CONCAT44((int)(uVar1 >> 0x10 | uVar1 << 0x10) / 1000,DAT_ffff0000b811aeb0);
        }
      }
      local_34 = 0;
      puVar4 = (uint *)FUN_ffff0000b804803c
                                 (param_1,iVar2,s_fast_charge_voltage_ffff0000b8087ea2,&local_34);
      if ((0 < local_34) && (puVar4 != (uint *)0x0)) {
        uVar1 = *puVar4;
        if (uVar1 != 0) {
          uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
          DAT_ffff0000b811aeb8 = (int)(uVar1 >> 0x10 | uVar1 << 0x10) / 1000;
        }
      }
      local_34 = 0;
      puVar4 = (uint *)FUN_ffff0000b804803c
                                 (param_1,iVar2,s_usb_charger_current_ffff0000b80a0eb5,&local_34);
      if ((0 < local_34) && (puVar4 != (uint *)0x0)) {
        uVar1 = *puVar4;
        if (uVar1 != 0) {
          uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
          DAT_ffff0000b811aebc = (int)(uVar1 >> 0x10 | uVar1 << 0x10) / 1000;
        }
      }
      local_34 = 0;
      puVar4 = (uint *)FUN_ffff0000b804803c
                                 (param_1,iVar2,s_ac_charger_current_ffff0000b808b58d,&local_34);
      if ((0 < local_34) && (puVar4 != (uint *)0x0)) {
        uVar1 = *puVar4;
        if (uVar1 != 0) {
          uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
          _DAT_ffff0000b811aec0 =
               CONCAT44(DAT_ffff0000b811aec4,(int)(uVar1 >> 0x10 | uVar1 << 0x10) / 1000);
        }
      }
      local_34 = 0;
      puVar4 = (uint *)FUN_ffff0000b804803c
                                 (param_1,iVar2,s_ac_charger_input_current_ffff0000b80a0106,
                                  &local_34);
      if ((0 < local_34) && (puVar4 != (uint *)0x0)) {
        uVar1 = *puVar4;
        if (uVar1 != 0) {
          uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
          _DAT_ffff0000b811aec0 =
               CONCAT44((int)(uVar1 >> 0x10 | uVar1 << 0x10) / 1000,DAT_ffff0000b811aec0);
        }
      }
      local_34 = 0;
      puVar4 = (uint *)FUN_ffff0000b804803c
                                 (param_1,iVar2,s_non_std_ac_charger_current_ffff0000b80a0ec9,
                                  &local_34);
      if ((0 < local_34) && (puVar4 != (uint *)0x0)) {
        uVar1 = *puVar4;
        if (uVar1 != 0) {
          uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
          _DAT_ffff0000b811aed0 =
               CONCAT44(DAT_ffff0000b811aed4,(int)(uVar1 >> 0x10 | uVar1 << 0x10) / 1000);
        }
      }
      local_34 = 0;
      puVar4 = (uint *)FUN_ffff0000b804803c
                                 (param_1,iVar2,s_charging_host_charger_current_ffff0000b808999e,
                                  &local_34);
      if ((0 < local_34) && (puVar4 != (uint *)0x0)) {
        uVar1 = *puVar4;
        if (uVar1 != 0) {
          uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
          _DAT_ffff0000b811aed0 =
               CONCAT44((int)(uVar1 >> 0x10 | uVar1 << 0x10) / 1000,DAT_ffff0000b811aed0);
        }
      }
      local_34 = 0;
      puVar4 = (uint *)FUN_ffff0000b804803c
                                 (param_1,iVar2,s_ta_ac_charger_current_ffff0000b809153f,&local_34);
      if ((0 < local_34) && (puVar4 != (uint *)0x0)) {
        uVar1 = *puVar4;
        if (uVar1 != 0) {
          uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
          DAT_ffff0000b811aed8 = (int)(uVar1 >> 0x10 | uVar1 << 0x10) / 1000;
        }
      }
      local_34 = 0;
      puVar4 = (uint *)FUN_ffff0000b804803c
                                 (param_1,iVar2,s_pd_charger_current_ffff0000b808ce44,&local_34);
      if ((0 < local_34) && (puVar4 != (uint *)0x0)) {
        uVar1 = *puVar4;
        if (uVar1 != 0) {
          uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
          DAT_ffff0000b811aedc = (int)(uVar1 >> 0x10 | uVar1 << 0x10) / 1000;
        }
      }
      local_34 = 0;
      puVar4 = (uint *)FUN_ffff0000b804803c
                                 (param_1,iVar2,s_pre_charger_current_ffff0000b8097db9,&local_34);
      if ((0 < local_34) && (puVar4 != (uint *)0x0)) {
        uVar1 = *puVar4;
        if (uVar1 != 0) {
          uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
          _DAT_ffff0000b811aee0 =
               CONCAT44(DAT_ffff0000b811aee4,(int)(uVar1 >> 0x10 | uVar1 << 0x10) / 1000);
        }
      }
      local_34 = 0;
      puVar4 = (uint *)FUN_ffff0000b804803c
                                 (param_1,iVar2,s_pre_charger_voltage_ffff0000b809d4e7,&local_34);
      if ((0 < local_34) && (puVar4 != (uint *)0x0)) {
        uVar1 = *puVar4;
        if (uVar1 != 0) {
          uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
          _DAT_ffff0000b811aee0 =
               CONCAT44((int)(uVar1 >> 0x10 | uVar1 << 0x10) / 1000,DAT_ffff0000b811aee0);
        }
      }
      local_34 = 0;
      puVar4 = (uint *)FUN_ffff0000b804803c
                                 (param_1,iVar2,s_temp_t4_threshold_ffff0000b8091555,&local_34);
      if ((0 < local_34) && (puVar4 != (uint *)0x0)) {
        uVar1 = *puVar4;
        if (uVar1 != 0) {
          uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
          DAT_ffff0000b811aee8 = uVar1 >> 0x10 | uVar1 << 0x10;
        }
      }
      local_34 = 0;
      puVar4 = (uint *)FUN_ffff0000b804803c
                                 (param_1,iVar2,s_temp_t3_threshold_ffff0000b8095249,&local_34);
      if ((0 < local_34) && (puVar4 != (uint *)0x0)) {
        uVar1 = *puVar4;
        if (uVar1 != 0) {
          uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
          DAT_ffff0000b811aeec = uVar1 >> 0x10 | uVar1 << 0x10;
        }
      }
      local_34 = 0;
      puVar4 = (uint *)FUN_ffff0000b804803c
                                 (param_1,iVar2,s_temp_t1_threshold_ffff0000b8086fd5,&local_34);
      if ((0 < local_34) && (puVar4 != (uint *)0x0)) {
        uVar1 = *puVar4;
        if (uVar1 != 0) {
          uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
          _DAT_ffff0000b811aef0 = CONCAT44(DAT_ffff0000b811aef4,uVar1 >> 0x10 | uVar1 << 0x10);
        }
      }
      local_34 = 0;
      puVar4 = (uint *)FUN_ffff0000b804803c
                                 (param_1,iVar2,s_temp_t4_thres_minus_x_degree_ffff0000b8097dcd,
                                  &local_34);
      if ((0 < local_34) && (puVar4 != (uint *)0x0)) {
        uVar1 = *puVar4;
        if (uVar1 != 0) {
          uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
          _DAT_ffff0000b811aef0 = CONCAT44(uVar1 >> 0x10 | uVar1 << 0x10,DAT_ffff0000b811aef0);
        }
      }
      local_34 = 0;
      puVar4 = (uint *)FUN_ffff0000b804803c
                                 (param_1,iVar2,s_temp_t1_thres_plus_x_degree_ffff0000b809c28a,
                                  &local_34);
      if ((0 < local_34) && (puVar4 != (uint *)0x0)) {
        uVar1 = *puVar4;
        if (uVar1 != 0) {
          uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
          DAT_ffff0000b811aef8 = uVar1 >> 0x10 | uVar1 << 0x10;
        }
      }
      local_34 = 0;
      lVar3 = FUN_ffff0000b804803c(param_1,iVar2,s_enable_anime_ffff0000b8091567,&local_34);
      DAT_ffff0000b811aefc = lVar3 != 0;
      local_34 = 0;
      puVar4 = (uint *)FUN_ffff0000b804803c
                                 (param_1,iVar2,s_led_brightness_ffff0000b809525b,&local_34);
      if ((0 < local_34) && (puVar4 != (uint *)0x0)) {
        uVar1 = *puVar4;
        if (uVar1 != 0) {
          uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
          _DAT_ffff0000b811af00 = CONCAT44(DAT_ffff0000b811af04,uVar1 >> 0x10 | uVar1 << 0x10);
        }
      }
      local_34 = 0;
      puVar4 = (uint *)FUN_ffff0000b804803c
                                 (param_1,iVar2,s_blinking_times_ffff0000b8097dea,&local_34);
      if ((0 < local_34) && (puVar4 != (uint *)0x0)) {
        uVar1 = *puVar4;
        if (uVar1 != 0) {
          uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
          _DAT_ffff0000b811af00 = CONCAT44(uVar1 >> 0x10 | uVar1 << 0x10,DAT_ffff0000b811af00);
        }
      }
      local_34 = 0;
      puVar4 = (uint *)FUN_ffff0000b804803c
                                 (param_1,iVar2,s_blinking_period_ffff0000b80a0ee4,&local_34);
      if ((0 < local_34) && (puVar4 != (uint *)0x0)) {
        uVar1 = *puVar4;
        if (uVar1 != 0) {
          uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
          _DAT_ffff0000b811af08 = CONCAT44(DAT_ffff0000b811af0c,uVar1 >> 0x10 | uVar1 << 0x10);
        }
      }
      local_34 = 0;
      puVar4 = (uint *)FUN_ffff0000b804803c(param_1,iVar2,s_r_charger_1_ffff0000b809d4fb,&local_34);
      if ((0 < local_34) && (puVar4 != (uint *)0x0)) {
        uVar1 = *puVar4;
        if (uVar1 != 0) {
          uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
          _DAT_ffff0000b811af08 = CONCAT44(uVar1 >> 0x10 | uVar1 << 0x10,DAT_ffff0000b811af08);
        }
      }
      local_34 = 0;
      puVar4 = (uint *)FUN_ffff0000b804803c(param_1,iVar2,s_r_charger_2_ffff0000b80860e1,&local_34);
      if ((0 < local_34) && (puVar4 != (uint *)0x0)) {
        uVar1 = *puVar4;
        if (uVar1 != 0) {
          uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
          _DAT_ffff0000b811af10 = CONCAT44(_DAT_ffff0000b811af14,uVar1 >> 0x10 | uVar1 << 0x10);
        }
      }
      local_34 = 0;
      puVar4 = (uint *)FUN_ffff0000b804803c
                                 (param_1,iVar2,s_boot_battery_voltage_max_ffff0000b8098a8a,
                                  &local_34);
      if ((0 < local_34) && (puVar4 != (uint *)0x0)) {
        uVar1 = *puVar4;
        if (uVar1 != 0) {
          uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
          DAT_ffff0000b811aeac = uVar1 >> 0x10 | uVar1 << 0x10;
        }
      }
      local_34 = 0;
      puVar4 = (uint *)FUN_ffff0000b804803c
                                 (param_1,iVar2,s_boot_battery_voltage_gap_ffff0000b809996c,
                                  &local_34);
      if ((0 < local_34) && (puVar4 != (uint *)0x0)) {
        uVar1 = *puVar4;
        if (uVar1 != 0) {
          uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
          DAT_ffff0000b811aea8 = uVar1 >> 0x10 | uVar1 << 0x10;
        }
      }
      local_34 = 0;
      lVar3 = FUN_ffff0000b804803c(param_1,iVar2,s_enable_check_vsys_ffff0000b808e96e,&local_34);
      local_34 = 0;
      _DAT_ffff0000b811af18 = CONCAT11(DAT_ffff0000b811af19,lVar3 != 0);
      lVar3 = FUN_ffff0000b804803c(param_1,iVar2,s_enable_boot_volt_ffff0000b8096f18,&local_34);
      DAT_ffff0000b811aea3 = lVar3 != 0;
      local_34 = 0;
      lVar3 = FUN_ffff0000b804803c(param_1,iVar2,s_enable_power_measure_ffff0000b808c47d,&local_34);
      local_34 = 0;
      _DAT_ffff0000b811af18 = CONCAT11(lVar3 != 0,DAT_ffff0000b811af18);
      lVar3 = FUN_ffff0000b804803c(param_1,iVar2,s_switch_battery_side_ffff0000b80a011f,&local_34);
      DAT_ffff0000b811af24 = lVar3 != 0;
      local_34 = 0;
      puVar4 = (uint *)FUN_ffff0000b804803c(param_1,iVar2,s_cs_gpios_ffff0000b8091574,&local_34);
      if ((0 < local_34) && (puVar4 != (uint *)0x0)) {
        uVar1 = *puVar4;
        if (uVar1 != 0) {
          uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
          _DAT_ffff0000b811af20 = uVar1 >> 0x10 | uVar1 << 0x10;
        }
      }
      local_34 = 0;
      puVar4 = (uint *)FUN_ffff0000b804803c
                                 (param_1,iVar2,s_shutdown_voltage_ffff0000b8096006,&local_34);
      if (((0 < local_34) && (puVar4 != (uint *)0x0)) && (uVar1 = *puVar4, uVar1 != 0)) {
        uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
        DAT_ffff0000b811af1c = uVar1 >> 0x10 | uVar1 << 0x10;
      }
      FUN_ffff0000b804c33c
                (s__s__d___s__chroff__d_pe__d_powpa_ffff0000b808ce57,
                 s_init_cust_data_from_dt_ffff0000b809157d,0x1bd,
                 s_init_cust_data_from_dt_ffff0000b809157d,(undefined1)DAT_ffff0000b811aea0,
                 DAT_ffff0000b811aea2,DAT_ffff0000b811aea0._1_1_,_DAT_ffff0000b811aeb0 & 0xffffffff,
                 DAT_ffff0000b811aeb4,DAT_ffff0000b811aeb8);
      FUN_ffff0000b804c33c
                (s__s__d___s__usb__d_ac__d__d_nac___ffff0000b80931ab,
                 s_init_cust_data_from_dt_ffff0000b809157d,0x1c3,
                 s_init_cust_data_from_dt_ffff0000b809157d,DAT_ffff0000b811aebc,
                 _DAT_ffff0000b811aec0 & 0xffffffff,DAT_ffff0000b811aec4,
                 _DAT_ffff0000b811aed0 & 0xffffffff,DAT_ffff0000b811aed4,DAT_ffff0000b811aed8,
                 DAT_ffff0000b811aedc,DAT_ffff0000b811aee8,DAT_ffff0000b811aeec,DAT_ffff0000b811aef0
                );
    }
  }
  FUN_ffff0000b804c33c
            (s__s__d___s__done_ffff0000b808a7dd,s_charger_init_ffff0000b808f8ae,0x1df,
             s_charger_init_ffff0000b808f8ae);
  return;
}


