
void FUN_ffff0000be0038cc(void)

{
  int iVar1;
  
  DAT_ffff0000be0f9018 = FUN_ffff0000be0303e8(1,9);
  if (DAT_ffff0000be0f9018 == 0) {
    FUN_ffff0000be05fd98
              (s__RTC__s__d___s__get_spmi_device_f_ffff0000be0b9662,
               s_mt6685_rtc_init_ffff0000be0b7221,0xde,s_mt6685_rtc_init_ffff0000be0b7221);
  }
  FUN_ffff0000be029ca0(0,&DAT_ffff0000be0d52b8);
  FUN_ffff0000be029ca0(1,&DAT_ffff0000be0d52dc);
  FUN_ffff0000be029ca0(2,&DAT_ffff0000be0d5300);
  FUN_ffff0000be029ca0(3,&DAT_ffff0000be0d534e);
  FUN_ffff0000be029da0(&DAT_ffff0000be0d5354,0x1f);
  FUN_ffff0000be029dc0(&PTR_FUN_ffff0000be0d5398);
  iVar1 = FUN_ffff0000be0203a4
                    (s_mt6685_rtc_shutdown_callback_ffff0000be0b07a5,&LAB_ffff0000be0039b8,0);
  if (iVar1 != 0) {
    FUN_ffff0000be05fd98
              (s__RTC__s__d___s__rtc_register_shu_ffff0000be0c3a2d,
               s_mt6685_rtc_init_ffff0000be0b7221,0xea,s_mt6685_rtc_init_ffff0000be0b7221,iVar1);
    return;
  }
  return;
}


