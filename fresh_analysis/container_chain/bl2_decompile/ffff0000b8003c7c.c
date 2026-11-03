
void FUN_ffff0000b8003c7c(void)

{
  int iVar1;
  
  DAT_ffff0000b810c038 = FUN_ffff0000b80263f0(1,9);
  if (DAT_ffff0000b810c038 == 0) {
    FUN_ffff0000b804c33c
              (s__RTC__s__d___s__get_spmi_device_f_ffff0000b8096dbd,
               s_mt6685_rtc_init_ffff0000b809509e,0xde,s_mt6685_rtc_init_ffff0000b809509e);
  }
  FUN_ffff0000b80212d4(0,&DAT_ffff0000b80ad268);
  FUN_ffff0000b80212d4(1,&DAT_ffff0000b80ad28c);
  FUN_ffff0000b80212d4(2,&DAT_ffff0000b80ad2b0);
  FUN_ffff0000b80212d4(3,&DAT_ffff0000b80ad2fe);
  FUN_ffff0000b80213d4(&DAT_ffff0000b80ad304,0x1f);
  FUN_ffff0000b80213f4(&PTR_FUN_ffff0000b80ad348);
  iVar1 = FUN_ffff0000b8014d0c
                    (s_mt6685_rtc_shutdown_callback_ffff0000b808f7c5,&LAB_ffff0000b8003d68,0);
  if (iVar1 != 0) {
    FUN_ffff0000b804c33c
              (s__RTC__s__d___s__rtc_register_shu_ffff0000b809f15e,
               s_mt6685_rtc_init_ffff0000b809509e,0xea,s_mt6685_rtc_init_ffff0000b809509e,iVar1);
    return;
  }
  return;
}


