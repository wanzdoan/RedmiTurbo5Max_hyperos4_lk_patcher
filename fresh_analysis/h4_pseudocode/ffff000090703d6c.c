
void FUN_ffff000090703d6c(void)

{
  int iVar1;
  
  DAT_ffff00009083d048 = FUN_ffff000090740610(1,9);
  if (DAT_ffff00009083d048 == 0) {
    FUN_ffff00009077e11c
              (s__RTC__s__d___s__get_spmi_device_f_ffff0000907ef291,
               s_mt6685_rtc_init_ffff0000907ebd5d,0xde,s_mt6685_rtc_init_ffff0000907ebd5d);
  }
  FUN_ffff00009073789c(0,&DAT_ffff00009081626c);
  FUN_ffff00009073789c(1,&DAT_ffff000090816290);
  FUN_ffff00009073789c(2,&DAT_ffff0000908162b4);
  FUN_ffff00009073789c(3,&DAT_ffff000090816302);
  FUN_ffff00009073799c(&DAT_ffff000090816308,0x1f);
  FUN_ffff0000907379bc(&PTR_FUN_ffff000090816348);
  iVar1 = FUN_ffff000090722730
                    (s_mt6685_rtc_shutdown_callback_ffff0000907e2462,&LAB_ffff000090703e58,0);
  if (iVar1 != 0) {
    FUN_ffff00009077e11c
              (s__RTC__s__d___s__rtc_register_shu_ffff0000907fe8b4,
               s_mt6685_rtc_init_ffff0000907ebd5d,0xea,s_mt6685_rtc_init_ffff0000907ebd5d,iVar1);
    return;
  }
  return;
}


