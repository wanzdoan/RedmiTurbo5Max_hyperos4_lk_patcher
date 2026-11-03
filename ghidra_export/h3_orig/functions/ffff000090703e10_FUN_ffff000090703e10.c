// ffff000090703e10  size=4  FUN_ffff000090703e10

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090703e10(void)

{
  int iVar1;
  
  _DAT_ffff00009083d0a8 = FUN_ffff000090740378(1,9);
  if (_DAT_ffff00009083d0a8 == 0) {
    FUN_ffff00009077de54
              (s__RTC__s__d___s__get_spmi_device_f_ffff0000907ef120,
               s_mt6685_rtc_init_ffff0000907ebbda,0xde,s_mt6685_rtc_init_ffff0000907ebbda);
  }
  FUN_ffff000090737604(0,&DAT_ffff00009081626c);
  FUN_ffff000090737604(1,&DAT_ffff000090816290);
  FUN_ffff000090737604(2,&DAT_ffff0000908162b4);
  FUN_ffff000090737604(3,&DAT_ffff000090816302);
  FUN_ffff000090737704(&DAT_ffff000090816308,0x1f);
  FUN_ffff000090737724(&PTR_FUN_ffff000090816348);
  iVar1 = FUN_ffff0000907224ec
                    (s_mt6685_rtc_shutdown_callback_ffff0000907e2391,FUN_ffff000090703efc,0);
  if (iVar1 != 0) {
    FUN_ffff00009077de54
              (s__RTC__s__d___s__rtc_register_shu_ffff0000907fe70c,
               s_mt6685_rtc_init_ffff0000907ebbda,0xea,s_mt6685_rtc_init_ffff0000907ebbda,iVar1);
    return;
  }
  return;
}


